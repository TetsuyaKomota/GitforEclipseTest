package komota.DMP;

import komota.supers.ArmFrame;
import komota.supers.Goal;
import komota.supers.Hand;

public class DMPSample0 {

	//DMPの、force function=0とした動作のサンプル。
	//Handをフィールドに持ち、moveでエンドエフェクタを動かす

	//basis functionはstudywolfのものを参考にしている。論文の方とはτの扱いとかが微妙に違う

	//加速度の更新式に使用する係数
	final double ALPHA 	= 0.5;
	final double BETA 	= 0.05;
	final double TAU	= 1;
	final double ALPHA_X= 0.00001;

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

	//ForceFunction
	public ForceFunction f;

	//描画するArmFrame
	ArmFrame frame;

	//コンストラクタ
	public DMPSample0(ArmFrame frame, Hand hand, Goal goal){
		this.frame = frame;
		this.hand = hand;
		this.goal = goal;
		this.f = new ForceFunction(2,this.frame.getHand().end_effector.getXY(),this.frame.getGoal().getXY());
	}

	//時間カウントのゲッター
	public int getTimeCount(){
		return this.timecount;
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
		this.ax = TAU*TAU*(temp + this.f.outputF(getXfromTime(this.timecount))[0]);
//		System.out.println("F0:"+this.f.outputF(getXfromTime(this.timecount))[0]+"  F1:"+this.f.outputF(getXfromTime(this.timecount))[1]);

		temp = this.goal.getXY()[1] - this.hand.end_effector.getXY()[1];
		temp *= BETA;
		temp -= this.vy;
		temp *= ALPHA;
		this.ay = TAU*TAU*(temp + this.f.outputF(getXfromTime(this.timecount))[1]);

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

	//時間カウントからXを返すメソッド
	public double getXfromTime(int time){
		double output = Math.exp(-TAU*ALPHA_X*time);
		return output;
	}



	//fの構成関数ψクラス
	class Psi {
		//係数
		int index;
		double h;
		double c;

		//コンストラクタ。
		//引数は（インデックス、生成ψ個数）
		Psi(int idx,int numberofBFs){
			this.index = idx;
			this.c = Math.exp(-1*DMPSample0.this.TAU*this.index/numberofBFs);
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

	//force function クラス
	public class ForceFunction{
		//重み配列
		double[] weights;
		//構成関数配列
		Psi[] psis;
		//エンドエフェクタの初期位置
		double[] startXY;
		//目標位置
		double[] goalXY;
		//コンストラクタ
		//引数は（構成関数の数、エンドエフェクタの初期位置、目標位置）
		ForceFunction(int numberofPsi,double[] startXY,double[] goalXY){
			this.startXY = startXY;
			this.goalXY = goalXY;
			this.weights = new double[numberofPsi];
			this.psis = new Psi[numberofPsi];
			for(int i=0;i<numberofPsi;i++){

				this.weights[i] = 1;
 				this.weights[i] /= numberofPsi;

				this.psis[i] = new Psi(i,numberofPsi);
			}
		}
		//DMPの式における出力。時間ではなくXを引数に与える
		public double[] outputF(double X){

			double output[] = new double[2];
			output[0] = 0;
			output[1] = 0;
			double normalization = 0;
			double weightingsummation = 0;


			for(int i=0;i<this.weights.length;i++){

//				System.out.print("[" + i + "]" + " weight:"+this.weights[i] + " psi:" + this.psis[i].getPsifromX(X));

				normalization += this.psis[i].getPsifromX(X);
				weightingsummation += this.weights[i]*this.psis[i].getPsifromX(X);
			}
			output[0] = (weightingsummation / normalization) * X * (goalXY[0] - startXY[0]);
			output[1] = (weightingsummation / normalization) * X * (goalXY[1] - startXY[1]);

			return output;
		}
		//重み更新に使用するf_dを算出するメソッド。
		//引数には（使用するTrajectoryの開始index、使用するTrajectoryの終了index）

		double[][] getFdfromTrajectory(int startidx,int goalidx){
			int length = goalidx - startidx +1;
			double[][] output;
			double[][] tempy;
			double[][] tempv;
			double[][] tempa;
			if(startidx < goalidx){
				output = new double[length][];
				tempy = new double[length][];
				tempv = new double[length][];
				tempa = new double[length][];

				//yの取得
				for(int i=0;i<length;i++){
					tempy[i] = DMPSample0.this.hand.getTrajectory()[startidx+i].getXY();
				}

				//yからvの計算
				double[] temp1 = new double[2];
				temp1[0] = tempy[0][0] - tempy[1][0];
				temp1[1] = tempy[0][1] - tempy[1][1];
				double[] temp2 = new double[2];
				for(int i=0;i<length-1;i++){
					temp2[0] = tempy[i+1][0] - tempy[i][0];
					temp2[1] = tempy[i+1][1] - tempy[i][1];
					tempv[i] = new double[2];
					tempv[i][0] = (temp1[0]+temp2[0])/2;
					tempv[i][1] = (temp1[1]+temp2[1])/2;
					temp1 = temp2;
				}
				tempv[length-1] = new double[2];
				tempv[length-1][0] = (temp1[0]+temp2[0])/2;
				tempv[length-1][1] = (temp1[1]+temp2[1])/2;
				//vからaの計算
				temp1[0] = tempv[0][0] - tempv[1][0];
				temp1[1] = tempv[0][1] - tempv[1][1];
				for(int i=0;i<length-1;i++){
					temp2[0] = tempv[i+1][0] - tempv[i][0];
					temp2[1] = tempv[i+1][1] - tempv[i][1];
					tempa[i] = new double[2];
					tempa[i][0] = (temp1[0]+temp2[0])/2;
					tempa[i][1] = (temp1[1]+temp2[1])/2;
					temp1 = temp2;
				}
				tempa[length-1] = new double[2];
				tempa[length-1][0] = (temp1[0]+temp2[0])/2;
				tempa[length-1][1] = (temp1[1]+temp2[1])/2;
				//yとvとaからf_dの計算
				for(int i=0;i<length;i++){
					output[i] = new double[2];
					output[i][0] = tempa[i][0] - DMPSample0.this.ALPHA * (DMPSample0.this.BETA * (DMPSample0.this.goal.getXY()[0] - tempy[i][0]) - tempv[i][0]);
					output[i][1] = tempa[i][1] - DMPSample0.this.ALPHA * (DMPSample0.this.BETA * (DMPSample0.this.goal.getXY()[1] - tempy[i][1]) - tempv[i][1]);
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
			double[][] Fd = getFdfromTrajectory(startidx,goalidx);
			double[][] S = new double[Fd.length][];
			for(int i=0;i<Fd.length;i++){
				S[i] = new double[2];
				S[i][0] = DMPSample0.this.getXfromTime(i)*(DMPSample0.this.goal.getXY()[0] - DMPSample0.this.hand.getTrajectory()[startidx].getXY()[0]);
				S[i][1] = DMPSample0.this.getXfromTime(i)*(DMPSample0.this.goal.getXY()[1] - DMPSample0.this.hand.getTrajectory()[startidx].getXY()[1]);
			}
			//重みの更新
			for(int i=0;i<DMPSample0.this.f.weights.length;i++){
				double newweight = 0;

				double temp = 0;
				for(int j=0;j<Fd.length;j++){
					temp += DMPSample0.this.f.psis[i].getPsifromX(DMPSample0.this.getXfromTime(j)) * DMPSample0.this.getXfromTime(j) * (DMPSample0.this.goal.getXY()[0] - DMPSample0.this.hand.getTrajectory()[startidx].getXY()[0])*Fd[j][0];
					temp += DMPSample0.this.f.psis[i].getPsifromX(DMPSample0.this.getXfromTime(j)) * DMPSample0.this.getXfromTime(j) * (DMPSample0.this.goal.getXY()[1] - DMPSample0.this.hand.getTrajectory()[startidx].getXY()[1])*Fd[j][1];
				}
				newweight = temp;
				temp = 0;
				for(int j=0;j<Fd.length;j++){
					temp += DMPSample0.this.f.psis[i].getPsifromX(DMPSample0.this.getXfromTime(j)) * DMPSample0.this.getXfromTime(j) * (DMPSample0.this.goal.getXY()[0] - DMPSample0.this.hand.getTrajectory()[startidx].getXY()[0]) * (DMPSample0.this.goal.getXY()[0] - DMPSample0.this.hand.getTrajectory()[startidx].getXY()[0]);
					temp += DMPSample0.this.f.psis[i].getPsifromX(DMPSample0.this.getXfromTime(j)) * DMPSample0.this.getXfromTime(j) * (DMPSample0.this.goal.getXY()[1] - DMPSample0.this.hand.getTrajectory()[startidx].getXY()[1]) * (DMPSample0.this.goal.getXY()[1] - DMPSample0.this.hand.getTrajectory()[startidx].getXY()[1]);
				}
				newweight /= temp;

				DMPSample0.this.f.weights[i] = newweight;
			}
			System.out.println("[DMPSample0.ForceFunction.updateWeights]更新完了！");
		}
	}

	//動作確認用の、学習しないForceFunction
	class TestForceFunction extends ForceFunction{

		TestForceFunction(int numberofPsi, double[] startXY, double[] goalXY) {
			super(numberofPsi, startXY, goalXY);
			// TODO 自動生成されたコンストラクター・スタブ
		}
		@Override
		public double[] outputF(double X){
			double[] output = new double[2];
			output[0] = 70*DMPSample0.this.getXfromTime(timecount);
			output[1] = 70*DMPSample0.this.getXfromTime(timecount);
			return output;
		}

	}
}
