package komota.coordinate;

public class Coordinate2_1 extends MyCoordinate_TEST{

	@Override
	public double[] convert(double[][] inputs){
		System.out.println("[Coordinate2_1]convert:done");
		return super.convert(inputs);
	}
	@Override
	public double[] inverseConvert(double[][] inputs){
		System.out.println("[Coordinate2_1]inverseConvert:done");
		return super.convert(inputs);
	}

}
