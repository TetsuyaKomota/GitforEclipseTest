package komota.lifegame;

public class Life3 extends LifeFrame {

	@Override
	public void startWORLD() {
		// TODO 自動生成されたメソッド・スタブ
		//エージェントをランダムに召喚
		for(int i=0;i<Statics.NUMBEROFAGENTS;i++){
			for(int j=0;j<Statics.NUMBEROFAGENTS;j++){
				this.agents[i][j].status = (int)(Math.random()*2 - 0.5);
				//this.agents[i][j].status = 0;
			}
		}
	}

	@Override
	public int rule(int[] neighbors, int status) {
		// TODO 自動生成されたメソッド・スタブ
		int output = status;
		//ライフゲーム
		output = Life1.RULE_gameofLIFE(neighbors, output);
		//対抗勢力
		output = Life2.RULE_gameofLIFE_ENEMY(neighbors, output);
		//無からエージェントが誕生する
		output = Life3.RULE_birth(neighbors, output, 1, 0.0001);
		//浸食感染
		output = Life1.RULE_influence(neighbors, output);
		//対抗勢力は浸食感染を捕食する
		output = Life3.RULE_predation(neighbors, output, 2, 6, 0.8);
		//突然変異
		if(status == 1 && Math.random() > 0.999999){
			output = 6;
		}
		else if(status == 1 && neighbors[1] > 3){
			if(Math.random() > 0.99999){
				output = 2;
			}
		}
		return output;
	}

	//誕生
	public static int RULE_birth(int[] neighbors,int status ,int child,double probability){
		int output = status;
		//空き地に，無条件で一定の確率probabilityで子childが誕生する
		if(status == 0){
			if(Math.random() > 1-probability){
				output = child;
			}
		}
		return output;
	}
	//交配
	public static int RULE_crossing(int[] neighbors,int status, int parents,int child,double probability){
		int output = status;
		//親parentsがちょうど3匹存在する空き地に，子childが確率probabilityで誕生する
		if(status == 0 && neighbors[parents] == 3){
			if(Math.random() > 1-probability){
				output = child;
			}
		}
		return output;
	}

	//捕食
	public static int RULE_predation(int[] neighbors,int status, int predator,int victim, double probability){
		//捕食者predator2つ以上が被食者victimに隣接している場合，確率probabilityで捕食する
		int output = status;
		if(status == victim && neighbors[predator] > 1){
			if(Math.random()>1-probability){
				output = predator;
			}
		}
		return output;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		Life3 life = new Life3();
	}

}
