package Lesson4;
/**
 * ジャンケンのプレイヤーを表すクラス。
 */
public class Player
{
	// ジャンケンの手を表す定数
	public static final int STONE = 0; // グー
	public static final int SCISSORS = 1; // チョキ
	public static final int PAPER = 2; // パー

	//------------------------
	// プレイヤークラスの属性
	//------------------------
	/** プレイヤーの名前 */
	private String name_;

	/** プレイヤーの勝った回数 */
	private int winCount_ = 0;

	/** じゃんけん開始時のコメント */
	private String startcomment = "今からじゃんけんを開始します";

	/** じゃんけん勝利時のコメント */
	private String wincomment = "私が勝ちました";
	/** じゃんけん敗北時のコメント */
	private String losecomment = "私が負けました";
	/** じゃんけん引き分け時のコメント */
	private String drawcomment = "引き分けました";

	/** 最終的な勝者コメント */
	private String winnerscomment = "最終的に私が勝利しました";
	/** 最終的な敗者コメント */
	private String loserscomment = "最終的に私が敗北しました";
	/** 最終的な引き分けコメント */
	private String drawerscomment = "最終的に引き分けました";

	/** 直前の勝負の勝敗 */
	private int result = NOTHING;
	static final int NOTHING = -1;
	static final int WIN = 0;
	static final int LOSE = 1;
	static final int DRAW = 2;

	/** 直前に出した手 */
	private int showed = NOTHING;


	//------------------------
	// プレイヤークラスの操作
	//------------------------
	/**
	 * プレイヤークラスのコンストラクタ。
	 *
	 * @param name 名前
	 */
	public Player(String name)
	{
		this.name_ = name;
	}

	/**
	 * 勝負を開始するときに勝利回数，直前の情報などを初期化する
	 */
	public void initialize(){
		this.winCount_ = 0;
		this.setResult(NOTHING);
		this.setShowed(NOTHING);
	}

	/**
	 * ジャンケンの手を出す。
	 *
	 * @return ジャンケンの手
	 */
	public int showHand()
	{
		// プレイヤーの手
		int hand = 0;

		// 0.0以上3.0未満の小数として乱数を得る
		double randomNum = Math.random() * 3;
		if (randomNum < 1)
		{
			// randomNum が 0.0以上1.0未満の場合、グー
			hand = STONE;
		}
		else if (randomNum < 2)
		{
			// randomNum が 1.0以上2.0未満の場合、チョキ
			hand = SCISSORS;
		}
		else if (randomNum < 3)
		{
			// randomNum が 2.0以上3.0未満の場合、パー
			hand = PAPER;
		}

		// 決定した手を戻り値として返す
		return hand;
	}

	/**
	 * 審判から勝敗を聞く。
	 *
	 * @param result true:勝ち,false:負け
	 */
	public void notifyResult(boolean result)
	{
		if (true == result)
		{
			// 勝った場合は勝ち数を加算する
			winCount_ += 1;
		}
	}

	/**
	 * 自分の勝った回数を答える。
	 *
	 * @return 勝った回数
	 */
	public int getWinCount()
	{
		return winCount_;
	}

	/**
	 * 自分の名前を答える。
	 *
	 * @return 名前
	 */
	public String getName()
	{
		return name_;
	}

	/**
	 * 開始時のコメントを変更する
	 * @param input 開始時のコメント
	 */
	public void setStartComment(String input){
		this.startcomment = input;
	}
	/**
	 * 開始時のコメントをこたえる
	 * @return 開始時のコメント
	 */
	public String getStartComment(){
		return this.startcomment;
	}
	/**
	 * 勝利時のコメントを変更する
	 * @param input 勝利時のコメント
	 */
	public void setWinComment(String input){
		this.wincomment = input;
	}
	/**
	 * 勝利時のコメントをこたえる
	 * @return 勝利時のコメント
	 */
	public String getWinComment(){
		return this.wincomment;
	}
	/**
	 * 敗北時のコメントを変更する
	 * @param input 敗北時のコメント
	 */
	public void setLoseComment(String input){
		this.losecomment = input;
	}
	/**
	 * 敗北時のコメントをこたえる
	 * @return 敗北時のコメント
	 */
	public String getLoseComment(){
		return this.losecomment;
	}
	/**
	 * 引き分け時のコメントを変更する
	 * @param input 引き分け時のコメント
	 */
	public void setDrawComment(String input){
		this.drawcomment = input;
	}
	/**
	 * 引き分け時のコメントをこたえる
	 * @return 引き分け時のコメント
	 */
	public String getDrawComment(){
		return this.drawcomment;
	}



	/**
	 * 最終的な勝者コメントを変更する
	 * @param input 最終的な勝者コメント
	 */
	public void setWinnersComment(String input){
		this.winnerscomment = input;
	}
	/**
	 * 最終的な勝者コメントをこたえる
	 * @return 最終的な勝者コメント
	 */
	public String getWinnersComment(){
		return this.winnerscomment;
	}
	/**
	 * 最終的な敗者コメントを変更する
	 * @param input 最終的な敗者コメント
	 */
	public void setLosersComment(String input){
		this.loserscomment = input;
	}
	/**
	 * 最終的な敗者コメントをこたえる
	 * @return 最終的な敗者コメント
	 */
	public String getLosersComment(){
		return this.loserscomment;
	}
	/**
	 * 最終的な引き分けコメントを変更する
	 * @param input 最終的な引き分けコメント
	 */
	public void setDrawersComment(String input){
		this.drawerscomment = input;
	}
	/**
	 * 最終的な引き分けコメントをこたえる
	 * @return 最終的な引き分けコメント
	 */
	public String getDrawersComment(){
		return this.drawerscomment;
	}

	public void setResult(int input){
		this.result = input;
	}
	public int getResult(){
		return this.result;
	}
	public void setShowed(int input){
		this.showed = input;
	}
	public int getShowed(){
		return this.showed;
	}
}
