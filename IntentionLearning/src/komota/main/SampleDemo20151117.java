package komota.main;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SampleDemo20151117 extends SampleTask_001s_TEST{

	//フィールド
	//カレー
	BufferedImage curry = null;

	public static void main(String[] args){
		SampleDemo20151117 frame = new SampleDemo20151117();
		frame.tasktitle = "カレー運んで";

		try {
			frame.curry = ImageIO.read(new File("media/image/forDemo2.jpg"));
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(){

		for(int i=0;i<this.panels.length;i++){
			for(int j=0;j<this.panels[0].length;j++){
				this.panels[i][j].setStatus(0);
			}
		}
		this.panels[130][180].setStatus(1);
		int randomblue = 30 + (int)(Math.random() * 65);
		int randomyellow = 30 + (int)(Math.random() * 65);
		this.panels[randomblue][20].setStatus(2);
		this.panels[randomyellow][67].setStatus(3);

		this.outputStart();
	}
	@Override
	public void draw(){
		super.draw();
		Graphics2D g = (Graphics2D)this.buffer.getDrawGraphics();
		AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f);
		g.setComposite(composite);
		g.setColor(Color.orange);
		g.fillRect(200, 200, 150, 300);
		g.setColor(Color.gray);
		g.fillRect(800, 500, 100, 400);
		g.fillRect(650, 800, 150, 100);
		composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f);
		g.setComposite(composite);
		if(this.curry != null){
			for(int i=0;i<this.panels.length;i++){
				for(int j=0;j<this.panels[0].length;j++){
					if(this.panels[i][j].status == 1){
						g.drawImage(curry,MyPanel.SIZE_FRAME+(MyPanel.SIZE_PANEL+MyPanel.SIZE_SEPALATOR)*j-curry.getWidth()/2,MyPanel.SIZE_FRAME+(MyPanel.SIZE_PANEL+MyPanel.SIZE_SEPALATOR)*i-curry.getHeight()/2,null);
						break;
					}
				}
			}
		}

		g.dispose();
		buffer.show();
	}

	@Override
	public void functionPlugin4(){
		this.setOutputFile("test5.txt");
		this.tasktitle = "サラダ運んで";
		try {
			this.curry = ImageIO.read(new File("media/image/forDemo4.jpg"));
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		initialize();

	}

}
