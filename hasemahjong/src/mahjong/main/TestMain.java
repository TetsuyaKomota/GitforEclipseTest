package mahjong.main;

import java.io.IOException;

import mahjong.rules.Rule_Shanten;
import mahjong.rules.Rule_USER;

public class TestMain {

	public static void main(String[] args){

		try {
			Hai.readImage();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

		Master master = new Master();

		Table table_1 = new Table();
		Table table_2 = new Table();
		Table table_3 = new Table();
		Table table_4 = new Table();

		Rule rule_1 = new Rule_USER();
		Rule rule_2 = new Rule_Shanten();
		Rule rule_3 = new Rule();
		Rule rule_4 = new Rule();

		Player player_1 = new Player("Player_1",master,table_1,rule_1);
		Player player_2 = new Player("Player_2",master,table_2,rule_2);
		Player player_3 = new Player("Player_3",master,table_3,rule_3);
		Player player_4 = new Player("Player_4",master,table_4,rule_4);

		ViewFrame frame = new ViewFrame(player_1,player_2,player_3,player_4,master,table_1,table_2,table_3,table_4);

		master.initializeYama();

		master.registerPlayer(player_1);
		master.registerPlayer(player_2);
		master.registerPlayer(player_3);
		master.registerPlayer(player_4);

		master.prepareGame();
		System.out.println(player_1);
		System.out.println(player_2);
		System.out.println(player_3);
		System.out.println(player_4);

		master.startGame();

	}

}
