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

public class LifeFrame extends JFrame{

	public static void main(String[] args){
		LifeFrame frame = new LifeFrame();
	}


	//バッファストラテジ
	public BufferStrategy buffer;

	//タイマークラス
	Timer t;

	//エージェント
	Agent[][] agents;

	//エージェントの量
	static final int NUMBEROFAGENTS = 500;

	//エージェントのサイズ
	static final int SIZEOFAGENT = 1;

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
		t.schedule(new RenderTask(), 0,50);

	}
	/* ************************************************************************************************************************************************* */
	/* ************************************************************************************************************************************************* */
	/* ************************************************************************************************************************************************* */
	//スペースキーを押したときに起こす処理
public void pushSPACE(){
	//エージェントをランダムに召喚
	for(int i=0;i<NUMBEROFAGENTS;i++){
		for(int j=0;j<NUMBEROFAGENTS;j++){
			this.agents[i][j].status = (int)(Math.random()*2 - 0.5);
		}
	}
	//生命の泉を召喚
	this.agents[(int)(NUMBEROFAGENTS*Math.random())][(int)(NUMBEROFAGENTS*Math.random())].status = 6;
}
//ライフゲーム．ここをオーバーライドしてゲームを作ろう
public int gameofLIFE(int[] neighbors,int status){
	int output = status;
	//単純なライフゲーム
	int count = 0;
	for(int i=0;i<neighbors.length;i++){
		if(neighbors[i] == 1){
			count++;
		}
	}
	if(status == 1 && (count < 2 || count > 3)){
		if(Math.random()>0.02){
			output = 0;
		}
	}
	else if(status == 0 && (count == 3)){
		output = 1;
	}
	//生命の泉
	int mother = 0;
	for(int i=0;i<neighbors.length;i++){
		if(neighbors[i] == 6){
			mother++;
		}
	}
	if(status == 6 && count > 5){
		output = 8;
	}
	if(status == 1 && mother >= 3){
		output = 6;
	}
	if(status == 0 && Math.random()*mother > 0.5){
		output = 1;
	}
	if(status == 0 && mother == 1 && Math.random() > 0.9){
		output = 6;
	}
	//腐食した泉
	int lostmother = 0;
	for(int i=0;i<neighbors.length;i++){
		if(neighbors[i] == 8){
			lostmother++;
		}
	}
	if(status == 6 && lostmother > 0 && Math.random()*lostmother > 0.9){
		output = 8;
	}
	if(status == 8 && mother < 2){
		output = 0;
	}
	return output;
}
/* ************************************************************************************************************************************************* */
/* ************************************************************************************************************************************************* */
/* ************************************************************************************************************************************************* */


	//Gを押した時に起こす処理
	public void pushGoal(){
	}

	//初期化
	public void initialize(){
		//エージェント作製
		this.agents = new Agent[NUMBEROFAGENTS][NUMBEROFAGENTS];
		for(int i=0;i<NUMBEROFAGENTS;i++){
			for(int j=0;j<NUMBEROFAGENTS;j++){
				this.agents[i][j] = new Agent(i,j);
			}
		}
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

		@Override
		public void run() {
			// TODO 自動生成されたメソッド・スタブ
			Graphics2D g = (Graphics2D)LifeFrame.this.buffer.getDrawGraphics();
			//背景の描画
			g.setColor(new Color(240,240,240));
			g.fillRect(0, 0, LifeFrame.this.getWidth(), LifeFrame.this.getHeight());

			//エージェントの描画
			for(int i=0;i<NUMBEROFAGENTS;i++){
				for(int j=0;j<NUMBEROFAGENTS;j++){
					LifeFrame.this.agents[i][j].draw();
				}
			}
			//エージェントの移動
			int[][] nexts = new int[NUMBEROFAGENTS][NUMBEROFAGENTS];
			for(int i=0;i<NUMBEROFAGENTS;i++){
				for(int j=0;j<NUMBEROFAGENTS;j++){
					nexts[i][j] = LifeFrame.this.agents[i][j].move();
				}
			}
			for(int i=0;i<NUMBEROFAGENTS;i++){
				for(int j=0;j<NUMBEROFAGENTS;j++){
					LifeFrame.this.agents[i][j].status = nexts[i][j];
				}
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
		//コンストラクタ
		Agent(int gyou, int retsu, int status){
			this.position = new int[2];
			this.position[0] = gyou;
			this.position[1] = retsu;
			this.status = status;
		}
		Agent(int gyou, int retsu){
			this(gyou,retsu,0);
		}
		//描画
		void draw(){
			Graphics2D g = (Graphics2D)LifeFrame.this.buffer.getDrawGraphics();
			g.translate((LifeFrame.this.getWidth() - NUMBEROFAGENTS*SIZEOFAGENT)/2, (LifeFrame.this.getHeight() - NUMBEROFAGENTS*SIZEOFAGENT)/2);
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
			g.fillRect(this.position[1]*SIZEOFAGENT,this.position[0]*SIZEOFAGENT,SIZEOFAGENT,SIZEOFAGENT);
			g.dispose();
		}
		//状態変化のルール
		//ここをいろいろ改造して遊んでみて
		int move(){
			//周囲の状態を確認
			/*
			 *  0 1 2
			 *  3[4]5
			 *  6 7 8
			 */
			int[] neighbors = new int[9];

			for(int i=0;i<3;i++){
				for(int j=0;j<3;j++){
					if(	this.position[0]+i-1<0
						||this.position[1]+j-1<0
						||this.position[0] + i - 1 > NUMBEROFAGENTS-1
						||this.position[1] + j - 1 > NUMBEROFAGENTS-1){
						neighbors[3*i + j] = 0;
					}
					else{
						neighbors[3*i + j] = LifeFrame.this.agents[this.position[0]+i-1][this.position[1]+j-1].status;
					}
				}
			}
			//真ん中はダミーなので-1にしておく
			neighbors[4] = -1;

			//周囲取れてるか出力
/*
			System.out.print("[LifeFrame:Agent]move:neighbors("+this.position[0]+","+this.position[1]+"):");
			for(int i=0;i<neighbors.length;i++){
				System.out.print(" "+neighbors[i]);
			}
			System.out.println();
*/
			return LifeFrame.this.gameofLIFE(neighbors, this.status);
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
				LifeFrame.this.pushSPACE();
			}
			else if(e.getKeyCode() == KeyEvent.VK_G){
				LifeFrame.this.pushGoal();
			}
			else if(e.getKeyCode() == KeyEvent.VK_Z){
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
