package komota.main;

import java.io.File;

import komota.lib.DataSetGenerator;
import komota.lib.MyIO;
import komota.lib.Statics;
import komota.old.PR_Mat;
import komota.old.PR_Mat_SOINN;
import komota.supers.MyFrame;
import komota.supers.MyPR_v2;


public class SampleTask_Mat extends MyFrame{

	public static void main(String[] args){
		@SuppressWarnings("unused")
		SampleTask_Mat task = new SampleTask_Mat();
	}

	/* ************************************************************************************************************** */
	/* ************************************************************************************************************** */
	/*
	 *12/23現在最新のブラッシュアップ版。
	 */
	/* ************************************************************************************************************** */
	/* ************************************************************************************************************** */

	//定数
	//識別時の未学習動作閾値
	final double THRESHOLD = 10;

	//解析クラス
//	TestPatternRecognition tpr;
	PR_Mat pr_mat;
	PR_Mat_SOINN pr_mat_soinn;

	MyPR_v2 pr_v2;

	//PR_100_FE pr_FE;

	//識別結果
	int[] highest;

	//識別実験で使用．あとで消す
	double atodekesu = -1;




	//コンストラクタ
	public SampleTask_Mat(){
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
//		setOutputFile("logdata.txt");
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


		this.panels[(int)(Math.random() * Statics.NUMBEROFPANEL)][(int)(Math.random() * Statics.NUMBEROFPANEL)].setStatus(1);
		while(true){
			int temp1 = (int)(Math.random() * Statics.NUMBEROFPANEL);
			int temp2 = (int)(Math.random() * Statics.NUMBEROFPANEL);
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
					}else if(temp3<0.75&&temp1<Statics.NUMBEROFPANEL-50&&this.panels[temp1+40][temp2].getStatus() == 0){
						this.panels[temp1+40][temp2].setStatus(3);
						break;
					}else if(temp3<1.1&&temp2<Statics.NUMBEROFPANEL-50&&this.panels[temp1][temp2+40].getStatus() == 0){
						this.panels[temp1][temp2+40].setStatus(3);
						break;
					}
				}
				break;
			}
		}
		while(true){
			int temp1 = (int)(Math.random() * Statics.NUMBEROFPANEL);
			int temp2 = (int)(Math.random() * Statics.NUMBEROFPANEL);
			if(this.panels[temp1][temp2].getStatus() == 0){
				this.panels[temp1][temp2].setStatus(4);
				break;
			}
		}
		while(true){
			int temp1 = (int)(Math.random() * Statics.NUMBEROFPANEL);
			int temp2 = (int)(Math.random() * Statics.NUMBEROFPANEL);
			if(this.panels[temp1][temp2].getStatus() == 0){
				this.panels[temp1][temp2].setStatus(5);
				break;
			}
		}
		while(true){
			int temp1 = (int)(Math.random() * Statics.NUMBEROFPANEL);
			int temp2 = (int)(Math.random() * Statics.NUMBEROFPANEL);
			if(this.panels[temp1][temp2].getStatus() == 0){
//				this.panels[temp1][temp2].setStatus(6);
				break;
			}
		}
		while(true){
			int temp1 = (int)(Math.random() * Statics.NUMBEROFPANEL);
			int temp2 = (int)(Math.random() * Statics.NUMBEROFPANEL);
			if(this.panels[temp1][temp2].getStatus() == 0){
//				this.panels[temp1][temp2].setStatus(7);
				break;
			}
		}
		while(true){
			int temp1 = (int)(Math.random() * Statics.NUMBEROFPANEL);
			int temp2 = (int)(Math.random() * Statics.NUMBEROFPANEL);
			if(this.panels[temp1][temp2].getStatus() == 0){
//				this.panels[temp1][temp2].setStatus(8);
				break;
			}
		}
		while(true){
			int temp1 = (int)(Math.random() * Statics.NUMBEROFPANEL);
			int temp2 = (int)(Math.random() * Statics.NUMBEROFPANEL);
			if(this.panels[temp1][temp2].getStatus() == 0){
//				this.panels[temp1][temp2].setStatus(9);
				break;
			}
		}

		this.outputStart();
	}
	@Override
	public void functionPlugin1(){
		System.out.println("パターン認識クラスのインスタンスを生成");
		this.tasktitle = "パターン認識クラスのインスタンスを生成";
		this.pr_mat = new PR_Mat();
		this.pr_mat_soinn = new PR_Mat_SOINN();
//		this.pr_mat = new PR_Mat("dataset/log_RIGHT_TO_BLUE.txt");
//		this.pr_mat = new PR_Mat("dataset/log_NEAR_BY_ORANGE.txt");
		System.out.println("インスタンスを生成しました");
		this.expranation = "インスタンスを生成しました";
	}
	@Override
	public void functionPlugin2(){
		System.out.println("ログデータから学習");
		this.tasktitle = "ログデータから学習";
		this.pr_mat.learnfromLog();
		//this.pr_FE.learnfromLog();
		this.expranation = "学習完了";
	}
	@Override
	public void functionPlugin3(){
		System.out.println("学習結果から、動作を再現");
		this.tasktitle = "学習結果から、動作を再現";
		this.expranation = "";
		this.pr_mat.reproduction(this);
	}
	@Override
	public void functionPlugin4(){
		System.out.println("パターン認識クラス(SOINN付き)のインスタンスを生成");
		this.pr_mat_soinn = new PR_Mat_SOINN();
//		this.pr_mat = new PR_Mat("dataset/log_RIGHT_TO_BLUE.txt");
//		this.pr_mat = new PR_Mat("dataset/log_NEAR_BY_ORANGE.txt");
		System.out.println("インスタンスを生成しました");
	}
	@Override
	public void functionPlugin5(){
		System.out.println("ログデータから学習");
		this.tasktitle = "ログデータから学習(SOINN)";
		this.pr_mat_soinn.learnfromLog();
		this.expranation = "学習完了";
		//this.pr_FE.learnfromLog();
	}
	@Override
	public void functionPlugin6(){
		System.out.println("学習結果から、動作を再現");
		this.tasktitle = "学習結果から、動作を再現";
		this.expranation = "";
		this.pr_mat_soinn.reproduction(this);
	}
	@Override
	public void functionPlugin7(){
		this.pr_v2 = new MyPR_v2(5,"logdata.txt");
		pr_v2.show();
	}

	/* 2016/ 4/10              ******************************************************************************************** */

	@Override
	public void functionPluginQ(){
		int old = Statics.NUMBEROFMATRIXS;
		for(int t=0;t<20;t++){
			Statics.NUMBEROFMATRIXS++;
			this.pr_mat_soinn.evaluate(this,false);
		}
		Statics.NUMBEROFMATRIXS = old;
	}
	@Override
	public void functionPluginW(){
		DataSetGenerator generator = new DataSetGenerator(300);
		generator.getMyIO().writeFile("outputfromRandomizer.txt");
		generator.functionPlugin1();
	}
	@Override
	public void functionPluginE(){
		//とりあえず，SOINNに入れるデータの数を増やすことで精度がどのように変化するかの実験を繰り返す
		//resultへの出力クラスを作っておく．（データ間のパーティションとか入れるため）
		MyIO io_E = new MyIO();
		io_E.writeFile("result.txt");
		//データセットジェネレータの生成
		DataSetGenerator generator = new DataSetGenerator(300);
		generator.setVisible(false);
		this.setVisible(false);

		for(int time = 0 ; time < 500 ; time++){

			io_E.println("*********************************************");
			io_E.execute();

			//新しいデータセットを作成するため，古いほうを消す
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
			//データジェネレート
			generator.getMyIO().writeFile("outputfromRandomizer.txt");
			generator.functionPlugin1();

			//実験
			//使用するデータの読みこみ先を変更しておく
			System.out.println("パターン認識クラスのインスタンスを生成");
			this.pr_mat_soinn = new PR_Mat_SOINN("outputfromRandomizer.txt");
			System.out.println("インスタンスを生成しました");
			this.functionPluginQ();

		}

	}
	@Override
	public void functionPluginR(){
	}
	@Override
	public void functionPluginT(){
	}
	@Override
	public void functionPluginY(){
	}

	/* ******************************************************************************************************************** */
}
