package komota.libs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.DecimalFormat;

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

	//出力ストリームのゲッター
	public PrintWriter getPrintWriter(){
		return this.pw;
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

	//ファイル行列読み込み
	//引数に与えた行列IDが振られた行列を読みこみファイルから探す．
	/*
	 * ファイルは以下のようにフォーマットされているとする
	 * Mat_ID:(int id)
	 * dim:(int dimension)
	 * x00,x01,...,x0n
	 * x10,x11,...,x1n
	 * .
	 * .
	 * .
	 * xm0,xm1,..,xmn
	 * end_Mat:(int id)
	 *
	 * タブは使用可能
	 */
	public MyMatrix readMatrix(int Mat_ID){
		double[][] read = null;
		int dim = 0;

		//ファイルを読みこみ直す
		BufferedReader tempbr = null;
		File file = new File("log/"+this.file_IN);
		for(int t=0;t<1000;t++){
			try {
				tempbr = new BufferedReader(new FileReader(file));
			} catch (FileNotFoundException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
			if(this.br != null){
				break;
			}
		}
		//与えられた行列番号を探す
		String line = null;
		while(true){
			line = null;
			try {
				line = tempbr.readLine();
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
			if(line == null){
				return null;
			}
			else if(line.split(":")[0].equals("Mat_ID") && Integer.parseInt(line.split(":")[1]) == Mat_ID){
				break;
			}
		}
		//Mat_IDの次はdimがかかれているはずなので，それを読み取る
		line = null;
		try {
			line = tempbr.readLine();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		if(line == null){
			return null;
		}
		else if(line.split(":")[0].equals("dim")){
			dim = Integer.parseInt(line.split(":")[1]);
			read = new double[dim][dim];
		}
		else{
			return null;
		}
		//endが来るまで読み取り
		int count = 0;
		while(true){
			line = null;
			try {
				line = tempbr.readLine();
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
			if(line == null){
				return null;
			}
			else if(line.split(":").equals("end_Mat") && Integer.parseInt(line.split(":")[1]) == Mat_ID){
				break;
			}
			else if(count >= dim){
				break;
			}
			String[] splitted = line.split(",");
			for(int idx=0;idx<dim;idx++){
				read[count][idx] = Double.parseDouble(splitted[idx].replace("	", ""));
			}
			count++;
		}

		try {
			tempbr.close();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

		MyMatrix output = new MyMatrix(dim,read);
		return output;
	}

	//ファイル行書き出し
	public void println(String line){
		if(this.pw != null){
			this.pw.println(line);
		}
	}
	public void println(int integer){
		if(this.pw != null){
			this.pw.println(integer);
		}
	}
	public void println(double Double){
		if(this.pw != null){
			this.pw.println(Double);
		}
	}
	public void println(){
		if(this.pw != null){
			this.pw.println();
		}
	}

	//ファイル書き出し
	public void print(String str){
		if(this.pw != null){
			this.pw.print(str);
		}
	}
	public void print(int integer){
		if(this.pw != null){
			this.pw.print(integer);
		}
	}
	public void print(double Double){
		if(this.pw != null){
			this.pw.print(Double);
		}
	}

	//ファイル行列書き出し
	public void printMatrix(MyMatrix mat,int id){
		this.println("Mat_ID:"+id);
		this.println("dim:"+mat.dimension);
		for(int i=0;i<mat.dimension;i++){
			for(int j=0;j<mat.dimension;j++){
				this.print(mat.getData(i,j)+"\t");
				if(j<mat.dimension-1){
					this.print(",");
				}
			}
			this.println("");
		}
		this.println("end_Mat:"+id);
		this.execute();
	}
	//ファイル行列書き出し（概数化）
	public void printMatrix_approximately(MyMatrix mat,int id){
		DecimalFormat df = new DecimalFormat("0.00");
		this.println("Mat_ID:"+id);
		this.println("dim:"+mat.dimension);
		for(int i=0;i<mat.dimension;i++){
			for(int j=0;j<mat.dimension;j++){
				this.print(df.format(mat.getData(i,j))+"\t");
				if(j<mat.dimension-1){
					this.print(",");
				}
			}
			this.println("");
		}
		this.println("end_Mat:"+id);
		this.execute();
	}
	//ファイル行列書き出し（整数化）
	public void printMatrix_integer(MyMatrix mat,int id){
		this.println("Mat_ID:"+id);
		this.println("dim:"+mat.dimension);
		for(int i=0;i<mat.dimension;i++){
			for(int j=0;j<mat.dimension;j++){
				this.print((int)mat.getData(i,j)+"\t");
				if(j<mat.dimension-1){
					this.print(",");
				}
			}
			this.println("");
		}
		this.println("end_Mat:"+id);
		this.execute();
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
