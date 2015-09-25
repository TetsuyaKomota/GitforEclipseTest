package komota.test;

public class TestHMM {

	//HMM
	//状態数、状態、行動数、行動、出力数、出力、状態遷移確率、各状態からの出力確率、出力の事前確率、初期状態確率を持つ

//定数

//リスト
	//状態数
	int numstatus = 0;
	//状態
	int[] status = null;
	//行動数
	int numaction = 0;
	//行動
	int[] action = null;
	//出力数
	int numoutput = 0;
	//出力
	int[] output = null;
	//状態遷移確率([この状態で][この行動をとると][この状態になる]確率)
	double[][][] protransition = null;
	//出力確率([この状態で][この出力を得る]確率)
	double[][] prooutput = null;


//フィールド
//すべてにおいて、-1は不正
	//現在の状態（不可視）
	int curstatus = -1;
	//現在の行動
	int curaction = -1;
	//現在の出力
	int curoutput = -1;

	//コンストラクタ
	public TestHMM(int numstatus,int numaction,int numoutput){
		this.numaction = numaction;
		this.numoutput = numoutput;
		this.numstatus = numstatus;
		this.prooutput = new double[numstatus][numoutput];

		for(int i=0;i<numstatus;i++){
			for(int j=0;j<numoutput;j++){
				//理由不十分の原理
				this.prooutput[i][j] = 1/numoutput;
			}
		}

		this.protransition = new double[numstatus][numaction][numstatus];
		for(int i=0;i<numstatus;i++){
			for(int j=0;j<numaction;j++){
				for(int k=0;k<numstatus;k++){
					//理由不十分の原理
					this.protransition[i][j][k] = 1/numstatus;
				}
			}
		}
	}

	//「散歩」「洗濯」「読書」が「晴れ」「晴れ」「雨」から出力したことを推定する

	//ある出力列Oが出力するのに最も尤もらしいビタビ経路を出力する

	//ある出力oが出力するのに最も尤もらしい状態を推定する

	//

/*
・HMMのシミュレーションについて

・とりあえず、この前のサイトそのまんま実装する

・「散歩」が「晴れ」から出た結果であると推測する手順


s'_n = argmax(P(s_n = s'_n|o_n = "晴れ"))

P(s_n = s'_n|o_n = "晴れ") = P(o_n = "晴れ"|s_n = s'_n)P(o_n = "晴れ")/P(s_n = s'_n)

 */

}
