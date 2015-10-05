「HMM」プロジェクトのディレクトリ構成及びクラスの使用方法、メモなど

1.Constructure

	本プロジェクトには以下のパッケージが存在する

	1-1.komota.main

		実装完了し、清書したファイル。およびmainメソッド。

	1-2.komota.test

		書きかけのファイル。主にここで作業し、大きな変更の必要のなくなったものはmainパッケージに移行する。

2.How to use

	mainパッケージには以下の完成済みクラスが存在する

	2-1.HMM

		HMMクラス。

		[フィールド]
			・int		numstatus	:状態数
			・int[] 	status		:状態（未使用）
			・int 		numoutput	:出力数
			・int[]		output		:出力（未使用）

			・double[]		proinitial			:初期状態確率（事前確率）
			・double[][] 	protransition		:状態遷移確率([この状態から][この状態になる]確率)
			・double[][] 	prooutput			:出力確率([この状態で][この出力を得る]確率)

			・int	 curstatus		:現在の状態（通常不可視）
			・int	 curoutput		:現在の出力（未使用）

		[コンストラクタ]
			・HMM(int numstatus,int numoutput)
				HMMモデルを生成。状態数と出力数を引数に与える。（ともに整数として扱う）
				事前確率、遷移確率、出力確率は全て「1/母数」に初期化される

		[メソッド]
			[セッター、ゲッター]
				・getCurStatus		:ゲッター:現状態
				・setProInitial		:セッター:初期状態確率（事前確率）
				・setProTransition	:セッター:状態遷移確率
				・setProOutput		:セッター:出力確率
			[publicメソッド]
				・void		initialize()		:状態の初期化。初期状態確率に従って状態を決定する。（HMMを初期化するわけではない）
				・int		output()			:現状態から出力確率に従って出力する。
				・void		transition()		:現状態から状態遷移確率に従って状態遷移する。

				・double 	getLikelihood(int[] inputouts,int[] inputstas)	:出力列inputoutsが、状態列inputstasから得られることの尤度を計算する。
				・int[]		getBitabi(int[] outputs)						:出力列outputsが出力された場合に最も尤もらしい状態遷移（ビタビ経路）を出力する。
			[privateメソッド]
				・
				・
				・
