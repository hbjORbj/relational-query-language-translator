package honours_project;

public class Negation extends Expression {
	
	private Expression operand;
	
	public Negation (Expression operand) {
		super(Expression.Type.NEGATION);
		this.operand = operand;
	}
	
	@Override
	public String toString() {
		return String.format("%s(%s)",
				this.getType().getConnective(),
				operand.toString());
	}
}
