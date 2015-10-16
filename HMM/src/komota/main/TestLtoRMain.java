package komota.main;


public class TestLtoRMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ

		//あのプリントの例を再現

		System.out.println("*******************************************************************************************");
		LtoRHMM hmm = new LtoRHMM(4,3);
		//状態遷移確率を設定
		double[][] temptrans = new double[4][4];
		temptrans[0][0] = 0.7;
		temptrans[0][1] = 0.3;
		temptrans[0][2] = 0;
		temptrans[0][3] = 0;
		temptrans[1][0] = 0;
		temptrans[1][1] = 0.6;
		temptrans[1][2] = 0.4;
		temptrans[1][3] = 0;
		temptrans[2][0] = 0;
		temptrans[2][1] = 0;
		temptrans[2][2] = 0.1;
		temptrans[2][3] = 0.9;
		temptrans[3][0] = 0;
		temptrans[3][1] = 0;
		temptrans[3][2] = 0;
		temptrans[3][3] = 1;

		hmm.setProTransition(temptrans);

		//出力確率を設定
		double[][] tempout = new double[4][3];
		tempout[0][0] = 0.7;
		tempout[0][1] = 0.2;
		tempout[0][2] = 0.1;
		tempout[1][0] = 0.4;
		tempout[1][1] = 0.3;
		tempout[1][2] = 0.3;
		tempout[2][0] = 0.1;
		tempout[2][1] = 0.1;
		tempout[2][2] = 0.8;
		tempout[3][0] = 0;
		tempout[3][1] = 0;
		tempout[3][2] = 1;

		hmm.setProOutput(tempout);

		int[] tempinputs = {0,0,1,1,2,2,1,1,0,0};

//		System.out.println("Current likelihood:"+hmm.getHMMLikelihood(tempinputs));
//		hmm.show();
		hmm.learnwithBaum_Welch(tempinputs);
		hmm.show();
		//学習したパラメータを用いて出力列を再現
		hmm.initialize();
		System.out.print("Reproduction:");
		while(true){
			System.out.print(hmm.output()+" ");
			hmm.transition();
			if(hmm.getCurStatus() == hmm.numstatus-1)break;
		}
		System.out.println("");
//		System.out.println("Next likelihood:"+hmm.getHMMLikelihood(tempinputs));

		//複数の出力列からの学習のテスト
		System.out.println("Test for learning from some outputs");
		LtoRHMM hmm2 = new LtoRHMM(4,3);
		int[] temp1 = {0,0,0,0,0,0,1,1,1,1,2,2};
		int[] temp2 = {0,0,0,1,1,1,2,2,2,2,2,2};
		int[] temp3 = {0,0,0,0,1,1,1,1,1,2,2,2};
		int[] temp4 = {0,0,0,1,1,1,1,1,2,2,2,2};
		int[] temp5 = {0,0,0,0,1,1,1,1,2,2,2,2};

		hmm2.learnwithBaum_Welch(temp1);
		hmm2.learnwithBaum_Welch(temp2);
		hmm2.learnwithBaum_Welch(temp3);
		hmm2.learnwithBaum_Welch(temp4);
		hmm2.learnwithBaum_Welch(temp5);
		int count = 0;
		while(count++ < 100){
			hmm2.initialize();
			System.out.print("Reproduction:");
			while(true){
				System.out.print(hmm2.output()+" ");
				hmm2.transition();
				if(hmm2.getCurStatus() == hmm2.numstatus-1)break;
			}
			System.out.println("");
		}
		hmm2.show();

	}
}
