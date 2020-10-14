package honours_project;

import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;

public class Predicate {
	private String name;
	private List<Term> terms;
	
	public Predicate(String name, List<Term> terms) {
		this.name = name;
		this.terms = new ArrayList<>();
		this.terms.addAll(terms);
	}
	
	@Override
	public String toString() {
		return String.format("%s(%s)", name, String.join(",", terms));
	}
	
	@Override
	public Set<Term> free() {
		Set<Term> free = new HashSet<>();
		for (Term t : terms) {
			if (t.isVariable()) {
				free.add(t);
			}
		}
		return free;
	}
}
