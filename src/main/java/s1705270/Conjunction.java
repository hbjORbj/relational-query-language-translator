package s1705270;

public class Conjunction extends BinaryOperation {

	public Conjunction(Formula left, Formula right) {
		super(left, right, Formula.Type.CONJUNCTION);
	}

	@Override
	public Formula validRename(Term x, Term y) {
		Formula renamedLeft = leftOperand.validRename(x, y);
		Formula renamedRight = rightOperand.validRename(x, y);
		return new Conjunction(renamedLeft, renamedRight);
	}
}
