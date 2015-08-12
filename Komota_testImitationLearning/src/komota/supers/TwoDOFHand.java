package komota.supers;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class TwoDOFHand extends Hand{

	public TwoDOFHand() {
		super(3);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	public void render(ArmFrame frame){
		//Graphicsクラスの取得
		Graphics2D g = (Graphics2D)frame.buffer.getDrawGraphics();


		//描画位置を、中心を(0,0)に平行移動
		AffineTransform oldtr = g.getTransform();
		g.translate(ArmFrame.FRAMESIZE/2, ArmFrame.FRAMESIZE/2);

		//可動範囲の描画
		g.setColor(new Color(200,200,200,50));
		int range = (int)(this.maxrange*2);
		g.fillOval(Hand.JOINTSIZE/2-range/2,Hand.JOINTSIZE/2-range/2,range,range);


		//軌跡の描画
		//現時点の軌跡を獲得する
		this.trajectory[Trajectory.trajectory_index].update(this);
		if(++Trajectory.trajectory_index >= this.trajectory.length){
			Trajectory.trajectory_index = 0;
		}
		g.setColor(Hand.TRAJECTORY);
		for(int i=0;i<this.trajectory.length;i++){
			g.fillOval((int)(this.trajectory[i].x), (int)(this.trajectory[i].y), Hand.JOINTSIZE, Hand.JOINTSIZE);
		}

		//アームの描画
		g.setColor(Hand.ARMCOLOR);

		Arm temparm = this.arms[0];

		int[] temparmpos = new int[4];
		temparmpos[0] = (int)temparm.getJoint()[0].getXY()[0];
		temparmpos[1] = (int)temparm.getJoint()[0].getXY()[1];
		temparmpos[2] = (int)temparm.getJoint()[1].getXY()[0];
		temparmpos[3] = (int)temparm.getJoint()[1].getXY()[1];

		g.drawLine(temparmpos[0] + Hand.JOINTSIZE/2,temparmpos[1] + Hand.JOINTSIZE/2,temparmpos[2] + Hand.JOINTSIZE/2,temparmpos[3] + Hand.JOINTSIZE/2);

		temparm = this.arms[1];

		temparmpos = new int[4];
		temparmpos[0] = (int)temparm.getJoint()[0].getXY()[0];
		temparmpos[1] = (int)temparm.getJoint()[0].getXY()[1];
		temparmpos[2] = (int)temparm.getJoint()[1].getXY()[0];
		temparmpos[3] = (int)temparm.getJoint()[1].getXY()[1];

		g.drawLine(temparmpos[0] + Hand.JOINTSIZE/2,temparmpos[1] + Hand.JOINTSIZE/2,temparmpos[2] + Hand.JOINTSIZE/2,temparmpos[3] + Hand.JOINTSIZE/2);

		//関節の描画
		g.setColor(Hand.JOINTCOLOR);
		Joint tempjoint = this.joints[0];
		g.fillOval((int)tempjoint.getXY()[0], (int)tempjoint.getXY()[1],  Hand.JOINTSIZE,  Hand.JOINTSIZE);
		tempjoint = this.joints[1];
		g.fillOval((int)tempjoint.getXY()[0], (int)tempjoint.getXY()[1],  Hand.JOINTSIZE,  Hand.JOINTSIZE);
		tempjoint = this.joints[2];
		g.fillOval((int)tempjoint.getXY()[0], (int)tempjoint.getXY()[1],  Hand.JOINTSIZE,  Hand.JOINTSIZE);

		//Graphicsクラスの破棄、描画
		g.setTransform(oldtr);
		g.dispose();
		frame.buffer.show();
	}

	@Override
	public void inverseKinematics(){
		//http://so-zou.jp/robot/tech/kinematics/inverse-kinematics.htm から引用

		double alpha = 0;
		double beta = 0;

		double x = this.end_effector.getXY()[0];
		double y = this.end_effector.getXY()[1];

		double l1 = this.arms[0].getLength();
		double l2 = this.arms[1].getLength();

		double temp;

		double theta = 0;

		//αの計算
		temp = 0;
		temp = -(x*x + y*y) + l1*l1 + l2*l2;
		temp /= 2*l1*l2;

		alpha = Math.acos(temp);

		//βの計算
		temp = 0;
		temp = l1*l1 - l2*l2 + (x*x + y*y);
		temp /= 2*l1*Math.sqrt(x*x + y*y);

		beta = Math.acos(temp);

		//根本の角度を計算
/*
		if(this.end_effector.getXY()[0]>=0){
			theta = Math.atan(y/x) - beta;
			this.joints[0].setAngle(theta);
		}
		else{
*/
			theta = Math.atan(y/x) - beta;
			if(this.end_effector.getXY()[0]<0){
				theta+=Math.PI;
			}
			this.joints[0].setAngle(theta);
/*
		}
		//肘関節の角度を計算
		if(this.end_effector.getXY()[0]>=0){
			theta = Math.PI - alpha;
			this.joints[1].setAngle(theta);
		}
		else{
*/
			theta = Math.PI - alpha;
			this.joints[1].setAngle(theta);
/*
		}
*/
	}

}
