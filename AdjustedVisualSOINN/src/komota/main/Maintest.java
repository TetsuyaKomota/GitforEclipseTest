package komota.main;

import java.util.ArrayList;

import komota.supers.AdjustedVisualSOINN;
import komota.supers.MyDataFilter;
import soinn.SOINN;

public class Maintest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ


		ArrayList<double[]> samplegauss = new ArrayList<double[]>();

		SupervisedGauss gauss = new SupervisedGauss();


		samplegauss = MyDataFilter.normalization(gauss.gauss);
		SOINN soinn = new SOINN(2,100,10);


		AdjustedVisualSOINN frame = new AdjustedVisualSOINN(soinn);

		//VisualSOINNを生成してからデータを与える。（順序通り）

		frame.setDataset(samplegauss);
		frame.setRealtimeFlag(true);
		frame.setRandomDataFlag(true);


		/********************************************************************************/

		ArrayList<double[]> samplegauss2 = new ArrayList<double[]>();

		SupervisedGauss gauss2 = new SupervisedGauss();

		samplegauss2 = MyDataFilter.normalization(gauss2.gauss);

		//MovingDataGenerator g = new MovingDataGenerator();

		//g.moveI(100, 1);

		//samplegauss2 = g.dataset;

		SOINN soinn2 = new SOINN(2,1000000000,0);


		AdjustedVisualSOINN frame2 = new AdjustedVisualSOINN(soinn2);

		//VisualSOINNを生成してからデータを与える。（順序通り）

		frame2.setDataset(samplegauss2);
		frame2.setRealtimeFlag(true);
		frame2.setRandomDataFlag(true);
		//frame2.setRandomDataFlag(false);

	}

}
