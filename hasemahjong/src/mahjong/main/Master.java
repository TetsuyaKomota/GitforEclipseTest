package mahjong.main;

import java.util.ArrayList;

/**
 * 進行役を表すクラス。
 */
public class Master
{
    /** プレイヤーのリスト */
    protected ArrayList<Player> players_ = new ArrayList<Player>();
   /** 進行役の手牌(山) */
    private Hand yama = new Hand();
    /** ドラ */
    private Hai[] dora = new Hai[8];
    /** ドラがめくれているか */
    private boolean[] visibledora = new boolean[4];
    //コンストラクタ
    public Master(){

    }
    //山のゲッター
    public Hand getYama(){
    	return this.yama;
    }
    //ドラのゲッター
    public Hai[] getDora(){
    	//見えるドラだけ渡す
    	Hai[] output = new Hai[4];
    	for(int i=0;i<4;i++){
    		if(this.visibledora[i] == true){
    			output[i] = this.dora[i];
    		}
    	}
    	return output;
    }

    /**
     * ゲームの準備をする。
     *
     * @param cards トランプを進行役の手札として渡す
     */
    public void prepareGame()
    {
        System.out.println("【カードを配ります】");

        // トランプをシャッフルする
        yama.shuffle();

        //ドラを決める
        this.dora[0] = this.yama.lookHai(131);
        this.dora[1] = this.yama.lookHai(130);
        this.dora[2] = this.yama.lookHai(129);
        this.dora[3] = this.yama.lookHai(128);
        //ドラをめくる
        this.visibledora[0] = true;
        this.visibledora[1] = false;
        this.visibledora[2] = false;
        this.visibledora[3] = false;


        // プレイヤーの人数を取得する
        int numberOfPlayers = players_.size();

        for (int index = 0; index < 52; index++)
        {
            // カードから一枚引く
            Hai card = yama.pickHai(0);

            // 各プレイヤーに順番にカードを配る
            Player player = (Player)players_.get(index % numberOfPlayers);
            player.receiveHai(card);
        }
        //一度全員理牌する
        this.players_.get(0).getHand().repai();
        this.players_.get(1).getHand().repai();
        this.players_.get(2).getHand().repai();
        this.players_.get(3).getHand().repai();
    }

    /**
     * ゲームを開始する。
     */
    public void startGame()
    {
        System.out.println("\n【ゲームを開始します】");

        /*
         * 和了フラグ
         * [0]番プレイヤーが[1]番プレイヤーから和了．（自分ならツモ）
         */
        int[] hola = null;

        // プレイヤーの人数を取得する
        for (int count = 0;yama.getNumberOfHais() > 14; count++)
        {
            int playerIndex = count % players_.size();
            int nextPlayerIndex = (count + 1) % players_.size();

            // 指名するプレイヤーの取得
            Player player = (Player)players_.get(playerIndex);

            // 次のプレイヤーの取得
            Player nextPlayer = (Player)players_.get(nextPlayerIndex);

            // プレイヤーを指名する
            System.out.println("\n" + player + "さんの番です。");
            Hai dahai = player.play(nextPlayer);
            /* ********************************************* */
            /* ************上がり条件をここに記述する******* */
            //ツモ和了の場合，dahaiはnullを返すため，それを用いてツモ和了を識別する
            if(dahai == null){
            	hola = new int[2];
            	hola[0] = playerIndex;
            	hola[1] = playerIndex;
            }
            for(int i=0;i<this.players_.size();i++){
            	if(i != playerIndex && this.players_.get(i).getHand().isRon(dahai) == true){
            		hola = new int[2];
            		hola[0] = i;
            		hola[1] = playerIndex;
            	}
            }
            /* ********************************************* */
            if(hola != null){
            	break;
            }
        }
        if(hola != null){
        	if(hola[0] == hola[1]){
        		System.out.println(players_.get(hola[0]).name_+"さんのツモ和了です");
        	}
        	else{
        		System.out.println(players_.get(hola[0]).name_+"さんが"+players_.get(hola[1]).name_+"さんからロン和了です");
        	}
        }

        // プレイヤーが上がって残り1名になるとループを抜ける
        System.out.println("【ゲームを終了しました】");
    }

    /**
     * 上がりを宣言する。
     *
     * @param winner 上がったプレイヤー
     */
    public void declareWin(Player winner)
    {
        // 上がったプレイヤー
        System.out.println("  " + winner + "さんが上がりました！");

        // 上がったプレイヤーをリストからはずす
        deregisterPlayer(winner);

    }

    /**
     * ゲームに参加するプレイヤーを登録する。
     *
     * @param player 参加するプレイヤー
     */
    public void registerPlayer(Player player)
    {
        // リストに参加者を追加する
        players_.add(player);
    }
    /**
     * 山を初期化する
     */
    public void initializeYama(){
    	this.yama = new Hand();
    	for(int j=0;j<4;j++){
    		for(int i=1;i<=9;i++){
    			yama.addHai(new Hai(Hai.MAN,i));
    			yama.addHai(new Hai(Hai.SOU,i));
    			yama.addHai(new Hai(Hai.PIN,i));
    		}
    		for(int i=1;i<=4;i++){
    			yama.addHai(new Hai(Hai.KAZ,i));
    		}
    		for(int i=1;i<=3;i++){
    			yama.addHai(new Hai(Hai.SAN,i));
    		}
    	}
    }

    /**
     * ゲームに参加するプレイヤーを登録から削除する。
     * 勝ったプレイヤーをゲームから抜くときに使用する。
     *
     * @param player 削除するプレイヤー
     */
    public void deregisterPlayer(Player player)
    {
        // リストに参加者を追加する
        players_.remove(players_.indexOf(player));

        // 残りプレイヤーが１人になった時は敗者を表示する
        if (players_.size() == 1)
        {
            Player loser = (Player)players_.get(0);
            System.out.println("  " + loser + "さんの負けです！");
        }
    }
}
