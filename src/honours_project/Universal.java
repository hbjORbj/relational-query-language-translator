package honours_project;

import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;

public class Universal extends Formula {
	private Formula operand;
	private List<Term> terms;
	
	public Universal (List<Term> terms, Formula operand) {
		super(Formula.Type.UNIVERSAL);
		this.operand = operand;
		this.terms = new ArrayList<>();
		this.terms.addAll(terms);
	}
	
	@Override
	public boolean isValid() {
		// need to check if each term is free in given formula
		// To do: create a free() method in Formula class
		// Set<Term> free = operand.free() 
		for (Term t : terms) {
			if (!free.has(t)) return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		List<String> list = new ArrayList<>();
		for (Term t : terms) {
			list.add(t.toString());
		}
		return String.format("%s%s( %s )",
				this.getType().getConnective(),
				String.join(",", list),
				operand.toString());
	}
	
	public Set<Term> free() {
		// To do: create a free() method in Formula class
		// Set<Term> free = operand.free() 
		for (Term t : terms) {
			if (t.isVariable()) {
				free.delete(t);
			}
		}
		return free;
	}
}
