package komota.main;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;

public class SampleDemo20151117 extends SampleTask_001s_TEST{

	public static void main(String[] args){
		SampleDemo20151117 frame = new SampleDemo20151117();
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
		AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f);
		Graphics2D g = (Graphics2D)this.buffer.getDrawGraphics();
		g.setComposite(composite);
		g.setColor(Color.orange);
		g.fillRect(200, 200, 150, 300);
		g.setColor(Color.gray);
		g.fillRect(800, 500, 100, 400);
		g.fillRect(650, 800, 150, 100);
		g.dispose();
		buffer.show();
	}

}
