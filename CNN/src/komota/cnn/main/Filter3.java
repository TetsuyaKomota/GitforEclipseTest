package komota.cnn.main;

public class Filter3 extends Image {

	public Filter3(int x, int y ,double range) {
		super(x, y ,range);
		// TODO 自動生成されたコンストラクター・スタブ
		for(int j=0;j<this.Y_axis;j++){
			for(int i=0;i<this.X_axis;i++){
/*
				this.dots[i][j] = 0.3+(double)(Math.sqrt(Math.abs(this.X_axis/2 - i)*Math.abs(this.X_axis/2 - i) + Math.abs(this.Y_axis/2 - j)*Math.abs(this.Y_axis/2 - j)))/((this.X_axis+this.Y_axis)/2);
				if(this.dots[i][j] <= 0){
					this.dots[i][j] = 0;
				}
				if(this.dots[i][j] >= 1){
					this.dots[i][j] = 1;
				}
*/
/*
				if(Math.abs(j-this.Y_axis/2)<2){
					this.dots[i][j] = 0;
				}
				else{
					this.dots[i][j] = 1;
				}
*/
				double tempx = i-this.X_axis/2;
				double tempy = j-this.Y_axis/2;

				double tempr = tempx*tempx + tempy*tempy;

				if(Math.sqrt(tempr) <= (this.X_axis+this.Y_axis)/7+range){
					this.dots[i][j] = 1;
				}
				else{
					this.dots[i][j] = 0;
				}

			}
		}
	}

}
