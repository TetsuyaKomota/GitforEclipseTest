package komota.cnn.main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class CNNFrame extends MyFrame{

	public static void main(String[] argv){

		CNNFrame frame = new CNNFrame("CNN");
		InputImage image = null;
		try {
			image = new InputImage(180,180);
		} catch (IOException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		}
		frame.inputimage = image;
		frame.fomal = new Image(image.X_axis,image.Y_axis);
		frame.fomal.dots = image.dots;

		BufferedImage[] bimages = new BufferedImage[6];

		for(int i=0;i<bimages.length;i++){
			try {
				bimages[i] = ImageIO.read(new File("dataset/input"+i+".png"));
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}
		frame.bimages = bimages;





		frame.playflag = true;
	}


	Image fomal = null;
	Image inputimage = null;
	Image[] filter = new Image[3];
	int filtersize = 10;
	double filterrange = 2;

	BufferedImage[] bimages = new BufferedImage[6];

//	BufferedImage bimage = null;


	public CNNFrame(String framename) {
		super(framename);
		this.setSize(1400, 1000);
		this.filter[0] = new Filter1(filtersize,filtersize,filterrange);
		this.filter[1] = new Filter2(filtersize,filtersize,filterrange);
		this.filter[2] = new Filter3(filtersize,filtersize,filterrange);

		this.addKeyListener(new CNNKeyListener());
		//元画像読み込み
//		try {
//			this.bimage = ImageIO.read(new File("dataset/input.png"));
//		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
//			e.printStackTrace();
//		}


		// TODO 自動生成されたコンストラクター・スタブ
	}


	//引数に与えた画像を引数に指定した位置と倍率で描画する
	public void drawMyImage(Image image,int x,int y,int scale){
		Graphics2D g = (Graphics2D)this.buffer.getDrawGraphics();
		int dotcolor = 0;
		for(int j=0;j<image.Y_axis;j++){
			for(int i=0;i<image.X_axis;i++){
				dotcolor = (int)(image.dots[i][j] * 255);
				g.setColor(new Color(dotcolor,dotcolor,dotcolor));
				g.fillRect(x+i*scale, y+j*scale, scale, scale);
			}
		}
		g.dispose();
		buffer.show();
	}


	//たたみ込み計算。第一引数のImageに第二引数のImageで畳み込みを計算した結果をImageとして出力する。出力サイズが第一引数のサイズと同じになるようにパディング処理を行ってある前提
	public Image convolution(Image input,Image filter){

		Image output = new Image(input.X_axis-filter.X_axis,input.Y_axis-filter.Y_axis);
		double max = 0;

		for(int j=0;j<output.Y_axis;j++){
			for(int i=0;i<output.X_axis;i++){

				double current = 0;

				for(int rj=0;rj<filter.Y_axis;rj++){
					for(int ri=0;ri<filter.X_axis;ri++){
						current += input.dots[i+ri][j+rj]*filter.dots[ri][rj];
					}
				}
				output.dots[i][j] = current;
				if(current>max){
					max = current;
				}
			}
		}
		for(int j=0;j<output.Y_axis;j++){
			for(int i=0;i<output.X_axis;i++){
				output.dots[i][j] /= max;
			}
		}

		return output;


	}





	//プーリング計算。第一引数のImageを第二引数の粒度でマックスプーリングした結果をImageとして出力する。入力と出力のサイズが異なる
	//計算位置が重ならないようにストライド値はpoolrangeとしている
	public Image pooling(Image input ,int poolrange){
		Image output = new Image(input.X_axis/poolrange,input.Y_axis/poolrange);


		for(int j=0;j<output.Y_axis;j++){
			for(int i=0;i<output.X_axis;i++){

				double currentmax = 0;

				for(int rj=0;rj<poolrange;rj++){
					for(int ri=0;ri<poolrange;ri++){
						if(input.dots[poolrange*i+ri][poolrange*j+rj]>currentmax){
							currentmax = input.dots[poolrange*i+ri][poolrange*j+rj];
						}
					}
				}
				output.dots[i][j] = currentmax;

			}
		}

		return output;
	}


	//遊び処理。プーリング後の画像を統合して一枚に戻す
	public Image convert(Image img1,Image img2,Image img3){
		Image output = new Image(img1.X_axis,img1.Y_axis);
		double max = 0;
		for(int j=0;j<output.Y_axis;j++){
			for(int i=0;i<output.X_axis;i++){
				output.dots[i][j] = img1.dots[i][j]+img2.dots[i][j]+img3.dots[i][j];
				if(output.dots[i][j]>max){
					max = output.dots[i][j];
				}
			}
		}
		for(int j=0;j<output.Y_axis;j++){
			for(int i=0;i<output.X_axis;i++){
				output.dots[i][j] /= max;
			}
		}
		return output;
	}

	@Override
	public void run() {
		// TODO 自動生成されたメソッド・スタブ

		Graphics2D g = (Graphics2D)this.buffer.getDrawGraphics();
		g.setColor(new Color(240,240,240));
		g.fillRect(0,0,this.getWidth(),this.getHeight());
		g.dispose();
		buffer.show();


		if(inputimage != null){
//			inputimage.padding(5);
			drawMyImage(inputimage,50,410,1);
			drawMyImage(filter[0],350,250,4);
			drawMyImage(filter[1],350,500,4);
			drawMyImage(filter[2],350,750,4);

//			drawMyImage(pooling(inputimage,4),500,100,9);

			Image tempimage = inputimage.padding(filter[0].X_axis/2);

			Image conv1 = convolution(tempimage,filter[0]);
			Image conv2 = convolution(tempimage,filter[1]);
			Image conv3 = convolution(tempimage,filter[2]);


			drawMyImage(conv1,500,100,1);
			drawMyImage(conv2,500,410,1);
			drawMyImage(conv3,500,720,1);


			Image pool1 = pooling(conv1,4);
			Image pool2 = pooling(conv2,4);
			Image pool3 = pooling(conv3,4);


			drawMyImage(pool1,800,100,4);
			drawMyImage(pool2,800,410,4);
			drawMyImage(pool3,800,720,4);


			Image output = convert(pool1,pool2,pool3);

			drawMyImage(output,1100,410,4);

//			Graphics2D g = (Graphics2D)this.buffer.getDrawGraphics();
//			g.drawImage(bimage, 1100, 700,null);


			this.playflag = false;
		}
	}

	class CNNKeyListener implements KeyListener{

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO 自動生成されたメソッド・スタブ
/*
			int size = CNNFrame.this.filtersize;
			double range = CNNFrame.this.filterrange;

			if(e.getKeyCode() == KeyEvent.VK_UP){
				size += 3;
				CNNFrame.this.filter[0]=new Filter1(size,size,range);
				CNNFrame.this.filter[1]=new Filter2(size,size,range);
				CNNFrame.this.filter[2]=new Filter3(size,size,range);
			}
			else if(e.getKeyCode() == KeyEvent.VK_DOWN){
				size -= 3;
				if(size<0)size = 0;
				CNNFrame.this.filter[0]=new Filter1(size,size,range);
				CNNFrame.this.filter[1]=new Filter2(size,size,range);
				CNNFrame.this.filter[2]=new Filter3(size,size,range);
			}
			else if(e.getKeyCode() == KeyEvent.VK_LEFT){
				range -= 3;
				if(range<0)range = 0;
				CNNFrame.this.filter[0]=new Filter1(size,size,range);
				CNNFrame.this.filter[1]=new Filter2(size,size,range);
				CNNFrame.this.filter[2]=new Filter3(size,size,range);

			}
			else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
				range += 3;
				CNNFrame.this.filter[0]=new Filter1(size,size,range);
				CNNFrame.this.filter[1]=new Filter2(size,size,range);
				CNNFrame.this.filter[2]=new Filter3(size,size,range);

			}
			CNNFrame.this.playflag = true;
*/

		}

		@Override
		public void keyPressed(KeyEvent e) {
			// TODO 自動生成されたメソッド・スタブ
			int size = CNNFrame.this.filtersize;
			double range = CNNFrame.this.filterrange;

//			CNNFrame.this.inputimage.dots = CNNFrame.this.fomal.dots;

			if(e.getKeyCode() == KeyEvent.VK_UP){
				size += 1;
				CNNFrame.this.filter[0]=new Filter1(size,size,range);
				CNNFrame.this.filter[1]=new Filter2(size,size,range);
				CNNFrame.this.filter[2]=new Filter3(size,size,range);
			}
			else if(e.getKeyCode() == KeyEvent.VK_DOWN){
				size -= 1;
				if(size<0)size = 0;
				CNNFrame.this.filter[0]=new Filter1(size,size,range);
				CNNFrame.this.filter[1]=new Filter2(size,size,range);
				CNNFrame.this.filter[2]=new Filter3(size,size,range);
			}
			else if(e.getKeyCode() == KeyEvent.VK_LEFT){
				range -= 1;
				if(range<0)range = 0;
				CNNFrame.this.filter[0]=new Filter1(size,size,range);
				CNNFrame.this.filter[1]=new Filter2(size,size,range);
				CNNFrame.this.filter[2]=new Filter3(size,size,range);

			}
			else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
				range += 1;
				CNNFrame.this.filter[0]=new Filter1(size,size,range);
				CNNFrame.this.filter[1]=new Filter2(size,size,range);
				CNNFrame.this.filter[2]=new Filter3(size,size,range);

			}
			else if(e.getKeyCode() == KeyEvent.VK_1){
				InputImage input = new InputImage(180,180,CNNFrame.this.bimages[0]);
				CNNFrame.this.inputimage = input;
			}
			else if(e.getKeyCode() == KeyEvent.VK_2){
				InputImage input = new InputImage(180,180,CNNFrame.this.bimages[1]);
				CNNFrame.this.inputimage = input;
			}
			else if(e.getKeyCode() == KeyEvent.VK_3){
				InputImage input = new InputImage(180,180,CNNFrame.this.bimages[2]);
				CNNFrame.this.inputimage = input;
			}
			else if(e.getKeyCode() == KeyEvent.VK_4){
				InputImage input = new InputImage(180,180,CNNFrame.this.bimages[3]);
				CNNFrame.this.inputimage = input;
			}
			else if(e.getKeyCode() == KeyEvent.VK_5){
				InputImage input = new InputImage(180,180,CNNFrame.this.bimages[4]);
				CNNFrame.this.inputimage = input;
			}
			else if(e.getKeyCode() == KeyEvent.VK_6){
				InputImage input = new InputImage(180,180,CNNFrame.this.bimages[5]);
				CNNFrame.this.inputimage = input;
			}

			CNNFrame.this.filtersize = size;
			CNNFrame.this.filterrange = range;
			CNNFrame.this.playflag = true;

		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO 自動生成されたメソッド・スタブ

		}

	}


}
