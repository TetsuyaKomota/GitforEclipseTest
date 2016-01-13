package komota.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import komota.main.MyPR;
import komota.main.MySerialFrame;

public class CheckFrame extends MySerialFrame{

	public static void main(String[] args){
		CheckFrame frame = new CheckFrame();
	}



	MyPR pr;
	int idx = 0;
	int[][] goal;


	//コンストラクタ
	public CheckFrame(){
		this.goal = new int[100][2];
		File file = new File("log/output_W.txt");
		BufferedReader br =  null;
		try {
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		String line;
		int count = 0;
		while(true){
			line = null;
			try {
				line = br.readLine();
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
			if(line == null){
				break;
			}
			if(line.split(",")[0].equals("moved ") == true){
				this.goal[count][0] = Integer.parseInt(line.split(",")[1]);
				this.goal[count][1] = Integer.parseInt(line.split(",")[2]);
				count++;
			}
		}
		try {
			br.close();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

	@Override
	public void functionPlugin1(){
		//this.pr = new MyPR("ERROR_W/output_W_each50.txt");
		this.pr = new MyPR("output_W.txt");
	}
	@Override
	public void functionPlugin2(){
		this.idx++;
		System.out.println("index:"+idx);
		this.pr.arrangeField(this, this.pr.logdata[idx]);
		this.setSecondSelected(this.goal[idx]);
	}
	@Override
	public void functionPlugin3(){
		this.idx--;
		System.out.println("index:"+idx);
		this.pr.arrangeField(this, this.pr.logdata[idx]);
		this.setSecondSelected(this.goal[idx]);
	}

}
