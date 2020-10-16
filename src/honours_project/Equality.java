package honours_project;

import java.util.Set;
import java.util.HashSet;

public class Equality extends Condition {
	private Term left;
	private Term right;
	
	public Equality(Term left, Term right) {
		super(Condition.Type.EQUAL);
		this.left = left;
		this.right = right;
	}
	
	@Override
	public String toString() {
		return String.format("%s %s %s", left.toString(), this.getType().getConnective(), right.toString());
	}
	
	public Set<Term> free() {
		Set<Term> free = new HashSet<>();
		if (left.isVariable()) {
			free.add(left);
		}
		if (right.isVariable()) {
			free.add(right);
		}
		return free;
	}
}
