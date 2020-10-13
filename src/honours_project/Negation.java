package honours_project;

import honours_project.Term;

public class Negation<Relation> implements Term<Relation, Element> {
	public static <Relation> Negation<Relation> not(Term<Relation, Element> term) {
		return new Negation<Relation>(term);
	}

	private Term term;

	private Negation(Term term) {
		super();
		this.term = term;
	}

	public Term.Precedence precedence() {
		return Term.Precedence.NEGATION;
	}
}
