package mahjong.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

public class ViewFrame extends JFrame{

	//プレイヤー
	private Player[] players = new Player[4];
	//進行役
	private Master master;
	//テーブル
	private Table[] table = new Table[4];

	//バッファストラテジ
	BufferStrategy buffer;

	//コンストラクタ
	public ViewFrame(Player player_1,Player player_2,Player player_3,Player player_4,Master master,Table table_1,Table table_2,Table table_3,Table table_4){
		this.players[0] = player_1;
		this.players[1] = player_2;
		this.players[2] = player_3;
		this.players[3] = player_4;

		this.master = master;

		this.table[0] = table_1;
		this.table[1] = table_2;
		this.table[2] = table_3;
		this.table[3] = table_4;

		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(1000,1000);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setVisible(true);

		this.createBufferStrategy(2);
		this.buffer = this.getBufferStrategy();
		this.setIgnoreRepaint(true);

		Timer t = new Timer();
		t.schedule(new RenderTask(), 0,20);
	}

	//表示
	public void draw(){
		Graphics2D g = (Graphics2D)this.buffer.getDrawGraphics();
		g.setColor(new Color(70,150,70));
		g.fillRect(0, 0, this.getWidth(), this.getHeight());

		//g.setColor(new Color(200,200,0));
		Color haicolor = Color.white;
		Color moji = Color.black;
//		g.setColor(new Color(240,240,240));

		//手牌表示１
		for(int i=0;i<this.players[0].getHand().getNumberOfHais();i++){
			g.setColor(haicolor);
			//g.fillRect(120+41*i, 700, 40, 60);
			g.setColor(moji);
			//g.drawString(this.players[0].getHand().lookHai(i).toString(), 120+41*i, 700);
			g.drawImage(this.players[0].getHand().lookHai(i).getImage(),220+41*i, 900, null);
		}
		//手牌表示２
		for(int i=0;i<this.players[1].getHand().getNumberOfHais();i++){
			g.setColor(haicolor);
			//g.fillRect(694, 660 - 41*i, 60, 40);
			g.setColor(moji);

			AffineTransform oldtr = g.getTransform();
			AffineTransform newtr = new AffineTransform();
			newtr.setToRotation(-90*Math.PI/180, 894 + 20, 760 - 41*i + 20);

			g.setTransform(newtr);
			g.drawImage(this.players[1].getHand().lookHai(i).getImage(),894, 760 - 41*i, null);
			g.setTransform(oldtr);
		}
		//手牌表示３
		for(int i=0;i<this.players[2].getHand().getNumberOfHais();i++){
			g.setColor(haicolor);
			//g.fillRect(654-41*i, 66, 40, 60);
			g.setColor(moji);

			AffineTransform oldtr = g.getTransform();
			AffineTransform newtr = new AffineTransform();
			newtr.setToRotation(-180*Math.PI/180, 754-41*i + 20, 86 + 20);

			g.setTransform(newtr);
			g.drawImage(this.players[2].getHand().lookHai(i).getImage(),754-41*i, 86, null);
			g.setTransform(oldtr);

		}
		//手牌表示４
		for(int i=0;i<this.players[3].getHand().getNumberOfHais();i++){
			g.setColor(haicolor);
			//g.fillRect(60, 126+41*i, 60, 40);
			g.setColor(moji);
			AffineTransform oldtr = g.getTransform();
			AffineTransform newtr = new AffineTransform();
			newtr.setToRotation(-270*Math.PI/180, 70 + 20, 236+41*i + 20);

			g.setTransform(newtr);
			g.drawImage(this.players[3].getHand().lookHai(i).getImage(),60, 226+41*i, null);
			g.setTransform(oldtr);

		}


		//河表示1
		for(int i=0;i<this.table[0].kawa.size();i++){
			g.setColor(haicolor);
			g.drawImage(this.table[0].kawa.get(i).getImage(),377 + 41 * (i % 6), 660 + 61 * (i / 6), null);
			//g.fillRect(377 + 41 * (i % 6), 660 + 61 * (i / 6), 40, 60);
		}
		//河表示2
		for(int i=0;i<this.table[1].kawa.size();i++){
			g.setColor(haicolor);

			AffineTransform oldtr = g.getTransform();
			AffineTransform newtr = new AffineTransform();
			newtr.setToRotation(-90*Math.PI/180, 660 + 61 * (i / 6) + 20, 623 - 41 * (i % 6) + 30);

			g.setTransform(newtr);
			g.drawImage(this.table[1].kawa.get(i).getImage(),660 + 61 * (i / 6),623 - 41 * (i % 6), null);
			//g.fillRect(660 + 61 * (i / 6),623 - 41 * (i % 6), 40, 60);
			g.setTransform(oldtr);
		}
		//河表示3
		for(int i=0;i<this.table[2].kawa.size();i++){
			g.setColor(haicolor);

			AffineTransform oldtr = g.getTransform();
			AffineTransform newtr = new AffineTransform();
			newtr.setToRotation(-180*Math.PI/180, 623 - 41 * (i % 6) + 20, 340 - 61 * (i / 6) + 30);

			g.setTransform(newtr);
			g.drawImage(this.table[2].kawa.get(i).getImage(), 623 - 41 * (i % 6), 340 - 61 * (i / 6), null);
			//g.fillRect( 623 - 41 * (i % 6), 340 - 61 * (i / 6), 40, 60);
			g.setTransform(oldtr);
		}
		//河表示4
		for(int i=0;i<this.table[3].kawa.size();i++){
			g.setColor(haicolor);

			AffineTransform oldtr = g.getTransform();
			AffineTransform newtr = new AffineTransform();
			newtr.setToRotation(-270*Math.PI/180, 340 - 61 * (i / 6) + 20, 377 + 41 * (i % 6) + 30);

			g.setTransform(newtr);
			g.drawImage(this.table[3].kawa.get(i).getImage(), 340 - 61 * (i / 6) , 377 + 41 * (i % 6), null);
			//g.fillRect( 340 - 61 * (i / 6) , 377 + 41 * (i % 6), 40, 60);
			g.setTransform(oldtr);
		}

		//その他の情報表示

		//残り山数表示
		g.drawImage(Hai.images[37],this.getWidth()/2-50,this.getHeight()/2-50,null);
		g.setColor(Color.white);
		g.setFont(new Font("Arial", Font.BOLD, 36));
		g.drawString("×"+(this.master.getYama().getNumberOfHais()-14), this.getWidth()/2+20, this.getHeight()/2);
		//ドラ表示
		Hai[] dora = this.master.getDora();
		for(int i=0;i<4;i++){
			if(dora[i] != null){
				g.drawImage(dora[i].getImage(),this.getWidth()/2-82+41*i,this.getHeight()/2+10,null);
			}
			else{
				g.drawImage(Hai.images[37],this.getWidth()/2-82+41*i,this.getHeight()/2+10,null);
			}
		}

		//表示
		g.dispose();
		buffer.show();
	}

	class RenderTask extends TimerTask{

		@Override
		public void run() {
			// TODO 自動生成されたメソッド・スタブ
			ViewFrame.this.draw();
		}

	}

}
