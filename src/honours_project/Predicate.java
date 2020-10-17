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
	
	public Set<Term> adom() {
		Set<Term> adom = new HashSet<>();
		for (Term t : terms) {
			if (t.isConstant()) {
				adom.add(t);
			}
		}
		return adom;
	}
}
