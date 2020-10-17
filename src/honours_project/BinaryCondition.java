package honours_project;

import java.util.HashSet;
import java.util.Set;

public abstract class BinaryCondition extends Condition {
	
	protected Term left;
	protected Term right;

	public BinaryCondition (Term left, Term right, Condition.Type type) {
		super(type);
		this.left = left;
		this.right = right;
	}

	@Override
	public String toString() {
		return String.format( "%s %s %s",
				left,
				this.getType().getConnective(),
				right);
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
