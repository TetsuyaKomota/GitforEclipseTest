package komota.pr.main;

import komota.main.MyFrame;
import komota.main.MyPR;

public class PR_101 extends MyPR {
	//識別動作テストの100の、盤面の記憶と再現のみに使用するPR

	//フィールド
	//最終状態の目標位置
	int[] lastposition;
	//直前のstartログ
	StepLog laststartlog;

	//最終状態と直前startログの更新。ログは最新のものを獲得済み(インスタンス生成したて)を前提とする
	public void setLog(MyFrame frame){
		this.lastposition = frame.getSecondSelected();
		int idx = -1;
		for(int i=0;i<this.logdata.length;i++){
			if(this.logdata[i] == null){
				break;
			}
			else if(this.logdata[i].getType() == START){
				idx = i;
			}
		}
		this.laststartlog = this.logdata[idx];
	}

	//最終状態の呼び出し(目標位置のみ。setLogをしてから盤面はinitializeしないこと)
	public void loadLastPosition(MyFrame frame){
		frame.setSecondSelected(this.lastposition);
	}
	//直前startログの呼び出し。
	public void loadLastStartLog(MyFrame frame){
		this.arrangeField(frame, this.laststartlog);
	}

}
