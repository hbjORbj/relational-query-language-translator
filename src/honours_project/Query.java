package honours_project;

import java.util.ArrayList;
import java.util.List;

public class Query {
	
	private List<Term> terms;
	private Formula operand;
	
	public Query (List<Term> terms, Formula operand) {
		this.terms = new ArrayList<>();
		this.terms.addAll(terms);
	}
	
	@Override
	public String toString() {
		List<String> list = new ArrayList<>();
		String vars;
		for (Term t : terms) {
			list.add(t.toString());
		}
		if (list.isEmpty()) {
			vars = "()";
		}
		else {
			vars = String.join(",", list);
		}
		
		return String.format("{%s | %s}", vars, operand.toString());
	}
	
}
