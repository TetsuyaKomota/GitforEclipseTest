package Lesson4;
/**
 * 戦略インターフェースに対応したジャンケンプログラム
 */
public class ObjectJanken
{
  public static void main(String[] args)
  {
	// 審判のインスタンス生成
	Judge judge = new Judge();

	// プレイヤー１（村田さん）の生成
	Player murata = new Murata("村田さん");

	// プレイヤー２（山田さん）の生成
	Player yamada = new Yamada("山田さん");

	// プレイヤー３（佐藤さん）の生成
	Player satou = new Satou("佐藤さん");

	// プレイヤー４（田中さん）の生成
	Player tanaka = new Tanaka("田中さん");

	// 村田さんと山田さんをプレイヤーとしてジャンケンを開始する
	judge.startJanken(murata, yamada);
	// 村田さんと佐藤さんをプレイヤーとしてジャンケンを開始する
	judge.startJanken(murata, satou);
	// 佐藤さんと山田さんをプレイヤーとしてジャンケンを開始する
	judge.startJanken(satou, yamada);
	// 田中さんと村田さんをプレイヤーとしてジャンケンを開始する
	judge.startJanken(tanaka, murata);
  }
}
