
package komota.lib;

import java.util.ArrayList;

public class HyperMean {

	//ベクトルデータ集合の外れ値を取り除くクラス．


	public static void main(String[] args){

		ArrayList<double[]> temp = new ArrayList<double[]>();
		for(int i=0;i<1000;i++){
			double[] data = new double[121];
			for(int j=1;j<=9;j++){
					data[j] = j + (double)j*(Math.random()-0.5);
			}
			if(Math.random() < 0.1){
				for(int k=0;k<data.length;k++){
					data[k] = 10000;
				}
			}
			temp.add(data);
		}
		//普通に平均をとる
		double[] normalmean = new double[temp.get(0).length];
		for(int i=0;i<normalmean.length;i++){
			for(int j=0;j<temp.size();j++){
				normalmean[i] += temp.get(j)[i];
			}
			normalmean[i] /= temp.size();
		}

		System.out.println("普通の平均");
		for(int i=0;i<normalmean.length;i++){
			System.out.print(normalmean[i]+" ");
		}
		System.out.println();

		//HyperMeanを用いた平均
		HyperMean hy = new HyperMean();
		for(int i=0;i<temp.size();i++){
			hy.addData(temp.get(i));
		}
		double[] hypermean = hy.getHyperMean();

		System.out.println("HyperMean による平均");
		for(int i=0;i<hypermean.length;i++){
			System.out.print(hypermean[i]+" ");
		}
		System.out.println();
	}

	//定数
	//外れ値閾値
	private static final double threshold = 0.3;

	//フィールド
	//入力ベクトル集合
	private ArrayList<double[]> data;
	//ベクトル長さ
	private int datalength = 0;

	//コンストラクタ
	public HyperMean(){
		this.data = new ArrayList<double[]>();
	}

	//データインプット
	public void addData(double[] input){
		if(this.datalength == 0 || this.datalength == input.length){
			this.data.add(input);
			if(this.datalength == 0){
				this.datalength = input.length;
			}
		}
		else{
			System.out.println("[HyperMean]addData:HyperMean クラスに代入したベクトルの長さが不正です");
		}
	}

	//枝刈り平均出力
	public double[] getHyperMean(){
		double [] output = new double[this.datalength];

		//最大外れ値を取り除いた際の平均値移動量が閾値未満になるまで繰り返す
		while(this.data.size() > 1){
			//平均を計算する
			double[] before = this.getMean_Euclidean(this.data);
			//平均から最も離れたデータを探す
			double max = 0;
			int idx = -1;
			for(int i=0;i<this.data.size();i++){
				if(this.getDistance_Euclidean(before, this.data.get(i)) > max){
					max = this.getDistance_Euclidean(before, this.data.get(i));
					idx = i;
				}
			}
			//それを取り除き，もう一度平均を計算する
			double[] escdata = this.data.remove(idx);
			double[] after = this.getMean_Euclidean(this.data);
			//最初に求めた平均との差を求める
			double improve = this.getDistance_Euclidean(before, after);
			//閾値以上なら取り除いたデータを削除し，最初に戻る
			if(improve >= HyperMean.threshold){
			}
			//閾値未満なら取り除いたデータを入れ直し，ループを終了する
			else{
				this.data.add(escdata);
				output = before;
				break;
			}
		}

		System.out.println("[HyperMean]getHyperMean:残ったデータ量:"+this.data.size());

		return output;
	}

	//平均計算．とりあえず単純に足して割る．今後もっと特殊な平均計算を行う可能性もあるので一応メソッド化しておく
	private double[] getMean_Euclidean(ArrayList<double[]> input){
		double[] output = new double[input.get(0).length];
		for(int j=0;j<output.length;j++){
			for(int i=0;i<input.size();i++){
				output[j] += input.get(i)[j];
			}
			output[j] /= input.size();
		}
		return output;
	}
	//距離計算．とりあえず単純にユークリッド距離．今後もっと(略
	private double getDistance_Euclidean(double[] input1, double[] input2){
		double output = 0;

		for(int i=0;i<input1.length;i++){
			output += (input1[i] - input2[i]) * (input1[i] - input2[i]);
		}
		output = Math.sqrt(output);

		return output;
	}

}