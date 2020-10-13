package honours_project;

import honours_project.Relation;
import honours_project.Term;

public class Negation<R extends Relation> implements Term<R, Boolean> {
	public static <R extends Relation> Negation<R> not(Term<R, Boolean> term) {
		return new Negation<R>(term);
	}
}
