package komota.supers;

public class Joint {

	//関節クラス。通常は根本、肘関節、末端の３つが生成される

	//関節番号。0:根本　1:肘関節　2:末端
	private int ID;

	//関節の位置情報
	private double[] position;

	//関節の角度情報。各アームと、x軸方向のなす角
	private double angle;

	//接続するアーム。[0]:根本側　[1]:末端側
	private Arm[] arms;

	//コンストラクタ
	public Joint(int ID,double x,double y,double angle){
		this.ID = ID;
		this.position = new double[2];
		this.position[0] = x;
		this.position[1] = y;
		this.angle = angle;
		this.arms = new Arm[2];
		arms[0] = null;
		arms[1] = null;
	}

	//各セッター、ゲッター
	public Joint setID(int ID){
		this.ID = ID;
		return this;
	}
	public int getID(){
		return this.ID;
	}
	public Joint setXY(double x,double y){
		this.position[0] = x;
		this.position[1] = y;
		return this;
	}
	public double[] getXY(){
		return this.position;
	}
	public Joint setAngle(double angle){
		this.angle = angle%(2*Math.PI);
		return this;
	}
	public double getAngle(){
		return this.angle;
	}
	public Joint setArms(Arm a1,Arm a2){
		this.arms[0] = a1;
		this.arms[1] = a2;
		return this;
	}
	public Arm[] getArms(){
		return this.arms;
	}




}
