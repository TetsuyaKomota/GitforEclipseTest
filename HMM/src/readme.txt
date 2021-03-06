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
				・void		show()	:各確率や状態数、出力数などを表示する
			[privateメソッド]
				・
				・
				・

	2-2.LtoRHMM

		HMMの、状態遷移が左から右の一本道のみのモデル。



3.Note

	[HMM覚書]
		・wikipediaより。HMMは有限状態機械で表せ、「各隠れ状態の持つ値」に「各経路の増分」を考慮した結果、最良の経路を保持する、という形で解読する。
			各経路の増分という考え方は、遷移確率で表現しているため間違いではないと思われる。が、「各隠れ状態の持つ値」という部分が不明。現在の実装では、
			各状態は値を保持していない。
		・
	[LtoRHMM使い方]
		・学習させる出力列の、出力の変化＋１以上の状態数を持つモデルを生成するとうまく行く
		・learnwithBaumWelch([学習させる出力列])でOK
		・尤度計算の閾値とループ回数はそれぞれ定数THRESHOLD,LOOPCOUNTで決められる。
		・pw.println()で、log/test.txtにテキストを書き込める

	[バウム＝ウェルチアルゴリズムの計算時間]
		・状態数を2から3に変更したとき、圧倒的に計算時間が長くなった。
		・状態数を5にしたとき、一晩（８時間）では尤度再計算1回目にすら到達しなかった。
		・出力数を3から1000000に変更したが、大きな時間変化は感じなかった。
		・出力列の長さ（時間ステップ数）を39にしたところ、状態数2でも計算が1ステップも動かなかった。
		・以上の結果から、grid計算がこのアルゴリズムのボトルネックになっていることがわかる。

	[不具合報告]
		・出力のステップ数が状態数より少ないとバグる。（実際そんなことはないはずなので気にする必要はない）
		・特定の条件で尤度がNaNになる。←どうも、この条件かつ「3つ以上の出力列で学習する」とまずいらしい。
		(条件例)
		1.
		LtoRHMM hmm2 = new LtoRHMM(6,100);
		int[] temp1 = {0,0,1,2,3,4};
		int[] temp4 = {0,1,1,2,3,4};
		int[] temp2 = {0,1,2,2,3,4};
		int[] temp3 = {0,1,2,3,3,4};
		int[] temp5 = {0,1,2,3,4,4};
		・詳しく調べた結果、「学習する出力列が上記のうち2つ、またはtemp1,temp5の組み合わせ以外の2つ、または連番（順連、逆連）の3つでのみ正常動作する」
		・NaNになっている個所を調べてみると、どうやらΓの値が間違えているっぽい。temp1,2,3,4を入力した場合、temp4のΓ[0][0][0]を計算したところでNaNになっている。
			それ以降はそのΓをもとに計算を行っているため、全てNaNとなり、結果として尤度がNaNと出力された。
		・Γの値というより、直前のΓを用いて計算した出力確率が、ある状態ですべてゼロになっており、それを用いたΓの再計算でゼロ割などが発生してNaNになったと考えられる。
		・つまり問題はΓから出力確率を再計算するところ
		・ちゃんと見たらΓの分母であるlikelihoodがゼロになってしまっているのが悪そう。n番目の出力列を学習しきった後、n+1番目の出力列での尤度を計算した際に、一時的に低い尤度が出てしまうのが原因？
		・HMMの状態数が時間ステップ数と同じ場合、過学習が行われて別の出力列の尤度がゼロになることが判明した。ついにNaNになる問題に終止符が打たれたといえる。

		[今後のスケジュール]
		・(済)計算時間を短縮する（ボトルネックがどうやらgetHMMLikelihoodで全通り試してしまっていることっぽい。とりあえずそこを修正）
		・1次元のGMMを実装する
		・GMMを用いたReproductionを行う
		・GMMを用いたLearningを行う
		・多次元に拡張する
		・（同時並行で）GUI作成
		・（同時並行で）離散空間での実装を考える（今回の研究で重要視しているのが「終了状態の推測」なのだとしたら、軌道を推定するHMMは不要なのではという見地より）
