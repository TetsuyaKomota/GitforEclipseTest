package komota.gauss;

import java.util.Random;

import komota.main.MyPR;

public class Gauss {

	//平均ベクトルと分散共分散行列を持つガウスクラス。


	public static void main(String[] args){
		Gauss gauss = new Gauss(2);
		MyPR pr = new MyPR();
		double[][] vecs = new double[100][2];

		double[] mean = new double[2];
		mean[0] = 0;
		mean[1] = 0;
		for(int i=0;i<vecs.length;i++){
			vecs[i][0] = Math.cos((Math.PI * 2/vecs.length)*i);
			vecs[i][1] = Math.sin((Math.PI * 2/vecs.length)*i);
			mean[0] += vecs[i][0];
			mean[1] += vecs[i][1];
		}
		mean[0] /= vecs.length;
		mean[1] /= vecs.length;

		double variance = 0;
		for(int i=0;i<vecs.length;i++){
			variance += (vecs[i][0] - mean[0])*(vecs[i][0] - mean[0])+(vecs[i][1] - mean[1])*(vecs[i][1] - mean[1]);
		}
		variance /= vecs.length;

		gauss.setMean(mean);
		gauss.setCovariance(variance);
		System.out.println("gauss:mean:("+mean[0]+","+mean[1]+")  variance:"+variance);

		for(int i=0;i<vecs.length;i++){
			System.out.println("vecs["+i+"]:("+vecs[i][0]+","+vecs[i][1]+"):probability:"+gauss.getProbability(vecs[i]));
		}
	}

	//フィールド
	//次元数
	int dimension;

	//平均ベクトル
	double[] mean;

	//分散共分散行列
	double[][] covariance;

	//コンストラクタ
	public Gauss(int dimension){
		//平均ベクトル、共分散行列の次元を設定する
		this.dimension = dimension;
		this.mean = new double[dimension];
		this.covariance = new double[dimension][dimension];
		//平均ベクトルをゼロベクトル、共分散行列を単位行列に設定する
		for(int i=0;i<dimension;i++){
			this.mean[i] = 0;
			for(int j=0;j<dimension;j++){
				if(i==j){
					this.covariance[i][j] = 1;
				}
				else{
					this.covariance[i][j] = 0;
				}
			}
		}
	}

	//各種セッター、ゲッター
	public void setMean(double[] m){
		if(this.mean.length != mean.length){
			return;
		}
		else{
			this.mean = m;
		}
	}
	public double[] getMean(){
		return this.mean;
	}
	public void setCovaiance(double[][] cov){
		if(this.covariance.length != covariance.length || this.covariance[0].length != covariance[0].length){
			return;
		}
		else{
			this.covariance = cov;
		}
	}
	//共分散行列のセッターにdoubleを与えると、その値倍の単位行列に設定する
	public void setCovariance(double variance){
		for(int i=0;i<this.dimension;i++){
			for(int j=0;j<this.dimension;j++){
				if(i==j){
					this.covariance[i][j] = variance;
				}
				else{
					this.covariance[i][j] = 0;
				}
			}
		}
	}
	public double[][] getCovariance(){
		return this.covariance;
	}

	//vから行列式を求める。
	public double getDetV(){
		double[][] tempv = new double[this.dimension][this.dimension];
		//分散行列を変更しないようにコピー
		for(int i=0;i<this.dimension;i++){
			for(int j=0;j<this.dimension;j++){
				tempv[i][j] = this.covariance[i][j];
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
	//乱数ベクトル取得
	public double[] getNextGaussian(){
		double[] output = new double[this.dimension];
		Random rand = new Random();

		for(int i=0;i<this.dimension;i++){
			//共分散を扱うのが面倒だったため、とりあえず分散値のみを使用している。気が向いたら修正する
			output[i] = this.mean[i] + Math.sqrt(this.covariance[i][i])*rand.nextGaussian();
		}

		return output;
	}

	//ベクトルの生起確率（この分布からこのベクトルが出力される確率）
	public double getProbability(double[] input){
		double output = 0;

		//共分散行列の行列式
		double det = 0;
		//指数部分
		double index = 0;
		//生起確率では、共分散行列は一般的な共分散行列として計算する。（対角行列を前提としない）
		//つまり上記の共分散実装をしやすいようにしておく

		//行列式の計算。天下り
		det = this.getDetV();

		//指数部分の計算
		double[] tempindex = new double[this.dimension];
		for(int i=0;i<this.dimension;i++){
			tempindex[i] = 0;
		}
		for(int i=0;i<this.dimension;i++){
			for(int j=0;j<this.dimension;j++){
				tempindex[i] += (input[j] - this.mean[j])*this.covariance[j][i];
			}
		}
		for(int i=0;i<this.dimension;i++){
			index += tempindex[i] * (input[i] - this.mean[i]);
		}
		//2πのd/2乗部分
		output = Math.pow(2*Math.PI, (double)this.dimension/2);
		output = (double)1/output;
		//共分散の行列式の二乗根で割る
		output /= Math.sqrt(det);
		//eの部分
		output *= Math.exp(index);

		return output;
	}
}
