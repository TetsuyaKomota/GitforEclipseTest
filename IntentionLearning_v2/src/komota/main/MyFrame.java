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

	//描画関係の定数．Staticsじゃ作れないので泣く泣くここに集結
	/** 選択オブジェクト及び選択マスの枠線．ほかでいじることはまずない*/
	static BasicStroke wideStroke = new BasicStroke(Statics.FRAME_SIZE_OF_SELECTED_PANEL);
	/** 背景色*/
	static Color background = new Color(240,240,240);
	/** 文字色*/
	static Color colorofstring = Color.black;
	/** 空きマスの色*/
	static Color colorofspace = Color.white;
	/** 選択中のオブジェクトの枠の色*/
	static Color colorofselectedobject = Color.black;
	/** 選択中の空きマスの枠の色*/
	static Color colorofselectedspace = new Color(255,100,200);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		@SuppressWarnings("unused")
		MyFrame frame = new MyFrame();
		frame.panels[0][0].setStatus(1);
	}

	//バッファストラテジ
	public BufferStrategy buffer;

	//パネル
	//空間的な2次元配列は全て[行][列]に統一！！！！
	public MyPanel[][] panels;

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
	String file_name = "test.txt";
	PrintWriter pw;

	//タイマークラス
	Timer t;

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

		this.panels = new MyPanel[Statics.NUMBEROFPANEL][Statics.NUMBEROFPANEL];
		Statics.SIZE_PANEL = ((this.getWidth() - Statics.SIZE_FRAME * 2 + Statics.SIZE_SEPALATOR ) / Statics.NUMBEROFPANEL ) - Statics.SIZE_SEPALATOR;

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

		this.t = new Timer();
		t.schedule(new RenderTask(), 0,12);

		this.initialize();
	}

	//各種セッター、ゲッター
	public MyPanel[][] getPanels(){
		return this.panels;
	}
	public void setSelected(int[] selected){
		this.selected = selected;
	}
	public void setSecondSelected(int[] selected){
		this.secondselected = selected;
	}
	public int[] getSelected(){
		return this.selected;
	}
	public int[] getSecondSelected(){
		return this.secondselected;
	}

	//出力先ファイル名を変更
	public void setOutputFile(){
		File file = null;
		while(file == null || file.exists() == false){
			try {
				FileOutputStream fos = new FileOutputStream("log/"+file_name,true);
				OutputStreamWriter osw = new OutputStreamWriter(fos);
				this.pw = new PrintWriter(osw);
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				System.out.println("ファイルなし");
				e.printStackTrace();
			}
			file = new File("log/"+file_name);
		}
	}
	public void setOutputFile(String file_name){
		this.file_name = file_name;
		setOutputFile();
	}

	//スペースキーを押したときに起こす処理
	public void pushSPACE(){
		if(this.selected[0] != -1 && this.selected[1] != -1 && this.secondselected[0] != -1 && this.secondselected[1] != -1){
			int temp = this.panels[selected[0]][selected[1]].getStatus();
			this.panels[selected[0]][selected[1]].setStatus(this.panels[secondselected[0]][secondselected[1]].getStatus());
			this.panels[secondselected[0]][secondselected[1]].setStatus(temp);

			//途中状態ログ
			outputStatus();

			this.selected[0] = -1;
			this.selected[1] = -1;
			this.secondselected[0] = -1;
			this.secondselected[1] = -1;
		}
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


	/* *************************************************************************************************************************** */
	//ログ出力
	//状態空間出力．
	private void outputLog(String command){
		pw.print(command);
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
	//初期状態を出力する．主に初期化直後に使用する
	public void outputStart(){
		outputLog("start ,");
	}
	//途中状態を出力する
	public void outputStatus(){
		outputLog("status,");
	}
	//ゴール時に最終状態を出力する
	public void outputGoal(){
		outputLog("goal  ,");
	}
	//計算結果などをresult.txtに出力
	public void outputResult(String out){
		this.pw.print(out);
	}
	public void outputlnResult(String out){
		this.pw.println(out);
	}
	/* *************************************************************************************************************************** */

	//オブジェクトを描画
	public void drawObjects(){
		Graphics2D  g = (Graphics2D)this.buffer.getDrawGraphics();

		for(int i=0;i<this.panels.length;i++){
			for(int j=0;j<this.panels[0].length;j++){
				int tempstatus = this.panels[i][j].status;
				if(tempstatus>0){
					switch(tempstatus){
					case 0:
						g.setColor(Color.white);
						break;
					case 1:
						g.setColor(Color.red);
						break;
					case 2:
						g.setColor(Color.blue);
						break;
					case 3:
						g.setColor(Color.yellow);
						break;
					case 4:
						g.setColor(Color.green);
						break;
					case 5:
						g.setColor(Color.orange);
						break;
					case 6:
						g.setColor(Color.pink);
						break;
					case 7:
						g.setColor(Color.lightGray);
						break;
					case 8:
						g.setColor(Color.gray);
						break;
					case 9:
						g.setColor(Color.black);
						break;
					}
					g.fillRect(Statics.SIZE_FRAME+(Statics.SIZE_PANEL+Statics.SIZE_SEPALATOR)*j - ((Statics.SIZE_PANEL+Statics.SIZE_SEPALATOR)*Statics.SIZE_OBJECT)/2,Statics.SIZE_FRAME+(Statics.SIZE_PANEL+Statics.SIZE_SEPALATOR)*i - ((Statics.SIZE_PANEL+Statics.SIZE_SEPALATOR)*Statics.SIZE_OBJECT)/2,(Statics.SIZE_PANEL+Statics.SIZE_SEPALATOR)*Statics.SIZE_OBJECT,(Statics.SIZE_PANEL+Statics.SIZE_SEPALATOR)*Statics.SIZE_OBJECT);

					if(this.selected[0] == i && this.selected[1] == j){
						g.setColor(MyFrame.colorofselectedobject);
						g.setStroke(MyFrame.wideStroke);
						g.drawRect(Statics.SIZE_FRAME+(Statics.SIZE_PANEL+Statics.SIZE_SEPALATOR)*j - ((Statics.SIZE_PANEL+Statics.SIZE_SEPALATOR)*Statics.SIZE_OBJECT)/2,Statics.SIZE_FRAME+(Statics.SIZE_PANEL+Statics.SIZE_SEPALATOR)*i - ((Statics.SIZE_PANEL+Statics.SIZE_SEPALATOR)*Statics.SIZE_OBJECT)/2,(Statics.SIZE_PANEL+Statics.SIZE_SEPALATOR)*Statics.SIZE_OBJECT,(Statics.SIZE_PANEL+Statics.SIZE_SEPALATOR)*Statics.SIZE_OBJECT);
					}
				}
			}
		}
		g.dispose();
		this.buffer.show();
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
	public void functionPluginQ(){
		System.out.println("No Function in This Key.");
	}
	public void functionPluginW(){
		System.out.println("No Function in This Key.");
	}
	public void functionPluginE(){
		System.out.println("No Function in This Key.");
	}
	public void functionPluginR(){
		System.out.println("No Function in This Key.");
	}
	public void functionPluginT(){
		System.out.println("No Function in This Key.");
	}
	public void functionPluginY(){
		System.out.println("No Function in This Key.");
	}


	//タイマータスク
	class RenderTask extends TimerTask{

		@Override
		public void run() {
			// TODO 自動生成されたメソッド・スタブ
			Graphics2D g = (Graphics2D)MyFrame.this.buffer.getDrawGraphics();
			//背景の描画
			g.setColor(MyFrame.background);
			g.fillRect(0, 0, MyFrame.this.getWidth(), MyFrame.this.getHeight());
			//文字情報の描画
			g.setColor(MyFrame.colorofstring);
			g.drawString(MyFrame.this.expranation, Statics.SIZE_FRAME, Statics.SIZE_FRAME/2 + 10);
			g.drawString(MyFrame.this.tasktitle, Statics.SIZE_FRAME, Statics.SIZE_FRAME/2 + 30);
			g.drawString(MyFrame.this.howtouse, Statics.SIZE_FRAME, Statics.SIZE_FRAME+Statics.NUMBEROFPANEL*(Statics.SIZE_PANEL+Statics.SIZE_SEPALATOR)+40);

			g.dispose();

			//空白パネルの描画
			for(int i=0;i<MyFrame.this.panels.length;i++){
				for(int j=0;j<MyFrame.this.panels[0].length;j++){
					MyFrame.this.panels[i][j].drawSpace();
				}
			}

			g = (Graphics2D)MyFrame.this.buffer.getDrawGraphics();
			g.setStroke(MyFrame.wideStroke);
			if(MyFrame.this.secondselected[0] >= 0 && MyFrame.this.secondselected[1] >= 0){
				g.setColor(MyFrame.colorofselectedspace);
				g.drawRect(Statics.SIZE_FRAME+(Statics.SIZE_PANEL+Statics.SIZE_SEPALATOR)*(MyFrame.this.secondselected[1]),Statics.SIZE_FRAME+(Statics.SIZE_PANEL+Statics.SIZE_SEPALATOR)*(MyFrame.this.secondselected[0]),Statics.SIZE_PANEL,Statics.SIZE_PANEL);
			}
			g.dispose();

			MyFrame.this.drawObjects();
			MyFrame.this.buffer.show();
		}
	}

	//キーリスナー
	class MyKeyListener implements KeyListener{

		@Override
		public void keyTyped(KeyEvent e) {
		}
		@Override
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_SPACE){
				MyFrame.this.pushSPACE();
			}
			else if(e.getKeyCode() == KeyEvent.VK_G){
				MyFrame.this.pushGoal();
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
			else if(e.getKeyCode() == KeyEvent.VK_Q){
				MyFrame.this.functionPluginQ();
			}
			else if(e.getKeyCode() == KeyEvent.VK_W){
				MyFrame.this.functionPluginW();
			}
			else if(e.getKeyCode() == KeyEvent.VK_E){
				MyFrame.this.functionPluginE();
			}
			else if(e.getKeyCode() == KeyEvent.VK_R){
				MyFrame.this.functionPluginR();
			}
			else if(e.getKeyCode() == KeyEvent.VK_T){
				MyFrame.this.functionPluginT();
			}
			else if(e.getKeyCode() == KeyEvent.VK_Y){
				MyFrame.this.functionPluginY();
			}
			else{
				System.out.println("[MyFrame.MyKeyListener]keyPressed:This key isn't used.");
			}
		}
		@Override
		public void keyReleased(KeyEvent e) {
		}
	}
	//マウスリスナー
	class MyMouseListener implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent e) {
			for(int i=0;i<MyFrame.this.panels.length;i++){
				for(int j=0;j<MyFrame.this.panels[0].length;j++){
					if(MyFrame.this.panels[i][j].isClicked(e.getPoint().x, e.getPoint().y)){
						//クリックされたパネルは、オブジェクトの裏に隠れた空きパネル、オブジェクトの外の空きパネル（本当の空きパネル）、オブジェクトパネルの3通り
						if(MyFrame.this.panels[i][j].getStatus() == 0){
							int temp[] = new int[2];
							temp[0] = -1;
							temp[1] = -1;
							for(int a=0;a<Statics.SIZE_OBJECT;a++){
								for(int b=0;b<Statics.SIZE_OBJECT;b++){
									if(i-(Statics.SIZE_OBJECT/2)+a<0||j-(Statics.SIZE_OBJECT/2)+b<0||i-(Statics.SIZE_OBJECT/2)+a>Statics.NUMBEROFPANEL-1 || j-(Statics.SIZE_OBJECT/2)+b>Statics.NUMBEROFPANEL-1){
									}
									else if(MyFrame.this.panels[i-(Statics.SIZE_OBJECT/2)+a][j-(Statics.SIZE_OBJECT/2)+b].status>0){
										temp[0] = i-(Statics.SIZE_OBJECT/2)+a;
										temp[1] = j-(Statics.SIZE_OBJECT/2)+b;
									}
								}
							}
							if(temp[0] != -1 && temp[1] != -1){
								MyFrame.this.selected[0] = temp[0];
								MyFrame.this.selected[1] = temp[1];
							}
							else{
								MyFrame.this.secondselected[0] = i;
								MyFrame.this.secondselected[1] = j;
							}
						}
						else{
							MyFrame.this.selected[0] = i;
							MyFrame.this.selected[1] = j;
						}
						break;
					}
				}
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
		}
		@Override
		public void mouseReleased(MouseEvent e) {
		}
		@Override
		public void mouseEntered(MouseEvent e) {
		}
		@Override
		public void mouseExited(MouseEvent e) {
		}
	}
/* ********************************************************************************************************************* */
// パネルクラス

	class MyPanel {

		//フィールド
		//パネルの状態
		private int status;
		//パネルの場所
		private int[] position;

		//コンストラクタ
		public MyPanel(int status,int[] position){
			this.status = status;
			this.position = position;
		}

		//セッター、ゲッター
		public MyPanel setStatus(int status){
			this.status = status;
			return this;
		}
		public int getStatus(){
			return this.status;
		}
		public MyPanel setPosition(int[] position){
			this.position = position;
			return this;
		}
		public int[] getPosition(){
			return this.position;
		}

		//描画
		public void drawSpace(){
			Graphics2D  g = (Graphics2D)MyFrame.this.buffer.getDrawGraphics();
			g.setColor(MyFrame.colorofspace);
			g.fillRect(Statics.SIZE_FRAME + (Statics.SIZE_PANEL+Statics.SIZE_SEPALATOR)*this.position[1],Statics.SIZE_FRAME + (Statics.SIZE_PANEL+Statics.SIZE_SEPALATOR)*this.position[0],Statics.SIZE_PANEL,Statics.SIZE_PANEL);
			g.dispose();
		}
		//クリックした位置が、このパネルかどうかを判定する
		public boolean isClicked(int x,int y){
			if(		x>=Statics.SIZE_FRAME + (Statics.SIZE_PANEL+Statics.SIZE_SEPALATOR)*this.position[1]
				&&	x<=Statics.SIZE_FRAME + (Statics.SIZE_PANEL+Statics.SIZE_SEPALATOR)*this.position[1] + Statics.SIZE_PANEL
				&&	y>=Statics.SIZE_FRAME + (Statics.SIZE_PANEL+Statics.SIZE_SEPALATOR)*this.position[0]
				&&	y<=Statics.SIZE_FRAME + (Statics.SIZE_PANEL+Statics.SIZE_SEPALATOR)*this.position[0] + Statics.SIZE_PANEL){
				return true;
			}
			return false;
		}
	}
}
