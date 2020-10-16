package honours_project;

import java.util.ArrayList;
import java.util.List;

public class Existential extends Expression {
	private Expression operand;
	private List<String> terms;
	
	public Existential (List<String> terms, Expression operand) {
		super(Expression.Type.EXISTENTIAL);
		this.operand = operand;
		this.terms = new ArrayList<>();
		this.terms.addAll(terms);
	}
	
	@Override
	public String toString() {
		return String.format("%s%s( %s )",
				this.getType().getConnective(),
				String.join(",", terms),
				operand.toString());
	}
}
