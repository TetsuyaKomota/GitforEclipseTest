package komota.cnn.main;

public class Filter2 extends Image {

	public Filter2(int x, int y,double range) {
		super(x, y ,range);
		// TODO 自動生成されたコンストラクター・スタブ
		for(int j=0;j<this.Y_axis;j++){
			for(int i=0;i<this.X_axis;i++){
/*
				this.dots[i][j] = (double)(Math.abs(i-j))/((this.X_axis+this.Y_axis)/2);
				if(this.dots[i][j] <= 0){
					this.dots[i][j] = 0;
				}
*/
				if(Math.abs(i-j)<range){
					this.dots[i][j] = 1;
				}
				else{
					this.dots[i][j] = 0;
				}
			}
		}
	}

}
