package komota.gomi;

import komota.lib.MyIO;
import komota.lib.MyMatrix;

public class Test20160518 {

	public static void main(String[] args){
		MyIO io = new MyIO();
		io.readFile("logdata.txt");
		MyMatrix mat1 = io.readMatrix(30);
		MyMatrix mat2 = io.readMatrix(40);

		mat1.show();
		mat2.show();

		MyMatrix mat3 = mat1.convolution(mat2, 1, true).approximate();
		mat3.show();
		MyMatrix mat4 = mat1.convolution(mat2, 1, false).approximate();
		mat4.show();
		MyMatrix mat5 = mat1.convolution(mat2, 2, true).approximate();
		mat5.show();
		MyMatrix mat6 = mat1.convolution(mat2, 0.5, true).approximate();
		mat6.show();
	}

}
