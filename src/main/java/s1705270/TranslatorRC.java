package s1705270;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import uk.ac.ed.pguaglia.real.lang.BaseExpression;
import uk.ac.ed.pguaglia.real.lang.Difference;
import uk.ac.ed.pguaglia.real.lang.Expression;
import uk.ac.ed.pguaglia.real.lang.Intersection;
import uk.ac.ed.pguaglia.real.lang.Product;
import uk.ac.ed.pguaglia.real.lang.Projection;
import uk.ac.ed.pguaglia.real.lang.Renaming;
import uk.ac.ed.pguaglia.real.lang.ReplacementException;
import uk.ac.ed.pguaglia.real.lang.Selection;
import uk.ac.ed.pguaglia.real.lang.Union;

public class TranslatorRC { // Translates RC into RA

	private Schema schema;
	private Map<String,String> env; // Variable -> Attribute

	public TranslatorRC(Schema sch) {
		this.schema = sch;
	}

	public Expression adom(String name) {
		return adom(name,true);
	}

	public Expression adom(String name, boolean view) {
		if (view) {
			return new BaseExpression("Adom_" + name);
		}
		return adomExpr(name);
	}

	public Expression adomExpr(String name) {
		Expression exp = null;
		for (String rel : schema.getRelations()) {
			for (String attr : schema.getAttributes(rel)) {
				Map<String,String> repl = new HashMap<>();
				repl.put(attr, name);
				try {
					Expression exp2 = new Renaming(repl, new Projection(Arrays.asList(new String[] {attr}), new BaseExpression(rel)));
					if (exp == null) {
						exp = exp2;
					} else {
						exp = new Union(exp, exp2);
					}
				} catch (ReplacementException e) {
					throw new RuntimeException(e);
				}
			}
		}
		return exp;
	}

	private Expression conjunctionToRA(Conjunction conj) throws TranslationException {
		Formula f1 = conj.getLeftOperand();
		Formula f2 = conj.getRightOperand();
		Set<Term> free1 = f1.free();
		Set<Term> free2 = f2.free();

		Expression e1 = translateToRA(f1);
		Expression e2 = translateToRA(f2);

		for (Term term : free2) {
			if (!free1.contains(term)) {
				e1 = new Product(e1, adom(env.get(term.toString())));
			}
		}

		for (Term term : free1) {
			if (!free2.contains(term)) {
				e2 = new Product(e2, adom(env.get(term.toString())));
			}
		}

		return new Intersection(e1, e2);
	}

	private Expression disjunctionToRA(Disjunction disj) throws TranslationException {
		Formula f1 = disj.getLeftOperand();
		Formula f2 = disj.getRightOperand();
		Set<Term> free1 = f1.free();
		Set<Term> free2 = f2.free();
		
		Expression e1 = translateToRA(f1);
		Expression e2 = translateToRA(f2);

		for (Term term : free2) {
			if (!free1.contains(term)) {
				e1 = new Product(e1, adom(env.get(term.toString())));
			}
		}

		for (Term term : free1) {
			if (!free2.contains(term)) {
				e2 = new Product(e2, adom(env.get(term.toString())));
			}
		}

		return new Union(e1, e2);
	}

	private Expression predicateToRA(Predicate pred) throws TranslationException {
		String name = pred.getName();
		List<Term> terms = pred.getTerms();
		Expression relation = new BaseExpression(name);
		List<String> attributes = schema.getAttributes(name);
		Map<String,String> replacements = new HashMap<>();
		for (int i=0; i < attributes.size(); i++) {
			Term t = terms.get(i); 
			String var = t.toString();
			String attr = null;
			if (env.containsKey(var)) {
				attr = env.get(var);
			} else {
				attr = "A" + t.getValue();
				env.put(var, attr); // maps variable "?x1" to attribute "Ax1"
			}
			replacements.put(attributes.get(i), attr);
		}
		try {
			return new Renaming(replacements, relation);
		} catch ( ReplacementException e ) {
			throw new TranslationException(e.getMessage());
		}
	}

	private Expression existentialToRA(Existential ext) throws TranslationException {
		Formula f = ext.getOperand();
		Expression e = translateToRA(f);

		Set<Term> free = ext.free();
		List<String> attributes = new ArrayList<>();
		for (Term term : free) {
			attributes.add(env.get(term.toString()));
		}

		return new Projection(attributes, e);
	}

	private uk.ac.ed.pguaglia.real.lang.Term termToTerm(Term t) {
		if (t.isConstant()) {
			return new uk.ac.ed.pguaglia.real.lang.Term(t.getValue(), true);
		}
		if (t.isVariable()) {
			String var = t.toString();
			String attr = null;
			if (env.containsKey(var)) {
				attr = env.get(var);
			} else {
				attr = "A" + t.getValue();
				env.put(var, attr);
			}
			return new uk.ac.ed.pguaglia.real.lang.Term(attr, false);
		}
		return null;
	}

	private Expression equalityToRA(Equality eq) {
		uk.ac.ed.pguaglia.real.lang.Term t1 = termToTerm(eq.getLeftTerm());
		uk.ac.ed.pguaglia.real.lang.Term t2 = termToTerm(eq.getRightTerm());

		if ((t1.isConstant() && t2.isConstant()) || (t1.isAttribute() && t2.isAttribute() && t1.getValue().equals(t2.getValue()))) { // No atoms of the form: c1 op c2 OR x op x
			return null;
		}


		uk.ac.ed.pguaglia.real.lang.Equality cond = new uk.ac.ed.pguaglia.real.lang.Equality(t1,t2);
		Expression exp = null;
		if (t1.isAttribute()) {
			exp = adom(t1.getValue());
		}
		if (t2.isAttribute()) {
			if (exp == null) {
				exp = adom(t2.getValue());
			} else {
				exp = new Product(exp, adom(t2.getValue()));
			}
		}

		return new Selection(cond, exp);
	}

	//	private Expression lessThanToRA(LessThan eq) {
	//		uk.ac.ed.pguaglia.real.lang.Term t1 = termToTerm(eq.getLeftTerm());
	//		uk.ac.ed.pguaglia.real.lang.Term t2 = termToTerm(eq.getRightTerm());
	//		if ((t1.isConstant() && t2.isConstant()) || (t1 == t2)) { // No atoms of the form: c1 op c2 OR x op x
	//			return null;
	//		}
	//		
	//		uk.ac.ed.pguaglia.real.lang.LessThan cond = new uk.ac.ed.pguaglia.real.lang.LessThan(t1,t2); // RA LessThan Class is needed
	//		Expression exp = null;
	//		if (t1.isAttribute()) {
	//			exp = Adom(t1.getValue());
	//		}
	//		if (t2.isAttribute()) {
	//			if (exp == null) {
	//				exp = Adom(t2.getValue());
	//			} else {
	//				exp = new Product(exp, Adom(t2.getValue()));
	//			}
	//		}
	//		return new Selection(cond, exp);
	//	}

	private Expression negationToRA(Negation neg) throws TranslationException {
		Formula f = neg.getOperand();
		Set<Term> free = neg.free();
		Expression exp2 = translateToRA(f);

		Expression exp1 = null;
		for (Term term : free) {
			if (exp1 == null) {
				exp1 = adom(env.get(term.toString()));
			} else {
				exp1 = new Product(exp1, adom(env.get(term.toString())));				
			}
		}
		return new Difference(exp1, exp2);
	}

	private Expression translateToRA( Formula f ) throws TranslationException {
		if (f instanceof Conjunction) {
			return conjunctionToRA((Conjunction) f);
		} else if (f instanceof Disjunction) {
			return disjunctionToRA((Disjunction) f);
		} else if (f instanceof Existential) {
			return existentialToRA((Existential) f);
		} else if (f instanceof Predicate) {
			return predicateToRA((Predicate) f);
		} else if (f instanceof Equality) {
			return equalityToRA((Equality) f);
		} else if (f instanceof LessThan) {
			throw new TranslationException("NOT IMPLEMENTED");
		} else if (f instanceof Negation) {
			return negationToRA((Negation) f);
		} else { // we should never reach this point
			throw new RuntimeException("Unknown kind of formula");
		}
	}

	public Expression translate(Formula f) throws TranslationException {
		this.env  = new HashMap<>(); // Variable -> Attribute
		return translateToRA(f);
	}
}