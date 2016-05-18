package Lesson3;

public class STONEPlayer extends Player{

	public STONEPlayer(String name) {
		super(name);
		// TODO 自動生成されたコンストラクター・スタブ
	}
	//グーしか出さないプレイヤー

	@Override
	public int showHand(){
		return Player.STONE;
	}

}
