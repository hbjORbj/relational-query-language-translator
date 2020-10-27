package s1705270;

import java.util.HashSet;
import java.util.Set;

public abstract class BinaryOperation extends Formula {

	protected Formula leftOperand;
	protected Formula rightOperand;

	public Formula getLeftOperand() {
		return leftOperand;
	}

	public Formula getRightOperand() {
		return rightOperand;
	}

	public BinaryOperation(Formula left, Formula right, Formula.Type type) {
		super(type);
		this.leftOperand = left;
		this.rightOperand = right;
	}

	@Override
	public String toString() {
		return String.format("(%s) %s (%s)", leftOperand, this.getType().getConnective(), rightOperand);
	}

	public Set<Term> free() {
		Set<Term> free = new HashSet<>();
		free.addAll(leftOperand.free());
		free.addAll(rightOperand.free());
		return free;
	}

}
