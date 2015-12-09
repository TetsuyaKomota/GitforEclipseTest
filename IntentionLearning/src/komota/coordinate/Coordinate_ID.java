package komota.coordinate;

public class Coordinate_ID extends MyCoordinate{

	//変換、逆変換ともにただの恒等写像

	//タスク的視野で使用する、IDにおける視野
	public static double range_ID = 10;

	@Override
	public double[] convert(double[][] inputs){
		double[] output = new double[2];
		output[0] = inputs[0][0];
		output[1] = inputs[0][1];
		return output;
	}
	@Override
	public double[] inverseConvert(double[][] inputs){
		double[] output = new double[2];
		output[0] = inputs[0][0];
		output[1] = inputs[0][1];
		return output;
	}

}
