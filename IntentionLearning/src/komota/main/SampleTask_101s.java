package komota.main;

import komota.pr.main.PR_100_GL;
import komota.pr.main.PR_100_ID;
import komota.pr.main.PR_100_LT;
import komota.pr.main.PR_101;

public class SampleTask_101s extends MySerialFrame{

	public static void main(String[] args){
		SampleTask_101s task = new SampleTask_101s();
	}

	/* ************************************************************************************************************** */
	/* ************************************************************************************************************** */
	/*
	 *MyTaskクラスの動作テスト版
	 */
	/* ************************************************************************************************************** */
	/* ************************************************************************************************************** */

	//解析クラス
//	TestPatternRecognition tpr;
	PR_100_LT pr_LT;
	PR_100_ID pr_ID;
	PR_100_GL pr_GL;

	//盤面記憶用PRクラス
	PR_101 save;

	//タスククラス
	MyTask task_RtB;
	MyTask task_NbO;
	MyTask task_MtS;

	//コンストラクタ
	public SampleTask_101s(){
		super();
//		this.tpr = new TestPatternRecognition();
		this.tasktitle = "動作名";
		setOutputFile("logdata.txt");
		initialize();
	}

	@Override
	public void initialize(){

		for(int i=0;i<this.panels.length;i++){
			for(int j=0;j<this.panels[0].length;j++){
				this.panels[i][j].setStatus(0);
			}
		}

		/*
		 * 環境中の物体配置は、以下のように定める
		 * 1. トラジェクタの位置をランダムに決定する
		 * 2. 物体2(青色のオブジェクト)の位置を、トラジェクタの位置以外でランダムに決定する
		 * 3. 物体3(黄色のオブジェクト)の位置を、物体2の上下左右にそれぞれ40マス離れた位置のうち、可能な位置からランダムに決定する
		 * 4. 物体4(緑色のオブジェクト)の位置を、現在物体が配置されていない位置からランダムに決定する
		 * 5. 物体5(橙色のオブジェクト)の位置を、現在物体が配置されていない位置からランダムに決定する
		 *
		 * つまり環境中には1つのトラジェクタと4つのオブジェクトが配置され、そのうち必ず上下左右40マスに隣り合う青色と黄色以外は独立にランダムで場所が決定する
		 */


		this.panels[(int)(Math.random() * MyFrame.NUMBEROFPANEL)][(int)(Math.random() * MyFrame.NUMBEROFPANEL)].setStatus(1);
		while(true){
			int temp1 = (int)(Math.random() * MyFrame.NUMBEROFPANEL);
			int temp2 = (int)(Math.random() * MyFrame.NUMBEROFPANEL);
			if(this.panels[temp1][temp2].getStatus() == 0){
				this.panels[temp1][temp2].setStatus(2);
				while(true){
					double temp3 = Math.random();
					if(temp3<0.25&&temp1>50&&this.panels[temp1-40][temp2].getStatus() == 0){
						this.panels[temp1-40][temp2].setStatus(3);
						break;
					}else if(temp3<0.5&&temp2>50&&this.panels[temp1][temp2-40].getStatus() == 0){
						this.panels[temp1][temp2-40].setStatus(3);
						break;
					}else if(temp3<0.75&&temp1<MyFrame.NUMBEROFPANEL-50&&this.panels[temp1+40][temp2].getStatus() == 0){
						this.panels[temp1+40][temp2].setStatus(3);
						break;
					}else if(temp3<1.1&&temp2<MyFrame.NUMBEROFPANEL-50&&this.panels[temp1][temp2+40].getStatus() == 0){
						this.panels[temp1][temp2+40].setStatus(3);
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
		System.out.println("各パターン認識クラスのインスタンスを生成");
		this.pr_LT = new PR_100_LT(9,this.file_name);
		this.pr_ID = new PR_100_ID(9,this.file_name);
		this.pr_GL = new PR_100_GL(9,this.file_name);
	}
	@Override
	public void functionPlugin2(){
		System.out.println("ログデータから学習");
		this.pr_LT.learnfromLog();
		this.pr_ID.learnfromLog();
		this.pr_GL.learnfromLog();
	}
	@Override
	public void functionPlugin3(){
		System.out.println("学習結果から、動作を再現");
		System.out.println("[SampleTask_101s]functionPlugin3:MaxLikelihood: ID:"+this.pr_ID.getMaxLikelihood()+" LT:"+pr_LT.getMaxLikelihood()+" GL:"+pr_GL.getMaxLikelihood());
		if(this.pr_GL.getMaxLikelihood() >= this.pr_LT.getMaxLikelihood() && this.pr_GL.getMaxLikelihood() >= this.pr_ID.getMaxLikelihood()){
			this.pr_GL.reproduction(this);
		}
		else if(this.pr_LT.getMaxLikelihood() >= this.pr_ID.getMaxLikelihood() && this.pr_LT.getMaxLikelihood() >= this.pr_GL.getMaxLikelihood()){
			this.pr_LT.reproduction(this);
		}
		else{
			this.pr_ID.reproduction(this);
		}
	}
	@Override
	public void functionPlugin4(){
		System.out.println("[SampleTask_101s]functionPlugin4:タスククラスのインスタンス生成");
		this.expranation = "[SampleTask_101s]functionPlugin4:タスククラスのインスタンス生成";
		this.task_RtB = new MyTask("log_RIGHT_TO_BLUE.txt","赤を青の右に動かす");
		this.task_NbO = new MyTask("log_NEAR_BY_ORANGE.txt","赤を橙に近づける");
		this.task_MtS = new MyTask("log_MAKE_THE_SIGNAL.txt","等間隔に赤、黄、青と並べる");
		//一応、現在のログでPRインスタンスを生成し直しておく
		//this.functionPlugin1();
		System.out.println("インスタンス生成完了");
		this.expranation = "インスタンス生成完了";
	}
	@Override
	public void functionPlugin5(){
/*
 * 1. 現在の盤面と、直前のstartログの盤面を何らかの方法で保持する
 * 2. 保持したstartログに盤面を合わせる
 * 3. task_RtBでreproductionTask
 * 4. secondselectedと、保持した最終状態の位置との距離をdis_RtBとして保存
 * 5. タスクを切り替え、2. に戻る
 * 6. disの最も小さいタスクのtasknameを出力する
 */

		double dis_RtB = 10000;
		double dis_NbO = 10000;
		double dis_MtS = 10000;

		int[] tempselected = new int[2];

		//1.
		this.save = new PR_101();
		this.save.setLog(this);
		//2.
		this.save.loadLastStartLog(this);
		//3.
		this.task_RtB.reproductionTask(this);
		tempselected = this.getSecondSelected();
		//4.
		dis_RtB = (tempselected[0] - this.save.getLastPosition()[0])*(tempselected[0] - this.save.getLastPosition()[0])+(tempselected[1] - this.save.getLastPosition()[1])*(tempselected[1] - this.save.getLastPosition()[1]);

		//2.
		this.save.loadLastStartLog(this);
		//3.
		this.task_NbO.reproductionTask(this);
		tempselected = this.getSecondSelected();
		//4.
		dis_NbO = (tempselected[0] - this.save.getLastPosition()[0])*(tempselected[0] - this.save.getLastPosition()[0])+(tempselected[1] - this.save.getLastPosition()[1])*(tempselected[1] - this.save.getLastPosition()[1]);

		//2.
		this.save.loadLastStartLog(this);
		//3.
		this.task_MtS.reproductionTask(this);
		tempselected = this.getSecondSelected();
		//4.
		dis_MtS = (tempselected[0] - this.save.getLastPosition()[0])*(tempselected[0] - this.save.getLastPosition()[0])+(tempselected[1] - this.save.getLastPosition()[1])*(tempselected[1] - this.save.getLastPosition()[1]);

		//6.
		if(dis_RtB<dis_NbO && dis_RtB<dis_MtS){
			this.tasktitle = this.task_RtB.getTaskName();
		}
		else if(dis_NbO<dis_RtB && dis_NbO<dis_MtS){
			this.tasktitle = this.task_NbO.getTaskName();
		}
		else{
			this.tasktitle = this.task_MtS.getTaskName();
		}
	}
	@Override
	public void functionPlugin6(){
		//デモ用。体裁を整える
		this.expranation = "動作名";
		this.tasktitle = "";
	}
	@Override
	public void functionPlugin7(){
	}
}
