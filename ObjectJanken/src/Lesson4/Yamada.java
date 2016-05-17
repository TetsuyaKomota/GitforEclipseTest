package Lesson4;
/**
 * プレイヤーを継承した山田さんクラス。
 */
public class Yamada extends Player
{
	/**
	 * コンストラクタ。
	 *
	 * @param name 名前
	 */
	public Yamada(String name)
	{
		// スーパークラスのコンストラクタを呼び出す
		super(name);
		this.setStartComment("すぐに力の差をわからせてあげるわ．");
		this.setWinComment("ふふ，その程度？");
		this.setLoseComment("なかなかやるわね．");
		this.setDrawComment("まだまだこれからよ．");
		this.setWinnersComment("ふふふ，お疲れさま．リベンジならいつでも受けて立つわ．");
		this.setLosersComment("くっころせっ．．．！");
		this.setDrawersComment("引き分けね．楽しい戦いだったわ．");
	}

	/**
	 * ジャンケンの手を出す。
	 * スーパークラスの同名メソッドをオーバーライドしている。
	 *
	 * @return ジャンケンの手
	 */
	public int showHand()
	{
		// 必ずパーを出す
		return PAPER;
	}
}
