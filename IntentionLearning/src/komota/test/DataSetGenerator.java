package komota.test;

import java.util.Random;

import komota.main.MyFrame;


public class DataSetGenerator {

	//データセットを自動生成するクラス

	public static void main(String[] args){

		double bunsan = 1;
		int universe = 100000000;

		Random r = new Random();
		DataSetGenerator g = new DataSetGenerator();
		double ran = 0;
		double max = 0;
		double min = 0;
		double mean = 0;
		double sqmean = 0;
		double variance = 0;
		int count = 0;
		int sup = 0;
		int inf = 0;
		while(count++ < universe){
//			ran = g.nextError(bunsan);
			ran = Math.abs(g.nextError(bunsan));
//			System.out.println("output,"+ran);
			if(ran>max){
				max = ran;
			}
			if(ran<min){
				min = ran;
			}
			if(ran>Math.sqrt(bunsan)*3){
				sup++;
			}
			if(ran<-Math.sqrt(bunsan)*3){
				inf++;
			}

			mean = mean*(count-1)/count+ran/count;
			sqmean = sqmean*(count-1)/count+ran*ran/count;
			variance = sqmean - mean*mean;
		}
		System.out.println("まっくす"+max+" みにまむ"+min+" へいきん"+mean+" ぶんさん"+variance);
		System.out.println("0.998になると嬉しいけど、"+(double)(universe - sup - inf)/universe+"でした。 sup:"+sup+" inf:"+inf);
		System.out.println(-0.5+"って"+(int)(-0.5)+"だよ");
	}


//定数

	//生成するデータセット量
	final int NUMBEROFDATASET = 10;
	//分散値に対する外れ値の比率. 3 なら、0.1%の割合で生じる外れ値を除外する
	final int OUTLIER = 3;

	//物体
	public static final int RED			= 1;
	public static final int BLUE 		= 2;
	public static final int YELLOW 		= 3;
	public static final int GREEN 		= 4;
	public static final int ORANGE 		= 5;
	public static final int PINK 		= 6;
	public static final int LIGHTGRAY 	= 7;
	public static final int GRAY 		= 8;
	public static final int BLACK 		= 9;

//フィールド

	//ガウス誤差の分散
	//double variance;
	//ガウスの種

	Random rand;

	//コンストラクタ
	public DataSetGenerator(){
		this.rand = new Random();
	}

	//ガウス誤差を生成する
	double nextError(double variance){
		double output = this.rand.nextGaussian()*Math.sqrt(variance);
/*
		while(output > variance*OUTLIER || output < -variance*OUTLIER){
			output = (this.rand.nextGaussian() - 0.5)*variance;
		}
*/
		return output;
	}

	//RIGHT_TO_BLUEの自動生成
	public void generate_RIGHT_TO_BLUE(MyFrame frame,double variance){

		int[] temperror = new int[2];
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
						temperror[0] = (int)nextError(variance);
						temperror[1] = (int)nextError(variance);

						if(		     i+temperror[0] > 0
								&&15+j+temperror[1] > 0
								&&   i+temperror[0] < MyFrame.NUMBEROFPANEL-2
								&&15+j+temperror[1] < MyFrame.NUMBEROFPANEL-2){
							secondselected[0] =    i+temperror[0];
							secondselected[1] = 15+j+temperror[1];
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
	//RIGHT_TO_(任意の物体)の自動生成
	public void generate_RIGHT_TO_(int object,MyFrame frame,double variance){

		int[] temperror = new int[2];
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
					else if(frame.panels[i][j].getStatus() == object){

						//青の右＋ガウス誤差 の位置が画面内かどうか
						temperror[0] = (int)nextError(variance);
						temperror[1] = (int)nextError(variance);

						if(		     i+temperror[0] > 0
								&&15+j+temperror[1] > 0
								&&   i+temperror[0] < MyFrame.NUMBEROFPANEL-2
								&&15+j+temperror[1] < MyFrame.NUMBEROFPANEL-2){
							secondselected[0] =    i+temperror[0];
							secondselected[1] = 15+j+temperror[1];
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
	//NEAR_BY_ORANGEの自動生成
	public void generate_NEAR_BY_ORANGE(MyFrame frame,double variance){

		int[] temperror = new int[2];
		int[] selected= new int[2];
		int[] secondselected= new int[2];
		int[] tempred = new int[2];
		int[] temporange = new int[2];

		int count = 0;

		while(count < NUMBEROFDATASET){

			//startログを生成
			frame.initialize();
			//selected,secondselectedを初期化
			selected[0] = -1;
			selected[1] = -1;
			secondselected[0] = -1;
			secondselected[1] = -1;
			tempred[0] = -1;
			tempred[1] = -1;
			temporange[0] = -1;
			temporange[1] = -1;
			frame.setSelected(selected);
			frame.setSecondSelected(secondselected);

			//トラジェクタと青いオブジェクトを検索
			for(int i=0;i<MyFrame.NUMBEROFPANEL;i++){
				for(int j=0;j<MyFrame.NUMBEROFPANEL;j++){
					if(frame.panels[i][j].getStatus() == 1){
						selected[0] = i;
						selected[1] = j;
						frame.setSelected(selected);
						tempred[0] = i;
						tempred[1] = j;
					}
					else if(frame.panels[i][j].getStatus() == 5){

						temperror[0] = (int)nextError(variance);
						temperror[1] = (int)nextError(variance);

						temporange[0] = i;
						temporange[1] = j;
					}
				}
			}
			int[] tempsecondselected = new int[2];
			tempsecondselected[0] = (int)((double)(tempred[0]+temporange[0])/2+0.5) + temperror[0];
			tempsecondselected[1] = (int)((double)(tempred[1]+temporange[1])/2+0.5) + temperror[1];

			if(		  tempsecondselected[0] > 0
					&&tempsecondselected[1] > 0
					&&tempsecondselected[0] < MyFrame.NUMBEROFPANEL - 2
					&&tempsecondselected[1] < MyFrame.NUMBEROFPANEL - 2
					){
				secondselected[0] = tempsecondselected[0];
				secondselected[1] = tempsecondselected[1];
				frame.setSecondSelected(secondselected);
			}

			//トラジェクタと目標位置がセットされたはずなので、移動する
			if(frame.getSecondSelected()[0] > 0 && frame.getSecondSelected()[1] > 0){
				frame.pushSPACE();
				frame.pushGoal();
				count++;
			}
		}


	}
	//NEAR_BY_(任意の物体)の自動生成
	public void generate_NEAR_BY_(int object,MyFrame frame,double variance){

		int[] temperror = new int[2];
		int[] selected= new int[2];
		int[] secondselected= new int[2];
		int[] tempred = new int[2];
		int[] temporange = new int[2];

		int count = 0;

		while(count < NUMBEROFDATASET){

			//startログを生成
			frame.initialize();
			//selected,secondselectedを初期化
			selected[0] = -1;
			selected[1] = -1;
			secondselected[0] = -1;
			secondselected[1] = -1;
			tempred[0] = -1;
			tempred[1] = -1;
			temporange[0] = -1;
			temporange[1] = -1;
			frame.setSelected(selected);
			frame.setSecondSelected(secondselected);

			//トラジェクタと青いオブジェクトを検索
			for(int i=0;i<MyFrame.NUMBEROFPANEL;i++){
				for(int j=0;j<MyFrame.NUMBEROFPANEL;j++){
					if(frame.panels[i][j].getStatus() == 1){
						selected[0] = i;
						selected[1] = j;
						frame.setSelected(selected);
						tempred[0] = i;
						tempred[1] = j;
					}
					else if(frame.panels[i][j].getStatus() == object){

						temperror[0] = (int)nextError(variance);
						temperror[1] = (int)nextError(variance);

						temporange[0] = i;
						temporange[1] = j;
					}
				}
			}
			int[] tempsecondselected = new int[2];
			tempsecondselected[0] = (int)((double)(tempred[0]+temporange[0])/2+0.5) + temperror[0];
			tempsecondselected[1] = (int)((double)(tempred[1]+temporange[1])/2+0.5) + temperror[1];

			if(		  tempsecondselected[0] > 0
					&&tempsecondselected[1] > 0
					&&tempsecondselected[0] < MyFrame.NUMBEROFPANEL - 2
					&&tempsecondselected[1] < MyFrame.NUMBEROFPANEL - 2
					){
				secondselected[0] = tempsecondselected[0];
				secondselected[1] = tempsecondselected[1];
				frame.setSecondSelected(secondselected);
			}

			//トラジェクタと目標位置がセットされたはずなので、移動する
			if(frame.getSecondSelected()[0] > 0 && frame.getSecondSelected()[1] > 0){
				frame.pushSPACE();
				frame.pushGoal();
				count++;
			}
		}


	}
	//AWAY_FROM_GREENの自動生成
	public void generate_AWAY_FROM_GREEN(MyFrame frame,double variance){

		int[] temperror = new int[2];
		int[] selected= new int[2];
		int[] secondselected= new int[2];
		int[] tempred = new int[2];
		int[] tempgreen = new int[2];

		int count = 0;

		while(count < NUMBEROFDATASET){

			//startログを生成
			frame.initialize();
			//selected,secondselectedを初期化
			selected[0] = -1;
			selected[1] = -1;
			secondselected[0] = -1;
			secondselected[1] = -1;
			tempred[0] = -1;
			tempred[1] = -1;
			tempgreen[0] = -1;
			tempgreen[1] = -1;
			frame.setSelected(selected);
			frame.setSecondSelected(secondselected);

			//トラジェクタと青いオブジェクトを検索
			for(int i=0;i<MyFrame.NUMBEROFPANEL;i++){
				for(int j=0;j<MyFrame.NUMBEROFPANEL;j++){
					if(frame.panels[i][j].getStatus() == 1){
						selected[0] = i;
						selected[1] = j;
						frame.setSelected(selected);
						tempred[0] = i;
						tempred[1] = j;
					}
					else if(frame.panels[i][j].getStatus() == 4){

						temperror[0] = (int)nextError(variance);
						temperror[1] = (int)nextError(variance);

						tempgreen[0] = i;
						tempgreen[1] = j;
					}
				}
			}
			int[] tempsecondselected = new int[2];
			tempsecondselected[0] = 2*tempred[0]-tempgreen[0] + temperror[0];
			tempsecondselected[1] = 2*tempred[1]-tempgreen[1] + temperror[1];

			if(		  tempsecondselected[0] > 0
					&&tempsecondselected[1] > 0
					&&tempsecondselected[0] < MyFrame.NUMBEROFPANEL - 2
					&&tempsecondselected[1] < MyFrame.NUMBEROFPANEL - 2
					){
				secondselected[0] = tempsecondselected[0];
				secondselected[1] = tempsecondselected[1];
				frame.setSecondSelected(secondselected);
			}

			//トラジェクタと目標位置がセットされたはずなので、移動する
			if(frame.getSecondSelected()[0] > 0 && frame.getSecondSelected()[1] > 0){
				frame.pushSPACE();
				frame.pushGoal();
				count++;
			}
		}


	}
	//AWAY_FROM_(任意の物体)の自動生成
	public void generate_AWAY_FROM_(int object,MyFrame frame,double variance){

		int[] temperror = new int[2];
		int[] selected= new int[2];
		int[] secondselected= new int[2];
		int[] tempred = new int[2];
		int[] tempgreen = new int[2];

		int count = 0;

		while(count < NUMBEROFDATASET){

			//startログを生成
			frame.initialize();
			//selected,secondselectedを初期化
			selected[0] = -1;
			selected[1] = -1;
			secondselected[0] = -1;
			secondselected[1] = -1;
			tempred[0] = -1;
			tempred[1] = -1;
			tempgreen[0] = -1;
			tempgreen[1] = -1;
			frame.setSelected(selected);
			frame.setSecondSelected(secondselected);

			//トラジェクタと青いオブジェクトを検索
			for(int i=0;i<MyFrame.NUMBEROFPANEL;i++){
				for(int j=0;j<MyFrame.NUMBEROFPANEL;j++){
					if(frame.panels[i][j].getStatus() == 1){
						selected[0] = i;
						selected[1] = j;
						frame.setSelected(selected);
						tempred[0] = i;
						tempred[1] = j;
					}
					else if(frame.panels[i][j].getStatus() == object){

						temperror[0] = (int)nextError(variance);
						temperror[1] = (int)nextError(variance);

						tempgreen[0] = i;
						tempgreen[1] = j;
					}
				}
			}
			int[] tempsecondselected = new int[2];
			tempsecondselected[0] = 2*tempred[0]-tempgreen[0] + temperror[0];
			tempsecondselected[1] = 2*tempred[1]-tempgreen[1] + temperror[1];

			if(		  tempsecondselected[0] > 0
					&&tempsecondselected[1] > 0
					&&tempsecondselected[0] < MyFrame.NUMBEROFPANEL - 2
					&&tempsecondselected[1] < MyFrame.NUMBEROFPANEL - 2
					){
				secondselected[0] = tempsecondselected[0];
				secondselected[1] = tempsecondselected[1];
				frame.setSecondSelected(secondselected);
			}

			//トラジェクタと目標位置がセットされたはずなので、移動する
			if(frame.getSecondSelected()[0] > 0 && frame.getSecondSelected()[1] > 0){
				frame.pushSPACE();
				frame.pushGoal();
				count++;
			}
		}


	}
	//MAKE_THE_SIGNALの自動生成
	public void generate_MAKE_THE_SIGNAL(MyFrame frame,double variance){

		int[] temperror = new int[2];
		int[] selected= new int[2];
		int[] secondselected= new int[2];
		int[] tempred = new int[2];
		int[] tempblue = new int[2];
		int[] tempyellow = new int[2];

		int count = 0;

		while(count < NUMBEROFDATASET){

			//startログを生成
			frame.initialize();
			//selected,secondselectedを初期化
			selected[0] = -1;
			selected[1] = -1;
			secondselected[0] = -1;
			secondselected[1] = -1;
			tempred[0] = -1;
			tempred[1] = -1;
			tempblue[0] = -1;
			tempblue[1] = -1;
			tempyellow[0] = -1;
			tempyellow[1] = -1;

			frame.setSelected(selected);
			frame.setSecondSelected(secondselected);

			//トラジェクタと青いオブジェクトと黄色いオブジェクトを検索
			for(int i=0;i<MyFrame.NUMBEROFPANEL;i++){
				for(int j=0;j<MyFrame.NUMBEROFPANEL;j++){
					if(frame.panels[i][j].getStatus() == 1){
						selected[0] = i;
						selected[1] = j;
						frame.setSelected(selected);
						tempred[0] = i;
						tempred[1] = j;
					}
					else if(frame.panels[i][j].getStatus() == 2){
						tempblue[0] = i;
						tempblue[1] = j;
					}
					else if(frame.panels[i][j].getStatus() == 3){
						tempyellow[0] = i;
						tempyellow[1] = j;
					}
				}
			}
			temperror[0] = (int)nextError(variance);
			temperror[1] = (int)nextError(variance);
			int[] tempsecondselected = new int[2];
			tempsecondselected[0] = 2*tempyellow[0]-tempblue[0] + temperror[0];
			tempsecondselected[1] = 2*tempyellow[1]-tempblue[1] + temperror[1];

			if(		  tempsecondselected[0] > 0
					&&tempsecondselected[1] > 0
					&&tempsecondselected[0] < MyFrame.NUMBEROFPANEL - 2
					&&tempsecondselected[1] < MyFrame.NUMBEROFPANEL - 2
					){
				secondselected[0] = tempsecondselected[0];
				secondselected[1] = tempsecondselected[1];
				frame.setSecondSelected(secondselected);
			}

			//トラジェクタと目標位置がセットされたはずなので、移動する
			if(frame.getSecondSelected()[0] > 0 && frame.getSecondSelected()[1] > 0){
				frame.pushSPACE();
				frame.pushGoal();
				count++;
			}
		}


	}
	//MAKE_THE_SIG_の自動生成
	public void generate_MAKE_THE_SIG_(int centerobject,int rightobject,MyFrame frame,double variance){

		int[] temperror = new int[2];
		int[] selected= new int[2];
		int[] secondselected= new int[2];
		int[] tempred = new int[2];
		int[] tempblue = new int[2];
		int[] tempyellow = new int[2];

		int count = 0;

		while(count < NUMBEROFDATASET){

			//startログを生成
			frame.initialize();
			//selected,secondselectedを初期化
			selected[0] = -1;
			selected[1] = -1;
			secondselected[0] = -1;
			secondselected[1] = -1;
			tempred[0] = -1;
			tempred[1] = -1;
			tempblue[0] = -1;
			tempblue[1] = -1;
			tempyellow[0] = -1;
			tempyellow[1] = -1;

			frame.setSelected(selected);
			frame.setSecondSelected(secondselected);

			//トラジェクタと青いオブジェクトと黄色いオブジェクトを検索
			for(int i=0;i<MyFrame.NUMBEROFPANEL;i++){
				for(int j=0;j<MyFrame.NUMBEROFPANEL;j++){
					if(frame.panels[i][j].getStatus() == 1){
						selected[0] = i;
						selected[1] = j;
						frame.setSelected(selected);
						tempred[0] = i;
						tempred[1] = j;
					}
					else if(frame.panels[i][j].getStatus() == rightobject){
						tempblue[0] = i;
						tempblue[1] = j;
					}
					else if(frame.panels[i][j].getStatus() == centerobject){
						tempyellow[0] = i;
						tempyellow[1] = j;
					}
				}
			}
			temperror[0] = (int)nextError(variance);
			temperror[1] = (int)nextError(variance);
			int[] tempsecondselected = new int[2];
			tempsecondselected[0] = 2*tempyellow[0]-tempblue[0] + temperror[0];
			tempsecondselected[1] = 2*tempyellow[1]-tempblue[1] + temperror[1];

			if(		  tempsecondselected[0] > 0
					&&tempsecondselected[1] > 0
					&&tempsecondselected[0] < MyFrame.NUMBEROFPANEL - 2
					&&tempsecondselected[1] < MyFrame.NUMBEROFPANEL - 2
					){
				secondselected[0] = tempsecondselected[0];
				secondselected[1] = tempsecondselected[1];
				frame.setSecondSelected(secondselected);
			}

			//トラジェクタと目標位置がセットされたはずなので、移動する
			if(frame.getSecondSelected()[0] > 0 && frame.getSecondSelected()[1] > 0){
				frame.pushSPACE();
				frame.pushGoal();
				count++;
			}
		}


	}
	//MAKE_THE_TRIANGLEの自動生成
	public void generate_MAKE_THE_TRIANGLE(MyFrame frame,double variance){

		int[] temperror = new int[2];
		int[] selected= new int[2];
		int[] secondselected= new int[2];
		int[] tempred = new int[2];
		int[] tempblue = new int[2];
		int[] tempgreen = new int[2];

		int count = 0;

		while(count < NUMBEROFDATASET){

			//startログを生成
			frame.initialize();
			//selected,secondselectedを初期化
			selected[0] = -1;
			selected[1] = -1;
			secondselected[0] = -1;
			secondselected[1] = -1;
			tempred[0] = -1;
			tempred[1] = -1;
			tempblue[0] = -1;
			tempblue[1] = -1;
			tempgreen[0] = -1;
			tempgreen[1] = -1;

			frame.setSelected(selected);
			frame.setSecondSelected(secondselected);

			//トラジェクタと青いオブジェクトと黄色いオブジェクトを検索
			for(int i=0;i<MyFrame.NUMBEROFPANEL;i++){
				for(int j=0;j<MyFrame.NUMBEROFPANEL;j++){
					if(frame.panels[i][j].getStatus() == 1){
						selected[0] = i;
						selected[1] = j;
						frame.setSelected(selected);
						tempred[0] = i;
						tempred[1] = j;
					}
					else if(frame.panels[i][j].getStatus() == 2){
						tempblue[0] = i;
						tempblue[1] = j;
					}
					else if(frame.panels[i][j].getStatus() == 4){
						tempgreen[0] = i;
						tempgreen[1] = j;
					}
				}
			}
			temperror[0] = (int)nextError(variance);
			temperror[1] = (int)nextError(variance);
			int[] tempsecondselected = new int[2];

			tempsecondselected[0] = (int)(((double)(tempgreen[0]+tempblue[0]))/2 - ((double)(tempgreen[1]-tempblue[1]))*Math.sqrt(3)/2 + 0.5) + temperror[0];
			tempsecondselected[1] = (int)(((double)(tempgreen[1]+tempblue[1]))/2 + ((double)(tempgreen[0]-tempblue[0]))*Math.sqrt(3)/2 + 0.5) + temperror[1];

			if(		  tempsecondselected[0] > 0
					&&tempsecondselected[1] > 0
					&&tempsecondselected[0] < MyFrame.NUMBEROFPANEL - 2
					&&tempsecondselected[1] < MyFrame.NUMBEROFPANEL - 2
					){
				secondselected[0] = tempsecondselected[0];
				secondselected[1] = tempsecondselected[1];
				frame.setSecondSelected(secondselected);
			}

			//トラジェクタと目標位置がセットされたはずなので、移動する
			if(frame.getSecondSelected()[0] > 0 && frame.getSecondSelected()[1] > 0){
				frame.pushSPACE();
				frame.pushGoal();
				count++;
			}
		}


	}
	//MAKE_THE_TRI_の自動生成
	public void generate_MAKE_THE_TRI_(int leftobject,int rightobject,MyFrame frame,double variance){

		int[] temperror = new int[2];
		int[] selected= new int[2];
		int[] secondselected= new int[2];
		int[] tempred = new int[2];
		int[] tempblue = new int[2];
		int[] tempgreen = new int[2];

		int count = 0;

		while(count < NUMBEROFDATASET){

			//startログを生成
			frame.initialize();
			//selected,secondselectedを初期化
			selected[0] = -1;
			selected[1] = -1;
			secondselected[0] = -1;
			secondselected[1] = -1;
			tempred[0] = -1;
			tempred[1] = -1;
			tempblue[0] = -1;
			tempblue[1] = -1;
			tempgreen[0] = -1;
			tempgreen[1] = -1;

			frame.setSelected(selected);
			frame.setSecondSelected(secondselected);

			//トラジェクタと青いオブジェクトと黄色いオブジェクトを検索
			for(int i=0;i<MyFrame.NUMBEROFPANEL;i++){
				for(int j=0;j<MyFrame.NUMBEROFPANEL;j++){
					if(frame.panels[i][j].getStatus() == 1){
						selected[0] = i;
						selected[1] = j;
						frame.setSelected(selected);
						tempred[0] = i;
						tempred[1] = j;
					}
					else if(frame.panels[i][j].getStatus() == rightobject){
						tempblue[0] = i;
						tempblue[1] = j;
					}
					else if(frame.panels[i][j].getStatus() == leftobject){
						tempgreen[0] = i;
						tempgreen[1] = j;
					}
				}
			}
			temperror[0] = (int)nextError(variance);
			temperror[1] = (int)nextError(variance);
			int[] tempsecondselected = new int[2];

			tempsecondselected[0] = (int)(((double)(tempgreen[0]+tempblue[0]))/2 - ((double)(tempgreen[1]-tempblue[1]))*Math.sqrt(3)/2 + 0.5) + temperror[0];
			tempsecondselected[1] = (int)(((double)(tempgreen[1]+tempblue[1]))/2 + ((double)(tempgreen[0]-tempblue[0]))*Math.sqrt(3)/2 + 0.5) + temperror[1];

			if(		  tempsecondselected[0] > 0
					&&tempsecondselected[1] > 0
					&&tempsecondselected[0] < MyFrame.NUMBEROFPANEL - 2
					&&tempsecondselected[1] < MyFrame.NUMBEROFPANEL - 2
					){
				secondselected[0] = tempsecondselected[0];
				secondselected[1] = tempsecondselected[1];
				frame.setSecondSelected(secondselected);
			}

			//トラジェクタと目標位置がセットされたはずなので、移動する
			if(frame.getSecondSelected()[0] > 0 && frame.getSecondSelected()[1] > 0){
				frame.pushSPACE();
				frame.pushGoal();
				count++;
			}
		}


	}
	//MOVE_THE_CENTERの自動生成
	public void generate_MOVE_THE_CENTER(MyFrame frame,double variance){

		int[] temperror = new int[2];
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
			temperror[0] = (int)nextError(variance);
			temperror[1] = (int)nextError(variance);

			secondselected[0] = MyFrame.NUMBEROFPANEL/2+temperror[0];
			secondselected[1] = MyFrame.NUMBEROFPANEL/2+temperror[1];
			frame.setSecondSelected(secondselected);
			frame.pushSPACE();
			frame.pushGoal();
			count++;
		}


	}
	//MOVE_AT_RANDOMの自動生成(識別時にのみ利用)
	public void generate_MOVE_AT_RANDOM(MyFrame frame,double variance){

		int[] temperror = new int[2];
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
			temperror[0] = (int)nextError(variance);
			temperror[1] = (int)nextError(variance);

			//目標位置はランダム
			while(true){
				secondselected[0] = (int)((MyFrame.NUMBEROFPANEL-4)*Math.random());
				secondselected[1] = (int)((MyFrame.NUMBEROFPANEL-4)*Math.random());
				if(frame.panels[secondselected[0]][secondselected[1]].getStatus() == 0){
					break;
				}
			}
			frame.setSecondSelected(secondselected);
			frame.pushSPACE();
			frame.pushGoal();
			count++;
		}


	}

}
