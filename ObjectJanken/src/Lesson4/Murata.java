package Lesson4;
/**
 * プレイヤーを継承した村田さんクラス。
 */
public class Murata extends Player
{
	/**
	 * コンストラクタ。
	 *
	 * @param name 名前
	 */
	public Murata(String name)
	{
		// スーパークラスのコンストラクタを呼び出す
		super(name);
		this.setStartComment("勝つのはこの俺様だぁ！");
		this.setWinComment("よっしゃ！勝ったぜ！");
		this.setLoseComment("生きてるのがつらい");
		this.setDrawComment("ちっ，あいこか！");
		this.setWinnersComment("ふん，しょせんこの俺様の敵ではなかったようだな！");
		this.setLosersComment("馬鹿なっ！この俺様が敗北しただとっ！？");
		this.setDrawersComment("この俺様と引き分けるとはなかなかやるな！");
	}

	/**
	 * ジャンケンの手を出す。
	 * スーパークラスの同名メソッドをオーバーライドしている。
	 *
	 * @return ジャンケンの手
	 */
	public int showHand()
	{
		// 必ずグーを出す
		return STONE;
	}
}
