package komota.main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import komota.pr.main.PR_100;
import komota.pr.main.PR_100_GL;
import komota.pr.main.PR_100_ID;
import komota.pr.main.PR_100_LT;
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

	//コンストラクタ
	public SampleTask_100s(){
		super();
//		this.tpr = new TestPatternRecognition();
		this.tasktitle = "動作名";
		setOutputFile("logdata.txt");
		initialize();
	}

	@Override
	public void initialize(){

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
	}
	@Override
	public void functionPlugin2(){
		System.out.println("ログデータから学習");
		this.pr_LT.learnfromLog();
		this.pr_ID.learnfromLog();
		this.pr_GL.learnfromLog();
	}
	@Override
	public void functionPlugin3(){
		System.out.println("学習結果から、動作を再現");
		System.out.println("[SampleTask_100s]functionPlugin3:MaxLikelihood: ID:"+this.pr_ID.getMaxLikelihood()+" LT:"+pr_LT.getMaxLikelihood()+" GL:"+pr_GL.getMaxLikelihood());
		if(this.pr_GL.getMaxLikelihood() >= this.pr_LT.getMaxLikelihood() && this.pr_GL.getMaxLikelihood() >= this.pr_ID.getMaxLikelihood()){
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
//		g.generate_MOVE_THE_CENTER(this, 1);
		g.generate_RIGHT_TO_BLUE(this, 3);
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
		if (this.file.exists()){
			if (this.file.delete()){
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
		try {
		      FileOutputStream fos_Q = new FileOutputStream("log/output_Q.txt",true);
		      OutputStreamWriter osw_Q = new OutputStreamWriter(fos_Q);
		      pw_Q = new PrintWriter(osw_Q);
		      //fos_Q.close();
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


		//誤差を0～10まで変化させて再現誤差を評価する
		DataSetGenerator g = new DataSetGenerator();
		for(int error=0;error<11;error++){
			//各誤差を10回ずつ計算
			for(int t=0;t<10;t++){
				this.pw.println("result,error:"+error);
				add_Q(g,error);
				this.functionPlugin1();
				this.functionPlugin2();
				add_Q_Learn(error,pw_Q);
				//logdataを削除
				this.pw.close();
				if (this.file.exists()){
					if (this.file.delete()){
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
		}
		pw_Q.close();
		System.out.println("計算終わったよ～");
	}
	/* ************************************************************************************************************* */
	//functionPluginQ(誤差を変化させながらの再現誤差計算)の内部で生成する教示動作の種類を選択するメソッド。
	//ここを変更することで異なる動作の評価ができる
	private void add_Q(DataSetGenerator g,double variance){
		g.generate_MOVE_THE_CENTER(this, variance);
	}
	/* *************************************************** */
	private void add_Q_Learn(int error,PrintWriter pw){
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
		//評価値が4回連続同じ値になるまで、データ量を増やす
			if(index == 0){
				evaluationpoint = this.pr_ID.evaluate(this,false);
			}else if(index == 1){
				evaluationpoint = this.pr_LT.evaluate(this,false);
			}else{
				evaluationpoint = this.pr_GL.evaluate(this,false);
			}
		this.initialize();
		System.out.println("計算が終了しました。logdataを確認してください");
		pw.println("result,"+error+","+evaluationpoint);
	}
	/* ************************************************************************************************************* */

}
