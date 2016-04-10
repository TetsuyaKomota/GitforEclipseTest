package komota.main;

import komota.lib.MyMatrix;

public class PR_Mat_SOINN extends PR_Mat{

	//卒論の改良用の線形代数モデル．
	//データの保持形式がMyPRと異なるが，使用する特徴量はとりあえず物体の座標成分のみ

	//コンストラクタ
	public PR_Mat_SOINN(String filename){
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
	public PR_Mat_SOINN(){
		this("logdata.txt");
	}


	//学習メソッドの改良版テスト．logdataの使い方を改善
	@Override
	public void learnfromLog(){
		int num = this.logdata_mat[0].numberoffeatures;
		this.X = new MyMatrix(num);

		ExtendedSOINN soinn = new ExtendedSOINN(num*num,1000,5);

		MyMatrix starts = new MyMatrix(num);
		MyMatrix goals = new MyMatrix(num);

		int learntime = 0;

		//データ量を数える
		int numberofdata = 0;
		for(int i=0;i<this.logdata_mat.length;i++){
			if(this.logdata_mat[i] != null && this.logdata_mat[i].getType() == Statics.GOAL){
				numberofdata++;
			}
		}

		int[] selected = new int[numberofdata];

		int r = 1;
		int c = 0;

		while(learntime < Statics.NUMBEROFMATRIXS){
			for(int i=0;i<selected.length;i++){
				selected[i] = 0;
			}
			for(int i=0;i<num;i++){
				int temp = (i * r + c) % numberofdata;
				selected[temp] = 1;
			}

			this.contain(starts, goals, this.localize(selected));

			if(starts.getDetV() != 0){

				MyMatrix tempresults = goals.mult(starts.inv());

				soinn.inputSignal(tempresults.vectorize());

				learntime++;
			}
			else{
				System.out.println("正則じゃないよ");
			}

			r++;
			if(r >= num){
				r = 0;
				c++;
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
	}}
