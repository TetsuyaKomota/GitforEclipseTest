package komota.main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

public class MainFrame extends JFrame{

	/* ******************************************************************************************************************************************* */
	/* ******************************************************************************************************************************************* */
	/* ******************************************************************************************************************************************* */
	/* ******************************************************************************************************************************************* */
	/* ******************************************************************************************************************************************* */
	/* ******************************************************************************************************************************************* */
	/* ******************************************************************************************************************************************* */
	/* ************* 現在MainFrameは更新停止。MyFrameが次世代クラスなのでそっちを使うように******************************************************* */
	/* ******************************************************************************************************************************************* */
	/* ******************************************************************************************************************************************* */
	/* ******************************************************************************************************************************************* */
	/* ******************************************************************************************************************************************* */
	/* ******************************************************************************************************************************************* */
	/* ******************************************************************************************************************************************* */
	/* ******************************************************************************************************************************************* */





	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		MainFrame frame = new MainFrame();
	}

	//バッファストラテジ
	BufferStrategy buffer;

	//パネル
	MyPanel[] panels;

	//パネルの行、列数
	public static final int NUMBEROFPANEL = 10;
	//選択パネル枠の太さ
	static final float FRAME_SIZE_OF_SELECTED_PANEL = 3f;

	//選択状態パネル
	int selected = -1;
	int secondselected = -1;

	//タスク表示
	String tasktitle = "tasktitle";

	//出力先ファイル
	File file;
	String file_name = "test.txt";
	PrintWriter pw;

	//コンストラクタ
	public MainFrame(){
		this.setTitle("IntentionLearning");
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		this.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosed(WindowEvent arg0){
				MainFrame.this.pw.close();
			}
		});

		this.setSize(1000,1000);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setVisible(true);

		this.createBufferStrategy(2);
		this.buffer = this.getBufferStrategy();
		this.setIgnoreRepaint(true);

		this.panels = new MyPanel[MainFrame.NUMBEROFPANEL*MainFrame.NUMBEROFPANEL];
		MyPanel.SIZE_PANEL = ((this.getWidth() - MyPanel.SIZE_FRAME * 2 + MyPanel.SIZE_SEPALATOR ) / MainFrame.NUMBEROFPANEL ) - MyPanel.SIZE_SEPALATOR;
		for(int i=0;i<this.panels.length;i++){
			int[] position = new int[2];
			position[0] = i%MainFrame.NUMBEROFPANEL;
			position[1] = i/MainFrame.NUMBEROFPANEL;
			this.panels[i] = new MyPanel(i,position);
		}

		this.addKeyListener(new MainKeyListener());
		this.addMouseListener(new MyMouseListener());

		this.file = new File("log/"+file_name);
		try {
//			this.pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
		      FileOutputStream fos = new FileOutputStream("log/"+file_name,true);
		      OutputStreamWriter osw = new OutputStreamWriter(fos);
		      this.pw = new PrintWriter(osw);
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			System.out.println("ファイルなし");
		}


		Timer t = new Timer();
		t.schedule(new RenderTask(), 0,200);

		this.initialize();
	}

	//各種セッター、ゲッター
	public MyPanel[] getPanels(){
		return this.panels;
	}
	public MainFrame setSelected(int selected){
		this.selected = selected;
		return this;
	}
	public MainFrame setSecondSelected(int selected){
		this.secondselected = selected;
		return this;
	}

	//出力先ファイル名を変更
	public void setOutputFile(){
		this.file = new File("log/"+file_name);
		try {
//			this.pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
		      FileOutputStream fos = new FileOutputStream("log/"+file_name,true);
		      OutputStreamWriter osw = new OutputStreamWriter(fos);
		      this.pw = new PrintWriter(osw);
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			System.out.println("ファイルなし");
		}
	}
	public void setOutputFile(String file_name){
		this.file_name = file_name;
		setOutputFile();
	}



	//Gを押した時に起こす処理
	public void pushGoal(){
		this.outputGoal();
		this.selected = -1;
		this.secondselected = -1;
		this.initialize();
	}

	//初期化
	public void initialize(){

	}

	//初期化時に初期状態を出力する
	public void outputStart(){
/*
		pw.println("**start:"
				+MainFrame.this.panels[0].status+","
				+MainFrame.this.panels[1].status+","
				+MainFrame.this.panels[2].status+","
				+MainFrame.this.panels[3].status+","
				+MainFrame.this.panels[4].status+","
				+MainFrame.this.panels[5].status+","
				+MainFrame.this.panels[6].status+","
				+MainFrame.this.panels[7].status+","
				+MainFrame.this.panels[8].status
);
*/
		pw.print("start ,");
		for(int i=0;i<MainFrame.this.panels.length;i++){
			pw.print(MainFrame.this.panels[i].status);
			if(i<MainFrame.this.panels.length-1){
				pw.print(",");
			}
		}

		pw.println();
	}

	//ゴール時に最終状態を出力する
	public void outputGoal(){
/*
		pw.println("**goal:"
				+MainFrame.this.panels[0].status+","
				+MainFrame.this.panels[1].status+","
				+MainFrame.this.panels[2].status+","
				+MainFrame.this.panels[3].status+","
				+MainFrame.this.panels[4].status+","
				+MainFrame.this.panels[5].status+","
				+MainFrame.this.panels[6].status+","
				+MainFrame.this.panels[7].status+","
				+MainFrame.this.panels[8].status
);
*/
		pw.print("goal  ,");
		for(int i=0;i<MainFrame.this.panels.length;i++){
			pw.print(MainFrame.this.panels[i].status);
			if(i<MainFrame.this.panels.length-1){
				pw.print(",");
			}
		}
		pw.println();

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

	//タイマータスク
	class RenderTask extends TimerTask{

		@Override
		public void run() {
			// TODO 自動生成されたメソッド・スタブ
			Graphics2D g = (Graphics2D)MainFrame.this.buffer.getDrawGraphics();
			//背景の描画
			g.setColor(new Color(240,240,240));
			g.fillRect(0, 0, MainFrame.this.getWidth(), MainFrame.this.getHeight());

			g.setColor(Color.black);
			g.drawString(MainFrame.this.tasktitle, MyPanel.SIZE_FRAME, MyPanel.SIZE_FRAME/2);
			g.drawString("SPACE:exchange the first clicked and the second clicked.   G:finish the task.", MyPanel.SIZE_FRAME, MyPanel.SIZE_FRAME/2+20);
/*
			if(MainFrame.this.selected >= 0){
				g.setColor(Color.black);
				g.fillRect(MyPanel.SIZE_FRAME+(MyPanel.SIZE_PANEL+MyPanel.SIZE_SEPALATOR)*(MainFrame.this.selected%3)-5,MyPanel.SIZE_FRAME+(MyPanel.SIZE_PANEL+MyPanel.SIZE_SEPALATOR)*(MainFrame.this.selected/3)-5,MyPanel.SIZE_PANEL+10,MyPanel.SIZE_PANEL+10);
			}
			if(MainFrame.this.secondselected >= 0){
				g.setColor(new Color(255,100,200));
				g.fillRect(MyPanel.SIZE_FRAME+(MyPanel.SIZE_PANEL+MyPanel.SIZE_SEPALATOR)*(MainFrame.this.secondselected%3)-5,MyPanel.SIZE_FRAME+(MyPanel.SIZE_PANEL+MyPanel.SIZE_SEPALATOR)*(MainFrame.this.secondselected/3)-5,MyPanel.SIZE_PANEL+10,MyPanel.SIZE_PANEL+10);
			}
*/
			g.dispose();

			for(int i=0;i<MainFrame.this.panels.length;i++){
				MainFrame.this.panels[i].draw(MainFrame.this);
			}

			g = (Graphics2D)MainFrame.this.buffer.getDrawGraphics();
			BasicStroke wideStroke = new BasicStroke(MainFrame.FRAME_SIZE_OF_SELECTED_PANEL);
			g.setStroke(wideStroke);
			if(MainFrame.this.selected >= 0){
				g.setColor(Color.black);
				g.drawRect(MyPanel.SIZE_FRAME+(MyPanel.SIZE_PANEL+MyPanel.SIZE_SEPALATOR)*(MainFrame.this.selected%MainFrame.NUMBEROFPANEL) + (int)MainFrame.FRAME_SIZE_OF_SELECTED_PANEL/2,MyPanel.SIZE_FRAME+(MyPanel.SIZE_PANEL+MyPanel.SIZE_SEPALATOR)*(MainFrame.this.selected/MainFrame.NUMBEROFPANEL)+ (int)MainFrame.FRAME_SIZE_OF_SELECTED_PANEL/2,MyPanel.SIZE_PANEL - (int)MainFrame.FRAME_SIZE_OF_SELECTED_PANEL,MyPanel.SIZE_PANEL - (int)MainFrame.FRAME_SIZE_OF_SELECTED_PANEL);
			}
			if(MainFrame.this.secondselected >= 0){
				g.setColor(new Color(255,100,200));
				g.drawRect(MyPanel.SIZE_FRAME+(MyPanel.SIZE_PANEL+MyPanel.SIZE_SEPALATOR)*(MainFrame.this.secondselected%MainFrame.NUMBEROFPANEL),MyPanel.SIZE_FRAME+(MyPanel.SIZE_PANEL+MyPanel.SIZE_SEPALATOR)*(MainFrame.this.secondselected/MainFrame.NUMBEROFPANEL),MyPanel.SIZE_PANEL,MyPanel.SIZE_PANEL);
			}
			g.dispose();
			MainFrame.this.buffer.show();
		}
	}

	//キーリスナー
	class MainKeyListener implements KeyListener{

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO 自動生成されたメソッド・スタブ

		}

		@Override
		public void keyPressed(KeyEvent e) {
			// TODO 自動生成されたメソッド・スタブ
/*
			if(e.getKeyCode() == KeyEvent.VK_0
					||e.getKeyCode() == KeyEvent.VK_1
					||e.getKeyCode() == KeyEvent.VK_2
					||e.getKeyCode() == KeyEvent.VK_3
					||e.getKeyCode() == KeyEvent.VK_4
					||e.getKeyCode() == KeyEvent.VK_5
					||e.getKeyCode() == KeyEvent.VK_6
					||e.getKeyCode() == KeyEvent.VK_7
					||e.getKeyCode() == KeyEvent.VK_8
				){
				System.out.println("You push:"+e.getKeyChar());

				if(MainFrame.this.selected == -1){
					MainFrame.this.selected = e.getKeyCode() - KeyEvent.VK_0;
				}
				else if(MainFrame.this.secondselected == -1&&MainFrame.this.selected != e.getKeyCode() - KeyEvent.VK_0){
					MainFrame.this.secondselected = e.getKeyCode() - KeyEvent.VK_0;
				}
			}else
*/
			if(e.getKeyCode() == KeyEvent.VK_SPACE){
				if(MainFrame.this.secondselected != -1){
					int temp = MainFrame.this.panels[selected].getStatus();
					MainFrame.this.panels[selected].setStatus(MainFrame.this.panels[secondselected].getStatus());
					MainFrame.this.panels[secondselected].setStatus(temp);
					int[] fpanel = MainFrame.this.panels[selected].getPosition();
					int[] spanel = MainFrame.this.panels[secondselected].getPosition();
					MainFrame.this.pw.print("changehogehoge,");
					for(int i=0;i<MainFrame.this.panels.length;i++){
						if(i == fpanel[0]*MainFrame.NUMBEROFPANEL + fpanel[1]){
							pw.print("1");
						}
						else if(i == spanel[0]*MainFrame.NUMBEROFPANEL + spanel[1]){
							pw.print("2");
						}
						else{
							pw.print("0");
						}
						if(i<MainFrame.this.panels.length-1){
							pw.print(",");
						}
					}
					pw.println();

/*
					MainFrame.this.pw.println("status:"
							+MainFrame.this.panels[0].status+","
							+MainFrame.this.panels[1].status+","
							+MainFrame.this.panels[2].status+","
							+MainFrame.this.panels[3].status+","
							+MainFrame.this.panels[4].status+","
							+MainFrame.this.panels[5].status+","
							+MainFrame.this.panels[6].status+","
							+MainFrame.this.panels[7].status+","
							+MainFrame.this.panels[8].status
							);
*/
					MainFrame.this.pw.print("status,");
					for(int i=0;i<MainFrame.this.panels.length;i++){
						pw.print(MainFrame.this.panels[i].status);
						if(i<MainFrame.this.panels.length-1){
							pw.print(",");
						}
					}
					pw.println();

					MainFrame.this.selected = -1;
					MainFrame.this.secondselected = -1;
				}
			}
			else if(e.getKeyCode() == KeyEvent.VK_Z){
				if(MainFrame.this.secondselected != -1){
					MainFrame.this.secondselected = -1;
				}
				else if(MainFrame.this.selected != -1){
					MainFrame.this.selected = -1;
				}
			}
			else if(e.getKeyCode() == KeyEvent.VK_G){
				MainFrame.this.pushGoal();
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
				MainFrame.this.panels[MainFrame.this.selected].setStatus(e.getKeyCode() - KeyEvent.VK_NUMPAD0);
			}
			else if(e.getKeyCode() == KeyEvent.VK_1){
				MainFrame.this.functionPlugin1();
			}
			else if(e.getKeyCode() == KeyEvent.VK_2){
				MainFrame.this.functionPlugin2();
			}
			else if(e.getKeyCode() == KeyEvent.VK_3){
				MainFrame.this.functionPlugin3();
			}
			else if(e.getKeyCode() == KeyEvent.VK_4){
				MainFrame.this.functionPlugin4();
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
			for(int i=0;i<MainFrame.this.panels.length;i++){
				if(MainFrame.this.panels[i].isClicked(e.getPoint().x, e.getPoint().y)){
					if(MainFrame.this.selected == -1 && MainFrame.this.panels[i].getStatus() != 0){
						MainFrame.this.selected = i;
					}
					else if(MainFrame.this.secondselected == -1&&MainFrame.this.selected != i&&MainFrame.this.panels[i].getStatus() == 0){
						MainFrame.this.secondselected = i;
					}
					break;
				}
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO 自動生成されたメソッド・スタブ
			for(int i=0;i<MainFrame.this.panels.length;i++){
				if(MainFrame.this.panels[i].isClicked(e.getPoint().x, e.getPoint().y)){
					if(MainFrame.this.selected == -1 && MainFrame.this.panels[i].getStatus() != 0){
						MainFrame.this.selected = i;
					}
					else if(MainFrame.this.secondselected == -1&&MainFrame.this.selected != i&&MainFrame.this.panels[i].getStatus() == 0){
						MainFrame.this.secondselected = i;
					}

					break;
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
