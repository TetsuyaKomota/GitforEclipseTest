package komota.main;

import komota.lib.MatFactory;
import komota.lib.MyMatrix;
import komota.lib.Statics;


public class PRv2_EM extends PRv2_GA{

	public PRv2_EM(int numofobjects, String filename) {
		super(numofobjects, filename);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	//学習時のエージェントはランダムではなく最急降下法に依って更新する
	@Override
	public void learnfromLog(){
		// TODO 自動生成されたメソッド・スタブ

/*
 * 0. エージェント X を生成．初期値はゼロ行列
 * 1. e_min ← mean(X * start - goal)
 * 2. 繰り返し
 * 		2-1. e_min が閾値以下なら3.へ
 * 		2-2. for i,j
 * 			2-2-1. X_temp(i,j) ← X + X^str(i行j列要素だけ str であとはゼロ)
 * 			2-2-2. e ← mean(X_temp * start - goal)
 * 			2-2-3. if e < e_min
 * 				2-2-3-1. gyou,retsu ← i,j
 * 				2-2-3-2. e_min ← e
 * 			2-2-4. X_temp(i,j) ← X - X^str(i行j列要素だけ str であとはゼロ)
 * 			2-2-5. e ← mean(X_temp * start - goal)
 * 			2-2-6. if e < e_min
 * 				2-2-6-1. gyou,retsu ← i,j
 * 				2-2-6-2. e_min ← e
 * 		2-3. X ← X_temp(gyou,retsu) ※正負に注意
 * 3. X を出力
 * */

		//0.
		//this.X = new MyMatrix(this.getStartLog(0).length+1);
		this.X = MatFactory.random(this.getStartLog(0).length+1, Statics.NUMBEROFPANEL, Statics.NUMBEROFPANEL);
		double e_min = 0;
		double e = 0;
		double stride = Statics.EM_STRIDE;
		int dim = this.X.getDimension();
		MyMatrix X_temp = new MyMatrix(dim);

		int gyou = -1;
		int retsu = -1;
		int sign = 0;

		double e_prev = 0;


		//以下，閾値以下になるまで繰り返し
		while(true){
			//以下，評価値が変動しなくなるまで繰り返し
			this.X = MatFactory.random(this.getStartLog(0).length+1, Statics.NUMBEROFPANEL, Statics.NUMBEROFPANEL);
			stride = Statics.EM_STRIDE;

			gyou = -1;
			retsu = -1;
			sign = 0;

			e_prev = calcE(this.X);
			e_min = calcE(this.X);

			while(true){
				//1.
				//System.out.println("ゆかちん:"+e_min+"     "+e_prev);

				if((e_prev - e_min < Statics.EM_PROGRESS_NORMA && e_prev != e_min)|| e_min < Statics.EM_THRETHOLD){
					break;
				}

				e_prev = e_min;

				for(int i=0;i<dim;i++){
					for(int j=0;j<dim;j++){
						//2-2-1.
						X_temp = new MyMatrix(dim);
						X_temp.setData(i, j, stride);
						e = calcE(this.X.add(X_temp));
						if(e < e_min){
							e_min = e;
							gyou = i;
							retsu = j;
							sign = 1;
						}
						e = calcE(this.X.sub(X_temp));
						if(e < e_min){
							e_min = e;
							gyou = i;
							retsu = j;
							sign = -1;
						}
					}
				}
				this.X.setData(gyou, retsu, this.X.getData(gyou,retsu) + sign * stride);
				stride *= Statics.EM_annealing;
			}
			if(e_min < Statics.EM_THRETHOLD){
				break;
			}
			else{
				System.out.println("収束結果は "+e_min+" となりましたが，もっと頑張れると思うのでもう一度計算します");
			}
		}

	}
}
