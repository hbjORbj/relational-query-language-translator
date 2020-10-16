package honours_project;

import java.util.List;
import java.util.ArrayList;

public class Universal extends Expression {
	private Expression operand;
	private List<String> terms;
	
	public Universal (List<String> terms, Expression operand) {
		super(Expression.Type.UNIVERSAL);
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
