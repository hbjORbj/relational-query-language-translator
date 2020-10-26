package honours_project;

import java.util.HashSet;
import java.util.Set;

public abstract class BinaryComparison extends Comparison {
	
	protected Term left;
	protected Term right;

	public BinaryComparison (Term left, Term right, Comparison.Type type) {
		super(type);
		this.left = left;
		this.right = right;
	}

	@Override
	public String toString() {
		return String.format( "%s %s %s",
				left,
				this.getComparisonType().getConnective(),
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
