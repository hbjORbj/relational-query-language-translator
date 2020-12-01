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
		this.env  = new HashMap<>();
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
	    		Disjunction disj = (Disjunction) f;
	    		Formula f1 = disj.getLeftOperand();
	    		Formula f2 = disj.getRightOperand();
	    		Set<Term> free1 = f1.free();
	    		Set<Term> free2 = f2.free();
	    		Stack<Expression> temp1 = new Stack<>();
	    		Stack<Expression> temp2 = new Stack<>();
	    		for (Term term : free2) {
	    			if (!free1.contains(term)) {
	    				if (temp1.size() > 0) {
	    					Expression product = Product(temp1.pop(), Adom(""));
	    					temp1.push(Adom(product)); // Adom() hasn't yet been written
	    				} else {
	    					temp1.push(Adom(term));
	    				}
	    			}
	    		}
	    		for (Term term : free1) {
	    			if (!free2.contains(term)) {
	    				if (temp2.size() > 0) {
	    					Expression product = Product(temp2.pop(), term);
	    					temp2.push(Adom(product));
	    				} else {
	    					temp2.push(Adom(term));
	    				}
	    			}
	    		}
	    		
	    		Expression e1 = Translator.translateToRA(f1);
	    		Expression e2 = Translator.translateToRA(f2);
	    		Expression adomProduct1;
	    		Expression adomProduct2;
	    		
	    		e1 = Product(e1, temp1.pop());
	    		e2 = Product(e2, temp2.pop());
	    		Expression e = Union(e1, e2);
	    		return e;
	    } else if (f instanceof Existential) {
	    		Existential exist = (Existential) f;
	    		Formula f1 = exist.getOperand();
	    		Expression e1 = translateToRA(f1);

	    		List<Term> terms = exist.getTerms();
	    		Set<Term> free = exist.free();
	    		List<String> attributes;
	    		for (Term term: free) {
	    			if (!terms.contains(term)) {
	    				attributes.add(term.toString());
	    			}
	    		}
	    		
	    		Expression e = Projection(attributes, e1);
	  	    return e;
	    } else if (f instanceof Predicate) {
	    		Predicate predic = (Predicate) f;
	    		String name = predic.getName();
	    		List<Term> terms = predic.getTerms();
	    		
	    		Expression operand = new BaseExpression(name);
	    		Map<String,String> replacements = new HashMap<>();
	    		for (Term term : terms) {
	    			replacements.put("?", term.toString()); // what should go into "?"
	    		}
	    		
	    		Expression e = Renaming(replacements, operand); 
	  	    return e;
	    } else if (f instanceof Equality) {
	    		return equalityToRA((Equality) f);
	    } else { // we should never reach this point
	      throw new RuntimeException("Unknown kind of formula");
	    }
	  }

	  public static Formula translateToRC( Expression e ) {
	    // similar stuff appropriate for RA->RC
	    return null;
	  }
}