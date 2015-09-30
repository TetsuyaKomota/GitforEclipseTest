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
	//出力数
	int numoutput = 0;
	//出力
	int[] output = null;
	//初期状態確率
	double[] proinitial = null;
	//状態遷移確率([この状態から][この状態になる]確率)
	double[][] protransition = null;
	//出力確率([この状態で][この出力を得る]確率)
	double[][] prooutput = null;


//フィールド
//すべてにおいて、-1は不正
	//現在の状態（不可視）
	int curstatus = -1;
	//現在の出力
	int curoutput = -1;

	//コンストラクタ
	public TestHMM(int numstatus,int numoutput){
/*
		this.numaction = numaction;
*/
		this.numoutput = numoutput;
		this.numstatus = numstatus;
		this.prooutput = new double[numstatus][numoutput];
		this.proinitial = new double[numstatus];

		for(int i=0;i<numstatus;i++){
			//理由不十分の原理
			this.proinitial[i] = 1/numstatus;
			for(int j=0;j<numoutput;j++){
				//理由不十分の原理
				this.prooutput[i][j] = (double)1/numoutput;
			}
		}

		this.protransition = new double[numstatus][numstatus];
		for(int i=0;i<numstatus;i++){
			for(int k=0;k<numstatus;k++){
				//理由不十分の原理
				this.protransition[i][k] = (double)1/numstatus;
			}
		}
	}
/* ****************************************************************************************************** */
/*セッター、ゲッター                                                                                                                                                                                                                                                     */
	//現状態のゲッター。本来不可視
	public int getCurStatus(){
		return this.curstatus;
	}
	//状態遷移確率のセッター
	public TestHMM setProTransition(double[][] inputs){
		//正規化
		for(int i=0;i<this.numstatus;i++){
			double temp = 0;
			for(int j=0;j<this.numstatus;j++){
				temp += this.protransition[i][j];
			}
			for(int j=0;j<this.numstatus;j++){
				this.protransition[i][j] = inputs[i][j]/temp;
			}
		}
		return this;
	}

	//初期状態確率のセッター
	public TestHMM setProInitial(double[] inputs){
		//正規化
		double temp=0;
		for(int i=0;i<this.numstatus;i++){
			temp+=inputs[i];
		}
		for(int i=0;i<this.numstatus;i++){
			this.proinitial[i] = inputs[i]/temp;
		}
		return this;
	}
/* ****************************************************************************************************** */
	//HMMの初期化
	/*
	 * 初期状態確率に従って状態を選択
	 */
	public void initialize(){
		double random = Math.random();
		int idx = 0;
		double temp = this.proinitial[idx];
		while(random > temp){
			idx++;
			temp += this.proinitial[idx];
		}
		this.curstatus = idx;
	}

	//出力。
	//現在の状態から、出力確率に従って出力する
	public int output(){
		double random = Math.random();
		int idx = 0;
		double temp = this.prooutput[curstatus][idx];
		while(random > temp){
			idx++;
			temp += this.prooutput[curstatus][idx];
		}
	return idx;
	}

	//状態遷移。
	//現在の状態から、状態遷移確率に従って状態遷移する
	public void transition(){
		double random = Math.random();
		int idx = 0;
		double temp = this.protransition[curstatus][idx];
		while(random > temp){
			idx++;
			temp += this.protransition[curstatus][idx];
		}
		this.curstatus = idx;
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
