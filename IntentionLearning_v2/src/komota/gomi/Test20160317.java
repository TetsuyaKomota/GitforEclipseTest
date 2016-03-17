package komota.gomi;

import komota.lib.MyIO;
import komota.lib.MyMatrix;
import komota.supers.AdjustedVisualSOINN;
import soinn.SOINN;

public class Test20160317 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		MyMatrix mat1 = new MyMatrix(11);
		MyIO io = new MyIO();
		io.readFile("test.txt");
		mat1 = io.readMatrix(1);
		mat1.show();
		double[] vec1 = mat1.vectorize();
		for(int i=0;i<vec1.length;i++){
			System.out.print(vec1[i]+" ");
		}
		System.out.println();

		//SOINNを試す
		SOINN soinn = new SOINN(121,1000,5);
		AdjustedVisualSOINN frame = new AdjustedVisualSOINN(soinn,"VisualSOINN",50,50,50,true);

		io.readFile("20160317/testoutputmatrix.txt");

		for(int idx=0;idx<70;idx++){
			mat1 = io.readMatrix(idx);
			if(mat1 != null){
				vec1 = mat1.vectorize();
				soinn.inputSignal(vec1);
				soinn.classify();
				System.out.println(idx+"番目のデータを入れたときは"+ soinn.getClassNum()+"個だよ");
			}
		}
		soinn.removeUnnecessaryNode();
		soinn.classify();


		if(soinn.getClassNum() == 1){
			System.out.println("やったね！");
		}
		else{
			System.out.println("ふぇぇ．．"+soinn.getClassNum()+"個だよ...");
		}
	}

}
