package arm.main;

import java.awt.Color;
import java.awt.Graphics2D;

import arm.libraries.MyVector;

public class Hand {
	//腕のモデル

	//定数
	//jointsにおいてどの関節点であるかを表す定数
	public static final int HAND_ROOT = 0;
	public static final int HAND_ELBOW = 1;
	public static final int HAND_END = 2;
	//armsにおいてどの長さであるかを表す定数
	public static final int LENGTH_ROOTtoELBOW = 0;
	public static final int LENGTH_ELBOWtoEND = 1;

	//描画色
	final Color JOINTCOLOR = Color.red;
	final Color ARMCOLOR = Color.black;


	//フィールド
	//関節点。とりあえず２自由度前提
	MyObject[] joints;
	//エンドエフェクタ。joints[HAND_END]のことだが、操作することが多いため特別に参照している
	public MyObject end_effector;
	//腕の長さ
	double[] arms;

	//コンストラクタ
	/*
	 * position:	エンドエフェクタの位置ベクトル
	 * length_RtoE:	ROOTからELBOWまでの長さ
	 * length_EtoE:	ELBOWからENDまでの長さ
	 */
	public Hand(MyVector position,double length_RtoE,double length_EtoE){
		//めいっぱい伸ばした範囲内にエンドエフェクタがないならエラー
		if(position.getLength() >length_RtoE+length_EtoE){
			System.out.println("[Hand]It is invalid input.");
		}
		else{
			this.joints = new MyObject[3];
			this.joints[HAND_ROOT] = new MyObject();
			this.joints[HAND_ELBOW] = new MyObject();
			this.joints[HAND_END] = new MyObject(position.getX(),position.getY(),0,0,0,0);
			this.end_effector = this.joints[HAND_END];
			this.arms = new double[2];
			this.arms[LENGTH_ROOTtoELBOW] = length_RtoE;
			this.arms[LENGTH_ELBOWtoEND] = length_EtoE;
			this.inverceKinematics();

		}
	}


	//逆運動学から肘関節の座標を調節
	void inverceKinematics(){

	}

	//描画
	public void draw(ArmFrame frame){

		Graphics2D g = (Graphics2D)frame.buffer.getDrawGraphics();
		g.setColor(ARMCOLOR);
		g.drawLine((int)this.joints[HAND_ROOT].getPosition().getX(), (int)this.joints[HAND_ROOT].getPosition().getY(), (int)this.joints[HAND_ELBOW].getPosition().getX(), (int)this.joints[HAND_ELBOW].getPosition().getY());
		g.drawLine((int)this.joints[HAND_END].getPosition().getX(), (int)this.joints[HAND_END].getPosition().getY(), (int)this.joints[HAND_ELBOW].getPosition().getX(), (int)this.joints[HAND_ELBOW].getPosition().getY());
		g.dispose();
		joints[HAND_ROOT].draw(frame);
		joints[HAND_ELBOW].draw(frame);
		joints[HAND_END].draw(frame);

	}
	//移動
	public void move(){
		this.end_effector.move();
		this.inverceKinematics();
	}
}
