package Lesson9.PageOne;
import Lesson9.trump.Card;
import Lesson9.trump.Hand;
import Lesson9.trump.Master;
import Lesson9.trump.Player;
import Lesson9.trump.Rule;
import Lesson9.trump.Table;

/**
 * ページワンプログラム。
 */
public class PageOne
{
    public static void main(String args[])
    {
        // 進行役の生成
        Master master = new PageOneMaster();

        // テーブルの生成
        Table table = new PageOneTable();

        // ルールの生成
        Rule rule = new PageOneRule();

        //ユーザールールの生成
        Rule rule_user = new PageOneRule_forUser();

        // プレイヤーの生成
        Player murata = new PageOnePlayer("村田", master, table, rule);
        Player yamada = new PageOnePlayer("山田", master, table, rule);
        Player saito  = new PageOnePlayer("斎藤", master, table, rule);
        Player user   = new PageOnePlayer("俺様", master, table, rule_user);

        // プレイヤーを登録
        master.registerPlayer(murata);
        master.registerPlayer(yamada);
        master.registerPlayer(saito);
        master.registerPlayer(user);

        // トランプを生成する
        Hand trump = createTrump();
        trump.shuffle();

        // ゲームの準備をする
        master.prepareGame(trump);

        // ゲームを開始する
        master.startGame();

    }

    /**
     * 53枚のトランプを生成する。
     *
     * @return トランプを格納したDeal
     */
	private static Hand createTrump() {
		Hand trump = new Hand();

		// 各スート13枚のカードをを生成する
		for (int number = 1; number <= Card.CARD_NUM; number++) {
			trump.addCard(new Card(Card.SUIT_CLUB, number));
			trump.addCard(new Card(Card.SUIT_DIAMOND, number));
			trump.addCard(new Card(Card.SUIT_HEART, number));
			trump.addCard(new Card(Card.SUIT_SPADE, number));
		}

		return trump;
	}
}

