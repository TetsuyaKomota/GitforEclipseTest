package Lesson3;

public class MultiJanken {


    public static void main(String[] args)
    {
        // 審判（斎藤さん）のインスタンス生成
        Judge saito = new Judge();

        //プレイヤーたちを生成
        Player[] players = new Player[10];

        for(int i=0;i<players.length;i++){
        	players[i] = new Player("PLAYER_"+i);
        }

        // 村田さんと山田さんをプレイヤーとしてジャンケンを開始する
        saito.startJanken(players,5);
    }
}
