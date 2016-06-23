package mahjong.main;

import java.util.ArrayList;

/**
 * 手札を表すクラス。
 */
public class Hand
{
	/** 手札にあるカードを保持するためのリスト */
	private ArrayList<Hai> hand_ = new ArrayList<Hai>();

	/**
	 * カードを加える。<br>
	 *
	 * @param hai 加えるカード
	 */
	public void addHai(Hai hai)
	{
		// カードをリストの最後に追加する
		hand_.add(hai);
	}

	/**
	 * カードを見る。
	 * positionは 0 から getNumberOfCards() の範囲で指定するすること。
	 *
	 * @param position カードの位置。
	 * @return position で指定された位置のカード。範囲外の場合はnullを返す。
	 */
	public Hai lookHai(int position)
	{
		Hai lookingCard = null;

		// 引数で指定された位置が妥当であるかチェックする
		if ((0 <= position) && (position < hand_.size()))
		{
			lookingCard = (Hai)hand_.get(position);
		}

		return lookingCard;
	}

	/**
	 * カードを引く。
	 * 引いたカードは手札から削除される。
	 *
	 * @param position カードの位置。
	 * @return position で指定された位置のカード。範囲外の場合はnullを返す。
	 */
	public Hai pickHai(int position)
	{
		Hai pickedCard = null;

		// 引数で指定された位置が妥当であるかチェックする
		if ((0 <= position) && (position < hand_.size()))
		{
			pickedCard = (Hai)hand_.remove(position);
		}

		return pickedCard;
	}

	/**
	 * シャッフルする。
	 */
	public void shuffle()
	{
		// 手札の枚数を取得
		int number = hand_.size();

		// カードを抜き出す位置
		int pos;

		// カードをランダムに抜き取って最後に加える動作を繰り返す
		for (int count = 0; count < number * 2; count++)
		{
			// ランダムな位置からカードを一枚抜き取る
			pos = (int) (Math.random() * number);
			Hai pickedCard = (Hai)hand_.remove(pos);

			// 抜き取ったカードを最後に加える
			hand_.add(pickedCard);
		}
	}

	/**
	 * 理牌する
	 */
	public void repai(){
		ArrayList<Hai> rehand = new ArrayList<Hai>();
		int idx = 0;
		int token = 1;
		while(token <= 9){
			while(idx < this.hand_.size()){
				if(this.hand_.get(idx).getSuit() == Hai.MAN && this.hand_.get(idx).getNumber() == token){
					rehand.add(this.hand_.remove(idx));
					idx = 0;
				}
				else{
					idx++;
				}
			}
			idx = 0;
			token++;
		}
		token = 1;
		idx = 0;
		while(token <= 9){
			while(idx < this.hand_.size()){
				if(this.hand_.get(idx).getSuit() == Hai.SOU && this.hand_.get(idx).getNumber() == token){
					rehand.add(this.hand_.remove(idx));
					idx = 0;
				}
				else{
					idx++;
				}
			}
			idx = 0;
			token++;
		}
		token = 1;
		idx = 0;
		while(token <= 9){
			while(idx < this.hand_.size()){
				if(this.hand_.get(idx).getSuit() == Hai.PIN && this.hand_.get(idx).getNumber() == token){
					rehand.add(this.hand_.remove(idx));
					idx = 0;
				}
				else{
					idx++;
				}
			}
			idx = 0;
			token++;
		}
		token = 1;
		idx = 0;
		while(token <= 4){
			while(idx < this.hand_.size()){
				if(this.hand_.get(idx).getSuit() == Hai.KAZ && this.hand_.get(idx).getNumber() == token){
					rehand.add(this.hand_.remove(idx));
					idx = 0;
				}
				else{
					idx++;
				}
			}
			idx = 0;
			token++;
		}
		token = 1;
		idx = 0;
		while(token <= 3){
			while(idx < this.hand_.size()){
				if(this.hand_.get(idx).getSuit() == Hai.SAN && this.hand_.get(idx).getNumber() == token){
					rehand.add(this.hand_.remove(idx));
					idx = 0;
				}
				else{
					idx++;
				}
			}
			idx = 0;
			token++;
		}
		this.hand_ = rehand;
	}

	public boolean isRon(Hai dahai){
		Hand virtualhand = new Hand();
		for(int i=0;i<this.hand_.size();i++){
			virtualhand.addHai(this.hand_.get(i));
		}
		virtualhand.addHai(dahai);
		virtualhand.repai();
		if(virtualhand.countShanten() < 0){
			return true;
		}
		else{
			return false;
		}
	}

	/**
	 * シャンテン数を数える
	 * @return シャンテン数
	 */
	public int countShanten(){
		int flag = 0;
		int output = 100;
		int temp = 200;

		//逃げの手牌．数え終わった牌をこっちに退避させておく
		ArrayList<Hai> esc = new ArrayList<Hai>();

		//理牌
		this.repai();

		/*
		 * 面子手 8 -
		・面子×2
		・搭子(面子と合わせて最大4つまで)×1
		・雀頭(1対子のみ)×1

		七対子 6 -
		・対子

		国士無双 13 -
		・一九字牌（1種1枚まで）
		 */

		//順子，刻子，対子，塔子のパターン
		int mentsu = this.countshuntsu(esc) + this.countKohtsu(esc);
		int toitsu = this.countToitsu(esc);
		int ryanpen = this.countRyanpenTahtsu(esc);
		int kanchan = this.countKanchanTahtsu(esc);

		while(this.hand_.size()>0){
			esc.add(this.hand_.remove(0));
		}

		this.hand_ = esc;
		this.repai();

		System.out.println("面子："+mentsu+" 対子："+toitsu+" 両面，ペンチャン塔子："+ryanpen+" カンチャン塔子："+kanchan);

		temp = 8;
		//面子＊２
		temp -= mentsu*2;
		//塔子＋面子 < 4なら塔子
		if(mentsu + ryanpen + kanchan < 4){
			temp -= ryanpen;
			temp -= kanchan;
		}
		else{
			temp -= (4-mentsu);
			//雀頭
			if(toitsu > 0){
				temp -= 1;
			}
		}
		if(temp < output){
			output = temp;
		}

		//刻子，順子，対子，塔子のパターン
		mentsu = this.countKohtsu(esc) + this.countshuntsu(esc);
		toitsu = this.countToitsu(esc);
		ryanpen = this.countRyanpenTahtsu(esc);
		kanchan = this.countKanchanTahtsu(esc);

		while(this.hand_.size()>0){
			esc.add(this.hand_.remove(0));
		}

		this.hand_ = esc;
		this.repai();

		System.out.println("面子："+mentsu+" 対子："+toitsu+" 両面，ペンチャン塔子："+ryanpen+" カンチャン塔子："+kanchan);

		temp = 8;
		//面子＊２
		temp -= mentsu*2;
		//塔子＋面子 < 4なら塔子
		if(mentsu + ryanpen + kanchan < 4){
			temp -= ryanpen;
			temp -= kanchan;
		}
		else{
			temp -= (4-mentsu);
			//雀頭
			if(toitsu > 0){
				temp -= 1;
			}
		}

		if(temp < output){
			output = temp;
		}

		//順子，刻子，塔子，対子のパターン
		mentsu =this.countshuntsu(esc) +  this.countKohtsu(esc);
		ryanpen = this.countRyanpenTahtsu(esc);
		kanchan = this.countKanchanTahtsu(esc);
		toitsu = this.countToitsu(esc);

		while(this.hand_.size()>0){
			esc.add(this.hand_.remove(0));
		}

		this.hand_ = esc;
		this.repai();

		System.out.println("面子："+mentsu+" 対子："+toitsu+" 両面，ペンチャン塔子："+ryanpen+" カンチャン塔子："+kanchan);

		temp = 8;
		//面子＊２
		temp -= mentsu*2;
		//塔子＋面子 < 4なら塔子
		if(mentsu + ryanpen + kanchan < 4){
			temp -= ryanpen;
			temp -= kanchan;
		}
		else{
			temp -= (4-mentsu);
			//雀頭
			if(toitsu > 0){
				temp -= 1;
			}
		}

		if(temp < output){
			output = temp;
		}

		//刻子，順子，塔子，対子のパターン
		mentsu = this.countKohtsu(esc) + this.countshuntsu(esc);
		ryanpen = this.countRyanpenTahtsu(esc);
		kanchan = this.countKanchanTahtsu(esc);
		toitsu = this.countToitsu(esc);

		while(this.hand_.size()>0){
			esc.add(this.hand_.remove(0));
		}

		this.hand_ = esc;
		this.repai();

		System.out.println("面子："+mentsu+" 対子："+toitsu+" 両面，ペンチャン塔子："+ryanpen+" カンチャン塔子："+kanchan);

		temp = 8;
		//面子＊２
		temp -= mentsu*2;
		//塔子＋面子 < 4なら塔子
		if(mentsu + ryanpen + kanchan < 4){
			temp -= ryanpen;
			temp -= kanchan;
		}
		else{
			temp -= (4-mentsu);
			//雀頭
			if(toitsu > 0){
				temp -= 1;
			}
		}

		if(temp < output){
			output = temp;
		}
		//対子，順子，刻子，塔子のパターン
		toitsu = this.countToitsu(esc);
		mentsu =this.countshuntsu(esc) +  this.countKohtsu(esc);
		ryanpen = this.countRyanpenTahtsu(esc);
		kanchan = this.countKanchanTahtsu(esc);

		while(this.hand_.size()>0){
			esc.add(this.hand_.remove(0));
		}

		this.hand_ = esc;
		this.repai();

		System.out.println("面子："+mentsu+" 対子："+toitsu+" 両面，ペンチャン塔子："+ryanpen+" カンチャン塔子："+kanchan);

		temp = 8;
		//面子＊２
		temp -= mentsu*2;
		//塔子＋面子 < 4なら塔子
		if(mentsu + ryanpen + kanchan < 4){
			temp -= ryanpen;
			temp -= kanchan;
		}
		else{
			temp -= (4-mentsu);
			//雀頭
			if(toitsu > 0){
				temp -= 1;
			}
		}

		if(temp < output){
			output = temp;
		}

		//対子，刻子，順子，塔子のパターン
		toitsu = this.countToitsu(esc);
		mentsu = this.countKohtsu(esc) + this.countshuntsu(esc);
		ryanpen = this.countRyanpenTahtsu(esc);
		kanchan = this.countKanchanTahtsu(esc);

		while(this.hand_.size()>0){
			esc.add(this.hand_.remove(0));
		}

		this.hand_ = esc;
		this.repai();

		System.out.println("面子："+mentsu+" 対子："+toitsu+" 両面，ペンチャン塔子："+ryanpen+" カンチャン塔子："+kanchan);

		temp = 8;
		//面子＊２
		temp -= mentsu*2;
		//塔子＋面子 < 4なら塔子
		if(mentsu + ryanpen + kanchan < 4){
			temp -= ryanpen;
			temp -= kanchan;
		}
		else{
			temp -= (4-mentsu);
			//雀頭
			if(toitsu > 0){
				temp -= 1;
			}
		}

		if(temp < output){
			output = temp;
		}


		//七対子のパターン
		temp = 6 - this.countToitsu(esc);

		while(this.hand_.size()>0){
			esc.add(this.hand_.remove(0));
		}

		this.hand_ = esc;
		this.repai();

		if(temp < output){
			flag = 1;
			output = temp;
		}


		//国士無双のパターン
		temp = this.countKokushimusou();
		if(temp < output){
			flag = 2;
			output = temp;
		}

		if(flag == 1){
			System.out.println("今,七対子"+output+"向聴です");
		}
		else if(flag == 2){
			System.out.println("今,国士無双"+output+"向聴です");
		}
		else{
			System.out.println("今,"+output+"向聴です");
		}

		return output;
	}

	//hand_から順子を取り除き，escに入れ，順子の数を返す
	private int countshuntsu(ArrayList<Hai> esc){
		int output = 0;
		int idx = 0;
		int count = 0;
		int tempsuit = -10;
		int tempnumber = -10;
		while(idx < this.hand_.size()){
			if(tempsuit == this.hand_.get(idx).getSuit()){
				if(tempnumber+1 == this.hand_.get(idx).getNumber()){
					count++;
				}
				else if(tempnumber+1 < this.hand_.get(idx).getNumber()){
					count = 0;
				}
				tempnumber = this.hand_.get(idx).getNumber();
			}else if(this.hand_.get(idx).getSuit() == Hai.KAZ ||this.hand_.get(idx).getSuit() == Hai.SAN ){
				break;
			}
			else{
				tempsuit = this.hand_.get(idx).getSuit();
				tempnumber = this.hand_.get(idx).getNumber();
				count = 0;
			}
			if(count >= 2){
				for(int j=0;j<3;j++){
					for(int i=idx-j;i>=0;i--){
						if(this.hand_.get(i).getNumber() == tempnumber-j){
							System.out.println("順子"+(j+1)+"枚目:"+this.hand_.get(i).toString());
							esc.add(this.hand_.remove(i));
							break;
						}
					}
				}
				output++;
				count = 0;
				tempnumber = -10;
				idx = 0;
			}
			else{
				idx++;
			}
		}
		return output;
	}
	//hand_から刻子を取り除き，escに入れて，刻子の数を返す
	private int countKohtsu(ArrayList<Hai> esc){
		int output = 0;
		int idx = 0;
		int count = 0;
		int tempsuit = -10;
		int tempnumber = -10;
		while(idx < this.hand_.size()){
			if(tempsuit == this.hand_.get(idx).getSuit()){
				if(tempnumber == this.hand_.get(idx).getNumber()){
					count++;
				}
				else{
					count = 0;
				}
				tempnumber = this.hand_.get(idx).getNumber();
			}
			else{
				tempsuit = this.hand_.get(idx).getSuit();
				tempnumber = this.hand_.get(idx).getNumber();
				count = 0;
			}
			if(count >= 2){
				for(int j=0;j<3;j++){
					for(int i=idx-j;i>=0;i--){
						if(this.hand_.get(i).getNumber() == tempnumber){
							System.out.println("刻子"+(j+1)+"枚目:"+this.hand_.get(i).toString());
							esc.add(this.hand_.remove(i));
							break;
						}
					}
				}
				output++;
				count = 0;
				tempnumber = -10;
				idx = 0;
			}
			else{
				idx++;
			}
		}
		return output;
	}
	//hand_から対子を取り除き，escに入れて，対子の数を返す.面子はすべて取り除かれていることを前提とする
	private int countToitsu(ArrayList<Hai> esc){
		int output = 0;
		int idx = 0;
		int count = 0;
		int tempsuit = -10;
		int tempnumber = -10;
		while(idx < this.hand_.size()){
			if(tempsuit == this.hand_.get(idx).getSuit()){
				if(tempnumber == this.hand_.get(idx).getNumber()){
					count++;
				}
				else{
					count = 0;
				}
				tempnumber = this.hand_.get(idx).getNumber();
			}
			else{
				tempsuit = this.hand_.get(idx).getSuit();
				tempnumber = this.hand_.get(idx).getNumber();
			}
			if(count >= 1){
				for(int j=0;j<2;j++){
					for(int i=idx-j;i>=0;i--){
						if(this.hand_.get(i).getNumber() == tempnumber){
							System.out.println("対子"+(j+1)+"枚目:"+this.hand_.get(i).toString());
							esc.add(this.hand_.remove(i));
							break;
						}
					}
				}
				output++;
				count = 0;
				tempnumber = -10;
				idx = 0;
			}
			else{
				idx++;
			}
		}
		return output;
	}
	//hand_から両面，ペンチャン塔子を取り除き，escに入れて，両面塔，ペンチャン子の数を返す
	private int countRyanpenTahtsu(ArrayList<Hai> esc){
		int output = 0;
		int idx = 0;
		int count = 0;
		int tempsuit = -10;
		int tempnumber = -10;
		while(idx < this.hand_.size()){
			if(tempsuit == this.hand_.get(idx).getSuit()){
				if(tempnumber+1 == this.hand_.get(idx).getNumber()){
					count++;
				}
				else if(tempnumber+1 < this.hand_.get(idx).getNumber()){
					count = 0;
				}
				tempnumber = this.hand_.get(idx).getNumber();
			}else if(this.hand_.get(idx).getSuit() == Hai.KAZ ||this.hand_.get(idx).getSuit() == Hai.SAN ){
				break;
			}
			else{
				tempsuit = this.hand_.get(idx).getSuit();
				tempnumber = this.hand_.get(idx).getNumber();
			}
			if(count >= 1){
				for(int j=0;j<2;j++){
					for(int i=idx-j;i>=0;i--){
						if(this.hand_.get(i).getNumber() == tempnumber-j){
							System.out.println("両面，ペンチャン"+(j+1)+"枚目:"+this.hand_.get(i).toString());
							esc.add(this.hand_.remove(i));
							break;
						}
					}
				}
				output++;
				count = 0;
				tempnumber = -10;
				idx = 0;
			}
			else{
				idx++;
			}
		}
		return output;
	}
	//hand_からカンチャン塔子を取り除き，escに入れて，カンチャン塔子の数を返す
	private int countKanchanTahtsu(ArrayList<Hai> esc){
		int output = 0;
		int idx = 0;
		int count = 0;
		int tempsuit = -10;
		int tempnumber = -10;
		while(idx < this.hand_.size()){
			if(tempsuit == this.hand_.get(idx).getSuit()){
				if(tempnumber+2 == this.hand_.get(idx).getNumber()){
					count++;
				}
				else if(tempnumber+2 < this.hand_.get(idx).getNumber()){
					count = 0;
				}
				tempnumber = this.hand_.get(idx).getNumber();
			}else if(this.hand_.get(idx).getSuit() == Hai.KAZ ||this.hand_.get(idx).getSuit() == Hai.SAN ){
				break;
			}
			else{
				tempsuit = this.hand_.get(idx).getSuit();
				tempnumber = this.hand_.get(idx).getNumber();
			}
			if(count >= 1){
				for(int j=0;j<2;j++){
					for(int i=idx-j;i>=0;i--){
						if(this.hand_.get(i).getNumber() == tempnumber-2*j){
							System.out.println("カンチャン"+(j+1)+"枚目:"+this.hand_.get(i).toString());
							esc.add(this.hand_.remove(i));
							break;
						}
					}
				}
				output++;
				count = 0;
				tempnumber = -10;
				idx = 0;
			}
			else{
				idx++;
			}
		}
		return output;
	}

	//hand_が国士無双何向聴か返す
	private int countKokushimusou(){
		int output = 13;

		ArrayList<Hai> esc = new ArrayList<Hai>();

		for(int i=0;i<this.hand_.size();i++){
			if(this.hand_.get(i).getSuit() == Hai.MAN && this.hand_.get(i).getNumber() == 1){
				output--;
				esc.add(this.hand_.remove(i));
				break;
			}
		}
		for(int i=0;i<this.hand_.size();i++){
			if(this.hand_.get(i).getSuit() == Hai.MAN && this.hand_.get(i).getNumber() == 9){
				output--;
				esc.add(this.hand_.remove(i));
				break;
			}
		}
		for(int i=0;i<this.hand_.size();i++){
			if(this.hand_.get(i).getSuit() == Hai.SOU && this.hand_.get(i).getNumber() == 1){
				output--;
				esc.add(this.hand_.remove(i));
				break;
			}
		}
		for(int i=0;i<this.hand_.size();i++){
			if(this.hand_.get(i).getSuit() == Hai.SOU && this.hand_.get(i).getNumber() == 9){
				output--;
				esc.add(this.hand_.remove(i));
				break;
			}
		}
		for(int i=0;i<this.hand_.size();i++){
			if(this.hand_.get(i).getSuit() == Hai.PIN && this.hand_.get(i).getNumber() == 1){
				output--;
				esc.add(this.hand_.remove(i));
				break;
			}
		}
		for(int i=0;i<this.hand_.size();i++){
			if(this.hand_.get(i).getSuit() == Hai.PIN && this.hand_.get(i).getNumber() == 9){
				output--;
				esc.add(this.hand_.remove(i));
				break;
			}
		}
		for(int i=0;i<this.hand_.size();i++){
			if(this.hand_.get(i).getSuit() == Hai.KAZ && this.hand_.get(i).getNumber() == 1){
				output--;
				esc.add(this.hand_.remove(i));
				break;
			}
		}
		for(int i=0;i<this.hand_.size();i++){
			if(this.hand_.get(i).getSuit() == Hai.KAZ && this.hand_.get(i).getNumber() == 2){
				output--;
				esc.add(this.hand_.remove(i));
				break;
			}
		}
		for(int i=0;i<this.hand_.size();i++){
			if(this.hand_.get(i).getSuit() == Hai.KAZ && this.hand_.get(i).getNumber() == 3){
				output--;
				esc.add(this.hand_.remove(i));
				break;
			}
		}
		for(int i=0;i<this.hand_.size();i++){
			if(this.hand_.get(i).getSuit() == Hai.KAZ && this.hand_.get(i).getNumber() == 4){
				output--;
				esc.add(this.hand_.remove(i));
				break;
			}
		}
		for(int i=0;i<this.hand_.size();i++){
			if(this.hand_.get(i).getSuit() == Hai.SAN && this.hand_.get(i).getNumber() == 1){
				output--;
				esc.add(this.hand_.remove(i));
				break;
			}
		}
		for(int i=0;i<this.hand_.size();i++){
			if(this.hand_.get(i).getSuit() == Hai.SAN && this.hand_.get(i).getNumber() == 2){
				output--;
				esc.add(this.hand_.remove(i));
				break;
			}
		}
		for(int i=0;i<this.hand_.size();i++){
			if(this.hand_.get(i).getSuit() == Hai.SAN && this.hand_.get(i).getNumber() == 3){
				output--;
				esc.add(this.hand_.remove(i));
				break;
			}
		}
		for(int i=0;i<this.hand_.size();i++){
			if(
					(this.hand_.get(i).getSuit() == Hai.MAN && this.hand_.get(i).getNumber() == 1)
					||(this.hand_.get(i).getSuit() == Hai.MAN && this.hand_.get(i).getNumber() == 9)
					||(this.hand_.get(i).getSuit() == Hai.SOU && this.hand_.get(i).getNumber() == 1)
					||(this.hand_.get(i).getSuit() == Hai.SOU && this.hand_.get(i).getNumber() == 9)
					||(this.hand_.get(i).getSuit() == Hai.PIN && this.hand_.get(i).getNumber() == 1)
					||(this.hand_.get(i).getSuit() == Hai.PIN && this.hand_.get(i).getNumber() == 9)
					||(this.hand_.get(i).getSuit() == Hai.KAZ && this.hand_.get(i).getNumber() == 1)
					||(this.hand_.get(i).getSuit() == Hai.KAZ && this.hand_.get(i).getNumber() == 2)
					||(this.hand_.get(i).getSuit() == Hai.KAZ && this.hand_.get(i).getNumber() == 3)
					||(this.hand_.get(i).getSuit() == Hai.KAZ && this.hand_.get(i).getNumber() == 4)
					||(this.hand_.get(i).getSuit() == Hai.SAN && this.hand_.get(i).getNumber() == 1)
					||(this.hand_.get(i).getSuit() == Hai.SAN && this.hand_.get(i).getNumber() == 2)
					||(this.hand_.get(i).getSuit() == Hai.SAN && this.hand_.get(i).getNumber() == 3)
			){
				output--;
				esc.add(this.hand_.remove(i));
				break;
			}
		}
		while(this.hand_.size()>0){
			esc.add(this.hand_.remove(0));
		}
		this.hand_ = esc;
		this.repai();
		return output;
	}



	/**
	 * 枚数を数える。
	 *
	 * @return 手札にあるカードの枚数
	 */
	public int getNumberOfHais()
	{
		return hand_.size();
	}

	/**
	 * 手札にあるカードを文字列で表現する。
	 * ObjectクラスのtoStringメソッドをオーバーライドしたメソッド。
	 *
	 * @return 手札にあるカードの文字列表現
	 */
	public String toString()
	{
		StringBuffer string = new StringBuffer();

		int size = hand_.size();
		if (size > 0)
		{
			for (int index = 0; index < size; index++)
			{
				Hai card = (Hai)hand_.get(index);
				string.append(card);
				string.append(" ");
			}
		}

		return string.toString();
	}
}
