package arm.libraries;

import java.awt.geom.Point2D;

public class MyVector {

	//MyObjectの位置や速度を表すのに使用するベクトルクラス

	//位置
	Point2D.Double position;

	//コンストラクタ
	public MyVector(Point2D.Double position){
		this.position = position;
	}
	public MyVector(double x,double y){
		this.position = new Point2D.Double(x, y);
	}

	//ベクトル同士の加減算
	public MyVector add(){
		return null;
	}
	public MyVector sub(){
		return null;
	}
	//内積
	public MyVector in(){
		return null;
	}

}
