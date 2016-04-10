package komota.main;

public class Statics {
	//設定フレームレート
	public static final int FRAME_RATE = 60;

	//パネルの行、列数
	public static final int NUMBEROFPANEL = 200;
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
	public static int NUMBEROFMATRIXS = 2;
	//PR_Mat_SOINNのクロスバリデーション使用データ量
	public static int NUMBEROFEVALUATION = 2;

	//ログデータの最大数
	public static final int MAX_NUMBEROFLOG = 20000;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		System.out.println("Hello world.");
	}
}
