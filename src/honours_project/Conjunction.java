package honours_project;

public class Conjunction extends BinaryOperation {
	
	public Conjunction (Formula left, Formula right) {
		super(left, right, Formula.Type.CONJUNCTION);
	}
	
}
