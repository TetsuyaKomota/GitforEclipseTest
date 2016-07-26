package komota.gomi;

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

import komota.lib.MatFactory;
import komota.lib.MyMatrix;

public class AMFrame extends JFrame{

	public static void main(String[] args){
		AMFrame frame = new AMFrame();
	}

	BufferStrategy buffer;
	MyPanel[][] panels;
	MyMatrix memory;

	static int NUMBEROFPANELS = 8;


	//コンストラクタ
	public AMFrame(){
		this.setTitle("AMFrame");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(1000,1000);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setVisible(true);

		this.createBufferStrategy(2);
		this.buffer = this.getBufferStrategy();
		this.setIgnoreRepaint(true);

		this.panels = new MyPanel[NUMBEROFPANELS][NUMBEROFPANELS];
		this.memory = new MyMatrix(NUMBEROFPANELS*NUMBEROFPANELS);
		for(int i=0;i<this.panels.length;i++){
			for(int j=0;j<this.panels[0].length;j++){
				this.panels[i][j] = new MyPanel(i,j);
			}
		}

		Timer t = new Timer();
		t.schedule(new RenderTask(),0,16);

		this.addMouseListener(new MyMouseListener());
		this.addKeyListener(new MyKeyListener());


	}

	class MyPanel{

		//コンストラクタ
		MyPanel(int gyou,int retsu){
			this.x = retsu;
			this.y = gyou;
			this.state = -1;
		}

		int x;
		int y;
		int state;
		static final int SIZE = 95;
		static final int STARTPOSITION = 100;

		public void draw(){
			Graphics2D g = (Graphics2D)AMFrame.this.buffer.getDrawGraphics();
			if(this.state == 1){
				g.setColor(Color.black);
			}
			else{
				g.setColor(Color.white);
			}
			g.fillRect((STARTPOSITION)+(SIZE+5)*x,(STARTPOSITION)+(SIZE+5)*y,SIZE,SIZE);
			g.dispose();
		}
		boolean isClicked(int x,int y){
			if(
					x >= (STARTPOSITION)+(SIZE+5)*this.x &&
					y >= (STARTPOSITION)+(SIZE+5)*this.y &&
					x < (STARTPOSITION)+(SIZE+5)*this.x + SIZE &&
					y < (STARTPOSITION)+(SIZE+5)*this.y + SIZE
					){
				return true;
			}
			else{
				return false;
			}
		}

		void clicked(){
			System.out.println("clicked  "+this.x+","+this.y);
			this.state *= -1;
		}

	}

	void memorize(){
		MyMatrix temp = new MyMatrix(NUMBEROFPANELS*NUMBEROFPANELS);
		for(int i=0;i<this.panels.length;i++){
			for(int j=0;j<this.panels[0].length;j++){
				temp.setData(i*NUMBEROFPANELS + j, 0, this.panels[i][j].state);
			}
		}
		this.memory = this.memory.add(temp.mult(temp.trans()).sub(MatFactory.unit(NUMBEROFPANELS*NUMBEROFPANELS)));
	}
	void associate(){
		MyMatrix temp = new MyMatrix(NUMBEROFPANELS*NUMBEROFPANELS);
		for(int i=0;i<this.panels.length;i++){
			for(int j=0;j<this.panels[0].length;j++){
				temp.setData(i*NUMBEROFPANELS + j, 0, this.panels[i][j].state);
			}
		}
		temp = this.memory.mult(temp);
		for(int i=0;i<this.panels.length;i++){
			for(int j=0;j<this.panels[0].length;j++){

				if(temp.getData(i*NUMBEROFPANELS + j, 0)>=0){
					this.panels[i][j].state = 1;
				}
				else{
					this.panels[i][j].state = -1;
				}

			}
		}

	}

	class RenderTask extends TimerTask{

		@Override
		public void run() {
			// TODO 自動生成されたメソッド・スタブ
			Graphics2D g = (Graphics2D)AMFrame.this.buffer.getDrawGraphics();
			g.setColor(new Color(200,200,200));
			g.fillRect(0, 0, AMFrame.this.getWidth(), AMFrame.this.getHeight());
			g.dispose();
			for(int i=0;i<AMFrame.this.panels.length;i++){
				for(int j=0;j<AMFrame.this.panels[0].length;j++){
					AMFrame.this.panels[i][j].draw();
				}
			}
			AMFrame.this.buffer.show();
		}

	}

	class MyMouseListener implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO 自動生成されたメソッド・スタブ
			int x = e.getX();
			int y = e.getY();
			//System.out.println("MouseListener reflected  "+x+","+y);
			for(int i=0;i<AMFrame.this.panels.length;i++){
				for(int j=0;j<AMFrame.this.panels[0].length;j++){
					if(AMFrame.this.panels[i][j].isClicked(x, y) == true){
						AMFrame.this.panels[i][j].clicked();
						break;
					}
				}
			}
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

	class MyKeyListener implements KeyListener{

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO 自動生成されたメソッド・スタブ

		}

		@Override
		public void keyPressed(KeyEvent e) {
			// TODO 自動生成されたメソッド・スタブ
			switch(e.getKeyCode()){
			case KeyEvent.VK_Z:
				System.out.println("画面を初期化します");
				for(int i=0;i<NUMBEROFPANELS;i++){
					for(int j=0;j<NUMBEROFPANELS;j++){
						AMFrame.this.panels[i][j].state = -1;
					}
				}
				break;
			case KeyEvent.VK_X:
				System.out.println("記憶します");
				AMFrame.this.memorize();
				break;
			case KeyEvent.VK_C:
				System.out.println("連想します");
				AMFrame.this.associate();
				break;
			case KeyEvent.VK_D:
				System.out.println("記憶を初期化します");
				AMFrame.this.memory = new MyMatrix(NUMBEROFPANELS*NUMBEROFPANELS);
				break;
			case KeyEvent.VK_S:
				System.out.println("記憶行列を表示します");
				AMFrame.this.memory.show();
				break;
			default:
				System.out.println("そのキーに機能は存在しません");
			}

		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO 自動生成されたメソッド・スタブ

		}

	}

}
