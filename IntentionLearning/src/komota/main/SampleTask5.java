package komota.main;

import komota.pr.TestPatternRecognition;

public class SampleTask5 extends MyFrame{

	public static void main(String[] args){
		SampleTask5 task = new SampleTask5();
	}

	//解析クラス
	TestPatternRecognition tpr;

	//コンストラクタ
	public SampleTask5(){
		super();
		this.tpr = new TestPatternRecognition();
		this.tasktitle = "55555Move the RED to left of the BLUE.";
		setOutputFile("test4.txt");
		initialize();
	}

	@Override
	public void initialize(){

		for(int i=0;i<this.panels.length;i++){
			for(int j=0;j<this.panels[0].length;j++){
				this.panels[i][j].setStatus(0);
			}
		}

		this.panels[(int)(Math.random() * MyFrame.NUMBEROFPANEL)][(int)(Math.random() * MyFrame.NUMBEROFPANEL)].setStatus(1);
		while(true){
			int temp = (int)(Math.random() * MyFrame.NUMBEROFPANEL);
			if(this.panels[temp][temp].getStatus() != 1 && temp%MyFrame.NUMBEROFPANEL != 0){
				this.panels[temp][temp].setStatus(2);
				break;
			}
		}

		this.outputStart();
	}
	@Override
	public void functionPlugin1(){
		System.out.println("additional function.");
		this.tpr.testOutput();
//		this.tpr.testConvert(this);
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
