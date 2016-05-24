package Lesson5;

public class MeanedRandomTactics implements Tactics{

	//それぞれを出す確率を記憶する
	private double showed_STONE;
	private double showed_SCISSORS;
	private double showed_PAPER;

	//コンストラクタ
	public MeanedRandomTactics(){
		this.showed_STONE = (double)5/6;
		this.showed_SCISSORS = (double)1/12;
		this.showed_PAPER = (double)1/12;
	}

	@Override
	public int readTactics() {
		// TODO 自動生成されたメソッド・スタブ
		int output = 0;
		double rand = Math.random();
		//現在の確率に従って出す手を決める
		if(rand < this.showed_STONE){
			output = Player.STONE;
		}
		else if(rand < this.showed_STONE+this.showed_SCISSORS){
			output = Player.SCISSORS;
		}
		else{
			output = Player.PAPER;
		}
		//今回出す手の確率重みを相対的に下げる
		switch(output){
		case Player.STONE:
			this.showed_SCISSORS	+=1;
			this.showed_PAPER		+=1;
			break;
		case Player.SCISSORS:
			this.showed_STONE		+=1;
			this.showed_PAPER		+=1;
			break;
		case Player.PAPER:
			this.showed_STONE		+=1;
			this.showed_SCISSORS	+=1;
			break;
		}
		//正規化
		double sum = this.showed_STONE+this.showed_SCISSORS+this.showed_PAPER;
		this.showed_STONE		/=sum;
		this.showed_SCISSORS	/=sum;
		this.showed_PAPER		/=sum;

		return output;
	}

}
