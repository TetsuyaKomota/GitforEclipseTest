package mahjong.main;

/**
 * プレイヤーを表すクラス。
 */
public class Player
{
    /** 名前 */
    protected String name_;

    /** テーブル */
    protected Table table_;

    /** 手札 */
    protected Hand myHand_ = new Hand();

    /** 進行役 */
    protected Master master_;

    /** ルール */
    protected Rule rule_;

    /**
     * コンストラクタ。
     *
     * @param name 名前
     * @param master 進行役
     * @param table テーブル
     * @param rule ルール
     */
    public Player(String name, Master master, Table table, Rule rule)
    {
        this.name_ = name;
        this.master_ = master;
        this.table_ = table;
        this.rule_ = rule;

        this.rule_.setPlayer(this);
    }

    /**
     * 手牌のゲッター
     * @return 手牌
     */
    public Hand getHand(){
    	return this.myHand_;
    }

    /**
     * 順番を指名する。
     * 実際の処理はサブクラスで記述すること。
     *
     * @param nextPlayer 次のプレイヤー
     */
    public Hai play(Player nextPlayer){
    	Hai tsumo = master_.getYama().pickHai(0);
    	this.receiveHai(tsumo);

    	int index = -1;
    	while(true){
    		String dahai = this.dahai();
    		int tempsuit = -1;
    		int tempnumber = -1;
    		switch(dahai.substring(0, 1)){
    		case "M":
    			tempsuit = Hai.MAN;
    			tempnumber = Integer.parseInt(dahai.substring(1));
    			break;
    		case "S":
    			tempsuit = Hai.SOU;
    			tempnumber = Integer.parseInt(dahai.substring(1));
    			break;
    		case "P":
    			tempsuit = Hai.PIN;
    			tempnumber = Integer.parseInt(dahai.substring(1));
    			break;
    		case "東":
    			tempsuit = Hai.KAZ;
    			tempnumber = 1;
    			break;
    		case "西":
    			tempsuit = Hai.KAZ;
    			tempnumber = 3;
    			break;
    		case "南":
    			tempsuit = Hai.KAZ;
    			tempnumber = 2;
    			break;
    		case "北":
    			tempsuit = Hai.KAZ;
    			tempnumber = 4;
    			break;
    		case "白":
    			tempsuit = Hai.SAN;
    			tempnumber = 1;
    			break;
    		case "發":
    			tempsuit = Hai.SAN;
    			tempnumber = 2;
    			break;
    		case "中":
    			tempsuit = Hai.SAN;
    			tempnumber = 3;
    			break;

    		}
    		for(int i=0;i<this.myHand_.getNumberOfHais();i++){
    			//dahaiによって指定した牌が手牌にあるか

    			if(this.myHand_.lookHai(i).getSuit() == tempsuit
    					&& this.myHand_.lookHai(i).getNumber() == tempnumber){
    				index = i;
    				break;
    			}
    		}
    		if(index >= 0){
    			break;
    		}
    	}
    	System.out.println(this.myHand_.lookHai(index)+"を切りました");
    	Hai output = this.myHand_.lookHai(index);
    	table_.putCard(this.myHand_.pickHai(index));

       	this.myHand_.repai();

       	return output;
    }

    public String dahai(){

    	try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

    	//String output = this.myHand_.lookHai(0).toString();
    	String output = this.rule_.dahai();
    	return output;
    }

    /**
     * カードを配る。<br>
     * カードを受け取った時の処理を拡張したい場合は、
     * 本メソッドをサブクラスでオーバーライドすること。
     *
     * @param hai 受け取ったカード
     */
    public void receiveHai(Hai hai)
    {
        // 受け取ったカードを手札に加える
    	System.out.println(hai+"をツモりました");
        myHand_.addHai(hai);
    }

    /**
     * プレイヤーの名前を返す。
     * ObjectクラスのtoStringメソッドをオーバーライドしたメソッド。
     *
     * @return プレイヤーの名前
     */
    public String toString()
    {
    	String tehaioutput = "";

    	for(int i=0;i<this.myHand_.getNumberOfHais();i++){
    		tehaioutput = tehaioutput + "," + this.myHand_.lookHai(i);
    	}

        return name_+tehaioutput;

    }
}
