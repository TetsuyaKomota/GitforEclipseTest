package komota.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class MyTask {
	//複数のタスクプリミティブの列で動作を認識し、位置遷移と状態遷移を含むより高度な動作の再現を行うクラス

	//フィールド
	//動作プリミティブ数
	int numberofprimitives;
	//動作プリミティブ列
	MyTaskPrimitive[] primitives;
	//動作名
	String taskname = "";

	//コンストラクタ
	//コンストラクタ時にログデータから動作プリミティブ数を把握してインスタンスを生成する
	public MyTask(String filename,String taskname){
		this.taskname = taskname;
		File file = new File("log/"+filename);
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		//startログからgoalログの間のstatusログの数+1個のタスクプリミティブを生成する
		int count = 0;
		while(true){
			String templine = null;
			try {
				templine = br.readLine();
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
			if(templine == null){
				System.out.println("[MyTask]ログねぇじゃんクソかよ");
				break;
			}
			if(templine.split(",")[0].equals("start ") == true){
				count = 1;
			}
			else if(templine.split(",")[0].equals("status") == true){
				count++;
			}
			else if(templine.split(",")[0].equals("goal  ") == true){
				break;
			}
		}
		this.primitives = new MyTaskPrimitive[count];
		for(int i=0;i<this.primitives.length;i++){
			this.primitives[i] = new MyTaskPrimitive(filename,taskname+"_"+i);
		}
	}

	//タスク名のゲッター
	public String getTaskName(){
		return this.taskname;
	}

	//最も尤もらしいPRのreproductionを実行する
	public void reproductionTask(MyFrame frame){
	}


}
