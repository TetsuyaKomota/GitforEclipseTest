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
	public void learnwithBaum_Welch(int[] inputs){
		//grid空間を生成([この状態に][この時点でいることの評価値])
		double[][] forwardgrid = new double[this.numstatus][inputs.length+1];
		double[][] backwardgrid = new double[this.numstatus][inputs.length+1];
		//現在の確率における尤度を計算する
		double templikelihood = this.getHMMLikelihood(inputs);
		int loopcount = 0;

		while(loopcount++ < 100){
		//現在の確率から、gridの値を計算
		//最初にforwardgridの計算
		for(int j=0;j<inputs.length+1;j++){
			for(int i=0;i<this.numstatus;i++){
				//開始直後は状態番号0にいるはずなので、grid[0][0] = 1 以外はgrid[i][0] = 0 である
				if(j == 0){
					if(i == 0){
						forwardgrid[i][j] = 1;
					}
					else{
						forwardgrid[i][j] = 0;
					}
				}
				//最終状態は最終時点でのみ到達でき、また最終時点では必ず最終状態に到達することから、関係ないgrid値をゼロにする
				else if(i == this.numstatus - 1){
					if(j == inputs.length + 1 - 1){
						forwardgrid[i][j] = 1;
					}
					else{
						forwardgrid[i][j] = 0;
					}
				}
				//「残り時点数」が「残り状態数」未満の場合、最終状態に到達できないため、grid値をゼロにする

				else if((inputs.length + 1 - j) < (this.numstatus - i)){
					forwardgrid[i][j] = 0;
				}

				else{
					//開始直後以外のgridは、「前時点のiのgrid * iからiへの遷移確率 * iでのinputs[j]の出力確率」+ 「前時点でのi-1のgrid * i-1からiへの遷移確率 * i-1でのinputs[j]の出力確率」
//					System.out.print("forwardgrid["+i+"]["+j+"]:"+forwardgrid[i][j-1]+"*"+this.protransition[i][i]+"*"+this.prooutput[i][inputs[j]]);
					forwardgrid[i][j] = forwardgrid[i][j-1] * this.protransition[i][i] * this.prooutput[i][inputs[j-1]];
					//状態番号0は例外として除去しておく
					if(i > 0){
						forwardgrid[i][j] += forwardgrid[i-1][j-1] * this.protransition[i-1][i] * this.prooutput[i-1][inputs[j-1]];
//						System.out.print(" + "+forwardgrid[i-1][j-1] +"*"+ this.protransition[i-1][i] +"*"+ this.prooutput[i-1][inputs[j-1]]);
					}
//					System.out.println("");
				}
			}
		}
		//forwardgridの値を確認
//		showGrid(forwardgrid);

		//次にbackwardgridの計算
		for(int j=inputs.length + 1 - 1;j >= 0;j--){
			for(int i=this.numstatus - 1;i >= 0;i--){
				//終了直後は状態番号numstatus-1にいるはずなので、grid[numstatus-1][inputs.length-1] = 1 以外はgrid[i][inputs.length-1] = 0 である
				if(j == inputs.length + 1 - 1 || i == this.numstatus - 1){
					if(j == inputs.length + 1 - 1 && i == this.numstatus - 1){
						backwardgrid[i][j] = 1;
					}
					else{
						backwardgrid[i][j] = 0;
					}
				}
				else if(j == 0 && i != 0){
					backwardgrid[i][j] = 0;
				}
				else{
					//終了直後以外のgridは、「次時点のiのgrid * iからiへの遷移確率 * iでのinputs[j]の出力確率」+ 「前時点でのi+1のgrid * iからi+1への遷移確率 * iでのinputs[j]の出力確率」
//					System.out.print("backwardgrid["+i+"]["+j+"]:"+backwardgrid[i][j+1]+"*"+this.protransition[i][i]+"*"+this.prooutput[i][inputs[j]]);
					backwardgrid[i][j] = backwardgrid[i][j+1] * this.protransition[i][i] * this.prooutput[i][inputs[j]];
					//状態番号numstatus-1は例外として除去しておく
					if(i < this.numstatus-1){
//						System.out.print(" + "+backwardgrid[i+1][j+1] +"*"+ this.protransition[i][i+1] +"*"+ this.prooutput[i][inputs[j]]);
						backwardgrid[i][j] += backwardgrid[i+1][j+1] * this.protransition[i][i+1] * this.prooutput[i][inputs[j]];
					}
//					System.out.println("");
				}
			}
		}
		//backwardgridの値を確認
//		showGrid(backwardgrid);

		//gridの計算が完了したので、次はこれをもとにΓを求める
		double[][][] G = new double[inputs.length][this.numstatus][this.numstatus];

		for(int i=0;i<inputs.length;i++){
			for(int j=0;j<this.numstatus;j++){
				for(int k=0;k<this.numstatus;k++){
					G[i][j][k] = (forwardgrid[j][i]*this.protransition[j][k]*this.prooutput[j][inputs[i]]*backwardgrid[k][i+1])/templikelihood;
				}
			}
		}
		//Γの計算が完了したので、次はこれをもとに状態遷移確率と出力確率を再計算する
		//まずは状態遷移確率
		for(int i=0;i<this.numstatus;i++){
			for(int j=0;j<this.numstatus;j++){

				//状態遷移確率、出力確率ともに最終状態での結果が死んでいるため、そこだけ例外で設定
				if(i == this.numstatus - 1){
					if(j == this.numstatus - 1){
						this.protransition[i][j] = 1;
					}
					else{
						this.protransition[i][j] = 0;
					}
				}
				else{
					double temptop = 0;
					double tempbuttom = 0;
					for(int si=0;si<inputs.length;si++){
						double temp = 0;
						temptop += G[si][i][j];
						for(int sj=0;sj<this.numstatus;sj++){
							temp += G[si][i][sj];
						}
						tempbuttom += temp;
					}
					this.protransition[i][j] = temptop/tempbuttom;
				}
			}
		}
		//次は出力確率
		for(int i=0;i<this.numstatus;i++){
			for(int j=0;j<this.numoutput;j++){

				//状態遷移確率、出力確率ともに最終状態での結果が死んでいるため、そこだけ例外で設定
				if(i == this.numstatus - 1){
					if(j == this.numoutput - 1){
						this.prooutput[i][j] = 1;
					}
					else{
						this.prooutput[i][j] = 0;
					}
				}
				else{
					double temptop = 0;
					double tempbuttom = 0;
					for(int si=0;si<inputs.length;si++){
						for(int sj=0;sj<this.numstatus;sj++){
							if(inputs[si] == j){
								temptop += G[si][i][sj];
							}
							tempbuttom += G[si][i][sj];
						}
					}
					this.prooutput[i][j] = temptop/tempbuttom;
				}
			}
		}

		//再度尤度を計算、尤度を更新していなければ終了

		double nextlikelihood = this.getHMMLikelihood(inputs);
		System.out.println("[Left_to_Right_HMM]learnwithBaumWelch:Likelihood:"+nextlikelihood);

		if(nextlikelihood <= templikelihood){
			//break;
		}
		else{
			templikelihood = nextlikelihood;
		}

		}//while文の終端
	}

	/* ****************************************************************************************************************************** */
	//テスト用。gridの中身を表示する
	private void showGrid(double[][] grid){
		System.out.println("Show Grid");
		for(int i=0;i<grid.length;i++){
			for(int j=0;j<grid[0].length;j++){
				System.out.printf("%.5f  ",grid[i][j]);
			}
			System.out.println("");
		}
	}
	/* ****************************************************************************************************************************** */


}
