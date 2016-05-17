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

public class AutoCorrelationFunction {
	/* ******************************************************************************************************************* */
	/* ******************************************************************************************************************* */
	/* ******************************************************************************************************************* */
	/* ******************************************************************************************************************* */
	/* ******************************************************************************************************************* */
	/* ******************************************************************************************************************* */
	//データセットのファイル名．ここだけいじればいいよ
	static String filename = null;
	/* ******************************************************************************************************************* */
	/* ******************************************************************************************************************* */
	/* ******************************************************************************************************************* */
	/* ******************************************************************************************************************* */
	/* ******************************************************************************************************************* */
	/* ******************************************************************************************************************* */

	public static void main(String[] args){
		AutoCorrelationFunction func = new AutoCorrelationFunction();
		func.popup();
		while(AutoCorrelationFunction.filename == null){
			System.out.println(AutoCorrelationFunction.filename);
		}
		System.out.println("ここにはきたよ！");

		func.input(AutoCorrelationFunction.filename);
		func.show();
		func.output("result_"+AutoCorrelationFunction.filename+".csv");

		for(int t=0;t<func.numofdata;t++){
			func.pw.println(t+","+func.calcAutoCorrelationFunction(t));
		}
		func.pw.close();
		func.popupcomp();
		System.out.println("おわったよぉ～！");


	}

	//干潮データ
	int[] data;

	//データ量
	int numofdata;

	//ファイルリーダ
	BufferedReader br;
	//ファイルライタ
	PrintWriter pw;

	//フラグ
	static boolean flag = false;

	//ファイル読み込み
	void input(String filename){
		File file = new File("dataset/"+filename);
		for(int i=0;i<1000;i++){
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
			System.out.println("ファイルは正常に読み込めました");

			String templine = null;
			int idx  = 0;
			this.data = new int[7500];
			while(true){
				templine = null;
				try {
					templine = br.readLine();
				} catch (IOException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}
				if(templine == null){
					break;
				}
/*
				String[] tempdata = templine.split(",");
				for(int i=0;i<tempdata.length;i++){
					this.data[idx] = Integer.parseInt(tempdata[i]);
					idx++;
				}
*/
				for(int t=0;t<24;t++){
					this.data[idx] = Integer.parseInt(templine.substring(t*3,t*3+3).trim());
					idx++;
				}
			}
			//EOF代わり
			this.data[idx] = -999;
			this.numofdata = idx;
		}
		else{
			System.out.println("ファイルの読み込みに失敗しました．ファイル名を確認してください.");

		}
	}
	//τを入力するとAutoCovarianceを計算する
	double calcAutoCovariance(int tau){
		double output = 0;

		int idx = 0;

		while(true){
			if(this.data[idx+tau] < 0){
				break;
			}
			output += this.data[idx]*this.data[idx+tau];
			idx++;
		}
		output /= idx;

		return output;
	}
	//分散を求める
	double getV(){
		double output = 0;
		double E = 0;
		double Q = 0;

		int idx = 0;

		while(true){
			if(this.data[idx] < 0){
				break;
			}
			E += this.data[idx];
			Q += this.data[idx] * this.data[idx];
			idx++;
		}
		E /= idx;
		Q /= idx;

		output = Q - E * E;

		return output;
	}

	//τを与えるとAutoCorrelationFunctionを出力する
	double calcAutoCorrelationFunction(int tau){
		double output = 0;

		output = this.calcAutoCovariance(tau) / this.getV();

		return output;
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

	//ポップアップ
	void popup(){
		TextBox box = new TextBox();
		AutoCorrelationFunction.flag = true;
		System.out.println("ここにきたよ！");
	}
	//官僚ポップアップ
	void popupcomp(){
		CompleteBox box = new CompleteBox();
		AutoCorrelationFunction.flag = true;
		while(AutoCorrelationFunction.flag){

		}
	}

	//デバッグ用
	void show(){
		int count = 0;
		System.out.println("以下のデータが読み込まれています");
		while(this.data[count] > 0){
			System.out.print(" "+this.data[count]);
			count++;
		}
		System.out.println();
		System.out.println("データ数は "+this.numofdata+",分散は  "+this.getV()+"です");
	}



	class TextBox extends JFrame{

		JTextArea text = null;

		TextBox(){
			setTitle("ファイル名を入力してください");
			setBounds(100,100,300,250);
			this.setLocationRelativeTo(null);

			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			JPanel p = new JPanel();

			JLabel label1 = new JLabel();

			label1.setText("データセットのファイル名を入力してください");
			this.text = new JTextArea(6,20);

			JButton OK = new JButton("OK");
			OK.addActionListener(new MyActionListener());

			p.add(label1);
			p.add(text);
			p.add(OK);

			Container contentPane = this.getContentPane();
			contentPane.add(p,BorderLayout.CENTER);

			this.setVisible(true);

		}

		class MyActionListener implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO 自動生成されたメソッド・スタブ
				AutoCorrelationFunction.filename = TextBox.this.text.getText();
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
