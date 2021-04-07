package s1705270;

import java.util.Map;

public class Equality extends BinaryComparison {

	public Equality(Term left, Term right) {
		super(left, right, Type.EQUAL);
	}

	@Override
	public Formula validRename(Term x, Term y, Map<String, String> renamingEnv) {
		Term renamedLeft = left.equals(x) ? new Term(y.getValue(), y.isConstant()) : new Term(x.getValue(), x.isConstant());
		Term renamedRight = right.equals(x) ? new Term(y.getValue(), y.isConstant()) : new Term(x.getValue(), x.isConstant());
		return new Equality(renamedLeft, renamedRight);
	}
}
