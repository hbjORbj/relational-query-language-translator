package s1705270;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Predicate extends Formula {
	private String name;
	private List<Term> terms;

	public Predicate(String name, List<Term> terms) {
		super(Formula.Type.PREDICATE);
		this.name = name;
		this.terms = new ArrayList<>();
		this.terms.addAll(terms);
	}

	public String getName() {
		return this.name;
	}

	public List<Term> getTerms() {
		return this.terms;
	}
	
	@Override
	public String toString() {
		List<String> list = new ArrayList<>();
		for (Term t : terms) {
			list.add(t.toString());
		}
		return String.format("%s(%s)", name, String.join(",", list));
	}

	public Set<Term> free() {
		Set<Term> free = new HashSet<>();
		for (Term t : terms) {
			if (t.isVariable()) {
				free.add(t);
			}
		}
		return free;
	}

	@Override
	public Formula validRename(Term x, Term y) {
		List<Term> newTerms = new ArrayList<Term>();
		for (Term t : terms) {
			if (t.equals(x)) {
				newTerms.add(new Term(y.getValue(), y.isConstant()));
			} else {
				newTerms.add(new Term(t.getValue(), t.isConstant()));
			}
		}
		return new Predicate(name, newTerms);
	}
}
