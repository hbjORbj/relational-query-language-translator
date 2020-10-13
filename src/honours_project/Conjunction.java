package honours_project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import honours_project.Relation;
import honours_project.Term;

public class Conjunction<R extends Relation> implements Term<R, Boolean>, Iterable<Term<R, Boolean>> {
	
	private Conjunction(List<Term<R, Boolean>> terms) {
		this.terms = terms;
	}
	
	public R relation() {
		return this.terms.get(0).relation;
	}
	
	public Term.Precedence precedence() {
		return Term.Precedence.CONJUNCTION;
	}
}
