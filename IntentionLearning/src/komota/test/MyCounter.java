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
			System.out.println("************************MORE***************************");
			System.out.println("RtO:"+counter.countGoal("log_MORE_RIGHT_TO_ORANGE.txt"));
			System.out.println("RtG:"+counter.countGoal("log_MORE_RIGHT_TO_GREEN.txt"));
			System.out.println("RtY:"+counter.countGoal("log_MORE_RIGHT_TO_YELLOW.txt"));
			System.out.println("NbB:"+counter.countGoal("log_MORE_NEAR_BY_BLUE.txt"));
			System.out.println("NbG:"+counter.countGoal("log_MORE_NEAR_BY_GREEN.txt"));
			System.out.println("NbY:"+counter.countGoal("log_MORE_NEAR_BY_YELLOW.txt"));
			System.out.println("AfB:"+counter.countGoal("log_MORE_AWAY_FROM_BLUE.txt"));
			System.out.println("AfO:"+counter.countGoal("log_MORE_AWAY_FROM_ORANGE.txt"));
			System.out.println("AfY:"+counter.countGoal("log_MORE_AWAY_FROM_YELLOW.txt"));

			counter.countW_disMm();
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

	//output_W.txtの結果から，各動作のdisの最大値と再招致を算出
	public void countW_disMm() throws IOException{
		//動作の種類が変わっても求められるようにする
		double[] Max = new double[100];
		double[] min = new double[100];
		int[][] bunpu = new int[100][15];
		for(int i=0;i<Max.length;i++){
			Max[i] = -1;
			min[i] = 100000;
		}
		int idx = -1;

		File file = new File("log/output_W.txt");
		BufferedReader br = null;

		br = new BufferedReader(new FileReader(file));

		String line = br.readLine();

		while(true){
			if(line == null){
				break;
			}
			else if(line.split(",")[0].equals("task") == true){
				idx++;
			}
			else if(line.split(",")[0].equals("dis   ") == true){
				if(Double.parseDouble(line.split(",")[1]) > Max[idx]){
					Max[idx] = Double.parseDouble(line.split(",")[1]);
				}
				else if(Double.parseDouble(line.split(",")[1]) < min[idx]){
					min[idx] = Double.parseDouble(line.split(",")[1]);
				}

				if(Double.parseDouble(line.split(",")[1]) < bunpu[0].length-1){
					bunpu[idx][(int)Double.parseDouble(line.split(",")[1])]++;
				}
				else{
					bunpu[idx][bunpu[0].length-1]++;
				}
			}
			line = br.readLine();
		}
		for(int i=0;i<=idx;i++){
			System.out.print("Task:"+i+" Max:"+Max[i]+" min:"+min[i]+" bunpu:");
			for(int j=0;j<bunpu[0].length;j++){
				System.out.print(" ["+j+"]:"+bunpu[i][j]);
			}
			System.out.println();
		}
		br.close();

	}

}
