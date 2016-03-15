package komota.gomi;

import komota.lib.MyIO;
import komota.lib.MyMatrix;

public class Test20160315 {


	//行列の雑なテストをMyMatrixから移植したもの
	/**
	 * @param args
	 */
	public static void main(String[] args){
		int dim = 3;
		int count = 0;
		int check = 1;
		double mean = 0;
		double variance = 10;
		MyMatrix mat1 = new MyMatrix(dim);
		MyMatrix mat2 = new MyMatrix(dim);
		MyMatrix means = new MyMatrix(dim);

		//出力クラス
		MyIO io = new MyIO();
		io.readFile("test.txt");
		io.writeFile("test.txt");

		//行列式が1になる行列とは珍しいものなのかを試行
		//この実験から，行列式は大抵の場合，e14~e17程度のスケールとなることが判明
/*
		while(true){
			count++;
			for(int i=0;i<dim;i++){
				for(int j=0;j<dim;j++){
					mat1.setData(i, j, (int)(Math.random()*200));
				}
			}
			if(Math.abs(mat1.getDetV()) <= 10000 && Math.abs(mat1.getDetV()) >= 0.000001){
				break;
			}
			else{
				//行列式の平均を求めてみよう
				mean *= count-1;
				mean += mat1.getDetV();
				mean /= count;
			}
			if(count == 1000*check){
				check++;
				System.out.println("done:"+1000*check + "  DET:"+ mat1.getDetV() + "  mean(DET):"+mean);
			}
		}
		System.out.println(count + ": DET="+mat1.getDetV());
		mat1.show();
		mat1.inv().show();
*/
		//逆行列の成分に，元の行列の成分の最大値を超えるような成分が出現することは珍しいものなのかを試行
		//この実験から，元行列の最大の成分の平方根サイズでもあまり現れないことが分かった
/*
		double max;

		while(true){
			count++;
			while(true){
				max = 0;
				for(int i=0;i<dim;i++){
					for(int j=0;j<dim;j++){
						mat1.setData(i, j, (int)(Math.random()*200));
						if(mat1.getData(i, j)>max){
							max = mat1.getData(i, j);
						}
					}
				}
				if(mat1.getDetV() != 0){
					break;
				}
			}
			mat1 = mat1.inv();
			boolean flag = false;
			for(int i=0;i<dim;i++){
				for(int j=0;j<dim;j++){
					if(Math.abs(mat1.getData(i, j)) > Math.sqrt(max)){
						flag = true;
					}
				}
			}
			if(flag == true){
				break;
			}
			else if(count == 1000*check){
				check++;
				System.out.println("done:"+1000*check + "  DET:"+ mat1.getDetV());
			}
		}
		System.out.println(count + ": MAX="+max + ": DET="+mat1.getDetV());
//		mat1.inv().show();
//		mat1.show();
*/
		//200*200空間の教示データをランダム生成
		//逆行列化
		//ガウス誤差との積を求める
		//平均とりまくる．たまに出力する

		//同じ計算を20回繰り返す
/*
		for(int T=0;T<20;T++){
			count = 0;
			means = new MyMatrix(dim);
			for(int time=0;time<8;time++){
				while(true){
					count++;
					while(true){
						for(int i=0;i<dim;i++){
							for(int j=0;j<dim;j++){
								mat1.setData(i, j, (int)(Math.random()*200));
							}
						}
						if(mat1.getDetV() != 0){
							break;
						}
					}

					mat1 = mat1.inv();

					for(int i=0;i<dim;i++){
						for(int j=0;j<dim;j++){
							double rand = Math.random();
							double x = -100;
							double gauss = 0;
							while(true){
								gauss += Math.exp(-x*x / (2*variance)) / Math.sqrt(2*Math.PI*variance);
								if(rand < gauss){
									mat2.setData(i, j, (int)x);
									break;
								}
								else{
									x++;
								}
							}
						}
					}
					//平均を更新
					means = means.mult(count-1).add(mat1.mult(mat2)).mult((double)1/count);

					//平均を更新
					//一定回数で出力
					if(count >= Math.pow(10, time)){
						break;
					}
				}
				//出力誤差の平均を求める
				double sum = 0;
				for(int i=0;i<dim;i++){
					for(int j=0;j<dim;j++){
						sum += means.getData(i, j);
					}
				}
				sum /= dim*dim;

				int temp = 0;
				io.print("TIME,1");
				while(temp < time){
					io.print("0");
					temp++;
				}
				io.println(",MEAN,"+sum);
				//means.show();
				io.execute();
			}
		}
		io.close();
*/
		mat1 = io.readMatrix(0);
		mat1.show();
		io.printMatrix(mat1.mult(3), 1);
	}

}
