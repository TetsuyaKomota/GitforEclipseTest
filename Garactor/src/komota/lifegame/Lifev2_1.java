package komota.lifegame;

public class Lifev2_1 extends LifeFrame_v2 {

	@Override
	public void startWORLD() {
		// TODO 自動生成されたメソッド・スタブ
		for(int i=0;i<Statics.NUMBEROFAGENTS;i++){
			for(int j=0;j<Statics.NUMBEROFAGENTS;j++){
				this.agents[i][j].status = (int)(Math.random()*2 - 0.5);
				if(this.agents[i][j].status == 1){
					this.agents[i][j].status += (int)(Math.random()*2 - 0.5);
				}
			}
		}

	}

	@Override
	public double[] rule(int[] neighbors, int status) {
		// TODO 自動生成されたメソッド・スタブ
		double[] output = new double[Statics.NUMBEROFSTATUS];
		double[] temp = new double[Statics.NUMBEROFSTATUS];


		//単純なライフゲーム
		if(status == 1 && neighbors[1] > 1 && neighbors[1] < 4){
			for(int i=0;i<temp.length;i++){
				if(i==1){
					temp[i] = 1;
				}
				else{
					temp[i] = 0;
				}
			}
		}
		else if(status == 1){
			for(int i=0;i<temp.length;i++){
				if(i==0){
					temp[i] = 1;
				}
				else{
					temp[i] = 0;
				}
			}
		}
		for(int i=0;i<output.length;i++){
			output[i] += temp[i];
			temp[i] = 0;
		}
		if(status == 0 && neighbors[1] == 3){
			for(int i=0;i<temp.length;i++){
				if(i==1){
					temp[i] = 1;
				}
				else{
					temp[i] = 0;
				}
			}
		}
		for(int i=0;i<output.length;i++){
			output[i] += temp[i];
			temp[i] = 0;
		}
		//対抗勢力
		if(status == 2 && neighbors[2] > 1 && neighbors[2] < 4){
			for(int i=0;i<temp.length;i++){
				if(i==2){
					temp[i] = 1;
				}
				else{
					temp[i] = 0;
				}
			}
		}
		else if(status == 2){
			for(int i=0;i<temp.length;i++){
				if(i==0){
					temp[i] = 1;
				}
				else{
					temp[i] = 0;
				}
			}
		}
		for(int i=0;i<output.length;i++){
			output[i] += temp[i];
			temp[i] = 0;
		}
		if(status == 0 && neighbors[2] == 3){
			for(int i=0;i<temp.length;i++){
				if(i==2){
					temp[i] = 1;
				}
				else{
					temp[i] = 0;
				}
			}
		}
		for(int i=0;i<output.length;i++){
			output[i] += temp[i];
			temp[i] = 0;
		}

		//各確率を1未満にする
		int sum = 0;
		for(int i=0;i<output.length;i++){
			sum+=output[i];
		}
		for(int i=0;i<output.length;i++){
			output[i]/=sum;;
		}
		return output;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		Lifev2_1 life = new Lifev2_1();
	}

}
