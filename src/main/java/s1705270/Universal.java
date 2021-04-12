package s1705270;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;

public class Universal extends Formula {
	private Formula operand;
	private List<Term> terms;

	public Universal(List<Term> terms, Formula operand) {
		super(Formula.Type.UNIVERSAL);
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

	@Override
	public Formula validRename(Term x, Term y) {
		List<Term> newTerms = new ArrayList<Term>();
		for (Term t : terms) {
			if (t.equals(x)) {
				newTerms.add(new Term(y.getValue(), y.isConstant()));
			} else {
				newTerms.add(new Term(x.getValue(), x.isConstant()));
			}
		}
		return new Universal(newTerms, operand.validRename(x, y));
	}
}
