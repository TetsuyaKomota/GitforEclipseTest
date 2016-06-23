package mahjong.main;

public class Yaku extends Hand{

	//手牌を与えると幡数を返す
	public int countHan(Hand hand){
		int output = 0;

		for(int i=0;i<hand.getNumberOfHais();i++){
			this.addHai(hand.lookHai(i));
		}

		//九連宝燈九面待ち
		//大四喜
		//四暗刻単騎待ち

		//天和
		//地和
		//清老頭
		//九連宝燈
		//緑一色
		//小四喜
		//字一色
		//大三元
		//四暗刻

		//清一色
		//清一色食い下がり

		//混一色
		//混一色食い下がり

		//純全帯
		//純全帯食い下がり

		//二盃口

		//小三元

		//三暗刻
		//三色同刻
		//対々和

		//三色同順
		//三色同順食い下がり

		//全帯
		//全帯食い下がり

		//混老頭

		//一気通貫
		//一気通貫食い下がり

		//ダブル立直

		//河底撈魚
		//海底撈月
		//嶺上開花
		//役牌
		//一盃口
		//タンヤオ
		//平和
		//門前ツモ
		//立直
		//一発
		//ドラ

		//七対子
		//国士無双
		//国士無双十三面待ち

		return output;
	}

}
