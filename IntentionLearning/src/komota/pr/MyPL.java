package komota.pr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import komota.main.MainFrame;

public class MyPL {

	//ログデータに基づく機械学習を行うテンプレ。これを継承する。

	/*
	 * 機能一覧
	 * 	・選択したログデータを配列などに取得できる
	 * 	・
	 */

	//定数
	//絶対移動の閾値
	final double THRESHOLD = 0.9;
	//絶対移動ベクトルの学習率
	final double RATE = 0.3;
	//ログデータの種類を表すときに使う種類定数
	public static final int START = 0;
	public static final int GOAL = 1;
	public static final int CHANGE = 2;
	public static final int STATUS = 3;

	//フィールド
	File file;
	BufferedReader br;
	String file_name = "test.txt";
	//取得したログデータ。[ステップ数（何行目か）][種類（**start,**goal,  change,status）][行][列]
	//種類が  changeの場合、変化前後のパネルの前を１、後を２とする
	StepLog[] logdata = null;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ

		MyPL test = new MyPL();
		test.show();
	}


	//コンストラクタ
	public MyPL(){
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
				inputLogStep(step,line);
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
	//デバッグ用。出力テスト
	public void show(){
		for(int i=0;i<this.logdata.length;i++){
			this.logdata[i].show();
		}
	}

	//データをもとに、絶対移動する方向を推測する。
	/*
	 * テスト用なので、前提として
	 * 1. 赤を動かすことを知っている
	 * 2. 絶対移動であることを知っている
	 * 3. ３マス平方の離散空間である
	 */
	public String[] inputLogStep(int step,String input){

		String regex_start = "\\*\\*start:";
		String regex_goal = "\\*\\*goal:";
		String regex_change = "  change:";
		String regex_status = "status:";

		Pattern pt_start = Pattern.compile(regex_start);
		Pattern pt_goal = Pattern.compile(regex_goal);
		Pattern pt_change = Pattern.compile(regex_change);
		Pattern pt_status = Pattern.compile(regex_status);

		Matcher m_start = pt_start.matcher(input);
		Matcher m_goal = pt_goal.matcher(input);
		Matcher m_change = pt_change.matcher(input);
		Matcher m_status = pt_status.matcher(input);

		if(m_start.find()){
			int[] inputs = new int[MainFrame.NUMBEROFPANEL];
			for(int i=0;i<inputs.length;i++){
				inputs[i] = Integer.parseInt(input.split(regex_start)[1].split(",")[i]);
			}
			this.logdata[step] = new StepLog(step,START,inputs);
		}
		else if(m_goal.find()){
			int[] inputs = new int[MainFrame.NUMBEROFPANEL];
			for(int i=0;i<inputs.length;i++){
				inputs[i] = Integer.parseInt(input.split(regex_goal)[1].split(",")[i]);
			}
			this.logdata[step] = new StepLog(step,GOAL,inputs);
		}
		else if(m_change.find()){
			int[] inputs = new int[MainFrame.NUMBEROFPANEL];
			for(int i=0;i<inputs.length;i++){
				inputs[i] = 0;
			}
			this.logdata[step] = new StepLog(step,CHANGE,inputs);
		}
		else if(m_status.find()){
			int[] inputs = new int[MainFrame.NUMBEROFPANEL];
			for(int i=0;i<inputs.length;i++){
				inputs[i] = Integer.parseInt(input.split(regex_status)[1].split(",")[i]);
			}
			this.logdata[step] = new StepLog(step,STATUS,inputs);
		}

		return null;
	}

	/* ************************************************************************************************** */
	//解析用のログデータを表す内部クラス
	class StepLog{
		//フィールド
		//ステップ数
		int step = -1;
		//種類
		int type = -1;
		//状態配列
		int[][] statuses = null;

		//コンストラクタ
		StepLog(int step , int type , int[] Stepdata){
			this.step = step;
			this.type = type;
			this.statuses = new int[MainFrame.NUMBEROFPANEL][MainFrame.NUMBEROFPANEL];
			for(int i=0;i<MainFrame.NUMBEROFPANEL;i++){
				for(int j=0;j<MainFrame.NUMBEROFPANEL;j++){
					this.statuses[i][j] = Stepdata[i*MainFrame.NUMBEROFPANEL + j];
				}
			}
		}

		//表示
		void show(){
			String typename = null;
			switch(this.type){
			case START:
				typename = "\\*\\*start:";
			case GOAL:
				typename = "\\*\\*goal:";
			case CHANGE:
				typename = "  change";
			case STATUS:
				typename = "status:";
			}
			System.out.print(typename);
			for(int i=0;i<MainFrame.NUMBEROFPANEL;i++){
				System.out.print(statuses[i/MainFrame.NUMBEROFPANEL][i%MainFrame.NUMBEROFPANEL]+",");
			}
			System.out.println("");
		}
	}

	/* ************************************************************************************************** */

}
