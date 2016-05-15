package Lesson3;

public class PAPERPlayer extends Player{

	public PAPERPlayer(String name) {
		super(name);
		// TODO 自動生成されたコンストラクター・スタブ
	}
	//パーしか出さないプレイヤー

	@Override
	public int showHand(){
		return Player.PAPER;
	}


}
