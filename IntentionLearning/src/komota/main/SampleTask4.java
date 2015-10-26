package komota.main;

import komota.pr.TestPatternRecognition;

public class SampleTask4 extends MainFrame{

	public static void main(String[] args){
		SampleTask4 task = new SampleTask4();
	}

	//解析クラス
	TestPatternRecognition tpr;

	//コンストラクタ
	public SampleTask4(){
		super();
		this.tpr = new TestPatternRecognition();
		this.tasktitle = "Move the RED to left of the BLUE.";
		setOutputFile("test4.txt");
		initialize();
	}

	@Override
	public void initialize(){

		for(int i=0;i<this.panels.length;i++){
			this.panels[i].setStatus(0);
		}

		this.panels[(int)(Math.random() * MainFrame.NUMBEROFPANEL * MainFrame.NUMBEROFPANEL)].setStatus(1);
		while(true){
			int temp = (int)(Math.random() * MainFrame.NUMBEROFPANEL * MainFrame.NUMBEROFPANEL);
			if(this.panels[temp].getStatus() != 1){
				this.panels[temp].setStatus(2);
				break;
			}
		}

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
