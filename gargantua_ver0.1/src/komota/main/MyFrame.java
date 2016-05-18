package komota.main;

import java.awt.image.BufferStrategy;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

import komota.libs.Statics;
import komota.supers.Mode;

public class MyFrame extends JFrame{

	//バッファストラテジ
	private BufferStrategy buffer;

	//現在のモード
	Mode currentchapter;

	MyFrame(MyKeyListener key){
		this.setTitle("ガルガンチュア v0.1");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//枠のサイズを変更。6,28はそれぞれ「左右の枠の幅の和」「上下の枠の幅の和」
		this.setSize(Statics.WINDOW_WIDTH, Statics.WINDOW_HEIGHT);
		//キー入力インターフェースを装着
		this.addKeyListener(key);
		this.setResizable(false);
		this.setVisible(true);

		//バッファの作成
		this.setIgnoreRepaint(true);
		this.createBufferStrategy(2);
		this.buffer = this.getBufferStrategy();

		//タイマータスクを起動
		Timer t = new Timer();
		t.schedule(new RenderTask(), 0,16);
	}

	//バッファのゲッター
	public BufferStrategy getBuffer(){
		return this.buffer;
	}

	//モードのセッター
	public void setCurrentChapter(Mode input){
		this.currentchapter = input;
	}




	class RenderTask extends TimerTask{

		@Override
		public void run() {
			// TODO 自動生成されたメソッド・スタブ
/*
			Graphics2D g = (Graphics2D)MyFrame.this.buffer.getDrawGraphics();
			g.fillRect(0, 0, Statics.FRAME_WIDTH, Statics.FRAME_HEIGHT);
			g.dispose();
			MyFrame.this.buffer.show();
*/
			if(MyFrame.this.currentchapter != null){
				MyFrame.this.currentchapter.run();
			}
		}

	}

}
