package komota.main;

import komota.pr.test.TestPR1;

public class SampleQuestionnaire1 extends MyFrame{

	public static void main(String[] args){
		SampleQuestionnaire1 task = new SampleQuestionnaire1();
	}

	//フレーム問題に関するアンケートクラス




	//解析クラス
	TestPR1 tpr1;

	//初期状態クラス
	Pattern[] patterns = null;

	//現在選択中の初期状態
	int currentpattern = 0;

	//コンストラクタ
	public SampleQuestionnaire1(){
		super();
		this.tasktitle = "このメッセージはでないはずだよ";
		this.howtouse = "操作キー 1:戻る 2:進む 3:? 4:?   z:選択取り消し space:オブジェクト移動 g:タスク完了";
		setOutputFile("test5.txt");

		this.patterns = new Pattern[10];

		/* ************************************************************************************************************************************* */
		//各ページのテキストと初期状態をここに書く
		this.patterns[0] = new Pattern("アンケートにご協力ありがとうございます。2を押してください。","");
		this.patterns[1] = new Pattern("操作方法の説明を行います(2で進む)","タスク：赤　を　青　の左に動かす");
		this.patterns[1].setPattern(1,5,1);
		this.patterns[1].setPattern(10,15,2);
		this.patterns[1].setPattern(9,12,3);
		this.patterns[2] = new Pattern("これから下の400個のパネルに、この様な色付きのパネルが配置されます(2で進む)","タスク：赤　を　青　の左に動かす               ↓↓↓↓↓これ↓↓↓↓↓");
		this.patterns[2].setPattern(1,5,1);
		this.patterns[2].setPattern(10,15,2);
		this.patterns[2].setPattern(9,12,3);
		/* ************************************************************************************************************************************* */

		initialize();
	}

	@Override
	public void initialize(){
		if(this.patterns == null){
			return;
		}
		this.tasktitle = this.patterns[this.currentpattern].getTitle();
		this.expranation = this.patterns[this.currentpattern].getExpranation();
		for(int i=0;i<this.panels.length;i++){
			for(int j=0;j<this.panels[0].length;j++){
				this.panels[i][j].setStatus(this.patterns[this.currentpattern].getPattern()[i][j]);
			}
		}

		//this.outputStart();
	}
	@Override
	public void functionPlugin1(){
		if(this.currentpattern > 0){
			this.currentpattern--;
			initialize();
		}
	}
	@Override
	public void functionPlugin2(){
		if(this.currentpattern < this.patterns.length - 1 && this.patterns[this.currentpattern+1] != null){
			this.currentpattern++;
			initialize();
		}
	}
	@Override
	public void functionPlugin3(){
//		System.out.println("reproduction.");
//		this.tpr1.reproduction(this);
	}
	@Override
	public void functionPlugin4(){
//		System.out.println("bye");
//		this.tpr1.close();
	}
	/*
	 *
	 * １．出力先ファイル名をfile_nameに代入
	 * ２．タスク名をtasktitleに代入
	 * ３．initializeをオーバーライド。最終行にoutputStart()
	 *
	 */

	/* ****************************************************************************************************************** */
	//各タスクの初期状態とタスク名などを持たせた内部クラス
	class Pattern{
		//フィールド
		//説明文
		String expranation = null;
		//タスク名
		String title = null;
		//初期状態
		int[][] initialtable = null;
		//そのタスクの答え。教示データの場合は答えを表示できるようにする
		int[] ans = null;
		int[] secondans = null;

		//コンストラクタ
		Pattern(String expranation,String title){
			this.expranation = expranation;
			this.title = title;
			this.initialtable = new int[MyFrame.NUMBEROFPANEL][MyFrame.NUMBEROFPANEL];
			for(int i=0;i<MyFrame.NUMBEROFPANEL;i++){
				for(int j=0;j<MyFrame.NUMBEROFPANEL;j++){
					this.initialtable[i][j] = 0;
				}
			}
			this.ans = new int[2];
			this.secondans = new int[2];
			this.ans[0] = -1;
			this.ans[1] = -1;
			this.secondans[0] = -1;
			this.secondans[1] = -1;
		}

		//セッター、ゲッター。初期状態のセッターは各パネルごとに行う
		void setPattern(int gyou,int retsu,int status){
			this.initialtable[gyou][retsu] = status;
		}
		int[][] getPattern(){
			return this.initialtable;
		}
		String getTitle(){
			return this.title;
		}
		String getExpranation(){
			return this.expranation;
		}
		void setAnswer(int ansg,int ansr,int secansg,int secansr){
			this.ans[0] = ansg;
			this.ans[1] = ansr;
			this.secondans[0] = secansg;
			this.secondans[1] = secansr;
		}
	}


	/* ****************************************************************************************************************** */

}
