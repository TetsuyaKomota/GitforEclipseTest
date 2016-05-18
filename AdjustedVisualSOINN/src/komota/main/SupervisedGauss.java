package komota.main;

import java.util.ArrayList;
import java.util.Random;

//2015/5/29
//２次元ガウス分布＋一様ノイズを教師有で生成するクラス
//SOINNには教師信号は代入しない
//クラス番号-1がノイズ



public class SupervisedGauss {
	public ArrayList<double[]> gauss = new ArrayList<double[]>();

	static final int FINALNUM = 10000;

	int num;
	final double NOISE = 0.5;
	Random rand = new Random();
	//コンストラクタ
	public SupervisedGauss(int num){
		this.num = num;
		for (int i = 0; i < num; i++) {
			double[] temp = new double[3];
			temp[0] = 0.7 * rand.nextGaussian() + 5;
			temp[1] = 0.7 * rand.nextGaussian() + 5;
			temp[2] = 0;
			gauss.add(temp);
			//一様ノイズ
			if(Math.random() < NOISE){
				temp = new double[3];
				temp[0] = Math.random() * 20 - 2;
				temp[1] = Math.random() * 20 - 2;
				temp[2] = -1;
				gauss.add(temp);
			}
		}
		for (int i = 0; i < num; i++) {
			double[] temp = new double[3];
			temp[0] = 0.7 * rand.nextGaussian() + 15;
			temp[1] = 0.7 * rand.nextGaussian() + 10;
			temp[2] = 1;
			gauss.add(temp);
			//一様ノイズ
			if(Math.random() < NOISE){
				temp = new double[3];
				temp[0] = Math.random() * 20 - 2;
				temp[1] = Math.random() * 20 - 2;
				temp[2] = -1;
				gauss.add(temp);
			}
		}
		for (int i = 0; i < num; i++) {
			double[] temp = new double[3];
			temp[0] = 0.7 * rand.nextGaussian() +5;
			temp[1] = 0.7 * rand.nextGaussian() + 15;
			temp[2] = 2;
			gauss.add(temp);
			temp = new double[3];
			temp[0] = 0.7 * rand.nextGaussian() + 11;
			temp[1] = 0.7 * rand.nextGaussian() + 13;
			temp[2] = 3;
			gauss.add(temp);
			//一様ノイズ
			if(Math.random() < NOISE){
				temp = new double[3];
				temp[0] = Math.random() * 20 - 2;
				temp[1] = Math.random() * 20 - 2;
				temp[2] = -1;
				gauss.add(temp);
			}
		}
		/*
		for(int i = 0;i < num;i++){
			double[] temp = new double[2];
			temp[0] = i / 10;
			temp[1] = Math.sin(temp[0]) * 5;
			temp = new double[2];
			temp[0] = i / 10;
			temp[1] = Math.sin(temp[0]) * 5;
			gauss.add(temp);
			//一様ノイズ
			if(Math.random() < NOISE){
				temp = new double[2];
				temp[0] = Math.random() * 10 - 10;
				temp[1] = Math.random() * 10 - 10;
				gauss.add(temp);
			}
		}
		*/
	}

	public SupervisedGauss(){
		this(SupervisedGauss.FINALNUM);
	}

	public int gausssize(){
		return gauss.size();
	}

	public double[] gaussget(int i){
		return gauss.get(i);
	}


}
