package honours_project;

import java.util.Set;
import java.util.HashSet;

public class Equality {
	private Term left;
	private Term right;
	
	public Equality(Term left, Term right) {
		// super(Condition.Type.COMPARISON);
		this.left = left;
		this.right = right;
	}
	
	@Override
	public String toString() {
		return String.format("%s = %s", left.toString(), right.toString());
	}
	
	@Override
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
