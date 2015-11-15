package komota.pr.test;

import komota.main.MyFrame;
import komota.main.MyPR;

public class PR_1_1 extends MyPR{

	//参照点を原点に、画面に平行な座標系で動作をとらえる試行。重心位置を参照点に含めるバージョン
	/*
	 * 実装上の問題で、「重心位置の状態番号はどのように割り当てようか」という話がある
	 * 学習フェーズに状態番号を用いている関係上、うまく決めないといけない
	 * 学習フェーズにはオブジェクトを参照点とするもののベクトルだけを先に更新し、それをもとに重心位置の座標を更新すればいい
	 * つまりコンストラクタ呼び出し時に用意したオブジェクトの順番で考えるというもの
	 * あとは重心位置の順番をどのようにするかだが、
	 */




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
	public PR_1_1(int numref){
		int numberobreference = (int)Math.pow(2, numref) - 1;
		this.numref = numberobreference;
		this.refs = new ReferencePoint[numberobreference];

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
			if(logdata[t] == null){
				break;
			}
			if(logdata[t].getType() == START){
				for(int i=0;i<height;i++){
					for(int j=0;j<width;j++){
						if(this.logdata[t].getStepStatus(i, j) > 1){
							int tempstatus = this.logdata[t].getStepStatus(i, j);
							for(int k=0;k<this.refs.length;k++){
								if(this.refs[k].status == tempstatus){
									this.refs[k].reference[0] = i;
									this.refs[k].reference[1] = j;
								}
							}
						}
					}
				}
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

	//学習結果に基づいてタスクを推定する
	public void reproduction(MyFrame frame){
		double templikelihood = -10000000;
		int tempref = -1;
		//尤度最大の参照点を検索する
		for(int i=0;i<this.refs.length;i++){
			if(this.refs[i].likelihood > templikelihood){
				templikelihood = this.refs[i].likelihood;
				tempref = i;
			}
		}
		System.out.println("[TestPR1]reproduction:tempref:"+tempref);
		//選択された参照点を現在の座標に更新する
		//状態が0（画面中心が参照点）の場合は例外
		if(this.refs[tempref].status != 0){
			for(int i=0;i<height;i++){
				for(int j=0;j<width;j++){
					if(frame.getPanels()[i][j].getStatus() == this.refs[tempref].status){
						this.refs[tempref].reference[0] = i;
						this.refs[tempref].reference[1] = j;
					}
				}
			}
		}
		System.out.println("[TestPR1]reproduction:ref.reference:"+refs[tempref].reference[0]+" , "+refs[tempref].reference[1]);
		System.out.println("[TestPR1]reproduction:ref.goalpoint:"+refs[tempref].goalpoint[0]+" , "+refs[tempref].goalpoint[1]);
		//参照点の絶対ベクトル＋参照点からの相対ベクトル＝トラジェクタの推定移動先
		double[] tempoutput = new double[2];
		tempoutput[0] = this.refs[tempref].reference[0] + this.refs[tempref].goalpoint[0];
		tempoutput[1] = this.refs[tempref].reference[1] + this.refs[tempref].goalpoint[1];
		//doubleになっているので、パネルに変換する(まあただの四捨五入)
		System.out.println("[TestPR1]reproduction:tempoutput:"+tempoutput[0]+" , "+tempoutput[1]);
		int[] output = new int[2];
		output[0] = (int)(tempoutput[0] + 0.5);
		output[1] = (int)(tempoutput[1] + 0.5);
		frame.setSecondSelected(output);
	}

	//表示
	public void showReference(){
		for(int i=0;i<this.refs.length;i++){
			this.refs[i].show();
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
			System.out.println("[TestPR1.ReferencePoint]learn:status:"+this.status+"tempgoal:"+tempgoal[0]+" , "+tempgoal[1]);
			//学習回数を学習率としてgoalpointベクトルを更新する
			this.goalpoint[0] = this.goalpoint[0] * ((double)(this.numlearning)/(this.numlearning + 1)) + tempgoal[0] * ((double)1/(this.numlearning + 1));
			this.goalpoint[1] = this.goalpoint[1] * ((double)(this.numlearning)/(this.numlearning + 1)) + tempgoal[1] * ((double)1/(this.numlearning + 1));
			System.out.println("[TestPR1.ReferencePoint]learn:status:"+this.status+" goalpoint:"+goalpoint[0]+" , "+goalpoint[1]);
			//学習回数をインクリメント
			this.numlearning++;
			//近さを求める
			double[] tempcloseness = new double[2];
			tempcloseness[0] = tempgoal[0] - this.goalpoint[0];
			tempcloseness[1] = tempgoal[1] - this.goalpoint[1];
			double closeness = Math.sqrt(tempcloseness[0]*tempcloseness[0]+tempcloseness[1]*tempcloseness[1]);
			//likelihood += （近さ値-近さ閾値）
			System.out.println("[TestPR1.ReferencePoint]learn:closeness:"+closeness);
			likelihood += (E - closeness);
		}

		//表示
		void show(){
			System.out.println("status:"+status+"  reference:"+this.reference[0]+" , "+this.reference[1] + "  goalpoint;"+this.goalpoint[0]+" , "+this.goalpoint[1] + "  likelihood:"+this.likelihood);
		}
	}
	/* ************************************************************************************************************* */
}
