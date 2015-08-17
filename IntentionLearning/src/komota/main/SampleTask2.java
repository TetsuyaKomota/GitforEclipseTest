package komota.main;

public class SampleTask2 extends MainFrame{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		SampleTask2 task = new SampleTask2();
	}

	//コンストラクタ
	public SampleTask2(){
		super();
		this.tasktitle = "Move the RED between two YELLOWs.";
		setOutputFile("test2.txt");
		initialize();
	}

	@Override
	public void initialize(){
/*
		this.panels[0].setStatus(0);
		this.panels[1].setStatus(2);
		this.panels[2].setStatus(0);
		this.panels[3].setStatus(0);
		this.panels[4].setStatus(0);
		this.panels[5].setStatus(0);
		this.panels[6].setStatus(0);
		this.panels[7].setStatus(0);
		this.panels[8].setStatus(1);
*/
		for(int i=0;i<this.panels.length;i++){
			this.panels[i].setStatus(0);
		}
//		this.panels[(int)(Math.random()*3)].setStatus(2);
//		this.panels[3+(int)(Math.random()*6)].setStatus(1);

		int rand = (int)(Math.random()*6);

		switch(rand){
		case 0:
			this.panels[0].setStatus(3);
			this.panels[2].setStatus(3);
			this.panels[7].setStatus(1);
			this.panels[8].setStatus(6);
			break;
		case 1:
			this.panels[3].setStatus(3);
			this.panels[5].setStatus(3);
			this.panels[0].setStatus(1);
			this.panels[8].setStatus(6);
			break;
		case 2:
			this.panels[6].setStatus(3);
			this.panels[8].setStatus(3);
			this.panels[1].setStatus(1);
			this.panels[4].setStatus(6);
			break;
		case 3:
			this.panels[0].setStatus(3);
			this.panels[6].setStatus(3);
			this.panels[1].setStatus(1);
			this.panels[3].setStatus(6);
			break;
		case 4:
			this.panels[1].setStatus(3);
			this.panels[7].setStatus(3);
			this.panels[8].setStatus(1);
			this.panels[3].setStatus(6);
			break;
		case 5:
			this.panels[2].setStatus(3);
			this.panels[8].setStatus(3);
			this.panels[0].setStatus(1);
			this.panels[6].setStatus(6);
			break;
		}


		this.outputStart();
	}

	/*
	@Override
	public void pushGoal(){
		initialize();
		pw.println("Done");
	}
	*/


	/*
	 *
	 * １．出力先ファイル名をfile_nameに代入
	 * ２．タスク名をtasktitleに代入
	 * ３．initializeをオーバーライド。最終行にoutputStart()
	 *
	 */
}
