package Lesson5;
/**
 * 戦略インターフェースに対応したジャンケンプログラム。
 */
public class ObjectJanken
{
  public static void main(String[] args)
  {
	// 審判のインスタンス生成
	Judge judge = new Judge();

	// プレイヤー１（村田さん）の生成
	Player murata = new Player("村田さん");

	// 村田さんに渡す戦略クラスを生成する
	Tactics murataTactics = new StoneOnlyTactics();

	// 村田さんに戦略クラスを渡す
	murata.setTactics(murataTactics);

	// プレイヤー２（山田さん）の生成
	Player yamada = new Player("山田さん");


	//************************************************************
	// 山田さんに渡す戦略クラスを生成する
	Tactics yamadaTactics = new MeanedRandomTactics();
	//************************************************************

	// 山田さんに戦略クラスを渡す
	yamada.setTactics(yamadaTactics);



	//************************************************************
	// プレイヤー３（田中さん）の生成
	Player tanaka = new Player("田中さん");
	//田中さんに渡す戦略とセリフのクラスを生成する
	Tanakas_SOUL soul = new Tanakas_SOUL();
	//田中さんに戦略クラスとセリフクラスを渡す
	tanaka.setTactics(soul);
	tanaka.setSerifu(soul);
	//************************************************************


	//************************************************************
	//セリフクラスを渡す
	murata.setSerifu(new Serifu_Male());
	yamada.setSerifu(new Serifu_Female());
	//************************************************************

	// 村田さんと山田さんをプレイヤーとしてジャンケンを開始する
	judge.startJanken(murata, yamada);

	//************************************************************
	// 山田さんと田中さんをプレイヤーとしてジャンケンを開始する
	judge.startJanken(yamada, tanaka);
	//************************************************************

  }
}
