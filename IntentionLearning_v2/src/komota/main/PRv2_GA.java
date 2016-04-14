package komota.main;

import komota.lib.MatFactory;
import komota.lib.MyMatrix;
import komota.lib.Statics;
import komota.supers.MyFrame;
import komota.supers.MyPR_v2;

public class PRv2_GA extends MyPR_v2{


	//進化計算を用いた学習クラス

	//フィールド
	//学習結果行列
	MyMatrix X;

	public PRv2_GA(int numofobjects, String filename) {
		super(numofobjects, filename);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	public void learnfromLog() {
		// TODO 自動生成されたメソッド・スタブ

		/*
		 * 1. GA_NUMBEROFAGENTS 個の 初期エージェントをランダム生成
		 * 2. max_rate ← |(初期状態)(エージェント) - (最終状態)| の最小値
		 * 3. max_rate < GA_THRETHOLD なら 4.へ
		 *
		 * 		3-1. |(初期状態)(エージェント) - (最終状態)| の昇順にソート
		 * 		3-2. 上位 GA_NUMBEROBAGENTS * GA_ELITE_RATE 個のエージェントはそのまま保存
		 * 		3-3. 残りのうち GA_NUMBEROBAGENTS * GA_METAMORPHOSE_RATE 個のエージェントの成分一つをランダムに変更.残りが奇数になる場合はもう一つ突然変異
		 * 		3-4. 残りのエージェントを2個ずつのペアにする．
		 * 		3-5. ランダムに1行をスワップ
		 * 		3-6. これらを新しいエージェントとして 2.に戻る
		 *
		 * 4. max_rate ← |(初期状態)(エージェント) - (最終状態)| を最小とするエージェントを出力
		 */

		//1.
		MyMatrix[] currentagent = new MyMatrix[Statics.GA_NUMBEROFAGENTS];
		MyMatrix[] nextagent = new MyMatrix[Statics.GA_NUMBEROFAGENTS];

		//ソートしたりするのに使う，|(初期状態)(エージェント) - (最終状態)| 入れ
		double[] distance = new double[Statics.GA_NUMBEROFAGENTS];

		//デバッグ用
		int debug_count = 0;
		double debug_current = -1;

		for(int i=0;i<Statics.GA_NUMBEROFAGENTS;i++){
			currentagent[i] = MatFactory.random(this.getStartLog(0).length+1/*定数項分*/, Statics.NUMBEROFPANEL, Statics.NUMBEROFPANEL);
		}

		//以下評価値が閾値を下回るまでループ
		while(true){

			//評価値計算
			int count = 0;
			while(true){
				if(count >= this.getNumberofLog()){
					break;
				}
				double[] tempstart = new double[this.getStartLog(0).length+1/*定数項分*/];
				tempstart[0] = 1;
				for(int i=1;i<tempstart.length;i++){
					tempstart[i] = this.getStartLog(count)[i-1];
				}
				for(int t=0;t<Statics.GA_NUMBEROFAGENTS;t++){
					double[] temppredicted = currentagent[t].multwithVec(tempstart);
					double[] tempgoal = new double[this.getStartLog(0).length+1/*定数項分*/];
					tempgoal[0] = 1;
					for(int i=1;i<tempgoal.length;i++){
						tempgoal[i] = this.getGoalLog(count)[i-1];
					}
					double tempdistance = 0;
					for(int d=0;d<tempgoal.length;d++){
						tempdistance += (temppredicted[d] - tempgoal[d])*(temppredicted[d] - tempgoal[d]);
					}
					distance[t] = ((distance[t]) * count + tempdistance)/(count+1);
				}
				count++;
			}

			//評価値をもとにソート．面倒だからバブルソートでいいよ
			for(int i=0;i<Statics.GA_NUMBEROFAGENTS;i++){
				for(int j=Statics.GA_NUMBEROFAGENTS-1;j>i;j--){

					if(distance[j] < distance[j-1]){

						double temp = distance[j-1];
						distance[j-1] = distance[j];
						distance[j] = temp;

						MyMatrix tempmat = currentagent[j-1];
						currentagent[j-1] = currentagent[j];
						currentagent[j] = tempmat;
					}

				}
			}

			//currentagent[0]が現状最適なエージェントになったはずなので，閾値検証
			if(debug_current < 0 || debug_current > distance[0]){
				System.out.println("current evaluation point:"+distance[0]+" calculation time:"+debug_count);
				debug_current = distance[0];
				debug_count = 0;
			}
			else{
				debug_count++;
			}
			if(distance[0] < Statics.GA_THRETHOLD){
				break;
			}
			else{
				//閾値を下回っていない場合，次世代エージェントを作成する
				/*
				 * A. 残りエージェント数 NUM を初期化する
				 * B. nextagent 配列を初期化する
				 * C. GA_ELITE_RATE * GA_NUMBEROFAGENT 個のエージェントを上からnextagentに移す．
				 * D. NUM ← NUM - GA_ELITE_RATE * GA_NUMBEROFAGENT
				 * E. currentagent の残りのエージェントを適当に並べなおす
				 * F. GA_METAMORPHOSE * GA_NUMBEROFAGENT 個のエージェントを上から取り出し，適当な要素をランダムに設定しなおしてnextagentに移す．
				 * G. NUM ← NUM - GA_METAMORPHOSE * GA_NUMBEROFAGENT
				 * H. NUM が奇数なら，もう一つを上から取り出し，適当な要素をランダムに設定しなおしてnextagentに移し，NUM ← NUM - 1．
				 * I. 上から2つずつを取り出し，適当な行を入れ替えてnextagentに移し，NUM ← NUM - 2
				 * J. NUM > 0 ならG.へ
				 * K. currentagent ← nextagent
				 */
				//A.
				int numberofnext = 0;
				//B.
				nextagent = new MyMatrix[Statics.GA_NUMBEROFAGENTS];
				//C.
				int temp = 0;
				int check = (int)(Statics.GA_ELITE_RATE * Statics.GA_NUMBEROFAGENTS);
				while(temp < check){
					nextagent[numberofnext] = currentagent[temp];
					numberofnext++;
					temp++;
				}
				//E.
				MyMatrix[] tempmat = new MyMatrix[Statics.GA_NUMBEROFAGENTS - numberofnext];
				temp = 0;
				check = tempmat.length;
				while(temp < check){
					int rand = (int)(tempmat.length * Math.random());
					if(tempmat[rand] == null){
						tempmat[rand] = currentagent[numberofnext + temp];
						temp++;
					}
				}
				//F.
				int d = this.getStartLog(0).length+1/*定数項分*/;
				temp = 0;
				check = (int)(Statics.GA_METAMORPHOSE_RATE * Statics.GA_NUMBEROFAGENTS);
				while(temp < check){
					MyMatrix mat = tempmat[temp];
					mat.setData((int)(d*Math.random()),(int)(d*Math.random()), (int)(Statics.NUMBEROFPANEL * Math.random()));
					/*
					 * ここ嘘かも
					 */
					nextagent[numberofnext] = mat;
					numberofnext++;
					temp++;
				}
				//H.
				if(Statics.GA_NUMBEROFAGENTS - numberofnext % 2 == 1){
					MyMatrix mat = tempmat[numberofnext];
					mat.setData((int)(d*Math.random()),(int)(d*Math.random()), (int)(Statics.NUMBEROFPANEL * Math.random()));
					/*
					 * ここ嘘かも
					 */
					nextagent[numberofnext] = mat;
					numberofnext++;
				}

				temp = 0;
				check = Statics.GA_NUMBEROFAGENTS - numberofnext;
				while(temp < check){
					MyMatrix mat1 = tempmat[tempmat.length-temp-1];
					MyMatrix mat2 = tempmat[tempmat.length-temp-2];

					int swapidx = (int)(d*Math.random());
					double[] swap = mat1.getData()[swapidx];
					for(int i=0;i<d;i++){
						mat1.setData(swapidx, i, mat2.getData(swapidx, i));
						mat2.setData(swapidx, i, swap[i]);
					}

					nextagent[numberofnext] = mat1;
					numberofnext++;
					nextagent[numberofnext] = mat2;
					numberofnext++;

					temp += 2;
				}
				currentagent = nextagent;
			}
		}
		//currentagent[0]が最適な行列
		this.X = currentagent[0];
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
