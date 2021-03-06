package komota.lifegame;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

public abstract class LifeFrame extends JFrame{

	public static void main(String[] args){
		//LifeFrame frame = new LifeFrame();
		System.out.println("このクラスを継承してruleを具象化して実行してください");
	}


	//バッファストラテジ
	public BufferStrategy buffer;

	//タイマークラス
	Timer t;

	//描画フラグ
	boolean renderflag = true;

	//エージェント
	Agent[][] agents;

	//エージェント種類累計
	int[] amounts;


	//コンストラクタ
	public LifeFrame(){
		this.setTitle("LifeGame");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.setSize(1000,1000);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setVisible(true);

		this.createBufferStrategy(2);
		this.buffer = this.getBufferStrategy();
		this.setIgnoreRepaint(true);

		this.addKeyListener(new MyKeyListener());
		this.addMouseListener(new MyMouseListener());

		this.initialize();

		this.t = new Timer();
		t.schedule(new RenderTask(), 0,1000/Statics.FRAMERATE);

	}
	/* ************************************************************************************************************************************************* */
	/* ************************************************************************************************************************************************* */
	/* ************************************************************************************************************************************************* */
	//スペースキーを押したときに起こす処理
public abstract void startWORLD();
//ライフゲーム．ここをオーバーライドしてゲームを作ろう
public abstract int rule(int[] neighbors,int status);
/* ************************************************************************************************************************************************* */
/* ************************************************************************************************************************************************* */
/* ************************************************************************************************************************************************* */


	//Gを押した時に起こす処理
	public void pushGoal(){
	}

	//初期化
	public void initialize(){
		//エージェント作製
		this.agents = new Agent[Statics.NUMBEROFAGENTS][Statics.NUMBEROFAGENTS];
		for(int i=0;i<Statics.NUMBEROFAGENTS;i++){
			for(int j=0;j<Statics.NUMBEROFAGENTS;j++){
				this.agents[i][j] = new Agent(i,j);
			}
		}
		//累計
		this.amounts = new int[Statics.NUMBEROFSTATUS];
	}



	//計算結果などをresult.txtに出力
	public void printResult(String out){
	}
	public void printlnResult(String out){
	}
	//パネルの描画以外の特別な描画を行いたい場合はここをオーバーライド
	public void draw(){
	}

	//各サブクラスでキーに反応する機能を追加したい場合にオーバーライドする
	public void functionPlugin1(){
		System.out.println("No Function in This Key.");
	}
	public void functionPlugin2(){
		System.out.println("No Function in This Key.");
	}
	public void functionPlugin3(){
		System.out.println("No Function in This Key.");
	}
	public void functionPlugin4(){
		System.out.println("No Function in This Key.");
	}
	public void functionPlugin5(){
		System.out.println("No Function in This Key.");
	}
	public void functionPlugin6(){
		System.out.println("No Function in This Key.");
	}
	public void functionPlugin7(){
		System.out.println("No Function in This Key.");
	}
	public void functionPlugin8(){
		System.out.println("No Function in This Key.");
	}
	public void functionPlugin9(){
		System.out.println("No Function in This Key.");
	}
	public void functionPluginQ(){
		System.out.println("No Function in This Key.");
	}
	public void functionPluginW(){
		System.out.println("No Function in This Key.");
	}
	public void functionPluginE(){
		System.out.println("No Function in This Key.");
	}
	public void functionPluginR(){
		System.out.println("No Function in This Key.");
	}
	public void functionPluginT(){
		System.out.println("No Function in This Key.");
	}
	public void functionPluginY(){
		System.out.println("No Function in This Key.");
	}


	//タイマータスク
	class RenderTask extends TimerTask{

		//背景色
		Color backgrond;
		//描画時に使用する，次状態を格納する配列
		int[][] nexts;

		//コンストラクタ
		RenderTask(){
			this.backgrond = new Color(240,240,240);
			this.nexts = new int[Statics.NUMBEROFAGENTS][Statics.NUMBEROFAGENTS];
		}

		@Override
		public void run() {

			if(LifeFrame.this.renderflag == false){
				return;
			}
			//累計取り直し
			for(int i=0;i<LifeFrame.this.amounts.length;i++){
				LifeFrame.this.amounts[i] = 0;
			}
			Graphics2D g = (Graphics2D)LifeFrame.this.buffer.getDrawGraphics();
			//背景の描画
			g.setColor(this.backgrond);
			g.fillRect(0, 0, LifeFrame.this.getWidth(), LifeFrame.this.getHeight());

			//エージェントの描画と認識
			for(int i=0;i<Statics.NUMBEROFAGENTS;i++){
				for(int j=0;j<Statics.NUMBEROFAGENTS;j++){
					LifeFrame.this.amounts[LifeFrame.this.agents[i][j].status]++;
					LifeFrame.this.agents[i][j].draw();
					LifeFrame.this.agents[i][j].recognize();
				}
			}
			//エージェントの移動
			for(int i=0;i<Statics.NUMBEROFAGENTS;i++){
				for(int j=0;j<Statics.NUMBEROFAGENTS;j++){
					nexts[i][j] = LifeFrame.this.agents[i][j].move();
				}
			}
			for(int i=0;i<Statics.NUMBEROFAGENTS;i++){
				for(int j=0;j<Statics.NUMBEROFAGENTS;j++){
					LifeFrame.this.agents[i][j].status = nexts[i][j];
				}
			}
			//累計出力
			g.setColor(Color.black);
			g.drawString("State : amount", 50, 50);
			for(int i=0;i<Statics.NUMBEROFSTATUS;i++){
				g.drawString(i+" : "+LifeFrame.this.amounts[i], 50, 70+20*i);
			}
			g.dispose();
			LifeFrame.this.draw();
			LifeFrame.this.buffer.show();
		}
	}

	//エージェントクラス
	class Agent{
		//座標[0]:行 [1]:列
		int[] position;
		//状態
		int status;
		//周囲の状態
		int[] neighbors;
		//コンストラクタ
		Agent(int gyou, int retsu, int status){
			this.position = new int[2];
			this.position[0] = gyou;
			this.position[1] = retsu;
			this.status = status;
			this.neighbors = new int[Statics.NUMBEROFSTATUS];
		}
		Agent(int gyou, int retsu){
			this(gyou,retsu,0);
		}
		//描画
		void draw(){
			Graphics2D g = (Graphics2D)LifeFrame.this.buffer.getDrawGraphics();
			g.translate((LifeFrame.this.getWidth() - Statics.NUMBEROFAGENTS*Statics.SIZEOFAGENT)/2, (LifeFrame.this.getHeight() - Statics.NUMBEROFAGENTS*Statics.SIZEOFAGENT)/2);
			switch(this.status){
			case 0:
				g.setColor(Color.white);
				break;
			case 1:
				g.setColor(Color.red);
				break;
			case 2:
				g.setColor(Color.blue);
				break;
			case 3:
				g.setColor(Color.orange);
				break;
			case 4:
				g.setColor(Color.green);
				break;
			case 5:
				g.setColor(Color.yellow);
				break;
			case 6:
				g.setColor(Color.pink);
				break;
			case 7:
				g.setColor(Color.lightGray);
				break;
			case 8:
				g.setColor(Color.darkGray);
				break;
			case 9:
				g.setColor(Color.black);
				break;
			}
			g.fillRect(this.position[1]*Statics.SIZEOFAGENT,this.position[0]*Statics.SIZEOFAGENT,Statics.SIZEOFAGENT,Statics.SIZEOFAGENT);
			g.dispose();
		}
		//状態認識
		void recognize(){
			int gyou = -1;
			int retsu= -1;
			for(int i=0;i<this.neighbors.length;i++){
				this.neighbors[i] = 0;
			}
			for(int i=-1;i<2;i++){
				for(int j=-1;j<2;j++){
					gyou = this.position[0]+i;
					retsu = this.position[1]+j;
					if(gyou<0){
						gyou += Statics.NUMBEROFAGENTS;
					}
					if(gyou>Statics.NUMBEROFAGENTS-1){
						gyou -= Statics.NUMBEROFAGENTS;
					}
					if(retsu<0){
						retsu += Statics.NUMBEROFAGENTS;
					}
					if(retsu>Statics.NUMBEROFAGENTS-1){
						retsu -= Statics.NUMBEROFAGENTS;
					}
					if(i!=0||j!=0){
						this.neighbors[LifeFrame.this.agents[gyou][retsu].status]++;
					}
				}
			}
		}
		//状態変化
		int move(){
			return LifeFrame.this.rule(this.neighbors,this.status);
		}
	}


	//キーリスナー
	class MyKeyListener implements KeyListener{

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO 自動生成されたメソッド・スタブ

		}

		@Override
		public void keyPressed(KeyEvent e) {
			// TODO 自動生成されたメソッド・スタブ
			if(e.getKeyCode() == KeyEvent.VK_SPACE){
				LifeFrame.this.renderflag = false;
				//描画がストップするまでの時間を置く
				try {
					Thread.sleep(1000/Statics.FRAMERATE);
				} catch (InterruptedException e1) {
					// TODO 自動生成された catch ブロック
					e1.printStackTrace();
				}
				LifeFrame.this.startWORLD();
				LifeFrame.this.renderflag = true;
			}
			else if(e.getKeyCode() == KeyEvent.VK_G){
				LifeFrame.this.pushGoal();
			}
			else if(e.getKeyCode() == KeyEvent.VK_S){
				LifeFrame.this.renderflag = (!LifeFrame.this.renderflag);
			}
			else if(e.getKeyCode() == KeyEvent.VK_X){
			}
			else if(e.getKeyCode() == KeyEvent.VK_1){
				LifeFrame.this.functionPlugin1();
			}
			else if(e.getKeyCode() == KeyEvent.VK_2){
				LifeFrame.this.functionPlugin2();
			}
			else if(e.getKeyCode() == KeyEvent.VK_3){
				LifeFrame.this.functionPlugin3();
			}
			else if(e.getKeyCode() == KeyEvent.VK_4){
				LifeFrame.this.functionPlugin4();
			}
			else if(e.getKeyCode() == KeyEvent.VK_5){
				LifeFrame.this.functionPlugin5();
			}
			else if(e.getKeyCode() == KeyEvent.VK_6){
				LifeFrame.this.functionPlugin6();
			}
			else if(e.getKeyCode() == KeyEvent.VK_7){
				LifeFrame.this.functionPlugin7();
			}
			else if(e.getKeyCode() == KeyEvent.VK_8){
				LifeFrame.this.functionPlugin8();
			}
			else if(e.getKeyCode() == KeyEvent.VK_9){
				LifeFrame.this.functionPlugin9();
			}
			else if(e.getKeyCode() == KeyEvent.VK_Q){
				LifeFrame.this.functionPluginQ();
			}
			else if(e.getKeyCode() == KeyEvent.VK_W){
				LifeFrame.this.functionPluginW();
			}
			else if(e.getKeyCode() == KeyEvent.VK_E){
				LifeFrame.this.functionPluginE();
			}
			else if(e.getKeyCode() == KeyEvent.VK_R){
				LifeFrame.this.functionPluginR();
			}
			else if(e.getKeyCode() == KeyEvent.VK_T){
				LifeFrame.this.functionPluginT();
			}
			else if(e.getKeyCode() == KeyEvent.VK_Y){
				LifeFrame.this.functionPluginY();
			}
			else{
				System.out.println("[LifeFrame.MyKeyListener]keyPressed:This key isn't used.");
			}


		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO 自動生成されたメソッド・スタブ

		}

	}

	//マウスリスナー
	class MyMouseListener implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO 自動生成されたメソッド・スタブ

		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO 自動生成されたメソッド・スタブ

		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO 自動生成されたメソッド・スタブ

		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO 自動生成されたメソッド・スタブ

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO 自動生成されたメソッド・スタブ

		}

	}
}
