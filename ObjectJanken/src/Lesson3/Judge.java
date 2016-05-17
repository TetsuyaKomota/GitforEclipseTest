package Lesson3;

/**
 * ジャンケンの審判を表すクラス。
 */
public class Judge
{
    /**
     * ジャンケンを開始する。
     *
     * @param player1 判定対象プレイヤー1
     * @param player2 判定対象プレイヤー2
     * @oaram jankencount じゃんけん回数
     */
    public Player[] startJanken(Player player1,Player player2,int jankencount){
        Player[] output = new Player[1];
        // ジャンケンの開始を宣言する
        System.out.println("【ジャンケン開始】\n");

        // ジャンケンを jankencount 回行う
        for (int cnt = 0; cnt < jankencount; cnt++)
        {
            // 何回戦目か表示する
            System.out.println("【" + (cnt + 1) + "回戦目】");

            while(true){
            	Player winner = judgeJanken(player1,player2);

            	if(winner != null){
                    // 勝者を表示する
                    System.out.println("\n" + winner.getName() + "が勝ちました!\n");

                    // 勝ったプレイヤーへ結果を伝える
                    winner.notifyResult(true);
            		break;
            	}
            	System.out.println("\nあいこでしょ！\n");
            }
        }

        // ジャンケンの終了を宣言する
        System.out.println("【ジャンケン終了】\n");

        // 最終的な勝者を判定する
        Player finalWinner = judgeFinalWinner(player1, player2);

        // 結果の表示
        System.out.print(
            player1.getWinCount() + " 対 " + player2.getWinCount() + "で");

        if (finalWinner != null)
        {
            System.out.println(finalWinner.getName() + "の勝ちです！\n");
        }
        else
        {
            System.out.println("引き分けです！\n");
        }
        output[0] = finalWinner;
        return output;
    }

    //じゃんけん回数を指定しない場合，3回戦行う
    public Player[] startJanken(Player player1,Player player2){
    	return this.startJanken(player1, player2, 3);
    }

    /**
     * 3人以上で同時にじゃんけんを行う．
     * @param players プレイヤー配列
     * @param jankencount じゃんけん回数
     */
    public Player[] startJanken(Player[] players,int jankencount){
    	Player[] output = null;
        // ジャンケンの開始を宣言する
    	System.out.println("【ジャンケン開始】\n");
    	// ジャンケンを jankencount 回行う
    	for (int cnt = 0; cnt < jankencount; cnt++){
    		// 何回戦目か表示する
    		System.out.println("【" + (cnt + 1) + "回戦目】");

    		while(true){
    			Player[] winners = judgeJanken(players);

    			if(winners != null){
    				// 勝者を表示する
    				for(int i=0;i<winners.length;i++){
    					if(winners[i] == null){
    						break;
    					}
    					System.out.println("\n" + winners[i].getName() + "が勝ちました!\n");
    					// 勝ったプレイヤーへ結果を伝える
    					winners[i].notifyResult(true);
    				}
    				break;
    			}
    			System.out.println("\nあいこでしょ！\n");
    		}
    	}
        // ジャンケンの終了を宣言する
        System.out.println("【ジャンケン終了】\n");

        // 最終的な勝者を判定する
        Player[] finalWinner = judgeFinalWinner(players);
        output = finalWinner;

        // 結果の表示
        System.out.println("プレイヤー名:勝利回数");
        for(int i=0;i<players.length;i++){
        	if(players[i] == null){
        		break;
        	}
        	System.out.println(players[i].getName() + ":" + players[i].getWinCount());
        }

        if (finalWinner != null)
        {
        	System.out.println("よってこの勝負，");
            for(int i=0;i<finalWinner.length;i++){
            	if(finalWinner[i] == null){
            		break;
            	}
            	System.out.println(finalWinner[i].getName() + ",");
            }
            System.out.println( "の勝ちです！\n");

        }
        else
        {
            System.out.println("引き分けです！\n");
        }
        return output;
    }
    //じゃんけん回数を指定しない場合，3回戦行う
    public Player[] startJanken(Player[] players){
    	return this.startJanken(players, 3);
    }

    //勝者が一人になるまでじゃんけんを繰り返す
    public Player decideTop(Player[] players , int jankencount){
    	Player output = null;

    	Player[] survivers = players;

    	while(survivers[1] != null){
    		survivers = this.startJanken(survivers, jankencount);
    	}

    	output = survivers[0];

    	return output;
    }



    /**
     * 「ジャンケン、ポン！」と声をかけ、
     * プレイヤーの手を見て、どちらが勝ちかを判定する。
     *
     * @param player1 判定対象プレイヤー1
     * @param player2 判定対象プレイヤー2
     * @return 勝ったプレイヤー。引き分けの場合は null を返す。
     */
    private Player judgeJanken(Player player1, Player player2)
    {
        Player winner = null;

        // プレイヤー１の手を出す
        int player1hand = player1.showHand();

        // プレイヤー２の手を出す
        int player2hand = player2.showHand();

        // それぞれの手を表示する
        printHand(player1hand);
        System.out.print(" vs. ");
        printHand(player2hand);
        System.out.print("\n");

        // プレイヤー１が勝つ場合
        if ((player1hand == Player.STONE && player2hand == Player.SCISSORS)
            || (player1hand == Player.SCISSORS && player2hand == Player.PAPER)
            || (player1hand == Player.PAPER && player2hand == Player.STONE))
        {
            winner = player1;
        }
        // プレイヤー２が勝つ場合
        else if (
            (player1hand == Player.STONE && player2hand == Player.PAPER)
                || (player1hand == Player.SCISSORS && player2hand == Player.STONE)
                || (player1hand == Player.PAPER
                    && player2hand == Player.SCISSORS))
        {
            winner = player2;
        }

        // どちらでもない場合は引き分け(nullを返す)

        return winner;
    }
    /**
     * 3人以上の手を見て，勝者を判定する
     * @param players 判定対象プレイヤー配列
     * @return
     */
    private Player[] judgeJanken(Player[] players){
    	Player[] output = null;
    	//参加者の人数を把握する．参加者人数はplayersの長さとは異なる
    	int count = 0;
    	while(count < players.length && players[count] != null){
    		count++;
    	}

    	//全員分の手を出す
    	int[] playershand = new int[players.length];
    	for(int i=0;i<count;i++){
    		playershand[i] = players[i].showHand();
    	}
    	//全員の手を表示する．10人ごとに改行する
    	for(int i=0;i<count;i++){
    		this.printHand(playershand[i]);
    		if(i < playershand.length-1){
    			System.out.print(" vs. ");
    		}
    		if(i%10 == 9){
    			System.out.println();
    		}
    	}
    	//グー，チョキ，パーのグループに分ける
    	Player[] G = new Player[players.length];
    	int count_G = 0;
    	Player[] C = new Player[players.length];
    	int count_C = 0;
    	Player[] P = new Player[players.length];
    	int count_P = 0;

    	for(int i=0;i<count;i++){
    		switch(playershand[i]){
    		case Player.STONE:
    			G[count_G] = players[i];
    			count_G++;
    			break;
    		case Player.SCISSORS:
    			C[count_C] = players[i];
    			count_C++;
    			break;
    		case Player.PAPER:
    			P[count_P] = players[i];
    			count_P++;
    			break;
    		}
    	}
    	//勝敗をcount_*を用いて確認する
    	if(count_G != 0 && count_C != 0 && count_P == 0){
    		//グーとチョキがいてパーがいないならグーが勝者
    		output = G;
    	}
    	else
    	if(count_G != 0 && count_C == 0 && count_P != 0){
    		//パーとグーがいてチョキがいないならパーが勝者
    		output = P;
    	}
    	else
    	if(count_G == 0 && count_C != 0 && count_P != 0){
    		//チョキとパーがいてグーがいないならチョキが勝者
    		output = C;
    	}

    	//それ以外ならあいこ
    	return output;
    }

    /**
     * 3人以上のじゃんけんにおける最終的な勝者を判定する．
     *
     * @param players 判定対象プレイヤー配列
     * @return 勝者プレイヤー配列．
     */
    private Player[] judgeFinalWinner(Player[] players){
    	Player[] output = new Player[players.length];
    	//参加者人数を確認する．参加者人数はplayersの長さとは異なる
    	int count = 0;
    	while(count < players.length && players[count] != null){
    		count++;
    	}

    	//全員から勝ち数を聞き，最大勝利回数を確認する
    	int [] wincount = new int[players.length];
    	int maxwin = 0;
    	for(int i=0;i<count;i++){
    		wincount[i] = players[i].getWinCount();
    		if(wincount[i] > maxwin){
    			maxwin = wincount[i];
    		}
    	}
    	//最大勝利回数を持つプレイヤーを output に入れる
    	int idx = 0;
    	for(int i=0;i<count;i++){
    		if(wincount[i] == maxwin){
    			output[idx] = players[i];
    			idx++;
    		}
    	}

    	return output;
    }


    /**
     * 最終的な勝者を判定する。
     *
     * @param player1 判定対象プレイヤー1
     * @param player2 判定対象プレイヤー2
     * @return 勝ったプレイヤー。引き分けの場合は null を返す。
     */
    private Player judgeFinalWinner(Player player1, Player player2)
    {
        Player winner = null;

        // Player1から勝ち数を聞く
        int player1WinCount = player1.getWinCount();

        // Player2から勝ち数を聞く
        int player2WinCount = player2.getWinCount();

        if (player1WinCount > player2WinCount)
        {
            // プレイヤー1の勝ち
            winner = player1;
        }
        else if (player1WinCount < player2WinCount)
        {
            // プレイヤー2の勝ち
            winner = player2;
        }

        // どちらでもない場合は引き分け(nullを返す)

        return winner;
    }

    /**
     * ジャンケンの手を表示する。
     *
     * @param hand ジャンケンの手
     */
    private void printHand(int hand)
    {
        switch (hand)
        {
            case Player.STONE :
                System.out.print("グー");
                break;

            case Player.SCISSORS :
                System.out.print("チョキ");
                break;

            case Player.PAPER :
                System.out.print("パー");
                break;

            default :
                break;
        }
    }
}
