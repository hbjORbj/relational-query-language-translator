package s1705270;

import java.util.Map;

public class Disjunction extends BinaryOperation {

	public Disjunction(Formula left, Formula right) {
		super(left, right, Formula.Type.DISJUNCTION);
	}

	@Override
	public Formula validRename(Term x, Term y, Map<String, String> renamingEnv) {
		Formula renamedLeft = leftOperand.validRename(x, y, renamingEnv);
		Formula renamedRight = rightOperand.validRename(x, y, renamingEnv);
		return new Disjunction(renamedLeft, renamedRight);
	}
}
