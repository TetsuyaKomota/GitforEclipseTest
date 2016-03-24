package komota.main;

import komota.gomi.ExtendedSOINN;
import komota.lib.MyIO;
import komota.lib.MyMatrix;

public class PR_Mat_SOINN extends PR_Mat{

	//卒論の改良用の線形代数モデル．
	//データの保持形式がMyPRと異なるが，使用する特徴量はとりあえず物体の座標成分のみ

	//特徴量配列ログクラス
	StepLog_Mat[] logdata_mat = null;
	//学習結果の係数行列
	MyMatrix X;

	//コンストラクタ
	public PR_Mat_SOINN(String filename){
		super(filename);
	}
	public PR_Mat_SOINN(){
		this("logdata.txt");
	}

	//学習
	@Override
	public void learnfromLog(){

		MyIO io = new MyIO();
		io.writeFile("20160317/testoutputmatrix_SOINN.txt");

		int num = this.logdata_mat[0].numberoffeatures;
		this.X = new MyMatrix(num);

		ExtendedSOINN soinn = new ExtendedSOINN(121,1000,5);

		//平均をいっぱいとる.とりあえず5回
		for(int t=0;t<66;t++){

			int shift = 0;
			while(true){
				MyMatrix tempstarts = new MyMatrix(num);
				MyMatrix tempgoals = new MyMatrix(num);

				//教示データ行列を作成する
				//教示データは上から10使用し，tごとに下にずらしていく
				int count = 0;
				int idx = 0;
				while(count < t + shift){
					if(this.logdata_mat[idx].getType() == Statics.GOAL){
						count++;
					}
					idx++;
				}
				count = 0;
				int[] ts = null;
				while(true){
					//startログの場合，更新する
					if(this.logdata_mat[idx].getType() == Statics.START){
						ts = this.logdata_mat[idx].getStepStatusField();
					}
					//goalログの場合，直前のstartログとともにtemp行列に代入する
					else if(this.logdata_mat[idx].getType() == Statics.GOAL){
						for(int i=0;i<num;i++){
							tempstarts.setData(i, count, ts[i]);
							tempgoals.setData(i,count,this.logdata_mat[idx].getStepStatusField()[i]);
						}
						count++;
					}
					//ログをnum個取得したら(temp行列が完成したら)ループを抜ける
					if(count >= num){
						break;
					}
					else{
						idx++;
					}
				}
				//正則であるかチェック．正則でない場合，ログとして不成立
				if(tempstarts.getDetV() != 0){
					MyMatrix tempresults = tempgoals.mult(tempstarts.inv());

					io.println(t+"回目の教示データ行列");
					io.printMatrix_approximately(tempresults, t);

					soinn.inputSignal(tempresults.vectorize());

					io.println(t+"回目の教示データ適用後のX");
					io.printMatrix_approximately(X, 200+t);


					break;
				}
				else{
					System.out.println("正則でない");
					shift++;
				}
			}
		}
		soinn.classify();
		double[] vec = soinn.getNodeMean(0);
		System.out.println("SOINN学習結果");
		for(int n = 0;n<vec.length;n++){
			System.out.print(vec[n]+" ");
		}
		System.out.println();
		this.X = new MyMatrix(num,vec);
		System.out.println("学習しました");
		this.X.approximate().show_approximately();
	}
}
