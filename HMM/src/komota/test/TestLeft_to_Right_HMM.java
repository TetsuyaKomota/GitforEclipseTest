package komota.test;

import komota.main.HMM;

public class TestLeft_to_Right_HMM extends HMM{

	public TestLeft_to_Right_HMM(int numstatus, int numoutput) {
		super(numstatus, numoutput);
		// TODO 自動生成されたコンストラクター・スタブ

		//Left_to_Rightは状態番号0からスタート前提なので、初期状態確率を設定し直す。
		this.proinitial[0] = 1.0;
		for(int i=1;i<this.proinitial.length;i++){
			this.proinitial[i] = 0;
		}

		//Left_to_Rightは遷移は自分自身か一つ先の状態にしか行えないので、状態遷移確率を設定し直す。
		for(int i=0;i<this.numstatus;i++){
			for(int j=0;j<this.numstatus;j++){
				if(i == j || i+1 == j){
					this.protransition[i][j] = 0.5;
				}
				else{
					this.protransition[i][j] = 0;
				}
			}
		}

	}

	//初期状態は変更できないようにする
	@Override
	public HMM setProInitial(double[] inputs){
		System.out.println("[Left_to_Right_HMM]このメッセージは出ないはずだよ");
		return null;
	}

	//状態遷移確率を変更するセッターが実行された際、隣の状態への遷移と自分自身への遷移以外はゼロにして射影する。
	@Override
	public HMM setProTransition(double[][] inputs){
		//入力が正規か確認する
		if(inputs.length != this.numstatus || inputs[0].length != this.numstatus){
			System.out.println("[Left_to_Right_HMM]setProTransition:It's invalid inputs");
			return null;
		}
		//更新する
		double temp = 0;
		for(int i=0;i<this.numstatus;i++){
			if(i < numstatus - 1){
				temp = inputs[i][i] + inputs[i][i+1];
			}
			else{
				temp = inputs[i][i];
			}
			this.protransition[i][i] = inputs[i][i] / temp;
			if(i < numstatus - 1){
				this.protransition[i][i + 1] = inputs[i][i + 1] / temp;
			}
		}

		return null;
	}

	//バウム＝ウェルチアルゴリズムにより、パラメータの学習を行う。
	public void learnwithBaum_Welch(double[] inputs){

	}


}
