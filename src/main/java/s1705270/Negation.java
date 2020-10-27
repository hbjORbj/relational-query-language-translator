package s1705270;

import java.util.HashSet;
import java.util.Set;

public class Negation extends Formula {

	private Formula operand;

	public Negation(Formula operand) {
		super(Formula.Type.NEGATION);
		this.operand = operand;
	}

	@Override
	public String toString() {
		return String.format("%s(%s)", this.getType().getConnective(), operand.toString());
	}

	public Set<Term> free() {
		Set<Term> free = new HashSet<>();
		free.addAll(operand.free());
		return free;
	}
}
