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

		for(int i=0;i<Statics.GA_NUMBEROFAGENTS;i++){
			currentagent[i] = MatFactory.random(this.getStartLog(0).length, Statics.NUMBEROFPANEL, Statics.NUMBEROFPANEL);
		}

		//以下評価値が閾値を下回るまでループ
		while(true){

			//評価値計算
			int count = 0;
			while(true){
				if(count >= this.getNumberofLog()){
					break;
				}
				double[] tempstart = this.getStartLog(count);
				for(int t=0;t<Statics.GA_NUMBEROFAGENTS;t++){
					double[] temppredicted = currentagent[t].multwithVec(tempstart);
					double[] tempgoal = this.getGoalLog(count);
					double tempdistance = 0;
					for(int d=0;d<Statics.NUMBEROFPANEL;d++){
						tempdistance += (temppredicted[d] - tempgoal[d])*(temppredicted[d] - tempgoal[d]);
					}
					distance[t] = ((distance[t]) * count + tempdistance)/(count+1);
				}
				count++;
			}

			//評価値をもとにソート．面倒だからバブルソートでいいよ
			for(int i=0;i<Statics.GA_NUMBEROFAGENTS;i++){
				for(int j=Statics.GA_NUMBEROFAGENTS-1;j>i;j++){

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
			if(distance[0] < Statics.GA_THRETHOLD){
				break;
			}
			else{
				//閾値を下回っていない場合，次世代エージェントを作成する
				/* ******************************************************** */
				/* ******************************************************** */
				/* ******************************************************** */
				/* ******************************************************** */
				/* ******************************************************** */
				/* ******************************************************** */
				/* ******************************************************** */
			}
		}
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
