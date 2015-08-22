package komota.pr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestPatternRecognition {

	//定数
	//絶対移動の閾値
	final double THRESHOLD = 0.9;
	//絶対移動ベクトルの学習率
	final double rate = 0.3;

	//フィールド
	File file;
	BufferedReader br;
	String file_name = "test3.txt";
	//絶対移動ベクトル
	double[] vec;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ

		TestPatternRecognition test = new TestPatternRecognition();
		test.testOutput();
		test.close();

	}


	//コンストラクタ
	public TestPatternRecognition(){
		this.file = new File("log/"+file_name);
		try {
			this.br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		this.vec = new double[2];
		this.vec[0] = 0;
		this.vec[1] = 1;
	}

	//クローズ処理
	public void close(){
		try {
			this.br.close();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

	//とりあえず、最終行まで出力するテスト
	public void testOutput(){
		while(true){
			String line = null;
			try {
				line = br.readLine();
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
			if(line != null){
				System.out.println(line);
				updateVec(line);
			}
			else{
				break;
			}
		}
	}
	//データをもとに、絶対移動する方向を推測する。
	/*
	 * テスト用なので、前提として
	 * 1. 赤を動かすことを知っている
	 * 2. 絶対移動であることを知っている
	 * 3. ３マス平方の離散空間である
	 */
	public String[] updateVec(String input){

		String regex_start = "\\*\\*start:";
		String regex_goal = "\\*\\*goal:";
		String regex_change = "  change:";
		String regex_status = "status:";

		Pattern pt_start = Pattern.compile(regex_start);
		Pattern pt_goal = Pattern.compile(regex_goal);
		Pattern pt_change = Pattern.compile(regex_change);
		Pattern pt_status = Pattern.compile(regex_status);

		Matcher m_start = pt_start.matcher(input);
		Matcher m_goal = pt_goal.matcher(input);
		Matcher m_change = pt_change.matcher(input);
		Matcher m_status = pt_status.matcher(input);


		//「  change:s,t」という文字列から「s」と「t」を取得してtemp2に格納する
		if(m_change.find()){
			System.out.println("Matched.");
			String temp = input.split(regex_change)[1];
			String[] temp2 = temp.split(",");
			System.out.println("picked out "+temp2[0] + " and "+ temp2[1]);
			return temp2;
		}
		//次の行の「status:p,p,p,p,p,p,p,p,p」という文字列からtemp3に状態を格納する。
		else if(m_status.find()){
			String temp = input.split(regex_status)[1];
			String[] temp2 = temp.split(",");
			System.out.println("picked out "+temp2[0] + " and "+ temp2[1]);
			return temp2;
		}
		//temp2のどっちからどっちに、temp3の「１」が移動しているかを取得し、移動後の「１」の位置をtemp4に格納する
		//temp2とtemp4からベクトルを取得し、学習率をかけてvecを更新する
		return null;

	}

}
