package mahjong.main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * トランプのカードを表すクラス。
 */
public class Hai
{
	/** 萬子を表す定数 */
	public static final int MAN = 1;

	/** 索子を表す定数 */
	public static final int SOU = 2;

	/** 筒子を表す定数 */
	public static final int PIN = 3;

	/** 風牌を表す定数 */
	public static final int KAZ = 4;

	/** 三元牌を表す定数 */
	public static final int SAN = 5;

	/** スートの数 */
	public static final int SUIT_NUM = 4;

	/** 値の数 */
	public static final int HAI_NUM = 9;

	/** カードの示すスート */
	protected int suit_;

	/** カードの示す数 */
	protected int number_;

	/** 牌の画像置き場 */
	public static BufferedImage[] images = null;

	/**
	 * コンストラクタ。
	 * スートと数を指定して新しいカードのインスタンスを生成する。<br>
	 * スートはSUIT_SPADE、SUIT_DIAMOND、SUIT_CLUB、SUIT_HEARTの
	 * いずれかを指定する。
	 *
	 * @param suit スート
	 * @param number 数
	 */
	public Hai(int suit, int number)
	{
		this.suit_ = suit;
		this.number_ = number;
	}

	public static void readImage() throws IOException{
		if(Hai.images != null){
			return;
		}
		Hai.images = new BufferedImage[38];

		int idx = 0;

		for(int i=1;i<=9;i++){
			Hai.images[idx++] = ImageIO.read(new File("img/wz"+i+".png"));
		}
		for(int i=1;i<=9;i++){
			Hai.images[idx++] = ImageIO.read(new File("img/sz"+i+".png"));
		}
		for(int i=1;i<=9;i++){
			Hai.images[idx++] = ImageIO.read(new File("img/pz"+i+".png"));
		}
		for(int i=1;i<=4;i++){
			Hai.images[idx++] = ImageIO.read(new File("img/kz"+i+".png"));
		}
		for(int i=1;i<=3;i++){
			Hai.images[idx++] = ImageIO.read(new File("img/sg"+i+".png"));
		}
		Hai.images[idx++] = ImageIO.read(new File("img/aw5.png"));
		Hai.images[idx++] = ImageIO.read(new File("img/as5.png"));
		Hai.images[idx++] = ImageIO.read(new File("img/ap5.png"));
		Hai.images[idx++] = ImageIO.read(new File("img/up1.png"));
	}

	public BufferedImage getImage(){
		BufferedImage output = null;

		if(this.suit_ == Hai.MAN){
			output = Hai.images[this.number_ - 1];
		}
		else if(this.suit_ == Hai.SOU){
			output = Hai.images[8 + this.number_];
		}
		else if(this.suit_ == Hai.PIN){
			output = Hai.images[17 + this.number_];
		}
		else if(this.suit_ == Hai.KAZ){
			output = Hai.images[26 + this.number_];
		}
		else if(this.suit_ == Hai.SAN){
			output = Hai.images[30 + this.number_];
		}
		else{
			output = Hai.images[37];
		}

		return output;
	}

	/**
	 * 数を見る。
	 *
	 * @return 数
	 */
	public int getNumber()
	{
		return number_;
	}

	/**
	 * スートを見る。
	 *
	 * @return スート
	 */
	public int getSuit()
	{
		return suit_;
	}

	/**
	 * カードを文字列で表現する。<br>
	 * ObjectクラスのtoStringメソッドをオーバーライドしたメソッド。
	 *
	 * @return カードの文字表現
	 */
	public String toString()
	{
		String output = "ERROR";

		//字牌の場合，そのまま返す
		if(this.suit_ == KAZ){
			switch(this.number_){
			case 1:
				output = "東";
				break;
			case 2:
				output = "南";
				break;
			case 3:
				output = "西";
				break;
			case 4:
				output = "北";
				break;
			default:
				output = "ERROR";
			}
		}
		else if(this.suit_ == SAN){
			switch(this.number_){
			case 1:
				output = "白";
				break;
			case 2:
				output = "發";
				break;
			case 3:
				output = "中";
				break;

			}
		}
		else{
			switch(this.suit_){
			case MAN:
				output = "M"+this.number_;
				break;
			case SOU:
				output = "S"+this.number_;
				break;
			case PIN:
				output = "P"+this.number_;
				break;
			default:
				output = "ERROR";
			}
		}
		return output;
	}
}
