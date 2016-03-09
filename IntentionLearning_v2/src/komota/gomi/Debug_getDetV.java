package komota.gomi;

import komota.lib.MyMatrix;

public class Debug_getDetV {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		int dimension = 3;

		MyMatrix mat1 = new MyMatrix(dimension);
		MyMatrix mat2 = new MyMatrix(dimension);

		mat1.setData(0, 0, 1);
		mat1.setData(0, 1, 1);
		mat1.setData(0, 2, 2);
		mat1.setData(1, 0, 3);
		mat1.setData(1, 1, 0);
		mat1.setData(1, 2, 8);
		mat1.setData(2, 0, 2);
		mat1.setData(2, 1, 3);
		mat1.setData(2, 2, 3);

		mat1.show();
		mat1.inv().show();
		mat1.inv().inv().show();
		mat1.trans().show();
		int count = 0;
		boolean flag = false;
		double mean = 0;
		while(!flag){
			count++;
			while(true){
				for(int i=0;i<dimension;i++){
					for(int j=0;j<dimension;j++){
						mat1.setData(i, j, (int)(Math.random()*200));
					}
				}
				if(mat1.getDetV() != 0){
					break;
				}
			}
			mat2 = mat1.mult(mat1.inv());
			mean *= count;
			for(int i=0;i<dimension;i++){
				for(int j=0;j<dimension;j++){
					if(i==j){
						mean += Math.abs(mat2.getData(i, j) - 1);
					}else{
						mean += Math.abs(mat2.getData(i, j));
					}
					if(i == j && Math.abs(mat2.getData(i, j) - 1) > 1){
						flag = true;
						System.out.println("mat1:");
						mat1.show();
						System.out.println("mat1.inv:");
						mat1.inv().show();
						System.out.println("mat2:");
						mat2.show();
					}
					else if(i != j && Math.abs(mat2.getData(i, j)) > 1){
						flag = true;
						System.out.println("mat1:");
						mat1.show();
						System.out.println("mat1.inv:");
						mat1.inv().show();
						System.out.println("mat2:");
						mat2.show();
					}
				}
			}
			mean /= count + dimension * dimension;
		}
		System.out.println("count:"+count+" mean:"+mean);


	}

}
