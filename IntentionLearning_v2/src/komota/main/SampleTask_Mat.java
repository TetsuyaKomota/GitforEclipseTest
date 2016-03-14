package komota.main;


public class SampleTask_Mat extends MyFrame{

	public static void main(String[] args){
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
//		setOutputFile("logdata.txt");
		initialize();
	}

	@Override
	public void initialize(){
		super.initialize();
		setOutputFile("logdata.txt");
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
		System.out.println("各パターン認識クラスのインスタンスを生成");
		this.pr_mat = new PR_Mat();
		//this.pr_FE = new PR_100_FE(9,this.file_name);
	}
	@Override
	public void functionPlugin2(){
		System.out.println("ログデータから学習");
		this.pr_mat.learnfromLog();
		//this.pr_FE.learnfromLog();
	}
	@Override
	public void functionPlugin3(){
		System.out.println("学習結果から、動作を再現");
		this.pr_mat.reproduction(this);
	}
}
