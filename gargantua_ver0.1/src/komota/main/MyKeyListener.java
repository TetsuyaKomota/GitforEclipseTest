package komota.main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import komota.libs.Statics;

public class MyKeyListener implements KeyListener{

	//フィールド
	//キーの入力状態
	private boolean[] keystatus;

	//コンストラクタ
	MyKeyListener(){
		this.keystatus = new boolean[Statics.KEY_NUMBEROFKEY];
	}

	//キー状態のセッター，ゲッター
	public void setKeyStatus(int keycode,boolean input){
		this.keystatus[keycode] = input;
	}
	public boolean getKeyStatus(int keycode){
		return this.keystatus[keycode];
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		System.out.println("ボタン押した");
		switch(e.getKeyCode()){
		case KeyEvent.VK_SPACE:
			this.keystatus[Statics.KEY_SPACE] = true;
			break;
		case KeyEvent.VK_X:
			this.keystatus[Statics.KEY_X] = true;
			break;
		case KeyEvent.VK_Z:
			this.keystatus[Statics.KEY_Z] = true;
			break;
		case KeyEvent.VK_UP:
			this.keystatus[Statics.KEY_UP] = true;
			break;
		case KeyEvent.VK_DOWN:
			this.keystatus[Statics.KEY_DOWN] = true;
			break;
		case KeyEvent.VK_RIGHT:
			this.keystatus[Statics.KEY_RIGHT] = true;
			break;
		case KeyEvent.VK_LEFT:
			this.keystatus[Statics.KEY_LEFT] = true;
			break;
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		System.out.println("ボタン離した");
		switch(e.getKeyCode()){
		case KeyEvent.VK_SPACE:
			this.keystatus[Statics.KEY_SPACE] = false;
			break;
		case KeyEvent.VK_X:
			this.keystatus[Statics.KEY_X] = false;
			break;
		case KeyEvent.VK_Z:
			this.keystatus[Statics.KEY_Z] = false;
			break;
		case KeyEvent.VK_UP:
			this.keystatus[Statics.KEY_UP] = false;
			break;
		case KeyEvent.VK_DOWN:
			this.keystatus[Statics.KEY_DOWN] = false;
			break;
		case KeyEvent.VK_RIGHT:
			this.keystatus[Statics.KEY_RIGHT] = false;
			break;
		case KeyEvent.VK_LEFT:
			this.keystatus[Statics.KEY_LEFT] = false;
			break;
		}

	}

}
