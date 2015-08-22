package komota.main;

import komota.pr.TestPatternRecognition;

public class SampleTask3 extends MainFrame{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		SampleTask3 task = new SampleTask3();
	}

	//解析クラス
	TestPatternRecognition tpr;

	//コンストラクタ
	public SampleTask3(){
		super();
		this.tpr = new TestPatternRecognition();
		this.tasktitle = "Move the RED left.";
		setOutputFile("test3.txt");
		initialize();
	}

	@Override
	public void initialize(){

		for(int i=0;i<this.panels.length;i++){
			this.panels[i].setStatus(0);
		}

		int rand = (int)(Math.random() * 9);
		this.panels[rand].setStatus(1);

		this.outputStart();
	}
	@Override
	public void functionPlugin1(){
		System.out.println("additional function.");
		this.tpr.testOutput();
		this.tpr.testConvert(this);
	}
	public void functionPlugin2(){
		System.out.println("bye");
		this.tpr.close();
	}
	/*
	 *
	 * １．出力先ファイル名をfile_nameに代入
	 * ２．タスク名をtasktitleに代入
	 * ３．initializeをオーバーライド。最終行にoutputStart()
	 *
	 */
}
