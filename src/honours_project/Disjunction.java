package honours_project;

public class Disjunction extends BinaryOperation {
	
	public Disjunction (Formula left, Formula right) {
		super(left, right, Formula.Type.DISJUNCTION);
	}
	
}
