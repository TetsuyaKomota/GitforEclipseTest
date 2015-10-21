package komota.main;


public class TestLtoRMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ

		//あのプリントの例を再現

		System.out.println("*******************************************************************************************");
/*
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

		int[] tempinputs = {0,0,1,2};

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
		LtoRHMM hmm2 = new LtoRHMM(4,100);
		int[] temp1 = {0,0,0,0,0,1,1,1,1,1,1,2,2,2,2,2,2,2,3,3,3,3,3,3,3,4,4,4,4,4,4};
		int[] temp2 = {0,0,0,1,1,1,1,1,1,1,2,2,2,2,2,2,2,3,3,3,3,3,3,3,4,4,4,4,4,4,4};
		int[] temp3 = {0,0,0,0,0,0,1,1,1,1,1,1,1,1,2,2,2,2,2,2,2,2,3,3,3,3,3,4,4,4,4};
		int[] temp4 = {0,0,0,0,0,0,0,1,1,1,1,1,1,1,2,2,2,2,2,2,3,3,3,3,3,3,3,4,4,4,4};
		int[] temp5 = {0,0,0,0,0,0,1,1,1,1,1,2,2,2,2,2,3,3,3,3,3,3,4,4,4,4,4,4,4,4,4};


		int[] tempa = temp1;
		int[] tempb = temp2;
		int[] tempc = temp3;
		int[] tempd = temp4;

		System.out.println("temp1");
		hmm2.learnwithBaum_Welch(tempa);
		System.out.println("temp2");
		hmm2.learnwithBaum_Welch(tempb);
		//		hmm2.show();
		System.out.println("temp3");
		hmm2.learnwithBaum_Welch(tempc);
		System.out.println("temp4");
		hmm2.learnwithBaum_Welch(tempd);
		System.out.println("temp5");
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
		//		hmm2.show();

		LtoRHMM hmm3 = new LtoRHMM(10,4);

		int[] temps0 = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,2,2,2,2,2,2,2,2,2,2,2,2,3,3,3,3,3,3,3,3,3,3,3,3,3};
		int[] temps1 = {0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,2,2,2,2,2,2,2,2,2,2,2,2,3,3,3,3,3,3,3,3,3,3,3,3,3};
		int[] temps2 = {0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,2,2,2,2,2,2,2,2,2,2,2,3,3,3,3,3,3,3,3,3,3};
		int[] temps3 = {0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,2,2,2,2,2,2,2,2,2,2,3,3,3,3,3,3,3,3,3,3,3,3,3,3};
		int[] temps4 = {0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,2,2,2,2,2,2,2,2,2,2,2,3,3,3,3,3,3,3,3,3};
		int[] temps5 = {0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,2,2,2,2,2,2,2,2,2,2,2,3,3,3,3,3,3,3,3,3,3,3,3};
		int[] temps6 = {0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,2,2,2,2,2,2,2,2,2,2,2,3,3,3,3,3,3,3,3,3,3};
		int[] temps7 = {0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,3,3,3,3,3,3,3,3};
		int[] temps8 = {0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,2,2,2,2,2,2,2,2,2,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3};
		int[] temps9 = {0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,2,2,2,2,2,2,2,2,2,2,2,3,3,3,3,3,3,3,3,3,3,3};
		int[] temps10 = {0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,2,2,2,2,2,2,2,2,2,2,2,3,3,3,3,3,3,3,3,3,3,3,3};
		int[] temps11 = {0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,2,2,2,2,2,2,2,2,2,2,2,3,3,3,3,3,3,3,3,3};
		int[] temps12 = {0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,2,2,2,2,2,2,2,2,2,3,3,3,3,3,3,3,3,3,3,3,3,3};
		int[] temps13 = {0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,2,2,2,2,2,2,2,2,2,2,3,3,3,3,3,3,3,3};
		int[] temps14 = {0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,2,2,2,2,2,2,2,2,2,2,3,3,3,3,3,3,3,3,3};

		int[][] tempss = new int[14 + 1][];

		tempss[0] = temps0;
		tempss[1] = temps1;
		tempss[2] = temps2;
		tempss[3] = temps3;
		tempss[4] = temps4;
		tempss[5] = temps5;
		tempss[6] = temps6;
		tempss[7] = temps7;
		tempss[8] = temps8;
		tempss[9] = temps9;
		tempss[10] = temps10;
		tempss[11] = temps11;
		tempss[12] = temps12;
		tempss[13] = temps13;
		tempss[14] = temps14;

		for(int i=0;i<tempss.length;i++){
			System.out.println("temps["+i+"]");
			hmm3.learnwithBaum_Welch(tempss[i]);
		}
		count = 0;
		hmm3.show();
		while(count++ < 100){
			hmm3.initialize();
			System.out.print("Reproduction:");
			while(true){
				System.out.print(hmm3.output()+" ");
				hmm3.transition();
				if(hmm3.getCurStatus() == hmm3.numstatus-1)break;
			}
			System.out.println("");
		}
*/

		System.out.println("Test for limit of status");

		int numstatus = 10;
		int numoutput  = numstatus * 100;

		LtoRHMM hmm4 = new LtoRHMM(numstatus,numoutput);

		int[] templimitoutput = new int[numstatus*10];
		for(int i=0;i<templimitoutput.length;i++){
			templimitoutput[i] = i/5;
		}
		hmm4.learnwithBaum_Welch(templimitoutput);
//		hmm4.show();
		int count = 0;
		while(count++ < 100){
			hmm4.initialize();
			System.out.print("Reproduction:");
			while(true){
				System.out.print(hmm4.output()+" ");
				hmm4.transition();
				if(hmm4.getCurStatus() == hmm4.numstatus-1)break;
			}
			System.out.println("");
		}

	}
}
