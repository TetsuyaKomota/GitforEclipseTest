package Lesson9.PageOne;

import Lesson9.trump.Card;
import Lesson9.trump.Hand;
import Lesson9.trump.Master;
import Lesson9.trump.Player;

/**
 * ページワン用進行役クラス。
 */
public class PageOneMaster extends Master
{

    /**
     * ゲームの準備をする。
     *
     * @param cards トランプを進行役の手札として渡す
     */
	private Hand hand_;


    /* ゲーム開始
     * 7枚ずつ配るためにオーバーライドした
     * @see trump.Master#prepareGame(trump.Hand)
     */
	@Override
    public void prepareGame(Hand cards)
    {
        System.out.println("【カードを配ります】");

        // トランプをシャッフルする
        cards.shuffle();

        // プレイヤーの人数を取得する
        int numberOfPlayers = players_.size();

        //各人に7枚ずつ配布する
        for (int index = 0; index < 7*numberOfPlayers; index++)
        {
            // カードから一枚引く
            Card card = cards.pickCard(0);

            // 各プレイヤーに順番にカードを配る
            Player player = (Player)players_.get(index % numberOfPlayers);
            player.receiveCard(card);
        }

        //残りを自分の手札とする
        hand_=cards;

    }

    /**
     * パスを宣言する。<br>
     *
     * @param player パスするプレイヤー
     */
    public void pass(PageOnePlayer player)
    {
        // パスを表示
        System.out.print("  " + player + "さんはパスしました！");

        // 自分自身の手札から一枚引く
        Card card = hand_.pickCard(0);

        //カードが一枚引けたか
		if (card != null) {
			// パスしたプレイヤーに渡す
			System.out.println(" " + card + "を渡します");
			player.receiveCard(card);
		} else {
			// nullであれば残りのカードが無いので終了する
			System.out.println("\n【カードが無いので終了します.】");
			System.exit(0);
		}

    }



}
