package komota.supers;

public class Arm {

	//腕クラス。通常は根本-肘関節と肘関節-末端の２つが生成される

	//腕番号。0:根本-肘関節　1:肘関節-末端
	private int ID;

	//長さ
	private double length;

	//両端の関節。関節番号が低い順であること前提
	private Joint[] joints;

	//コンストラクタ。長さを決め打ちする版
	public Arm(int ID,Joint j1,Joint j2,double length){
		this.ID = ID;
		this.joints = new Joint[2];
		joints[0] = j1;
		joints[1] = j2;
		this.length = length;
	}

	//各セッターとゲッター
	public Arm setID(int ID){
		this.ID = ID;
		return this;
	}
	public int getID(){
		return this.ID;
	}
	public double getLength(){
		return this.length;
	}
	public Arm setJoint(Joint j1,Joint j2){
		this.joints[0] = j1;
		this.joints[1] = j2;
		return this;
	}
	public Joint[] getJoint(){
		return this.joints;
	}


}
