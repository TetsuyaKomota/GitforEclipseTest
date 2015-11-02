package komota.pr.test;

import komota.pr.MyPL;

public class TestPL1 extends MyPL{

	//参照点を原点に、画面に平行な座標系で動作をとらえる試行。重心位置は参照点として数えないバージョン

	//定数

	//フィールド
	//参照点の個数
	int numref = 0;
	//参照点クラス
	ReferencePoint[] refs;

	//コンストラクタ
	public TestPL1(int numref){
		this.numref = numref;
		this.refs = new ReferencePoint[numref];

		//参照点一つ目は画面中央にする。
		//logdataの0行目（logdata[0]というStepDataインスタンス）から状態0と1以外のオブジェクトがくるまで回す
		//0と1以外がlogdata[0].getStepStatusField()[i][j]にあったらrefs[k].reference[0] = i,[i] = jとして、状態もセット


	}

	/* ************************************************************************************************************* */
	//参照点ごとに学習された位置ベクトルと尤度を持つ内部クラス
	class ReferencePoint{
		//フィールド
		//参照点の状態
		int status;
		//参照点の位置ベクトル[0]=行 [1]=列
		double[] reference;
		//参照点からの位置ベクトル
		double[] goalpoint;
		//尤度
		double likelihood;

		//コンストラクタ
		ReferencePoint(int status, double[] reference){
			this.status = status;
			this.reference = reference;

			this.goalpoint = new double[2];
			goalpoint[0] = 0;
			goalpoint[1] = 0;

			this.likelihood = 0;
		}
	}
	/* ************************************************************************************************************* */
}
