package komota.main;

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

	//タスク名。識別時にこれを出力する
	String taskname;

	//コンストラクタ
	//インスタンス生成時に学習する
	public MyTaskPrimitive(String filename,String taskname){
		this.pr_ID = new PR_100_ID(9,filename);
		this.pr_LT = new PR_100_LT(9,filename);
		this.pr_GL = new PR_100_GL(9,filename);

		pr_ID.learnfromLog();
		pr_LT.learnfromLog();
		pr_GL.learnfromLog();

		this.taskname = taskname;
	}
	//タスク名のゲッター
	public String getTaskName(){
		return this.taskname;
	}

	//最も尤もらしいPRのreproductionを実行する
	public void reproductionTask(MyFrame frame){
		System.out.println("[MyTask]reproductionTask:MaxLikelihood: ID:"+this.pr_ID.getMaxLikelihood()+" LT:"+pr_LT.getMaxLikelihood()+" GL:"+pr_GL.getMaxLikelihood());
		if(this.pr_GL.getMaxLikelihood() >= this.pr_LT.getMaxLikelihood() && this.pr_GL.getMaxLikelihood() >= this.pr_ID.getMaxLikelihood()){
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
}
