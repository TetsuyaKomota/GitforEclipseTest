package komota.crowd;

public class CrowdSimu {

	public static void main(String[] args){
		CrowdSimu simu = new CrowdSimu();
		int count = 0;
		while(count++ < 100000){
			simu.show();
			simu.move();
			if(simu.isSolved()){
				System.out.println(count+"ステップで渋滞が解消しました");
				return;
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}
	}
	int[] corwd;

	public CrowdSimu(){
		this.corwd = new int[100];
		for(int i=0;i<this.corwd.length;i++){
			if(Math.random() > 0.6){
				this.corwd[i] = 0;
			}
			else{
				this.corwd[i] = 1;
			}
		}
	}

	public void show(){
		for(int i=0;i<this.corwd.length;i++){
			//System.out.print(this.corwd[i]+"");
			if(this.corwd[i] == 0){
				System.out.print("□");
			}
			else{
				System.out.print("■");
			}
		}
		System.out.println();
	}
	public void move(){
		int[] next = new int[this.corwd.length];
		for(int i=next.length-1;i>=0;i--){
			if(i == next.length-1){
				next[i] = 0;
			}
			else if(this.corwd[i] == 1 && this.corwd[i+1] == 0){
				next[i] = 0;
			}
			else if(this.corwd[i] == 1 && this.corwd[i+1] == 1){
				next[i] = 1;
			}
			else if(i == 0){
				next[i] = 1;
			}
			else if(this.corwd[i] == 0 && this.corwd[i-1] == 0){
				next[i] = 0;
			}
			else if(this.corwd[i] == 0 && this.corwd[i-1] == 1){
				next[i] = 1;
			}
			else{
				next[i] = -1;
			}
		}
		for(int i=0;i<this.corwd.length;i++){
			this.corwd[i] = next[i];
		}
	}
	public boolean isSolved(){
		if(this.corwd[0] == 0){
			for(int i=0;i<this.corwd.length-1;i++){
				if(this.corwd[i] != i%2){
					return false;
				}
			}
		}
		else{
			for(int i=0;i<this.corwd.length;i++){
				if(this.corwd[i] != (i+1)%2){
					return false;
				}
			}
		}
		return true;
	}

}
