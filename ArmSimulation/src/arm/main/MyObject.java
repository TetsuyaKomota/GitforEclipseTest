package arm.main;

import java.awt.Color;
import java.awt.Graphics2D;

import arm.libraries.MyVector;

public class MyObject {

	//Handの関節含め、ArmFrame上に存在する「点物体」のクラス

	//定数
	//範囲の透明度
	static final float RANGEALPHA = 0.5f;
	//点物体のサイズ
	static final int SIZE = 10;

	//フィールド

	//位置ベクトル
	MyVector position;
	//速度ベクトル
	MyVector velocity;
	//加速度ベクトル
	MyVector acceleration;

	//範囲
	double range = 0;

	//描画色
	Color color = Color.black;

	//コンストラクタ
	public MyObject(MyVector position,MyVector velocity, MyVector acceleration){
		this.position = position;
		this.velocity = velocity;
		this.acceleration = acceleration;
	}
	public MyObject(double x,double y,double vx,double vy,double ax,double ay){
		this.position = new MyVector(x,y);
		this.velocity = new MyVector(vx,vy);
		this.acceleration = new MyVector(ax,ay);
	}
	public MyObject(){
		this(0,0,0,0,0,0);
	}

	//各種セッター、ゲッター
	public MyObject setPosition(MyVector position){
		this.position = position;
		return this;
	}
	public MyObject setPosition(double x,double y){
		this.position = new MyVector(x,y);
		return this;
	}
	public MyObject setVelocity(MyVector velocity){
		this.velocity = velocity;;
		return this;
	}
	public MyObject setVelocity(double x,double y){
		this.velocity = new MyVector(x,y);
		return this;
	}
	public MyObject setAcceleration(MyVector acceleration){
		this.acceleration = acceleration;
		return this;
	}
	public MyObject setAcceleration(double x,double y){
		this.acceleration = new MyVector(x,y);
		return this;
	}
	public MyObject setColor(Color color){
		this.color = color;
		return this;
	}
	public MyObject setRange(double range){
		this.range = range;
		return this;
	}
	public MyVector getPosition(){
		return this.position;
	}
	public MyVector getVelocity(){
		return this.velocity;
	}
	public MyVector getAcceleration(){
		return this.acceleration;
	}
	public Color getColor(){
		return this.color;
	}
	public double getRange(){
		return this.range;
	}

	//距離の算出
	public double distance(MyObject opp){
		double output = this.getPosition().distance(opp.getPosition());
		return output;
	}
	//衝突判定
	public boolean isCollision(MyObject opp){
		if(this.range >= this.distance(opp) || opp.range >= this.distance(opp)){
			return true;
		}
		return false;
	}

	//移動
	public void move(){
		this.velocity.add(this.acceleration);
		this.position.add(this.velocity);
	}

	//描画
	public void draw(ArmFrame frame){
		float[] temp = this.color.getColorComponents(null);
		Color rangecolor = new Color(temp[0],temp[1],temp[2],RANGEALPHA);

		Graphics2D g = (Graphics2D)frame.buffer.getDrawGraphics();
		g.setColor(rangecolor);
		g.fillOval((int)(this.position.getX()-this.range/2), (int)(this.position.getY()-this.range/2), (int)this.range, (int)this.range);
		g.setColor(this.color);
		g.fillOval((int)(this.position.getX()-SIZE/2),(int)(this.position.getY()-SIZE/2),SIZE,SIZE);
		g.dispose();
	}


}
