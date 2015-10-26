package komota.test;

import komota.main.LtoRHMM;

public class TestSerialGaussLtoRHMM extends LtoRHMM{

	//出力確率を混合ガウスモデルで扱えるバージョン
	//面倒だから既存の出力確率(prooutput)はそのまま残し、新たにprogaussoutputを作成

	//定数
	//GMMのガウス数
	public static int numberofgauss = 4;

	//フィールド
	//出力確率。（混合ガウスモデル）
	TestGMM[] progaussoutput = null;

	public TestSerialGaussLtoRHMM(int numstatus, int numoutput) {
		super(numstatus, numoutput);
		// TODO 自動生成されたコンストラクター・スタブ
		this.progaussoutput = new TestGMM[this.numstatus];
		for(int i=0;i<this.numstatus;i++){
			this.progaussoutput[i] = new TestGMM(TestSerialGaussLtoRHMM.numberofgauss);
		}
	}

	//出力確率のゲッター。GMMの確率密度関数を呼ぶ
	//よく考えたらGMMは多次元データほぼ前提なんだからオーバーライドするほどでもなかった？←1次元で実験するならした方がいい
	@Override
	public double getProOutput(int status,double output){
		double[] outputs = new double[1];
		outputs[0] = output;
		return this.progaussoutput[status].getGMMProbability(outputs);
	}
	//多次元データに対する出力確率のゲッター
	public double getProOutput(int status,double[] output){
		return this.progaussoutput[status].getGMMProbability(output);
	}
	//出力方法も新たに設定
	public double output_SerialHMM(){
		return this.progaussoutput[this.getCurStatus()].nextGaussian();
	}
	//一応、prooutputを用いたoutputは行えないようにしておく
	@Override
	public int output(){
		System.out.println("[TestSerialLtoRHMM]output:This method can't be used in \"Serial\" HMM.");
		return -1;
	}

	//バウム＝ウェルチアルゴリズムの引数を int[] から double[][] に変更したバージョンを作成する
	public void learnwithBaumWelch(double[][] inputs){
	}

}
