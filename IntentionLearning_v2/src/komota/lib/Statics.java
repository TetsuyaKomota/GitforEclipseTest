package komota.lib;

public class Statics {

	//デバッグモードフラグ．これを true にすると，いろんなメソッドの内部情報を標準出力するようにする．原因不明のエラーに遭遇したらとりあえずこれを true にしてみる
	public static final boolean __DEBUG_MODE__ = false;

	//設定フレームレート
	public static final int FRAME_RATE = 60;

	//パネルの行、列数
	public static final int NUMBEROFPANEL = 400;
	//パネル種類数
	//パネル数に対する特徴量の数をいろいろ変えたいことから，これを使った実装はなくす．代わりにNUMBEROFFEATURESを用いる実装に変更すること
	@Deprecated
	public static final int NUMBEROFKIND = 20;
	//特徴量数．9オブジェクト2次元回転なしなら 18
	public static final int NUMBEROFFEATURES = 40;




	//定数
	//パネルサイズ
	public static int SIZE_PANEL = 200;
	public static final int SIZE_FRAME = 100;
	public static final int SIZE_SEPALATOR = 1;

	//選択パネル枠の太さ
	public static final float FRAME_SIZE_OF_SELECTED_PANEL = 3f;
	//定数
	//オブジェクトサイズ（パネルサイズ何枚分か）
	public static final int SIZE_OBJECT = 10;
	//定数
	//ログデータの種類を表すときに使う種類定数
	public static final int START = 0;
	public static final int GOAL = 1;
	public static final int STATUS = 3;
	public static final int FEATURE = 4;

	//定数
	//行列式にする際の最小値
	public static final double MIN_DETERMINANT = 0.00000001;
	//概数値にする際の最小値
	public static final double MIN_APPROX = 0.0001;

	//定数
	//PR_Mat_SOINNにおける行列作成回数
	public static int NUMBEROFMATRIXS = 100;
	//PR_Mat_SOINNのクロスバリデーション使用データ量
	public static int NUMBEROFEVALUATION = 2;

	//ログデータの最大数
	public static final int MAX_NUMBEROFLOG = 20000;

	//以下PRv2_GA
	//エージェント数
	public static final int GA_NUMBEROFAGENTS = 300;
	//エリート率
	public static final double GA_ELITE_RATE = 0.2;
	//変位率
	public static final double GA_METAMORPHOSE_RATE = 0.8;
	//学習終了閾値
	public static final double GA_THRETHOLD = 100000;
	public static double EM_THRETHOLD = 10;
	//ストライド
	public static final double EM_STRIDE = 100;//100
	//焼きなまし率
	public static final double EM_annealing = 0.999;//0.999
	//更新量限界
	public static final double EM_PROGRESS_NORMA = 0.0001;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		System.out.println("Hello world.");
	}
}
