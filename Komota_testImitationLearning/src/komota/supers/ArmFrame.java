package komota.supers;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

import komota.DMP.DMPSample1;
import soinn.SOINN;

public class ArmFrame extends JFrame{

	//実験環境を描画するフレーム

	//定数

	static final int FRAMESIZE = 1000;

	//キー状態に使用する定数
	static final int NUMBEROFKEY = 4;
	static final int KEY_SPACE = 0;
	static final int KEY_R = 1;
	static final int KEY_L = 2;
	static final int KEY_S = 3;

	//腕
	Hand hand;

	//初期位置
	Start start = null;

	//目標
	Goal goal = null;

	//障害物
	Obstacle[] obstacles = null;

	//DMP
	DMPSample1 dmp = null;

	//バッファストラテジー
	BufferStrategy buffer;

	//マウスリスナー
	MyMouseAdapter mouseadapter = null;

	//キーリスナー
	MyKeyAdapter keyadapter = null;

	//キー状態
	boolean[] key;

	//SOINN
	SOINN soinn;

	//コンストラクタ
	public ArmFrame(){
		//JFrameとしての前準備
		this.setTitle("Imitation Learning");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(FRAMESIZE,FRAMESIZE);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setVisible(true);

		this.setIgnoreRepaint(true);
		this.createBufferStrategy(2);
		this.buffer = this.getBufferStrategy();

		this.hand = new TwoDOFHand();
		this.start = new Start(100,-200,0,this);
		this.goal = new Goal(0,300,10,this);
		this.obstacles = new Obstacle[3];
		this.obstacles[0] = new Obstacle(-100,100,30,this);
		this.obstacles[1] = new Obstacle(0,100,30,this);
		this.obstacles[2] = new Obstacle(100,100,30,this);

		this.dmp = new DMPSample1(this,this.hand,this.goal);

		this.mouseadapter = new MyMouseAdapter();
		this.addMouseMotionListener(mouseadapter);

		this.keyadapter = new MyKeyAdapter();
		this.addKeyListener(keyadapter);

		this.key = new boolean[NUMBEROFKEY];

		this.initializePosition();

		//タイマータスクの実行
		Timer t = new Timer();
		t.schedule(new RenderTask(), 0,12);
	}

	//腕のセッターとゲッター
	public void setHand(Hand hand){
		this.hand = hand;
	}
	public Hand getHand(){
		return this.hand;
	}
	//目標位置のセッターとゲッター
	public void setGoal(Goal goal){
		this.goal = goal;
	}
	public Goal getGoal(){
		return this.goal;
	}

	//動かす
	public void move(){

//		this.hand.locateEnd_Effector(this.hand.end_effector.getXY()[0]-1, this.hand.end_effector.getXY()[1]-1);

		/* *************************************************************************************************** */
		//これはどこに書くかよく考える
		if(this.key[ArmFrame.KEY_SPACE] == false && this.key[ArmFrame.KEY_R] == true){
			this.dmp.dmpflag = true;
		}
		else{
			this.dmp.dmpflag = false;
		}

		/* *************************************************************************************************** */

		this.goal.move();
		for(int i=0;i<this.obstacles.length;i++){
			this.obstacles[i].move();
		}
		if(this.dmp != null){
			this.dmp.move();
		}
		this.hand.inverseKinematics();
		this.hand.fromAngletoPosition();

	}

	//コンソールに出力する
	public void print(){
//		System.out.println(this.hand.end_effector.getXY()[0] + " , "+this.hand.end_effector.getXY()[1]);
//		System.out.println("joints[0].angle:"+this.hand.joints[0].getAngle()+"  joints[1].angle:"+this.hand.joints[1].getAngle());
//		System.out.println(this.goal.distanceFromEnd_Effector() + " :"+this.goal.isGoal());
/*
		System.out.printf("Obstacle0--distance:%.4f collision:"+this.obstacles[0].isCollision()+"\n",this.obstacles[0].distanceFromEnd_Effector());
		System.out.printf("Obstacle1--distance:%.4f collision:"+this.obstacles[1].isCollision()+"\n",this.obstacles[1].distanceFromEnd_Effector());
		System.out.printf("Obstacle2--distance:%.4f collision:"+this.obstacles[2].isCollision()+"\n",this.obstacles[2].distanceFromEnd_Effector());
		System.out.printf("goal--distance:%.4f goal:"+this.goal.isGoal()+"\n",this.goal.distanceFromEnd_Effector());
*/
//		System.out.println("DMP's timer:"+this.dmp.getTimeCount());
		System.out.println("Trajectory.startINDEX:"+Trajectory.start_index+"  goalINDEX:"+Trajectory.goal_index);
	}

	//描画を行う
	public void render(){

		Graphics2D g = (Graphics2D)this.buffer.getDrawGraphics();

		//背景色での塗りつぶし
		g.setColor(Hand.BACKGROUND);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());

		//グリッドの描画。
		g.setColor(Hand.GRID);
		for(int i = 0;i<ArmFrame.FRAMESIZE / 100 ;i++){
			g.drawLine(i*100 + Hand.JOINTSIZE/2, 0 + Hand.JOINTSIZE/2, i*100 + Hand.JOINTSIZE/2, this.getHeight() + Hand.JOINTSIZE/2);
			g.drawLine(0 + Hand.JOINTSIZE/2, i*100 + Hand.JOINTSIZE/2, this.getWidth() + Hand.JOINTSIZE/2, i*100 + Hand.JOINTSIZE/2);
		}

		this.start.render(this);
		this.goal.render(this);
		for(int i=0;i<this.obstacles.length;i++){
			this.obstacles[i].render(this);
		}
		this.hand.render(this);


		/* ************************************************************************************************* */
		//マイナーチェンジ
		if(this.soinn != null && this.soinn.getNodeNum(false)!=0){
			int randomid = (int)(Math.random() * this.soinn.getNodeNum(false));
			double[] recarentsignal = this.soinn.getNode(randomid).getSignal();
			this.soinn.inputSignal(recarentsignal);
		}
		/* ************************************************************************************************* */

	}

	//SOINNを対応
	public void setSOINN(SOINN soinn){
		this.soinn = soinn;
	}

	//エンドエフェクタを初期位置に移動
	public void initializePosition(){
		this.hand.locateEnd_Effector(this.start.getXY()[0], this.start.getXY()[1]);
	}






/* 以下システム内部クラス */

	class RenderTask extends TimerTask{

		@Override
		public void run() {
			// TODO 自動生成されたメソッド・スタブ
			ArmFrame.this.move();
			ArmFrame.this.render();
			ArmFrame.this.print();
		}
	}

	class MyMouseAdapter implements MouseMotionListener{

		@Override
		public void mouseMoved(MouseEvent e){
			int xpos = e.getX() - ArmFrame.this.getWidth()/2;
			int ypos = e.getY() - ArmFrame.this.getHeight()/2;
			if(ArmFrame.this.key[ArmFrame.KEY_SPACE] == true &&(xpos*xpos+ypos*ypos<=ArmFrame.this.hand.maxrange*ArmFrame.this.hand.maxrange)){
				double[] signal = new double[2];
				signal[0] = xpos;
				signal[1] = ypos;

				/* ************************************************************************************************* */
				//マイナーチェンジ
				if(ArmFrame.this.soinn != null){
					ArmFrame.this.soinn.inputSignal(signal);
				}
				/* ************************************************************************************************* */
				ArmFrame.this.hand.locateEnd_Effector(xpos, ypos);

			}
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			// TODO 自動生成されたメソッド・スタブ

		}
	}
	class MyKeyAdapter implements KeyListener{

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO 自動生成されたメソッド・スタブ

		}

		@Override
		public void keyPressed(KeyEvent e) {
			// TODO 自動生成されたメソッド・スタブ
			if(e.getKeyCode()==KeyEvent.VK_SPACE){
				if(ArmFrame.this.key[ArmFrame.KEY_SPACE] == false){
					Trajectory.start_index = Trajectory.trajectory_index;
				}
				ArmFrame.this.key[ArmFrame.KEY_SPACE] = true;
				//スペースキーを押している間は「動作教示中」
			}
			else if(e.getKeyCode()==KeyEvent.VK_R){
				ArmFrame.this.key[ArmFrame.KEY_R] = true;
			}
			else if(e.getKeyCode()==KeyEvent.VK_L){
				ArmFrame.this.key[ArmFrame.KEY_L] = true;
				ArmFrame.this.dmp.fx.updateWeights(Trajectory.start_index, Trajectory.goal_index);
			}
			else if(e.getKeyCode()==KeyEvent.VK_S){
				ArmFrame.this.key[ArmFrame.KEY_S] = true;
				ArmFrame.this.initializePosition();
			}


		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO 自動生成されたメソッド・スタブ
/*
			for(int i=0;i<ArmFrame.NUMBEROFKEY;i++){
				ArmFrame.this.key[i] = false;
			}
*/
			if(e.getKeyCode() == KeyEvent.VK_SPACE){
				ArmFrame.this.key[ArmFrame.KEY_SPACE] = false;
				//スペースキーを離した時のインデックスが「教示終了時のインデックス」
				Trajectory.goal_index = Trajectory.trajectory_index;
			}else if(e.getKeyCode()==KeyEvent.VK_R){
				ArmFrame.this.key[ArmFrame.KEY_R] = false;
			}
			else if(e.getKeyCode()==KeyEvent.VK_L){
				ArmFrame.this.key[ArmFrame.KEY_L] = false;
			}
			else if(e.getKeyCode()==KeyEvent.VK_S){
				ArmFrame.this.key[ArmFrame.KEY_S] = false;
			}

		}

	}
}
