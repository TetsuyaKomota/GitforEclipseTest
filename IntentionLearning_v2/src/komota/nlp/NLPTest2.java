package komota.nlp;

import java.util.List;
import java.util.Set;

import edu.cmu.lti.jawjaw.JAWJAW;
import edu.cmu.lti.jawjaw.db.SenseDAO;
import edu.cmu.lti.jawjaw.db.SynlinkDAO;
import edu.cmu.lti.jawjaw.db.SynsetDAO;
import edu.cmu.lti.jawjaw.db.SynsetDefDAO;
import edu.cmu.lti.jawjaw.db.WordDAO;
import edu.cmu.lti.jawjaw.pobj.Lang;
import edu.cmu.lti.jawjaw.pobj.POS;
import edu.cmu.lti.jawjaw.pobj.Sense;
import edu.cmu.lti.jawjaw.pobj.Synlink;
import edu.cmu.lti.jawjaw.pobj.Synset;
import edu.cmu.lti.jawjaw.pobj.SynsetDef;
import edu.cmu.lti.jawjaw.pobj.Word;

public class NLPTest2 {
	private static void run( String word, POS pos ) {
		// ファサードから日本語 WordNet にアクセス

		Set<String> attributes = JAWJAW.findAttributes(word, pos);
		Set<String> causes = JAWJAW.findCauses(word, pos);
		Set<String> definitions = JAWJAW.findDefinitions(word, pos);
		Set<String> domains = JAWJAW.findDomains(word, pos);
		Set<String> consequents = JAWJAW.findEntailments(word, pos);
		Set<String> hasinstance = JAWJAW.findHasInstances(word, pos);
		Set<String> holonyms = JAWJAW.findHolonyms(word, pos);
		Set<String> hypernyms = JAWJAW.findHypernyms(word, pos);
		Set<String> hyponyms = JAWJAW.findHyponyms(word, pos);
		Set<String> indomains = JAWJAW.findInDomains(word, pos);
		Set<String> instance = JAWJAW.findInstances(word, pos);
		Set<String> meronyms = JAWJAW.findMeronyms(word, pos);
		Set<String> seealso = JAWJAW.findSeeAlso(word, pos);
		Set<String> similarto = JAWJAW.findSimilarTo(word, pos);
		Set<String> synonyms = JAWJAW.findSynonyms(word, pos);
		Set<String> translations = JAWJAW.findTranslations(word, pos);




		// 結果表示（多義語はごっちゃになっています）
		System.out.println( "attributes of "+word+" : \t"+ attributes );
		System.out.println( "causes of "+word+" : \t"+ causes );

		System.out.println( "definitions of "+word+" : \t"+ definitions );

		System.out.println( "domains of "+word+" : \t"+ domains );

		System.out.println( word+" entails : \t\t"+ consequents );

		System.out.println( "hasinstance of "+word+" : \t"+ hasinstance );
		System.out.println( "holonyms of "+word+" : \t"+ holonyms);

		System.out.println( "hypernyms of "+word+" : \t"+ hypernyms );
		System.out.println( "hyponyms of "+word+" : \t"+ hyponyms );

		System.out.println( "indomains of "+word+" : \t"+ indomains );
		System.out.println( "instance of "+word+" : \t"+ instance );
		System.out.println( "meronyms of "+word+" : \t"+ meronyms );
		System.out.println( "seealso of "+word+" : \t"+ seealso );
		System.out.println( "similarto of "+word+" : \t"+ similarto );
		System.out.println( "synonyms of "+word+" : \t"+ synonyms );

		System.out.println( "translations of "+word+" : \t"+ translations );
	}

	private static void run2( String word, POS pos ) {
		// 日本語 WordNet に直接アクセスし、生データを処理
		List<Word> words = WordDAO.findWordsByLemmaAndPos(word, pos);
		List<Sense> senses = SenseDAO.findSensesByWordid( words.get(0).getWordid() );
		String synsetId = senses.get(0).getSynset();
		Synset synset = SynsetDAO.findSynsetBySynset( synsetId );
		SynsetDef synsetDef = SynsetDefDAO.findSynsetDefBySynsetAndLang(synsetId, Lang.eng);
		List<Synlink> synlinks = SynlinkDAO.findSynlinksBySynset( synsetId );
		// 結果表示
		System.out.println( words.get(0) );
		System.out.println( senses.get(0) );
		System.out.println( synset );
		System.out.println( synsetDef );
		System.out.println( synlinks.get(0) );
	}

	public static void main(String[] args) {
		// "買収"(動詞)という単語から得られる関係の一部をデモします
		NLPTest2.run( "買収", POS.v );
		System.out.println("*******************************************************************************************************************************");
		NLPTest2.run( "ブドウ", POS.n );
		System.out.println("*******************************************************************************************************************************");
		NLPTest2.run2( "自然言語処理", POS.n );

	}
}
