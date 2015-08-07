package arm.libraries;

import java.awt.geom.Point2D;

public class MyVector {

	//MyObjectの位置や速度を表すのに使用するベクトルクラス

	//位置
	Point2D.Double position;
	//範囲
	double range;

	//コンストラクタ
	public MyVector(Point2D.Double position,double range){
		this.position = position;
		this.range = range;
	}
	public MyVector(double x,double y){
		this.position = new Point2D.Double(x, y);
	}

	//各セッター、ゲッター
	public MyVector setVector(Point2D.Double position){
		this.position = position;
		return this;
	}
	public MyVector setVector(double x,double y){
		this.position = new Point2D.Double(x,y);
		return this;
	}
	public Point2D getVector(){
		return this.position;
	}
	public double getX(){
		return this.position.x;
	}
	public double getY(){
		return this.position.y;
	}

	//ベクトル長を取得
	public double getLength(){
		double output = Math.sqrt(this.position.x * this.position.x + this.position.y * this.position.y);
		return output;
	}

	//距離の計算
	public double distance(MyVector opp){
		double output;
		double tempx = this.position.x - opp.position.x;
		double tempy = this.position.y - opp.position.y;
		output = Math.sqrt(tempx*tempx + tempy*tempy);
		return output;
	}
	//ベクトル同士の加減算。元のベクトル値を更新するため、二つのベクトルのうち一方の値は変更される
	public MyVector add(MyVector opp){
		double tempx = this.position.x + opp.position.x;
		double tempy = this.position.y + opp.position.y;
		return this.setVector(tempx,tempy);
	}
	public MyVector add(double x,double y){
		double tempx = this.position.x + x;
		double tempy = this.position.y + y;
		return this.setVector(tempx,tempy);
	}
	public MyVector sub(MyVector opp){
		double tempx = this.position.x - opp.position.x;
		double tempy = this.position.y - opp.position.y;
		return this.setVector(tempx,tempy);
	}
	public MyVector sub(double x,double y){
		double tempx = this.position.x - x;
		double tempy = this.position.y - y;
		return this.setVector(tempx,tempy);
	}

	//元のベクトルを保持して、新しいインスタンスを生成する加減算。インスタンス生成の頻発は処理速度低下につながるため、なるべく上記を使用すること
	public MyVector addNew(MyVector opp){
		double tempx = this.position.x + opp.position.x;
		double tempy = this.position.y + opp.position.y;
		return new MyVector(tempx,tempy);
	}
	public MyVector addNew(double x,double y){
		double tempx = this.position.x + x;
		double tempy = this.position.y + y;
		return new MyVector(tempx,tempy);
	}
	public MyVector subNew(MyVector opp){
		double tempx = this.position.x - opp.position.x;
		double tempy = this.position.y - opp.position.y;
		return new MyVector(tempx,tempy);
	}
	public MyVector subNew(double x,double y){
		double tempx = this.position.x - x;
		double tempy = this.position.y - y;
		return new MyVector(tempx,tempy);
	}

	//内積
	public double innerProduct(MyVector opp){
		double output = this.position.x * opp.position.x + this.position.y * opp.position.y;
		return output;
	}

}
