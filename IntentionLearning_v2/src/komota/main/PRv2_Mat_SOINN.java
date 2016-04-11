package komota.main;

import komota.lib.MyMatrix;
import komota.lib.Statics;
import komota.supers.MyFrame;
import komota.supers.MyPR_v2;

public class PRv2_Mat_SOINN extends MyPR_v2{

	//フィールド
	//学習した行列
	MyMatrix X;

	//コンストラクタ
	public PRv2_Mat_SOINN(int numofobjects, String filename) {
		super(numofobjects, filename);
		// TODO 自動生成されたコンストラクター・スタブ
		this.X = new MyMatrix(this.getStartLog(0).length);
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

		ExtendedSOINN soinn = new ExtendedSOINN(dimension*dimension,1000,1000);

		//2.
		while(true){
			if(learningtime > Statics.NUMBEROFMATRIXS || stride >= dimension){
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
				soinn.inputSignal(x.vectorize());
				learningtime++;
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
		if(learningtime <= Statics.NUMBEROFMATRIXS){
			System.out.println("データが足りてないよ！");
		}
		//4.
		soinn.classify();
		//5.
		double[] vec = soinn.getNodeMean(0);
		this.X = new MyMatrix(dimension,vec);
		System.out.println("学習しました");
		this.X.approximate().show_approximately();
	}

	@Override
	public void reproduction(MyFrame frame) {
		// TODO 自動生成されたメソッド・スタブ

		//盤面から状態を取得する
		double[] current = new double[this.getStartLog(0).length];
		int idx = 1;
		current[0] = 1;
		while(true){
			for(int i=0;i<Statics.NUMBEROFPANEL;i++){
				for(int j=0;j<Statics.NUMBEROFPANEL;j++){
					if(frame.panels[i][j].getStatus() == idx){
						current[2*idx-1] = i;
						current[2*idx] = j;
					}
				}
			}
			idx++;
			if(idx > Statics.NUMBEROFKIND){
				break;
			}
		}
		//Xをかけて推定目標を求める
		double[] goal = new double[this.getStartLog(0).length];
		for(int i=0;i<goal.length;i++){
			for(int j=0;j<current.length;j++){
				goal[i] += this.X.getData(i, j) * current[j];
			}
		}
		//得られた推定目標に盤面を合わせる
		for(int i=0;i<goal.length;i++){
			System.out.print(goal[i]+",");
		}
		for(int i=0;i<Statics.NUMBEROFPANEL;i++){
			for(int j=0;j<Statics.NUMBEROFPANEL;j++){
				frame.panels[i][j].setStatus(0);
			}
		}
		for(int i=1;i<=this.getNumberofObjects();i++){
			frame.panels[(int)(goal[2*i-1]+0.5)][(int)(goal[2*i]+0.5)].setStatus(i);
		}
	}

	@Override
	public void initialize() {
		// TODO 自動生成されたメソッド・スタブ

	}


}
