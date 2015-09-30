package komota.main;

import komota.test.TestHMM;

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

		System.out.println("Hello HMM!");

		TestHMM hmm = new TestHMM(3, 3);
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

		//初期状態確率のテスト
		System.out.println("Test for initialize");
		for(int i=0;i<20;i++){
			for(int j=0;j<20;j++){
				hmm.initialize();
				System.out.print(hmm.getCurStatus()+" ");
			}
			System.out.println("");
		}

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

	}

}
