package komota.coordinate;

public class Normalizer {

	public static void main(String[] args){
		System.out.println("はろーわーるど");
	}

	//引数は「goalpoint(変換元ベクトル),reference(基準となる点),startpoint(基準となる点)」と定める
	//referenceとstartpointに明確な違いはない。あくまでMyCoordinateとそろえるため

	//投げられたベクトルに与えられた二点間距離を１とする正規化をかける。
	public static double[] normalize(double[][] inputs){
		double[] output = inputs[0];
		//二点間距離を求める
		double distance = (inputs[1][0] - inputs[2][0])*(inputs[1][0] - inputs[2][0])+(inputs[1][1] - inputs[2][1])*(inputs[1][1] - inputs[2][1]);
		distance = Math.sqrt(distance);
		//ベクトルを二点間距離で正規化する
		output[0] /= distance;
		output[1] /= distance;

		return output;
	}

	//投げられた被正規化ベクトルに与えられた二点間距離を反映させる逆正規化をかける。
	public static double[] inverseNormalize(double[][] inputs){
		double[] output = inputs[0];
		//二点間距離を求める
		double distance = (inputs[1][0] - inputs[2][0])*(inputs[1][0] - inputs[2][0])+(inputs[1][1] - inputs[2][1])*(inputs[1][1] - inputs[2][1]);
		distance = Math.sqrt(distance);
		//ベクトルを二点間距離で逆正規化する
		output[0] *= distance;
		output[1] *= distance;

		return output;
	}

}
