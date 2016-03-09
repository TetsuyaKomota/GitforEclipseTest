package komota.lib;


//真面目に行列を実装しよう
//正方行列限定
public class MyMatrix{

	public static void main(String[] args){
		int dim = 3;
		int count = 0;
		int check = 1;
		double mean = 0;
		double variance = 10;
		MyMatrix mat1 = new MyMatrix(dim);
		MyMatrix mat2 = new MyMatrix(dim);
		MyMatrix means = new MyMatrix(dim);
		//行列式が1になる行列とは珍しいものなのかを試行
		//この実験から，行列式は大抵の場合，e14~e17程度のスケールとなることが判明
/*
		while(true){
			count++;
			for(int i=0;i<dim;i++){
				for(int j=0;j<dim;j++){
					mat1.setData(i, j, (int)(Math.random()*200));
				}
			}
			if(Math.abs(mat1.getDetV()) <= 10000 && Math.abs(mat1.getDetV()) >= 0.000001){
				break;
			}
			else{
				//行列式の平均を求めてみよう
				mean *= count-1;
				mean += mat1.getDetV();
				mean /= count;
			}
			if(count == 1000*check){
				check++;
				System.out.println("done:"+1000*check + "  DET:"+ mat1.getDetV() + "  mean(DET):"+mean);
			}
		}
		System.out.println(count + ": DET="+mat1.getDetV());
		mat1.show();
		mat1.inv().show();
*/
		//逆行列の成分に，元の行列の成分の最大値を超えるような成分が出現することは珍しいものなのかを試行
		//この実験から，元行列の最大の成分の平方根サイズでもあまり現れないことが分かった
/*
		double max;

		while(true){
			count++;
			while(true){
				max = 0;
				for(int i=0;i<dim;i++){
					for(int j=0;j<dim;j++){
						mat1.setData(i, j, (int)(Math.random()*200));
						if(mat1.getData(i, j)>max){
							max = mat1.getData(i, j);
						}
					}
				}
				if(mat1.getDetV() != 0){
					break;
				}
			}
			mat1 = mat1.inv();
			boolean flag = false;
			for(int i=0;i<dim;i++){
				for(int j=0;j<dim;j++){
					if(Math.abs(mat1.getData(i, j)) > Math.sqrt(max)){
						flag = true;
					}
				}
			}
			if(flag == true){
				break;
			}
			else if(count == 1000*check){
				check++;
				System.out.println("done:"+1000*check + "  DET:"+ mat1.getDetV());
			}
		}
		System.out.println(count + ": MAX="+max + ": DET="+mat1.getDetV());
//		mat1.inv().show();
//		mat1.show();
*/
		//200*200空間の教示データをランダム生成
		//逆行列化
		//ガウス誤差との積を求める
		//平均とりまくる．たまに出力する

		for(int time=0;time<100;time++){
			while(true){
				count++;
				while(true){
					for(int i=0;i<dim;i++){
						for(int j=0;j<dim;j++){
							mat1.setData(i, j, (int)(Math.random()*200));
						}
					}
					if(mat1.getDetV() != 0){
						break;
					}
				}

				mat1 = mat1.inv();

				for(int i=0;i<dim;i++){
					for(int j=0;j<dim;j++){
						double rand = Math.random();
						double x = -100;
						double gauss = 0;
						while(true){
							gauss += Math.exp(-x*x / (2*variance)) / Math.sqrt(2*Math.PI*variance);
							if(rand < gauss){
								mat2.setData(i, j, (int)x);
								break;
							}
							else{
								x++;
							}
						}
					}
				}
				//平均を更新
				means = means.mult(count-1).add(mat1.mult(mat2)).mult((double)1/count);

				//平均を更新
				//一定回数で出力
				if(count == Math.pow(10, time+1)/*100000*time+1*/){
					break;
				}
			}
			System.out.println("TIME:"+time);
			means.show();
		}

	}


	//定数
	//行列式の切り捨て境界.これより小さな行列式はゼロとして扱う
	final double INFERIOR = 0.00000001;

	//次元
	int dimension;
	//行列値
	private double[][] data;

	//コンストラクタ
	public MyMatrix(int dimension){
		this.dimension = dimension;
		this.data = new double[dimension][dimension];
	}
	//コンストラクタで数値を代入することもできる
	public MyMatrix(int dimension,double[][] inputs){
		this(dimension);
		//入力行列が読み込み可能かどうか
		if(inputs == null || inputs[0] == null){
			return;
		}
		for(int i=0;i<inputs.length;i++){
			for(int j=0;j<inputs[0].length;j++){
				//既に行列値にはゼロが代入されているはずなので，そのままinputsのある分だけ代入させればいい
				this.data[i][j] = inputs[i][j];
			}
		}
	}
	//行列値のセッター
	//(開始点，入力行列).基本的には0,0,dimension*dimensionサイズの行列 という引数
	public void setData(int startg,int startr,double[][] inputs){
		for(int i=startg;i<this.dimension;i++){
			for(int j=startr;j<this.dimension;j++){
				this.data[i][j] = inputs[i-startg][j-startr];
			}
		}
	}
	//行列値一か所のみのセッター
	public void setData(int gyou,int retsu,double input){
		this.data[gyou][retsu] = input;
	}
	//行列値のゲッター
	public double[][] getData(){
		return this.data;
	}
	//行列値一か所のみのゲッター
	public double getData(int gyou,int retsu){
		return this.data[gyou][retsu];
	}

	//行列の演算
	//加算
	public MyMatrix add(MyMatrix input){
		MyMatrix output = new MyMatrix(this.dimension);
		for(int i=0;i<this.dimension;i++){
			for(int j=0;j<this.dimension;j++){
				output.setData(i, j, this.getData(i,j)+input.getData(i, j));
			}
		}
		return output;
	}
	//減算
	public MyMatrix sub(MyMatrix input){
		MyMatrix output = new MyMatrix(this.dimension);
		for(int i=0;i<this.dimension;i++){
			for(int j=0;j<this.dimension;j++){
				output.setData(i, j, this.getData(i,j)-input.getData(i, j));
			}
		}
		return output;
	}
	//乗算
	public MyMatrix mult(MyMatrix input){
		MyMatrix output = new MyMatrix(this.dimension);
		for(int i=0;i<this.dimension;i++){
			for(int j=0;j<this.dimension;j++){
				for(int m=0;m<this.dimension;m++){
					output.setData(i,j,output.getData(i,j) + this.data[i][m] * input.getData()[m][j]);
				}
			}
		}
		return output;
	}
	//定数倍
	public MyMatrix mult(double input){
		MyMatrix output = new MyMatrix(this.dimension);
		for(int i=0;i<this.dimension;i++){
			for(int j=0;j<this.dimension;j++){
				output.setData(i, j, this.getData(i, j)*input);
			}
		}
		return output;
	}
	//転置
	public MyMatrix trans(){
		MyMatrix output = new MyMatrix(this.dimension);
		for(int i=0;i<this.dimension;i++){
			for(int j=0;j<this.dimension;j++){
				output.setData(i, j, this.getData(j,i));
			}
		}
		return output;
	}
	//修正版行列式

	public double getDetV(){
		double output = 1;
		/*
		 * 1. i=0
		 * 2. i行i列が非ゼロになるように行れ替え．それができないなら戻り値0
		 * 3. i列のi+1以上の行の成分がゼロになるように基本変形
		 * 4. i++して2.へ
		 *
		 */
		//行列値をコピー
		double[][] tempv = new double[this.dimension][this.dimension];
		for(int i=0;i<this.dimension;i++){
			for(int j=0;j<this.dimension;j++){
				tempv[i][j] = this.getData(i, j);
			}
		}
		for(int i=0;i<this.dimension;i++){
			//2.
			boolean zeroflag = false;
			if(tempv[i][i] == 0){
				zeroflag = true;
				for(int j=i;j<this.dimension;j++){
					if(tempv[j][i] != 0){
						double[] temp = tempv[i];
						tempv[i] = tempv[j];
						tempv[j] = temp;
						output *= -1;
						zeroflag = false;
					}
				}
				if(zeroflag == true){
					return 0;
				}
			}
			//3.
			for(int j=i+1;j<this.dimension;j++){
				double buf = tempv[j][i] / tempv[i][i];
				for(int k=0;k<this.dimension;k++){
					tempv[j][k] -= tempv[i][k] * buf;
				}
			}
			//4.
		}
		//三角行列の行列式は対角成分の積
		for(int i=0;i<this.dimension;i++){
			output *= tempv[i][i];
		}
		//求まった行列式が下界より小さい場合はゼロとする
		if(output < INFERIOR){
			return 0;
		}
		return output;
	}


	//改良版逆行列
	public MyMatrix inv(){
		MyMatrix output = new MyMatrix(this.dimension);

		//掃き出し用の行列を準備する
		double[][] temp = new double[this.dimension][2*this.dimension];

		for(int i=0;i<this.dimension;i++){
			for(int j=0;j<this.dimension;j++){
				temp[i][j] = this.getData(i, j);
			}
		}
		for(int t=0;t<this.dimension;t++){
			temp[t][t+this.dimension] = 1;
		}

		//対角部分が非ゼロの行を持ってくる．持って来れないなら逆行列なし
		for(int i=0;i<this.dimension;i++){
			boolean zeroflag = false;
			if(temp[i][i] == 0){
				zeroflag = true;
				for(int j=i;j<this.dimension;j++){
					if(temp[j][i] != 0){
						double[] templine = temp[i];
						temp[i] = temp[j];
						temp[j] = templine;
						zeroflag = false;
					}
				}
				if(zeroflag == true){
					System.out.println("これが戦犯やで！");
					System.out.println("DET="+this.getDetV());
					this.show();
					return null;
				}
			}
			//対角部分が1になるように行を割り算
			double buf = temp[i][i];
			for(int k=0;k<2*this.dimension;k++){
				temp[i][k] /= buf;
			}

			//i列の他の成分がゼロになるように引き算
			for(int j=0;j<this.dimension;j++){
				if(j!=i){
					buf = temp[j][i];
					for(int k=0;k<2*this.dimension;k++){
						temp[j][k] -= temp[i][k] * buf;
					}
				}
			}
		}
		for(int i=0;i<this.dimension;i++){
			for(int j=0;j<this.dimension;j++){
				output.setData(i, j, temp[i][j+this.dimension]);
			}
		}
		return output;
	}

	//標準出力
	public void show(){
		for(int i=0;i<this.dimension;i++){
			for(int j=0;j<this.dimension;j++){
				System.out.print(this.getData(i, j) + " ");
			}
			System.out.println();
		}
		System.out.println();
	}

	//NaNになっている要素が存在するか判定
	//主にデバッグで使用する
	public boolean isNaN(){
		for(int i=0;i<this.dimension;i++){
			for(int j=0;j<this.dimension;j++){
				if(Double.isNaN(this.getData(i,j)) == true){
					return true;
				}
			}
		}
		return false;
	}
}