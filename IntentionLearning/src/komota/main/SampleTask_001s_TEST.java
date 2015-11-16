package komota.main;

import komota.pr.main.PR_001;

public class SampleTask_001s_TEST extends MySerialFrame{

	public static void main(String[] args){
		SampleTask_001s_TEST task = new SampleTask_001s_TEST();
	}

	/* ************************************************************************************************************** */
	/* ************************************************************************************************************** */
	/*
	 * SampleTask_***系クラスは、実験成果として保存されるべきクラスであることを示す。
	 * SampleTast_***s系クラスは、擬似連続空間（MySerialFrame）を継承したクラスであることを示す。
	 * SampleTask_001は「1つ以上のランドマークを参照点とし、それとの相対位置を観点とする意図理解」
	 */
	/* ************************************************************************************************************** */
	/* ************************************************************************************************************** */

	//解析クラス
//	TestPatternRecognition tpr;
	PR_001 tpr1;

	//コンストラクタ
	public SampleTask_001s_TEST(){
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
			int temp1 = (int)(Math.random() * MyFrame.NUMBEROFPANEL);
			int temp2 = (int)(Math.random() * MyFrame.NUMBEROFPANEL);
			if(this.panels[temp1][temp2].getStatus() == 0){
				this.panels[temp1][temp2].setStatus(2);
				break;
			}
		}
		while(true){
			int temp1 = (int)(Math.random() * MyFrame.NUMBEROFPANEL);
			int temp2 = (int)(Math.random() * MyFrame.NUMBEROFPANEL);
			if(this.panels[temp1][temp2].getStatus() == 0){
				this.panels[temp1][temp2].setStatus(3);
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
				this.panels[temp1][temp2].setStatus(6);
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
		System.out.println("additional function.");
		this.tpr1 = new PR_001(9,this.file_name);
//		this.tpr.testConvert(this);
	}
	@Override
	public void functionPlugin2(){
		System.out.println("show.");
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
