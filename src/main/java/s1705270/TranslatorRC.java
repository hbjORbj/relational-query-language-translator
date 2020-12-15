package s1705270;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import uk.ac.ed.pguaglia.real.lang.BaseExpression;
import uk.ac.ed.pguaglia.real.lang.Condition;
import uk.ac.ed.pguaglia.real.lang.Difference;
import uk.ac.ed.pguaglia.real.lang.Expression;
import uk.ac.ed.pguaglia.real.lang.Intersection;
import uk.ac.ed.pguaglia.real.lang.Product;
import uk.ac.ed.pguaglia.real.lang.Projection;
import uk.ac.ed.pguaglia.real.lang.Renaming;
import uk.ac.ed.pguaglia.real.lang.ReplacementException;
import uk.ac.ed.pguaglia.real.lang.Selection;
import uk.ac.ed.pguaglia.real.lang.Union;

public class TranslatorRC {

	private Schema schema;
	private Map<String,String> env;

	public TranslatorRC(Schema sch) {
		this.schema = sch;
	}

	public Expression Adom(String name) {
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
				e1 = new Product(e1, Adom(env.get(term.toString())));
			}
		}
		for (Term term : free1) {
			if (!free2.contains(term)) {
				e2 = new Product(e2, Adom(env.get(term.toString())));
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
				e1 = new Product(e1, Adom(env.get(term.toString())));
			}
		}
		for (Term term : free1) {
			if (!free2.contains(term)) {
				e2 = new Product(e2, Adom(env.get(term.toString())));
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
				env.put(var, attr); // maps variable "?x" to attribute "Ax"
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

		List<Term> terms = ext.getTerms();
		Set<Term> free = ext.free();
		List<String> attributes = new ArrayList<>();
		for (Term term: free) {
			if (!terms.contains(term)) {
				attributes.add(term.toString());
			}
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
		uk.ac.ed.pguaglia.real.lang.Equality cond = new uk.ac.ed.pguaglia.real.lang.Equality(t1,t2);
		Expression exp = null;
		if (t1.isAttribute()) {
			exp = Adom(t1.getValue());
		}
		if (t2.isAttribute()) {
			if (exp == null) {
				exp = Adom(t2.getValue());
			} else {
				exp = new Product(exp, Adom(t2.getValue()));
			}
		}
		return new Selection(cond, exp);
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
		} else { // we should never reach this point
			throw new RuntimeException("Unknown kind of formula");
		}
	}

	public Expression translate(Formula f) throws TranslationException {
		this.env  = new HashMap<>(); // Variable -> Attribute
		return translateToRA(f);
	}

//	private Formula projectionToRC(Projection proj) {
//			Expression e = proj.getOperand();
//			List<String> attributes = proj.getAttributes();
//			Formula f = translateToRC(e);
//			Set<Term> free = f.free();
//			for (Term term : free) {
//				String attr = env.get(term.toString());
//				if (attributes.contains(attr)) {
//					free.remove(term);
//				}
//			}
//			return new Existential(free, f);
//	}
//	
//	private Formula selectionToRC(Selection selec) { //  Not Done yet
//		Expression e = selec.getOperand();
//		Condition cond = selec.getCondition();
//		
//		Set<String> attrs; // attributes appearing in given condition
//		
//		Formula f = translateToRC(e);
//		Set<Term> terms;
//		for (String attr : attrs) {
//			terms.add(env2.get(attr)); // env2 maps attribute to variable (we need to create this)
//		}
//		Formula e1 = new Predicate(terms);
//		Formula e2 = new Equality(terms[0], terms[1]);
//		return new Conjunction(e1, e2);
//	}
//	
//	public Formula translateToRC( Expression e ) {
//		if (e instanceof BaseExpression) {
//			return baseExprToRC((BaseExpression) e);
//		} else if (e instanceof Projection) {
//			return projectionToRC((Projection) e);
//		} else if (e instanceof Selection) {
//			return selectionToRC((Selection) e);
//		} else if (e instanceof Product) {  // not implemented yet
//			return productToRC((Product) e);
//		} else if (e instanceof Union) {  // not implemented yet
//			return unionToRC((Union) e);
//		} else if (e instanceof Difference) {  // not implemented yet
//			return differenceToRC((Difference) e);
//		} else { // we should never reach this point
//			throw new RuntimeException("Unknown kind of formula");
//		}
//	}
}