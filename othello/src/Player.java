import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class Player implements KeyListener{
	int x,y;
	int r=GameWorld.range;
	
	public Player(int x,int y){
		this.x=x;
		this.y=y;
	}
	
	public void draw(MyFrame f){
		if(GameWorld.SelectPlayer==0){
			f.setColor(128,128,128);
			f.fillRect(20+30*x, 25+30*y, 3, 3);
			f.fillRect(20+30*x+17, 25+30*y+17, 3, 3);
			f.fillRect(20+30*x, 25+30*y+17, 3, 3);
			f.fillRect(20+30*x+17, 25+30*y, 3, 3);
		}
		if(GameWorld.SelectPlayer==1){
			f.setColor(0,128,0);
			f.fillRect(30+30*x, 30+30*y, 3, 3);
			f.fillRect(30+30*x+27, 30+30*y+27, 3, 3);
			f.fillRect(30+30*x, 30+30*y+27, 3, 3);
			f.fillRect(30+30*x+27, 30+30*y, 3, 3);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		if(y>1&&e.getKeyCode()==KeyEvent.VK_UP){
			y=y-1;
			GameWorld.PushKey=1;
		}
		if(GameWorld.SelectPlayer==0
			&&e.getKeyCode()==KeyEvent.VK_DOWN){
			y=y+1;
			GameWorld.PushKey=1;
		}
		if(GameWorld.SelectPlayer==1
			&&y<2*r
			&&e.getKeyCode()==KeyEvent.VK_DOWN){
			y=y+1;
			GameWorld.PushKey=1;
		}
		if(x>1&&e.getKeyCode()==KeyEvent.VK_LEFT){
			x=x-1;
			GameWorld.PushKey=1;
		}
		if(x<2*r&&e.getKeyCode()==KeyEvent.VK_RIGHT){
			x=x+1;
			GameWorld.PushKey=1;
		}
		if(e.getKeyCode()==KeyEvent.VK_SPACE){
			System.out.println("プッシュスペース");
			GameWorld.PushSpace=1;
			r=GameWorld.range;
		}
		if(e.getKeyCode()==KeyEvent.VK_B){
			System.out.println("プッシュバックスペース");
			GameWorld.PushBackSpace=1;
		}
		
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

}
