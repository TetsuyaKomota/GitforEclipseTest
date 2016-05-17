package komota.supers;

import komota.main.MyFrame;
import komota.main.MyKeyListener;

public abstract class Mode {

	//フレームと相互につながる
	protected MyFrame frame;

	//キーリスナーと交互につながる
	protected MyKeyListener key;

	public abstract void run();

	//コンストラクタ
	public Mode(MyFrame frame, MyKeyListener key){
		this.frame = frame;
		this.key = key;
	}

}
