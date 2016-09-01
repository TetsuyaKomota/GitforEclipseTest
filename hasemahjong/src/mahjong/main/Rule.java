package mahjong.main;

/**
 * トランプルールインターフェース。
 * 現在の手札とテーブルから、テーブルに出しうるカードの組み合わせを見つける。
 */
public class Rule
{
	//仮想手牌
	protected Hand virtualhand = new Hand();

	private Player player_;

	public void setPlayer(Player player){
		this.player_ = player;
	}
	public Player getPlayer(){
		return this.player_;
	}

	public String dahai(){
		return this.tsumogiri();
	}

	//ツモ切り
	public String tsumogiri(){
		return this.player_.getHand().lookHai(this.player_.getHand().getNumberOfHais()-1).toString();
	}

	//仮想手牌読み込み
	public void readTehai(){
		this.virtualhand = new Hand();
		for(int i=0;i<this.getPlayer().getHand().getNumberOfHais();i++){
			this.virtualhand.addHai(new Hai(this.getPlayer().getHand().lookHai(i).getSuit(),this.getPlayer().getHand().lookHai(i).getNumber()));
		}
	}

}
