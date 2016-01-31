package komota.main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import komota.coordinate.Normalizer;
import komota.pr.main.PR_100_GL;
import komota.pr.main.PR_100_ID;
import komota.pr.main.PR_100_LT;
import komota.pr.main.PR_101;
import komota.test.DataSetGenerator;
import komota.test.LogRandomizer;

public class SampleTask_100s extends MySerialFrame{

	public static void main(String[] args){
		SampleTask_100s task = new SampleTask_100s();
	}

	/* ************************************************************************************************************** */
	/* ************************************************************************************************************** */
	/*
	 *12/23現在最新のブラッシュアップ版。
	 */
	/* ************************************************************************************************************** */
	/* ************************************************************************************************************** */

	//解析クラス
//	TestPatternRecognition tpr;
	PR_100_LT pr_LT;
	PR_100_ID pr_ID;
	PR_100_GL pr_GL;

	//PR_100_FE pr_FE;

	//タスククラス
	MyTaskPrimitive[] tasks;
	static final int NUMBEROFTASKS = 15;
	//盤面記憶用PRクラス(識別で使用)
	PR_101 save;
	//識別結果
	int[] highest;


	//コンストラクタ
	public SampleTask_100s(){
		super();
//		this.tpr = new TestPatternRecognition();
		this.tasktitle = "動作名";
		this.howtouse = "1,2:logdata学習 3:再現 4:識別用学習 5:押しちゃダメ 6:*** 7:ランダマイズ 8:順序問題,学習データ量テスト 9:データセット生成";
		setOutputFile("logdata.txt");
		initialize();
	}

	@Override
	public void initialize(){
		super.initialize();

		for(int i=0;i<this.panels.length;i++){
			for(int j=0;j<this.panels[0].length;j++){
				this.panels[i][j].setStatus(0);
			}
		}

		/*
		 * 環境中の物体配置は、以下のように定める
		 * 1. トラジェクタの位置をランダムに決定する
		 * 2. 物体2(青色のオブジェクト)の位置を、トラジェクタの位置以外でランダムに決定する
		 * 3. 物体3(黄色のオブジェクト)の位置を、物体2の上下左右にそれぞれ40マス離れた位置のうち、可能な位置からランダムに決定する
		 * 4. 物体4(緑色のオブジェクト)の位置を、現在物体が配置されていない位置からランダムに決定する
		 * 5. 物体5(橙色のオブジェクト)の位置を、現在物体が配置されていない位置からランダムに決定する
		 *
		 * つまり環境中には1つのトラジェクタと4つのオブジェクトが配置され、そのうち必ず上下左右40マスに隣り合う青色と黄色以外は独立にランダムで場所が決定する
		 */


		this.panels[(int)(Math.random() * MyFrame.NUMBEROFPANEL)][(int)(Math.random() * MyFrame.NUMBEROFPANEL)].setStatus(1);
		while(true){
			int temp1 = (int)(Math.random() * MyFrame.NUMBEROFPANEL);
			int temp2 = (int)(Math.random() * MyFrame.NUMBEROFPANEL);
			if(this.panels[temp1][temp2].getStatus() == 0){
				this.panels[temp1][temp2].setStatus(2);
				while(true){
					double temp3 = Math.random();
					if(temp3<0.25&&temp1>50&&this.panels[temp1-40][temp2].getStatus() == 0){
						this.panels[temp1-40][temp2].setStatus(3);
						break;
					}else if(temp3<0.5&&temp2>50&&this.panels[temp1][temp2-40].getStatus() == 0){
						this.panels[temp1][temp2-40].setStatus(3);
						break;
					}else if(temp3<0.75&&temp1<MyFrame.NUMBEROFPANEL-50&&this.panels[temp1+40][temp2].getStatus() == 0){
						this.panels[temp1+40][temp2].setStatus(3);
						break;
					}else if(temp3<1.1&&temp2<MyFrame.NUMBEROFPANEL-50&&this.panels[temp1][temp2+40].getStatus() == 0){
						this.panels[temp1][temp2+40].setStatus(3);
						break;
					}
				}
				break;
			}
		}
		while(true){
			int temp1 = (int)(Math.random() * MyFrame.NUMBEROFPANEL);
			int temp2 = (int)(Math.random() * MyFrame.NUMBEROFPANEL);
			if(this.panels[temp1][temp2].getStatus() == 0){
				this.panels[temp1][temp2].setStatus(4);
				break;
			}
		}
		while(true){
			int temp1 = (int)(Math.random() * MyFrame.NUMBEROFPANEL);
			int temp2 = (int)(Math.random() * MyFrame.NUMBEROFPANEL);
			if(this.panels[temp1][temp2].getStatus() == 0){
				this.panels[temp1][temp2].setStatus(5);
				break;
			}
		}
		while(true){
			int temp1 = (int)(Math.random() * MyFrame.NUMBEROFPANEL);
			int temp2 = (int)(Math.random() * MyFrame.NUMBEROFPANEL);
			if(this.panels[temp1][temp2].getStatus() == 0){
//				this.panels[temp1][temp2].setStatus(6);
				break;
			}
		}
		while(true){
			int temp1 = (int)(Math.random() * MyFrame.NUMBEROFPANEL);
			int temp2 = (int)(Math.random() * MyFrame.NUMBEROFPANEL);
			if(this.panels[temp1][temp2].getStatus() == 0){
//				this.panels[temp1][temp2].setStatus(7);
				break;
			}
		}
		while(true){
			int temp1 = (int)(Math.random() * MyFrame.NUMBEROFPANEL);
			int temp2 = (int)(Math.random() * MyFrame.NUMBEROFPANEL);
			if(this.panels[temp1][temp2].getStatus() == 0){
//				this.panels[temp1][temp2].setStatus(8);
				break;
			}
		}
		while(true){
			int temp1 = (int)(Math.random() * MyFrame.NUMBEROFPANEL);
			int temp2 = (int)(Math.random() * MyFrame.NUMBEROFPANEL);
			if(this.panels[temp1][temp2].getStatus() == 0){
//				this.panels[temp1][temp2].setStatus(9);
				break;
			}
		}

		this.outputStart();
	}
	@Override
	public void functionPlugin1(){
		System.out.println("各パターン認識クラスのインスタンスを生成");
		this.pr_LT = new PR_100_LT(9,this.file_name);
		this.pr_ID = new PR_100_ID(9,this.file_name);
		this.pr_GL = new PR_100_GL(9,this.file_name);
		//this.pr_FE = new PR_100_FE(9,this.file_name);
	}
	@Override
	public void functionPlugin2(){
		System.out.println("ログデータから学習");
		this.pr_LT.learnfromLog();
		this.pr_ID.learnfromLog();
		this.pr_GL.learnfromLog();
		//this.pr_FE.learnfromLog();
	}
	@Override
	public void functionPlugin3(){
		System.out.println("学習結果から、動作を再現");
		//System.out.println("[SampleTask_100s]functionPlugin3:MaxLikelihood: ID:"+this.pr_ID.getMaxLikelihood()+" LT:"+pr_LT.getMaxLikelihood()+" GL:"+pr_GL.getMaxLikelihood() + " FE:"+pr_FE.getMaxLikelihood());
		/*if(this.pr_FE.getMaxLikelihood() > 0){
			this.pr_FE.reproduction(this);
		}
		else */if(this.pr_GL.getMaxLikelihood() >= this.pr_LT.getMaxLikelihood() && this.pr_GL.getMaxLikelihood() >= this.pr_ID.getMaxLikelihood()){
			this.pr_GL.reproduction(this);
		}
		else if(this.pr_LT.getMaxLikelihood() >= this.pr_ID.getMaxLikelihood() && this.pr_LT.getMaxLikelihood() >= this.pr_GL.getMaxLikelihood()){
			this.pr_LT.reproduction(this);
		}
		else{
			this.pr_ID.reproduction(this);
		}
	}
	@Override
	public void functionPlugin4(){
		//識別用タスククラスのインスタンス生成
		System.out.println("[SampleTask_100s]functionPlugin4:タスククラスのインスタンス生成");
		this.expranation = "[SampleTask_100s]functionPlugin4:タスククラスのインスタンス生成";

		this.tasks = new MyTaskPrimitive[NUMBEROFTASKS];
		this.tasks[0] = new MyTaskPrimitive("log_MOVE_THE_CENTER.txt","赤を中央に移動する");
		this.tasks[1] = new MyTaskPrimitive("log_RIGHT_TO_BLUE.txt","赤を青の右に動かす");
		this.tasks[2] = new MyTaskPrimitive("log_NEAR_BY_ORANGE.txt","赤を橙に近づける");
		this.tasks[3] = new MyTaskPrimitive("log_AWAY_FROM_GREEN.txt","赤を緑から遠ざける");
		this.tasks[4] = new MyTaskPrimitive("log_MAKE_THE_SIGNAL.txt","等間隔に赤、黄、青と並べる");
		this.tasks[5] = new MyTaskPrimitive("log_MAKE_THE_TRIANGLE.txt","時計回りに赤、緑、青と並べる");
		this.tasks[6] = new MyTaskPrimitive("log_MORE_RIGHT_TO_ORANGE.txt","赤を橙の右に動かす");
		this.tasks[7] = new MyTaskPrimitive("log_MORE_RIGHT_TO_GREEN.txt","赤を緑の右に動かす");
		this.tasks[8] = new MyTaskPrimitive("log_MORE_RIGHT_TO_YELLOW.txt","赤を黄の右に動かす");
		this.tasks[9] = new MyTaskPrimitive("log_MORE_NEAR_BY_BLUE.txt","赤を青に近づける");
		this.tasks[10] = new MyTaskPrimitive("log_MORE_NEAR_BY_GREEN.txt","赤を緑に近づける");
		this.tasks[11] = new MyTaskPrimitive("log_MORE_NEAR_BY_YELLOW.txt","赤を黄に近づける");
		this.tasks[12] = new MyTaskPrimitive("log_MORE_AWAY_FROM_BLUE.txt","赤を青から遠ざける");
		this.tasks[13] = new MyTaskPrimitive("log_MORE_AWAY_FROM_ORANGE.txt","赤を橙から遠ざける");
		this.tasks[14] = new MyTaskPrimitive("log_MORE_AWAY_FROM_YELLOW.txt","赤を黄から遠ざける");
		//this.tasks[6] = new MyTaskPrimitive("log_TILT_RED_LITTLE.txt","赤を少し傾ける");
		//this.tasks[7] = new MyTaskPrimitive("log_TILT_RED_HARD.txt","赤を大きく傾ける");


		System.out.println("インスタンス生成完了");
		this.expranation = "インスタンス生成完了";

/*
		System.out.println("再現動作の評価値を計算");
		if(this.pr_ID.getMaxLikelihood() >= this.pr_LT.getMaxLikelihood() && this.pr_ID.getMaxLikelihood() >= this.pr_GL.getMaxLikelihood()){
			System.out.println("evaluation point:"+this.pr_ID.evaluate(this));
		}
		else if(this.pr_LT.getMaxLikelihood() >= this.pr_ID.getMaxLikelihood() && this.pr_LT.getMaxLikelihood() >= this.pr_GL.getMaxLikelihood()){
			System.out.println("evaluation point:"+this.pr_LT.evaluate(this));
		}
		else{
			System.out.println("evaluation point:"+this.pr_GL.evaluate(this));
		}
*/
	}
	@Override
	public void functionPlugin5(){
		System.out.println("再現動作の評価値を、学習に使用するデータ量を増やしながら計算");
		//どの座標系かを先に求める
		int index = -1;
		if(this.pr_ID.getMaxLikelihood() >= this.pr_LT.getMaxLikelihood() && this.pr_ID.getMaxLikelihood() >= this.pr_GL.getMaxLikelihood()){
			index = 0;
		}
		else if(this.pr_LT.getMaxLikelihood() >= this.pr_ID.getMaxLikelihood() && this.pr_LT.getMaxLikelihood() >= this.pr_GL.getMaxLikelihood()){
			index = 1;
		}
		else{
			index = 2;
		}

		MyPR.setNumberofEvaluation(2);
		//評価値が4回連続同じ値になるまで、データ量を増やす
		int count=0;
		double currentevaluationpoint = -1;
		double nextevaluationpoint = -1;
		while(true){
			currentevaluationpoint = nextevaluationpoint;
			if(index == 0){
				nextevaluationpoint = this.pr_ID.evaluate(this,false);
			}else if(index == 1){
				nextevaluationpoint = this.pr_LT.evaluate(this,false);
			}else{
				nextevaluationpoint = this.pr_GL.evaluate(this,false);
			}
			//一つ前の計算結果と同じか
			if(nextevaluationpoint == currentevaluationpoint){
				count++;
			}
			else{
				count = 0;
			}
			//countが10以上か(同じ計算結果が10回以上続いたか)
			if(count >= 10){
				break;
			}
			else{
				//使用するデータ量を増やす
				MyPR.setNumberofEvaluation(MyPR.getNumberofEvaluation()+1);
			}
		}
		this.initialize();
		System.out.println("計算が終了しました。logdataを確認してください");
	}
	@Override
	public void functionPlugin6(){
		/*
		 * 1. 現在の盤面と、直前のstartログの盤面を何らかの方法で保持する
		 * 2. 保持したstartログに盤面を合わせる
		 * 3. task_RtBでreproductionTask
		 * 4. secondselectedと、保持した最終状態の位置との距離をdis_RtBとして保存
		 * 5. タスクを切り替え、2. に戻る
		 * 6. disの最も小さいタスクのtasknameを出力する
		 */

				double[] dis_tasks = new double[NUMBEROFTASKS];
				for(int i=0;i<NUMBEROFTASKS;i++){
					dis_tasks[i] = 10000;
				}
				int[] tempselected = new int[2];
				this.save = new PR_101();
				this.save.setLog(this);
				for(int t=0;t<NUMBEROFTASKS;t++){
					this.save.loadLastStartLog(this);
					this.tasks[t].reproductionTask(this);
					tempselected = this.getSecondSelected();
					dis_tasks[t] = (tempselected[0] - this.save.getLastPosition()[0])*(tempselected[0] - this.save.getLastPosition()[0])+(tempselected[1] - this.save.getLastPosition()[1])*(tempselected[1] - this.save.getLastPosition()[1]);
					dis_tasks[t] = Math.sqrt(dis_tasks[t]);
				}
				//上位3タスクを取得
				int[] highest_idx = new int[3];
				double[] highest_point = new double[highest_idx.length];
				for(int i=0;i<highest_point.length;i++){
					highest_idx[i] = -1;
					highest_point[i] = 100000;
				}
				for(int t=0;t<NUMBEROFTASKS;t++){
					System.out.println(t+":"+this.tasks[t].taskname+":"+dis_tasks[t]);
					for(int i=0;i<highest_point.length;i++){
						if(dis_tasks[t]<highest_point[i]){
							for(int j=highest_point.length-1;j>i;j--){
								highest_idx[j] = highest_idx[j-1];
								highest_point[j] = highest_point[j-1];
							}
							highest_idx[i] = t;
							highest_point[i] = dis_tasks[t];
							break;
						}
					}
				}
				//上位3つのタスク名を標準出力
				//実験時はここをフィールドに持たせたりして別メソッドで評価する感じになると思う
				System.out.println("[SampleTask_100s]functionPlugin6:recognition result:");
				for(int i=0;i<highest_point.length;i++){
					System.out.println(i+". "+this.tasks[highest_idx[i]].taskname);
				}
				this.tasktitle = this.tasks[highest_idx[0]].taskname;
				this.highest = highest_idx;
				//一応、見栄えのためにsecondselectedを初期化しておく
				this.secondselected[0] = -1;
				this.secondselected[1] = -1;

/*
		System.out.println("再現動作の評価値を、視野を増やしながら計算");

		MyPR.setNumberofEvaluation(1000);
		PR_100.setViewRange(5);

		//評価値が4回連続同じ値になるまで、データ量を増やす
		int count=0;
		double currentevaluationpoint = -1;
		double nextevaluationpoint = -1;
		while(true){
			currentevaluationpoint = nextevaluationpoint;
			//PRインスタンスを再生成する(新しい視野で)
			this.functionPlugin1();
			//学習する
			this.functionPlugin2();
			//評価値を求める
			if(this.pr_ID.getMaxLikelihood() >= this.pr_LT.getMaxLikelihood() && this.pr_ID.getMaxLikelihood() >= this.pr_GL.getMaxLikelihood()){
				System.out.println("evaluation point:"+this.pr_ID.evaluate(this,false));
			}
			else if(this.pr_LT.getMaxLikelihood() >= this.pr_ID.getMaxLikelihood() && this.pr_LT.getMaxLikelihood() >= this.pr_GL.getMaxLikelihood()){
				System.out.println("evaluation point:"+this.pr_LT.evaluate(this,false));
			}
			else{
				System.out.println("evaluation point:"+this.pr_GL.evaluate(this,false));
			}
			//一つ前の計算結果と同じか
			if(nextevaluationpoint == currentevaluationpoint){
				count++;
			}
			else{
				count = 0;
			}
			//countが100以上か(同じ計算結果が100回以上続いたか)
			//ここは本来、「最尤値は閾値以上か」が入るとよさそう
			if(count >= 100){
				break;
			}
			else{
				//視野を増やす
				PR_100.setViewRange(PR_100.getViewRange()+5);
			}
		}
		this.initialize();
		System.out.println("計算が終了しました。logdataを確認してください");
*/
	}
	@Override
	public void functionPlugin7(){
		System.out.println("各パターン認識クラスのインスタンスを生成(データの読み込み順をランダマイズしてから)");
		LogRandomizer r = new LogRandomizer();
		r.randomize("log_NEAR_BY_ORANGE.txt", "outputfromRandomizer.txt");
		this.pr_LT = new PR_100_LT(9,"outputfromRandomizer.txt");
		this.pr_ID = new PR_100_ID(9,"outputfromRandomizer.txt");
		this.pr_GL = new PR_100_GL(9,"outputfromRandomizer.txt");
		File dfile = new File("log/outputfromRandomizer.txt");
		if (dfile.exists()){
			if (dfile.delete()){
				System.out.println("ファイルを削除しました");
			}else{
				System.out.println("ファイルの削除に失敗しました");
			}
		}else{
			System.out.println("ファイルが見つかりません");
		}
	}
	@Override
	public void functionPlugin8(){
		for(int t=0;t<30;t++){
			this.functionPlugin7();
			this.functionPlugin2();
			this.functionPlugin5();
		}
		LogRandomizer r = new LogRandomizer();
		r.encodeToCSV("logdata.txt", "encodeToCSV.csv");

		File dfile = new File("log/logdata.txt");
		if (dfile.exists()){
			if (dfile.delete()){
				System.out.println("ログファイルを削除しました");
			}else{
				System.out.println("ログファイルの削除に失敗しました");
			}
		}else{
			System.out.println("ファイルが見つかりません");
		}

	}
	@Override
	public void functionPlugin9(){
		//ランダムのログデータを生成
		System.out.println("データジェネレート！");
		DataSetGenerator g = new DataSetGenerator();
//		g.generate_MOVE_THE_CENTER(this, 2);
//		g.generate_RIGHT_TO_BLUE(this, 2);
//		g.generate_NEAR_BY_ORANGE(this, 2);
//		g.generate_AWAY_FROM_GREEN(this, 2);
//		g.generate_MAKE_THE_SIGNAL(this, 2);
//		g.generate_MAKE_THE_TRIANGLE(this, 2);
//		g.generate_RIGHT_TO_(DataSetGenerator.YELLOW, this, 2);
//		g.generate_NEAR_BY_(DataSetGenerator.YELLOW, this, 2);
//		g.generate_AWAY_FROM_(DataSetGenerator.YELLOW, this, 2);
		g.generate_MAKE_THE_SIG_(DataSetGenerator.YELLOW, DataSetGenerator.GREEN, this, 2);
//		g.generate_MAKE_THE_TRI_(DataSetGenerator.YELLOW, DataSetGenerator.BLUE, this, 2);
		System.out.println("データジェネレート完了！");

	}
	@Override
	public void functionPluginQ(){
		//まず、logdataを削除するテスト
		System.out.println("ちょっと待ってね～");
		//functionPlugin9でデータ生成
		this.functionPlugin9();
		//functionPlugin1,2,3で学習、再現し、データが存在していたことを確認
		this.functionPlugin1();
		this.functionPlugin2();
		this.functionPlugin3();
		//logdataを削除
		this.pw.close();

		File file = new File("log/"+this.file_name);

		if (file.exists()){
			if (file.delete()){
				System.out.println("ログファイルを削除しました");
			}else{
				System.out.println("ログファイルの削除に失敗しました");
			}
		}else{
			System.out.println("ファイルが見つかりません");
		}
		//logdataを生成
		this.setOutputFile("logdata.txt");
		//ほかのキーを押し、logdata.txtが問題なく使えるか確認

		//結果を出力するファイルの生成
	      PrintWriter pw_Q = null;
	      FileOutputStream fos_Q = null;
		try {
		      fos_Q = new FileOutputStream("log/output_Q.txt",true);
		      OutputStreamWriter osw_Q = new OutputStreamWriter(fos_Q);
		      pw_Q = new PrintWriter(osw_Q);
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			System.out.println("ファイルなし");
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e1) {
				// TODO 自動生成された catch ブロック
				e1.printStackTrace();
			}
			e.printStackTrace();
		}

		//誤差を0～10まで（0.5刻み）変化させて再現誤差を評価する
		DataSetGenerator g = new DataSetGenerator();
		for(int error=0;error<21;error++){
			//各誤差を50回ずつ計算
			for(int t=0;t<50;t++){
				this.pw.println("result,error:"+(double)error/2);
				add_Q(g,(double)error/2);
				this.functionPlugin1();
				this.functionPlugin2();
				add_Q_Learn((double)error/2,pw_Q);
				//logdataを削除
				this.pw.close();
				file = new File("log/"+this.file_name);
				if (file.exists()){
					if (file.delete()){
						System.out.println("ログファイルを削除しました");
					}else{
						System.out.println("ログファイルの削除に失敗しました");
					}
				}else{
					System.out.println("ファイルが見つかりません");
				}
				//logdataを生成
				this.setOutputFile("logdata.txt");
			}
			pw_Q.println("padding,999");
		}
		pw_Q.close();
		try {
			fos_Q.close();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		LogRandomizer r = new LogRandomizer();
		r.encodeToCSV("output_Q.txt", "output_Q.csv");
		System.out.println("計算終わったよ～");
	}
	/* ************************************************************************************************************* */
	//functionPluginQ(誤差を変化させながらの再現誤差計算)の内部で生成する教示動作の種類を選択するメソッド。
	//ここを変更することで異なる動作の評価ができる
	private void add_Q(DataSetGenerator g,double variance){
		//g.generate_MOVE_THE_CENTER(this, variance);
		//g.generate_NEAR_BY_ORANGE(this, variance);
		//g.generate_RIGHT_TO_BLUE(this, variance);
		//g.generate_AWAY_FROM_GREEN(this, variance);
		//g.generate_MAKE_THE_SIGNAL(this, variance);
		g.generate_MAKE_THE_TRIANGLE(this, variance);
	}
	/* *************************************************** */
	private void add_Q_Learn(double error,PrintWriter pw){
		System.out.println("再現動作の評価値を計算");
		//どの座標系かを先に求める
		int index = -1;
		if(this.pr_ID.getMaxLikelihood() >= this.pr_LT.getMaxLikelihood() && this.pr_ID.getMaxLikelihood() >= this.pr_GL.getMaxLikelihood()){
			index = 0;
		}
		else if(this.pr_LT.getMaxLikelihood() >= this.pr_ID.getMaxLikelihood() && this.pr_LT.getMaxLikelihood() >= this.pr_GL.getMaxLikelihood()){
			index = 1;
		}
		else{
			index = 2;
		}

		double evaluationpoint = 0;
		MyPR.setNumberofEvaluation(1000);
			if(index == 0){
				evaluationpoint = this.pr_ID.evaluate(this,false);
			}else if(index == 1){
				evaluationpoint = this.pr_LT.evaluate(this,false);
			}else{
				evaluationpoint = this.pr_GL.evaluate(this,false);
			}
		this.initialize();
		System.out.println("計算が終了しました。logdataを確認してください");
		pw.println("result,"+evaluationpoint+","+error);
	}
	/* ************************************************************************************************************* */

	@Override
	public void functionPluginW(){
		DataSetGenerator g = new DataSetGenerator();
		int errorcount = 0;
		//識別の実験のテスト
		System.out.println("識別テストはじめるよぉ～");
		this.functionPlugin4();

		//結果を出力するファイルの生成
	      PrintWriter pw_W = null;
	      FileOutputStream fos_W = null;
		try {
		      fos_W = new FileOutputStream("log/output_W.txt",true);
		      OutputStreamWriter osw_W = new OutputStreamWriter(fos_W);
		      pw_W = new PrintWriter(osw_W);
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			System.out.println("ファイルなし");
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e1) {
				// TODO 自動生成された catch ブロック
				e1.printStackTrace();
			}
			e.printStackTrace();
		}

		//各動作で識別の実験
		for(int taskidx=0;taskidx<this.tasks.length;taskidx++){
			errorcount = 0;
			switch(taskidx){
			case 0:
				pw_W.println("task,MOVE_THE_CENTER");
				break;
			case 1:
				pw_W.println("task,RIGHT_TO_BLUE");
				break;
			case 2:
				pw_W.println("task,NEAR_BY_ORANGE");
				break;
			case 3:
				pw_W.println("task,AWAY_FROM_GREEN");
				break;
			case 4:
				pw_W.println("task,MAKE_THE_SIGNAL");
				break;
			case 5:
				pw_W.println("task,MAKE_THE_TRIANGLE");
				break;
			}

			for(int t=0;t<100;t++){
				//logdataを削除
				this.pw.close();
				File file = new File("log/"+this.file_name);
				if (file.exists()){
					if (file.delete()){
						System.out.println("ログファイルを削除しました");
					}else{
						System.out.println("ログファイルの削除に失敗しました");
					}
				}else{
					System.out.println("ファイルが見つかりません");
				}
				//logdataを生成
				this.setOutputFile("logdata.txt");
				//ジェネレート
				//識別誤差は分散10とする
				double var = 10;
				switch(taskidx){
				case 0:
					g.generate_MOVE_THE_CENTER(this, var);
					break;
				case 1:
					g.generate_RIGHT_TO_BLUE(this, var);
					break;
				case 2:
					g.generate_NEAR_BY_ORANGE(this, var);
					break;
				case 3:
					g.generate_AWAY_FROM_GREEN(this, var);
					break;
				case 4:
					g.generate_MAKE_THE_SIGNAL(this, var);
					break;
				case 5:
					g.generate_MAKE_THE_TRIANGLE(this, var);
					break;
				}
				//saveインスタンス生成
				this.save = new PR_101();
				//saveのlogdataの最後のgoalをarrangeField
				int idx = 0;
				while(true){
					if(this.save.logdata[idx] == null){
						break;
					}
					else if(this.save.logdata[idx].getType() == MyPR.GOAL){
						this.save.arrangeField(this, this.save.logdata[idx]);
					}
					idx++;
				}
				//functionPlugin6
				this.functionPlugin6();
				if(this.highest[0] != taskidx){
					System.out.println(t+"回目で識別に失敗しました。");
					//エラーが生じた初期状態を出力
					save.loadLastStartLog(this);
					pw_W.print("start ");
					for(int i=0;i<MyFrame.NUMBEROFPANEL;i++){
						for(int j=0;j<MyFrame.NUMBEROFPANEL;j++){
							pw_W.print(","+this.panels[i][j].status);
						}
					}
					pw_W.println("");
					//最終状態も出力
					pw_W.println("moved ,"+save.getLastPosition()[0]+","+save.getLastPosition()[1]);
					//何と間違えたのか
					pw_W.println("suggest,"+highest[0]);
					errorcount++;
				}
			}
			System.out.println("誤識別は " + errorcount + " 回ありました");
			pw_W.println("result,"+errorcount);
		}
		pw_W.close();
		try {
			fos_W.close();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		System.out.println("おまたせ！計算終わったよー！");
	}
	@Override
	public void functionPluginE(){
		//特徴量ベクトルで描画状態を変えられているかのテスト。消してよい
		double[] temp = null;
		for(int i=0;i<MyFrame.NUMBEROFPANEL;i++){
			for(int j=0;j<MyFrame.NUMBEROFPANEL;j++){
				if(this.panels[i][j].getStatus() == 1){
					temp = this.panels[i][j].getFeatures();
					break;
				}
			}
		}
		temp[0] += 0.1;
		System.out.println(temp[0]+","+temp[1]+","+temp[2]);
		this.outputStatus();
	}
	@Override
	public void functionPluginR(){
		//まず、logdataを削除するテスト
		System.out.println("正規化長変える実験するよ～。ちょっと待ってね～");
		//フレームの描画を終了する。これだけで劇的に早くなったしバカじゃないの？
		this.t.cancel();
		//functionPlugin9でデータ生成
/*
		this.functionPlugin9();
		//functionPlugin1,2,3で学習、再現し、データが存在していたことを確認
		this.functionPlugin1();
		this.functionPlugin2();
		this.functionPlugin3();
*/
		//logdataを削除
		this.pw.close();

		File file = new File("log/"+this.file_name);

		if (file.exists()){
			if (file.delete()){
				System.out.println("ログファイルを削除しました");
			}else{
				System.out.println("ログファイルの削除に失敗しました");
			}
		}else{
			System.out.println("ファイルが見つかりません");
		}
		//logdataを生成
		this.setOutputFile("logdata.txt");
		//ほかのキーを押し、logdata.txtが問題なく使えるか確認

		//結果を出力するファイルの生成
	      PrintWriter pw_R = null;
	      FileOutputStream fos_R = null;
		try {
		      fos_R = new FileOutputStream("log/output_R.txt",true);
		      OutputStreamWriter osw_R = new OutputStreamWriter(fos_R);
		      pw_R = new PrintWriter(osw_R);
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			System.out.println("ファイルなし");
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e1) {
				// TODO 自動生成された catch ブロック
				e1.printStackTrace();
			}
			e.printStackTrace();
		}

		double oldunit = Normalizer.getUNIT();

		//正規化長を1～50まで（1刻み）変化させて再現誤差を評価する
		DataSetGenerator g = new DataSetGenerator();
		for(int unit=1;unit<51;unit++){
			Normalizer.setUNIT(unit);
			//各誤差を50回ずつ計算
			for(int t=0;t<50;t++){
				this.pw.println("result,UNIT:"+(double)unit);
				//教示誤差は,動作再現実験時に差が出始めている分散6とする
				add_R(g,10);
				if(t == 0){
					System.out.println("次は正規化長"+unit+"だよ!");
				}
				this.functionPlugin1();
				this.functionPlugin2();
				add_R_Learn((double)unit,pw_R);
				//logdataを削除
				this.pw.close();
				file = new File("log/"+this.file_name);
				if (file.exists()){
					if (file.delete()){
						System.out.println("ログファイルを削除しました");
					}else{
						System.out.println("ログファイルの削除に失敗しました");
					}
				}else{
					System.out.println("ファイルが見つかりません");
				}
				//logdataを生成
				this.setOutputFile("logdata.txt");
			}
			pw_R.println("padding,999");
		}
		pw_R.close();
		try {
			fos_R.close();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		LogRandomizer r = new LogRandomizer();
		r.encodeToCSV("output_R.txt", "output_R.csv");
		Normalizer.setUNIT(oldunit);
		System.out.println("計算終わったよ～");
	}
	/* ************************************************************************************************************* */
	//functionPluginQ(誤差を変化させながらの再現誤差計算)の内部で生成する教示動作の種類を選択するメソッド。
	//ここを変更することで異なる動作の評価ができる
	private void add_R(DataSetGenerator g,double variance){
		//g.generate_MOVE_THE_CENTER(this, variance);
		//g.generate_NEAR_BY_ORANGE(this, variance);
		//g.generate_RIGHT_TO_BLUE(this, variance);
		//g.generate_AWAY_FROM_GREEN(this, variance);
		g.generate_MAKE_THE_SIGNAL(this, variance);
		//g.generate_MAKE_THE_TRIANGLE(this, variance);
	}
	/* *************************************************** */
	private void add_R_Learn(double unit,PrintWriter pw){
		System.out.println("再現動作の評価値を計算");
		//どの座標系かを先に求める
		int index = -1;
		if(this.pr_ID.getMaxLikelihood() >= this.pr_LT.getMaxLikelihood() && this.pr_ID.getMaxLikelihood() >= this.pr_GL.getMaxLikelihood()){
			index = 0;
		}
		else if(this.pr_LT.getMaxLikelihood() >= this.pr_ID.getMaxLikelihood() && this.pr_LT.getMaxLikelihood() >= this.pr_GL.getMaxLikelihood()){
			index = 1;
		}
		else{
			index = 2;
		}

		double evaluationpoint = 0;
		MyPR.setNumberofEvaluation(10);
			if(index == 0){
				evaluationpoint = this.pr_ID.evaluate(this,false);
			}else if(index == 1){
				evaluationpoint = this.pr_LT.evaluate(this,false);
			}else{
				evaluationpoint = this.pr_GL.evaluate(this,false);
			}
		this.initialize();
		System.out.println("計算が終了しました。logdataを確認してください");
		pw.println("result,"+evaluationpoint+","+unit);
	}
	/* ************************************************************************************************************* */


/*
	@Override
	public void functionPluginR(){
		System.out.println("特徴量遷移の実験");
		this.pr_FE = new PR_100_FE(9,"logdata.txt");
		this.pr_FE.learnfromLog();
		this.pr_FE.reproduction(this);
		System.out.println("特徴量PRの最尤値は"+this.pr_FE.getMaxLikelihood()+"デス");
		System.out.println("PR_IDの最尤値は"+this.pr_ID.getMaxLikelihood()+"デス");
		System.out.println("PR_LTの最尤値は"+this.pr_LT.getMaxLikelihood()+"デス");
		System.out.println("PR_GLの最尤値は"+this.pr_GL.getMaxLikelihood()+"デス");
		System.out.println("実験終了");
	}
*/
}
