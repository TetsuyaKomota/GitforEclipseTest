package komota.main;

import java.util.ArrayList;

public class MovingDataGenerator {

	//時間ごとに遷移するデータに対するSOINNの挙動をチェックするためのデータセットジェネレータ

	//データセット
	ArrayList<double[]> dataset;

	//コンストラクタ
	public MovingDataGenerator(){

	}

	//動く「I」
	void moveI(int numofdata,int velocity){
		this.dataset = new ArrayList<double[]>();
		double[] temp;
		//velocity + idx を中心 0 に
		/*
		 *   *****
		 *    ***
		 *    ***
		 *    *0*
		 *    ***
		 *    ***
		 *   *****
		 *
		 */

		for(int idx = 0;idx<numofdata;idx++){

			for(int t=0;t<20;t++){

				for(int i=0;i<50;i++){
					temp = new double[2];

					temp[1] = 100 - 25 + 3*(Math.random()-0.5);
					temp[0] = velocity * idx - 24 + i+ 7*(Math.random()-0.5);

					this.dataset.add(temp);
				}
				for(int i=0;i<50;i++){
					for(int j=0;j<30;j++){

						temp = new double[2];

						temp[1] = 100 - 15 + i+ 7*(Math.random()-0.5);
						temp[0] = velocity * idx - 1 + j+ 3*(Math.random()-0.5);

						this.dataset.add(temp);
					}
				}
				for(int i=0;i<50;i++){
					temp = new double[2];

					temp[1] = 100 + 25+ 3*(Math.random()-0.5);
					temp[0] = velocity * idx - 24 + i+ 7*(Math.random()-0.5);

					this.dataset.add(temp);
				}
			}
		}
	}

}
