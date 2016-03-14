package komota.main;

import komota.lib.MyMatrix;

public class PR_Mat extends MyPR{

	//卒論の改良用の線形代数モデル．
	//データの保持形式がMyPRと異なるが，使用する特徴量はとりあえず物体の座標成分のみ

	//特徴量配列ログクラス
	StepLog_Mat[] logdata_mat = null;
	//学習結果の係数行列
	MyMatrix X;

	//コンストラクタ
	public PR_Mat(String filename){
		super(filename);

		this.logdata_mat = new StepLog_Mat[Statics.MAX_NUMBEROFLOG];
		//特徴量数を探索
		int num = 0;
		for(int i=0;i<Statics.NUMBEROFPANEL;i++){
			for(int j=0;j<Statics.NUMBEROFPANEL;j++){
				if(this.logdata[0].getStepStatusField()[i][j] != 0){
					num++;
				}
			}
		}


		int idx = 0;
		while(true){
			if(this.logdata[idx] == null){
				break;
			}
			this.logdata_mat[idx] = new StepLog_Mat(2*num+1,this.logdata[idx]);
			idx++;
		}
	}
	public PR_Mat(){
		this("logdata.txt");
	}

	//学習
	@Override
	public void learnfromLog(){
		int num = this.logdata_mat[0].numberoffeatures;
		this.X = new MyMatrix(num);
		//平均をいっぱいとる.とりあえず5回
		for(int t=0;t<56;t++){

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

					X = X.mult(t);
					X = X.add(tempresults);
					X = X.mult((double)1/(t+1));

					break;
				}
				else{
					System.out.println("正則でない");
					shift++;
				}
			}
		}
		System.out.println("学習しました");
		this.X.approximate().show();
	}


	//再現
	@Override
	public void reproduction(MyFrame frame){
		//現状態は最後のstartログに記録されているという前提を使得ないので，盤面から取得する
		int[] ts = new int[this.logdata_mat[0].numberoffeatures];

		ts[0] = 1;
		for(int i=0;i<Statics.NUMBEROFPANEL;i++){
			for(int j=0;j<Statics.NUMBEROFPANEL;j++){
				if(frame.panels[i][j].getStatus() != 0){
					ts[2*frame.panels[i][j].getStatus()-1] = i;
					ts[2*frame.panels[i][j].getStatus()] = j;
				}
			}
		}

		for(int i=0;i<ts.length;i++){
			System.out.print(ts[i]+" ");
		}
		System.out.println();

		int[] re = new int[ts.length];


		for(int i=0;i<ts.length;i++){
			for(int j=0;j<ts.length;j++){
				re[i] += this.X.approximate().getData(i, j) * ts[j];
			}
		}

		for(int i=0;i<ts.length;i++){
			System.out.print(re[i]+" ");
		}
		System.out.println();

		//赤の位置は(re[1],re[2])になっているはずなので
		int[] selected = new int[2];
		selected[0] = re[1];
		selected[1] = re[2];
		System.out.println("再現結果は("+selected[0] + "," + selected[1] + ")です");

		frame.setSecondSelected(selected);
	}

	//表示
	public void show(){
		for(int i=0;i<this.logdata_mat.length;i++){
			if(this.logdata_mat[i] != null){
				this.logdata_mat[i].show();
			}
			else{
				break;
			}
		}
	}


	//線形代数モデル用のStepLog
	class StepLog_Mat{
		//ステップ数
		int step = -1;
		//特徴量数
		int numberoffeatures;
		//データタイプ
		int type;
		//特徴量配列
		int[] statuses;

		//コンストラクタ
		StepLog_Mat(int num,StepLog before){
			this.numberoffeatures = num;

			this.step = before.step;
			this.type = before.type;

			this.statuses = new int[this.numberoffeatures];
			statuses[0] = 1;

			int idx = 1;
			while(true){
				for(int i=0;i<before.statuses.length;i++){
					for(int j=0;j<before.statuses[0].length;j++){
						if(before.statuses[i][j] == idx){
							this.statuses[2*idx-1] = i;
							this.statuses[2*idx] = j;
						}
					}
				}
				if(idx >= (this.numberoffeatures-1)/2){
					break;
				}
				else{
					idx++;
				}
			}
		}
		//セッター、ゲッター
		public int getStepStatus(int idx){
			return this.statuses[idx];
		}
		public int[] getStepStatusField(){
			return this.statuses;
		}
		public void setType(int type){
			this.type = type;
		}
		public int getType(){
			return this.type;
		}

		//表示
		void show(){
			String typename = null;
			switch(this.type){
			case Statics.START:
				typename = "start ,";
				break;
			case Statics.GOAL:
				typename = "goal  ,";
				break;
			case Statics.STATUS:
				typename = "status,";
			}
			System.out.print(typename);

			for(int i=0;i<this.statuses.length;i++){
				System.out.print(this.statuses[i]+ " ");
			}
			System.out.println("");
		}
	}
}
