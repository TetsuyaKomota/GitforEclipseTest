package komota.main;

import komota.pr.main.PR_100_FE;
import komota.pr.main.PR_100_GL;
import komota.pr.main.PR_100_ID;
import komota.pr.main.PR_100_LT;

public class MyTaskPrimitive {

	//学習させたタスクごとのクラス
	//フィールド
	//学習させたPR
	PR_100_ID pr_ID;
	PR_100_LT pr_LT;
	PR_100_GL pr_GL;
	PR_100_FE pr_FE;

	//タスク名。識別時にこれを出力する
	String taskname;

	//コンストラクタ
	//インスタンス生成時に学習する
	public MyTaskPrimitive(String filename,String taskname){
		this.pr_ID = new PR_100_ID(9,filename);
		this.pr_LT = new PR_100_LT(9,filename);
		this.pr_GL = new PR_100_GL(9,filename);
		this.pr_FE = new PR_100_FE(9,filename);

		pr_ID.learnfromLog();
		pr_LT.learnfromLog();
		pr_GL.learnfromLog();
		pr_FE.learnfromLog();

		this.taskname = taskname;
	}
	//タスク名のゲッター
	public String getTaskName(){
		return this.taskname;
	}

	//最も尤もらしいPRのreproductionを実行する
	public void reproductionTask(MyFrame frame){
		System.out.println("[MyTask]reproductionTask:MaxLikelihood: ID:"+this.pr_ID.getMaxLikelihood()+" LT:"+pr_LT.getMaxLikelihood()+" GL:"+pr_GL.getMaxLikelihood()+" FE:"+pr_FE.getMaxLikelihood());
		if(this.pr_FE.getMaxLikelihood() > 0){
			this.pr_FE.reproduction(frame);
		}
		else if(this.pr_GL.getMaxLikelihood() >= this.pr_LT.getMaxLikelihood() && this.pr_GL.getMaxLikelihood() >= this.pr_ID.getMaxLikelihood()){
			this.pr_GL.reproduction(frame);
		}
		else if(this.pr_LT.getMaxLikelihood() >= this.pr_ID.getMaxLikelihood() && this.pr_LT.getMaxLikelihood() >= this.pr_GL.getMaxLikelihood()){
			this.pr_LT.reproduction(frame);
		}
		else{
			this.pr_ID.reproduction(frame);
		}
	}

	//最尤値を出力する
	//※※※本質的には何の意味もないことに気付いたのでコメントアウト
/*
	public double getMaxLikelihood(){
		double output = 0;
		if(this.pr_GL.getMaxLikelihood() >= this.pr_LT.getMaxLikelihood() && this.pr_GL.getMaxLikelihood() >= this.pr_ID.getMaxLikelihood()){
			output = this.pr_GL.getMaxLikelihood();
		}
		else if(this.pr_LT.getMaxLikelihood() >= this.pr_ID.getMaxLikelihood() && this.pr_LT.getMaxLikelihood() >= this.pr_GL.getMaxLikelihood()){
			output = this.pr_LT.getMaxLikelihood();
		}
		else{
			output = this.pr_ID.getMaxLikelihood();
		}
		return output;
	}
*/
	/* **********************************???????????????????????????????????********************************* */
	//与えられたベクトルの尤度を求める
	public double getLikelihood(int[] position,MyFrame frame){
		double output = 0;
		/*
		 * 1. 最も尤もらしいＰＲクラスを選択
		 * 2. 最も尤もらしい参照点を選択
		 * 3. ベクトルを変換，正規化
		 * 4. GaussクラスのProbabilityを呼び出す．
		 */

		//初期状態を確保
		int[] tempinit = new int[2];
		int[] tempgl = new int[2];
		for(int i=0;i<MyFrame.NUMBEROFPANEL;i++){
			for(int j=0;j<MyFrame.NUMBEROFPANEL;j++){
				if(frame.panels[i][j].getStatus() == 1){
					tempinit[0] = i;
					tempinit[1] = j;
				}
				else if(frame.panels[i][j].getStatus() == this.pr_GL.mostlikelyland){
					tempgl[0] = i;
					tempgl[1] = j;
				}
			}
		}

		MyPR temppr = null;
		int check = 0;
		if(this.pr_GL.getMaxLikelihood() >= this.pr_LT.getMaxLikelihood() && this.pr_GL.getMaxLikelihood() >= this.pr_ID.getMaxLikelihood()){
			temppr = this.pr_GL;
			check = 2;
		}
		else if(this.pr_LT.getMaxLikelihood() >= this.pr_ID.getMaxLikelihood() && this.pr_LT.getMaxLikelihood() >= this.pr_GL.getMaxLikelihood()){
			temppr = this.pr_LT;
			check = 1;
		}
		else{
			temppr = this.pr_ID;
			check = 0;
		}
		//reproductionと叫べば，各オブジェクトの位置は最新のものに更新され，同時に最尤参照点も保持される
		temppr.reproduction(frame);
		//座標変換するための準備
		double[][] inputs = new double[3][2];
		inputs[0][0] = position[0];
		inputs[0][1] = position[1];
		inputs[1][0] = temppr.mostlikelyreference.reference[0];
		inputs[1][1] = temppr.mostlikelyreference.reference[1];
		if(check == 0){
			//恒等座標の場合，3つ目は適当でいい
			inputs[2][0] = -1;
			inputs[2][1] = -1;
		}
		else if(check == 1){
			//LT座標の場合，3つ目はトラジェクタ初期位置
			inputs[2][0] = tempinit[0];
			inputs[2][1] = tempinit[1];
		}
		else{
			//GL座標の場合，3つ目は参照するランドマーク
			inputs[2][0] = tempgl[0];
			inputs[2][1] = tempgl[1];
		}
		double[] convertedvec = temppr.coordinate.convert(inputs);

		//変換後のベクトルがガウスモデルから生起される確率を求める

		output = temppr.mostlikelyreference.gauss.getProbability(convertedvec);

		return output;
	}
	/* **********************************???????????????????????????????????********************************* */

}
