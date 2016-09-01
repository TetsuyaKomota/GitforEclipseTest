package komota.chapters;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import komota.libs.Statics;
import komota.main.MyFrame;
import komota.main.MyKeyListener;
import komota.modes.Manzai_Chara;
import komota.modes.Manzai_Mode;

public class Chapter_0_0 extends Manzai_Mode{


	//登場人物
	Manzai_Chara hitoA;
	Manzai_Chara hitoB;
	Manzai_Chara hitoC;
	Manzai_Chara hitoD;

	public Chapter_0_0(MyFrame frame, MyKeyListener key) {
		super(frame,key);
		// TODO 自動生成されたコンストラクター・スタブ

		this.hitoA = new Manzai_Chara("hitoA");
		this.hitoB = new Manzai_Chara("hitoA");
		this.hitoC = new Manzai_Chara("hitoA");
		this.hitoD = new Manzai_Chara("hitoA");

		hitoB.setFace(Statics.MANZAI_CHARA_FACE_HAPPY);
		hitoC.setFace(Statics.MANZAI_CHARA_FACE_ANGRY);
		hitoD.setFace(Statics.MANZAI_CHARA_FACE_SAD);

		// TODO 自動生成されたコンストラクター・スタブ
		try {
			this.background = ImageIO.read(new File("images/manzai/background/aut_3011.jpg"));
			this.chara[0] = hitoA;
			this.chara[1] = hitoB;
			this.chara[2] = hitoC;
			this.chara[3] = hitoD;
			this.column = ImageIO.read(new File("images/manzai/effect/hakkou1.png"));
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		this.messages[0][0] = "ひかり";
		this.messages[0][1] = "これはテストメッセージです．全角３０文字で一行一杯らしいので";
		this.messages[0][2] = "ここで改行されているとちょうどいいはずです．";
		this.messages[0][3] = "123456789012345678901234567890123456789012345678901234567890";

		this.messages[1][0] = "ひかり";
		this.messages[1][1] = "これが二言目のテキストです．スペースキーを押してこの状態に";
		this.messages[1][2] = "なったら正常です．もう一度スペースキーを押すと背景が変わり";
		this.messages[1][3] = "ます";


	}

	@Override
	public void callNext(){
		frame.setCurrentChapter(new Chapter_0_1(this.frame,this.key));
	}
}
