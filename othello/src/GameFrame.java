import java.util.Vector;

//更新しましたよ


public class GameFrame extends MyFrame{
	public void run(){
		GameWorld.player=new Player(1,1);
		addKeyListener(GameWorld.player);
		GameWorld.tokens=new Vector<Token>();
		GameWorld.savefile=new Vector<Token>();
		while(true){
			Remove();
			SelectRange();
			start();
			initialize();
			SelectTurn();
			Alldraw();
			while(CheckAllPutable(1)||CheckAllPutable(2)){
				int t=GameWorld.turn;
				CheckPass(1);
				if(CheckAllPutable(1)){
					RepresentPutable(1);
					if(t==0||t==2){
						PLAYER(1);
					}
					if(t==1||t==3){
						CPU4(1);
					}
				}
				CheckPass(2);
				if(CheckAllPutable(2)){
					RepresentPutable(2);
					if(t==0||t==3){
						CPU4(2);
					}
					if(t==1||t==2){
						PLAYER(2);
					}
				}
			}
			CheckWinner();
		}
	}

	public void Remove(){
		int size=GameWorld.tokens.size();
		for(int i=0;i<size;i++){
			GameWorld.tokens.remove(0);
			GameWorld.savefile.remove(0);
		}
	}
	public void start(){
		int r=GameWorld.range;
		for(int i=0;i<2*r+2;i++){
			for(int j=0;j<2*r+2;j++){
				GameWorld.tokens.add(new Token(30+30*j,30+30*i,0,0));
				GameWorld.savefile.add(new Token(0,0,0,0));
			}
		}
	}
	public void initialize(){
		GameWorld.PushSpace=0;
		GameWorld.SelectPlayer=0;
		int r=GameWorld.range;
		for(int i=0;i<GameWorld.tokens.size();i++){
			GameWorld.tokens.get(i).n=0;
			GameWorld.tokens.get(i).p=0;
			GameWorld.savefile.get(i).n=0;
			GameWorld.savefile.get(i).p=0;
		}
		for(int i=0;i<2*r+2;i++){
			GameWorld.tokens.get(i).n=3;
			GameWorld.savefile.get(i).p=3;
		}
		for(int i=(2*r+2)*(2*r+1);i<(2*r+2)*(2*r+2);i++){
			GameWorld.tokens.get(i).n=3;
			GameWorld.savefile.get(i).p=3;
		}
		for(int i=0;i<2*r+2;i++){
			GameWorld.tokens.get((2*r+2)*i).n=3;
			GameWorld.savefile.get((2*r+2)*i).p=3;
		}
		for(int i=1;i<2*r+3;i++){
			GameWorld.tokens.get((2*r+2)*i-1).n=3;
			GameWorld.savefile.get((2*r+2)*i-1).p=3;
		}
		GameWorld.tokens.get((2*r+3)*r).n=1;
		GameWorld.tokens.get((2*r+3)*r+1).n=2;
		GameWorld.tokens.get((2*r+3)*(r+1)-1).n=2;
		GameWorld.tokens.get((2*r+3)*(r+1)).n=1;
		GameWorld.savefile.get((2*r+3)*r).p=1;
		GameWorld.savefile.get((2*r+3)*r+1).p=2;
		GameWorld.savefile.get((2*r+3)*(r+1)-1).p=2;
		GameWorld.savefile.get((2*r+3)*(r+1)).p=1;
	}
	public void StartDrawturn(){
		int r=GameWorld.range;
		clear();
		GameWorld.player.draw(this);
		setColor(0,0,0);
		fillOval(30+30*(2*r+2)-5,30+30*(r+1),10,10);
		drawString("Player vs CPU",30+30*(2*r+2)+10,30+30*(r+1)+10,15);
		fillOval(30+30*(2*r+2)-5,30+30*(r+1)+30,10,10);
		drawString("CPU vs Player",30+30*(2*r+2)+10,30+30*(r+1)+40,15);
		fillOval(30+30*(2*r+2)-5,30+30*(r+1)+60,10,10);
		drawString("Player vs Player",30+30*(2*r+2)+10,30+30*(r+1)+70,15);
		fillOval(30+30*(2*r+2)-5,30+30*(r+1)+90,10,10);
		drawString("CPU vs CPU",30+30*(2*r+2)+10,30+30*(r+1)+100,15);
	}
	public void SelectTurn(){
		int r=GameWorld.range;
		int SP=0;

		GameWorld.SelectPlayer=0;
		GameWorld.player.x=2*r+2;
		GameWorld.player.y=r+1;
		StartDrawturn();
		while(SP==0){
			if(GameWorld.PushKey==1){
				if(GameWorld.player.x!=2*r+2){
					GameWorld.player.x=2*r+2;
				}
				if(GameWorld.player.y<r+1){
					GameWorld.player.y=r+1;
				}
				if(GameWorld.player.y>r+4){
					GameWorld.player.y=r+4;
				}
				StartDrawturn();
				GameWorld.PushKey=0;
			}

			if(GameWorld.PushSpace==1){
				SP=1;
				GameWorld.PushSpace=0;
			}
			sleep(0.1);
		}
		if(GameWorld.player.y==r+1){
			GameWorld.turn=0;
			GameWorld.SelectPlayer=1;
		}
		if(GameWorld.player.y==r+2){
			GameWorld.turn=1;
			GameWorld.SelectPlayer=1;
		}
		if(GameWorld.player.y==r+3){
			GameWorld.turn=2;
			GameWorld.SelectPlayer=1;
		}
		if(GameWorld.player.y==r+4){
			GameWorld.turn=3;
			GameWorld.SelectPlayer=1;
		}

		GameWorld.player.x=1;
		GameWorld.player.y=1;
	}
	public void StartDrawRange(){
		int r=GameWorld.range;
		clear();
		GameWorld.player.draw(this);
		setColor(0,0,0);
		fillOval(30+30*(2*r+2)-5,30+30*(r+1),10,10);
		drawString("Field Range=6",30+30*(2*r+2)+10,30+30*(r+1)+10,15);
		fillOval(30+30*(2*r+2)-5,30+30*(r+1)+30,10,10);
		drawString("Field Range=8",30+30*(2*r+2)+10,30+30*(r+1)+40,15);
		fillOval(30+30*(2*r+2)-5,30+30*(r+1)+60,10,10);
		drawString("Field Range=10",30+30*(2*r+2)+10,30+30*(r+1)+70,15);
		fillOval(30+30*(2*r+2)-5,30+30*(r+1)+90,10,10);
		drawString("field Range=12",30+30*(2*r+2)+10,30+30*(r+1)+100,15);
	}
	public void SelectRange(){
		int r=GameWorld.range;
		int SP=0;

		GameWorld.SelectPlayer=0;
		GameWorld.PushSpace=0;
		GameWorld.player.x=2*r+2;
		GameWorld.player.y=r+1;
		StartDrawRange();
		while(SP==0){
			if(GameWorld.PushKey==1){
				if(GameWorld.player.x!=2*r+2){
					GameWorld.player.x=2*r+2;
				}
				if(GameWorld.player.y<r+1) {
					GameWorld.player.y=r+1;
				}
				if(GameWorld.player.y>r+4) {
					GameWorld.player.y=r+4;
				}
				StartDrawRange();
				GameWorld.PushKey=0;
			}
			if(GameWorld.PushSpace==1){
				SP=1;
				GameWorld.PushSpace=0;
			}
			sleep(0.1);
		}
		GameWorld.range=GameWorld.player.y-(r-2);
		GameWorld.player.x=1;
		GameWorld.player.y=1;
		SP=0;
	}
	public void Alldraw(){
		int r=GameWorld.range;
		clear();
		setColor(0,0,0);
		drawString("Black "+CountBlack(),30+30*(2*r+2)+10,30+30*(r+1)+10,15);
		drawString("White "+(CountAll()-CountBlack()),30+30*(2*r+2)+10,30+30*(r+1)+40,15);
		GameWorld.player.draw(this);
		for(int i=0;i<GameWorld.tokens.size();i++){
			GameWorld.tokens.get(i).draw(this);
		}
		GameWorld.PushSpace=0;
		GameWorld.oita=0;

	}
	public void CheckRebirth(int l,int s,int m){
		int k=l+s;
		int c=GameWorld.tokens.get(k).n;
		int i=0;
		while(c==3-m){
			c=GameWorld.tokens.get(k+s).n;
			k=k+s;
			i++;
		}
		if(c==m){
			while(i!=0){
				GameWorld.tokens.get(k-s).n=m;
				k=k-s;
				i--;
			}
		}
	}
	public void PutToken(int l,int m){
		int r=GameWorld.range;
		GameWorld.tokens.get(l).n=m;
		for(int i=0;i<GameWorld.tokens.size();i++){
			GameWorld.tokens.get(i).p=0;
		}
		CheckRebirth(l,1,m);
		CheckRebirth(l,-1,m);
		CheckRebirth(l,2*r+3,m);
		CheckRebirth(l,-2*r-3,m);
		CheckRebirth(l,2*r+2,m);
		CheckRebirth(l,-2*r-2,m);
		CheckRebirth(l,2*r+1,m);
		CheckRebirth(l,-2*r-1,m);
		GameWorld.tokens.get(l).p=2;
	}
	public boolean CheckLinePutable(int l,int s,int m){
		int k=l+s;
		int c=GameWorld.tokens.get(k).n;
		if(c==3-m) {
			c=GameWorld.tokens.get(k+s).n;
			k=k+s;
			while(c==3-m){
				c=GameWorld.tokens.get(k+s).n;
				k=k+s;
			}
			if(c==m) return true;
		}
		return false;
	}
	public boolean CheckPutable(int l,int m){
		int r=GameWorld.range;
		if(GameWorld.tokens.get(l).n==0){
			if(CheckLinePutable(l,1,m)
				||CheckLinePutable(l,-1,m)
				||CheckLinePutable(l,2*r+1,m)
				||CheckLinePutable(l,-2*r-1,m)
				||CheckLinePutable(l,2*r+2,m)
				||CheckLinePutable(l,-2*r-2,m)
				||CheckLinePutable(l,2*r+3,m)
				||CheckLinePutable(l,-2*r-3,m)) return true;
		}
		return false;
	}
	public void RepresentPutable(int m){
		for(int i=0;i<GameWorld.tokens.size();i++){
			if(CheckPutable(i,m)){
				GameWorld.tokens.get(i).p=1;
			}
		}
		Alldraw();
	}
	public int CheckLineHowMany(int l,int s,int m){
		int k=l+s;
		int c=GameWorld.tokens.get(k).n;
		int i=0;
		while(c==3-m){
			c=GameWorld.tokens.get(k+s).n;
			k=k+s;
			i++;
		}
		if(c==m) return i;
		else return 0;
	}
	public int CheckHowMany(int l,int m){
		int r=GameWorld.range;
		int i=CheckLineHowMany(l,1,m)
				+CheckLineHowMany(l,-1,m)
				+CheckLineHowMany(l,2*r+1,m)
				+CheckLineHowMany(l,-2*r-1,m)
				+CheckLineHowMany(l,2*r+2,m)
				+CheckLineHowMany(l,-2*r-2,m)
				+CheckLineHowMany(l,2*r+3,m)
				+CheckLineHowMany(l,-2*r-3,m);
		return i;
	}
	public void SaveBefore(){
		for(int i=0;i<GameWorld.tokens.size();i++){
			GameWorld.savefile.get(i).n=GameWorld.tokens.get(i).n;
		}
	}
	public void SaveAfter(){
		for(int i=0;i<GameWorld.tokens.size();i++){
			GameWorld.savefile.get(i).p=GameWorld.tokens.get(i).n;
		}
	}
	public void RoadBefore(){
		for(int i=0;i<GameWorld.tokens.size();i++){
			GameWorld.tokens.get(i).n=GameWorld.savefile.get(i).n;
			GameWorld.tokens.get(i).p=0;
		}
	}
	public void RoadAfter(){
		for(int i=0;i<GameWorld.tokens.size();i++){
			GameWorld.tokens.get(i).n=GameWorld.savefile.get(i).p;
		}
	}
	//beforeは「プレイヤーの打つ直前のデータ」
	//afterは「プレイヤーの打った直後のデータ」
	public void PLAYER(int k){
		int oita=GameWorld.oita;
		int lo=0;
		int r=GameWorld.range;
		while(CheckAllPutable(k)&&oita==0){
			if(GameWorld.PushKey==1){
				Alldraw();
				GameWorld.PushKey=0;
			}
			if(GameWorld.PushBackSpace==1){
				RoadBefore();
				SaveAfter();
				RepresentPutable(k);
				Alldraw();
				GameWorld.PushBackSpace=0;
			}
			if(GameWorld.PushSpace==1){
				lo=GameWorld.player.x+GameWorld.player.y*(2*r+2);
				if(CheckPutable(lo,k)){
					SaveBefore();
					PutToken(lo,k);
					SaveAfter();
					oita=1;
					Alldraw();
				}
				GameWorld.PushSpace=0;
			}
		}
	}
	public int NextHowmany(int m){
		int i=0;
		for(int j=0;j<GameWorld.tokens.size();j++){
			if(CheckPutable(j,m)) i++;
		}
		return i;
	}

	public int LeastPutable(int m){
		//次の3-mの置けるところが一番少なくなるmの場所を出力するメソッド
		int i=1000;
		int lo=0;
		for(int j=0;j<GameWorld.tokens.size();j++){
			if(CheckPutable(j,m)){
				PutToken(j,m);
				if(NextHowmany(3-m)<i){
					i=NextHowmany(3-m);
					lo=j;
				}
				RoadAfter();
			}
		}
		return lo;
	}
	public int MostLeastPutable(int m){
		//mが次のLeastPutableが最大になるように置く場合のLeastPutableの値
		int i=0;
		for(int j=0;j<GameWorld.tokens.size();j++){
			if(CheckPutable(j,m)){
				PutToken(j,m);
				if(LeastPutable(3-m)>i){
					i=LeastPutable(3-m);
				}
				RoadAfter();
			}
		}
		return i;
	}
	public boolean CheckCornerPutable(int m){
		int r=GameWorld.range;
		//mが角におけるか判定する//
		if(CheckPutable(2*r+3,m)
			||CheckPutable(((2*r)+2)*2-2,m)
			||CheckPutable((2*r+2)*(2*r)+1,m)
			||CheckPutable((2*r+2)*(2*r+1)-2,m))return true;
		else return false;
	}
	public int LimitedLeastPutable(int m){
		//次の一手で角を打たれるところを除いたLeastPutable//
		int i=1000;
		int lo=0;
		for(int j=0;j<GameWorld.tokens.size();j++){
			if(CheckPutable(j,m)){
				PutToken(j,m);
				if(!CheckCornerPutable(3-m)&&NextHowmany(3-m)<i){
					i=NextHowmany(3-m);
					lo=j;
				}
				RoadAfter();
			}
		}
		return lo;

	}

	public void CPU(int k){
		int ma=0;
		int lo=0;
		sleep(0.5);
		for(int i=0;i<GameWorld.tokens.size();i++){
			int c=GameWorld.tokens.get(i).n;
			if(c==0){
				if(CheckPutable(i,k)){
					if(CheckHowMany(i,k)>ma){
						ma=CheckHowMany(i,k);
						lo=i;
					}
				}
			}
		}
		PutToken(lo,k);
		Alldraw();
	}
	public void CPU2(int k){
		sleep(0.5);
		PutToken(LeastPutable(k),k);
		SaveAfter();
		Alldraw();
	}
	//CPU3は失敗作//
	public void CPU3(int k){
		int i=1000;
		int lo=0;
		sleep(0.5);
		for(int j=0;j<GameWorld.tokens.size();j++){
			if(CheckPutable(j,k)){
				PutToken(j,k);
				if(MostLeastPutable(3-k)<i){
					i=MostLeastPutable(3-k);
					lo=j;
				}
				RoadAfter();
			}
		}
		PutToken(lo,k);
		SaveAfter();
		Alldraw();
	}

	public void CPU4(int k){
		int r=GameWorld.range;
		int o=0;
		sleep(0.5);
		//角に置ける時はまず角に置く//
		if(o==0&&CheckPutable(2*r+3,k)){
			PutToken(2*r+3,k);
			o=1;
		}
		if(o==0&&CheckPutable(((2*r)+2)*2-2,k)){
			PutToken(((2*r)+2)*2-2,k);
			o=1;
		}
		if(o==0&&CheckPutable((2*r+2)*(2*r)+1,k)){
			PutToken((2*r+2)*(2*r)+1,k);
			o=1;
		}
		if(o==0&&CheckPutable((2*r+2)*(2*r+1)-2,k)){
			PutToken((2*r+2)*(2*r+1)-2,k);
			o=1;
		}
		if(o==0&&LimitedLeastPutable(k)!=0){
			PutToken(LimitedLeastPutable(k),k);
			o=1;
		}
		if(o==0)PutToken(LeastPutable(k),k);
		o=0;
		SaveAfter();
		Alldraw();
	}

	public boolean CheckAllPutable(int m){
		for(int i=0;i<GameWorld.tokens.size();i++){
			int c=GameWorld.tokens.get(i).n;
			if(c==0){
				if(CheckPutable(i,m)) return true;
			}
		}
		return false;
	}
	public int CountAll(){
		int i=0;
		for(int j=0;j<GameWorld.tokens.size();j++){
			int c=GameWorld.tokens.get(j).n;
			if(c==1||c==2) i++;
		}
		return i;
	}
	public int CountBlack(){
		int i=0;
		for(int j=0;j<GameWorld.tokens.size();j++){
			int c=GameWorld.tokens.get(j).n;
			if(c==1) i++;
		}
		return i;
	}
	public void CheckPass(int m){
		int r=GameWorld.range;
		int X=0;
		int Y=0;
		if(CheckAllPutable(m));
		else{
			if(CheckAllPutable(3-m)){
				setColor(0,0,0);
				if(m==1)drawString("黒はパスです",30+30*(2*r+2)+10,30+30*(r+1)+70,15);
				if(m==2)drawString("白はパスです",30+30*(2*r+2)+10,30+30*(r+1)+70,15);
				X=GameWorld.player.x;
				Y=GameWorld.player.y;
				while(GameWorld.PushSpace==0){
					sleep(0.1);
					GameWorld.player.x=X;
					GameWorld.player.y=Y;
				}
				Alldraw();
				SaveAfter();
				GameWorld.PushSpace=0;
			}
		}
	}
	public void CheckWinner(){
		int r=GameWorld.range;
		int t=GameWorld.turn;
		if(t==0){
			if(2*CountBlack()>CountAll()){
				GameWorld.PushSpace=0;
				setColor(0,0,0);
				drawString("You Win!!",30+30*(2*r+2)+10,30+30*(r+1)+70,15);
			}
			if(2*CountBlack()==CountAll()){
				GameWorld.PushSpace=0;
				setColor(0,0,0);
				drawString("Draw",30+30*(2*r+2)+10,30+30*(r+1)+70,15);
			}
			if(2*CountBlack()<CountAll()){
				GameWorld.PushSpace=0;
				setColor(0,0,0);
				drawString("You Lose...",30+30*(2*r+2)+10,30+30*(r+1)+70,15);
			}
			while(GameWorld.PushSpace==0);
		}
		if(t==1){
			if(2*CountBlack()>CountAll()){
				GameWorld.PushSpace=0;
				setColor(0,0,0);
				drawString("You Lose...",30+30*(2*r+2)+10,30+30*(r+1)+70,15);
			}
			if(2*CountBlack()==CountAll()){
				GameWorld.PushSpace=0;
				setColor(0,0,0);
				drawString("Draw",30+30*(2*r+2)+10,30+30*(r+1)+70,15);
			}
			if(2*CountBlack()<CountAll()){
				GameWorld.PushSpace=0;
				setColor(0,0,0);
				drawString("You Win!!",30+30*(2*r+2)+10,30+30*(r+1)+70,15);
			}
			while(GameWorld.PushSpace==0);
		}
		if(t==2||t==3){
			if(2*CountBlack()>CountAll()){
				GameWorld.PushSpace=0;
				setColor(0,0,0);
				drawString("Black Win!!",30+30*(2*r+2)+10,30+30*(r+1)+70,15);
			}
			if(2*CountBlack()==CountAll()){
				GameWorld.PushSpace=0;
				setColor(0,0,0);
				drawString("Draw",30+30*(2*r+2)+10,30+30*(r+1)+70,15);
			}
			if(2*CountBlack()<CountAll()){
				GameWorld.PushSpace=0;
				setColor(0,0,0);
				drawString("White Win!!",30+30*(2*r+2)+10,30+30*(r+1)+70,15);
			}
			while(GameWorld.PushSpace==0);
		}
	}


}
