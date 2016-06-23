package mahjong.rules;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import mahjong.main.Rule;

public class Rule_USER extends Rule{

	@Override
	public String dahai(){

		this.getPlayer().getHand().countShanten();

	    String sin = null;
	    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	    try{
	      sin = br.readLine();
	    }catch(IOException e){
	      System.out.println("不正な入力:" + e.getMessage());
	      return null;
	    }
	    switch(sin){

	    case "ton":
	    	return "東";
	    case "nan":
	    	return "南";
	    case "xa":
	    	return "西";
	    case "pe":
	    	return "北";
	    case "haku":
	    	return "白";
	    case "hatsu":
	    	return "發";
	    case "chun":
	    	return "中";
	    case "":
	    	return this.tsumogiri();
	    default:
	    	return sin;

	    }
	}

}
