package komota.pr.main;

import komota.gauss.Gauss;
import komota.main.MyPR;

/* ********************************************************************************* */
/*
 * 100シリーズ以降で使用する参照点クラス
 * ガウスモデルを使用するが、7_1系で失敗だった目標位置ベクトルを生起確率の重みづけ平均にした部分を丸々カット
 */
/* ********************************************************************************* */



class ReferencePoint_100{
	//定数
	//この参照点を保持するPRクラス。coordinateの確認に使う
	MyPR pr = null;
	//ベクトルの近さ閾値。learnで使う
	static final double E = 10;
	//フィールド
	//参照点の状態。0は画面中央。10は重心位置。11はG_ランドマーク
	int status;
	//参照点の位置ベクトル[0]=行 [1]=列
	double[] reference;
	//参照点からの位置ベクトル
	double[] goalpoint;
	//分散
	double variance = 1;
	//尤度
	double likelihood;
	//学習回数。goalpoint更新の学習率に使用する
	int numlearning;
	//学習回数(分散)
	int numlearningvariance;
	//学習回数(likelihood)
	int numlearninglikelihood;

	//ガウス
	Gauss gauss;

	//コンストラクタ
	ReferencePoint_100(MyPR pr, int status, double referenceg,double referencer){
		this.pr = pr;
		this.status = status;
		this.reference = new double[2];
		this.reference[0] = referenceg;
		this.reference[1] = referencer;

		this.goalpoint = new double[2];
		goalpoint[0] = 0;
		goalpoint[1] = 0;

		this.likelihood = 1;
		this.numlearning = 0;
		this.numlearningvariance = 0;
		this.numlearninglikelihood = 0;

		this.gauss =new Gauss(2);
		this.gauss.setMean(goalpoint);
		this.gauss.setCovariance(variance);
	}

	//トラジェクタの位置ベクトル(絶対ベクトル)が引数として与えられたとき、goalpointの更新を行う
	void learn(double[] trajector,double[] startpoint){
		//相対ベクトルに変換する
		double[] tempgoal = new double[2];
		tempgoal[0] = trajector[0] - this.reference[0];
		tempgoal[1] = trajector[1] - this.reference[1];
		//座標系に合わせて座標変換する
		double[][] inputs = new double[3][2];
		inputs[0] = tempgoal;
		inputs[1][0] = this.reference[0];
		inputs[1][1] = this.reference[1];
		inputs[2] = startpoint;
		tempgoal = this.pr.coordinate.convert(inputs);
		System.out.println("[ReferencePoint]learn:status:"+this.status+" tempgoal:"+tempgoal[0]+" , "+tempgoal[1]);
		//学習回数を学習率としてgoalpointベクトルを更新する
		this.goalpoint[0] = this.goalpoint[0] * ((double)(this.numlearning)/(this.numlearning + 1)) + tempgoal[0] * ((double)1/(this.numlearning + 1));
		this.goalpoint[1] = this.goalpoint[1] * ((double)(this.numlearning)/(this.numlearning + 1)) + tempgoal[1] * ((double)1/(this.numlearning + 1));
		this.gauss.setMean(this.goalpoint);
		System.out.println("[ReferencePoint]learn:status:"+this.status+" goalpoint:"+goalpoint[0]+" , "+goalpoint[1]);
		//学習回数をインクリメント
		this.numlearning++;
		//近さを求める
		double[] tempcloseness = new double[2];
		tempcloseness[0] = tempgoal[0] - this.goalpoint[0];
		tempcloseness[1] = tempgoal[1] - this.goalpoint[1];
	}

	//分散を求める
	void learnVariance(double[] trajector,double[] startpoint){
		//相対ベクトルに変換する
		double[] tempgoal = new double[2];
		tempgoal[0] = trajector[0] - this.reference[0];
		tempgoal[1] = trajector[1] - this.reference[1];
		//座標系に合わせて座標変換する
		double[][] inputs = new double[3][2];
		inputs[0] = tempgoal;
		inputs[1][0] = this.reference[0];
		inputs[1][1] = this.reference[1];
		inputs[2] = startpoint;
		tempgoal = this.pr.coordinate.convert(inputs);
		//近さを求める
		double[] tempcloseness = new double[2];
		tempcloseness[0] = tempgoal[0] - this.goalpoint[0];
		tempcloseness[1] = tempgoal[1] - this.goalpoint[1];
		double closeness = tempcloseness[0]*tempcloseness[0]+tempcloseness[1]*tempcloseness[1];
		this.variance = this.numlearningvariance*this.variance/(this.numlearningvariance+1) + closeness/(this.numlearningvariance+1);
		this.gauss.setCovariance(variance);
		//学習回数をインクリメント
		this.numlearningvariance++;
	}

	void learnLikelihood(double[] trajector,double[] startpoint){
		//相対ベクトルに変換する
		double[] tempgoal = new double[2];
		tempgoal[0] = trajector[0] - this.reference[0];
		tempgoal[1] = trajector[1] - this.reference[1];
		//座標系に合わせて座標変換する
		double[][] inputs = new double[3][2];
		inputs[0] = tempgoal;
		inputs[1][0] = this.reference[0];
		inputs[1][1] = this.reference[1];
		inputs[2] = startpoint;
		tempgoal = this.pr.coordinate.convert(inputs);
		//近さを求める
		double[] tempcloseness = new double[2];
		tempcloseness[0] = tempgoal[0] - this.goalpoint[0];
		tempcloseness[1] = tempgoal[1] - this.goalpoint[1];
		double closeness = (double)1/Math.sqrt(tempcloseness[0]*tempcloseness[0]+tempcloseness[1]*tempcloseness[1]);
		closeness *= this.gauss.getProbability(tempgoal);
		System.out.println("[ReferencePoint]learnLikelihood:mean:("+this.gauss.getMean()[0]+","+this.gauss.getMean()[1]+") variance:"+this.gauss.getCovariance()[0][0]);
		System.out.println("[ReferencePoint]learnLikelihood:closeness:"+closeness+" probability:"+this.gauss.getProbability(tempgoal));
		//likelihood を更新
		likelihood = likelihood * ((double)(this.numlearninglikelihood)/(this.numlearninglikelihood + 1)) + closeness * ((double)1/(this.numlearninglikelihood + 1));
		//学習回数をインクリメント
		this.numlearninglikelihood++;
	}

	//表示
	void show(){
		System.out.println("status:"+status+"  reference:"+this.reference[0]+" , "+this.reference[1] + "  goalpoint;"+this.goalpoint[0]+" , "+this.goalpoint[1] + "  likelihood:"+this.likelihood);
	}
}