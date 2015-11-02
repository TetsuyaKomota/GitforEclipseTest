package komota.pr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import komota.main.MainFrame;

public class MyPR {

	//ログデータに基づく機械学習を行うテンプレ。これを継承する。

	/*
	 * 機能一覧
	 * 	・選択したログデータを配列などに取得できる
	 * 	・
	 */

	//定数
	//ログデータの種類を表すときに使う種類定数
	public static final int START = 0;
	public static final int GOAL = 1;
	public static final int CHANGE = 2;
	public static final int STATUS = 3;

	//フィールド
	File file;
	BufferedReader br;
	String file_name = "test4.txt";
	//取得したログデータ。[ステップ数（何行目か）][種類（**start,**goal,  change,status）][行][列]
	//種類が  changeの場合、変化前後のパネルの前を１、後を２とする
	public StepLog[] logdata = null;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ

		MyPR test = new MyPR();
		test.show();
	}


	//コンストラクタ
	public MyPR(){
		this.file = new File("log/"+file_name);
		try {
			this.br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		this.logdata = new StepLog[1000];
		int step = 0;
		while(true){
			String line = null;
			try {
				line = br.readLine();
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
			if(line == null){
				break;
			}
			else{
				this.logdata[step] = new StepLog(step,line);
				step++;
			}
		}
	}

	//クローズ処理
	public void close(){
		try {
			this.br.close();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}
	//表示
	public void show(){
		for(int i=0;i<this.logdata.length;i++){
			if(this.logdata[i] != null){
				this.logdata[i].show();
			}
			else{
				break;
			}
		}
	}

	/* ************************************************************************************************** */
	//解析用のログデータを表す内部クラス
	protected class StepLog{
		//フィールド
		//ステップ数
		int step = -1;
		//種類
		int type = -1;
		//状態配列
		int[][] statuses = null;

		//コンストラクタ
		StepLog(int step , String line){
			this.step = step;
			String[] tempstrings = line.split(",");
			if(tempstrings[0].equals("start ")){
				this.type = START;
			}
			else if(tempstrings[0].equals("goal  ")){
				this.type = GOAL;
			}
			else if(tempstrings[0].equals("change")){
				this.type = CHANGE;
			}
			else if(tempstrings[0].equals("status")){
				this.type = STATUS;
			}
			this.statuses = new int[MainFrame.NUMBEROFPANEL][MainFrame.NUMBEROFPANEL];
			for(int i=0;i<MainFrame.NUMBEROFPANEL;i++){
				for(int j=0;j<MainFrame.NUMBEROFPANEL;j++){
					statuses[i][j] = Integer.parseInt(tempstrings[i * MainFrame.NUMBEROFPANEL + j + 1]);
				}
			}
		}
		//ゲッター
		public int getStepStatus(int gyou,int retsu){
			return this.statuses[gyou][retsu];
		}
		public int[][] getStepStatusField(){
			return this.statuses;
		}
		public int getType(){
			return this.type;
		}

		//表示
		void show(){
			String typename = null;
			switch(this.type){
			case START:
				typename = "start ,";
				break;
			case GOAL:
				typename = "goal  ,";
				break;
			case CHANGE:
				typename = "change,";
				break;
			case STATUS:
				typename = "status,";
			}
			System.out.print(typename);
			for(int i=0;i<MainFrame.NUMBEROFPANEL*MainFrame.NUMBEROFPANEL;i++){
				System.out.print(statuses[i/MainFrame.NUMBEROFPANEL][i%MainFrame.NUMBEROFPANEL]+ " ");
			}
			System.out.println("");
		}
	}

	/* ************************************************************************************************** */

}
