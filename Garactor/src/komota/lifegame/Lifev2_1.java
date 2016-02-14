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

		//交配
		Lifev2_1.RULE_crossing(output, neighbors, status, 1, 1, 1);
		Lifev2_1.RULE_crossing(output, neighbors, status, 1, 2, 0);
		Lifev2_1.RULE_crossing(output, neighbors, status, 2, 1, 0);
		Lifev2_1.RULE_crossing(output, neighbors, status, 2, 2, 1);
		//捕食
		Lifev2_1.RULE_predation(output, neighbors, status, 1, 1, 0);
		Lifev2_1.RULE_predation(output, neighbors, status, 1, 2, 1);
		Lifev2_1.RULE_predation(output, neighbors, status, 2, 1, 1);
		Lifev2_1.RULE_predation(output, neighbors, status, 2, 2, 0);
		//生存
		Lifev2_1.RULE_survive(output, neighbors, status, 1, 1, 1);
		Lifev2_1.RULE_survive(output, neighbors, status, 2, 2, 1);
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

	//交配
	public static double[] RULE_crossing(double[] output,int[] neighbors,int status,int parents,int child,double probability){
		if(status == 0 && neighbors[parents] == 3){
			if(Math.random() > 1-probability){
				output[child] += 1;
			}
		}
		return output;
	}
	//生存
	public static double[] RULE_survive(double[] output, int[] neighbors,int status,int parents,int child,double probability){
		if(status == child && neighbors[parents] > 1 && neighbors[parents] < 4){
			if(Math.random() > 1-probability){
				output[child] += 1;
			}
		}
		else if(status == child){
			output[0] += 1;
		}
		return output;
	}
	//捕食
	public static double[] RULE_predation(double[] output,int[] neighbors,int status,int predator,int victim,double probability){
		if(status == victim && neighbors[predator] > 1){
			if(Math.random() > 1-probability){
				output[predator] += 1;
			}
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
