package komota.main;

import komota.lib.MyMatrix;
import komota.lib.Statics;
import komota.soinn.KomotaSOINN;
import komota.soinn.KomotaSOINN_Euclidean;
import komota.supers.MyPR_v2;

public class PRv2_Mat_SOINN_v2 extends MyPR_v2{

	public static void main(String[] args){

	}




	public PRv2_Mat_SOINN_v2(int numofobjects, String filename) {
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
		 * 2. (学習回数) > NUMBEROFMATRIXS || (ストライド) >= データ量 なら3.へ
		 *
		 * 		2-1. (ストライド)と(シフト)によってきまったデータで行列を作る
		 * 		2-2. 正則でないなら2-3.へ
		 *
		 * 			2-2-1. ベクトル化した行列をSOINNに入力
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
		 * 3. (学習回数) > NUMBEROFMATRIXS なら4.へ
		 *
		 * 		3-1. 「データ量が不足しています」と出力する
		 *
		 * 4. SOINN.calssify()
		 * 5. 代表ノードを選択し，行列に戻す
		 * 6. その行列をセーブして終了
		 */

		//1.
		int learningtime = 0;
		int stride = 1;
		int shift = 0;

		final int dimension = this.getStartLog(0).length;

		MyMatrix starts = new MyMatrix(dimension);
		MyMatrix goals = new MyMatrix(dimension);
		MyMatrix x = new MyMatrix(dimension);

		KomotaSOINN soinn = new KomotaSOINN_Euclidean(dimension*dimension,1000,1000);
		//KomotaSOINN soinn = new KomotaSOINN_CosSimilarity(dimension*dimension,1000,1000);

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
					x.show_approximately();
					soinn.inputSignal(x.vectorize());
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
		//3.
		System.out.println("[PRv2_Mat_SOINN]learnfromLog:learningtime:"+learningtime);
		if(learningtime <= Statics.NUMBEROFMATRIXS){
			System.out.println("データが足りてないよ！");
		}
		//4.
		soinn.removeUnnecessaryNode();
		soinn.removeUnnecessaryNode();
		soinn.removeUnnecessaryNode();
		soinn.classify();
		//5.
		//double[] vec = soinn.getNodeMean(0);

		double[] vec = new double[dimension*dimension];
		int nodenum = soinn.getNodeNum(false);

		//孤立ノード削除後の全ノードの平均を獲得し，学習結果とする
		for(int j=0;j<vec.length;j++){
			for(int i=0;i<nodenum;i++){
				vec[j] += soinn.getNode(i).getSignal()[j];
			}
			vec[j] /= nodenum;
		}

		this.X = new MyMatrix(dimension,vec);
		System.out.println("学習しました");
		this.X.approximate().show_approximately();
	}

	@Override
	public void initialize() {
		// TODO 自動生成されたメソッド・スタブ

	}
}
