package komota.modes;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import komota.libs.Statics;
import komota.main.MyFrame;
import komota.main.MyKeyListener;
import komota.supers.Mode;

public abstract class Manzai_Mode extends Mode{

	//背景
	protected BufferedImage background;

	//人の配置．4か所限定
	protected Manzai_Chara[] chara;

	//人の描画間隔．画像サイズを考慮して等間隔になるようにする
	private int chara_interval;

	//セリフ欄
	protected BufferedImage column;

	//セリフセット（4行）
	protected String[][] messages;
	//現在のセリフ番号
	protected int currentamessage;


	//コンストラクタ
	public Manzai_Mode(MyFrame frame,MyKeyListener key) {
		super(frame,key);
		this.chara = new Manzai_Chara[4];
		this.messages = new String[300][4];
	}

	//チャプターの終了時の処理．オーバーライドする
	public abstract void callNext();

	@Override
	public void run() {
		// TODO 自動生成されたメソッド・スタブ
		Graphics2D g = (Graphics2D)this.frame.getBuffer().getDrawGraphics();
		g.drawImage(background, 0, 0, null);
		for(int i=0;i<this.chara.length;i++){
			if(this.chara[i] != null){
				g.drawImage(chara[i].getFace(), Statics.MANZAI_CHARA_INTERVAL+Statics.FRAME_WIDTH+(250+Statics.MANZAI_CHARA_INTERVAL)*i, 100, null);
			}
		}
		g.drawImage(column, 0, 430, null);
		g.setColor(Color.white);
		Font font = new Font("ＭＳ 明朝", Font.BOLD, 32);
	    g.setFont(font);
		g.drawString(this.messages[this.currentamessage][0], 50, 500);
		g.drawString(this.messages[this.currentamessage][1], 50, 540);
		g.drawString(this.messages[this.currentamessage][2], 50, 580);
		g.drawString(this.messages[this.currentamessage][3], 50, 620);
		g.dispose();
		this.frame.getBuffer().show();

		if(this.key.getKeyStatus(Statics.KEY_SPACE) == true){
			this.key.setKeyStatus(Statics.KEY_SPACE, false);
			if(this.messages[this.currentamessage+1][0] == null){
				this.callNext();
			}
			else{
				this.currentamessage++;
			}
		}

	}

}
