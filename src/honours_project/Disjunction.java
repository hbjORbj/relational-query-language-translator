package honours_project;

public class Disjunction extends BinaryCondition {
	public Disjunction(Condition left, Condition right) {
		super(left, right, Condition.Type.DISJUNCTION);
	}
}
