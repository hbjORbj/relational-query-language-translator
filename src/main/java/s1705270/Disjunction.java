package s1705270;

public class Disjunction extends BinaryOperation {

	public Disjunction(Formula left, Formula right) {
		super(left, right, Formula.Type.DISJUNCTION);
	}

	@Override
	public Formula validRename(Term x, Term y) {
		Formula renamedLeft = leftOperand.validRename(x, y);
		Formula renamedRight = rightOperand.validRename(x, y);
		return new Disjunction(renamedLeft, renamedRight);
	}
}
