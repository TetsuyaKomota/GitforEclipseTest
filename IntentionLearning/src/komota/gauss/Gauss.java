package komota.gauss;

import java.util.Random;

public class Gauss {

	//平均ベクトルと分散共分散行列を持つガウスクラス。

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
	public double[][] getCovariance(){
		return this.covariance;
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
}
