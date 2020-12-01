package s1705270;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Existential extends Formula {
	private Formula operand;
	private List<Term> terms;
	
	public Existential(List<Term> terms, Formula operand) {
		super(Formula.Type.EXISTENTIAL);
		this.operand = operand;
		this.terms = new ArrayList<>();
		this.terms.addAll(terms);
	}

	public Formula getOperand() {
		return this.operand;
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
		return String.format("%s%s( %s )", this.getType().getConnective(), String.join(",", list), operand.toString());
	}

	public Set<Term> free() {
		Set<Term> free = new HashSet<>();
		free.addAll(operand.free());
		for (Term t : terms) {
			free.remove(t);
		}
		return free;
	}
}
