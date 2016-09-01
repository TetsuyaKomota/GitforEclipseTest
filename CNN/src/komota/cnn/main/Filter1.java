package komota.cnn.main;

public class Filter1 extends Image {

	public Filter1(int x, int y,double range) {
		super(x, y,range);
		// TODO 自動生成されたコンストラクター・スタブ
		for(int j=0;j<this.Y_axis;j++){
			for(int i=0;i<this.X_axis;i++){
//				this.dots[i][j] = (double)(Math.abs(this.X_axis/2 - i))/(this.X_axis/2);
				if(i <range){
					this.dots[i][j] = 1;
				}
				else{
					this.dots[i][j] = 0;
				}
			}
		}
	}

}
