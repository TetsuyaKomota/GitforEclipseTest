package komota.main;

import komota.test.TestLeft_to_Right_HMM;

public class TestLtoRMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ

		//あのプリントの例を再現

		System.out.println("*******************************************************************************************");
		TestLeft_to_Right_HMM hmm = new TestLeft_to_Right_HMM(4,3);
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

		hmm.show();
		hmm.learnwithBaum_Welch(tempinputs);
		hmm.show();
	}
}
