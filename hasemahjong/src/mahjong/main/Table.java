package mahjong.main;

import java.util.ArrayList;

/**
 * テーブルを表すインターフェース。
 */
public class Table
{
	//一人一人にテーブルを作ろう
	ArrayList<Hai> kawa;

	//コンストラクタ
	public Table(){
		this.kawa = new ArrayList<Hai>();
	}

	/**
	 * カードを置く。
	 * テーブルに置かれたカードをどのように扱うかは、サブクラスで実装する。
	 *
	 * @param card カード
	 */
	public void putCard(Hai card){
		this.kawa.add(card);
	}

	/**
	 * カードを見る。
	 *
	 * @return テーブルに置かれたカードを表す配列
	 */
	public ArrayList<Hai> getKawa(){
		return this.kawa;
	}
}
