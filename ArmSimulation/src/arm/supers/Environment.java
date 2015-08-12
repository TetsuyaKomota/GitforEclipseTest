package arm.supers;

import java.awt.Color;
import java.awt.Graphics2D;

import arm.main.ArmFrame;
import arm.main.MyObject;

public class Environment {

	//実験環境を表すクラス。エンドエフェクタの初期位置、目標位置、障害物などの情報を持つ

	//定数
	//色
	final Color STARTCOLOR = Color.blue;
	final Color GOALCOLOR = new Color(200,200,255);

	//フィールド
	//エンドエフェクタの初期位置
	MyObject start;
	//目標位置
	MyObject goal;
	//障害物
	MyObject[] obstacles;

	//コンストラクタ
	public Environment(){
		this.start = new MyObject();
		this.start.setColor(STARTCOLOR);
		this.goal = new MyObject();
		this.goal.setColor(GOALCOLOR);
	}

	//描画
	public void draw(ArmFrame frame){
		Graphics2D g = (Graphics2D)frame.buffer.getDrawGraphics();
		//初期位置の描画
		this.start.draw(frame);
		//障害物の描画
		for(int i=0;i<this.obstacles.length;i++){
			this.obstacles[i].draw(frame);
		}
		//目標位置の描画
		this.goal.draw(frame);
		g.dispose();
	}

	//移動
	public void move(){
		//障害物の移動
		for(int i=0;i<this.obstacles.length;i++){
			this.obstacles[i].move();
		}
		//目標位置の移動
		this.goal.move();
	}

	//ゴール判定
	public boolean isGoal(ArmFrame frame){
		if(frame.getHand().end_effector.isCollision(this.goal)){
			return true;
		}
		return false;
	}

	//障害物との衝突判定
	public boolean isCollision(ArmFrame frame){
		//肢体部分に触れてるかの判定方法を考える
		return false;
	}

}
