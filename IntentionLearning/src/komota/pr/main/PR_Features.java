package komota.pr.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

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
			//FEATUREログのみインスタンス生成
			else if(line.split(",")[0].equals("featur") == true){
				this.logdata_feature[step] = new StepLog_Feature(step,line);
				step++;
			}
		}
		//StepLogのコンストラクタで壊れたデータは受け付けないように変更したため、ここでの前処理は不要になった。
		//this.logdata[step-1] = null;
		this.close();

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
		StepLog_Feature(int step , String line){
			this.step = step;
			String[] tempstrings = line.split(",");
			this.type = FEATURE;
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
