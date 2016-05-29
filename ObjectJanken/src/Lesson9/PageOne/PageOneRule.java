package Lesson9.PageOne;

import Lesson9.trump.Card;
import Lesson9.trump.Hand;
import Lesson9.trump.Rule;
import Lesson9.trump.Table;

/**
 * ページワン用ルールクラス。
 */
public class PageOneRule implements Rule
{
	/**
	 * テーブルに置けるカードを探す。
	 *
	 * @param hand 手札
	 * @param table テーブル
	 * @return 見つかったカードの組み合わせ。見つからなかった場合はnullを返す。
	 */
	public Card[] findCandidate(Hand hand, Table table)
	{
		// テーブルに置けるカードの候補 最初は無し
		Card[] candidate = null;

		//tableのカードを見る
		//table.getCardsは配列なので，要素0,0を取り出す
		Card[][] cards = table.getCards();
		Card tableCard = cards[0][0];

		//table=nullであればテーブルにカードが無いので好きなカードを出せる
		if (tableCard == null) {
			candidate = new Card[1];
			candidate[0] = hand.pickCard(0);
		}
		else {
			// 手札にあるカードを1枚ずつチェックして、テーブルに置けるか調べる
			int numberOfHand = hand.getNumberOfCards();
			for (int position = 0; position < numberOfHand; position++) {
				// 手札にあるカードを見る
				Card lookingCard = hand.lookCard(position);

				//スートか数字が同じであるか？
				if ((lookingCard.getSuit() == tableCard.getSuit())
						|| (lookingCard.getNumber() == tableCard.getNumber())) {
					// 手札からカードを引いて候補とする
					candidate = new Card[1];
					candidate[0] = hand.pickCard(position);
					break;
				}
			}
		}
		return candidate;
	}
}
