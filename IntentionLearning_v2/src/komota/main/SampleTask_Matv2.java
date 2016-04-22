package komota.main;

import komota.lib.MyIO;
import komota.lib.Statics;
import komota.supers.MyFrame;

public class SampleTask_Matv2 extends MyFrame{


	public static void main(String[] args){
		@SuppressWarnings("unused")
		SampleTask_Matv2 frame = new SampleTask_Matv2();
	}

	//解析クラス
	PRv2_Mat_SOINN pr;
	PRv2_GA pr_ga;
	PRv2_EM pr_em;

	//コンストラクタ
	public SampleTask_Matv2(){
		super();
//		this.tpr = new TestPatternRecognition();
		this.tasktitle = "動作名";
		this.howtouse = "1,2:logdata学習 3:再現 4:識別用学習 5:押しちゃダメ 6:*** 7:ランダマイズ 8:順序問題,学習データ量テスト 9:データセット生成";
		setOutputFile("logdata.txt");
		initialize();
	}

	@Override
	public void initialize(){
		super.initialize();
//		setOutputFile("logdata.txt");
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


		this.panels[(int)(Math.random() * Statics.NUMBEROFPANEL)][(int)(Math.random() * Statics.NUMBEROFPANEL)].setStatus(1);
		while(true){
			int temp1 = (int)(Math.random() * Statics.NUMBEROFPANEL);
			int temp2 = (int)(Math.random() * Statics.NUMBEROFPANEL);
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
					}else if(temp3<0.75&&temp1<Statics.NUMBEROFPANEL-50&&this.panels[temp1+40][temp2].getStatus() == 0){
						this.panels[temp1+40][temp2].setStatus(3);
						break;
					}else if(temp3<1.1&&temp2<Statics.NUMBEROFPANEL-50&&this.panels[temp1][temp2+40].getStatus() == 0){
						this.panels[temp1][temp2+40].setStatus(3);
						break;
					}
				}
				break;
			}
		}
		while(true){
			int temp1 = (int)(Math.random() * Statics.NUMBEROFPANEL);
			int temp2 = (int)(Math.random() * Statics.NUMBEROFPANEL);
			if(this.panels[temp1][temp2].getStatus() == 0){
				this.panels[temp1][temp2].setStatus(4);
				break;
			}
		}
		while(true){
			int temp1 = (int)(Math.random() * Statics.NUMBEROFPANEL);
			int temp2 = (int)(Math.random() * Statics.NUMBEROFPANEL);
			if(this.panels[temp1][temp2].getStatus() == 0){
				this.panels[temp1][temp2].setStatus(5);
				break;
			}
		}
		while(true){
			int temp1 = (int)(Math.random() * Statics.NUMBEROFPANEL);
			int temp2 = (int)(Math.random() * Statics.NUMBEROFPANEL);
			if(this.panels[temp1][temp2].getStatus() == 0){
//				this.panels[temp1][temp2].setStatus(6);
				break;
			}
		}
		while(true){
			int temp1 = (int)(Math.random() * Statics.NUMBEROFPANEL);
			int temp2 = (int)(Math.random() * Statics.NUMBEROFPANEL);
			if(this.panels[temp1][temp2].getStatus() == 0){
//				this.panels[temp1][temp2].setStatus(7);
				break;
			}
		}
		while(true){
			int temp1 = (int)(Math.random() * Statics.NUMBEROFPANEL);
			int temp2 = (int)(Math.random() * Statics.NUMBEROFPANEL);
			if(this.panels[temp1][temp2].getStatus() == 0){
//				this.panels[temp1][temp2].setStatus(8);
				break;
			}
		}
		while(true){
			int temp1 = (int)(Math.random() * Statics.NUMBEROFPANEL);
			int temp2 = (int)(Math.random() * Statics.NUMBEROFPANEL);
			if(this.panels[temp1][temp2].getStatus() == 0){
//				this.panels[temp1][temp2].setStatus(9);
				break;
			}
		}

		this.outputStart();
	}

	@Override
	public void functionPlugin1(){
		System.out.println("PRクラス生成");
		this.pr = new PRv2_Mat_SOINN(5,"logdata.txt");
		System.out.println("PRクラス生成完了");
	}

	@Override
	public void functionPlugin2(){
		System.out.println("学習開始");
		this.pr.learnfromLog();
		this.pr.calcE(this.pr.X);
		System.out.println("学習完了");
	}
	@Override
	public void functionPlugin3(){
		System.out.println("動作再現");
		this.pr.reproduction(this);
	}
	@Override
	public void functionPlugin4(){
		System.out.println("PRクラス生成");
		this.pr_ga = new PRv2_GA(5,"logdata.txt");
		System.out.println("PRクラス生成完了");
	}
	@Override
	public void functionPlugin5(){
		System.out.println("学習開始");
		this.pr_ga.learnfromLog();
		this.pr_ga.calcE(this.pr_ga.X);
		System.out.println("学習完了");
	}
	@Override
	public void functionPlugin6(){
		System.out.println("動作再現");
		this.pr_ga.reproduction(this);
	}
	@Override
	public void functionPlugin7(){
		System.out.println("PRクラス生成");
		this.pr_em = new PRv2_EM(5,"logdata.txt");
		System.out.println("PRクラス生成完了");
	}
	@Override
	public void functionPlugin8(){
		System.out.println("学習開始");
		this.pr_em.learnfromLog();
		System.out.println("学習完了");
	}
	@Override
	public void functionPlugin9(){
		System.out.println("動作再現");
		this.pr_em.reproduction(this);
	}
	@Override
	public void functionPluginQ(){
		System.out.println("EMアルゴリズムでの学習テスト");
		MyIO io_Q = new MyIO();
		io_Q.writeFile("result_Q.txt");

		int count = 0;
		long starttime = 0;
		while(count<5){
			count++;
			Statics.EM_THRETHOLD /= 10;
			starttime = System.currentTimeMillis();
			functionPlugin8();
			io_Q.println("****COUNT:"+count);
			io_Q.printMatrix_approximately(pr_em.X, count);
			io_Q.println("****e_min:"+pr_em.calcE(pr_em.X));
			io_Q.println("****time[s]:"+ (double)(System.currentTimeMillis() - starttime)/1000);
			io_Q.println("*************************************");
			io_Q.execute();
		}
		System.out.println("テスト終了");
	}

	@Override
	public void functionPluginW(){
		MyIO io = new MyIO();
		io.readFile("20160421/result_Q.txt");

		this.pr_em.X = io.readMatrix(1).approximate();
	}


}
