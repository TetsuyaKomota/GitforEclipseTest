package komota.gomi;

import komota.lib.MatFactory;
import komota.lib.MyIO;
import komota.lib.MyMatrix;

public class Test20160324 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		//次元
		int dimension = 2;
		//ガウス誤差
		double variance = -0.1;
		//ノルム誤差
		double error = 0;
		//誤差指標
		double threshold = 1;

		MyIO io = new MyIO();

		io.writeFile("20160324/result.txt");

		for(int d=0;d<1000;d++){
			variance = -0.1;
			dimension++;
			for(int v=0;v<21;v++){
				variance+=0.1;
				MyMatrix mat1 = new MyMatrix(dimension);
				MyMatrix mat2 = new MyMatrix(dimension);
				MyMatrix mat3 = new MyMatrix(dimension);

				int count = 0;
				double mean = 0;

				while(count++ < 1000){

					mat1 = MatFactory.unit(dimension);

					mat2 = MatFactory.random(dimension);

					mat3 = mat2.add(MatFactory.randomGaussian(dimension, variance));

					error = mat3.mult(mat2.inv()).sub(mat1).getMaxNorm();

					mean *= count;
					mean += error;
					mean /= count+1;

				}
				io.println("dimension,"+dimension+",variance,"+variance+",mean,"+mean);
			}
		}
	}





}
