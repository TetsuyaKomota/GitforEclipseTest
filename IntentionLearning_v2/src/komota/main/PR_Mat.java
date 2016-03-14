package komota.main;

public class PR_Mat extends MyPR{

	//卒論の改良用の線形代数モデル．
	//データの保持形式がMyPRと異なるが，使用する特徴量はとりあえず物体の座標成分のみ

	//特徴量配列ログクラス
	StepLog_Mat[] logdata_mat = null;

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
			this.logdata_mat[idx] = new StepLog_Mat(num,this.logdata[idx]);
			idx++;
		}
	}
	public PR_Mat(){
		this("logdata.txt");
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
