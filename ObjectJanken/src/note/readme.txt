オブジェクト指向分析設計 演習課題 16M11777 菰田 徹也

1. 基本課題

 if(winner != null) の条件文を winner != null になるまでループさせることで勝敗がつくまでじゃんけんを繰り返すようにした．

2. 応用課題（拡張機能）

	2-1. じゃんけん回数を引数で指定できるようにstartJanken()を変更．オーバーロードによってじゃんけん回数を指定しない場合は今まで通り3回戦行うように実装した．
	2-2. 3人以上で同時にじゃんけんを行えるようにstartJanken(Player[] players)をオーバーロード．その実装のために，judgeJanken(Player] players),judgeFinalWinner(Player[] players)もオーバーロード．
	2-3. 3人以上で同時にじゃんけんの動作確認用のMultiJudgeクラスを作成
	2-4. 特定の手しか出さないSTONEPlayerクラス，SCISSORSplayerクラス，PAPERPlayerクラスを追加．Playerクラスを継承しshowHand()メソッドをオーバーライドすることで実装

	2-5. 全員同時にじゃんけんするのではなく，指定した人数以下のグループに分かれてじゃんけんを繰り返す機能を追加，それを行うManagerクラスを実装．
		2-5-1. JudgeクラスのstartJanken()メソッドを，勝者を返す形に変更
		2-5-2. Judgeクラスに，3人以上でじゃんけんを行う場合に勝者が一人になるまでじゃんけんを繰り返すdecideTop()メソッドを追加．
		2-5-3. Managerクラスの説明

			フィールド
				maxnum	:int		:同時にじゃんけんする最大人数．勝ち残っているPlayerをこの人数以下のグループに分割して個々にじゃんけんを行う
				gloups	:Player[][]	:グループ分けされた，勝ち残っているPlayer.
			メソッド
				createJudge()	:private	: 勝ち残っているPlayerを maxnum 以下の人数のグループに分割し，gloupsフィールドに保持する
				manageJanken()	:private	: JudgeのdecideTop()メソッドを呼び出してグループごとに勝者が一人になるまでじゃんけんを行い，勝者を集めたPLayer[]配列を再生成する
				startJanken()	:public		: createJudge()メソッドとmanageJanken()メソッドを繰り返し，勝者がただ一人になるまでじゃんけんを繰り返す．

			トーナメント型じゃんけんを使用することで，MultiJankenクラスを用いた300人同時じゃんけんでは勝者が決まるのにかなりの時間を要するところを，Managerクラスを用いたObjectJanken_Tournamentでは一瞬で勝負が決まるようになった．

3. 拡張機能の使用方法

 mainメソッドはObjectJanken，MultiJanken，ObjectJanken_Tournament の三つに存在する．それぞれ

 ・サンプルプログラムそのまま(自然な拡張になっていることの確認)
 ・多人数同時じゃんけんの実装
 ・多人数トーナメント型じゃんけんの実装

 のテストを行える．MultiJankenクラス，ObjectJanken_Tournamentクラスに関しては，playersの配列長さを変更することで人数の変更を行える．

