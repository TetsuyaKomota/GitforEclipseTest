package komota.main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

import komota.lib.MyIO;

/**
 * 意図理解実験シミュレータ
 * @author komota
 * @version 2.0
 */

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
	 * @param args 使用しない
	 */
	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		@SuppressWarnings("unused")
		MyFrame frame = new MyFrame();
	}

	//フィールド
	/** バッファストラテジ*/
	protected BufferStrategy buffer;

	/** パネル*/
	public MyPanel[][] panels;
	//空間的な2次元配列は全て[行][列]に統一！！！！

	/** 選択状態パネル*/
	int[] selected = new int[2];
	int[] secondselected = new int[2];

	/** 説明文などの文字列出力。アンケートなどで使用する*/
	public String expranation = "";

	/** タスク名*/
	public String tasktitle = "tasktitle";

	/** 操作方法など*/
	public String howtouse = "SPACE:exchange the first clicked and the second clicked.   G:finish the task.";

	/** 出力先ファイル*/
	String file_name = "logdata.txt";

	/** ファイル入出力用クラス*/
	MyIO io;

	/* プライベートフィールド */

	/** タイマークラス*/
	private Timer t;

	/** 累計フレーム数*/
	private int framecount;

	/** シミュレータ実行時の時間*/
	private long framestarttime;

	/** 実行フレームレート*/
	private long frame_rate;

	/**
	 * コンストラクタ
	 */

	public MyFrame(){
		this.framestarttime = System.currentTimeMillis();
		this.frame_rate = Statics.FRAME_RATE;
		this.setTitle("IntentionLearning v2.0");
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

		this.setOutputFile();

		this.t = new Timer();
		t.schedule(new RenderTask(), 0,Math.round((double)1000/Statics.FRAME_RATE));

		this.initialize();
	}

	/**
	 * 状態空間のゲッター
	 * @return 状態空間配列
	 */
	public MyPanel[][] getPanels(){
		return this.panels;
	}
	/**
	 * 選択オブジェクトのセッター
	 * @param selected 選択状態にするオブジェクトの座標
	 */
	public void setSelected(int[] selected){
		this.selected = selected;
	}
	/**
	 * 選択マスのセッター
	 * @param selected 選択状態にする空きマスの座標
	 */
	public void setSecondSelected(int[] selected){
		this.secondselected = selected;
	}
	/**
	 * 選択中のオブジェクト座標のゲッター
	 * @return 選択中のオブジェクトの座標
	 */
	public int[] getSelected(){
		return this.selected;
	}
	/**
	 * 選択中の空きマスの座標のゲッター
	 * @return 選択中の空きマスの座標
	 */
	public int[] getSecondSelected(){
		return this.secondselected;
	}
	/**
	 * シミュレータを実行してからの累計フレーム数のゲッター
	 * @return シミュレータを実行してからの累計フレーム数
	 */
	public int getFrameCount(){
		return this.framecount;
	}
	/**
	 * シミュレータを実行してからの累計経過時間のゲッター
	 * @return シミュレータを実行してからの累計経過時間
	 */
	public long getFrameTime(){
		return (System.currentTimeMillis() - this.framestarttime);
	}
	/**
	 * 現在の実行フレームレートのゲッター
	 * @return 実行フレームレート
	 */
	public long getFrameRate(){
		return this.frame_rate;
	}

	/**
	 * 出力先ファイル名を変更
	 * 出力先ファイルのファイル名をあらかじめfile_nameに保持しておく必要があるため，このメソッドを直接使用するべきではない．
	 * 代わりにsetOutputFile(String file_name)を使用すること.
	 */
	@Deprecated
	public void setOutputFile(){
/*
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
*/
		this.io = new MyIO();
		this.io.writeFile(this.file_name);
	}
	/**
	 * 出力先ファイル名を変更
	 * 引数に与えた名前のファイルを出力先ファイルに設定する．
	 * @param file_name 出力先ファイル名
	 */
	public void setOutputFile(String file_name){
		this.file_name = file_name;
		setOutputFile();
	}

	/**
	 * スペースキーを押したときに起こす処理.
	 * オブジェクトと空きマスを選択中なら，選択オブジェクトを選択マスへ移動する．その後，途中状態としてログを出力する．
	 */
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

	/**
	 * Gを押した時に起こす処理.
	 * Gを押した時点での状態を終了状態としてログを出力する．その後，自動的に初期化を行う．基本的にサブクラスではinitializeでオブジェクトの配置し直しと同時にoutputStartを実行するので，Gを押した時点で次の初期状態の出力まで行うと考えてよい．
	 */
	public void pushGoal(){
		this.outputGoal();
		this.selected[0] = -1;
		this.selected[1] = -1;
		this.secondselected[0] = -1;
		this.secondselected[1] = -1;
		this.initialize();
	}

	/**
	 * ※オーバーライドされるべきメソッド
	 * 初期化を行う．サブクラスではここに盤面の設定ルールを記述した後，最後にoutputStart()を実行して初期状態をファイル出力する．
	 */
	public void initialize(){
	}


	/* *************************************************************************************************************************** */
	/**
	 * 状態空間を出力先ファイルに出力する．
	 * このメソッドは直接使用せず，コマンドを指定した各メソッドを使用する．
	 * @param command ログデータの先頭に付与するコマンド
	 */
	private void outputLog(String command){
		this.io.print(command);
		for(int i=0;i<MyFrame.this.panels.length;i++){
			for(int j=0;j<MyFrame.this.panels[0].length;j++){
				this.io.print(MyFrame.this.panels[i][j].status);
				if(i<MyFrame.this.panels.length-1 || j<MyFrame.this.panels.length-1){
					this.io.print(",");
				}
			}
		}
		this.io.println();
	}
	/**
	 * 初期状態を出力する．主に初期化直後に使用する
	 */
	public void outputStart(){
		outputLog("start ,");
	}
	/**
	 * 途中状態を出力する.
	 */
	public void outputStatus(){
		outputLog("status,");
	}
	/**
	 * ゴール時に最終状態を出力する.
	 */
	public void outputGoal(){
		outputLog("goal  ,");
	}
	/**
	 * 出力先ファイルへ文字列を出力する汎用メソッド．主に計算結果などを出力するのに使用する．コマンドなどのフォーマットは行わないので使用する場合は書式に注意すること．
	 * @param out 出力文字列
	 */
	public void outputResult(String out){
		this.io.print(out);
	}
	/**
	 * 出力先ファイルへ文字列を出力する汎用メソッド．出力後に改行する．主に計算結果などを出力するのに使用する．コマンドなどのフォーマットは行わないので使用する場合は書式に注意すること．
	 * @param out 出力文字列
	 */
	public void outputlnResult(String out){
		this.io.println(out);
	}
	/* *************************************************************************************************************************** */

	/**
	 * オブジェクトを描画する．
	 */
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
				else if(this.secondselected[0] == i && this.secondselected[1] == j){
					g.setColor(MyFrame.colorofselectedspace);
					g.setStroke(MyFrame.wideStroke);
					g.drawRect(Statics.SIZE_FRAME+(Statics.SIZE_PANEL+Statics.SIZE_SEPALATOR)*j - ((Statics.SIZE_PANEL+Statics.SIZE_SEPALATOR)*Statics.SIZE_OBJECT)/2,Statics.SIZE_FRAME+(Statics.SIZE_PANEL+Statics.SIZE_SEPALATOR)*i - ((Statics.SIZE_PANEL+Statics.SIZE_SEPALATOR)*Statics.SIZE_OBJECT)/2,(Statics.SIZE_PANEL+Statics.SIZE_SEPALATOR)*Statics.SIZE_OBJECT,(Statics.SIZE_PANEL+Statics.SIZE_SEPALATOR)*Statics.SIZE_OBJECT);
				}

			}
		}
		g.dispose();
		this.buffer.show();
	}
	/**
	 * ※オーバーライドされるべきメソッド
	 * 1キーで実行するプラグインメソッド．
	 * 各サブクラスでキーに反応する機能を追加したい場合にオーバーライドする
	 */
	public void functionPlugin1(){
		System.out.println("No Function in This Key.");
	}
	/**
	 * ※オーバーライドされるべきメソッド
	 * 2キーで実行するプラグインメソッド．
	 * 各サブクラスでキーに反応する機能を追加したい場合にオーバーライドする
	 */
	public void functionPlugin2(){
		System.out.println("No Function in This Key.");
	}
	/**
	 * ※オーバーライドされるべきメソッド
	 * 3キーで実行するプラグインメソッド．
	 * 各サブクラスでキーに反応する機能を追加したい場合にオーバーライドする
	 */
	public void functionPlugin3(){
		System.out.println("No Function in This Key.");
	}
	/**
	 * ※オーバーライドされるべきメソッド
	 * 4キーで実行するプラグインメソッド．
	 * 各サブクラスでキーに反応する機能を追加したい場合にオーバーライドする
	 */
	public void functionPlugin4(){
		System.out.println("No Function in This Key.");
	}
	/**
	 * ※オーバーライドされるべきメソッド
	 * 5キーで実行するプラグインメソッド．
	 * 各サブクラスでキーに反応する機能を追加したい場合にオーバーライドする
	 */
	public void functionPlugin5(){
		System.out.println("No Function in This Key.");
	}
	/**
	 * ※オーバーライドされるべきメソッド
	 * 6キーで実行するプラグインメソッド．
	 * 各サブクラスでキーに反応する機能を追加したい場合にオーバーライドする
	 */
	public void functionPlugin6(){
		System.out.println("No Function in This Key.");
	}
	/**
	 * ※オーバーライドされるべきメソッド
	 * 7キーで実行するプラグインメソッド．
	 * 各サブクラスでキーに反応する機能を追加したい場合にオーバーライドする
	 */
	public void functionPlugin7(){
		System.out.println("No Function in This Key.");
	}
	/**
	 * ※オーバーライドされるべきメソッド
	 * 8キーで実行するプラグインメソッド．
	 * 各サブクラスでキーに反応する機能を追加したい場合にオーバーライドする
	 */
	public void functionPlugin8(){
		System.out.println("No Function in This Key.");
	}
	/**
	 * ※オーバーライドされるべきメソッド
	 * 9キーで実行するプラグインメソッド．
	 * 各サブクラスでキーに反応する機能を追加したい場合にオーバーライドする
	 */
	public void functionPlugin9(){
		System.out.println("No Function in This Key.");
	}
	/**
	 * ※オーバーライドされるべきメソッド
	 * Qキーで実行するプラグインメソッド．
	 * 各サブクラスでキーに反応する機能を追加したい場合にオーバーライドする
	 */
	public void functionPluginQ(){
		System.out.println("No Function in This Key.");
	}
	/**
	 * ※オーバーライドされるべきメソッド
	 * Wキーで実行するプラグインメソッド．
	 * 各サブクラスでキーに反応する機能を追加したい場合にオーバーライドする
	 */
	public void functionPluginW(){
		System.out.println("No Function in This Key.");
	}
	/**
	 * ※オーバーライドされるべきメソッド
	 * Eキーで実行するプラグインメソッド．
	 * 各サブクラスでキーに反応する機能を追加したい場合にオーバーライドする
	 */
	public void functionPluginE(){
		System.out.println("No Function in This Key.");
	}
	/**
	 * ※オーバーライドされるべきメソッド
	 * Rキーで実行するプラグインメソッド．
	 * 各サブクラスでキーに反応する機能を追加したい場合にオーバーライドする
	 */
	public void functionPluginR(){
		System.out.println("No Function in This Key.");
	}
	/**
	 * ※オーバーライドされるべきメソッド
	 * Tキーで実行するプラグインメソッド．
	 * 各サブクラスでキーに反応する機能を追加したい場合にオーバーライドする
	 */
	public void functionPluginT(){
		System.out.println("No Function in This Key.");
	}	/**
	 * ※オーバーライドされるべきメソッド
	 * Yキーで実行するプラグインメソッド．
	 * 各サブクラスでキーに反応する機能を追加したい場合にオーバーライドする
	 */
	public void functionPluginY(){
		System.out.println("No Function in This Key.");
	}


	/**
	 * タイマータスク．主に描画を行う．
	 * @author komota
	 *
	 */
	class RenderTask extends TimerTask{

		/*プライベートフィールド*/
		/** 直前のフレームの開始時刻*/
		long lasttime;
		/** 最新のフレームの開始時刻*/
		long starttime;

		/**
		 * コンストラクタ
		 */
		RenderTask(){
			this.starttime = System.currentTimeMillis() - (long)((double)1000/Statics.FRAME_RATE);
		}

		@Override
		/**
		 * runメソッド．主に描画を行う．
		 */
		public void run() {
			// TODO 自動生成されたメソッド・スタブ

			//直前のフレーム開始からこのフレーム開始までの時刻の差がフレームタイムになる．
			this.lasttime = this.starttime;
			this.starttime = System.currentTimeMillis();

			//フレームレートを推定
			MyFrame.this.frame_rate = Math.round((double)1000 / (this.starttime - this.lasttime));

			//累計フレーム数をインクリメント
			MyFrame.this.framecount++;

			Graphics2D g = (Graphics2D)MyFrame.this.buffer.getDrawGraphics();
			//背景の描画
			g.setColor(MyFrame.background);
			g.fillRect(0, 0, MyFrame.this.getWidth(), MyFrame.this.getHeight());
			//文字情報の描画
			g.setColor(MyFrame.colorofstring);
			g.drawString(MyFrame.this.expranation, Statics.SIZE_FRAME, Statics.SIZE_FRAME/2 + 10);
			g.drawString("frame:"+MyFrame.this.getFrameCount(), Statics.SIZE_FRAME+Statics.NUMBEROFPANEL*(Statics.SIZE_PANEL+Statics.SIZE_SEPALATOR)+5, Statics.SIZE_FRAME+10);
			g.drawString("time :"+MyFrame.this.getFrameTime(), Statics.SIZE_FRAME+Statics.NUMBEROFPANEL*(Statics.SIZE_PANEL+Statics.SIZE_SEPALATOR)+5, Statics.SIZE_FRAME+30);
			g.drawString("fps  :"+MyFrame.this.getFrameRate(), Statics.SIZE_FRAME+Statics.NUMBEROFPANEL*(Statics.SIZE_PANEL+Statics.SIZE_SEPALATOR)+5, Statics.SIZE_FRAME+50);
			g.drawString("fps_m:"+MyFrame.this.getFrameTime()/MyFrame.this.getFrameCount(), Statics.SIZE_FRAME+Statics.NUMBEROFPANEL*(Statics.SIZE_PANEL+Statics.SIZE_SEPALATOR)+5, Statics.SIZE_FRAME+70);
			g.drawString(MyFrame.this.tasktitle, Statics.SIZE_FRAME, Statics.SIZE_FRAME/2 + 30);
			g.drawString(MyFrame.this.howtouse, Statics.SIZE_FRAME, Statics.SIZE_FRAME+Statics.NUMBEROFPANEL*(Statics.SIZE_PANEL+Statics.SIZE_SEPALATOR)+40);

			g.dispose();

			//空白パネルの描画
			for(int i=0;i<MyFrame.this.panels.length;i++){
				for(int j=0;j<MyFrame.this.panels[0].length;j++){
					MyFrame.this.panels[i][j].drawSpace();
				}
			}
			MyFrame.this.drawObjects();
			MyFrame.this.buffer.show();
		}
	}

	/**
	 * キーリスナー
	 * @author komota
	 *
	 */
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
	/**
	 * マウスリスナー
	 * @author komota
	 *
	 */
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
	/**
	 * パネルクラス
	 * @author komota
	 *
	 */
	public class MyPanel {

		//フィールド
		/**
		 * パネルの状態
		 */
		private int status;
		/**
		 * パネルの座標
		 */
		private int[] position;

		/**
		 * コンストラクタ
		 * @param status パネルの初期状態
		 * @param position パネルの座標
		 */
		public MyPanel(int status,int[] position){
			this.status = status;
			this.position = position;
		}
		/**
		 * パネルの状態のセッター
		 * @param status パネルの状態
		 */
		public void setStatus(int status){
			this.status = status;
		}
		/**
		 * パネルの状態のゲッター
		 * @return パネルの状態
		 */
		public int getStatus(){
			return this.status;
		}

		/**
		 * パネルを描画する.
		 * オブジェクトの描画はMyFrame.drawObject()で行うため，このメソッドで行うのは空きマスを白で描画するのみである．
		 */
		public void drawSpace(){
			Graphics2D  g = (Graphics2D)MyFrame.this.buffer.getDrawGraphics();
			g.setColor(MyFrame.colorofspace);
			g.fillRect(Statics.SIZE_FRAME + (Statics.SIZE_PANEL+Statics.SIZE_SEPALATOR)*this.position[1],Statics.SIZE_FRAME + (Statics.SIZE_PANEL+Statics.SIZE_SEPALATOR)*this.position[0],Statics.SIZE_PANEL,Statics.SIZE_PANEL);
			g.dispose();
		}
		/**
		 * クリックした位置が、このパネルかどうかを判定する.マウスリスナーによってクリックを検知した時，すべてのパネルのこのメソッドを実行してクリックされたパネルを探索する．
		 * @param x クリックされた地点のx座標
		 * @param y クリックされた地点のy座標
		 * @return クリックされた地点がこのパネル内ならtrue,そうでないならfalse
		 */
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
