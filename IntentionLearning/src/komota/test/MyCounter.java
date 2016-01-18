package komota.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class MyCounter {

	//ログデータ数を数えたりする用の雑なやつ

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		MyCounter counter = new MyCounter();

		try {
			System.out.println("log:"+counter.countGoal("logdata.txt"));
			System.out.println("MtC:"+counter.countGoal("log_MOVE_THE_CENTER.txt"));
			System.out.println("RtB:"+counter.countGoal("log_RIGHT_TO_BLUE.txt"));
			System.out.println("NbO:"+counter.countGoal("log_NEAR_BY_ORANGE.txt"));
			System.out.println("AfG:"+counter.countGoal("log_AWAY_FROM_GREEN.txt"));
			System.out.println("MtS:"+counter.countGoal("log_MAKE_THE_SIGNAL.txt"));
			System.out.println("MtT:"+counter.countGoal("log_MAKE_THE_TRIANGLE.txt"));
//			System.out.println("TRL:"+counter.countGoal("log_TILT_RED_LITTLE.txt"));
//			System.out.println("TRH:"+counter.countGoal("log_TILT_RED_HARD.txt"));
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

	//引数で与えたデータの教示回数（goalの数）を数える
	public int countGoal(String filename) throws IOException{
		int output = 0;

		File file = new File("log/"+filename);
		BufferedReader br = null;

		br = new BufferedReader(new FileReader(file));

		String line = br.readLine();

		while(line != null){
			if(line.split(",")[0].equals("goal  ")){
				output++;
			}
			line = br.readLine();
		}
		br.close();

		return output;
	}

}
