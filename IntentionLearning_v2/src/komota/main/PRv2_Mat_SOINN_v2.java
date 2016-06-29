package komota.main;

import komota.lib.DataSetGenerator_v2;
import komota.lib.MyMatrix;
import komota.lib.Statics;
import komota.soinn.KomotaSOINN;
import komota.soinn.KomotaSOINN_Chebyshev;
import komota.supers.MyPR_v2;

public class PRv2_Mat_SOINN_v2 extends MyPR_v2{

	public static void main(String[] args){
		//データ自体の誤りチェック
		DataSetGenerator_v2 g = new DataSetGenerator_v2("2D_NbO", 0);
		g.setNumberofData(100);
		//g.functionPlugin1();
		PRv2_Mat_SOINN_v2 pr = new PRv2_Mat_SOINN_v2(5,"logdata.txt");
		int count=0;
		for(int i=0;i<pr.getNumberofLog();i++){
			if(g.isTrueLog(pr.getStartLog(i), pr.getGoalLog(i)) == false){
				count++;
			}
		}
		System.out.println("The Number of Error Data is :"+count);
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

		//KomotaSOINN soinn = new KomotaSOINN_Euclidean(dimension*dimension,1000,1000);
		KomotaSOINN soinn = new KomotaSOINN_Chebyshev(dimension*dimension,1000,1000);
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

					//以下コメントアウト部分は，SOINNに代入する行列 x をあらかじめ選別してあげればうまくいくのかを実験したもの．
					//この結果から，SOINNがノイズをうまく除去できていれば，いい学習が行えることが分かった
					//つまり，今からやるべきはSOINNの中身を工夫すること．

					/* ****************************************************************************************************************************
					//x を表示し，SOINNに入力するかしないか選べるようにする．
					x.show_approximately();
					String sin = null;
					BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
					try {
						sin = br.readLine();
					} catch (IOException e) {
						// TODO 自動生成された catch ブロック
						e.printStackTrace();
					}
					if(sin.equals("a")){
						soinn.inputSignal(x.vectorize());
						learningtime++;
					}
					/* ****************************************************************************************************************************
					//手作業があまりに面倒だったので，for文でチェックする
					boolean tflag = true;
					double threshold = 0.2;
					for(int ti=0;ti<x.getDimension();ti++){
						for(int tj=0;tj<x.getDimension();tj++){
							if(ti == 1 && tj == 1){
								if(Math.abs(x.getData(ti, tj) - 0.5) > threshold){
									tflag = false;
								}
							}
							else if(ti == 1 && tj == x.getDimension() - 2){
								if(Math.abs(x.getData(ti, tj) - 0.5) > threshold){
									tflag = false;
								}
							}
							else if(ti == 2 && tj == 2){
								if(Math.abs(x.getData(ti, tj) - 0.5) > threshold){
									tflag = false;
								}
							}
							else if(ti == 2 && tj == x.getDimension() - 1){
								if(Math.abs(x.getData(ti, tj) - 0.5) > threshold){
									tflag = false;
								}
							}
							else if(ti == tj){
								if(Math.abs(x.getData(ti, tj) - 1.0) > threshold){
									tflag = false;
								}
							}
							else if(ti != 0 && tj != 0){
								if(Math.abs(x.getData(ti, tj) - 0.0) > threshold){
									tflag = false;
								}
							}
						}
					}
					if(tflag == true){
						soinn.inputSignal(x.vectorize());
						learningtime++;
					}

					/* **************************************************************************************************************************** */
					//x.show_approximately();
					soinn.inputSignal(x.vectorize());
					learningtime++;
					/* **************************************************************************************************************************** */

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
