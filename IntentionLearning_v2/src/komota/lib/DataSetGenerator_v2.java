package komota.lib;

import java.io.IOException;

import komota.main.PRv2_Mat_SOINN_v2;

public class DataSetGenerator_v2 extends DataSetGenerator{

	//grand truth
	MyMatrix grtrh = null;
	//誤差の分散
	double variance;

	public static void main(String[] args){
		DataSetGenerator_v2 frame = new DataSetGenerator_v2("2D_NbO",0.1);
	}

	//コンストラクタ
	public DataSetGenerator_v2(String cmd, double variance){
		this.setGrandTruth(cmd);
		this.variance = variance;
	}

	public void setGrandTruth(String cmd){
		MyIO io = new MyIO();
		io.readFile("generator_grtrh/grtrh_"+cmd+".txt");
		this.grtrh = io.readMatrix(666);
		io.close();
	}
	//汎化誤差を求める際に，Grand truth を利用するときに使うゲッター
	public MyMatrix getGrandTruth(){
		return this.grtrh;
	}

	public MyMatrix teach(){
		//grand truth と誤差によって現環境からの目標状態に動かす
		//適切な（環境範囲内にすべて収まる）結果でない場合は null を返す
		MyMatrix next;
		MyMatrix current = this.getStatusforMatrix();
		next = grtrh.mult(current);
		//誤差を付与する
		int num = this.countNumberofObject();
		for(int i=1;i<2*num+1;i++){
			next.setData(i, 0, next.getData(i,0)+this.nextError(variance));
			if(next.getData(i,0) < 0 || next.getData(i,0) >= Statics.NUMBEROFPANEL){
				System.out.println("戦犯:"+i+","+next.getData(i, 0));
				next = null;
				break;
			}
		}
		return next;
	}

	//卒論結果との比較実験をやる際に同条件（赤の位置のみにガウス誤差）を作るためのジェネレーター
	public MyMatrix teach_prev(){
		//grand truth と誤差によって現環境からの目標状態に動かす
		//適切な（環境範囲内にすべて収まる）結果でない場合は null を返す
		MyMatrix next;
		MyMatrix current = this.getStatusforMatrix();
		next = grtrh.mult(current);
		//誤差を付与する

		next.setData(1, 0, next.getData(1,0)+this.nextError(variance));
		next.setData(2, 0, next.getData(2,0)+this.nextError(variance));

		return next;
	}

	//引数で与えたstartログとgoalログが，このジェネレータから生成されたデータとして正しければ true を返す
	//とりあえず分散はゼロで動かすこと前提，閾値は手打ち
	public boolean isTrueLog(double[] start,double[] goal){
		double[] exstart = new double[this.grtrh.dimension];
		double[] exgoal = new double[this.grtrh.dimension];
		for(int i=0;i<start.length;i++){
			exstart[i] = start[i];
			exgoal[i] = goal[i];
		}
		double[] temp = this.grtrh.multwithVec(exstart);
		double error = 0;
		for(int i=0;i<temp.length;i++){
			error += Math.abs(temp[i] - exgoal[i]);
		}
		if(error < 1.0000){
			return true;
		}
		else{
			return false;
		}
	}

	@Override
	public void functionPlugin1(){
		System.out.println("データジェネレート2！");
		int count = 0;
		while(count < this.numberofdataset){
			this.initialize();
			MyMatrix next = this.teach();
			if(next != null){
				this.setStatusforMatrix(next);
				this.pushSPACE();
				this.pushGoal();
				count++;
			}
			else{
				System.out.println("だめだ！");
			}
		}
		System.out.println("データジェネレート完了！");
		try {
			System.out.println("logdata.txtには"+this.countGoal("logdata.txt")+"個のデータが存在します");
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		this.getMyIO().close();

	}
	@Override
	public void functionPlugin2(){
		System.out.println("データジェネレート2_prev！");
		int count = 0;
		while(count < this.numberofdataset){
			this.initialize();
			MyMatrix next = this.teach_prev();
			if(next != null){
				this.setStatusforMatrix(next);
				this.pushSPACE();
				this.pushGoal();
				count++;
			}
			else{
				System.out.println("だめだ！");
			}
		}
		System.out.println("データジェネレート完了！");
		try {
			System.out.println("logdata.txtには"+this.countGoal("logdata.txt")+"個のデータが存在します");
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		this.getMyIO().close();
	}
	@Override
	public void functionPlugin3(){
		//startログから，grand truth を用いて作成される int にならされてないログデータを作る
		PRv2_Mat_SOINN_v2 pr = new PRv2_Mat_SOINN_v2(5,"logdata.txt");
		MyIO io = new MyIO();
		io.writeFile("20160627/generate_func3.txt");
		for(int i=0;i<pr.getNumberofLog();i++){
			double[] exstart = new double[this.grtrh.dimension];
			double[] exgoal = new double[this.grtrh.dimension];
			for(int j=0;j<pr.getStartLog(i).length;j++){
				exstart[j] = pr.getStartLog(i)[j];
			}
			exgoal = this.grtrh.multwithVec(exstart);
			io.print("start ");
			for(int j=0;j<exstart.length;j++){
				io.print(","+exstart[j]);
			}
			io.println();
			io.print("goal  ");
			for(int j=0;j<exstart.length;j++){
				io.print(","+exgoal[j]);
			}
			io.println();
		}
		io.execute();
		io.close();
		System.out.println("おっけー");
	}
}
