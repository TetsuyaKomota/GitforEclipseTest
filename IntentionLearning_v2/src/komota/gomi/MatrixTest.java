package komota.gomi;

public class MatrixTest {

	public static void main(String[] args){
		int M = 10;
		MatrixTest test = new MatrixTest(M);
//		test.tensorx.show();
//		test.tensorx.checkI();
		System.out.println("実験するよ～");
		for(int num = 1;num<10000000;num*=10){
			test = new MatrixTest(num);
			test.tensorx.setX(100);
			test.tensoralpha.setAlpha(10);
			double temp = 0;
			for(int m=0;m<num;m++){
				temp += test.mult(m, 0, 0);
			}
			temp /= num;
			System.out.println(temp);
		}
	}

	//マトリクスって書いたけどこれテンソルだわ．

	//次元数
	int dimension = 3;
	//方程式群数
	int M = 1000;

	//テンソル
	Tensor tensorx;
	Tensor tensoralpha;
	//コンストラクタ
	public MatrixTest(int M){
		this.M = M;
		this.tensorx = new Tensor();
		this.tensoralpha = new Tensor();
	}

	//掛け算を計算する
	double mult(int m,int j,int k){
		double output = 0;

		for(int i=0;i<dimension;i++){
			output += this.tensoralpha.get(m, j, i) * this.tensorx.get(m, i, k);
		}

		return output;
	}

	class Tensor{
		//テンソル量
		double[][][] tens;
		//次元数
		int dimension;
		//方程式群数
		int M;

		//コンストラクタ
		public Tensor(){
			this.dimension = MatrixTest.this.dimension;
			this.M = MatrixTest.this.M;
			this.tens = new double[M][dimension][dimension];
		}
		//ゲッター
		public double get(int M,int gyou,int retsu){
			return this.tens[M][gyou][retsu];
		}
		//全要素を入力した分散を持つガウス誤差にする
		//本来は要素の次元kごとに異なる分散を与えるべき
		public void setAlpha(double variance){
			for(int i=0;i<this.tens.length;i++){
				for(int j=0;j<this.tens[0].length;j++){
					for(int k=0;k<this.tens[0][0].length;k++){
						double rand = Math.random();
						double x = -100;
						double gauss = 0;
						while(true){
							gauss += Math.exp(-x*x / (2*variance)) / Math.sqrt(2*Math.PI*variance);
							if(rand < gauss){
								this.tens[i][j][k] = x;
								break;
							}
							else{
								x++;
							}
						}
					}
				}
			}
		}
		//全要素を入力した範囲の一様分布にする
		public void setX(double range){
			for(int m=0;m<this.tens.length;m++){
				for(int i=0;i<this.tens[0].length;i++){
					for(int j=0;j<this.tens[0][0].length;j++){
						this.tens[m][i][j] = range*Math.random();
					}
				}
			}
		}

		//出力して確認
		public void show(){
			for(int j=0;j<this.tens[0].length;j++){
				for(int k=0;k<this.tens[0][0].length;k++){
					System.out.print((int)this.tens[0][j][k]+" ");
				}
				System.out.println();
			}
		}

		//教示誤差のi平均がゼロに収束することの確認
		public void checkI(){
			for(int k=0;k<this.tens[0][0].length;k++){
				double sum  =0;
				for(int i=0;i<this.tens[0].length;i++){
					sum += this.tens[0][i][k];
				}
				sum /= this.tens[0].length;
				System.out.print(sum + " ");
			}
		}
	}

}
