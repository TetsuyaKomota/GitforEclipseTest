package komota.soinn;

public class KomotaSOINN_Chebyshev extends KomotaSOINN{

	public KomotaSOINN_Chebyshev(int dimension, int removeNodeTime, int deadAge) {
		super(dimension, removeNodeTime, deadAge);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	public double distance(double[] signal1, double[] signal2) {
		if ((signal1 == null) || (signal2 == null))
		{
			return 0.0;
		}

		double max = 0.0;
		for (int i = 0; i < this.getDimension(); i++)
		{
			//sum += (signal1[i] - signal2[i]) * (signal1[i] - signal2[i]);
			if(max < (signal1[i] - signal2[i]) * (signal1[i] - signal2[i])){
				max = (signal1[i] - signal2[i]) * (signal1[i] - signal2[i]);
			}
		}

		return Math.sqrt(max);
	}


}
