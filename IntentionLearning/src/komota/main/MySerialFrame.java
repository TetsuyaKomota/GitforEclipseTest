package komota.main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MySerialFrame extends MyFrame{

	//描画時、単にオブジェクトに対して大きい四角を上書きすればいいだけの話だった
	//粒度の高い状態空間で、オブジェクトを操作しやすいように改良した

	public static void main(String[] args){
		MySerialFrame frame = new MySerialFrame();
	}

	//定数
	//オブジェクトサイズ（パネルサイズ何枚分か）
	static final int SIZE_OBJECT = 10;

	//コンストラクタ
	public MySerialFrame(){
		super();
		//マウスリスナーを設定し直す
		this.addMouseListener(new MySerialMouseListener());
	}


	//描画時、大きい四角を上書きする
	@Override
	public void draw(){
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
					g.fillRect(MyPanel.SIZE_FRAME+(MyPanel.SIZE_PANEL+MyPanel.SIZE_SEPALATOR)*j - ((MyPanel.SIZE_PANEL+MyPanel.SIZE_SEPALATOR)*SIZE_OBJECT)/2,MyPanel.SIZE_FRAME+(MyPanel.SIZE_PANEL+MyPanel.SIZE_SEPALATOR)*i - ((MyPanel.SIZE_PANEL+MyPanel.SIZE_SEPALATOR)*SIZE_OBJECT)/2,(MyPanel.SIZE_PANEL+MyPanel.SIZE_SEPALATOR)*SIZE_OBJECT,(MyPanel.SIZE_PANEL+MyPanel.SIZE_SEPALATOR)*SIZE_OBJECT);

					if(this.selected[0] == i && this.selected[1] == j){
						g.setColor(Color.black);
						g.drawRect(MyPanel.SIZE_FRAME+(MyPanel.SIZE_PANEL+MyPanel.SIZE_SEPALATOR)*j - ((MyPanel.SIZE_PANEL+MyPanel.SIZE_SEPALATOR)*SIZE_OBJECT)/2,MyPanel.SIZE_FRAME+(MyPanel.SIZE_PANEL+MyPanel.SIZE_SEPALATOR)*i - ((MyPanel.SIZE_PANEL+MyPanel.SIZE_SEPALATOR)*SIZE_OBJECT)/2,(MyPanel.SIZE_PANEL+MyPanel.SIZE_SEPALATOR)*SIZE_OBJECT,(MyPanel.SIZE_PANEL+MyPanel.SIZE_SEPALATOR)*SIZE_OBJECT);
					}
					else if(this.secondselected[0] == i && this.secondselected[1] == j){
						g.setColor(new Color(255,100,200));
						g.drawRect(MyPanel.SIZE_FRAME+(MyPanel.SIZE_PANEL+MyPanel.SIZE_SEPALATOR)*j - ((MyPanel.SIZE_PANEL+MyPanel.SIZE_SEPALATOR)*SIZE_OBJECT)/2,MyPanel.SIZE_FRAME+(MyPanel.SIZE_PANEL+MyPanel.SIZE_SEPALATOR)*i - ((MyPanel.SIZE_PANEL+MyPanel.SIZE_SEPALATOR)*SIZE_OBJECT)/2,(MyPanel.SIZE_PANEL+MyPanel.SIZE_SEPALATOR)*SIZE_OBJECT,(MyPanel.SIZE_PANEL+MyPanel.SIZE_SEPALATOR)*SIZE_OBJECT);
					}
				}
			}
		}
		g.dispose();
		this.buffer.show();
	}

	/* **************************************************************************************************************************** */
	//マウスリスナー。オブジェクトがパネルごとでなくなるため、クリックの判定も個別に作成しなければならない
	class MySerialMouseListener implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO 自動生成されたメソッド・スタブ
			for(int i=0;i<MySerialFrame.this.panels.length;i++){
				for(int j=0;j<MySerialFrame.this.panels[0].length;j++){
					if(MySerialFrame.this.panels[i][j].isClicked(e.getPoint().x, e.getPoint().y)){
						//クリックされたパネルは、オブジェクトの裏に隠れた空きパネル、オブジェクトの外の空きパネル（本当の空きパネル）、オブジェクトパネルの3通り
						if(MySerialFrame.this.panels[i][j].getStatus() == 0){
							int temp[] = new int[2];
							temp[0] = -1;
							temp[1] = -1;
							for(int a=0;a<MySerialFrame.SIZE_OBJECT;a++){
								for(int b=0;b<MySerialFrame.SIZE_OBJECT;b++){
									if(i-(MySerialFrame.SIZE_OBJECT/2)+a<0||j-(MySerialFrame.SIZE_OBJECT/2)+b<0||i-(MySerialFrame.SIZE_OBJECT/2)+a>MyFrame.NUMBEROFPANEL-1 || j-(MySerialFrame.SIZE_OBJECT/2)+b>MyFrame.NUMBEROFPANEL-1){
									}
									else if(MySerialFrame.this.panels[i-(MySerialFrame.SIZE_OBJECT/2)+a][j-(MySerialFrame.SIZE_OBJECT/2)+b].status>0){
										temp[0] = i-(MySerialFrame.SIZE_OBJECT/2)+a;
										temp[1] = j-(MySerialFrame.SIZE_OBJECT/2)+b;
									}
								}
							}
							if(temp[0] != -1 && temp[1] != -1){
								MySerialFrame.this.selected[0] = temp[0];
								MySerialFrame.this.selected[1] = temp[1];
							}
							else{
								MySerialFrame.this.secondselected[0] = i;
								MySerialFrame.this.secondselected[1] = j;
							}
						}
						else{
							MySerialFrame.this.selected[0] = i;
							MySerialFrame.this.selected[1] = j;
						}
/*
						if(MySerialFrame_Test.this.selected[0] == -1 && MySerialFrame_Test.this.selected[1] == -1 && MySerialFrame_Test.this.panels[i][j].getStatus() != 0){
							MySerialFrame_Test.this.selected[0] = i;
							MySerialFrame_Test.this.selected[1] = j;
						}
						else if(MySerialFrame_Test.this.secondselected[0] == -1 && MySerialFrame_Test.this.secondselected[1] == -1 && (MySerialFrame_Test.this.selected[0] != i || MySerialFrame_Test.this.selected[1] != j) && MySerialFrame_Test.this.panels[i][j].getStatus() == 0){
							MySerialFrame_Test.this.secondselected[0] = i;
							MySerialFrame_Test.this.secondselected[1] = j;
						}
*/
						break;
					}
				}
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO 自動生成されたメソッド・スタブ
			for(int i=0;i<MySerialFrame.this.panels.length;i++){
				for(int j=0;j<MySerialFrame.this.panels[0].length;j++){
					if(MySerialFrame.this.panels[i][j].isClicked(e.getPoint().x, e.getPoint().y)){
						if(MySerialFrame.this.selected[0] == -1 && MySerialFrame.this.selected[1] == -1 && MySerialFrame.this.panels[i][j].getStatus() != 0){
							MySerialFrame.this.selected[0] = i;
							MySerialFrame.this.selected[1] = j;
						}
						else if(MySerialFrame.this.secondselected[0] == -1 && MySerialFrame.this.secondselected[1] == -1 && (MySerialFrame.this.selected[0] != i || MySerialFrame.this.selected[1] != j) && MySerialFrame.this.panels[i][j].getStatus() == 0){
							MySerialFrame.this.secondselected[0] = i;
							MySerialFrame.this.secondselected[1] = j;
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
