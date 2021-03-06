package komota.libs;

public class Statics {
	//デバッグモードフラグ．これを true にすると，いろんなメソッドの内部情報を標準出力するようにする．原因不明のエラーに遭遇したらとりあえずこれを true にしてみる
	public static final boolean __DEBUG_MODE__ = false;

	//画面サイズ
	public static final int WINDOW_WIDTH 	= 1200;
	public static final int WINDOW_HEIGHT 	= 675;
	public static final int FRAME_WIDTH		= 3;

	//キーリスナーで使用するキーの数
	public static final int KEY_NUMBEROFKEY = 7;
	//キーリスナーで使うキー定数
	public static final int KEY_SPACE 	= 0;
	public static final int KEY_X 		= 1;
	public static final int KEY_Z 		= 2;
	public static final int KEY_UP 		= 3;
	public static final int KEY_DOWN 	= 4;
	public static final int KEY_RIGHT 	= 5;
	public static final int KEY_LEFT 	= 6;

	//漫才モードのキャラ間隔
	public static final int MANZAI_CHARA_INTERVAL	= 40;
	//漫才モードのテキスト横長さ(全角30文字)
	public static final int MANZAI_TEXT_LENGTH = 30;

	//漫才モードの表情の数
	public static final int MANZAI_CHARA_NUMBEROFFACE = 5;

	//漫才モードの人の表情
	public static final int MANZAI_CHARA_FACE_NORMAL 	= 0;
	public static final int MANZAI_CHARA_FACE_HAPPY 	= 1;
	public static final int MANZAI_CHARA_FACE_ANGRY 	= 2;
	public static final int MANZAI_CHARA_FACE_SAD 		= 3;
	public static final int MANZAI_CHARA_FACE_KOMARI 	= 4;

	//行列に関する定数
	public static final double MATRIX_MIN_DETERMINANT = 0.00000001;
	//概数値にする際の最小値
	public static final double MATRIX_MIN_APPROX = 0.0001;


}
