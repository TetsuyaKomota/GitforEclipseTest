package komota.modes;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import komota.libs.Statics;

public class Manzai_Chara {

	//フィールド
	//キャラ名
	private String name;

	//キャラ画像サイズ
	private int size;

	//現在の表情
	private int currentface;

	//表情ごとの画像
	private BufferedImage[] faces;

	//コンストラクタ
	public Manzai_Chara(String name){
		this.name = name;
		this.currentface = Statics.MANZAI_CHARA_FACE_NORMAL;
		this.faces = new BufferedImage[Statics.MANZAI_CHARA_NUMBEROFFACE];

		try {
			for(int i=0;i<Statics.MANZAI_CHARA_NUMBEROFFACE;i++){
				this.faces[i] 	= ImageIO.read(new File("images/manzai/chara/"+this.name+"/img_"+i+".png"));
			}
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

		this.size = this.faces[0].getWidth();
	}

	//キャラ名のゲッター
	public String getName(){
		return this.name;
	}
	//画像サイズのゲッター
	public int getSize(){
		return this.size;
	}
	//表情のセッター
	public void setFace(int input){
		this.currentface = input;
	}
	//表情のゲッター
	public BufferedImage getFace(){
		return this.faces[this.currentface];
	}

}
