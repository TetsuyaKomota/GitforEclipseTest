package komota.main;

import komota.lib.MyIO;

public class MyPR_v2 {

	//特徴量ベースで書かれた新しいログフォーマットに対応したPRクラス
	//ログデータはインスタンス生成時点で前後状態対で保持する

	//フィールド
	//空間中のオブジェクト数
	int numofobjects;

	//ログデータ
	StepLog[] logdata;

	//入出力クラス
	MyIO io;

	//コンストラクタ
	public MyPR_v2(int numofobjects,String filename){

		this.numofobjects = numofobjects;
		this.logdata = new StepLog[Statics.MAX_NUMBEROFLOG];
		this.io = new MyIO();

		this.io.readFile(filename);

		String templine = null;
		String tempstart = null;
		int count = 0;

		while(true){
			templine = this.io.readLine();
			if(templine == null){
				break;
			}
			if(templine.split(",")[0].equals("start ") == true){
				tempstart = templine;
			}
			else if(templine.split(",")[0].equals("goal  ") == true){
				this.logdata[count] = new StepLog(count,tempstart,templine);
				count++;
			}
		}
	}

	//表示．デバッグ用
	public void show(){
		int idx = 0;
		while(true){
			if(this.logdata[idx] == null){
				break;
			}
			System.out.println(idx+"番目のデータの中身は");
			System.out.print("初期状態が");
			for(int i=0;i<2*this.numofobjects+1;i++){
				System.out.print(","+this.logdata[idx].startlog[i]);
			}
			System.out.println();
			System.out.print("目標状態が");
			for(int i=0;i<2*this.numofobjects+1;i++){
				System.out.print(","+this.logdata[idx].goallog[i]);
			}
			System.out.println("です");
			idx++;
		}
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
			this.startlog = new double[2 * MyPR_v2.this.numofobjects + 1];
			this.goallog = new double[2 * MyPR_v2.this.numofobjects + 1];

			//初期状態の獲得
			String[] temp = start.split(",");
			for(int i=0;i<startlog.length;i++){
				startlog[i] = Double.parseDouble(temp[i+1]);
			}
			//目標状態の獲得
			temp = goal.split(",");
			for(int i=0;i<startlog.length;i++){
				goallog[i] = Double.parseDouble(temp[i+1]);
			}
		}
	}

}
