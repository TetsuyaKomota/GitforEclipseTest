package komota.soinn;

public class KomotaSOINN_Euclidean extends KomotaSOINN{

	public KomotaSOINN_Euclidean(int dimension, int removeNodeTime, int deadAge) {
		super(dimension, removeNodeTime, deadAge);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	public double distance(double[] signal1, double[] signal2) {
		if ((signal1 == null) || (signal2 == null))
		{
			return 0.0;
		}

		double sum = 0.0;
		for (int i = 0; i < this.getDimension(); i++)
		{
			sum += (signal1[i] - signal2[i]) * (signal1[i] - signal2[i]);
		}

		return Math.sqrt(sum);
	}


}
