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
			//勝っていた場合，その時出した手をもう一度出しやすくする
			switch(this.getShowed()){
			case Player.STONE:
				this.prob[Player.STONE]+=1;
				break;
			case Player.SCISSORS:
				this.prob[Player.SCISSORS]+=1;
				break;
			case Player.PAPER:
				this.prob[Player.PAPER]+=1;
				break;
			}
		}
		else if(this.getResult() == Player.LOSE){
			//負けていた場合，その時相手が出した手に勝てる手を出しやすくする
			switch(this.getShowed()){
			case Player.STONE:
				this.prob[Player.SCISSORS]+=1;
				break;
			case Player.SCISSORS:
				this.prob[Player.PAPER]+=1;
				break;
			case Player.PAPER:
				this.prob[Player.STONE]+=1;
				break;
			}
		}
		else if(this.getResult() == Player.DRAW){
			//引き分けていた場合，その時相手が出した手に勝てる手を出しやすくする
			switch(this.getShowed()){
			case Player.STONE:
				this.prob[Player.PAPER]+=1;
				break;
			case Player.SCISSORS:
				this.prob[Player.STONE]+=1;
				break;
			case Player.PAPER:
				this.prob[Player.SCISSORS]+=1;
				break;
			}
		}
		//正規化
		double sum = this.prob[Player.STONE] + this.prob[Player.SCISSORS] + prob[Player.PAPER];
		this.prob[Player.STONE]		 /= sum;
		this.prob[Player.SCISSORS]	 /= sum;
		this.prob[Player.PAPER]		 /= sum;
		//probをもとに手を決定する
		double rand = Math.random();
		if(rand < prob[Player.STONE]){
			output = Player.STONE;
		}
		else if(rand < prob[Player.STONE] + prob[Player.SCISSORS]){
			output = Player.SCISSORS;
		}
		else{
			output = Player.PAPER;
		}
		return output;
	}

}
