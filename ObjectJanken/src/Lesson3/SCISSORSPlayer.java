package Lesson3;

public class SCISSORSPlayer extends Player{

	public SCISSORSPlayer(String name) {
		super(name);
		// TODO 自動生成されたコンストラクター・スタブ
	}
	//	チョキしか出さないプレイヤー

	@Override
	public int showHand(){
		return Player.SCISSORS;
	}


}
