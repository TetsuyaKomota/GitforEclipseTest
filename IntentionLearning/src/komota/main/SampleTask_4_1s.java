package komota.main;

import komota.pr.main.PR_002_ID;
import komota.pr.main.PR_002_LT;
import komota.pr.test.PR_4_1;

public class SampleTask_4_1s extends MySerialFrame{

	public static void main(String[] args){
		SampleTask_4_1s task = new SampleTask_4_1s();
	}

	/* ************************************************************************************************************** */
	/* ************************************************************************************************************** */
	/*
	 * SampleTask_***系クラスは、実験成果として保存されるべきクラスであることを示す。
	 * SampleTast_***s系クラスは、擬似連続空間（MySerialFrame）を継承したクラスであることを示す。
	 * SampleTask_003は001と002の混合で、複数の座標系から尤もらしい観点を推定する
	 */
	/* ************************************************************************************************************** */
	/* ************************************************************************************************************** */

	//解析クラス
//	TestPatternRecognition tpr;
	PR_002_LT tpr1;
	PR_002_ID tpr2;
	PR_4_1 tpr3;

	//コンストラクタ
	public SampleTask_4_1s(){
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
					}else if(temp3<0.5&&temp2<MyFrame.NUMBEROFPANEL-30&&this.panels[temp1][temp2+20].getStatus() == 0){
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

		this.outputStart();
	}
	@Override
	public void functionPlugin1(){
		System.out.println("additional function.");
//		this.tpr1 = new PR_002_LT(9,this.file_name);
//		this.tpr2 = new PR_002_ID(9,this.file_name);
		this.tpr3 = new PR_4_1(9,this.file_name);
//		this.tpr.testConvert(this);
	}
	@Override
	public void functionPlugin2(){
		System.out.println("show.");
//		this.tpr1.show();
//		this.tpr1.learnfromLog();
//		this.tpr2.learnfromLog();
		this.tpr3.learnfromLog();

//		this.tpr1.showReference();
	}
	@Override
	public void functionPlugin3(){
		System.out.println("reproduction.");
		this.tpr3.reproduction(this);
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
