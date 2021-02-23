package s1705270;

public class Implication extends BinaryOperation {

	public Implication(Formula left, Formula right) {
		super(left, right, Formula.Type.IMPLICATION);
	}

	@Override
	public Formula validRename(Term x, Term y) {
		Formula renamedLeft = leftOperand.validRename(x, y);
		Formula renamedRight = rightOperand.validRename(x, y);
		return new Implication(renamedLeft, renamedRight);
	}
}
