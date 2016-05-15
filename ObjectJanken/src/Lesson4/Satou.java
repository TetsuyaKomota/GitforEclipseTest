package Lesson4;

public class Satou extends Player{

	public Satou(String name) {
		super(name);
		// TODO 自動生成されたコンストラクター・スタブ
		this.setStartComment("そんなことより野球しようぜ！");
		this.setWinComment("カバの汗って赤いって知ってた？");
		this.setLoseComment("たくあんって100ボルトの電圧をかけると光るんだってよ！");
		this.setDrawComment("消しゴムを凍らせると爆発するぞ！");
		this.setWinnersComment("メロンパンって日本発祥なんだよ！");
		this.setLosersComment("ちんこ");
		this.setDrawersComment("うんこ");
	}

	@Override
	public int showHand(){
		int output = Player.NOTHING;
		//直前の勝敗によって出す手を決める
		switch(this.getResult()){
		case Player.WIN:
			//直前に勝利している場合，同じ手を出す
			System.out.println(this.getName()+"(さっき勝ってるしな．同じ手でいいか)");
			output = this.getShowed();
			break;
		case Player.LOSE:
			//直前に敗北している場合，ランダムに手を出す
			System.out.println(this.getName()+"(さっき負けたしな．適当に出すか)");
			output = (int)(3*Math.random());
			break;
		case Player.DRAW:
			//直前に引き分けている場合，直前に出した手に勝てる手を出す
			System.out.println(this.getName()+"(さっき引き分けたしな．あれに勝てる手にしよう)");
			output = (this.getShowed()+2)%3;
			break;
			//それ以外の場合はランダムに手を出す
		case Player.NOTHING:
			System.out.println(this.getName()+"(最初はとりあえず適当に出すか)");
			output = (int)(3*Math.random());
		}
		return output;
	}



}
