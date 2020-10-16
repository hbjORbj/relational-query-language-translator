package honours_project;

public class Negation extends Formula {
	
	private Formula operand;
	
	public Negation (Formula operand) {
		super(Formula.Type.NEGATION);
		this.operand = operand;
	}
	
	@Override
	public String toString() {
		return String.format("%s(%s)",
				this.getType().getConnective(),
				operand.toString());
	}
}
