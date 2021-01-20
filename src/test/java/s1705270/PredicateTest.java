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
import uk.ac.ed.pguaglia.real.lang.Renaming;
import uk.ac.ed.pguaglia.real.lang.ReplacementException;

class PredicateTest {

	@Test
	void classFunctionsTest() {
		List<Term> terms = new ArrayList<>();
		Term t1 = new Term("id", false);
		Term t2 = new Term("name", false);
		Term t3 = new Term("age", false);
		terms.add(t1);
		terms.add(t2);
		terms.add(t3);
		
		Predicate pred = new Predicate("User", terms);

		Set<Term> free = new HashSet<Term>();
		free.add(t1);
		free.add(t2);
		free.add(t3);

		assertEquals(pred.getName(), "User");
		assertEquals(pred.getTerms(), terms);
		assertEquals(pred.toString(), "User(?id,?name,?age)");
		assertEquals(pred.free(), free);
	}
	
	@Test
	void parsingTest() {
		Formula f1 = Formula.parse("User(?x1,?x2,?x3)");
		Predicate pred = (Predicate) f1;
		
		List<Term> terms = new ArrayList<>();
		Term t1 = new Term("x1", false);
		Term t2 = new Term("x2", false);
		Term t3 = new Term("x3", false);
		terms.add(t1);
		terms.add(t2);
		terms.add(t3);
		
		assertEquals(f1.getType().toString(), "PREDICATE");
		assertEquals(pred.getName(), "User");
		assertEquals(pred.getTerms(), terms);
		assertEquals(pred.toString(), "User(?x1,?x2,?x3)");
	}
	
	@Test
	void translationTest() throws TranslationException {
		HashMap<String,List<String>> map = new HashMap<String, List<String>>();
		map.put("User", Arrays.asList(new String[] {"id","name","age"}));
		Schema sch = new Schema(map);
		TranslatorRC trans = new TranslatorRC(sch);
		Formula f1 = Formula.parse("User(?x1,?x2,?x3)");
		
		Expression rel = new BaseExpression("User");
		Map<String,String> replacements = new HashMap<>();
		replacements.put("id", "Ax1");
		replacements.put("name", "Ax2");
		replacements.put("age", "Ax3");
		Expression exp = null;
		try {
			exp = new Renaming(replacements, rel);
		} catch (ReplacementException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		assertEquals(trans.translate(f1).toString(), exp.toString());
	}
	
}
