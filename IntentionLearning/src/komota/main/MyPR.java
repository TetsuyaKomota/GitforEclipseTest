package komota.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import komota.coordinate.Coordinate_ID;
import komota.coordinate.MyCoordinate;



public class MyPR {

	//ログデータに基づく機械学習を行うテンプレ。これを継承する。

	//定数
	//ログデータの種類を表すときに使う種類定数
	public static final int START = 0;
	public static final int GOAL = 1;
	public static final int CHANGE = 2;
	public static final int STATUS = 3;
	public static final int FEATURE = 4;

	//スタティックフィールド
	//クロスバリデーションにおける使用するデータ量
	private static int numberofevaluation = 1000;

	//フィールド
	File file;
	BufferedReader br;
	String file_name = "logdata.txt";
	//取得したログデータ
	public StepLog[] logdata = null;

	//座標変換クラス
	public MyCoordinate coordinate = new Coordinate_ID();

	//オブジェクト数
	public int numref;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ

		MyPR test = new MyPR();
		test.show();
	}


	//コンストラクタ
	public MyPR(String filename){
		this.file_name = filename;
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
			//ログデータ中に書きだされたコメントや実行結果などを無視する
			else if(line.split(",")[0].equals("result") == true){
			}
			/*
			else if(line.split(",")[0].equals("featur") == true){
			}
			*/
			//ログデータが壊れている部分は無視する
			/*
			else if(line.split(",")[0].equals("featur") == false && line.split(",").length != MyFrame.NUMBEROFPANEL*MyFrame.NUMBEROFPANEL + 1){
			}
			*/
			else{
				this.logdata[step] = new StepLog(step,line);
				step++;
			}
		}
		//StepLogのコンストラクタで壊れたデータは受け付けないように変更したため、ここでの前処理は不要になった。
		//this.logdata[step-1] = null;
		this.close();

		this.numref = 0;
	}
	public MyPR(){
		this("logdata.txt");
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
	//評価に使用するデータ量のセッターとゲッター
	public static void setNumberofEvaluation(int num){
		MyPR.numberofevaluation = num;
	}
	public static int getNumberofEvaluation(){
		return MyPR.numberofevaluation;
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
	//盤面を、引数で与えたログデータのものに変更する
	public void arrangeField(MyFrame frame, StepLog log){
		for(int i=0;i<MyFrame.NUMBEROFPANEL;i++){
			for(int j=0;j<MyFrame.NUMBEROFPANEL;j++){
				frame.panels[i][j].setStatus(log.statuses[i][j]);
			}
		}
	}
	//学習機構。オーバーライドする
	public void learnfromLog(){
	}
	//再現。オーバーライドする
	public void reproduction(MyFrame frame){
	}
	//参照点の学習結果リセット。オーバーライドする
	public void initialize(){
	}

	//クロスバリデーションを行って学習結果の評価を行う
	public double evaluate(MyFrame frame,boolean initialize){
		double output = 0;
		//交差回数
		int count = 0;

		int t=0;
		int T=0;
		for(t=0;t<this.logdata.length;t++){
			//ログ読み切ったら終了
			//交差回数が使用データ量を上回った場合も終了
			if(this.logdata[t] == null || count > MyPR.numberofevaluation){
				break;
			}
			//"goal"データが来るまで回す
			if(this.logdata[t].getType() != GOAL){
			}
			else{
				//直前の"start"データが来るまで戻る
				T = t;
				while(this.logdata[T].getType() != START){
					T--;
				}
				//logdata[t]のタイプを一時的にstatusに変換し、学習が行われないようにする
				logdata[t].setType(STATUS);
				//学習結果を一度消去
				initialize();
				//学習を行う
				learnfromLog();
				//logdata[t]のタイプをgoalに戻す
				logdata[t].setType(GOAL);
				//盤面をテストケースに合わせる
				arrangeField(frame,logdata[T]);
				//再現させる
				reproduction(frame);
				//再現結果と正解の差を求める
				//logdata[t]のトラジェクタの位置を得る
				int[] truepoint = new int[2];
				for(int i=0;i<MyFrame.NUMBEROFPANEL;i++){
					for(int j=0;j<MyFrame.NUMBEROFPANEL;j++){
						if(logdata[t].statuses[i][j] == 1){
							truepoint[0] = i;
							truepoint[1] = j;
							break;
						}
					}
				}
				//再現したsecondselectedとのずれを求める
				double diserror = Math.sqrt((frame.secondselected[0]-truepoint[0])*(frame.secondselected[0]-truepoint[0]) + (frame.secondselected[1]-truepoint[1])*(frame.secondselected[1]-truepoint[1]));
				System.out.println("step:"+t+"  true:"+truepoint[0]+","+truepoint[1]+" select:"+frame.secondselected[0]+","+frame.secondselected[1]+" error:"+diserror);
				//outputの更新。outputはdiserrorの平均値にする
				System.out.println("[MyPR]evaluate:count:"+count);
				output = ((double)count/(count+1))*output + ((double)1/(count+1))*diserror;
				count++;
			}
		}
		//計算結果をresult.txtに出力する
  		frame.pw.println(
				"result,"+output
				);
		//盤面をいじくってしまっているので、最後にinitializeを実行する
  		//条件を変えながら評価値を何度も計算する場合、initializeによるstartログ発生が煩わしいため、引数でinitializeを行わないようにできる
		if(initialize == true){
			frame.initialize();
		}
 		return output;
	}
	public double evaluate(MyFrame frame){
		return evaluate(frame,true);
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
		//特徴量配列
		double[][] features = null;


		//コンストラクタ
		StepLog(int step , String line){
			this.step = step;
			String[] tempstrings = line.split(",");
			if(tempstrings.length < MyFrame.NUMBEROFPANEL*MyFrame.NUMBEROFPANEL){
				if(tempstrings[0].equals("featur") == true){
					System.out.println("いえぇーい！");
					this.type = FEATURE;
				}
				else{
					return;
				}
			}
			else if(tempstrings[0].equals("start ")){
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
			if(this.type != FEATURE){
				this.statuses = new int[MyFrame.NUMBEROFPANEL][MyFrame.NUMBEROFPANEL];
				for(int i=0;i<MyFrame.NUMBEROFPANEL;i++){
					for(int j=0;j<MyFrame.NUMBEROFPANEL;j++){
						statuses[i][j] = Integer.parseInt(tempstrings[i * MyFrame.NUMBEROFPANEL + j + 1]);
					}
				}
			}
			else{
				this.features = new double[MyPR.this.numref][MyPanel.NUMBEROFFEATURE];

				int idx = 0;
				int tempref = -1;
				int tempfeature = 0;
				while(true){
					//データ読み終わりまでwhile
					if(idx >= tempstrings.length){
						break;
					}
					//ログタイプとパーティションは無視
					else if(tempstrings[idx].equals("featur") == true){
					}
					else if(tempstrings[idx].equals("***") == true){
						tempref = -1;
						tempfeature = 0;
					}
					//tempref を設定
					else if(tempref == -1){
						tempref = Integer.parseInt(tempstrings[idx]);
					}
					//特徴量を格納
					else{
						this.features[tempref][tempfeature] = Double.parseDouble(tempstrings[idx]);
						tempfeature++;
					}
					idx++;
				}

			}
		}
		//セッター、ゲッター
		public int getStepStatus(int gyou,int retsu){
			return this.statuses[gyou][retsu];
		}
		public int[][] getStepStatusField(){
			return this.statuses;
		}
		public void setType(int type){
			this.type = type;
		}
		public int getType(){
			return this.type;
		}
		public double getStepFeature(int status,int feature){
			return this.features[status][feature];
		}
		public double[][] getStepFeaturesField(){
			return this.features;
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

			for(int i=0;i<MyFrame.NUMBEROFPANEL*MyFrame.NUMBEROFPANEL;i++){
				System.out.print(statuses[i/MyFrame.NUMBEROFPANEL][i%MyFrame.NUMBEROFPANEL]+ " ");
			}
			System.out.println("");
		}
	}

	/* ************************************************************************************************** */

}
