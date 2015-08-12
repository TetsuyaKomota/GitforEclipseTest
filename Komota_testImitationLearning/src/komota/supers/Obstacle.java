package komota.supers;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class Obstacle {
	//障害物。座標と有効範囲を持ち、「エンドエフェクタが障害に達しているか」を判定するメソッド、「エンドエフェクタと障害の距離」を出力するメソッドを持つ
	//後に障害地点が移動する場合などの拡張性も考え、速度も持つとする

	//描画に使う色の定数
	static final Color OBSTACLECOLOR = new Color(20,20,120);
	static final Color OBSTACLERANGECOLOR = new Color(20,20,120,50);


	//座標と範囲
	int x;
	int y;
	int range;

	//速度
	int vx = 0;
	int vy = 0;

	//描画するArmFrame
	ArmFrame frame = null;

	//コンストラクタ
	public Obstacle(int x,int y,int range,ArmFrame frame){
		this.x = x;
		this.y = y;
		this.range = range;
		this.frame = frame;
	}

	//「エンドエフェクタと障害の距離」を出力するメソッド
	public double distanceFromEnd_Effector(){
		Joint effector = this.frame.hand.end_effector;

		double ex = effector.getXY()[0];
		double ey = effector.getXY()[1];

		ex -= this.x;
		ey -= this.y;

		return Math.sqrt(ex*ex + ey*ey);
	}

	//「エンドエフェクタが障害に達しているか」を判定するメソッド
	public boolean isCollision(){
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
		g.setColor(Obstacle.OBSTACLERANGECOLOR);
		g.fillOval(this.x - this.range + Hand.JOINTSIZE/2, this.y - this.range + Hand.JOINTSIZE/2, 2*this.range, 2*this.range);

		//目標位置の描画
		g.setColor(Obstacle.OBSTACLECOLOR);
		g.fillOval(x , this.y , Hand.JOINTSIZE, Hand.JOINTSIZE);

		//Graphicsクラスの破棄、描画
		g.setTransform(oldtr);
		g.dispose();
	}


}
