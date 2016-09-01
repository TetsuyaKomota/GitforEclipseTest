package komota.supers;

import komota.lib.MyIO;
import komota.lib.MyMatrix;
import komota.lib.Statics;

public abstract class MyPR_v3 {

	//特徴量ベースで書かれた新しいログフォーマットに対応したPRクラス
	//ログデータはインスタンス生成時点で前後状態対で保持する

	//フィールド
	//空間中のオブジェクト数は v3 では考慮しない． v3 は完全に特徴量ベースのみで考える
	//v3 では，存在しないオブジェクトの特徴量を考慮しない．それは MyFrame 内であらかじめ削除されていることを前提とする
	//int numofobjects;

	//特徴量数
	int numoffeatures;

	//ログデータ
	StepLog[] logdata;

	//ログデータ数
	int numberoflog;

	//入出力クラス
	MyIO io;

	//学習結果
	private MyMatrix X;

	//コンストラクタ
	public MyPR_v3(String filename){

		this.logdata = new StepLog[Statics.MAX_NUMBEROFLOG];
		this.numberoflog = 0;
		this.io = new MyIO();

		this.io.readFile(filename);

		String templine = null;
		String tempstart = null;

		while(true){
			templine = this.io.readLine();
			if(templine == null){
				break;
			}
			if(templine.split(",")[0].equals("start ") == true){
				tempstart = templine;
			}
			else if(templine.split(",")[0].equals("goal  ") == true){
				this.logdata[this.numberoflog] = new StepLog(this.numberoflog,tempstart,templine);
				this.numberoflog++;
			}
		}
		this.numoffeatures = this.getStartLog(0).length;
	}

	//ゲッター，セッター
	public double[][] getLogData(int stepID){
		double[][] output = new double[2][];

		output[0] = this.logdata[stepID].startlog;
		output[1] = this.logdata[stepID].goallog;

		return output;
	}
	public double[] getStartLog(int stepID){
		return this.logdata[stepID].startlog;
	}
	public double[] getGoalLog(int stepID){
		return this.logdata[stepID].goallog;
	}
	public int getNumberofFeatures(){
		return this.numoffeatures;
	}
	public int getNumberofLog(){
		return this.numberoflog;
	}
	public void setX(MyMatrix input){
		this.X = input;
	}
	public MyMatrix getX(){
		return this.X;
	}


	//学習機構。オーバーライドする
	public abstract void learnfromLog();
	//学習結果リセット。オーバーライドする
	public abstract void initialize();

	//再現．単純な行列計算で実現
	public void reproduction(MyFrame frame){
		frame.setStatusforMatrix(this.getX().mult(frame.getStatusforMatrix()));
	}


	//ログデータと渡した行列による推定結果との誤差を出力
	public double calcE(){
		double output = 0;

		for(int t=0;t<this.getNumberofLog();t++){
			double temp1 = 0;
			for(int i=0;i<this.X.getDimension();i++){
				double temp2 = 0;
				for(int j=0;j<this.X.getDimension();j++){

						temp2 += this.X.getData(i, j) * this.getStartLog(t)[j];

				}

					temp2 -= this.getGoalLog(t)[i];

				temp1 += temp2*temp2;
			}
			output += Math.sqrt(temp1);
		}
		output /= this.getNumberofLog();
		return output;
	}

	//ログデータを表す内部クラス
	class StepLog{
		//フィールド
		//ログID
		int step;
		//初期状態
		double[] startlog;
		//目標状態
		double[] goallog;

		//コンストラクタ
		StepLog(int id,String start,String goal){
			this.step = id;

			int length = start.split(",").length - 1;

			this.startlog = new double[length];
			this.goallog = new double[length];

			//初期状態の獲得
			String[] temp = start.split(",");
			for(int i=0;i<startlog.length;i++){
				startlog[i] = Double.parseDouble(temp[i+1]);
			}
			//目標状態の獲得
			temp = goal.split(",");
			for(int i=0;i<goallog.length;i++){
				goallog[i] = Double.parseDouble(temp[i+1]);
			}
		}
	}

}
