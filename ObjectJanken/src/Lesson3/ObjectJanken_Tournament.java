package Lesson3;

public class ObjectJanken_Tournament {

	public static void main(String[] args){

		//マネージャーを生成
		Manager manager = new Manager(5);

		//プレイヤーたちを生成
        Player[] players = new Player[300];

        for(int i=0;i<players.length;i++){
        	players[i] = new Player("PLAYER_"+i);
        }


        //トーナメント型じゃんけんによって勝者を決める．
        manager.startJanken(players);

	}

}
