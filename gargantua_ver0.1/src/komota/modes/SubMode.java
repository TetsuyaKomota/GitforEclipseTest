package komota.modes;

import java.awt.Color;
import java.awt.Graphics2D;

import komota.libs.Statics;
import komota.main.MyFrame;
import komota.main.MyKeyListener;
import komota.supers.Mode;

public class SubMode extends Mode{

	public SubMode(MyFrame frame, MyKeyListener key) {
		super(frame,key);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	public void run() {
		// TODO 自動生成されたメソッド・スタブ
		Graphics2D g = (Graphics2D)this.frame.getBuffer().getDrawGraphics();
		g.fillRect(0, 0, Statics.WINDOW_WIDTH, Statics.WINDOW_HEIGHT);
		g.setColor(Color.white);
		g.fillOval(200, 200, 100, 100);
		g.dispose();
		this.frame.getBuffer().show();

	}

}
