package komota.main;

import komota.test.TestLeft_to_Right_HMM;



public class testMain {

	/**
	 * @param args
	 */

	//出力の定数
	public static final int SUNNY = 0;
	public static final int CLOUDY = 1;
	public static final int RAINNY = 2;

	//行動の定数
	public final int WALKING = 0;
	public final int WASHING = 1;
	public final int READING = 2;



	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ

		/*
		 * 以下テスト設定
		 *・	状態	初期確率（事前確率）遷移握率	晴れ	曇り	雨	出力確率	散歩	読書
		 *		0.晴れ	0.8								0.8		0.2		0.0				0.9		0.1
		 *		1.曇り	0.15							0.0		0.2		0.8				0.5		0.5
		 *		2.雨	0.05							0.8		0.2		0.0				0.0		1.0
		 *
		 *{0,0,1,0,0,1} (散歩、散歩、読書、散歩、散歩、読書)となるのに最も尤もらしい天気遷移を推定する
		 *
		 */




		System.out.println("Hello HMM!");

		HMM hmm = new TestLeft_to_Right_HMM(3, 2);
		//初期状態設定
		double[] inits = {0.8,0.15,0.05};
		hmm.setProInitial(inits);
		//状態遷移確率設定
		double[][] trans = new double[3][3];
		trans[0][0] = 0.8;
		trans[0][1] = 0.2;
		trans[0][2] = 0.0;
		trans[1][0] = 0.0;
		trans[1][1] = 0.2;
		trans[1][2] = 0.8;
		trans[2][0] = 0.8;
		trans[2][1] = 0.2;
		trans[2][2] = 0.0;
		hmm.setProTransition(trans);
		//出力確率設定
		double[][] outp = new double[3][2];
		outp[0][0] = 0.9;
		outp[0][1] = 0.1;
		outp[1][0] = 0.5;
		outp[1][1] = 0.5;
		outp[2][0] = 0.0;
		outp[2][1] = 1.0;
		hmm.setProOutput(outp);
		//出力列
		int[] test 		= {0,0,1,0,0,1};
		int[] test2 	= {1,1,1,1,1,0,0,0,1,1,1};
		int[] test3		= {0,0,0,0,0,0,1,0,0,0,1,1,1,1,1};

		//初期状態確率のテスト
		System.out.println("Test for initialize");
		int zero	 = 0;
		int one		 = 0;
		int two		 = 0;
		for(int i=0;i<20;i++){
			for(int j=0;j<20;j++){
				hmm.initialize();
				if(hmm.getCurStatus() == 0){
					zero++;
				}else if(hmm.getCurStatus() == 1){
					one++;
				}else if(hmm.getCurStatus() == 2){
					two++;
				}
				System.out.print(hmm.getCurStatus()+" ");
			}
			System.out.println("");
		}
		System.out.println("Total   0(晴):"+zero+"  1(曇):"+one+"  2(雨):"+two);

		//出力確率のテスト
		System.out.println("Test for output");
		hmm.initialize();
		System.out.println(hmm.getCurStatus()+" ");
		for(int i=0;i<20;i++){
			for(int j=0;j<20;j++){
				System.out.print(hmm.output()+" ");
			}
			System.out.println("");
		}

		//状態遷移確率のテスト
		System.out.println("Test for transition");
		hmm.initialize();
		System.out.println(hmm.getCurStatus()+" ");
		for(int i=0;i<20;i++){
			for(int j=0;j<20;j++){
				hmm.transition();
				System.out.print(hmm.getCurStatus()+" ");
			}
			System.out.println("");
		}

		//ビタビ経路推定のテスト
		System.out.println("Test for Bitabi");
		int[] testbitabi = hmm.getBitabi(test);
		for(int i=0;i<testbitabi.length;i++){
			System.out.print(testbitabi[i] + "  ");
		}
		System.out.println("");

		testbitabi = hmm.getBitabi(test2);
		for(int i=0;i<testbitabi.length;i++){
			System.out.print(testbitabi[i] + "  ");
		}
		System.out.println("");

		testbitabi = hmm.getBitabi(test3);
		for(int i=0;i<testbitabi.length;i++){
			System.out.print(testbitabi[i] + "  ");
		}
		System.out.println("");

		//モデル尤度のテスト
		System.out.println("Test for HMMLikelihood");
		int[] testoutputsforHMMlikelihood = {0,0,0,0,1,1,1,1};
		for(int i=0;i<testoutputsforHMMlikelihood.length;i++){
			System.out.print(testoutputsforHMMlikelihood[i]+" ");
		}
		System.out.println("");
		System.out.println(hmm.getHMMLikelihood(testoutputsforHMMlikelihood));
	}

}
