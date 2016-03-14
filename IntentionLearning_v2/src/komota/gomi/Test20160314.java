package komota.gomi;

import komota.lib.MyIO;
import komota.lib.MyMatrix;
import komota.main.MyPR;
import komota.main.PR_Mat;

public class Test20160314 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		String test1 = "unkoMat_ID:TEST";
		String test2 = "hogehoge";
		String test3 = "Mat_ID:";

		System.out.println("TEST1:leangth of splitted:"+test1.split("Mat_ID:").length);
		System.out.println("TEST2:leangth of splitted:"+test2.split("Mat_ID:").length);
		System.out.println("TEST3:leangth of splitted:"+test3.split("Mat_ID:").length);

		System.out.println("TEST1[0]:"+test1.split("Mat_ID:")[0]+" TEST1[1]:"+test1.split("Mat_ID:")[1]);
		System.out.println("TEST2[0]:"+test2.split("Mat_ID:")[0]);

		//行列読み込みのテスト
		MyIO testio = new MyIO();
		testio.readFile("20160314/mats.txt");
		MyMatrix testmat1 = testio.readMatrix(1);
		MyMatrix testmat2 = testio.readMatrix(2);
		if(testmat1 != null && testmat2 != null){
			testmat1.show();
			testmat2.show();
		}
		else{
			System.out.println("へたくそ！");
		}

		testmat2.mult(testmat1.inv()).show();
		//概数化のテスト
		testmat2.mult(testmat1.inv()).approximate().show();


		//線形代数モデルのテスト
		MyPR pr = new MyPR("logdata.txt");
		//pr.show();
		System.out.println("ここは越えたよ");

		PR_Mat prmat = new PR_Mat("logdata.txt");
		System.out.println("ここも越えたよ");

		prmat.show();

	}

}
