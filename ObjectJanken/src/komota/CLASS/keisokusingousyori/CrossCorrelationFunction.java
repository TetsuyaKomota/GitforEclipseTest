package komota.CLASS.keisokusingousyori;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class CrossCorrelationFunction {


	static String filename1 = null;
	static String filename2 = null;

	public static void main(String[] args){
		CrossCorrelationFunction func = new CrossCorrelationFunction();
		func.popup();
		while(CrossCorrelationFunction.filename1 == null || CrossCorrelationFunction.filename2 == null){
			//System.out.println("おまんこ");
		}
		//System.out.println(CrossCorrelationFunction.filename1);
		//System.out.println(CrossCorrelationFunction.filename2);

		func.input(CrossCorrelationFunction.filename1, CrossCorrelationFunction.filename2);

		//System.out.println("ここは言ってる");

		func.output("result_C_"+CrossCorrelationFunction.filename1+","+CrossCorrelationFunction.filename2+".csv");

		//System.out.println("ここは行ってる"+func.numofdata);

		for(int t=0;t<func.numofdata;t++){
			func.pw.println(t+","+func.calcCrossCorrelationFunction(t));
			//System.out.println("いぐぅぅ！！");
		}

		//System.out.println("ここはイってる");

		func.pw.close();
		func.popupcomp();
		//System.out.println("おわったよぉ～！");

	}

	//干潮データ
	int[] data1;
	int[] data2;

	//データ量
	int numofdata;

	//ファイルリーダ
	BufferedReader br1;
	//ファイルリーダ
	BufferedReader br2;
	//ファイルライタ
	PrintWriter pw;

	//フラグ
	static boolean flag = false;

	//ファイル読み込み
	void input(String filename_1,String filename_2){
		File file = new File("dataset/"+filename_1);
		for(int i=0;i<1000;i++){
			try {
				this.br1 = new BufferedReader(new FileReader(file));
			} catch (FileNotFoundException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
			if(this.br1 != null){
				break;
			}
		}
		file = new File("dataset/"+filename_2);
		for(int i=0;i<1000;i++){
			try {
				this.br2 = new BufferedReader(new FileReader(file));
			} catch (FileNotFoundException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
			if(this.br2 != null){
				break;
			}
		}
		if(this.br1 != null && this.br2 != null){
			//System.out.println("ファイルは正常に読み込めました");

			String templine = null;
			int idx  = 0;
			this.data1 = new int[7500];
			while(true){
				templine = null;
				try {
					templine = br1.readLine();
				} catch (IOException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}
				if(templine == null){
					break;
				}
				for(int t=0;t<24;t++){
					this.data1[idx] = Integer.parseInt(templine.substring(t*3,t*3+3).trim());
					idx++;
				}
			}
			//EOF代わり
			this.data1[idx] = -999;

			templine = null;
			idx  = 0;
			this.data2 = new int[7500];
			while(true){
				templine = null;
				try {
					templine = br2.readLine();
				} catch (IOException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}
				if(templine == null){
					break;
				}
				for(int t=0;t<24;t++){
					this.data2[idx] = Integer.parseInt(templine.substring(t*3,t*3+3).trim());
					idx++;
				}
			}
			//EOF代わり
			this.data2[idx] = -999;

			//二つのデータのうち，少ないほうのデータ量をnumofdataに入れる
			int tempidx = 0;
			while(data1[tempidx] >= 0){
				tempidx++;
			}
			if(idx>tempidx){
				this.numofdata = tempidx;
			}
			else{
				this.numofdata = idx;
			}
		}
		else{
			//System.out.println("ファイルの読み込みに失敗しました．ファイル名を確認してください.");

		}
	}

	//τを入力するとCrossCovarianceを計算する
	double calcCovariance(int tau){
		double output = 0;

		int idx = 0;

		while(true){
			if(this.data2[idx+tau] < 0){
				break;
			}
			output += this.data1[idx]*this.data2[idx+tau];
			idx++;
		}
		output /= idx;

		return output;
	}
	//分散を求める
	double getV(int d){
		double output = 0;
		double E = 0;
		double Q = 0;

		int idx = 0;

		int[] data;

		if(d == 1){
			data = this.data1;
		}
		else{
			data = this.data2;
		}

		while(true){
			if(data[idx] < 0){
				break;
			}
			E += data[idx];
			Q += data[idx] * data[idx];
			idx++;
		}
		E /= idx;
		Q /= idx;

		output = Q - E * E;

		return output;
	}
	//τを与えるとAutoCorrelationFunctionを出力する
		double calcCrossCorrelationFunction(int tau){
			double output = 0;

			output = this.calcCovariance(tau);

			output /= Math.sqrt(this.getV(1));
			output /= Math.sqrt(this.getV(2));

			return output;
		}




	//ポップアップ
	public void popup(){
		TextBox box = new TextBox();
	}
	//官僚ポップアップ
	void popupcomp(){
		CompleteBox box = new CompleteBox();
		while(true){

		}

	}
	//出力ファイルを用意する
	void output(String filename){
		try {
			FileOutputStream fos = new FileOutputStream("result/"+filename,true);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			this.pw = new PrintWriter(osw);
		} catch (FileNotFoundException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}



	class TextBox extends JFrame{

		JTextArea text1 = null;
		JTextArea text2 = null;

		TextBox(){
			setTitle("ファイル名を入力してください");
			setBounds(100,100,300,150);
			this.setLocationRelativeTo(null);

			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			JPanel p = new JPanel();

			JLabel label1 = new JLabel();

			label1.setText("データセットのファイル名を入力してください");
			this.text1 = new JTextArea(1,20);
			this.text2 = new JTextArea(1,20);

			JButton OK = new JButton("OK");
			OK.addActionListener(new MyActionListener());

			p.add(label1);
			p.add(text1);
			p.add(text2);
			p.add(OK);

			Container contentPane = this.getContentPane();
			contentPane.add(p,BorderLayout.CENTER);

			this.setVisible(true);

		}

		class MyActionListener implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO 自動生成されたメソッド・スタブ
				CrossCorrelationFunction.filename1 = TextBox.this.text1.getText();
				CrossCorrelationFunction.filename2 = TextBox.this.text2.getText();
				AutoCorrelationFunction.flag = false;
				TextBox.this.dispose();
			}

		}

	}



	class CompleteBox extends JFrame{

		JTextArea text = null;

		CompleteBox(){
			setTitle("ファイルを書き出しました");
			setBounds(100,100,300,250);
			setSize(700,100);
			this.setLocationRelativeTo(null);
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			JPanel p = new JPanel();

			JLabel label1 = new JLabel();

			label1.setText("ファイルを書き出しました．resultフォルダを確認してください");

			p.add(label1);

			Container contentPane = this.getContentPane();
			contentPane.add(p,BorderLayout.CENTER);

			this.setVisible(true);

		}
	}


}
