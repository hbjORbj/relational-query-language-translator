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
import uk.ac.ed.pguaglia.real.lang.Projection;
import uk.ac.ed.pguaglia.real.lang.Renaming;
import uk.ac.ed.pguaglia.real.lang.ReplacementException;

class ExistentialTest {

	@Test
	void classFunctionsTest() {
		Term t1 = new Term("x1", false);
		Term t2 = new Term("x2", false);
		List<Term> terms1 = new ArrayList<Term>();
		terms1.add(t1);
		terms1.add(t2);
		
		List<Term> terms2 = new ArrayList<Term>();
		terms2.add(t1);
		
		Predicate pred = new Predicate("Customer", terms1);
		Existential ext = new Existential(terms2, pred);

		Set<Term> free = new HashSet<Term>();
		free.add(t2);

		assertEquals(ext.getOperand(), pred);
		assertEquals(ext.getTerms(), terms2);
		assertEquals(ext.toString(), "[E]?x1( Customer(?x1,?x2) )");
		assertEquals(ext.free(), free);
	}
	
	@Test
	void parsingTest() {
		Formula f1 = Formula.parse("[E]?x1(Customer(?x1,?x2))");
		Existential ext = (Existential) f1;
		
		List<Term> terms = new ArrayList<>();
		Term t1 = new Term("x1", false);
		terms.add(t1);
		
		assertEquals(f1.getType().toString(), "EXISTENTIAL");
		assertEquals(ext.getOperand().toString(), "Customer(?x1,?x2)");
		assertEquals(ext.getTerms(), terms);
		assertEquals(ext.toString(), "[E]?x1( Customer(?x1,?x2) )");
	}
	
	@Test
	void translationTest() throws TranslationException {
		HashMap<String,List<String>> map = new HashMap<String, List<String>>();
		map.put("Customer", Arrays.asList(new String[] {"CustID","Name"}));
		Schema sch = new Schema(map);
		TranslatorRC trans = new TranslatorRC(sch);
		Formula f1 = Formula.parse("[E]?x1(Customer(?x1,?x2))");
		
		Expression rel = new BaseExpression("Customer");
		Map<String,String> replacements = new HashMap<>();
		replacements.put("CustID", "Ax1");
		replacements.put("Name", "Ax2");
		Expression exp = null;
		try {
			exp = new Renaming(replacements, rel);
		} catch (ReplacementException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		List<String> attrs = new ArrayList<>();
		attrs.add("Ax2");
		exp = new Projection(attrs, exp);
		Expression result = trans.translate(f1);

		assertEquals(result.toString(), exp.toString());
		assertEquals(result.toString(), "<P>[Ax2]( <R>[CustID->Ax1,Name->Ax2]( Customer ) )");
	}

}
