package komota.soinn;

public class KomotaSOINN_CosSimilarity extends KomotaSOINN{

	public KomotaSOINN_CosSimilarity(int dimension, int removeNodeTime, int deadAge) {
		super(dimension, removeNodeTime, deadAge);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	public double distance(double[] signal1, double[] signal2) {
		double output = 0;
		double det1 = 0;
		double det2 = 0;

		for(int i=0;i<this.getDimension();i++){
			output	 += signal1[i] * signal2[i];
			det1	 += signal1[i] * signal1[i];
			det2	 += signal2[i] * signal2[i];
		}
		output /= det1;
		output /= det2;

		return output;
	}

}
