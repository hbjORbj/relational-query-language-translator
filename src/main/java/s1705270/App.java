package main.java.s1705270;

import java.util.ArrayList;
import java.util.List;

public class App {

	public static void main(String[] args) {
		Term x = new Term("x", false);
		Term y = new Term("y", false);
		Term c = new Term("c", true);

		List<Term> terms = new ArrayList<>();
		terms.add(x);
		terms.add(y);
		Predicate p = new Predicate("P", terms);

		Equality eq1 = new Equality(x, c);
		Equality eq2 = new Equality(y, c);
		Disjunction disj = new Disjunction(eq1, eq2);

		Term t = new Term("x", false);
		List<Term> terms2 = new ArrayList<>();
		terms2.add(t);
		Formula f = new Existential(terms2, new Conjunction(p, disj));

		System.out.println(f);
		System.out.println(f.free());
	}

}
