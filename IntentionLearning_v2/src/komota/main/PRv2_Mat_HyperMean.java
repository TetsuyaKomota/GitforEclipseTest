package komota.main;

import komota.lib.DataSetGenerator_v2;
import komota.lib.HyperMean;
import komota.lib.MyMatrix;
import komota.supers.MyPR_v2;

public class PRv2_Mat_HyperMean extends MyPR_v2{

	public static void main(String[] args){
		//データ自体の誤りチェック
		DataSetGenerator_v2 g = new DataSetGenerator_v2("2D_NbO", 0);
		g.setNumberofData(100);
		//g.functionPlugin1();
		PRv2_Mat_HyperMean pr = new PRv2_Mat_HyperMean(5,"logdata.txt");
		int count=0;
		for(int i=0;i<pr.getNumberofLog();i++){
			if(g.isTrueLog(pr.getStartLog(i), pr.getGoalLog(i)) == false){
				count++;
			}
		}
		System.out.println("The Number of Error Data is :"+count);
	}




	public PRv2_Mat_HyperMean(int numofobjects, String filename) {
		super(numofobjects, filename);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	public void learnfromLog() {
		// TODO 自動生成されたメソッド・スタブ

		/*
		 * 学習の流れ
		 *
		 * 1. 変数を宣言する: (学習回数),(ストライド),(シフト)
		 * 2. (ストライド) >= データ量 なら3.へ
		 *
		 * 		2-1. (ストライド)と(シフト)によってきまったデータで行列を作る
		 * 		2-2. 正則でないなら2-3.へ
		 *
		 * 			2-2-1. ベクトル化した行列をHyperMeanに入力
		 * 			2-2-2. (学習回数)++
		 *
		 * 		2-3. (シフト)++
		 * 		2-4. (シフト) < データ量 なら2-5.へ
		 *
		 * 			2-4-1. (シフト)=0
		 * 			2-4-2. (ストライド)++
		 *
		 * 		2-5. 2.へ
		 *
		 * 3. HyperMeanから外れ値除去平均を取得し，行列に戻す
		 * 4. その行列をセーブして終了
		 */

		//1.
		int learningtime = 0;
		int stride = 1;
		int shift = 0;

		final int dimension = this.getStartLog(0).length;

		MyMatrix starts = new MyMatrix(dimension);
		MyMatrix goals = new MyMatrix(dimension);
		MyMatrix x = new MyMatrix(dimension);

		HyperMean hm = new HyperMean();

		//2.
		while(true){
			if(stride >= dimension){
				break;
			}
			//2-1.
			for(int i=0;i<dimension;i++){
				double[] temps = this.getStartLog((i * stride + shift) % this.getNumberofLog());
				double[] tempg = this.getGoalLog((i * stride + shift) % this.getNumberofLog());

				for(int j=0;j<dimension;j++){
					starts.setData(j, i, temps[j]);
					goals.setData(j, i, tempg[j]);
				}
			}

			//2-2.
			if(starts.getDetV() != 0){
				x = goals.mult(starts.inv());
				if(x != null){
					hm.addData(x.vectorize());
					learningtime++;
				}
			}

			//2-3.
			shift++;
			//2-4.
			if(shift >= this.getNumberofLog()){
				shift = 0;
				stride++;
			}
		}
		System.out.println("[PRv2_Mat_HyperMean]learnfromLog:learningtime:"+learningtime);
		//3.
		double[] vec = hm.getHyperMean();

		this.X = new MyMatrix(dimension,vec);

		/* 超ハイパーモード
		for(int i=1;i<this.X.getDimension();i++){
			this.X.setData(i, 0, 0);
		}
		/**/

		//4.
		System.out.println("学習しました");
		this.X.approximate().show_approximately();
	}

	@Override
	public void initialize() {
		// TODO 自動生成されたメソッド・スタブ

	}
}
