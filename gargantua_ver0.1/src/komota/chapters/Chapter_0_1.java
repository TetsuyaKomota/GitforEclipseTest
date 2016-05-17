package komota.chapters;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import komota.main.MyFrame;
import komota.main.MyKeyListener;
import komota.modes.Manzai_Chara;
import komota.modes.Manzai_Mode;

public class Chapter_0_1 extends Manzai_Mode{

	//登場人物
	Manzai_Chara hitoA;

	public Chapter_0_1(MyFrame frame, MyKeyListener key) {
		super(frame,key);

		this.hitoA = new Manzai_Chara("hitoA");

		// TODO 自動生成されたコンストラクター・スタブ
		try {
			this.background = ImageIO.read(new File("images/manzai/background/kaeri.jpg"));
			this.chara[3] = hitoA;
			this.column = ImageIO.read(new File("images/manzai/effect/hakkou1.png"));
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		this.messages[0][0] = "ひかり";
		this.messages[0][1] = "これはテストメッセージです．全角３０文字で一行一杯らしいので";
		this.messages[0][2] = "ここで改行されているとちょうどいいはずです．";
		this.messages[0][3] = "123456789012345678901234567890123456789012345678901234567890";



	}

	@Override
	public void callNext() {
		// TODO 自動生成されたメソッド・スタブ

	}

}
