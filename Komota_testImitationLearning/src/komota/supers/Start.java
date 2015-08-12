package komota.supers;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class Start {
	//初期地点。座標と有効範囲を持つ

	//描画に使う色の定数
	static final Color STARTCOLOR = new Color(20,20,220);

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
	public Start(int x,int y,int range,ArmFrame frame){
		this.x = x;
		this.y = y;
		this.range = range;
		this.frame = frame;
	}
	//XYのセッターとゲッター
	public Start setXY(double[] xy){
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

	//描画処理
	public void render(ArmFrame frame){
		Graphics2D g = (Graphics2D)frame.buffer.getDrawGraphics();

		//描画位置を、中心を(0,0)に平行移動
		AffineTransform oldtr = g.getTransform();
		g.translate(ArmFrame.FRAMESIZE/2, ArmFrame.FRAMESIZE/2);

		//初期位置の描画
		g.setColor(Start.STARTCOLOR);
		g.fillOval((int)this.x , (int)this.y , Hand.JOINTSIZE, Hand.JOINTSIZE);

		//Graphicsクラスの破棄、描画
		g.setTransform(oldtr);
		g.dispose();
	}


}
