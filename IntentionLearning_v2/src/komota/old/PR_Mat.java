package komota.old;

import komota.lib.MyIO;
import komota.lib.MyMatrix;
import komota.lib.Statics;
import komota.supers.MyFrame;


//状態空間ベースのログを使用する旧バージョン．MyFrame側では既にその形でのログは取れなくなっているため，このクラスは現在使用できない
@Deprecated
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

	//学習メソッドの改良版テスト．logdataの使い方を改善
	@Override
	public void learnfromLog(){
		int num = this.logdata_mat[0].numberoffeatures;
		this.X = new MyMatrix(num);

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

		while(learntime < 10){
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

				X = X.mult(learntime);
				X = X.add(tempresults);
				X = X.mult((double)1/(learntime+1));

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
		System.out.println("学習しました");
		this.X.approximate().show_approximately();
	}


	//与えられた番号のベクトルのID列を返す
	//selectedはm個のデータ中i番目のデータを使用する場合は1，使用しないデータの場合は0が入った配列
	int[] localize(int[] selected){
		int[] output = new int[selected.length];

		for(int i=0;i<output.length;i++){
			output[i] = -1;
		}

		int token = -1;
		int count = -1;

		for(int t=0;t<selected.length;t++){
			if(selected[t] == 1){
				while(true){
					token++;
					if(this.logdata_mat[token].getType() == Statics.GOAL){
						count++;
						if(count == t){
							output[t] = token;
							break;
						}
					}
				}
			}
		}

		return output;
	}

	//与えられた番号のベクトルを組み合わせた行列を返す
	//idxはm個のデータ中i番目のデータを使用する場合，idx[i]にi番目のデータのIDが入っていて，j番目が使用しないデータの場合はidx[j]に-1が入っているような配列
	void contain(MyMatrix starts,MyMatrix goals,int[] idx){
		//idxが必要次元なければ何もしない
		if(idx == null){
			return;
		}
		int count = 0;
		for(int i=0;i<idx.length;i++){
			if(idx[i] != -1){
				count++;
			}
		}
		if(count != starts.getDimension()){
			return;
		}

		//idxを参照して必要なデータをピッキングする
		count = 0;
		for(int i=0;i<idx.length;i++){
			if(idx[i] != -1){
				//直前のstartログを探す
				int j = idx[i];
				while(true){
					j--;
					if(this.logdata_mat[j].getType() == Statics.START){
						break;
					}
				}
				for(int k=0;k<this.logdata_mat[0].numberoffeatures;k++){
					starts.setData(k,count,this.logdata_mat[j].getStepStatusField()[k]);
					goals.setData(k,count,this.logdata_mat[idx[i]].getStepStatusField()[k]);
				}
				count++;
			}
		}

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

	//クロスバリデーションを行って学習結果の評価を行う
	@Override
	public double evaluate(MyFrame frame,boolean initialize){

		MyIO io = new MyIO();
		io.writeFile("result.txt");

		double output = 0;
		//交差回数
		int count = 0;

		int t=0;
		int T=0;
		for(t=0;t<this.logdata.length;t++){
			//ログ読み切ったら終了
			//交差回数が使用データ量を上回った場合も終了
			if(this.logdata[t] == null || count > Statics.NUMBEROFEVALUATION){
				break;
			}
			//"goal"データが来るまで回す
			if(this.logdata[t].getType() != Statics.GOAL){
			}
			else{
				//直前の"start"データが来るまで戻る
				T = t;
				while(this.logdata[T].getType() != Statics.START){
					T--;
				}
				//logdata[t]のタイプを一時的にstatusに変換し、学習が行われないようにする
				logdata[t].setType(Statics.STATUS);
				//学習を行う
				learnfromLog();
				//logdata[t]のタイプをgoalに戻す
				logdata[t].setType(Statics.GOAL);

				//推定出力を求める
				int[] prediction = new int[this.X.getDimension()];
				int[] ts = logdata_mat[T].getStepStatusField();

				for(int i=0;i<ts.length;i++){
					for(int j=0;j<ts.length;j++){
						prediction[i] += this.X.approximate().getData(i, j) * ts[j];
					}
				}

				//再現したpredictionとのずれを求める

				double distance = 0;
				for(int i=0;i<prediction.length;i++){
					distance += (prediction[i] - logdata_mat[t].getStepStatus(i)) * (prediction[i] - logdata_mat[t].getStepStatus(i));
				}
				distance = Math.sqrt(distance);

				output = (output * count + distance)/(count + 1);
				count++;
			}
		}
		//計算結果をresult.txtに出力する
		io.println(
				"result,"+output
				);
		io.execute();

		//盤面をいじくってしまっているので、最後にinitializeを実行する
		//条件を変えながら評価値を何度も計算する場合、initializeによるstartログ発生が煩わしいため、引数でinitializeを行わないようにできる
		if(initialize == true){
			frame.initialize();
		}
		System.out.println("おわったよぉ～～！");
		return output;
	}
	public double evaluate(MyFrame frame){
		return evaluate(frame,true);
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
