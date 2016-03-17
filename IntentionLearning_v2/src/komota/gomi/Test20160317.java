package komota.gomi;

import komota.lib.MyIO;
import komota.lib.MyMatrix;
import komota.supers.AdjustedVisualSOINN;

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
		ExtendedSOINN soinn = new ExtendedSOINN(121,1000,5);
		AdjustedVisualSOINN frame = new AdjustedVisualSOINN(soinn,"VisualSOINN",50,50,50,true);

		io.readFile("20160317/testoutputmatrix.txt");

		for(int idx=0;idx<70;idx++){
			mat1 = io.readMatrix(idx);


			if(mat1 != null){
				//まったく同じデータではノードが死んでしまうので，微妙な揺らぎを補正
				//実際は行列自体が全くきれいなものになってしまうことこそ問題なので，その原因を探すこと
				for(int i=0;i<mat1.getDimension();i++){
					for(int j=0;j<mat1.getDimension();j++){
						mat1.setData(i, j, mat1.getData(i,j)+(Math.random()-0.5)*0.0001);
					}
				}
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
			vec1 = soinn.getNodeMean(0);
			mat1 = new MyMatrix(11,vec1);
			mat1.show_approximately();
		}
		else{
			System.out.println("ふぇぇ．．"+soinn.getClassNum()+"個だよ...");
		}

/*
		soinn = new SOINN(3,10,5);
		for(int i=0;i<100;i++){
			double[] vec = new double[3];
			vec[0] = 3;
			vec[1] = 3;
			vec[2] = 3;

			soinn.inputSignal(vec);
			soinn.classify();
			System.out.println(i+"番目のデータを入れたときは"+ soinn.getClassNum()+"個だよ");

		}
		soinn.removeUnnecessaryNode();
		soinn.classify();


		if(soinn.getClassNum() == 1){
			System.out.println("やったね！");
		}
		else{
			System.out.println("ふぇぇ．．"+soinn.getClassNum()+"個だよ...");
		}
*/

	}



}
