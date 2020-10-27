package honours_project;

public class Implication extends BinaryOperation {

	public Implication(Formula left, Formula right) {
		super(left, right, Formula.Type.IMPLICATION);
	}

}
