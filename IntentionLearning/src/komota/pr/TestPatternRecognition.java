package komota.pr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import komota.main.MainFrame;

public class TestPatternRecognition {

	//定数
	//絶対移動の閾値
	final double THRESHOLD = 0.9;
	//絶対移動ベクトルの学習率
	final double RATE = 0.3;

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
		this.vec[1] = 0;
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

	//とりあえず、ゴールを出力するテスト
	public void testConvert(MainFrame frame){
		//vecを最も近い整数に変換する
		int[] intvec = new int[2];
		intvec[0] = (int)Math.round(vec[0]);
		intvec[1] = (int)Math.round(vec[1]);
		for(int i = 0;i<frame.getPanels().length;i++){
			if(frame.getPanels()[i].getStatus() == 1){
				if(
					frame.getPanels()[i].getPosition()[0]+intvec[0] >= 0
				&&	frame.getPanels()[i].getPosition()[0]+intvec[0] <= 2
				&&	frame.getPanels()[i].getPosition()[1]+intvec[1] >= 0
				&&	frame.getPanels()[i].getPosition()[1]+intvec[1] <= 2
				){
					frame.setSelected(i).setSecondSelected(i+intvec[1]*3 + intvec[0]);
				}
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
			String[] temppos1 = temp2[0].split(" ");
			String[] temppos2 = temp2[1].split(" ");

			System.out.println("picked out "+temp2[0] + " and "+ temp2[1]);
			System.out.println("temppos1 = " + temppos1[0] + " " + temppos1[1] + "  temppos2 = " + temppos2[0] + " " + temppos2[1]);

			int[] pos1 = new int[2];
			int[] pos2 = new int[2];

			pos1[0] = Integer.parseInt(temppos1[0]);
			pos1[1] = Integer.parseInt(temppos1[1]);
			pos2[0] = Integer.parseInt(temppos2[0]);
			pos2[1] = Integer.parseInt(temppos2[1]);

			//changeで選択したマスのポジションを使ってベクトルを更新する
			double[] tempvec = new double[2];
			tempvec[0] = pos2[0] - pos1[0];
			tempvec[1] = pos2[1] - pos1[1];

			this.vec[0] = (1 - RATE)*vec[0] + RATE * tempvec[0];
			this.vec[1] = (1 - RATE)*vec[1] + RATE * tempvec[1];
			System.out.println("vec = ("+vec[0]+","+vec[1]+")");
			return temp2;
		}
		//temp2のどっちからどっちに、temp3の「１」が移動しているかを取得し、移動後の「１」の位置をtemp4に格納する
		//temp2とtemp4からベクトルを取得し、学習率をかけてvecを更新する
		return null;

	}

}
