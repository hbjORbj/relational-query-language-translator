package s1705270;

public class Implication extends BinaryOperation {

	public Implication(Formula left, Formula right) {
		super(left, right, Formula.Type.IMPLICATION);
	}

}
