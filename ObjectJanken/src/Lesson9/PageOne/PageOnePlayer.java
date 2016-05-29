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
    }

    /**
     * 順番を指名する。
     *
     * @param nextPlayer 次のプレイヤー(PageOneでは使わないので無視する)
     */
    public void play(Player nextPlayer)
    {
    	//課題：適当な命令を記述する
    }

}
