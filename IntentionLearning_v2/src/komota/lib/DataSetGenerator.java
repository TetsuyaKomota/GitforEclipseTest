package komota.lib;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

import komota.supers.MyFrame;


public class DataSetGenerator extends MyFrame{

	//データセットを自動生成するクラス

	public static void main(String[] args){

		DataSetGenerator g = new DataSetGenerator();
		g.numberofdataset = 10;

	}


	@Override
	public void functionPlugin1(){
		System.out.println("データジェネレート！");
		//（おそらく）NextGaussianをintにキャストして使っているので，分散の有効桁数は高々小数2桁まで
//		this.generate_MOVE_THE_CENTER(this, 2);
//		this.generate_RIGHT_TO_BLUE(this, 0.2);
		this.generate_NEAR_BY_ORANGE(this, 0.2);
//		this.generate_AWAY_FROM_GREEN(this, 2);
//		this.generate_MAKE_THE_SIGNAL(this, 2);
//		this.generate_MAKE_THE_TRIANGLE(this, 2);
//		this.generate_RIGHT_TO_(DataSetGenerator.YELLOW, this, 2);
//		this.generate_NEAR_BY_(DataSetGenerator.YELLOW, this, 2);
//		this.generate_AWAY_FROM_(DataSetGenerator.YELLOW, this, 2);
//		this.generate_MAKE_THE_SIG_(DataSetGenerator.YELLOW, DataSetGenerator.GREEN, this, 2);
//		this.generate_MAKE_THE_TRI_(DataSetGenerator.YELLOW, DataSetGenerator.BLUE, this, 2);
		System.out.println("データジェネレート完了！");
		try {
			System.out.println("logdata.txtには"+this.countGoal("logdata.txt")+"個のデータが存在します");
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		this.getMyIO().close();

	}
	@Override
	public void functionPlugin2(){
		try {
			System.out.println("logdata.txtには"+this.countGoal("logdata.txt")+"個のデータが存在します");
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

	}

	//生成するデータセット量
	int numberofdataset = 10;
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
	public DataSetGenerator(int numberofdata){
		this.numberofdataset = numberofdata;
		this.rand = new Random();
	}
	public DataSetGenerator(){
		this(10);
	}

	//生成データ量のセッター
	public void setNumberofData(int num){
		this.numberofdataset = num;
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

		while(count < numberofdataset){

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
			for(int i=0;i<Statics.NUMBEROFPANEL;i++){
				for(int j=0;j<Statics.NUMBEROFPANEL;j++){
					if(frame.getPanels()[i][j].getStatus() == 1){
						selected[0] = i;
						selected[1] = j;
						frame.setSelected(selected);
					}
					else if(frame.getPanels()[i][j].getStatus() == 2){

						//青の右＋ガウス誤差 の位置が画面内かどうか
						temperror[0] = (int)nextError(variance);
						temperror[1] = (int)nextError(variance);

						if(		     i+temperror[0] > 0
								&&15+j+temperror[1] > 0
								&&   i+temperror[0] < Statics.NUMBEROFPANEL-2
								&&15+j+temperror[1] < Statics.NUMBEROFPANEL-2){
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

		while(count < numberofdataset){

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
			for(int i=0;i<Statics.NUMBEROFPANEL;i++){
				for(int j=0;j<Statics.NUMBEROFPANEL;j++){
					if(frame.getPanels()[i][j].getStatus() == 1){
						selected[0] = i;
						selected[1] = j;
						frame.setSelected(selected);
					}
					else if(frame.getPanels()[i][j].getStatus() == object){

						//青の右＋ガウス誤差 の位置が画面内かどうか
						temperror[0] = (int)nextError(variance);
						temperror[1] = (int)nextError(variance);

						if(		     i+temperror[0] > 0
								&&15+j+temperror[1] > 0
								&&   i+temperror[0] < Statics.NUMBEROFPANEL-2
								&&15+j+temperror[1] < Statics.NUMBEROFPANEL-2){
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

		while(count < numberofdataset){

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
			for(int i=0;i<Statics.NUMBEROFPANEL;i++){
				for(int j=0;j<Statics.NUMBEROFPANEL;j++){
					if(frame.getPanels()[i][j].getStatus() == 1){
						selected[0] = i;
						selected[1] = j;
						frame.setSelected(selected);
						tempred[0] = i;
						tempred[1] = j;
					}
					else if(frame.getPanels()[i][j].getStatus() == 5){

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
					&&tempsecondselected[0] < Statics.NUMBEROFPANEL - 2
					&&tempsecondselected[1] < Statics.NUMBEROFPANEL - 2
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

		while(count < numberofdataset){

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
			for(int i=0;i<Statics.NUMBEROFPANEL;i++){
				for(int j=0;j<Statics.NUMBEROFPANEL;j++){
					if(frame.getPanels()[i][j].getStatus() == 1){
						selected[0] = i;
						selected[1] = j;
						frame.setSelected(selected);
						tempred[0] = i;
						tempred[1] = j;
					}
					else if(frame.getPanels()[i][j].getStatus() == object){

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
					&&tempsecondselected[0] < Statics.NUMBEROFPANEL - 2
					&&tempsecondselected[1] < Statics.NUMBEROFPANEL - 2
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

		while(count < numberofdataset){

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
			for(int i=0;i<Statics.NUMBEROFPANEL;i++){
				for(int j=0;j<Statics.NUMBEROFPANEL;j++){
					if(frame.getPanels()[i][j].getStatus() == 1){
						selected[0] = i;
						selected[1] = j;
						frame.setSelected(selected);
						tempred[0] = i;
						tempred[1] = j;
					}
					else if(frame.getPanels()[i][j].getStatus() == 4){

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
					&&tempsecondselected[0] < Statics.NUMBEROFPANEL - 2
					&&tempsecondselected[1] < Statics.NUMBEROFPANEL - 2
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

		while(count < numberofdataset){

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
			for(int i=0;i<Statics.NUMBEROFPANEL;i++){
				for(int j=0;j<Statics.NUMBEROFPANEL;j++){
					if(frame.getPanels()[i][j].getStatus() == 1){
						selected[0] = i;
						selected[1] = j;
						frame.setSelected(selected);
						tempred[0] = i;
						tempred[1] = j;
					}
					else if(frame.getPanels()[i][j].getStatus() == object){

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
					&&tempsecondselected[0] < Statics.NUMBEROFPANEL - 2
					&&tempsecondselected[1] < Statics.NUMBEROFPANEL - 2
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

		while(count < numberofdataset){

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
			for(int i=0;i<Statics.NUMBEROFPANEL;i++){
				for(int j=0;j<Statics.NUMBEROFPANEL;j++){
					if(frame.getPanels()[i][j].getStatus() == 1){
						selected[0] = i;
						selected[1] = j;
						frame.setSelected(selected);
						tempred[0] = i;
						tempred[1] = j;
					}
					else if(frame.getPanels()[i][j].getStatus() == 2){
						tempblue[0] = i;
						tempblue[1] = j;
					}
					else if(frame.getPanels()[i][j].getStatus() == 3){
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
					&&tempsecondselected[0] < Statics.NUMBEROFPANEL - 2
					&&tempsecondselected[1] < Statics.NUMBEROFPANEL - 2
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

		while(count < numberofdataset){

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
			for(int i=0;i<Statics.NUMBEROFPANEL;i++){
				for(int j=0;j<Statics.NUMBEROFPANEL;j++){
					if(frame.getPanels()[i][j].getStatus() == 1){
						selected[0] = i;
						selected[1] = j;
						frame.setSelected(selected);
						tempred[0] = i;
						tempred[1] = j;
					}
					else if(frame.getPanels()[i][j].getStatus() == rightobject){
						tempblue[0] = i;
						tempblue[1] = j;
					}
					else if(frame.getPanels()[i][j].getStatus() == centerobject){
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
					&&tempsecondselected[0] < Statics.NUMBEROFPANEL - 2
					&&tempsecondselected[1] < Statics.NUMBEROFPANEL - 2
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

		while(count < numberofdataset){

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
			for(int i=0;i<Statics.NUMBEROFPANEL;i++){
				for(int j=0;j<Statics.NUMBEROFPANEL;j++){
					if(frame.getPanels()[i][j].getStatus() == 1){
						selected[0] = i;
						selected[1] = j;
						frame.setSelected(selected);
						tempred[0] = i;
						tempred[1] = j;
					}
					else if(frame.getPanels()[i][j].getStatus() == 2){
						tempblue[0] = i;
						tempblue[1] = j;
					}
					else if(frame.getPanels()[i][j].getStatus() == 4){
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
					&&tempsecondselected[0] < Statics.NUMBEROFPANEL - 2
					&&tempsecondselected[1] < Statics.NUMBEROFPANEL - 2
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

		while(count < numberofdataset){

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
			for(int i=0;i<Statics.NUMBEROFPANEL;i++){
				for(int j=0;j<Statics.NUMBEROFPANEL;j++){
					if(frame.getPanels()[i][j].getStatus() == 1){
						selected[0] = i;
						selected[1] = j;
						frame.setSelected(selected);
						tempred[0] = i;
						tempred[1] = j;
					}
					else if(frame.getPanels()[i][j].getStatus() == rightobject){
						tempblue[0] = i;
						tempblue[1] = j;
					}
					else if(frame.getPanels()[i][j].getStatus() == leftobject){
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
					&&tempsecondselected[0] < Statics.NUMBEROFPANEL - 2
					&&tempsecondselected[1] < Statics.NUMBEROFPANEL - 2
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

		while(count < numberofdataset){

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
			for(int i=0;i<Statics.NUMBEROFPANEL;i++){
				for(int j=0;j<Statics.NUMBEROFPANEL;j++){
					if(frame.getPanels()[i][j].getStatus() == 1){
						selected[0] = i;
						selected[1] = j;
						frame.setSelected(selected);
					}
				}
			}
			temperror[0] = (int)nextError(variance);
			temperror[1] = (int)nextError(variance);

			secondselected[0] = Statics.NUMBEROFPANEL/2+temperror[0];
			secondselected[1] = Statics.NUMBEROFPANEL/2+temperror[1];
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

		while(count < numberofdataset){

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
			for(int i=0;i<Statics.NUMBEROFPANEL;i++){
				for(int j=0;j<Statics.NUMBEROFPANEL;j++){
					if(frame.getPanels()[i][j].getStatus() == 1){
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
				secondselected[0] = (int)((Statics.NUMBEROFPANEL-4)*Math.random());
				secondselected[1] = (int)((Statics.NUMBEROFPANEL-4)*Math.random());
				if(frame.getPanels()[secondselected[0]][secondselected[1]].getStatus() == 0){
					break;
				}
			}
			frame.setSecondSelected(secondselected);
			frame.pushSPACE();
			frame.pushGoal();
			count++;
		}
	}

	/* *************************************************************************************************** */
	//引数で与えたデータの教示回数（goalの数）を数える
	public int countGoal(String filename) throws IOException{
		int output = 0;

		File file = new File("log/"+filename);
		BufferedReader br = null;

		br = new BufferedReader(new FileReader(file));

		String line = br.readLine();

		while(line != null){
			if(line.split(",")[0].equals("goal  ")){
				output++;
			}
			line = br.readLine();
		}
		br.close();

		return output;
	}


}
