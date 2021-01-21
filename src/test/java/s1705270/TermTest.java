package s1705270;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TermTest {

	@Test
	void classFunctionsTest() {
		Term t1 = new Term("x1", false);
		Term t2 = new Term("Edinburgh", true);
		Term t3 = new Term("x1", false);
		
		assert(t1.isVariable() == true);
		assert(t2.isVariable() == false);
		assert(t1.equals(t2) == false);
		assert(t1.equals(t3) == true);
		assertEquals(t1.getValue(), "x1");
		assertEquals(t1.toString(), "?x1");
		assertEquals(t2.getValue(), "Edinburgh");
		assertEquals(t2.toString(), "Edinburgh");
	}
	
	@Test
	void parsingTest() {
		Formula f1 = Formula.parse("?x1 = ?x2");
		Equality eq = (Equality) f1;
		assertEquals(eq.getLeftTerm().getValue(), "x1");
		assertEquals(eq.getRightTerm().getValue(), "x2");
	}

}
