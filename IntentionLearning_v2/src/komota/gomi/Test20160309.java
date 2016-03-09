package komota.gomi;

public class Test20160309 {



	public static void main(String[] args){
		Test20160309 test = new Test20160309();

		int dim = 3;

		test.matrix1 = test.new MyMetrics(dim);
		test.matrix2 = test.new MyMetrics(dim);
		test.matrix3 = test.new MyMetrics(dim);

		for(int i=0;i<dim;i++){
			for(int j=0;j<dim;j++){
				if(i==j){
					test.matrix1.setData(i, j, 1);
				}
				else{
					test.matrix1.setData(i, j, 0);
				}
				test.matrix2.setData(i, j, j+dim*i);
				test.matrix3.setData(i, j, (int)(Math.random()*10));
			}
		}
		//手打ち
		test.matrix2.setData(0, 0, 1);
		test.matrix2.setData(0, 1, 1);
		test.matrix2.setData(0, 2, 2);
		test.matrix2.setData(1, 0, 3);
		test.matrix2.setData(1, 1, 0);
		test.matrix2.setData(1, 2, 8);
		test.matrix2.setData(2, 0, 2);
		test.matrix2.setData(2, 1, 3);
		test.matrix2.setData(2, 2, 3);

		System.out.println("元の行列");
		test.matrix1.show();
		test.matrix2.show();
		test.matrix3.show();
		System.out.println("足し算");
		test.matrix3 = test.matrix1.add(test.matrix2);
		test.matrix3.show();
		System.out.println("引き算");
		test.matrix3 = test.matrix1.sub(test.matrix2);
		test.matrix3.show();
		System.out.println("掛け算");
		test.matrix3 = test.matrix1.mult(test.matrix2);
		test.matrix3.show();
		System.out.println("転置");
		test.matrix3 = test.matrix2.trans();
		test.matrix3.show();
		System.out.println("行列式");
		System.out.println(test.matrix2.getDetV());
		System.out.println("逆行列");
		test.matrix3 = test.matrix2.inv();
		test.matrix3.show();
		System.out.println("逆行列かけると単位行列になる");
		test.matrix3 = test.matrix2.inv();
		test.matrix2.mult(test.matrix3).show();
		System.out.println("逆行列の逆行列は元の行列");
		test.matrix3 = test.matrix3.inv();
		test.matrix3.show();


	}


	//後で消す
	MyMetrics matrix1;
	MyMetrics matrix2;
	MyMetrics matrix3;



	//真面目に行列を実装しよう
	//正方行列限定
	class MyMetrics{
		//次元
		int dimension;
		//行列値
		private double[][] data;

		//コンストラクタ
		public MyMetrics(int dimension){
			this.dimension = dimension;
			this.data = new double[dimension][dimension];
		}
		//コンストラクタで数値を代入することもできる
		public MyMetrics(int dimension,double[][] inputs){
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
		public MyMetrics add(MyMetrics input){
			MyMetrics output = new MyMetrics(this.dimension);
			for(int i=0;i<this.dimension;i++){
				for(int j=0;j<this.dimension;j++){
					output.setData(i, j, this.getData(i,j)+input.getData(i, j));
				}
			}
			return output;
		}
		//減算
		public MyMetrics sub(MyMetrics input){
			MyMetrics output = new MyMetrics(this.dimension);
			for(int i=0;i<this.dimension;i++){
				for(int j=0;j<this.dimension;j++){
					output.setData(i, j, this.getData(i,j)-input.getData(i, j));
				}
			}
			return output;
		}
		//乗算
		public MyMetrics mult(MyMetrics input){
			MyMetrics output = new MyMetrics(this.dimension);
			for(int i=0;i<this.dimension;i++){
				for(int j=0;j<this.dimension;j++){
					for(int m=0;m<this.dimension;m++){
						output.setData(i,j,output.getData(i,j) + this.data[i][m] * input.getData()[m][j]);
					}
				}
			}
			return output;
		}
		//転置
		public MyMetrics trans(){
			MyMetrics output = new MyMetrics(this.dimension);
			for(int i=0;i<this.dimension;i++){
				for(int j=0;j<this.dimension;j++){
					output.setData(i, j, this.getData(j,i));
				}
			}
			return output;
		}
		//行列式
		public double getDetV(){
			double[][] tempv = new double[this.dimension][this.dimension];
			//分散行列を変更しないようにコピー
			for(int i=0;i<this.dimension;i++){
				for(int j=0;j<this.dimension;j++){
					tempv[i][j] = this.getData(i,j);
				}
			}
			double tempdetv = 1;
			//対角成分にゼロがなくなるように行を交換。なくならなければ行列式はゼロ。
			for(int i=0;i<this.dimension;i++){
				boolean zeroflag = false;
				if(tempv[i][i] == 0){
					zeroflag = true;
					for(int j=i;j<this.dimension;j++){
						if(tempv[j][j] != 0){
							double[] temp = tempv[i];
							tempv[i] = tempv[j];
							tempv[j] = temp;
							tempdetv *= -1;
							zeroflag = false;
						}
					}
					if(zeroflag == true){
						return 0;
					}
				}
			}
			//行列を三角行列に変換
			for(int i=0;i<this.dimension;i++){
				for(int j=0;j<this.dimension;j++){
					if(i<j){
						double buf = tempv[j][i] / tempv[i][i];
						for(int k=0;k<this.dimension;k++){
							tempv[j][k] -= tempv[i][k] * buf;
						}
					}
				}
			}
			//三角行列の行列式は対角成分の積
			for(int i=0;i<this.dimension;i++){
				tempdetv *= tempv[i][i];
			}
			return tempdetv;
		}
		//逆行列
		public MyMetrics inv(){
			MyMetrics output = new MyMetrics(this.dimension);

			double[][] temp = new double[this.dimension][2*this.dimension];
			for(int i=0;i<this.dimension;i++){
				for(int j=0;j<this.dimension;j++){
					temp[i][j] = this.getData(i, j);
				}
			}
			for(int t=0;t<this.dimension;t++){
				temp[t][t+this.dimension] = 1;
			}
			//はき出し法を行って逆行列を求める
			//対角成分にゼロがなくなるように行を交換。なくならなければ逆行列なし。
			for(int i=0;i<this.dimension;i++){
				boolean zeroflag = false;
				if(temp[i][i] == 0){
					zeroflag = true;
					for(int j=i;j<this.dimension;j++){
						if(temp[j][j] != 0){
							double[] templine = temp[i];
							temp[i] = temp[j];
							temp[j] = templine;
							zeroflag = false;
						}
					}
					if(zeroflag == true){
						return null;
					}
				}
			}
			//行基本変形
			for(int i=0;i<this.dimension;i++){
				//対角成分を1にする
				double edge = temp[i][i];
				for(int j=0;j<2*this.dimension;j++){
					temp[i][j] /= edge;
				}
				//他の行から引く
				for(int k=0;k<this.dimension;k++){
					if(k!=i){
						double rate = temp[k][i];
						for(int j=0;j<2*this.dimension;j++){
							temp[k][j] -= temp[i][j] * rate;
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
	}
}
