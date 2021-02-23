package s1705270;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.antlr.v4.runtime.RecognitionException;

import uk.ac.ed.pguaglia.real.lang.Expression;
import uk.ac.ed.pguaglia.real.lang.ReplacementException;

public class App {

	public static void main(String[] args) throws RecognitionException, ReplacementException {
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

//		HashMap<String,List<String>> map1 = new HashMap<String, List<String>>();
//		map1.put("R", Arrays.asList(new String[] {"A", "B"}));
//		Schema sch1 = new Schema(map1);
//		TranslatorRC trans1 = new TranslatorRC(sch1);
//		Formula f1 = Formula.parse("~([E]?y(~([E]?z(~(R(?x,?y)) | R(?y,?z)))))");
//
//		try {
//			System.out.println(trans1.translate(f1));
//		} catch (TranslationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
//		HashMap<String,List<String>> map = new HashMap<String, List<String>>();
//		map.put("R", Arrays.asList(new String[] {"A", "B"}));
//		map.put("S", Arrays.asList(new String[] {"B", "C"}));
//		Schema sch = new Schema(map);
//		TranslatorRC trans = new TranslatorRC(sch);
//		Formula f = Formula.parse("[E]?y( R(?y,?x) & ~( [E]?z (S(?x,?z) & ~(R(?z,?y)) )))");
//		try {
//			System.out.println(trans.translate(f));
//		} catch (TranslationException e) {
//			e.printStackTrace();
//		}
		
		// Check the following test case with Professor
//		HashMap<String,List<String>> map = new HashMap<String, List<String>>();
//		map.put("R", Arrays.asList(new String[] {"A", "B","C"}));
//		map.put("S", Arrays.asList(new String[] {"C"}));
//		Schema sch = new Schema(map);
//		TranslatorRA trans = new TranslatorRA(sch);
//		Expression e = Expression.parse("<R>[B->C](<P>[A,B](R)) <D> <S>[A = C | A = 1](<P>[A](R) <X> (S))");
//		try {
//			System.out.println(trans.translate(e));
//		} catch (TranslationException error) {
//			error.printStackTrace();
//		}
		
	}
}
