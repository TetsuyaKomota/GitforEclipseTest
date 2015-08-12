package komota.supers;

public class Trajectory {

	//エンドエフェクタの軌跡。入出力データもこの形式で扱う

	//軌跡の時間。
	static int trajectory_timer = 0;
	//軌跡のインデックス
	static int trajectory_index = 0;
	//教示開始時のインデックス
	static int start_index = 0;
	//教示終了時のインデックス
	static int goal_index = 0;

	//時間単位とエンドエフェクタの座標
	int time;
	double x;
	double y;

	//各関節の角度
	double[] angles;

	//コンストラクタ。軌跡は逐次的に保存と破棄を繰り返すため、毎回コンストラクタを生成してたら重くなる可能性が高い。
	//よって、インスタンス生成はHandが生成されたときに一定量事前に行っておくとする
	public Trajectory(){
		this.time = 0;
		this.x = 0;
		this.y = 0;
	}
	//座標のゲッター
	public double[] getXY(){
		double[] output = new double[2];
		output[0] = this.x;
		output[1] = this.y;
		return output;
	}

	//軌跡の書き込み
	public void update(Hand hand){
		this.angles = new double[hand.joints.length];
		for(int i=0;i<angles.length;i++){
			angles[i] = hand.joints[i].getAngle();
		}
		this.x = hand.end_effector.getXY()[0];
		this.y = hand.end_effector.getXY()[1];

		this.time = Trajectory.trajectory_timer++;
	}
}
