package komota.coordinate;

public class Coordinate_GL extends MyCoordinate{

	//引数はいずれも「goalpoint、reference、startpoint」の順で格納されている

	//タスク的視野で使用する、GLにおける視野
	public static double range_GL = 30;

	//rangeのゲッター
	@Override
	public double getRange(){
		return range_GL;
	}

	@Override
	public double[] convert(double[][] inputs){
//		System.out.println("[Coordinate2_1]convert:done");
		double[] output = new double[2];
		//参照点、動作開始点ベクトルV(=startpoint - reference)を作る
		double[] tempvec = new double[2];
		tempvec[0] = inputs[2][0] - inputs[1][0];
		tempvec[1] = inputs[2][1] - inputs[1][1];
		//|V|を求める。（正弦余弦に使う）
		double tempsize = Math.sqrt(tempvec[0]*tempvec[0] + tempvec[1]*tempvec[1]);
		//正弦、余弦を求める
		double tempcos = tempvec[1]/tempsize;
		double tempsin = tempvec[0]/tempsize;
		output[1] = inputs[0][1]*tempcos + inputs[0][0]*tempsin;
		output[0] = inputs[0][0]*tempcos - inputs[0][1]*tempsin;
		return output;
	}
	@Override
	public double[] inverseConvert(double[][] inputs){
//		System.out.println("[Coordinate2_1]inverseConvert:done");
		double[] output = new double[2];
		//参照点、動作開始点ベクトルV(=startpoint - reference)を作る
		double[] tempvec = new double[2];
		tempvec[0] = inputs[2][0] - inputs[1][0];
		tempvec[1] = inputs[2][1] - inputs[1][1];
		//|V|を求める。（正弦余弦に使う）
		double tempsize = Math.sqrt(tempvec[0]*tempvec[0] + tempvec[1]*tempvec[1]);
		//正弦、余弦を求める
		double tempcos = tempvec[1]/tempsize;
		double tempsin = tempvec[0]/tempsize;
		output[1] = inputs[0][1]*tempcos - inputs[0][0]*tempsin;
		output[0] = inputs[0][0]*tempcos + inputs[0][1]*tempsin;
		//GLはなんか左右反転いらないみたいなので-1倍はしない
		//output[0]*=-1;
		//output[1]*=-1;
		return output;
	}

}
