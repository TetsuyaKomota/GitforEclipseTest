package komota.lib;

import java.util.Random;

public class MatFactory {


	//特定の行列のファクトリーメソッド集

	//単位行列を作る
	public static MyMatrix unit(int dimension){
		MyMatrix output = new MyMatrix(dimension);
		for(int i=0;i<dimension;i++){
			output.setData(i, i, 1);
		}
		return output;
	}
	//一様乱数行列を作る
	public static MyMatrix random(int dimension,double sup,double inf){
		MyMatrix output = new MyMatrix(dimension);
		if(sup < inf){
			return output;
		}
		for(int i=0;i<dimension;i++){
			for(int j=0;j<dimension;j++){
				output.setData(i, j, Math.random()*(sup - inf) + inf);
			}
		}
		return output;
	}
	public static MyMatrix random(int dimension){
		return random(dimension,1,0);
	}


	//ガウス誤差行列を作る
	public static MyMatrix randomGaussian(int dimension,double variance){
		MyMatrix output = new MyMatrix(dimension);
		Random rand = new Random();
		if(variance < 0){
			return output;
		}
		for(int i=0;i<dimension;i++){
			for(int j=0;j<dimension;j++){
				output.setData(i, j,rand.nextGaussian() * Math.sqrt(variance));
			}
		}
		return output;
	}

}
