package s1705270;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import com.sun.java.accessibility.util.Translator;

import uk.ac.ed.pguaglia.real.lang.BaseExpression;
import uk.ac.ed.pguaglia.real.lang.Condition;
import uk.ac.ed.pguaglia.real.lang.Expression;
import uk.ac.ed.pguaglia.real.lang.Intersection;
import uk.ac.ed.pguaglia.real.lang.Product;
import uk.ac.ed.pguaglia.real.lang.Selection;

public class TranslatorRC {

	private Schema schema;
	private Map<String,String> env;
	
	public TranslatorRC(Schema sch) {
		this.schema = sch;
		this.env  = new HashMap<>(); // Variable -> Attribute
	}
	
	private Expression Adom(String name) { // needs the schema as well
		return null;
	}

	private Expression conjunctionToRA(Conjunction conj) {
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
	
	private Expression disjunctionToRA(Disjunction disj) {
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
	
	private Expression predicateToRA(Predicate pred) {
		String name = pred.getName();
		List<Term> terms = pred.getTerms();
		
		Expression relation = new BaseExpression(name);
		Map<String,String> replacements = new HashMap<>();
		for (Term term : terms) {
			replacements.put(env.get(term.toString()), term.toString());
		}
		
		return new Renaming(replacements, relation); 
	}
	
	private Expression existentialToRA(Existential ext) {
		Formula f = ext.getOperand();
		Expression e = translateToRA(f);

		List<Term> terms = ext.getTerms();
		Set<Term> free = ext.free();
		List<String> attributes;
		for (Term term: free) {
			if (!terms.contains(term)) {
				attributes.add(term.toString());
			}
		}
		
		return new Projection(attributes, e);
	}
	
	private Expression equalityToRA(Equality eq) {
		uk.ac.ed.pguaglia.real.lang.Term t1 = new uk.ac.ed.pguaglia.real.lang.Term(env.get(eq.getLeftTerm().toString()), false);
		uk.ac.ed.pguaglia.real.lang.Term t2 = new uk.ac.ed.pguaglia.real.lang.Term(env.get(eq.getRightTerm().toString()), false);
		uk.ac.ed.pguaglia.real.lang.Equality cond = new uk.ac.ed.pguaglia.real.lang.Equality(t1,t2);
		return new Selection(cond, new Product(Adom(t1.getValue()), Adom(t2.getValue())));
	}

	public Expression translateToRA( Formula f ) {
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

	private Formula projectionToRC(Projection proj) {
			Expression e = proj.getOperand();
			List<String> attributes = proj.getAttributes();
			Formula f = translateToRC(e);
			Set<Term> free = f.free();
			for (Term term : free) {
				String attr = env.get(term.toString());
				if (attributes.contains(attr)) {
					free.remove(term);
				}
			}
			return new Existential(free, f);
	}
	
	private Formula selectionToRC(Selection selec) { //  Not Done yet
		Expression e = selec.getOperand();
		Condition cond = selec.getCondition();
		
		Set<String> attrs; // attributes appearing in given condition
		
		Formula f = translateToRC(e);
		Set<Term> terms;
		for (String attr : attrs) {
			terms.add(env2.get(attr)); // env2 maps attribute to variable (we need to create this)
		}
		Formula e1 = new Predicate(terms)
		Formula e2 = new Equality(terms[0], terms[1]);
		return new Conjunction(e1, e2);
	}
	
	public static Formula translateToRC( Expression e ) {
		if (e instanceof BaseExpression) {
			return baseExprToRC((BaseExpression) e);
		} else if (e instanceof Projection) {
			return projectionToRC((Projection) e);
		} else if (e instanceof Selection) {
			return selectionToRC((Selection) e);
		} else if (e instanceof Product) {  // not implemented yet
			return productToRC((Product) e);
		} else if (e instanceof Union) {  // not implemented yet
			return unionToRC((Union) e);
		} else if (e instanceof Difference) {  // not implemented yet
			return differenceToRC((Difference) e);
		} else { // we should never reach this point
			throw new RuntimeException("Unknown kind of formula");
		}
	}
}