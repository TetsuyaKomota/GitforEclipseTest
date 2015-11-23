package komota.coordinate;

public class Coordinate_4_1 extends MyCoordinate {

	//座標変換を行うクラス。PR一つ一つに異なるMyCoordinateを与えることによって、各PRごとに座標変換を割り当てることができる

	//定数
	//フィールド

	//変換メソッド。座標系ごとに、変換に必要なベクトルの種類や数が違うため、ベクトルの配列を引数に渡す
	//ルールとして、引数に渡すベクトル配列は、必ず変換元のベクトルを第一引数にする
	public double[] convert(double[][] inputs){
		double[] output = new double[2];
		//ここでは単純に恒等写像。オーバーライドして使う
		output[0] = inputs[0][0];
		output[1] = inputs[0][1];
		return output;
	}
	//逆変換メソッド。座標系ごとに、変換に必要なベクトルの種類や数が違うため、ベクトルの配列を引数に渡す
	//ルールとして、引数に渡すベクトル配列は、必ず変換元のベクトルを第一引数にする
	public double[] inverseConvert(double[][] inputs){
		double[] output = new double[2];
		//ここでは単純に恒等写像。オーバーライドして使う
		output[0] = inputs[0][0];
		output[1] = inputs[0][1];
		return output;
	}

}
