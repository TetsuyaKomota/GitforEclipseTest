package komota.main;

import komota.pr.main.PR_7_1_GL;
import komota.pr.main.PR_7_1_LT;

public class SampleTask_7_1s extends MySerialFrame{

	public static void main(String[] args){
		SampleTask_7_1s task = new SampleTask_7_1s();
	}

	/* ************************************************************************************************************** */
	/* ************************************************************************************************************** */
	/*
	 * ReferencePoint_Gaussのテスト
	 */
	/* ************************************************************************************************************** */
	/* ************************************************************************************************************** */

	//解析クラス
//	TestPatternRecognition tpr;
	PR_7_1_LT tpr1;
	PR_7_1_GL tpr2;
	PR_7_1_GL tpr3;

	//コンストラクタ
	public SampleTask_7_1s(){
		super();
//		this.tpr = new TestPatternRecognition();
		this.tasktitle = "思うようにやってみ.";
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
				while(true){
					double temp3 = Math.random();
					if(temp3<0.25&&temp1>30&&this.panels[temp1-20][temp2].getStatus() == 0){
						this.panels[temp1-20][temp2].setStatus(3);
						break;
					}else if(temp3<0.5&&temp2>30&&this.panels[temp1][temp2-20].getStatus() == 0){
						this.panels[temp1][temp2-20].setStatus(3);
						break;
					}else if(temp3<0.75&&temp1<MyFrame.NUMBEROFPANEL-30&&this.panels[temp1+20][temp2].getStatus() == 0){
						this.panels[temp1+20][temp2].setStatus(3);
						break;
					}else if(temp3<1.1&&temp2<MyFrame.NUMBEROFPANEL-30&&this.panels[temp1][temp2+20].getStatus() == 0){
						this.panels[temp1][temp2+20].setStatus(3);
						break;
					}
				}
				break;
			}
		}
		while(true){
			int temp1 = (int)(Math.random() * MyFrame.NUMBEROFPANEL);
			int temp2 = (int)(Math.random() * MyFrame.NUMBEROFPANEL);
			if(this.panels[temp1][temp2].getStatus() == 0){
//				this.panels[temp1][temp2].setStatus(3);
				break;
			}
		}
		while(true){
			int temp1 = (int)(Math.random() * MyFrame.NUMBEROFPANEL);
			int temp2 = (int)(Math.random() * MyFrame.NUMBEROFPANEL);
			if(this.panels[temp1][temp2].getStatus() == 0){
//				this.panels[temp1][temp2].setStatus(4);
				break;
			}
		}
		while(true){
			int temp1 = (int)(Math.random() * MyFrame.NUMBEROFPANEL);
			int temp2 = (int)(Math.random() * MyFrame.NUMBEROFPANEL);
			if(this.panels[temp1][temp2].getStatus() == 0){
//				this.panels[temp1][temp2].setStatus(5);
				break;
			}
		}
		while(true){
			int temp1 = (int)(Math.random() * MyFrame.NUMBEROFPANEL);
			int temp2 = (int)(Math.random() * MyFrame.NUMBEROFPANEL);
			if(this.panels[temp1][temp2].getStatus() == 0){
//				this.panels[temp1][temp2].setStatus(6);
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

/*
		this.panels[50][30+(int)(Math.random()*30)].setStatus(1);
		this.panels[25][30].setStatus(2);
		this.panels[120][80].setStatus(3);
*/
		this.outputStart();
	}
	@Override
	public void functionPlugin1(){
		System.out.println("additional function.");
//		this.tpr1 = new PR_002_LT(9,this.file_name);
		this.tpr2 = new PR_7_1_GL(9,this.file_name);
//		this.tpr3 = new PR_004_GL(9,this.file_name);
//		this.tpr.testConvert(this);
	}
	@Override
	public void functionPlugin2(){
		System.out.println("show.");
//		this.tpr1.show();
//		this.tpr1.learnfromLog();
		this.tpr2.learnfromLog();
//		this.tpr3.learnfromLog();

//		this.tpr1.showReference();
	}
	@Override
	public void functionPlugin3(){
		System.out.println("reproduction.");
		this.tpr2.reproduction(this);
/*
		if(this.tpr1.getMaxLikelihood() >= this.tpr2.getMaxLikelihood()){
			this.tpr1.reproduction(this);
		}
		else{
			this.tpr2.reproduction(this);
		}
*/
	}
	@Override
	public void functionPlugin4(){
		System.out.println("evaluation point:"+this.tpr2.evaluate(this,false));
		tpr2.setNumberofEvaluation(tpr2.getNumberofEvaluation()+1);
	}
	@Override
	public void functionPlugin5(){
		System.out.println("evaluation point:"+this.tpr2.evaluate(this));
	}
	@Override
	public void functionPlugin6(){
		this.tpr2.show();
	}
	@Override
	public void functionPlugin7(){
		this.tpr2.setNumberofEvaluation(this.tpr3.getNumberofEvaluation() + 10);
		System.out.println("[SampleTask_005s]functionPlugin7:set the number of data used to evaluate :"+this.tpr2.getNumberofEvaluation());
	}
	@Override
	public void functionPlugin8(){
		if(this.tpr2.getNumberofEvaluation() > 10){
			this.tpr2.setNumberofEvaluation(this.tpr3.getNumberofEvaluation() - 10);
			System.out.println("[SampleTask_005s]functionPlugin8:set the number of data used to evaluate :"+this.tpr2.getNumberofEvaluation());
		}
	}
	/*
	 *
	 * １．出力先ファイル名をfile_nameに代入
	 * ２．タスク名をtasktitleに代入
	 * ３．initializeをオーバーライド。最終行にoutputStart()
	 *
	 */

}
