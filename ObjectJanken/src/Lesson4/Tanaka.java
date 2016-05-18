package Lesson4;

public class Tanaka extends Player{

	//過去の手とその勝敗によって重みを付けた確率をもとに次の手を決定する田中さん

	/** 各手を出す確率 */
	private double[] prob;

	public Tanaka(String name) {
		super(name);
		// TODO 自動生成されたコンストラクター・スタブ
		this.setStartComment("よろしくお願いしますっ！");
		this.setWinComment("やった！勝ちました！");
		this.setLoseComment("うぅ...つ，次こそはっ！");
		this.setDrawComment("うはぁ，緊張しますね！");
		this.setWinnersComment("やったやった！私の勝ちですね！ありがとうございました！");
		this.setLosersComment("ふぇぇ，負けちゃいました...でも，とっても楽しかったですっ！");
		this.setDrawersComment("とってもいい勝負でしたね！もういっかい，やりますか？");

		this.prob = new double[3];
		this.prob[0] = (double)1/3;
		this.prob[1] = (double)1/3;
		this.prob[2] = (double)1/3;

	}

	@Override
	public int showHand(){
		int output = -1;

		//直前の手と結果から確率を更新する
		if(this.getResult() == Player.WIN){

		}

		return output;
	}

}
