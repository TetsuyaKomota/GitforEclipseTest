package komota.main;

import komota.pr.TestPatternRecognition;
import komota.pr.main.PR_000;

public class SampleTask5 extends MyFrame{

	public static void main(String[] args){
		SampleTask5 task = new SampleTask5();
	}

	//解析クラス
	TestPatternRecognition tpr;
	PR_000 tpr1;

	//コンストラクタ
	public SampleTask5(){
		super();
//		this.tpr = new TestPatternRecognition();
		this.tasktitle = "Move the RED to left of the BLUE.";
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
		while(true){
			int temp = (int)(Math.random() * MyFrame.NUMBEROFPANEL);
			if(this.panels[temp][temp].getStatus() != 1 && this.panels[temp][temp].getStatus() != 2 && temp%MyFrame.NUMBEROFPANEL != 0){
				this.panels[temp][temp].setStatus(3);
				break;
			}
		}

		this.outputStart();
	}
	@Override
	public void functionPlugin1(){
		System.out.println("additional function.");
		this.tpr1 = new PR_000(3);
//		this.tpr.testConvert(this);
	}
	@Override
	public void functionPlugin2(){
		System.out.println("start to learn.");
		this.tpr1.show();
		this.tpr1.learnfromLog();
		this.tpr1.showReference();
	}
	@Override
	public void functionPlugin3(){
		System.out.println("reproduction.");
		this.tpr1.reproduction(this);
	}
	@Override
	public void functionPlugin4(){
		System.out.println("bye");
		this.tpr1.close();
	}
	/*
	 *
	 * １．出力先ファイル名をfile_nameに代入
	 * ２．タスク名をtasktitleに代入
	 * ３．initializeをオーバーライド。最終行にoutputStart()
	 *
	 */

}
