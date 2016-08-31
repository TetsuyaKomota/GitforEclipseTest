package komota.modes;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import komota.libs.MyIO;
import komota.libs.Statics;
import komota.main.MyFrame;
import komota.main.MyKeyListener;
import komota.supers.Mode;

public abstract class Manzai_Mode_v2 extends Mode{

	//背景
	protected BufferedImage background;

	//人の配置．4か所限定
	protected Manzai_Chara[] chara;

	//人の描画間隔．画像サイズを考慮して等間隔になるようにする
	private int chara_interval;

	//セリフ欄
	protected BufferedImage column;

	//セリフセット（4行）
	protected String[] messages;
	//現在のセリフ番号
	protected int currentamessage;

	//シナリオファイルを呼び出すためのIOクラス
	MyIO io;

	//シナリオファイルの読み込みフラグ．読み込みを止めておきたいときは false にする
	boolean commandflag = true;


	//コンストラクタ
	public Manzai_Mode_v2(MyFrame frame,MyKeyListener key) {
		super(frame,key);
		this.chara = new Manzai_Chara[4];
		this.messages = new String[4];
		this.io = new MyIO();
		this.io.readFile("index.txt");
	}

	//読み込んだシナリオファイルのコマンドに応じて処理を実行するメソッド．
	//基本的に漫才モードでは，run() の実行ごとにコマンドを読み込む．
	//テキストの表示中など，コマンドの読み込みをストップさせるフラグを管理することで，漫才シーンを実現する
	void command(){
		//読み込み停止中なら 何もしない
		if(this.commandflag == false){
			return ;
		}
		//一行読み込み
		String line = this.io.readLine();
		//command の内容によって動作を変更する
		switch(line.split(">>>")[0]){
		//初期化コマンドであった場合,初期化を実行する
		case "init":
			break;
		//シナリオ以降コマンドであった場合，次のシナリオファイルを読み込む
		case "next":
			break;
		//終了コマンドであった場合，プログラムの実行を終了する
		case "end":
			break;
		//command がない場合，エラーログを残してその行は無視する
		default:
			System.out.println("[Manzai_Mode_v2]command:command not found. ::"+line);
		}
	}

	@Override
	public void run() {
		// TODO 自動生成されたメソッド・スタブ

		//コマンドを読み込む
		this.command();

		Graphics2D g = (Graphics2D)this.frame.getBuffer().getDrawGraphics();
		g.drawImage(background, 0, 0, null);
		for(int i=0;i<this.chara.length;i++){
			if(this.chara[i] != null){
				g.drawImage(chara[i].getFace(), Statics.MANZAI_CHARA_INTERVAL+Statics.FRAME_WIDTH+(250+Statics.MANZAI_CHARA_INTERVAL)*i, 100, null);
			}
		}
		g.drawImage(column, 0, 430, null);
		g.setColor(Color.white);
		Font font = new Font("ＭＳ 明朝", Font.BOLD, 32);
	    g.setFont(font);
		g.drawString(this.messages[0], 50, 500);
		g.drawString(this.messages[1], 50, 540);
		g.drawString(this.messages[2], 50, 580);
		g.drawString(this.messages[3], 50, 620);
		g.dispose();
		this.frame.getBuffer().show();

		if(this.key.getKeyStatus(Statics.KEY_SPACE) == true){
			this.key.setKeyStatus(Statics.KEY_SPACE, false);
		}

	}

}
