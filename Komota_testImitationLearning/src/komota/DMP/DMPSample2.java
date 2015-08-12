package komota.DMP;

import komota.supers.ArmFrame;
import komota.supers.Goal;
import komota.supers.Hand;

public class DMPSample2 {


	//ForceFunctionの構造を簡略化したもの。デモ用。
	//xy方向でそれぞれ独立して学習する

	//ForceFunctionが２次元データであり、更新式に使用する際にxy両方向で共有しているのがうまくいかない原因だと推測し、
	//ForceFunctionが１次元データになるように変更したもの

	//τとα_xを調整すると少しはそれっぽく動く。…けど、うまくいってるというにはまだほど遠い

	//Handをフィールドに持ち、moveでエンドエフェクタを動かす

	//basis functionはstudywolfのものを参考にしている。論文の方とはτの扱いとかが微妙に違う

	//加速度の更新式に使用する係数
	final double ALPHA 	= 0.1;
	final double BETA 	= 0.01;
	final double TAU	= 0.5;
	final double ALPHA_X= 0.01;

	//使用する教示データの時間間隔を表す定数
	final int TIMEFRAME = 6;

	//DMPを実行するかのフラグ。例えばエンドエフェクタを直接操作してるときはfalse
	public boolean dmpflag = true;

	//動かすHand
	Hand hand = null;

	//目標位置
	Goal goal = null;

	//速度と加速度
	double vx = 0;
	double vy = 0;
	double ax = 0;
	double ay = 0;

	//時間カウント
	int timecount = 0;

	//教示データ。Trajectoryから持たせるのが面倒くさいので（加えて時間間隔が開くようになるため）一度こっちにいれてしまう
	double[] teachx;
	double[] teachy;

	//教示データから算出したfの点集合。この点を通るようにfx、fyを更新する
	double[] teachfx;
	double[] teachfy;

	//ForceFunction
	public ForceFunction fx;
	public ForceFunction fy;

	//描画するArmFrame
	ArmFrame frame;

	//コンストラクタ
	public DMPSample2(ArmFrame frame, Hand hand, Goal goal){
		this.frame = frame;
		this.hand = hand;
		this.goal = goal;
//		this.fx = new ForceFunction(10,this.frame.getHand().end_effector.getXY()[0],this.frame.getGoal().getXY()[0],0);
//		this.fy = new ForceFunction(10,this.frame.getHand().end_effector.getXY()[1],this.frame.getGoal().getXY()[1],1);
	}

	//時間カウントのゲッター
	public int getTimeCount(){
		return this.timecount;
	}

	//教示データを配列に準備する
	//引数は（開始index,終了index）
	public void setTeach(int startidx,int endidx){
		//とりあえず、インデックスがトンネルしてない場合のみ実装
		if(startidx<endidx){
			this.teachx = new double[endidx-startidx+1];
			this.teachy = new double[endidx-startidx+1];
			for(int i=0;i<endidx-startidx;i++){
				this.teachx[i] = DMPSample2.this.hand.getTrajectory()[startidx+i].getXY()[0];
				this.teachy[i] = DMPSample2.this.hand.getTrajectory()[startidx+i].getXY()[1];
			}
		}else{
			this.teachx = null;
			this.teachy = null;
			return;
		}
	}

	//準備した教示データからForceFunctionの経由点集合teachfを準備する
	public void setTeachF(){
		int length = this.teachx.length;
		this.teachfx = new double[length];
		this.teachfy = new double[length];
			double[] tempy;
			double[] tempv;
			double[] tempa;
			double[] tempa_bmd;
				tempy = new double[length];
				tempv = new double[length];
				tempa = new double[length];
				tempa_bmd = new double[length];
/* ***************************************************************************************************************************** */
/* x方向の計算                                                                                                                   */
				//yの取得
				for(int i=0;i<length;i++){
					tempy[i] = teachx[i];
				}

				//yからvの計算
				double temp1 = tempy[1] - tempy[0];
				double temp2 = 0;;
				for(int i=0;i<length-1;i++){
					temp2 = tempy[i+1] - tempy[i];
					tempv[i] = (temp1+temp2)/2;
					temp1 = temp2;
				}
				tempv[length-1] = (temp1+temp2)/2;
				//vからaの計算
				temp1 = tempv[1] - tempv[0];
				for(int i=0;i<length-1;i++){
					temp2 = tempv[i+1] - tempv[i];
					tempa[i] = (temp1+temp2)/2;
					//各y,vから、バネマスダンパ的なaを算出する。それと上記で求めたaの差がFになるはず
					tempa_bmd[i] = DMPSample2.this.ALPHA * (DMPSample2.this.BETA * (tempy[length-1]-tempy[i]) - tempv[i]);
					//差分を求め、Fにいれる
					teachfx[i] = tempa[i] - tempa_bmd[i];
					temp1 = temp2;
				}
				tempa[length-1] = (temp1+temp2)/2;
				tempa_bmd[length-1] = 0;
				teachfx[length-1] = tempa[length-1] - tempa_bmd[length-1];
/*                                                                                                                               */
/* ***************************************************************************************************************************** */

/* ***************************************************************************************************************************** */
/* y方向の計算                                                                                                                   */
				//yの取得
				for(int i=0;i<length;i++){
					tempy[i] = teachy[i];
				}

				//yからvの計算
				temp1 = tempy[1] - tempy[0];
				temp2 = 0;;
				for(int i=0;i<length-1;i++){
					temp2 = tempy[i+1] - tempy[i];
					tempv[i] = (temp1+temp2)/2;
					temp1 = temp2;
				}
				tempv[length-1] = (temp1+temp2)/2;
				//vからaの計算
				temp1 = tempv[1] - tempv[0];
				for(int i=0;i<length-1;i++){
					temp2 = tempv[i+1] - tempv[i];
					tempa[i] = (temp1+temp2)/2;
					//各y,vから、バネマスダンパ的なaを算出する。それと上記で求めたaの差がFになるはず
					tempa_bmd[i] = DMPSample2.this.ALPHA * (DMPSample2.this.BETA * (tempy[length-1]-tempy[i]) - tempv[i]);
					//差分を求め、Fにいれる
					teachfy[i] = tempa[i] - tempa_bmd[i];
					temp1 = temp2;
				}
				tempa[length-1] = (temp1+temp2)/2;
				tempa_bmd[length-1] = 0;
				teachfy[length-1] = tempa[length-1] - tempa_bmd[length-1];
/*                                                                                                                               */
/* ***************************************************************************************************************************** */
	}

	//エンドエフェクタの位置の更新
	public void DMPUpdate_XY(){
		double[] prev = this.hand.end_effector.getXY();
		this.hand.locateEnd_Effector(prev[0]+vx, prev[1]+vy);
	}

	//速度の更新
	public void DMPUpdate_V(){
		this.vx+=this.ax;
		this.vy+=this.ay;
	}

	//加速度の更新。ここが主に各DMPで異なる
	public void DMPUpdate_A(){
		double temp = this.goal.getXY()[0] - this.hand.end_effector.getXY()[0];
		temp *= BETA;
		temp -= this.vx;
		temp *= ALPHA;
		this.ax = TAU*TAU*(temp + this.fx.outputF());
//		System.out.println("F0:"+this.f.outputF(getXfromTime(this.timecount))[0]+"  F1:"+this.f.outputF(getXfromTime(this.timecount))[1]);

		temp = this.goal.getXY()[1] - this.hand.end_effector.getXY()[1];
		temp *= BETA;
		temp -= this.vy;
		temp *= ALPHA;
		this.ay = TAU*TAU*(temp + this.fy.outputF());

	}

	//DMPの更新１セット。ArmFrameのmoveでこれを実行する
	public void move(){
		if(this.dmpflag == true){

			timecount++;
			DMPUpdate_A();
			DMPUpdate_V();
			DMPUpdate_XY();
		}
		else{
			timecount = 0;
		}
	}

	//ForceFunctionの重みを更新
	public void updateWeights(int start,int goal){
		setTeach(start,goal);
		setTeachF();
		this.fx = new ForceFunction(20, this.teachx[0], this.teachx[this.teachx.length-1], 0);
		this.fy = new ForceFunction(20, this.teachy[0], this.teachy[this.teachy.length-1], 1);
	}


	//ForceFunctionクラス。

	class ForceFunction{

		//構成関数の数、初期位置、目標位置
		int numberofBFs;
		double start;
		double goal;
		int XorY;

		//教示時の初期位置。これをもとにスケーリングする
		double teachstart;

		//構成関数
		Psi[] psis;

		//重み
		double[] weights;

		//コンストラクタ
		//引数は(構成関数の数, 初期位置, 目標位置, XかYか)
		ForceFunction(int numberofBFs, double start, double goal, int XorY){
			this.numberofBFs = numberofBFs;
			this.start = start;
			this.goal = goal;
			this.XorY = XorY;

			if(this.XorY == 0){
				this.teachstart = DMPSample2.this.teachx[0];
			}
			else{
				this.teachstart = DMPSample2.this.teachy[0];
			}

			this.psis = new Psi[numberofBFs];
			this.weights = new double[numberofBFs];
			for(int i=0;i<this.psis.length;i++){
				this.psis[i] = new Psi(i,this.XorY);
				if(this.XorY == 0){
					this.weights[i] = DMPSample2.this.teachfx[i];
				}
				else{
					this.weights[i] = DMPSample2.this.teachfy[i];
				}
			}
		}

		//出力関数
		double outputF(int time){
			double output = 0;

			for(int i=0;i<this.numberofBFs;i++){
				output+=this.weights[i]*this.psis[i].outputPsi(time);
			}
			output/= (time+1);
			output/= (this.goal - this.teachstart);
			output*= (this.goal - this.start);

			return output;
		}
		//出力関数
		double outputF(){
			double output;
			output = 10*Math.sin(DMPSample2.this.timecount/10) /(DMPSample2.this.timecount+1);
			return output;
		}
	}

	//構成関数クラス

	class Psi{
		//係数
		int index;
		double c;
		double h;

		//コンストラクタ
		Psi(int idx, int XorY){
			this.index = idx;
			this.c = DMPSample2.this.TIMEFRAME * this.index;
			if(XorY == 0){
				this.h = DMPSample2.this.teachfx[this.index] * DMPSample2.this.teachfx[this.index] * Math.PI;
			}
			else{
				this.h = DMPSample2.this.teachfx[this.index] * DMPSample2.this.teachfy[this.index] * Math.PI;
			}
		}

		//出力関数
		double outputPsi(int time){
			double output = 0;
			output = Math.exp(-this.h * (time - this.c) * (time - this.c));
			return output;
		}
	}








/*
	//時間カウントからXを返すメソッド
	public double getXfromTime(int time){
		double output = Math.exp(-TAU*ALPHA_X*time);
		return output;
	}


	//fの構成関数ψクラス
	//DMPSample2ではForceFunctionおよび構成関数は教示データが与えられてから、それに応じて生成される。
	class Psi {
		//係数
		int index;
		double h;
		double c;

		//コンストラクタ。
		//引数は（インデックス、生成ψ個数）
		Psi(int idx,int numberofBFs){
			this.index = idx;
			this.c = index * DMPSample2.this.TIMEFRAME;
			this.h = numberofBFs / this.c;
		}

		//インデックスのゲッター
		int getIndex(){
			return this.index;
		}

		//Xを与えるとψ（X）を返すメソッド。
		//時間を与えてはいけない。注意

		double getPsifromX(double X){
			double output = 0;
			double temp = X - this.c;
			temp = temp*temp;
			temp *= this.h;
			output = Math.exp(-temp);
			return output;
		}
	}

	//force function クラス。Sample0のものとは違い、outputFがdouble１次元になるように変更されてる
	public class ForceFunction{
		//重み配列
		double[] weights;
		//構成関数配列
		Psi[] psis;
		//エンドエフェクタの初期位置
		double startXY;
		//目標位置
		double goalXY;
		//XYどっち方向の関数であるか
		int XorY;
		//コンストラクタ
		//引数は（構成関数の数、エンドエフェクタの初期位置、目標位置、XYどっち方向か(0,1)）
		ForceFunction(int numberofPsi,double[] startXY,double[] goalXY ,int XorY){
			this.startXY = startXY[XorY];
			this.goalXY = goalXY[XorY];
			this.weights = new double[numberofPsi];
			this.psis = new Psi[numberofPsi];
			for(int i=0;i<numberofPsi;i++){

				this.weights[i] = 1;
 				this.weights[i] /= numberofPsi;

				this.psis[i] = new Psi(i,numberofPsi);
			}
			this.XorY = XorY;
		}
		//DMPの式における出力。時間ではなくXを引数に与える
		public double outputF(double X){

			double output = 0;
			double normalization = 0;
			double weightingsummation = 0;


			for(int i=0;i<this.weights.length;i++){

//				System.out.print("[" + i + "]" + " weight:"+this.weights[i] + " psi:" + this.psis[i].getPsifromX(X));

				normalization += this.psis[i].getPsifromX(X);
				weightingsummation += this.weights[i]*this.psis[i].getPsifromX(X);
			}
			output = (weightingsummation / normalization) * X * (goalXY - startXY);

			return output;
		}
		//重み更新に使用するf_dを算出するメソッド。
		//引数には（使用するTrajectoryの開始index、使用するTrajectoryの終了index）

		double[] getFdfromTrajectory(int startidx,int goalidx){

			int length = goalidx - startidx +1;
			double[] output;
			double[] tempy;
			double[] tempv;
			double[] tempa;
			if(startidx < goalidx){
				output = new double[length];
				tempy = new double[length];
				tempv = new double[length];
				tempa = new double[length];

				//yの取得
				for(int i=0;i<length;i++){
					tempy[i] = DMPSample2.this.hand.getTrajectory()[startidx+i].getXY()[this.XorY];
				}

				//yからvの計算
				double temp1 = tempy[1] - tempy[0];
				double temp2 = 0;;
				for(int i=0;i<length-1;i++){
					temp2 = tempy[i+1] - tempy[i];
					tempv[i] = (temp1+temp2)/2;
					temp1 = temp2;
				}
				tempv[length-1] = (temp1+temp2)/2;
				//vからaの計算
				temp1 = tempv[1] - tempv[0];
				for(int i=0;i<length-1;i++){
					temp2 = tempv[i+1] - tempv[i];
					tempa[i] = (temp1+temp2)/2;
					temp1 = temp2;
				}
				tempa[length-1] = (temp1+temp2)/2;
				//yとvとaからf_dの計算
				for(int i=0;i<length;i++){
					output[i] = tempa[i] - DMPSample2.this.ALPHA * (DMPSample2.this.BETA * (DMPSample2.this.goal.getXY()[this.XorY] - tempy[i]) - tempv[i]);
				}
			}
			else{
				//インデックスが一周してる場合の処理をよく考えて実装すること
				output = null;
			}
			return output;
		}

		//重み更新。
		//引数には（使用するTrajectoryの開始index、使用するTrajectoryの終了index）
		public void updateWeights(int startidx,int goalidx){
			//重み更新に必要なS,ψ,Fdを用意

			double[] Fd = getFdfromTrajectory(startidx,goalidx);
			double[] S = new double[Fd.length];
			for(int i=0;i<Fd.length;i++){
				S[i] = DMPSample2.this.getXfromTime(i)*(DMPSample2.this.goal.getXY()[this.XorY] - DMPSample2.this.hand.getTrajectory()[startidx].getXY()[this.XorY]);
			}
			//重みの更新fx
			for(int i=0;i<DMPSample2.this.fx.weights.length;i++){
				double newweight = 0;

				double temp = 0;
				for(int j=0;j<Fd.length;j++){
					temp += DMPSample2.this.fx.psis[i].getPsifromX(DMPSample2.this.getXfromTime(j)) * DMPSample2.this.getXfromTime(j) * (DMPSample2.this.goal.getXY()[this.XorY] - DMPSample2.this.hand.getTrajectory()[startidx].getXY()[this.XorY])*Fd[j];
				}
				newweight = temp;
				temp = 0;
				for(int j=0;j<Fd.length;j++){
					temp += DMPSample2.this.fx.psis[i].getPsifromX(DMPSample2.this.getXfromTime(j)) * DMPSample2.this.getXfromTime(j) * (DMPSample2.this.goal.getXY()[this.XorY] - DMPSample2.this.hand.getTrajectory()[startidx].getXY()[this.XorY]) * (DMPSample2.this.goal.getXY()[this.XorY] - DMPSample2.this.hand.getTrajectory()[startidx].getXY()[this.XorY]);
				}
				newweight /= temp;

				DMPSample2.this.fx.weights[i] = newweight;
			}
			System.out.println("[DMPSample0.ForceFunction.updateWeights]"+this.XorY+"方向ForceFunction更新完了！");
			//重みの更新fy
			for(int i=0;i<DMPSample2.this.fy.weights.length;i++){
				double newweight = 0;

				double temp = 0;
				for(int j=0;j<Fd.length;j++){
					temp += DMPSample2.this.fy.psis[i].getPsifromX(DMPSample2.this.getXfromTime(j)) * DMPSample2.this.getXfromTime(j) * (DMPSample2.this.goal.getXY()[this.XorY] - DMPSample2.this.hand.getTrajectory()[startidx].getXY()[this.XorY])*Fd[j];
				}
				newweight = temp;
				temp = 0;
				for(int j=0;j<Fd.length;j++){
					temp += DMPSample2.this.fy.psis[i].getPsifromX(DMPSample2.this.getXfromTime(j)) * DMPSample2.this.getXfromTime(j) * DMPSample2.this.getXfromTime(j) * (DMPSample2.this.goal.getXY()[this.XorY] - DMPSample2.this.hand.getTrajectory()[startidx].getXY()[this.XorY]) * (DMPSample2.this.goal.getXY()[this.XorY] - DMPSample2.this.hand.getTrajectory()[startidx].getXY()[this.XorY]);
				}
				newweight /= temp;

				DMPSample2.this.fy.weights[i] = newweight;
			}
			System.out.println("[DMPSample0.ForceFunction.updateWeights]"+(1-this.XorY)+"方向ForceFunction更新完了！");

		}
	}
*/

}
