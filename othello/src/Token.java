
public class Token {
	int x ,y ,n ,p;
	//n=0は「空きコマ」n=1は「黒コマ」n=2は「白コマ」n=3は「外枠」//
	//p=1は「次におけるところ」p=2は「直前においたところ」//
	
	public Token(int x,int y,int n,int p){
		this.x=x;
		this.y=y;
		this.n=n;
		this.p=p;
	}
	public void draw(MyFrame f){
		if(n==0){
			f.setColor(255, 255, 255);
			f.fillOval(x,y,30,30);
		}
		if(n==1){
			f.setColor(0, 0, 0);
			f.fillOval(x,y,30,30);
		}
		if(n==2){
			f.setColor(0, 0, 0);
			f.fillOval(x,y,30,30);
			f.setColor(255, 255, 255);
			f.fillOval(x+5, y+5, 20, 20);
		}
		if(n==3){
			f.setColor(0, 128, 0);
			f.fillRect(x,y,30,30);
		}
		if(p==1){
			f.setColor(64, 64, 255);
			f.fillRect(x+10,y+10,10,10);
		}
		if(p==2){
			f.setColor(255,64 , 64);
			f.fillRect(x,y,30,1);
			f.fillRect(x,y,1,30);
			f.fillRect(x,y+29,30,1);
			f.fillRect(x+29,y,1,30);
		}
	}

}
