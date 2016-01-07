package komota.coordinate;

public class Normalizer {

	public static void main(String[] args){
		System.out.println("はろーわーるど");
		double[] vector = new double[2];
		double[] start = new double[2];
		double[] goal = new double[2];
		double[] newgoal = new double[2];

		start[0] = 0;
		start[1] = 0;
		goal[0] = 2;
		goal[1] = 4;
		newgoal[0] = 3;
		newgoal[1] = 3;


		vector[0] = goal[0] - start[0];
		vector[1] = goal[1] - start[1];

		System.out.println("正規化前のベクトルは("+vector[0]+","+vector[1]+")で、大きさは"+Math.sqrt(vector[0]*vector[0]+vector[1]*vector[1])+"だよ♪");

		double[][] inputs = new double[3][];
		inputs[0] = vector;
		inputs[1] = goal;
		inputs[2] = start;

		vector = Normalizer.normalize(inputs);

		System.out.println("正規化後のベクトルは("+vector[0]+","+vector[1]+")で、大きさは"+Math.sqrt(vector[0]*vector[0]+vector[1]*vector[1])+"だよ♪");

		inputs[0] = vector;
		inputs[1] = newgoal;
		inputs[2] = start;

		vector = Normalizer.inverseNormalize(inputs);

		System.out.println("逆正規化後のベクトルは("+vector[0]+","+vector[1]+")で、大きさは"+Math.sqrt(vector[0]*vector[0]+vector[1]*vector[1])+"だよ♪");

	}

	//定数
	//正規化後のベクトル長。normalizeでベクトルをこの長さに、inverseNormalizeでこの長さのベクトルをもとの長さに変換する
	static final double UNIT = 20;

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
		output[0] *= UNIT;
		output[1] *= UNIT;


		return output;
	}

	//投げられた被正規化ベクトルに与えられた二点間距離を反映させる逆正規化をかける。
	public static double[] inverseNormalize(double[][] inputs){
		double[] output = inputs[0];
		//二点間距離を求める
		double distance = (inputs[1][0] - inputs[2][0])*(inputs[1][0] - inputs[2][0])+(inputs[1][1] - inputs[2][1])*(inputs[1][1] - inputs[2][1]);
		distance = Math.sqrt(distance);
		//ベクトルを二点間距離で逆正規化する
		output[0] /= UNIT;
		output[1] /= UNIT;
		output[0] *= distance;
		output[1] *= distance;

		return output;
	}

}
