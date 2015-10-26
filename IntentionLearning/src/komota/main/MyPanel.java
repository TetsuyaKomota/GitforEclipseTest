package komota.main;

import java.awt.Color;
import java.awt.Graphics2D;

public class MyPanel {

	//定数
	//パネルサイズ
	static int SIZE_PANEL = 200;
	static final int SIZE_FRAME = 100;
	static final int SIZE_SEPALATOR = 0;

	//フィールド
	//パネルの状態
	int status;
	//パネルの場所
	int[] position;

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
		g.fillRect(SIZE_FRAME + (SIZE_PANEL+SIZE_SEPALATOR)*this.position[0],SIZE_FRAME + (SIZE_PANEL+SIZE_SEPALATOR)*this.position[1],SIZE_PANEL,SIZE_PANEL);
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
		if(		x>=SIZE_FRAME + (SIZE_PANEL+SIZE_SEPALATOR)*this.position[0]
			&&	x<=SIZE_FRAME + (SIZE_PANEL+SIZE_SEPALATOR)*this.position[0] + SIZE_PANEL
			&&	y>=SIZE_FRAME + (SIZE_PANEL+SIZE_SEPALATOR)*this.position[1]
			&&	y<=SIZE_FRAME + (SIZE_PANEL+SIZE_SEPALATOR)*this.position[1] + SIZE_PANEL){
			return true;
		}
		return false;
	}

}
