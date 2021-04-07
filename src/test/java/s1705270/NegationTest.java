package s1705270;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import uk.ac.ed.pguaglia.real.lang.Expression;

class NegationTest {

	@Test
	void classFunctionsTest() {
		List<Term> terms = new ArrayList<>();
		Term t1 = new Term("x1", false);
		Term t2 = new Term("x2", false);
		Term t3 = new Term("x3", false);
		terms.add(t1);
		terms.add(t2);
		terms.add(t3);
		
		Predicate pred = new Predicate("User", terms);
		Negation neg = new Negation(pred);
		
		Set<Term> free = new HashSet<Term>();
		free.add(t1);
		free.add(t2);
		free.add(t3);

		assertEquals(neg.getOperand(), pred);
		assertEquals(neg.toString(), "~(User(?x1,?x2,?x3))");
		assertEquals(neg.free(), free);
	}
	
	@Test
	void parsingTest() {
		Formula f1 = Formula.parse("~User(?x1,?x2,?x3)");
		Negation neg = (Negation) f1;
		
		List<Term> terms = new ArrayList<>();
		Term t1 = new Term("x1", false);
		Term t2 = new Term("x2", false);
		Term t3 = new Term("x3", false);
		terms.add(t1);
		terms.add(t2);
		terms.add(t3);
		
		assertEquals(f1.getType().toString(), "NEGATION");
		assertEquals(neg.getOperand().toString(), "User(?x1,?x2,?x3)");
		assertEquals(neg.toString(), "~(User(?x1,?x2,?x3))");
	}
	
	@Test
	void translationTest() throws TranslationException {
		HashMap<String,List<String>> map = new HashMap<String, List<String>>();
		map.put("User", Arrays.asList(new String[] {"id","name","age"}));
		Schema sch = new Schema(map);
		TranslatorRC trans = new TranslatorRC(sch);
		Formula f1 = Formula.parse("~User(?x1,?x2,?x3)");
		
		Expression result = trans.translate(f1);
		assertEquals(result.toString(), "( ( Adom_Ax3 <X> Adom_Ax2 ) <X> Adom_Ax1 ) <D> <R>[name->Ax2,id->Ax1,age->Ax3]( User )");
	}

}
