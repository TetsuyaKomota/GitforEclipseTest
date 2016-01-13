package komota.pr.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import komota.main.MyFrame;
import komota.main.MyPR;
import komota.main.MyPanel;

public class PR_Features extends MyPR{


	public static void main(String[] args){
		PR_Features pr = new PR_Features(9,"logdata.txt");
		int un = 0;
		while(true){
			if(pr.logdata_feature[un] == null){
				break;
			}
			pr.logdata_feature[un].show();
			un++;
		}
	}

	//特徴量の遷移を考慮するPRクラス。
	//位置遷移を考慮するPRと合わせることで、より高次の動作を学習することを目的とする

	//定数
	//ログのタイプ定数
	public static final int FEATURE = 4;

	//フィールド
	StepLog_Feature[] logdata_feature;
	double[][] goalmean;
	double[][] goalQ;
	double[][] goalvariance;
	//オブジェクト数
	int numref;

	//コンストラクタ
	public PR_Features(int numref,String filename){
		super(filename);
		this.numref = numref;
		//ログ読みこみ
		File file = new File("log/"+filename);
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		this.logdata_feature = new StepLog_Feature[1000];
		int step = 0;
		int temptype = -1;
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
			//どのログにおける特徴量ログなのかを特徴量ログに付与するため、直前のログのタイプを取得しておく
			else if(line.split(",")[0].equals("start ") == true){
				temptype = START;
			}
			else if(line.split(",")[0].equals("goal  ") == true){
				temptype = GOAL;
			}
			else if(line.split(",")[0].equals("change") == true){
				temptype = CHANGE;
			}
			else if(line.split(",")[0].equals("status") == true){
				temptype = STATUS;
			}
			//FEATUREログのみインスタンス生成
			else if(line.split(",")[0].equals("featur") == true){
				this.logdata_feature[step] = new StepLog_Feature(step,temptype,line);
				step++;
			}
		}
		//StepLogのコンストラクタで壊れたデータは受け付けないように変更したため、ここでの前処理は不要になった。
		//this.logdata[step-1] = null;
		this.close();

		this.goalmean = new double[this.numref][MyPanel.NUMBEROFFEATURE];
		this.goalQ = new double[this.numref][MyPanel.NUMBEROFFEATURE];
		this.goalvariance = new double[this.numref][MyPanel.NUMBEROFFEATURE];
		for(int i=0;i<this.goalmean.length;i++){
			for(int j=0;j<MyPanel.NUMBEROFFEATURE;j++){
				this.goalmean[i][j] = 0;
				this.goalQ[i][j] = 0;
				this.goalvariance[i][j] = 0;
			}
		}
	}

	//学習
	@Override
	public void learnfromLog(){
		//goal参照回数
		int count = 0;
		double[][] tempfeatures = new double[this.numref][MyPanel.NUMBEROFFEATURE];
		for(int t=0;t<this.logdata_feature.length;t++){
			if(logdata_feature[t] == null || count > MyPR.getNumberofEvaluation()){
				break;
			}
			//"start "ログの場合、初期状態として特徴量を保持する
			if(logdata_feature[t].getType() == START){
				for(int s=0;s<tempfeatures.length;s++){
					for(int f=0;f<MyPanel.NUMBEROFFEATURE;f++){
						tempfeatures[s][f] = this.logdata_feature[t].getStepFeaturesField()[s][f];
					}
				}
			}
			//"goal  "ログの場合、初期状態との特徴量の変化量を取得し、平均値goalpointを更新する
			else if(logdata_feature[t].getType() == GOAL){

				double temp = 0;
				for(int s=0;s<this.numref;s++){
					for(int f=0;f<MyPanel.NUMBEROFFEATURE;f++){
						temp = this.logdata_feature[t].getStepFeaturesField()[s][f] - tempfeatures[s][f];

						//更新
						this.goalmean[s][f] = (count * this.goalmean[s][f] + temp)/(count+1);
						this.goalQ[s][f] = (count * this.goalQ[s][f] + temp*temp)/(count+1);
						this.goalvariance[s][f] = this.goalQ[s][f] - this.goalmean[s][f]*this.goalmean[s][f];
					}
				}

				count++;
			}
			//"change"ログの場合、何もしない
			else if(logdata_feature[t].getType() == CHANGE){
			}
			//"status"ログの場合、何もしない
			else if(logdata_feature[t].getType() == STATUS){
			}
		}
	}
	//再現
	//最も尤もらしい特徴量は、平均遷移量が大きく、分散の小さい特徴量。よって、goalmean/goalvarianceで評価する
	//位置遷移に関するPRとの比較が難しいと思う。要考察
	@Override
	public void reproduction(MyFrame frame){
		double tempmaxlikelihood = -10000;
		int temps = -1;
		int tempf = -1;
		for(int s=0;s<this.numref;s++){
			for(int f=0;f<MyPanel.NUMBEROFFEATURE;f++){
				System.out.println(this.goalmean[s][f]/(this.goalvariance[s][f]+1));
				if(tempmaxlikelihood < this.goalmean[s][f]/(this.goalvariance[s][f]+1)){
					tempmaxlikelihood = this.goalmean[s][f]/(this.goalvariance[s][f]+1);
					temps = s;
					tempf = f;
				}
			}
		}
		//盤面から、選択されたオブジェクトを探す
		for(int i=0;i<MyFrame.NUMBEROFPANEL;i++){
			for(int j=0;j<MyFrame.NUMBEROFPANEL;j++){
				if(frame.panels[i][j].getStatus() == temps){
					double[] features = frame.panels[i][j].getFeatures();
					features[tempf] += this.goalmean[temps][tempf];
					frame.panels[i][j].setFeatures(features);
				}
			}
		}
		System.out.println("[PR_Features]reproduction:選ばれたのは、("+temps+","+tempf+")デス");
	}
	//参照点の学習結果リセット
	@Override
	public void initialize(){
	}


	/* ************************************************************************************************** */
	//解析用のFEATUREログデータを表す内部クラス
	protected class StepLog_Feature{
		//フィールド
		//ステップ数
		int step = -1;
		//種類
		int type = -1;
		//状態配列
		double[][] features = null;

		//コンストラクタ
		StepLog_Feature(int step ,int type , String line){
			this.step = step;
			String[] tempstrings = line.split(",");
			this.type = type;
			this.features = new double[PR_Features.this.numref][MyPanel.NUMBEROFFEATURE];

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
		//セッター、ゲッター
		public double getStepFeature(int status,int feature){
			return this.features[status][feature];
		}
		public double[][] getStepFeaturesField(){
			return this.features;
		}
		public void setType(int type){
			this.type = type;
		}
		public int getType(){
			return this.type;
		}

		//表示
		void show(){
			String typename = "ふぃーちゃー";
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

			for(int s=0;s<PR_Features.this.numref;s++){
				System.out.print("Status:"+s+":");
				for(int f=0;f<MyPanel.NUMBEROFFEATURE;f++){
					System.out.print(" "+this.features[s][f]);
				}
			}

			System.out.println("");
		}
	}

	/* ************************************************************************************************** */
}
