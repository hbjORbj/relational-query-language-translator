package s1705270;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import uk.ac.ed.pguaglia.real.lang.BaseExpression;
import uk.ac.ed.pguaglia.real.lang.Expression;
import uk.ac.ed.pguaglia.real.lang.Product;
import uk.ac.ed.pguaglia.real.lang.Selection;
import uk.ac.ed.pguaglia.real.lang.Intersection;
import uk.ac.ed.pguaglia.real.lang.Renaming;
import uk.ac.ed.pguaglia.real.lang.ReplacementException;

public class ConjunctionTest {

	@Test
	public void classFunctionsTest() {
		List<Term> terms = new ArrayList<>();
		Term t1 = new Term("x1", false);
		Term t2 = new Term("x2", false);
		Term t3 = new Term("x3", false);
		terms.add(t1);
		terms.add(t2);
		terms.add(t3);
		
		Predicate left = new Predicate("User", terms);
		Equality right = new Equality(t2, new Term("Benny", true));
		Conjunction conj = new Conjunction(left, right);
		
		Set<Term> free = new HashSet<Term>();
		free.add(t1);
		free.add(t2);
		free.add(t3);

		assertEquals(conj.getLeftOperand(), left);
		assertEquals(conj.getRightOperand(), right);
		assertEquals(conj.toString(), "(User(?x1,?x2,?x3)) & (?x2 = Benny)");
		assertEquals(conj.free(), free);
	}
	
	@Test
	public void parsingTest() {
		Formula f1 = Formula.parse("User(?x1,?x2,?x3) & ?x2 = Benny");
		Conjunction conj = (Conjunction) f1;
		
		List<Term> terms = new ArrayList<>();
		Term t1 = new Term("x1", false);
		Term t2 = new Term("x2", false);
		Term t3 = new Term("x3", false);
		terms.add(t1);
		terms.add(t2);
		terms.add(t3);
		Predicate left = new Predicate("User", terms);
		Equality right = new Equality(t2, new Term("Benny", true));
		
		assertEquals(f1.getType().toString(), "CONJUNCTION");
		assertEquals(conj.getLeftOperand().toString(), left.toString());
		assertEquals(conj.getRightOperand().toString(), right.toString());
		assertEquals(conj.toString(), "(User(?x1,?x2,?x3)) & (?x2 = Benny)");
	}
	
	@Test
	void translationTest() throws TranslationException {
		HashMap<String,List<String>> map = new HashMap<String, List<String>>();
		map.put("User", Arrays.asList(new String[] {"id","name","age"}));
		Schema sch = new Schema(map);
		TranslatorRC trans = new TranslatorRC(sch);
		Formula f1 = Formula.parse("User(?x1,?x2,?x3) & ?x2 = Benny");

		Expression rel = new BaseExpression("User");
		Map<String,String> replacements = new HashMap<>();
		replacements.put("id", "Ax1");
		replacements.put("name", "Ax2");
		replacements.put("age", "Ax3");

		Expression e1 = null;
		try {
			e1 = new Renaming(replacements, rel);
		} catch (ReplacementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		uk.ac.ed.pguaglia.real.lang.Term t1 = new uk.ac.ed.pguaglia.real.lang.Term("Ax2", false);
		uk.ac.ed.pguaglia.real.lang.Term t2 = new uk.ac.ed.pguaglia.real.lang.Term("Benny", true);
		uk.ac.ed.pguaglia.real.lang.Equality cond = new uk.ac.ed.pguaglia.real.lang.Equality(t1,t2);
		Expression e2 = new Selection(cond, trans.adom("Ax2"));

		e2 = new Product(e2, trans.adom("Ax3"));
		e2 = new Product(e2, trans.adom("Ax1"));
		
		Expression exp = new Intersection(e1, e2);
		Expression result = trans.translate(f1);
		
		assertEquals(result.toString(), exp.toString());
		assertEquals(result.toString(), "<R>[name->Ax2,id->Ax1,age->Ax3]( User ) <I> "
				+ "( ( <S>[Ax2 = 'Benny']( Adom_Ax2 ) <X> Adom_Ax3 ) <X> Adom_Ax1 )");
	}

}
