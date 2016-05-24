package komota.cnn.main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class InputImage extends Image {

	BufferedImage bimage = null;


	public InputImage(int x, int y ,BufferedImage bimage) {
		super(x, y);

			this.bimage = bimage;

		// TODO 自動生成されたコンストラクター・スタブ
		for(int j=0;j<this.Y_axis;j++){
			for(int i=0;i<this.X_axis;i++){
				if(i  >= 111 && i <= 144 && j >= 6 && j<= 174){
					this.dots[i][j] = 0.7;
				}
				else if(i >= 36 && i <= 144 && j >= 6 && j <= 39){
					this.dots[i][j] = 0.7;
				}
				else if(i >= 36 && i <= 144 && j >= 141 && j <= 174){
					this.dots[i][j] = 0.7;
				}
				else if(i >= 36 && i <= 144 && j >= 72 && j <= 105){
					this.dots[i][j] = 0.7;
				}
				else{
					this.dots[i][j] = 1;
				}
			}
		}
		for(int j=0;j<this.Y_axis;j++){
			for(int i=0;i<this.X_axis;i++){
				if(i  >= 120 && i <= 135 && j >= 15 && j<= 165){
					this.dots[i][j] = 0;
				}
				else if(i >= 45 && i <= 135 && j >= 15 && j <= 30){
					this.dots[i][j] = 0;
				}
				else if(i >= 45 && i <= 135 && j >= 150 && j <= 165){
					this.dots[i][j] = 0;
				}
				else if(i >= 45 && i <= 135 && j >= 81 && j <= 96){
					this.dots[i][j] = 0;
				}
				else{
//					this.dots[i][j] = 1;
				}
			}
		}



		double max = 0;
		for(int j=0;j<this.Y_axis;j++){
			for(int i=0;i<this.X_axis;i++){
				this.dots[j][i] = -bimage.getRGB(j, i);
				if(this.dots[i][j] > max){
					max = this.dots[i][j];
				}
			}
		}
		for(int j=0;j<this.Y_axis;j++){
			for(int i=0;i<this.X_axis;i++){
				if(this.dots[i][j] < 0)this.dots[i][j] = 0;
				this.dots[i][j] = 1-(this.dots[i][j]/max);
			}
		}



	}


	public InputImage(int x,int y) throws IOException{
		this(x,y,ImageIO.read(new File("dataset/input0.png")));
	}


}
