package komota.lifegame;

public class Life4 extends LifeFrame {


	//確率
	double[][] probability_survive = new double[Statics.NUMBEROFSTATUS][Statics.NUMBEROFSTATUS];


	@Override
	public void startWORLD() {
		// TODO 自動生成されたメソッド・スタブ
		//エージェントをランダムに召喚
		this.probability_survive[0][0] = 0;
		this.probability_survive[0][1] = 0;
		this.probability_survive[0][2] = 0;
		this.probability_survive[1][0] = 0;
		this.probability_survive[1][1] = 1;
		this.probability_survive[1][2] = 0;
		this.probability_survive[2][0] = 0;
		this.probability_survive[2][1] = 0;
		this.probability_survive[2][2] = 1;


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
	public int rule(int[] neighbors, int status) {
		// TODO 自動生成されたメソッド・スタブ
		int output = status;
		//生存
		output = Life3.RULE_survive(neighbors, output, 1, 1, 1);
//		output = Life3.RULE_survive(neighbors, output, 1, 2, 0);
//		output = Life3.RULE_survive(neighbors, output, 2, 1, 0);
		output = Life3.RULE_survive(neighbors, output, 2, 2, 1);
		//交配
		output = Life3.RULE_crossing(neighbors, output, 1, 1, 0.3);
		output = Life3.RULE_crossing(neighbors, output, 1, 2, 0.7);
//		output = Life3.RULE_crossing(neighbors, output, 2, 1, 0);
		output = Life3.RULE_crossing(neighbors, output, 2, 2, 1);
		//捕食
/*
		output = Life3.RULE_predation(neighbors, output, 1, 1, 0);
		output = Life3.RULE_predation(neighbors, output, 1, 2, 1);
		output = Life3.RULE_predation(neighbors, output, 2, 1, 1);
		output = Life3.RULE_predation(neighbors, output, 2, 2, 0);
*/
		output = Life3.RULE_predation(neighbors, output, 1, 2, 0.5);
		output = Life3.RULE_predation(neighbors, output, 1, 6, 0.1);
		output = Life3.RULE_predation(neighbors, output, 2, 6, 1);
		//変位
		output = Life3.RULE_metamorphose(neighbors, output, 1, 1, 1);
		output = Life3.RULE_metamorphose(neighbors, output, 1, 2, 0);
		output = Life3.RULE_metamorphose(neighbors, output, 2, 1, 0);
		output = Life3.RULE_metamorphose(neighbors, output, 2, 2, 1);

		output = Life3.RULE_metamorphose(neighbors, output, 1, 6, 0.001);
		output = Life1.RULE_influence(neighbors, output);
		return output;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		Life4 life = new Life4();
	}

}
