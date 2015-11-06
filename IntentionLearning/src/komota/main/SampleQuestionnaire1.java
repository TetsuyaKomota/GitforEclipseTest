package komota.main;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

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

		this.patterns = new Pattern[100];

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
		this.patterns[3] = new Pattern("これらのパネルを動かして、下に記された\"タスク\"を達成してください(2で進む)","タスク：赤　を　青　の左に動かす ←これ");
		this.patterns[3].setPattern(1,5,1);
		this.patterns[3].setPattern(10,15,2);
		this.patterns[3].setPattern(9,12,3);
		this.patterns[4] = new Pattern("色付きの\"オブジェクト\"を、白色の\"空きパネル\"に移動することができます。赤いオブジェクトをクリックしてください(2で進む)","タスク：赤　を　青　の左に動かす");
		this.patterns[4].setPattern(1,5,1);
		this.patterns[4].setPattern(10,15,2);
		this.patterns[4].setPattern(9,12,3);
		this.patterns[5] = new Pattern("青のオブジェクトのすぐ左の空きパネルをクリックしてください(2で進む)","タスク：赤　を　青　の左に動かす");
		this.patterns[5].setPattern(1,5,1);
		this.patterns[5].setPattern(10,15,2);
		this.patterns[5].setPattern(9,12,3);
		this.patterns[5].setAnswer(1, 5, -1, -1);
		this.patterns[6] = new Pattern("\"オブジェクト\"と\"空きパネル\"が選択されている状態になったら、スペースキーを押してください(2で進む)","タスク：赤　を　青　の左に動かす");
		this.patterns[6].setPattern(1,5,1);
		this.patterns[6].setPattern(10,15,2);
		this.patterns[6].setPattern(9,12,3);
		this.patterns[6].setAnswer(1, 5, 10, 14);
		this.patterns[7] = new Pattern("これで\"オブジェクト\"が移動できました。(2で進む)","タスク：赤　を　青　の左に動かす");
		this.patterns[7].setPattern(10,14,1);
		this.patterns[7].setPattern(10,15,2);
		this.patterns[7].setPattern(9,12,3);
		this.patterns[7].setAnswer(-1,-1,-1,-1);
		this.patterns[8] = new Pattern("\"タスク：赤　を　青　の左に動かす\" が達成できたので、gを入力してください。(画面上では何も起こりません)(2で進む)","タスク：赤　を　青　の左に動かす");
		this.patterns[8].setPattern(10,14,1);
		this.patterns[8].setPattern(10,15,2);
		this.patterns[8].setPattern(9,12,3);
		this.patterns[8].setAnswer(-1,-1,-1,-1);
		this.patterns[9] = new Pattern("では、次のタスクをやってみてください。完了したらgを一回押した後、2で進んでください。(gを押した時の挙動は仕様なので気にしないでください)(2で進む)","タスク：赤　を　黄　の上に動かす");
		this.patterns[9].setPattern(12,6,1);
		this.patterns[9].setPattern(18,17,2);
		this.patterns[9].setPattern(13,1,3);
		this.patterns[9].setAnswer(-1,-1,-1,-1);
		this.patterns[10] = new Pattern("では、次のタスクもやってみてください。完了したらgを一回押した後、2で進んでください(2で進む)","タスク：赤　を　アレ　の上に動かす");
		this.patterns[10].setPattern(0,6,1);
		this.patterns[10].setPattern(19,7,2);
		this.patterns[10].setPattern(10,10,3);
		this.patterns[10].setPattern(8,1,4);
		this.patterns[10].setAnswer(-1,-1,-1,-1);
		this.patterns[10].isneedtext = true;
		this.patterns[11] = new Pattern("では、次のタスクもやってみてください。完了したらgを一回押した後、2で進んでください(2で進む)","タスク：アレ　を　アレ　の上に動かす");
		this.patterns[11].setPattern(5,5,1);
		this.patterns[11].setPattern(15,7,2);
		this.patterns[11].setPattern(10,15,3);
		this.patterns[11].setPattern(8,13,4);
		this.patterns[11].setPattern(9,13,5);
		this.patterns[11].setPattern(10,10,6);
		this.patterns[11].setPattern(8,1,7);
		this.patterns[11].setAnswer(-1,-1,-1,-1);
		this.patterns[11].isneedtext = true;
		this.patterns[12] = new Pattern("お疲れ様でした。これでアンケート終了です","ご協力ありがとうございました");
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
		this.patterns[this.currentpattern].answer();
		this.pw.println("page ,"+this.currentpattern);
		//this.outputStart();
	}

	//gを押した時、必要に応じてテキスト入力フォームを表示するためのオーバーライド
	@Override
	public void pushGoal(){
		super.pushGoal();
		this.patterns[this.currentpattern].getTextForm();

		this.currentpattern++;
		this.initialize();
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
		//gを押した時にテキスト入力が必要なページかどうか
		boolean isneedtext = false;

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
		void answer(){
			if(this.ans[0] != -1 && this.ans[1] != -1){
				SampleQuestionnaire1.this.selected = ans;
			}
			else{
				SampleQuestionnaire1.this.selected[0] = -1;
				SampleQuestionnaire1.this.selected[1] = -1;
			}
			if(this.secondans[0] != -1 && this.secondans[1] != -1){
				SampleQuestionnaire1.this.secondselected = secondans;
			}
			else{
				SampleQuestionnaire1.this.secondselected[0] = -1;
				SampleQuestionnaire1.this.secondselected[1] = -1;
			}
		}
		//gを押した時に、テキスト入力を要求するページの場合、入力フォームを表示する
		void getTextForm(){
			if(this.isneedtext){
				TextForm frame = new TextForm("解説");
				frame.setVisible(true);
			}
		}
	}


	//テキスト入力フレームの内部クラス
	class TextForm extends JFrame{

		  JTextArea text2 = null;

		  TextForm(String title){
		    setTitle(title);
		    setBounds(100, 100, 300, 250);
		    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		    JPanel p = new JPanel();

		    JLabel label1 = new JLabel();
		    label1.setText("なぜ、そのように動かしましたか？");
		    this.text2 = new JTextArea(6, 20);


		    JButton next = new JButton("次へ進む");
		    next.addActionListener(new MyActionListener());

		    p.add(label1);
		//   p.add(text1);
		    p.add(text2);
//		    p.add(text3);
		    p.add(next);
		    Container contentPane = getContentPane();
		    contentPane.add(p, BorderLayout.CENTER);
		  }
		  //入力文をログに残す
		  void outputText(String text){
			  SampleQuestionnaire1.this.pw.println("comment,"+text);
			  //次のページに進む
			  SampleQuestionnaire1.this.currentpattern++;
			  SampleQuestionnaire1.this.initialize();
		  }


		  class MyActionListener implements ActionListener{

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO 自動生成されたメソッド・スタブ
					TextForm.this.outputText(TextForm.this.text2.getText());
					TextForm.this.dispose();
				}

		  }
		}

	/* ****************************************************************************************************************** */

}
