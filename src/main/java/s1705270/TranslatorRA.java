package s1705270;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import uk.ac.ed.pguaglia.real.lang.BaseExpression;
import uk.ac.ed.pguaglia.real.lang.Condition;
import uk.ac.ed.pguaglia.real.lang.Difference;
import uk.ac.ed.pguaglia.real.lang.Expression;
import uk.ac.ed.pguaglia.real.lang.Equality;
import uk.ac.ed.pguaglia.real.lang.Disjunction;
import uk.ac.ed.pguaglia.real.lang.Conjunction;
import uk.ac.ed.pguaglia.real.lang.Intersection;
import uk.ac.ed.pguaglia.real.lang.Product;
import uk.ac.ed.pguaglia.real.lang.Projection;
import uk.ac.ed.pguaglia.real.lang.Renaming;
import uk.ac.ed.pguaglia.real.lang.ReplacementException;
import uk.ac.ed.pguaglia.real.lang.Selection;
import uk.ac.ed.pguaglia.real.lang.Union;

public class TranslatorRA { // Translates RA into RC

	private Schema schema;
	private Map<String,String> env; // Attribute -> Variable

	public TranslatorRA(Schema sch) {
		this.schema = sch;
	}
	
	private Formula baseExprToRC(BaseExpression e) {
		String name = e.toString();
		List<Term> terms = new ArrayList<>();
		List<String> attrs = schema.getAttributes(name);
		for (String attr : attrs) {
			String var = null;
			if (env.containsKey(attr)) {
				var = env.get(attr);
			} else {
				var = "?" + attr;
				env.put(attr, var); // maps attribute "A" to variable "?A"
			}
			Term t = new Term(var, false);
			terms.add(t);
		}
		return new Predicate(name, terms);
	}
	
	private Formula projectionToRC(Projection proj) {
			Expression e = proj.getOperand(); // Projection needs these methods: getOperand() and getAttributes()
			List<String> attrs = proj.getAttributes();
			Formula f = translateToRC(e);
			Set<Term> free = f.free();
			List<Term> terms = new ArrayList<>();
			
			for (String attr : attrs) {
				String var = env.get(attr);
				Term t = new Term(var, false);
				if (free.contains(t)) {
					free.remove(t);
				}
			}
			terms.addAll(free);
		
			return new Existential(terms, f);
	}
	
	private Formula selectionToRC(Selection sel) { 
		Expression e = sel.getOperand(); // Selection needs these methods: getOperand() and getCondition()
		Condition cond = sel.getCondition();
		Formula f1 = translateToRC(e);
		Formula f2 = translateToRC(cond);
		return new Conjunction(f1, f2);
	}
	
	private Formula productToRC(Product prod) { 
		Expression left = prod.getLeftOperand();
		Expression right = prod.getRightOperand();
		Formula f1 = translateToRC(left);
		Formula f2 = translateToRC(right);
		return new Conjunction(f1, f2);
	}
	
	private Formula unionToRC(Union uni) { 
		Expression left = uni.getLeftOperand();
		Expression right = uni.getRightOperand();
		Formula f1 = translateToRC(left);
		Formula f2 = translateToRC(right);
		return new Disjunction(f1, f2);
	}
	
	private Formula differenceToRC(Difference diff) { 
		Expression left = diff.getLeftOperand();
		Expression right = diff.getRightOperand();
		Formula f1 = translateToRC(left);
		Formula f2 = translateToRC(right);
		return new Conjunction(f1, new Negation(f2));
	}
	
	public Formula translateToRC( Expression e ) {
		if (e instanceof BaseExpression) {
			return baseExprToRC((BaseExpression) e);
		} else if (e instanceof Projection) {
			return projectionToRC((Projection) e);
		} else if (e instanceof Selection) {
			return selectionToRC((Selection) e);
		} else if (e instanceof Product) { 
			return productToRC((Product) e);
		} else if (e instanceof Union) { 
			return unionToRC((Union) e);
		} else if (e instanceof Difference) {  
			return differenceToRC((Difference) e);
		} else { // we should never reach this point
			throw new RuntimeException("Unknown kind of expression");
		}
	}
	
	private Formula equalityToRC(uk.ac.ed.pguaglia.real.lang.Equality c) { // RA Equality needs these methods: getLeftTerm() and getRightTerm()
		uk.ac.ed.pguaglia.real.lang.Term leftTerm = c.getLeftTerm();
		uk.ac.ed.pguaglia.real.lang.Term rightTerm = c.getRightTerm();
		Term t1 = new Term(env.get(leftTerm.getValue()), leftTerm.isConstant());
		Term t2 = new Term(env.get(rightTerm.getValue()), rightTerm.isConstant());
		return new Equality(t1, t2);
	}
	
	private Formula lessThanToRC(uk.ac.ed.pguaglia.real.lang.LessThan c) { // RA LessThan Class is needed
		uk.ac.ed.pguaglia.real.lang.Term leftTerm = c.getLeftTerm();
		uk.ac.ed.pguaglia.real.lang.Term rightTerm = c.getRightTerm();
		Term t1 = new Term(env.get(leftTerm.getValue()), leftTerm.isConstant());
		Term t2 = new Term(env.get(rightTerm.getValue()), rightTerm.isConstant());
		return new LessThan(t1, t2);
	}
	
	private Formula conjunctionToRC(uk.ac.ed.pguaglia.real.lang.Conjunction c) { // RA Conjunction needs these methods: getLeftCondtion() and getRightCondition()
		Condition leftCond = c.getLeftCondition();
		Condition rightCond = c.getRightCondition();
		Formula f1 = translateToRC(leftCond);
		Formula f2 = translateToRC(rightCond);
		return new Conjunction(f1, f2);
	}
	
	private Formula disjunctionToRC(uk.ac.ed.pguaglia.real.lang.Disjunction c) { // RA Disjunction needs these methods: getLeftCondtion() and getRightCondition()
		Condition leftCond = c.getLeftCondition();
		Condition rightCond = c.getRightCondition();
		Formula f1 = translateToRC(leftCond);
		Formula f2 = translateToRC(rightCond);
		return new Disjunction(f1, f2);
	}
	
	private Formula negationToRC(uk.ac.ed.pguaglia.real.lang.Negation c) {
		Condition cond = c.getCondition(); // RA Negation needs getCondition() method
		Formula f = translateToRC(cond);
		return new Negation(f);
	}
	
	public Formula translateToRC( Condition c ) {
		if (c instanceof uk.ac.ed.pguaglia.real.lang.Equality) {
			return equalityToRC((uk.ac.ed.pguaglia.real.lang.Equality) c);
		} else if (c instance of uk.ac.ed.pguaglia.real.lang.LessThan) {
			return lessThanToRC((uk.ac.ed.pguaglia.real.lang.LessThan) c);
		} else if (c instanceof uk.ac.ed.pguaglia.real.lang.Conjunction) {
			return conjunctionToRC((uk.ac.ed.pguaglia.real.lang.Conjunction) c);
		} else if (c instanceof uk.ac.ed.pguaglia.real.lang.Disjunction) {
			return disjunctionToRC((uk.ac.ed.pguaglia.real.lang.Disjunction) c);
		} else if (c instance of uk.ac.ed.pguaglia.real.lang.Negation) {
			return negationToRC((uk.ac.ed.pguaglia.real.lang.Negation) c);
		}
	}
	
	public Formula translate(Expression e) throws TranslationException {
		this.env  = new HashMap<>(); // Attribute -> Variable
		return translateToRC(e);
	}
}
