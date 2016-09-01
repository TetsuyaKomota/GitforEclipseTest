package mahjong.rules;

import mahjong.main.Hai;
import mahjong.main.Rule;

public class Rule_Shanten extends Rule{


	//コンストラクタ
	public Rule_Shanten(){
		for(int i=0;i<13;i++){
			this.virtualhand.addHai(new Hai(Hai.MAN,1));
		}
	}

	@Override
	public String dahai(){

		int tempshanten = 13;
		int tempidx = -1;
		for(int i=0;i<this.getPlayer().getHand().getNumberOfHais();i++){
			this.readTehai();
			this.virtualhand.pickHai(i);
			if(this.virtualhand.countShanten() < tempshanten){
				tempshanten = this.virtualhand.countShanten();
				tempidx = i;
			}
		}

		return this.getPlayer().getHand().lookHai(tempidx).toString();

	}

}
