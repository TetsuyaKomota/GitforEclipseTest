package Lesson5;


/**
 * ジャンケンを行うプレイヤークラス。
 */
public class Player
{
	/** グー */
	public static final int STONE = 0;

	/** チョキ */
	public static final int SCISSORS = 1;

	/** パー */
	public static final int PAPER = 2;

	/** プレイヤーの名前 */
	private String name;

	/** プレイヤーの勝った回数 */
	private int winCount = 0;

	/** 与えられた戦略 */
	private Tactics tactics_;

	//**************************************************************
	/** 与えられたセリフ */
	private Serifu serifu_;

	//**************************************************************


	/**
	 * プレイヤークラスのコンストラクタ。
	 *
	 * @param name 名前
	 */
	public Player(String name)
	{
		this.name = name;
	}

	/**
	 * プレイヤーに戦略を渡す。
	 *
	 * @param tactics 戦略
	 */
	void setTactics(Tactics tactics)
	{
		tactics_ = tactics;
	}

	//**************************************************************
	/**
	 * セリフのセッター
	 * @param input
	 */
	void setSerifu(Serifu input){
		this.serifu_ = input;
	}
	/**
	 * 開始時のセリフ
	 */
	void sayStart(){
		if(this.serifu_ != null){
			System.out.println(this.name+"「"+this.serifu_.start()+"」");
		}
		else{
			System.out.println("このプレイヤーのセリフは用意されていません");
		}
	}
	/**
	 * 勝利時のセリフ
	 */
	void sayWin(){
		if(this.serifu_ != null){
			System.out.println(this.name+"「"+this.serifu_.win()+"」");
		}
		else{
			System.out.println("このプレイヤーのセリフは用意されていません");
		}
	}
	/**
	 * 引き分け時のセリフ
	 */
	void sayDraw(){
		if(this.serifu_ != null){
			System.out.println(this.name+"「"+this.serifu_.draw()+"」");
		}
		else{
			System.out.println("このプレイヤーのセリフは用意されていません");
		}
	}
	/**
	 * 敗北時のセリフ
	 */
	void sayLose(){
		if(this.serifu_ != null){
			System.out.println(this.name+"「"+this.serifu_.lose()+"」");
		}
		else{
			System.out.println("このプレイヤーのセリフは用意されていません");
		}
	}
	/**
	 * 勝者コメント
	 */
	void sayWinner(){
		if(this.serifu_ != null){
			System.out.println(this.name+"「"+this.serifu_.winner()+"」");
		}
		else{
			System.out.println("このプレイヤーのセリフは用意されていません");
		}
	}
	/**
	 * 最終的な引き分けコメント
	 */
	void sayDrawer(){
		if(this.serifu_ != null){
			System.out.println(this.name+"「"+this.serifu_.drawer()+"」");
		}
		else{
			System.out.println("このプレイヤーのセリフは用意されていません");
		}
	}
	/**
	 * 敗者コメント
	 */
	void sayLoser(){
		if(this.serifu_ != null){
			System.out.println(this.name+"「"+this.serifu_.loser()+"」");
		}
		else{
			System.out.println("このプレイヤーのセリフは用意されていません");
		}
	}
	/**
	 * 初期化．勝利回数のリセット．Judgeクラスで最初に呼び出すことで連続で勝負が行えるようにする
	 */
	void initialize(){
		this.winCount = 0;
	}
	//**************************************************************

	/**
	 * ジャンケンの手を出す。
	 *
	 * @return ジャンケンの手
	 */
	int showHand()
	{
		// 与えられた戦略を読んでジャンケンの手を決める
		int hand = tactics_.readTactics();

		// 決定した手を戻り値として返す
		return hand;
	}

	/**
	 * 勝敗を聞く(教える)。
	 *
	 * @param result true:勝ち,false:負け
	 */
	void notifyResult(boolean result)
	{
		if (true == result)
		{
			// 勝った場合は勝ち数を加算する
			winCount += 1;
		}
	}

	/**
	 * 自分の勝った回数を答える
	 *
	 * @return 勝った回数
	 */
	int getWinCount()
	{
		return winCount;
	}

	/**
	 * 自分の名前を答える。
	 *
	 * @return 名前
	 */
	String getName()
	{
		return name;
	}
}
