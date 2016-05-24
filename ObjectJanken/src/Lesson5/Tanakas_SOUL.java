package Lesson5;

public class Tanakas_SOUL implements Serifu,Tactics{

	//故田中さんの戦略とセリフを宿したクラス

	@Override
	public int readTactics() {
		// TODO 自動生成されたメソッド・スタブ
		//生前の田中さんはグーしか出さなかったなぁ
		return Player.STONE;
	}

	@Override
	public String start() {
		// TODO 自動生成されたメソッド・スタブ
		//田中さんはじゃんけんを始めるときはいっつも礼儀正しく挨拶から入っていました
		return "よろしくお願いしますっ！";
	}

	@Override
	public String win() {
		// TODO 自動生成されたメソッド・スタブ
		//田中さんはじゃんけんに勝った時はいつもとてもうれしそうに笑っていましたね…
		return "やった！勝ちました！";
	}

	@Override
	public String draw() {
		// TODO 自動生成されたメソッド・スタブ
		//どんな時でも，その時その時を目いっぱい楽しんでいたんですよ，田中さんって人は．
		return "うはぁ，緊張しますね！";
	}

	@Override
	public String lose() {
		// TODO 自動生成されたメソッド・スタブ
		//何度負けてもめげない健気さが彼女の持ち味だったなぁ…
		return "うぅ...つ，次こそはっ！";
	}

	@Override
	public String winner() {
		// TODO 自動生成されたメソッド・スタブ
		//勝負に勝った時の田中さんのうれしそうな顔は，今でも忘れません．
		return "やったやった！私の勝ちですね！ありがとうございました！";
	}

	@Override
	public String drawer() {
		// TODO 自動生成されたメソッド・スタブ
		//できることなら，もう一度会いたいなぁ…．まぁ，そんなこと言ってもしょうがないんですけど．
		return "とってもいい勝負でしたね！もういっかい，やりますか？";
	}

	@Override
	public String loser() {
		// TODO 自動生成されたメソッド・スタブ
		//……っ，ごめんなさい，はは，なんだろう，涙が……
		return "ふぇぇ，負けちゃいました...でも，とっても楽しかったですっ！";
	}

}
