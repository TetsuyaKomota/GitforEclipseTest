package komota.main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;



public class MyFrame extends JFrame{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		MyFrame frame = new MyFrame();
	}

	//バッファストラテジ
	public BufferStrategy buffer;

	//パネル
	//空間的な2次元配列は全て[行][列]に統一！！！！
	public MyPanel[][] panels;

	//パネルの行、列数
	public static final int NUMBEROFPANEL = 200;
	//選択パネル枠の太さ
	static final float FRAME_SIZE_OF_SELECTED_PANEL = 3f;

	//選択状態パネル
	int[] selected = new int[2];
	int[] secondselected = new int[2];

	//説明文などの文字列出力。アンケートなどで使用する
	public String expranation = "";

	//タスク表示
	public String tasktitle = "tasktitle";

	//操作方法表示
	public String howtouse = "SPACE:exchange the first clicked and the second clicked.   G:finish the task.";

	//出力先ファイル
	File file;
	String file_name = "test.txt";
	PrintWriter pw;

	//コンストラクタ
	public MyFrame(){
		this.setTitle("IntentionLearning");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.setSize(1000,1000);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setVisible(true);

		this.createBufferStrategy(2);
		this.buffer = this.getBufferStrategy();
		this.setIgnoreRepaint(true);

		this.panels = new MyPanel[MyFrame.NUMBEROFPANEL][MyFrame.NUMBEROFPANEL];
		MyPanel.SIZE_PANEL = ((this.getWidth() - MyPanel.SIZE_FRAME * 2 + MyPanel.SIZE_SEPALATOR ) / MyFrame.NUMBEROFPANEL ) - MyPanel.SIZE_SEPALATOR;

		for(int i=0;i<this.panels.length;i++){
			for(int j=0;j<this.panels[0].length;j++){
				int[] position = new int[2];
				position[0] = i;
				position[1] = j;
				this.panels[i][j] = new MyPanel(0,position);
			}
		}
		this.selected[0] = -1;
		this.selected[1] = -1;
		this.secondselected[0] = -1;
		this.secondselected[1] = -1;

		this.addKeyListener(new MyKeyListener());
		this.addMouseListener(new MyMouseListener());

		this.file = new File("log/"+file_name);
		try {
		      FileOutputStream fos = new FileOutputStream("log/"+file_name,true);
		      OutputStreamWriter osw = new OutputStreamWriter(fos);
		      this.pw = new PrintWriter(osw);
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			System.out.println("ファイルなし");
			e.printStackTrace();
		}
		Timer t = new Timer();
		t.schedule(new RenderTask(), 0,200);

		this.initialize();
	}

	//各種セッター、ゲッター
	public MyPanel[][] getPanels(){
		return this.panels;
	}
	public MyFrame setSelected(int[] selected){
		this.selected = selected;
		return this;
	}
	public MyFrame setSecondSelected(int[] selected){
		this.secondselected = selected;
		return this;
	}
	public int[] getSecondSelected(){
		return this.secondselected;
	}

	//出力先ファイル名を変更
	public void setOutputFile(){
		this.file = new File("log/"+file_name);
		try {
		      FileOutputStream fos = new FileOutputStream("log/"+file_name,true);
		      OutputStreamWriter osw = new OutputStreamWriter(fos);
		      this.pw = new PrintWriter(osw);
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			System.out.println("ファイルなし");
			e.printStackTrace();
		}
	}
	public void setOutputFile(String file_name){
		this.file_name = file_name;
		setOutputFile();
	}



	//Gを押した時に起こす処理
	public void pushGoal(){
		this.outputGoal();
		this.selected[0] = -1;
		this.selected[1] = -1;
		this.secondselected[0] = -1;
		this.secondselected[1] = -1;
		this.initialize();
	}

	//初期化
	public void initialize(){

	}

	//初期化時に初期状態を出力する
	public void outputStart(){
		pw.print("start ,");
		for(int i=0;i<MyFrame.this.panels.length;i++){
			for(int j=0;j<MyFrame.this.panels[0].length;j++){
				pw.print(MyFrame.this.panels[i][j].status);
				if(i<MyFrame.this.panels.length-1 || j<MyFrame.this.panels.length-1){
					pw.print(",");
				}
			}
		}
		pw.println();
	}

	//ゴール時に最終状態を出力する
	public void outputGoal(){
		pw.print("goal  ,");
		for(int i=0;i<MyFrame.this.panels.length;i++){
			for(int j=0;j<MyFrame.this.panels[0].length;j++){
				pw.print(MyFrame.this.panels[i][j].status);
				if(i<MyFrame.this.panels.length-1 || j<MyFrame.this.panels.length-1){
					pw.print(",");
				}
			}
		}
		pw.println();
	}

	//計算結果などをresult.txtに出力
	public void printResult(String out){
		this.pw.print(out);
	}
	public void printlnResult(String out){
		this.pw.println(out);
	}
	//パネルの描画以外の特別な描画を行いたい場合はここをオーバーライド
	public void draw(){
	}

	//各サブクラスでキーに反応する機能を追加したい場合にオーバーライドする
	public void functionPlugin1(){
		System.out.println("No Function in This Key.");
	}
	public void functionPlugin2(){
		System.out.println("No Function in This Key.");
	}
	public void functionPlugin3(){
		System.out.println("No Function in This Key.");
	}
	public void functionPlugin4(){
		System.out.println("No Function in This Key.");
	}
	public void functionPlugin5(){
		System.out.println("No Function in This Key.");
	}
	public void functionPlugin6(){
		System.out.println("No Function in This Key.");
	}
	public void functionPlugin7(){
		System.out.println("No Function in This Key.");
	}
	public void functionPlugin8(){
		System.out.println("No Function in This Key.");
	}
	public void functionPlugin9(){
		System.out.println("No Function in This Key.");
	}

	//タイマータスク
	class RenderTask extends TimerTask{

		@Override
		public void run() {
			// TODO 自動生成されたメソッド・スタブ
			Graphics2D g = (Graphics2D)MyFrame.this.buffer.getDrawGraphics();
			//背景の描画
			g.setColor(new Color(240,240,240));
			g.fillRect(0, 0, MyFrame.this.getWidth(), MyFrame.this.getHeight());

			g.setColor(Color.black);
			g.drawString(MyFrame.this.expranation, MyPanel.SIZE_FRAME, MyPanel.SIZE_FRAME/2 + 10);
			g.drawString(MyFrame.this.tasktitle, MyPanel.SIZE_FRAME, MyPanel.SIZE_FRAME/2 + 30);
			g.drawString(MyFrame.this.howtouse, MyPanel.SIZE_FRAME, MyPanel.SIZE_FRAME+MyFrame.NUMBEROFPANEL*(MyPanel.SIZE_PANEL+MyPanel.SIZE_SEPALATOR)+40);

			g.dispose();

			for(int i=0;i<MyFrame.this.panels.length;i++){
				for(int j=0;j<MyFrame.this.panels[0].length;j++){
					MyFrame.this.panels[i][j].draw(MyFrame.this);
				}
			}

			g = (Graphics2D)MyFrame.this.buffer.getDrawGraphics();
			BasicStroke wideStroke = new BasicStroke(MyFrame.FRAME_SIZE_OF_SELECTED_PANEL);
			g.setStroke(wideStroke);
			if(MyFrame.this.selected[0] >= 0 && MyFrame.this.selected[1] >= 0){
				g.setColor(Color.black);
				g.drawRect(MyPanel.SIZE_FRAME+(MyPanel.SIZE_PANEL+MyPanel.SIZE_SEPALATOR)*(MyFrame.this.selected[1]) + (int)MyFrame.FRAME_SIZE_OF_SELECTED_PANEL/2,MyPanel.SIZE_FRAME+(MyPanel.SIZE_PANEL+MyPanel.SIZE_SEPALATOR)*(MyFrame.this.selected[0])+ (int)MyFrame.FRAME_SIZE_OF_SELECTED_PANEL/2,MyPanel.SIZE_PANEL - (int)MyFrame.FRAME_SIZE_OF_SELECTED_PANEL,MyPanel.SIZE_PANEL - (int)MyFrame.FRAME_SIZE_OF_SELECTED_PANEL);
			}
			if(MyFrame.this.secondselected[0] >= 0 && MyFrame.this.secondselected[1] >= 0){
				g.setColor(new Color(255,100,200));
				g.drawRect(MyPanel.SIZE_FRAME+(MyPanel.SIZE_PANEL+MyPanel.SIZE_SEPALATOR)*(MyFrame.this.secondselected[1]),MyPanel.SIZE_FRAME+(MyPanel.SIZE_PANEL+MyPanel.SIZE_SEPALATOR)*(MyFrame.this.secondselected[0]),MyPanel.SIZE_PANEL,MyPanel.SIZE_PANEL);
			}
			g.dispose();
			MyFrame.this.draw();
			MyFrame.this.buffer.show();
		}
	}

	//キーリスナー
	class MyKeyListener implements KeyListener{

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO 自動生成されたメソッド・スタブ

		}

		@Override
		public void keyPressed(KeyEvent e) {
			// TODO 自動生成されたメソッド・スタブ
			if(e.getKeyCode() == KeyEvent.VK_SPACE){
				if(MyFrame.this.selected[0] != -1 && MyFrame.this.selected[1] != -1 && MyFrame.this.secondselected[0] != -1 && MyFrame.this.secondselected[1] != -1){
					int temp = MyFrame.this.panels[selected[0]][selected[1]].getStatus();
					MyFrame.this.panels[selected[0]][selected[1]].setStatus(MyFrame.this.panels[secondselected[0]][secondselected[1]].getStatus());
					MyFrame.this.panels[secondselected[0]][secondselected[1]].setStatus(temp);
					//下記、changeログとstatusログは現状使用していないため、使用するまでコメントアウト
					//→statusログはTaskのreproductionで使用するため、コメントアウトを外す
/*
					MyFrame.this.pw.print("change,");
					for(int i=0;i<MyFrame.this.panels.length;i++){
						for(int j=0;j<MyFrame.this.panels[0].length;j++){
							if(i == selected[0] && j == selected[1]){
								pw.print("1");
							}
							else if(i == secondselected[0] && j == secondselected[1]){
								pw.print("2");
							}
							else{
								pw.print("0");
							}
							if(i<MyFrame.this.panels.length-1 || j<MyFrame.this.panels.length-1){
								pw.print(",");
							}
						}
					}
					pw.println();
*/
/*
					MyFrame.this.pw.print("status,");
					for(int i=0;i<MyFrame.this.panels.length;i++){
						for(int j=0;j<MyFrame.this.panels[0].length;j++){
							pw.print(MyFrame.this.panels[i][j].status);
							if(i<MyFrame.this.panels.length-1 || j<MyFrame.this.panels.length-1){
								pw.print(",");
							}
						}
					}
					pw.println();
*/
					MyFrame.this.selected[0] = -1;
					MyFrame.this.selected[1] = -1;
					MyFrame.this.secondselected[0] = -1;
					MyFrame.this.secondselected[1] = -1;
				}
			}
			else if(e.getKeyCode() == KeyEvent.VK_Z){
				if(MyFrame.this.secondselected[0] != -1 || MyFrame.this.secondselected[1] != -1){
					MyFrame.this.secondselected[0] = -1;
					MyFrame.this.secondselected[1] = -1;
				}
				else if(MyFrame.this.selected[0] != -1 || MyFrame.this.selected[1] != -1){
					MyFrame.this.selected[0] = -1;
					MyFrame.this.selected[1] = -1;
				}
			}
			else if(e.getKeyCode() == KeyEvent.VK_G){
				MyFrame.this.pushGoal();
			}
			else if(e.getKeyCode() == KeyEvent.VK_NUMPAD0
					||e.getKeyCode() == KeyEvent.VK_NUMPAD1
					||e.getKeyCode() == KeyEvent.VK_NUMPAD2
					||e.getKeyCode() == KeyEvent.VK_NUMPAD3
					||e.getKeyCode() == KeyEvent.VK_NUMPAD4
					||e.getKeyCode() == KeyEvent.VK_NUMPAD5
					||e.getKeyCode() == KeyEvent.VK_NUMPAD6
					||e.getKeyCode() == KeyEvent.VK_NUMPAD7
					||e.getKeyCode() == KeyEvent.VK_NUMPAD8
					||e.getKeyCode() == KeyEvent.VK_NUMPAD9){
				MyFrame.this.panels[MyFrame.this.selected[0]][selected[1]].setStatus(e.getKeyCode() - KeyEvent.VK_NUMPAD0);
			}
			else if(e.getKeyCode() == KeyEvent.VK_X){
				MyFrame.this.selected[0] = -1;
				MyFrame.this.selected[1] = -1;
				MyFrame.this.secondselected[0] = -1;
				MyFrame.this.secondselected[1] = -1;
				MyFrame.this.initialize();
			}
			else if(e.getKeyCode() == KeyEvent.VK_1){
				MyFrame.this.functionPlugin1();
			}
			else if(e.getKeyCode() == KeyEvent.VK_2){
				MyFrame.this.functionPlugin2();
			}
			else if(e.getKeyCode() == KeyEvent.VK_3){
				MyFrame.this.functionPlugin3();
			}
			else if(e.getKeyCode() == KeyEvent.VK_4){
				MyFrame.this.functionPlugin4();
			}
			else if(e.getKeyCode() == KeyEvent.VK_5){
				MyFrame.this.functionPlugin5();
			}
			else if(e.getKeyCode() == KeyEvent.VK_6){
				MyFrame.this.functionPlugin6();
			}
			else if(e.getKeyCode() == KeyEvent.VK_7){
				MyFrame.this.functionPlugin7();
			}
			else if(e.getKeyCode() == KeyEvent.VK_8){
				MyFrame.this.functionPlugin8();
			}
			else if(e.getKeyCode() == KeyEvent.VK_9){
				MyFrame.this.functionPlugin9();
			}

		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO 自動生成されたメソッド・スタブ

		}

	}

	//マウスリスナー
	class MyMouseListener implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO 自動生成されたメソッド・スタブ
			for(int i=0;i<MyFrame.this.panels.length;i++){
				for(int j=0;j<MyFrame.this.panels[0].length;j++){
					if(MyFrame.this.panels[i][j].isClicked(e.getPoint().x, e.getPoint().y)){
						if(MyFrame.this.selected[0] == -1 && MyFrame.this.selected[1] == -1 && MyFrame.this.panels[i][j].getStatus() != 0){
							MyFrame.this.selected[0] = i;
							MyFrame.this.selected[1] = j;
						}
						else if(MyFrame.this.secondselected[0] == -1 && MyFrame.this.secondselected[1] == -1 && (MyFrame.this.selected[0] != i || MyFrame.this.selected[1] != j) && MyFrame.this.panels[i][j].getStatus() == 0){
							MyFrame.this.secondselected[0] = i;
							MyFrame.this.secondselected[1] = j;
						}
						break;
					}
				}
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO 自動生成されたメソッド・スタブ
			for(int i=0;i<MyFrame.this.panels.length;i++){
				for(int j=0;j<MyFrame.this.panels[0].length;j++){
					if(MyFrame.this.panels[i][j].isClicked(e.getPoint().x, e.getPoint().y)){
						if(MyFrame.this.selected[0] == -1 && MyFrame.this.selected[1] == -1 && MyFrame.this.panels[i][j].getStatus() != 0){
							MyFrame.this.selected[0] = i;
							MyFrame.this.selected[1] = j;
						}
						else if(MyFrame.this.secondselected[0] == -1 && MyFrame.this.secondselected[1] == -1 && (MyFrame.this.selected[0] != i || MyFrame.this.selected[1] != j) && MyFrame.this.panels[i][j].getStatus() == 0){
							MyFrame.this.secondselected[0] = i;
							MyFrame.this.secondselected[1] = j;
						}
						break;
					}
				}
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO 自動生成されたメソッド・スタブ

		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO 自動生成されたメソッド・スタブ

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO 自動生成されたメソッド・スタブ

		}

	}
}
