package s1705270;

import java.util.Map;

public class Conjunction extends BinaryOperation {

	public Conjunction(Formula left, Formula right) {
		super(left, right, Formula.Type.CONJUNCTION);
	}

	@Override
	public Formula validRename(Term x, Term y, Map<String, String> renamingEnv) {
		Formula renamedLeft = leftOperand.validRename(x, y, renamingEnv);
		Formula renamedRight = rightOperand.validRename(x, y, renamingEnv);
		return new Conjunction(renamedLeft, renamedRight);
	}
}
