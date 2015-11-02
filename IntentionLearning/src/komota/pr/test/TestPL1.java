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
	//空間のサイズ
	double height;
	double width;

	//コンストラクタ
	public TestPL1(int numref){
		this.numref = numref;
		this.refs = new ReferencePoint[numref];

		//参照点一つ目は画面中央にする。
		this.height = this.logdata[0].getStepStatusField().length;
		this.width = this.logdata[0].getStepStatusField()[0].length;

		refs[0] = new ReferencePoint(0,height/2 ,width/2);

		//logdataの0行目（logdata[0]というStepDataインスタンス）から状態0と1以外のオブジェクトがくるまで回す
		int k=1;
		for(int i=0;i<height;i++){
			for(int j=0;j<width;j++){
				//0と1以外がlogdata[0].getStepStatusField()[i][j]にあったらrefs[k].reference[0] = i,[i] = jとして、状態もセット
				if(this.logdata[0].getStepStatus(i,j) > 1){
					this.refs[k] = new ReferencePoint(this.logdata[0].getStepStatus(i,j),i,j);
					k++;
				}
			}
		}
	}

	//ログデータに基づいて学習
	public void learnfromLog(){
		for(int t=0;t<this.logdata.length;t++){
			//"start "ログの場合、参照点の座標を更新する
			if(logdata[t].getType() == START){

			}
			//"goal  "ログの場合、トラジェクタの相対座標とgoalpointを比較し、goalpointとlikelihoodの更新をする
			else if(logdata[t].getType() == GOAL){
				double[] trajector = new double[2];
				for(int i=0;i<height;i++){
					for(int j=0;j<width;j++){
						if(this.logdata[t].getStepStatus(i, j) == 1){
							trajector[0] = i;
							trajector[1] = j;
						}
					}
				}
				//各参照点クラスのlearnメソッドで学習する
				for(int i=0;i<this.refs.length;i++){
					this.refs[i].learn(trajector);
				}
			}
			//"change"ログの場合、何もしない
			else if(logdata[t].getType() == CHANGE){

			}
			//"status"ログの場合、何もしない
			else if(logdata[t].getType() == STATUS){

			}
		}
	}

	/* ************************************************************************************************************* */
	//参照点ごとに学習された位置ベクトルと尤度を持つ内部クラス
	class ReferencePoint{
		//定数
		//ベクトルの近さ閾値。learnで使う
		static final double E = 10;
		//フィールド
		//参照点の状態。0は画面中央
		int status;
		//参照点の位置ベクトル[0]=行 [1]=列
		double[] reference;
		//参照点からの位置ベクトル
		double[] goalpoint;
		//尤度
		double likelihood;
		//学習回数。goalpoint更新の学習率に使用する
		int numlearning;

		//コンストラクタ
		ReferencePoint(int status, double referenceg,double referencer){
			this.status = status;
			this.reference = new double[2];
			this.reference[0] = referenceg;
			this.reference[1] = referencer;

			this.goalpoint = new double[2];
			goalpoint[0] = 0;
			goalpoint[1] = 0;

			this.likelihood = 1;
			this.numlearning = 0;
		}

		//トラジェクタの位置ベクトル(絶対ベクトル)が引数として与えられたとき、goalpointとlikelihoodの更新を行う
		void learn(double[] trajector){
			//相対ベクトルに変換する
			double[] tempgoal = new double[2];
			tempgoal[0] = trajector[0] - this.reference[0];
			tempgoal[1] = trajector[1] - this.reference[1];
			//近さを求める
			double[] tempcloseness = new double[2];
			tempcloseness[0] = (tempgoal[0] - this.goalpoint[0])/this.goalpoint[0];
			tempcloseness[1] = (tempgoal[1] - this.goalpoint[1])/this.goalpoint[1];
			double closeness = Math.sqrt(tempcloseness[0]*tempcloseness[0]+tempcloseness[1]*tempcloseness[1]);
			//likelihood += （近さ値-近さ閾値）
			likelihood += (closeness - E);
			//学習回数を学習率としてgoalpointベクトルを更新する
			this.goalpoint[0] = this.goalpoint[0] * ((this.numlearning)/(this.numlearning + 1)) + tempgoal[0] * (1/(this.numlearning + 1));
			this.goalpoint[1] = this.goalpoint[1] * ((this.numlearning)/(this.numlearning + 1)) + tempgoal[1] * (1/(this.numlearning + 1));
			//学習回数をインクリメント
			this.numlearning++;
		}
	}
	/* ************************************************************************************************************* */
}
