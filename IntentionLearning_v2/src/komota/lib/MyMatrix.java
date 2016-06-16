package komota.lib;

import java.text.DecimalFormat;


//真面目に行列を実装しよう
//正方行列限定
/**
 * 行列クラス
 * @author komota
 * @version 1.0
 */
public class MyMatrix{

	//フィールド

	/** 次元*/
	int dimension;
	/** 行列値*/
	private double[][] data;

	/**
	 * コンストラクタ
	 * @param dimension 行列の次元
	 */
	public MyMatrix(int dimension){
		this.dimension = dimension;
		this.data = new double[dimension][dimension];
	}
	/**
	 * コンストラクタ
	 * @param dimension 行列の次元
	 * @param inputs 行列の初期値．行列サイズより小さい配列を代入した場合，0を補完する．行列サイズより大きい配列を代入した場合，範囲外の数値を無視して代入する．
	 */
	public MyMatrix(int dimension,double[][] inputs){
		this(dimension);
		//入力行列が読み込み可能かどうか
		if(inputs == null || inputs[0] == null){
			return;
		}
		for(int i=0;i<inputs.length;i++){
			for(int j=0;j<inputs[0].length;j++){
				//既に行列値にはゼロが代入されているはずなので，そのままinputsのある分だけ代入させればいい
				if(i<this.dimension && j<this.dimension){
					this.data[i][j] = inputs[i][j];
				}
			}
		}
	}
	/**
	 * コンストラクタ
	 * @param dimension 行列の次元
	 * @param vector 行列の初期値ベクトル．行列の要素数と同じ要素数のベクトルでない場合は初期値ゼロの行列になる
	 */
	public MyMatrix(int dimension,double[] vector){
		this(dimension);
		//入力ベクトルが読み込み可能かどうか
		if(vector.length != this.dimension*this.dimension){
			return;
		}
		for(int i=0;i<vector.length;i++){
			this.data[i/this.dimension][i%this.dimension] = vector[i];
		}
	}
	/**
	 * 次元のゲッター
	 * @return この行列の次元
	 */
	public int getDimension(){
		return this.dimension;
	}
	/**
	 * 行列値のセッター.行列値すべてまたは一部分を同時に代入できる．
	 * @param startg 代入開始点の行番号
	 * @param startr 代入開始点の列番号
	 * @param inputs 代入値．代入開始点に対して，行列の範囲外にあたる数値は無視して代入する．
	 */
	public void setData(int startg,int startr,double[][] inputs){
		for(int i=startg;i<this.dimension;i++){
			for(int j=startr;j<this.dimension;j++){
				this.data[i][j] = inputs[i-startg][j-startr];
			}
		}
	}
	/**
	 * 行列値セッター.選択した要素のみの代入
	 * @param gyou 代入点の行番号
	 * @param retsu 代入点の列番号
	 * @param input 代入値
	 */
	public void setData(int gyou,int retsu,double input){
		if(gyou >= 0 && retsu >= 0 && gyou < this.dimension && retsu < this.dimension){
			this.data[gyou][retsu] = input;
		}
	}
	/**
	 * 行列値のゲッター
	 * @return 行列値配列
	 */
	public double[][] getData(){
		return this.data;
	}
	/**
	 * 行列値一か所のみのゲッター
	 * @param gyou 行番号
	 * @param retsu 列番号
	 * @return 行列値.引数が不適切な場合はコンソールにエラー出力して0を返す
	 */
	public double getData(int gyou,int retsu){

		if(gyou >= 0 && retsu >= 0 && gyou < this.dimension && retsu < this.dimension){
			return this.data[gyou][retsu];
		}
		else{
			System.out.println("[MyMatrix]getData:Error:invalid input:"+gyou+","+retsu);
			return 0;
		}
	}

	/**
	 * 二つの行列が等しいかを判定する
	 * @param input 等しいか判定する行列
	 * @return 等しい場合はtrue，そうでないならfalse
	 */
	public boolean isEqual(MyMatrix input){
		if(this.dimension != input.dimension){
			return false;
		}
		for(int i=0;i<this.dimension;i++){
			for(int j=0;j<this.dimension;j++){
				if(this.getData(i, j) != input.getData(i, j)){
					return false;
				}
			}
		}
		return true;
	}

	//行列の演算
	/**
	 * 加算
	 * @param input 加算行列
	 * @return 引数に与えた行列と足し合わせた行列
	 */
	public MyMatrix add(MyMatrix input){
		MyMatrix output = new MyMatrix(this.dimension);
		for(int i=0;i<this.dimension;i++){
			for(int j=0;j<this.dimension;j++){
				output.setData(i, j, this.getData(i,j)+input.getData(i, j));
			}
		}
		return output;
	}
	/**
	 * 減算
	 * @param input 減算行列
	 * @return 引数に与えた行列を引いた行列
	 */
	public MyMatrix sub(MyMatrix input){
		MyMatrix output = new MyMatrix(this.dimension);
		for(int i=0;i<this.dimension;i++){
			for(int j=0;j<this.dimension;j++){
				output.setData(i, j, this.getData(i,j)-input.getData(i, j));
			}
		}
		return output;
	}
	/**
	 * 乗算
	 * @param input 乗算行列
	 * @return 引数に与えた行列を右から掛け合わせた行列.inputがnullの場合はnullを返す．
	 */
	public MyMatrix mult(MyMatrix input){
		MyMatrix output = new MyMatrix(this.dimension);
		if(input == null){
			return null;
		}
		for(int i=0;i<this.dimension;i++){
			for(int j=0;j<this.dimension;j++){
				for(int m=0;m<this.dimension;m++){
					output.setData(i,j,output.getData(i,j) + this.data[i][m] * input.getData()[m][j]);
				}
			}
		}
		return output;
	}
	/**
	 * ベクトルとの乗算
	 * @param input 乗算ベクトル
	 * @return 乗算ベクトルを右からかけ合わせたベクトル
	 */
	public double[] multwithVec(double[] input){

		if(input.length != this.getDimension()){
			System.out.println("行列とベクトルの次元が違うよ");
			return null;
		}

		double[] output = new double[input.length];

		for(int i=0;i<output.length;i++){
			for(int j=0;j<output.length;j++){
				output[i] += this.getData(i, j) * input[j];
			}
		}

		return output;
	}

	/**
	 * 畳込み計算して結果を行列で返す
	 * @param patch 畳込みパッチ
	 * @param stride ストライド
	 * @param isZeropadding ゼロパディングをするかどうか．ゼロパディングをする場合，出力サイズはストライド1で入力サイズと同じになる
	 * @return 引数に与えたパッチで畳込計算した結果の行列
	 */
	public MyMatrix convolution(MyMatrix patch, double stride, boolean isZeropadding){
		MyMatrix output;
		if(isZeropadding == true){
			output = new MyMatrix((int)(this.getDimension()/stride));
		}
		else{
			output = new MyMatrix((int)((this.getDimension() - (patch.getDimension()-1))/stride));
		}

		for(int i=0;i<output.getDimension();i++){
			for(int j=0;j<output.getDimension();j++){
				double temp = 0;

				//パディングあり
				if(isZeropadding == true){
					for(int ki=0;ki<patch.getDimension();ki++){
						for(int kj=0;kj<patch.getDimension();kj++){
							//このif文がゼロパディング
							if(
									((int)(i*stride)-patch.getDimension()/2)+ki >= 0
									&& ((int)(j*stride)-patch.getDimension()/2)+kj >= 0
									&& ((int)(i*stride)-patch.getDimension()/2)+ki < this.getDimension()
									&& ((int)(j*stride)-patch.getDimension()/2)+kj < this.getDimension()
								){
								temp += this.getData(((int)(i*stride)-patch.getDimension()/2)+ki,((int)(j*stride)-patch.getDimension()/2)+kj) * patch.getData(ki, kj);
							}
						}
					}
				}
				//パディングなし
				else{
					for(int ki=0;ki<patch.getDimension();ki++){
						for(int kj=0;kj<patch.getDimension();kj++){
							if((int)(i*stride)+ki < this.getDimension() && (int)(j*stride)+kj< this.getDimension()){
								temp += this.getData((int)(i*stride)+ki, (int)(j*stride)+kj)*patch.getData(ki, kj);
							}
						}
					}
				}

				output.setData(i, j, temp);
			}
		}
		return output;
	}

	/**
	 * 定数倍
	 * @param input 乗算定数
	 * @return 各要素を引数に与えた定数倍した行列
	 */
	public MyMatrix mult(double input){
		MyMatrix output = new MyMatrix(this.dimension);
		for(int i=0;i<this.dimension;i++){
			for(int j=0;j<this.dimension;j++){
				output.setData(i, j, this.getData(i, j)*input);
			}
		}
		return output;
	}
	/**
	 * 転置
	 * @return この行列を転置した行列
	 */
	public MyMatrix trans(){
		MyMatrix output = new MyMatrix(this.dimension);
		for(int i=0;i<this.dimension;i++){
			for(int j=0;j<this.dimension;j++){
				output.setData(i, j, this.getData(j,i));
			}
		}
		return output;
	}
	/**
	 * 行列式を求める.Statics.MIN_DETERMINANT未満である場合は 0 を返す.
	 * @return この行列の行列式
	 */
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
		if(output < Statics.MIN_DETERMINANT){
			return 0;
		}
		return output;
	}


	/**
	 * 逆行列
	 * @return 個の行列の逆行列.正則でない場合は null を返す.
	 */
	@SuppressWarnings("unused")
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
					if(Statics.__DEBUG_MODE__ == true){
						System.out.println("これが戦犯やで！");
						System.out.println("DET="+this.getDetV());
						this.show();
					}
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

	//ノルム計算
	/**
	 * 最大ノルムを出力する.すなわち，この行列の全要素のうち，絶対値の最も大きい要素の絶対値を出力する
	 * @return 個の行列の最大ノルム
	 */
	public double getMaxNorm(){
		double output = 0;
		for(int i=0;i<this.dimension;i++){
			for(int j=0;j<this.dimension;j++){
				if(output < Math.abs(this.getData(i, j))){
					output = Math.abs(this.getData(i, j));
				}
			}
		}
		return output;
	}

	/**
	 * コンソールに行列値を標準出力する
	 */
	public void show(){
		for(int i=0;i<this.dimension;i++){
			for(int j=0;j<this.dimension;j++){
				System.out.print(this.getData(i, j) + " ");
			}
			System.out.println();
		}
		System.out.println();
	}

	/**
	 * コンソールに，行列値を小数点第2位までの概数で標準出力する
	 */
	public void show_approximately(){
		DecimalFormat df = new DecimalFormat("0.00");
		for(int i=0;i<this.dimension;i++){
			for(int j=0;j<this.dimension;j++){
				System.out.print(df.format(this.getData(i, j)) + " ");
			}
			System.out.println();
		}
		System.out.println();
	}


	/**
	 * 行列値を，Statics.MIN_APPROX未満を四捨五入して概数化する．このメソッドのみでは細かい数値が残ってしまう場合があるため，使用には注意が必要.
	 * @return 個の行列を概数化した行列
	 */
	public MyMatrix approximate(){
		MyMatrix output = new MyMatrix(this.dimension);

		for(int i=0;i<this.dimension;i++){
			for(int j=0;j<this.dimension;j++){
				double temp1 = this.data[i][j] / Statics.MIN_APPROX;

				int temp2 = (int) Math.round(temp1);

				output.setData(i, j, (double)temp2*Statics.MIN_APPROX);
			}
		}

		return output;
	}


	/**
	 * NaNになっている要素が存在するか判定する
	 * @return NaNになっている要素がある場合はtrue，そうでないならばfalse
	 */
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

	//行ごとに区切って一列に並べたベクトルに変換する
	public double[] vectorize(){
		double[] output = new double[this.dimension*this.dimension];
		for(int i=0;i<this.dimension;i++){
			for(int j=0;j<this.dimension;j++){
				output[i*this.dimension + j] = this.data[i][j];
			}
		}
		return output;
	}
}