package s1705270;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import uk.ac.ed.pguaglia.real.lang.Expression;
import uk.ac.ed.pguaglia.real.lang.Product;
import uk.ac.ed.pguaglia.real.lang.Selection;


class EqualityTest {

	@Test
	void classFunctionsTest() {
		Term t1 = new Term("x1", false);
		Term t2 = new Term("x2", false);
		Equality eq = new Equality(t1, t2);
		Set<Term> vars = new HashSet<Term>();
		vars.add(t1);
		vars.add(t2);
		assertEquals(eq.getLeftTerm(), t1);
		assertEquals(eq.getRightTerm(), t2);
		assertEquals(eq.toString(), "?x1 = ?x2");
		assertEquals(eq.free(), vars);
	}
	
	@Test
	void parsingTest() {
		Formula f1 = Formula.parse("?x1 = ?x2");
		Formula f2 = Formula.parse("?x1 = Scotland");

		Equality eq1 = (Equality) f1;
		Equality eq2 = (Equality) f2;
		
		assertEquals(f1.getType().toString(), "COMPARISON");
		assertEquals(eq1.getLeftTerm().getValue(), "x1");
		assertEquals(eq1.getRightTerm().getValue(), "x2");
		assertEquals(eq2.getLeftTerm().getValue(), "x1");
		assertEquals(eq2.getRightTerm().getValue(), "Scotland");
		assert(eq2.getLeftTerm().isConstant() == false);
		assert(eq2.getRightTerm().isConstant() == true);
	}
	
	@Test
	void translationTest() throws TranslationException {
		HashMap<String,List<String>> map = new HashMap<String, List<String>>();
		map.put("User", Arrays.asList(new String[] {"id","name","age"}));
		Schema sch = new Schema(map);
		TranslatorRC trans = new TranslatorRC(sch);
		Formula f1 = Formula.parse("?x1 = ?x3");

		uk.ac.ed.pguaglia.real.lang.Term t1 = new uk.ac.ed.pguaglia.real.lang.Term("Ax1", false);
		uk.ac.ed.pguaglia.real.lang.Term t2 = new uk.ac.ed.pguaglia.real.lang.Term("Ax3",false);
		uk.ac.ed.pguaglia.real.lang.Equality cond = new uk.ac.ed.pguaglia.real.lang.Equality(t1,t2);
		Expression exp = trans.adom("Ax1");
		exp = new Product(exp, trans.adom("Ax3"));
		exp = new Selection(cond, exp);
		Expression result = trans.translate(f1);

		assertEquals(result.toString(), exp.toString());
		assertEquals(result.toString(), "<S>[Ax1 = Ax3]( Adom_Ax1 <X> Adom_Ax3 )");
	}

}
