package komota.supers;

import java.awt.Color;


public class Hand {

	//関節とアームを持った「腕」クラス
	//描画や関節状態の変更などの基本的メソッドはこのクラスが持つ

	//定数
	//エンドエフェクタ側のことをFORWARD、根本側のことをBACKWARDとする
	public static final int FORWARD = 1;
	public static final int BACKWARD = 0;

	//軌跡の記憶数
	final int NUMBEROFTRAJECTORY = 1000;


	//描画に使う色の定数
		static final Color BACKGROUND 	= new Color(245,245,245);
		static final Color ARMCOLOR		= Color.darkGray;
		static final Color JOINTCOLOR	= Color.black;
		static final Color GRID			= Color.lightGray;
		static final Color TRAJECTORY	= new Color(245,200,245);

			static final int JOINTSIZE = 10;



	//関節とアームの集合
	//根本側の方が小さいインデックス
	Joint[] joints = null;
	Arm[] arms = null;

	//エンドエフェクタ。インデックス最大のjoints
	public Joint end_effector = null;

	//エンドエフェクタの可動半径。
	double maxrange = 0;

	//軌跡
	Trajectory[] trajectory;

	//コンストラクタ
	public Hand(int numberofjoints){
		//引数に与えた関節数で腕を生成する
		this.joints = new Joint[numberofjoints];
		for(int i=0;i<numberofjoints;i++){
			joints[i] = new Joint(i,0,0,0);
		}
		this.arms = new Arm[numberofjoints-1];
		for(int i=0;i<numberofjoints-1;i++){
			arms[i] = new Arm(i,this.joints[i],this.joints[i+1],200);
			this.maxrange += arms[i].getLength();
		}
		for(int i=0;i<numberofjoints;i++){
			//関節にアームを対応付け、同時に関節の座標を初期化
			if(i==0){
				this.joints[i].setArms(null, this.arms[0]).setXY(0, 0);
			}
			else if(i == numberofjoints-1){
				this.joints[i].setArms(this.arms[numberofjoints-2], null).setXY(this.joints[i-1].getXY()[0]+this.joints[i].getArms()[BACKWARD].getLength(), 0);
				this.end_effector = this.joints[i];
			}
			else{
				this.joints[i].setArms(this.arms[i-1], this.arms[i]).setXY(this.joints[i-1].getXY()[0]+this.joints[i].getArms()[BACKWARD].getLength(), 0);
			}
		}
		//軌跡のひな形を生成する
		this.trajectory = new Trajectory[NUMBEROFTRAJECTORY];
		for(int i=0;i<NUMBEROFTRAJECTORY;i++){
			this.trajectory[i] = new Trajectory();
		}
	}

	//軌跡のゲッター
	public Trajectory[] getTrajectory(){
		return this.trajectory;
	}



	//描画を行う
	public void render(ArmFrame frame){
	}

	//エンドエフェクタの座標をもとに各関節の角度を計算
	public void inverseKinematics(){
	}

	//各関節の角度から、各関節の座標を計算
	public void fromAngletoPosition(){
		double tempangle = this.joints[0].getAngle();
		for(int i=0;i<this.joints.length;i++){
			if(i!=0){
				this.joints[i].setXY(this.joints[i-1].getXY()[0] + this.joints[i].getArms()[BACKWARD].getLength()*Math.cos(tempangle), this.joints[i-1].getXY()[1] + this.joints[i].getArms()[BACKWARD].getLength()*Math.sin(tempangle));
				tempangle+=this.joints[i].getAngle();
				tempangle%=2*Math.PI;
			}
		}
	}

	//エンドエフェクタを指定した座標に位置させる
	public void locateEnd_Effector(double x,double y){
		if(x*x + y*y <= this.maxrange*this.maxrange){
			this.end_effector.setXY(x, y);
		}
		//範囲外が指定された場合、線分と範囲円の交点に位置させるが、このelseは削除してもいいかも
/*
		else{
			double tempx = x;
			double tempy = y;
			tempx /= x*x + y*y;
			tempy /= x*x + y*y;
			tempx *= this.maxrange;
			tempy *= this.maxrange;
			this.end_effector.setXY(tempx, tempy);
		}
*/
	}

}
