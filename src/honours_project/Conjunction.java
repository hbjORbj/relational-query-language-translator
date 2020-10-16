package honours_project;

public class Conjunction extends BinaryCondition {
	public Conjunction(Condition left, Condition right) {
		super(left, right, Condition.Type.CONJUNCTION);
	}
}
