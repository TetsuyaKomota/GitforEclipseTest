package Lesson3;

public class Manager {

	//トーナメント型じゃんけんをマネージメントするクラス

	//フィールド
	//同時にじゃんけんする最大人数
	int maxnum;

	//振り分けられたPlayer
	Player[][] gloups;


	//コンストラクタ
	public Manager(int maxnum){
		this.maxnum = maxnum;
	}

	//引数に与えられたPlayerの人数に応じたJudgeを召喚し，Playerを振り分ける
	private void createJudge(Player[] players){
		System.out.println("グループの振り分けを行います");
		//必要なjudgeの人数を決める
		int numberofgloups = (players.length/this.maxnum)+1;

		//上記分のJudgeとPlayer[]グループを作成する
		this.gloups = new Player[numberofgloups][this.maxnum];

		//gloupsにplayersを割り振っていく
		for(int i=0;i<players.length;i++){
			gloups[i%numberofgloups][i/numberofgloups] = players[i];
		}
	}

	//各グループごとのじゃんけんを指示し，勝者を集めたPlayer[]を作成する
	private Player[] manageJanken(){
		Player[] output = new Player[this.gloups.length];
		Judge judge = new Judge();

		for(int idx=0;idx<output.length;idx++){
			output[idx] = judge.decideTop(this.gloups[idx],1);
		}

		return output;
	}

	//最後の一人になるまで，グループ作成とじゃんけんのマネージメントを繰り替えす
	public Player startJanken(Player[] players){
		Player output = null;
		Player[] survivers = players;

		while(survivers.length > 1){
			this.createJudge(survivers);
			survivers = this.manageJanken();
		}
		output = survivers[0];
		System.out.println("よってこの勝負，");
		System.out.println(output.getName());
		System.out.println("の勝ちです！");
		return output;
	}

}
