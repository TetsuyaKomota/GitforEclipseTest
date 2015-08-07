package arm.main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

public class ArmFrame extends JFrame{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		System.out.println("Hello World from dynabook!");
		System.out.println("Hello World from Prime!");

		ArmFrame frame = new ArmFrame();
	}

	//定数
	final Color BGCOLOR = new Color(245,245,245);

	//バッファストラテジ
	BufferStrategy buffer;

	//コンストラクタ
	public ArmFrame(){
		this.setTitle("ArmFrame");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(1000,700);
		this.setResizable(false);
		this.setLocationRelativeTo(null);

		this.setVisible(true);

		this.createBufferStrategy(2);
		this.setIgnoreRepaint(true);
		this.buffer = this.getBufferStrategy();

		Timer t = new Timer();
		t.schedule(new RenderTask(), 0,16);
	}


	void render(){
		System.out.println("render");
		this.drawBackGround();
		this.buffer.show();
	}

	//背景の描画。これは描画店の移動を行う前に実行する
	void drawBackGround(){
		Graphics2D g = (Graphics2D)this.buffer.getDrawGraphics();
		g.setColor(this.BGCOLOR);
		g.fillRect(0,0,this.getWidth(),this.getHeight());
		g.dispose();
	}


	//TimerTask

	class RenderTask extends TimerTask{

		@Override
		public void run() {
			// TODO 自動生成されたメソッド・スタブ
			ArmFrame.this.render();
		}

	}
}
