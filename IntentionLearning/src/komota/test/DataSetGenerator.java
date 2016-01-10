package komota.test;

import java.util.Random;

import komota.main.MyFrame;


public class DataSetGenerator {

	//データセットを自動生成するクラス

//定数

	//生成するデータセット量
	final int NUMBEROFDATASET = 30;

//フィールド

	//ガウス誤差の分散
	//double variance;

	//コンストラクタ
	public DataSetGenerator(){
	}

	//RIGHT_TO_BLUEの自動生成
	public void generate_RIGHT_TO_BLUE(MyFrame frame,double variance){

		Random rand = new Random();
		double[] temperror = new double[2];
		int[] selected= new int[2];
		int[] secondselected= new int[2];

		int count = 0;

		while(count < NUMBEROFDATASET){

			//startログを生成
			frame.initialize();
			//selected,secondselectedを初期化
			selected[0] = -1;
			selected[1] = -1;
			secondselected[0] = -1;
			secondselected[1] = -1;
			frame.setSelected(selected);
			frame.setSecondSelected(secondselected);

			//トラジェクタと青いオブジェクトを検索
			for(int i=0;i<MyFrame.NUMBEROFPANEL;i++){
				for(int j=0;j<MyFrame.NUMBEROFPANEL;j++){
					if(frame.panels[i][j].getStatus() == 1){
						selected[0] = i;
						selected[1] = j;
						frame.setSelected(selected);
					}
					else if(frame.panels[i][j].getStatus() == 2){

						//青の右＋ガウス誤差 の位置が画面内かどうか
						temperror[0] = rand.nextGaussian()*variance;
						temperror[1] = rand.nextGaussian()*variance;

						if(	i+temperror[0] > 0
								&&j+temperror[1] > 0
								&&   i+temperror[0] < MyFrame.NUMBEROFPANEL-1
								&&15+j+temperror[1] < MyFrame.NUMBEROFPANEL-1){
							secondselected[0] =    i+(int)(temperror[0]+0.5);//四捨五入
							secondselected[1] = 15+j+(int)(temperror[1]+0.5);//四捨五入
							frame.setSecondSelected(secondselected);
						}

					}
				}
			}
			//トラジェクタと目標位置がセットされたはずなので、移動する
			if(frame.getSecondSelected()[0] != -1 && frame.getSecondSelected()[1] != -1){
				frame.pushSPACE();
				frame.pushGoal();
				count++;
			}
		}


	}
	//MOVE_THE_CENTERの自動生成
	public void generate_MOVE_THE_CENTER(MyFrame frame,double variance){

		Random rand = new Random();
		double[] temperror = new double[2];
		int[] selected= new int[2];
		int[] secondselected= new int[2];

		int count = 0;

		while(count < NUMBEROFDATASET){

			//startログを生成
			frame.initialize();
			//selected,secondselectedを初期化
			selected[0] = -1;
			selected[1] = -1;
			secondselected[0] = -1;
			secondselected[1] = -1;
			frame.setSelected(selected);
			frame.setSecondSelected(selected);

			//トラジェクタを検索
			for(int i=0;i<MyFrame.NUMBEROFPANEL;i++){
				for(int j=0;j<MyFrame.NUMBEROFPANEL;j++){
					if(frame.panels[i][j].getStatus() == 1){
						selected[0] = i;
						selected[1] = j;
						frame.setSelected(selected);
					}
				}
			}
			temperror[0] = rand.nextGaussian()*variance;
			temperror[1] = rand.nextGaussian()*variance;

			secondselected[0] = MyFrame.NUMBEROFPANEL/2+(int)(temperror[0]+0.5-variance/2);
			secondselected[1] = MyFrame.NUMBEROFPANEL/2+(int)(temperror[0]+0.5-variance/2);
			frame.setSecondSelected(secondselected);
			frame.pushSPACE();
			frame.pushGoal();
			count++;
		}


	}

}
