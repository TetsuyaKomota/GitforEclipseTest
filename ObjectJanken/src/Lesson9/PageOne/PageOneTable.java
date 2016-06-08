package Lesson9.PageOne;

import Lesson9.trump.Card;
import Lesson9.trump.Table;

/**
 * ページワンのテーブルを表すクラス。
 */

public class PageOneTable implements Table
{
	/** テーブルの一番上にあるカード */
	private Card card_;

	/**
	 * カードを置く。
	 *
	 * @param Card[] 配列の0個目の要素だけ有効
	 */
	public void putCard(Card[] cards)
	{
		//配列で受け取るので，最初の要素だけを記憶する
		card_=cards[0];
	}

	/**
	 * カードを見る。カードは1枚のみ
	 * @return テーブルのカードを2次元配列で返す．
	 *
	 * 有効なのは要素[0][0]のみ
	 */
	public Card[][] getCards()
	{
		//カードを返すための1×1の2次元配列を用意
		Card[][] table = new Card[1][1];
		//配列の要素[0][0]にカードを記録して返す
		table[0][0]=card_;
		return table;
	}

	/**
	 * テーブルを文字列で表現する。 捨てられたカードの文字列となる
	 *
	 * @return 捨てられたカードの文字列表現
	 */
	public String toString() {
		if (card_ !=null)
			return card_.toString();
		else
			return "null";
	}


	/**
	 * テストドライバ
	 * @param args
	 */
	public static void main(String args[]) {
		// テーブルの生成
		Table table = new PageOneTable();

		//カード1枚を作成してテーブルに置く
		Card[] c = new Card[1];
		c[0] = new Card(Card.SUIT_CLUB, 1);
		table.putCard(c);

		//置いたカードを見る
		Card[][] t = table.getCards();
		System.out.println("Result = " + t[0][0]);

		//カード1枚を作成してテーブルに置く
		c[0] = new Card(Card.SUIT_CLUB, 2);
		table.putCard(c);

		//置いたカードを見る
		t = table.getCards();
		System.out.println("Result = " + t[0][0]);
	}

}
