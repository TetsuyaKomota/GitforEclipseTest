package komota.lib;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class MyIO {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		MyIO io = new MyIO();
		io.writeFile("test.txt");

		io.close();
	}

	//フィールド
	//読みこみファイル名
	String file_IN;
	//書き出しファイル名
	String file_OUT;

	//読みこみファイル
	BufferedReader br = null;
	//書き出しファイル
	PrintWriter pw = null;


	//コンストラクタ
	public MyIO(){
	}

	//ファイル読み込み準備
	public boolean readFile(String file_IN){

		//読みこみファイル名取得
		this.file_IN = file_IN;

		//読みこみファイルを開く
		File file = new File("log/"+this.file_IN);
		for(int t=0;t<1000;t++){
			try {
				this.br = new BufferedReader(new FileReader(file));
			} catch (FileNotFoundException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
			if(this.br != null){
				break;
			}
		}

		if(this.br != null){
			return true;
		}
		else{
			return false;
		}
	}

	//ファイル書き出し準備
	public boolean writeFile(String file_OUT){

		//書きだしファイル名取得
		this.file_OUT = file_OUT;
		try {
		      FileOutputStream fos = new FileOutputStream("log/"+this.file_OUT,true);
		      OutputStreamWriter osw = new OutputStreamWriter(fos);
		      this.pw = new PrintWriter(osw);
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			System.out.println("ファイルなし");
			e.printStackTrace();
		}

		if(this.pw != null){
			return true;
		}
		else{
			return false;
		}
	}

	//ファイル行読み込み
	public String readLine(){
		String line = null;
		try {
			line = br.readLine();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return line;
	}

	//ファイル行書き出し
	public void println(String line){
		if(this.pw != null){
			this.pw.println(line);
		}
	}
	public void print(String str){
		if(this.pw != null){
			this.pw.print(str);
		}
	}

	//ファイル書き出し完了処理
	//（なぜか）クローズ処理を実行しないと書きだされないため，一度クローズしてすぐさま同じファイルを開き直す．
	//おそらく冗長な方法なので，連発はしないこと
	public void execute(){
		if(this.pw != null){
			this.pw.close();
		}
		this.writeFile(this.file_OUT);
	}

	//クローズ処理
	public void close(){
		try {
			if(this.br != null){
				this.br.close();
			}
			if(this.pw != null){
				this.pw.close();
			}
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

}
