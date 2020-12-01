package s1705270;

import java.beans.Expression;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class Translator {
	public static Expression translateToRA( Formula f ) {
	    if (f instanceof Conjunction) {
	    		Conjunction conj = (Conjunction) f;
	    		Formula f1 = conj.getLeftOperand();
	    		Formula f2 = conj.getRightOperand();
	    		Set<Term> free1 = f1.free();
	    		Set<Term> free2 = f2.free();
	    		Stack<Expression> temp1 = new Stack<>();
	    		Stack<Expression> temp2 = new Stack<>();
	    		for (Term term : free2) {
	    			if (!free1.contains(term)) {
	    				if (temp1.size() > 0) {
	    					Expression product = Product(temp1.pop(), term);
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
	    		Expression e = Intersection(e1, e2);
	    		return e;
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
	    					Expression product = Product(temp1.pop(), term);
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
	    		
	    		Expression operand = BaseExpression(name);
	    		Map<String,String> replacements = new HashMap<>();
	    		for (Term term : terms) {
	    			replacements.put("?", term.toString()); // what should go into "?"
	    		}
	    		
	    		Expression e = Renaming(replacements, operand); 
	  	    return e;
	    } else if (f instanceof Equality) {
	    		Equality equality = (Equality) f;
	    		Term t1 = equality.getLeftTerm();
	    		Term t2 = equality.getRightTerm();
	    		Condition cond = RA.Equality(t1, t2);
	    		// Expression e3 = Adom(e1) x Adom(e2)
	    		Expression e = Selection(cond, e3);
	    		return e;
	    } else { // we should never reach this point
	      throw new RuntimeException("Unknown kind of formula");
	    }
	  }

	  public static Formula translateToRC( Expression e ) {
	    // similar stuff appropriate for RA->RC
	    return null;
	  }
}



	  
	