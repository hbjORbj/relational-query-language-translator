package s1705270;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class App {

	public static void main(String[] args) {
//		Term x = new Term("x", false);
//		Term y = new Term("y", false);
//		Term c = new Term("c", true);
//
//		List<Term> terms = new ArrayList<>();
//		terms.add(x);
//		terms.add(y);
//		Predicate p = new Predicate("P", terms);
//
//		Equality eq1 = new Equality(x, c);
//		Equality eq2 = new Equality(y, c);
//		Disjunction disj = new Disjunction(eq1, eq2);
//
//		Term t = new Term("x", true);
//		List<Term> terms2 = new ArrayList<>();
//		terms2.add(t);
//		Formula f = new Existential(terms2, new Conjunction(p, disj));
//
//		System.out.println(f);
//		System.out.println(f.free());
//		HashMap<String,List<String>> map = new HashMap<String, List<String>>();
//		map.put("R", Arrays.asList(new String[] {"A","B"}));
//		Schema sch = new Schema(map);
//		System.out.println(sch.convert());
//		Formula f1 = Formula.parse("?x = c)");
//		System.out.println(f1);
//		TranslatorRC trans = new TranslatorRC(sch);
//		System.out.println(trans.Adom("N"));
//		try {
//			Expression exp  = trans.translate(f1);
//			System.out.println(exp);
//		} catch (TranslationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		Formula f2 = Formula.parse("[E]?x,?y(R(?x,?y))");
//		System.out.println(f2);
		
//		HashMap<String,List<String>> map = new HashMap<String, List<String>>();
//		map.put("Customer", Arrays.asList(new String[] {"CustID","Name"}));
//		map.put("Account", Arrays.asList(new String[] {"Number", "CustID"}));
//		Schema sch = new Schema(map);
//		TranslatorRC trans = new TranslatorRC(sch);
//		Formula f1 = Formula.parse("[E]?x4(Customer(?x1,?x2) & Account(?x3,?x4) & ?x1 = ?x4)");
//
//		try {
//			System.out.println(trans.translate(f1));
//		} catch (TranslationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		HashMap<String,List<String>> map = new HashMap<String, List<String>>();
		map.put("R", Arrays.asList(new String[] {"A"}));
		map.put("S", Arrays.asList(new String[] {"A","B"}));
		Schema sch = new Schema(map);
		TranslatorRC trans = new TranslatorRC(sch);
		Formula f1 = Formula.parse("R(?x) & ~([E]?y(S(?y,?z)))");

		try {
			System.out.println(trans.translate(f1));
		} catch (TranslationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
