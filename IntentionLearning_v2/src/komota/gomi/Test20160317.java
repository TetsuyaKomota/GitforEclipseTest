package komota.gomi;

import komota.lib.MyIO;
import komota.lib.MyMatrix;
import soinn.SOINN;

public class Test20160317 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		SOINN soinn = new SOINN(0,0,0);
		MyMatrix mat1 = new MyMatrix(3);
		MyIO io = new MyIO();
		io.readFile("test.txt");
		mat1 = io.readMatrix(1);
		mat1.show();
		double[] vec1 = mat1.vectorize();
		for(int i=0;i<vec1.length;i++){
			System.out.print(vec1[i]+" ");
		}
		System.out.println();
	}

}
