package komota.supers;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class Goal {
	//目標地点。座標と有効範囲を持ち、「エンドエフェクタが目標に達しているか」を判定するメソッド、「エンドエフェクタと目標の距離」を出力するメソッドを持つ
	//後に目標地点が移動する場合などの拡張性も考え、速度も持つとする

	//描画に使う色の定数
	static final Color GOALCOLOR = new Color(120,120,20);
	static final Color GOALRANGECOLOR = new Color(120,120,20,50);


	//座標と範囲
	double x;
	double y;
	double range;

	//速度
	int vx = 0;
	int vy = 0;

	//描画するArmFrame
	ArmFrame frame = null;

	//コンストラクタ
	public Goal(int x,int y,int range,ArmFrame frame){
		this.x = x;
		this.y = y;
		this.range = range;
		this.frame = frame;
	}
	//XYのセッターとゲッター
	public Goal setXY(double[] xy){
		this.x = xy[0];
		this.y = xy[1];
		return this;
	}
	public double[] getXY(){
		double[] output = new double[2];
		output[0] = this.x;
		output[1] = this.y;
		return output;
	}

	//「エンドエフェクタと目標の距離」を出力するメソッド
	public double distanceFromEnd_Effector(){
		Joint effector = this.frame.hand.end_effector;

		double ex = effector.getXY()[0];
		double ey = effector.getXY()[1];

		ex -= this.x;
		ey -= this.y;

		return Math.sqrt(ex*ex + ey*ey);
	}

	//「エンドエフェクタが目標に達しているか」を判定するメソッド
	public boolean isGoal(){
		return distanceFromEnd_Effector()<this.range;
	}

	//移動処理
	public void move(){
		this.x += vx;
		this.y += vy;
	}

	//描画処理
	public void render(ArmFrame frame){
		Graphics2D g = (Graphics2D)frame.buffer.getDrawGraphics();

		//描画位置を、中心を(0,0)に平行移動
		AffineTransform oldtr = g.getTransform();
		g.translate(ArmFrame.FRAMESIZE/2, ArmFrame.FRAMESIZE/2);


		//目標範囲の描画
		g.setColor(Goal.GOALRANGECOLOR);
		g.fillOval((int)(this.x - this.range + Hand.JOINTSIZE/2), (int)(this.y - this.range + Hand.JOINTSIZE/2), (int)(2*this.range), (int)(2*this.range));

		//目標位置の描画
		g.setColor(Goal.GOALCOLOR);
		g.fillOval((int)this.x , (int)this.y , Hand.JOINTSIZE, Hand.JOINTSIZE);

		//Graphicsクラスの破棄、描画
		g.setTransform(oldtr);
		g.dispose();
	}


}
