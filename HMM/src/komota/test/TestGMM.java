package komota.test;

public class TestGMM {

	//シリアルHMM用の混合ガウスモデル
	//次元数
	public static int dimension = 1;


	/* ************************************************************************************************************************* */
	//単体ガウスを表す内部クラス
	class Gauss{
		//フィールド
		double[] m;		//平均
		double[][] v;	//分散共分散行列
		double detv;

		//コンストラクタ
		Gauss(double[] mean,double[][] sigma){
			this.m = mean;
			this.v = sigma;
			setDetV();
		}
		Gauss(){
			//引数がない場合、平均はゼロ、分散は単位行列
			this.m = new double[dimension];
			this.v = new double[dimension][dimension];
			for(int i=0;i<dimension;i++){
				for(int j=0;j<dimension;j++){
					if(i == j){
						this.v[i][j] = 1;
					}
					else{
						this.v[i][j] = 0;
					}
				}
				this.m[i] = 0;
			}
			this.setDetV();
		}
		//vから行列式を求める。確率密度関数を実行するたびに計算するより、あらかじめ持たせておいた方が速そうと判断した
		void setDetV(){
			double[][] tempv = new double[TestGMM.dimension][TestGMM.dimension];
			//分散行列を変更しないようにコピー
			for(int i=0;i<TestGMM.dimension;i++){
				for(int j=0;j<TestGMM.dimension;j++){
					tempv[i][j] = v[i][j];
				}
			}
			double tempdetv = 1;
			//対角成分にゼロがなくなるように行を交換。なくならなければ行列式はゼロ。
			for(int i=0;i<TestGMM.dimension;i++){
				boolean zeroflag = false;
				if(tempv[i][i] == 0){
					zeroflag = true;
					for(int j=i;j<TestGMM.dimension;j++){
						if(tempv[j][j] != 0){
							double[] temp = tempv[i];
							tempv[i] = tempv[j];
							tempv[j] = temp;
							tempdetv *= -1;
							zeroflag = false;
						}
					}
					if(zeroflag == true){
						this.detv = 0;
						return;
					}
				}
			}
			//行列を三角行列に変換
			for(int i=0;i<TestGMM.dimension;i++){
				for(int j=0;j<TestGMM.dimension;j++){
					if(i<j){
						double buf = tempv[j][i] / tempv[i][i];
						for(int k=0;k<TestGMM.dimension;k++){
							tempv[j][k] -= tempv[i][k] * buf;
						}
					}
				}
			}
			//三角行列の行列式は対角成分の積
			for(int i=0;i<TestGMM.dimension;i++){
				tempdetv *= tempv[i][i];
			}
			this.detv = tempdetv;
		}

		//引数ベクトルの確率密度関数
		double getProbability(double[] x){
			double output = 1/Math.pow(2*Math.PI, TestGMM.dimension/2);
			double detsigma = detv;	//vの行列式
			double expo = 0;		//exp( ここ )

			//expoの計算

			double[] tempvector = new double[TestGMM.dimension];
			for(int i=0;i<TestGMM.dimension;i++){
				tempvector[i] = x[i] - this.m[i];
			}
			double[] tempvecsig = new double[TestGMM.dimension];
			for(int i=0;i<TestGMM.dimension;i++){
				double temp = 0;
				for(int j=0;j<TestGMM.dimension;j++){
					temp += tempvector[j] * this.v[j][i];
				}
				tempvecsig[i] = temp;
			}
			for(int i=0;i<TestGMM.dimension;i++){
				expo += tempvecsig[i] * tempvector[i];
			}

			//合体
			output /= Math.sqrt(detsigma);
			output *= Math.exp(expo);
			return output;
		}
		//確率密度関数に基づき、値を出力
		double getGaussRandom(){
			return 0;
		}
	}


	/* ************************************************************************************************************************* */

}
