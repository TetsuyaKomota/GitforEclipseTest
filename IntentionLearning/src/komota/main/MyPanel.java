package komota.main;

import java.awt.Color;
import java.awt.Graphics2D;


public class MyPanel {

	//定数
	//パネルサイズ
	public static int SIZE_PANEL = 200;
	public static final int SIZE_FRAME = 100;
	public static final int SIZE_SEPALATOR = 1;

	/* *************************************************************************************************** */
	/* *************************************************************************************************** */
	//特徴量の数
	//テスト中は試しに、feature[0]を角度とする
	public static final int NUMBEROFFEATURE = 3;
	/* *************************************************************************************************** */
	/* *************************************************************************************************** */

	//フィールド
	//パネルの状態
	int status;
	//パネルの場所
	int[] position;
	//特徴量ベクトル
	double[] features;

	//コンストラクタ
	public MyPanel(int status,int[] position){
		this.status = status;
		this.position = position;

		/* *************************************************************************************************** */
		/* *************************************************************************************************** */
		//特徴量ベクトルの初期化
		this.features = new double[NUMBEROFFEATURE];
		for(int i=0;i<this.features.length;i++){
			this.features[i] = 0;
		}
		/* *************************************************************************************************** */
		/* *************************************************************************************************** */
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
	/* *************************************************************************************************** */
	/* *************************************************************************************************** */
	public MyPanel setFeatures(double[] features){
		if(features.length == NUMBEROFFEATURE){
			this.features = features;
		}
		return this;
	}
	public MyPanel setFeatures(){
		for(int i=0;i<this.features.length;i++){
			this.features[i] = 0;
		}
		return this;
	}
	public double[] getFeatures(){
		return this.features;
	}
	/* *************************************************************************************************** */
	/* *************************************************************************************************** */


	//描画
	public void draw(MyFrame frame){
		Graphics2D  g = (Graphics2D)frame.buffer.getDrawGraphics();
		switch(this.status){
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
		g.fillRect(SIZE_FRAME + (SIZE_PANEL+SIZE_SEPALATOR)*this.position[1],SIZE_FRAME + (SIZE_PANEL+SIZE_SEPALATOR)*this.position[0],SIZE_PANEL,SIZE_PANEL);
//		g.setColor(Color.black);
//		g.drawString(""+this.status, SIZE_FRAME + (SIZE_PANEL+SIZE_SEPALATOR)*this.position[0],SIZE_FRAME + (SIZE_PANEL+SIZE_SEPALATOR)*this.position[1]);
		g.dispose();
	}
	//以前のバージョン。MainFrameを完全に削除したらこれはいらない
	public void draw(MainFrame frame){
		Graphics2D  g = (Graphics2D)frame.buffer.getDrawGraphics();
		switch(this.status){
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
		g.fillRect(SIZE_FRAME + (SIZE_PANEL+SIZE_SEPALATOR)*this.position[0],SIZE_FRAME + (SIZE_PANEL+SIZE_SEPALATOR)*this.position[1],SIZE_PANEL,SIZE_PANEL);
//		g.setColor(Color.black);
//		g.drawString(""+this.status, SIZE_FRAME + (SIZE_PANEL+SIZE_SEPALATOR)*this.position[0],SIZE_FRAME + (SIZE_PANEL+SIZE_SEPALATOR)*this.position[1]);
		g.dispose();
	}

	//クリックした位置が、このパネルかどうかを判定する
	public boolean isClicked(int x,int y){
		if(		x>=SIZE_FRAME + (SIZE_PANEL+SIZE_SEPALATOR)*this.position[1]
			&&	x<=SIZE_FRAME + (SIZE_PANEL+SIZE_SEPALATOR)*this.position[1] + SIZE_PANEL
			&&	y>=SIZE_FRAME + (SIZE_PANEL+SIZE_SEPALATOR)*this.position[0]
			&&	y<=SIZE_FRAME + (SIZE_PANEL+SIZE_SEPALATOR)*this.position[0] + SIZE_PANEL){
			return true;
		}
		return false;
	}

}
