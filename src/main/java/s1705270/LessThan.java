package s1705270;

public class LessThan extends BinaryComparison {

	public LessThan(Term left, Term right) {
		super(left, right, Type.LESSTHAN);
	}
	
	@Override
	public Formula validRename(Term x, Term y) {
		Term renamedLeft = left.equals(x) ? new Term(y.getValue(), y.isConstant()) : new Term(x.getValue(), x.isConstant());
		Term renamedRight = right.equals(x) ? new Term(y.getValue(), y.isConstant()) : new Term(x.getValue(), x.isConstant());
		return new LessThan(renamedLeft, renamedRight);
	}
}
