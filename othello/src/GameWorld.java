import java.util.Vector;


public class GameWorld {
	
	static Player player;
	static Vector<Token> tokens;
	static Vector<Token> savefile;
	static int range=4;
	static int PushSpace=0;
	static int PushKey=0;
	static int oita=0;
	static int turn=0;
	//turn=0はプレイヤーVS　CPU
	//turn=1はCPU　VSプレイヤー
	//turn=2はプレイヤーVSプレイヤー
	//turn=3はCPU　VS　CPU
	static int SelectPlayer=0;
	static int PushBackSpace=0;

}
