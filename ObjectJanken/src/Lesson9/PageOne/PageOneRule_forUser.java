package Lesson9.PageOne;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import Lesson9.trump.Card;
import Lesson9.trump.Hand;
import Lesson9.trump.Table;

public class PageOneRule_forUser extends PageOneRule{

	//ユーザーインターフェースを介して提出カードを選択できるルールクラス

	@Override
	public Card[] findCandidate(Hand hand, Table table){
		Card[] output = null;

		//手札の枚数とテーブルのカードを確認
		int numofcards = hand.getNumberOfCards();
		Card tabletop = table.getCards()[0][0];

		//提出可能カード群を作成
		Card[] candidate = new Card[numofcards];
		int count = 0;
		for(int index = 0;index<numofcards;index++){
			//テーブルにカードがない場合，すべての手札をcandidateに保存する
			if(tabletop == null){
				candidate[count] = hand.lookCard(index);
				count++;
			}
			//テーブルのカードとスートか数字が同じカードはcandidateに保存する
			else if(
					hand.lookCard(index).getNumber() == tabletop.getNumber()
					|| hand.lookCard(index).getSuit() == tabletop.getSuit()
					){
				candidate[count] = hand.lookCard(index);
				count++;
			}
		}

		//出せるカードがない場合はnullを返す
		if(count == 0){
			return output;
		}

		while(true){
			//提出可能カード群がcandidateに保存されたはずなので，一度表示する
			System.out.print("現在手札から出せるカード:");
			for(int i=0;i<count;i++){
				System.out.print("  "+candidate[i]);
			}
			System.out.println();
			//提出するカードを選択させる

			System.out.println("提出するカードを入力してください");
		    String sin = null;
		    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		    try{
		      sin = br.readLine();
		    }catch(IOException e){
		      System.out.println("不正な入力:" + e.getMessage());
		    }

		    //入力を解析する
		    if(sin.length() != 2){
		    	System.out.println("不正な入力:文字長");
		    	continue;
		    }

		    String suit_str = sin.substring(0, 1);
		    String num_str = sin.substring(1,2);

		    int suit = -1;
		    int num = -1;

		    switch(suit_str){
		    case "D":
		    	suit = Card.SUIT_DIAMOND;
		    	break;
		    case "S":
		    	suit = Card.SUIT_SPADE;
		    	break;
		    case "H":
		    	suit = Card.SUIT_HEART;
		    	break;
		    case "C":
		    	suit = Card.SUIT_CLUB;
		    	break;
		    default :
		    	System.out.println("不正な入力:スート");
		    	continue;
		    }
		    switch(num_str){
		    case "A":
		    	num = 1;
		    	break;
		    case "2":
		    	num = 2;
		    	break;
		    case "3":
		    	num = 3;
		    	break;
		    case "4":
		    	num = 4;
		    	break;
		    case "5":
		    	num = 5;
		    	break;
		    case "6":
		    	num = 6;
		    	break;
		    case "7":
		    	num = 7;
		    	break;
		    case "8":
		    	num = 8;
		    	break;
		    case "9":
		    	num = 9;
		    	break;
		    case "T":
		    	num = 10;
		    	break;
		    case "J":
		    	num = 11;
		    	break;
		    case "Q":
		    	num = 12;
		    	break;
		    case "K":
		    	num = 13;
		    	break;
		    default :
		    	System.out.println("不正な入力:数字");
		    	continue;
		    }
		    //入力されたカードがcandidateに存在すればそれをoutputに入れて終了
		    for(int i=0;i<count;i++){
		    	if(candidate[i].getSuit() == suit && candidate[i].getNumber() == num){
		    		output = new Card[1];
		    		output[0] = candidate[i];
		    		//提出するカードを手札から引き抜く
		    		for(int j=0;j<numofcards;j++){
		    			if(
		    					hand.lookCard(j).getSuit() == candidate[i].getSuit()
		    					&& hand.lookCard(j).getNumber() == candidate[i].getNumber()
		    					){
		    				hand.pickCard(j);
		    				break;
		    			}
		    		}
		    		break;
		    	}
		    }
		    //outputが用意されていれば終了
		    if(output != null){
		    	break;
		    }
		}

		return output;
	}

}
