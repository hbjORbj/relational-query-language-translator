package honours_project;

import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;

public class Universal extends Expression {
	private Expression operand;
	private List<Term> terms;
	
	public Universal (List<Term> terms, Expression operand) {
		super(Expression.Type.UNIVERSAL);
		this.operand = operand;
		this.terms = new ArrayList<>();
		this.terms.addAll(terms);
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
		Set<Term> free = new HashSet<>();
		for (Term t : terms) {
			if (t.isVariable()) {
				free.add(t);
			}
		}
		return free;
	}
}
