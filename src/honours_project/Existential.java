package honours_project;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Existential extends Formula {
	private Formula operand;
	private List<String> terms;
	
	public Existential (List<String> terms, Formula operand) {
		super(Formula.Type.EXISTENTIAL);
		this.operand = operand;
		this.terms = new ArrayList<>();
		this.terms.addAll(terms);
	}
	
	@Override
	public boolean isValid() {
		// need to check if each term is free in given formula
		// To do: create a free() method in Formula class
		Set<Term> free = operand.free() 
		for (Term t : terms) {
			if (!free.has(t)) return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		return String.format("%s%s( %s )",
				this.getType().getConnective(),
				String.join(",", terms),
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
