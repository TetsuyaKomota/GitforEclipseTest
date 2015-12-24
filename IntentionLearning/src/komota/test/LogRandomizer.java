package komota.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class LogRandomizer {

	public static void main(String[] args){
		LogRandomizer r = new LogRandomizer();
		r.randomize("log_RIGHT_TO_BLUE.txt","outputfromRandomizer.txt");
	}
	//出力先ファイル
	File file_out;
	String file_name_out = "outputfromRandomizer.txt";
	PrintWriter pw;

	//入力元ファイル
	File file_in;
	BufferedReader br;
	String file_name_in = "logdata.txt";


	//行ごとのデータ
	Log[] data;
	//データ最大量
	final int MAXSIZE = 1000;

	//データのインデックス数
	int dataindex = 0;

	//データを読み取ってランダマイズして標準出力
	public void randomize(String in,String out){

		this.data = new Log[MAXSIZE];

		this.file_in = new File("log/"+in);
		this.file_out = new File("log/"+out);
		try {
		      FileOutputStream fos = new FileOutputStream("log/"+out,true);
		      OutputStreamWriter osw = new OutputStreamWriter(fos);
		      this.pw = new PrintWriter(osw);
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			System.out.println("ファイルなし");
			e.printStackTrace();
		}
		this.file_in = new File("log/"+in);
		try {
			this.br = new BufferedReader(new FileReader(file_in));
		} catch (FileNotFoundException e) {
			// TODO 自動生成された catch ブロック
			System.out.println("うんこ");
			e.printStackTrace();
		}
		int num = 0;
		int dataindex = 0;
		while(num < MAXSIZE){
			String line = null;
			try {
				line = br.readLine();
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
				System.out.println("ちんこ");
			}
			if(line == null){
				break;
			}
			data[num] = new Log(line,dataindex);
			if(line.split(",")[0].equals("goal  ")){
				dataindex++;
			}
			num++;
		}
		try {
			br.close();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		this.dataindex = dataindex;

		//読み取ったデータをランダマイズ
		boolean[] isoutlist = new boolean[this.dataindex];
		for(int i=0;i<isoutlist.length;i++){
			isoutlist[i] = false;
		}
		int check = 0;
		while(check < this.dataindex){
			int rand = (int)(this.dataindex * Math.random());
			if(isoutlist[rand] == false){
				for(int i=0;i<this.data.length;i++){
					if(data[i] != null && data[i].index == rand){
						pw.println(data[i].data);
						data[i].isout = true;
					}
				}
				isoutlist[rand] = true;
				check++;
			}
		}
		pw.close();
	}

	//ログデータ。データ内容とセット番号、出力済みかどうかを持つ
	class Log{
		String data;
		int index;
		boolean isout;

		//コンストラクタ
		Log(String data , int index){
			this.data = data;
			this.index = index;
			this.isout = false;
		}
	}
}
