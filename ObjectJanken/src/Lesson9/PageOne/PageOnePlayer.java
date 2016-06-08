package Lesson9.PageOne;

import Lesson9.trump.Card;
import Lesson9.trump.Master;
import Lesson9.trump.Player;
import Lesson9.trump.Rule;
import Lesson9.trump.Table;

/**
 * ページワン用プレイヤークラス。
 */
public class PageOnePlayer extends Player
{

    /**
     * コンストラクタ。
     *
     * @param name    プレイヤーの名前
     * @param master 進行役
     * @param table  テーブル
     * @param rule    ルール
     */
    public PageOnePlayer(String name, Master master, Table table, Rule rule)
    {
    	//親(Playerのコンストラクタを呼び出す)
        super(name, master, table, rule);
    }

    /**
     * カードを配る。
     *
     * @param card 受け取ったカード
     */
    public void receiveCard(Card card)
    {
    	//課題：適当な命令を記述する

    	//ただ受け取ればいいので，スーパークラスのreceiveCard()をそのまま使用する
    	super.receiveCard(card);
    }

    /**
     * 順番を指名する。
     *
     * @param nextPlayer 次のプレイヤー(PageOneでは使わないので無視する)
     */
    public void play(Player nextPlayer)
    {
    	//課題：適当な命令を記述する

    	//パスがない以外は7並べと同じ処理でいい
        // 現在の手札を表示する
        System.out.println("  " + myHand_);

        // 現在の手札からテーブルに出せるものを探す
        Card[] candidate = rule_.findCandidate(myHand_, table_);

        // 候補がある場合はテーブルに出す
        if (candidate != null)
        {
            System.out.println("  " + candidate[0] + "を置きました。\n");
            table_.putCard(candidate);

            // テーブルの状態を表示する
            System.out.println(table_);

            // 手札がなくなったら、上がりを宣言する
            if (myHand_.getNumberOfCards() == 0)
            {
                master_.declareWin(this);
            }
        }
        else
        {
            // テーブルに出せるカードがなかった場合、パスする

        	((PageOneMaster)this.master_).pass(this);

        }

    }

}
