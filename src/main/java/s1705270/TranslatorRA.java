package s1705270;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import uk.ac.ed.pguaglia.real.db.SchemaException;
import uk.ac.ed.pguaglia.real.lang.BaseExpression;
import uk.ac.ed.pguaglia.real.lang.Condition;
import uk.ac.ed.pguaglia.real.lang.Difference;
import uk.ac.ed.pguaglia.real.lang.Expression;
import uk.ac.ed.pguaglia.real.lang.Intersection;
import uk.ac.ed.pguaglia.real.lang.Product;
import uk.ac.ed.pguaglia.real.lang.Projection;
import uk.ac.ed.pguaglia.real.lang.Renaming;
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
			if (!env.containsKey(attr)) {
				var = "?" + attr;
				env.put(attr, var); // maps attribute "A" to variable "?A"
			} else {
				var = env.get(attr);
			}

			Term t = new Term(var.substring(1), false);
			terms.add(t);
		}
		return new Predicate(name, terms);
	}
	
	private Formula projectionToRC(Projection proj) throws TranslationException {
			Expression e = proj.getOperand();
			Set<String> attrs;
			try {
				attrs = proj.signature(schema.convert());
			} catch (SchemaException e1) {
				throw new TranslationException(e1.getMessage());
			}
			Formula f = translateToRC(e);
			List<Term> existentialVars = new ArrayList<>();
			for (Term freeVar : f.free()) {
				if (attrs.contains(freeVar.getValue()) == false) {
					existentialVars.add(freeVar);
				}
			}
			if (existentialVars.isEmpty()) {
				return f;
			}
			return new Existential(existentialVars, f);
	}
	
	private Formula selectionToRC(Selection sel) throws TranslationException { 
		Expression e = sel.getOperand();
		Condition cond = sel.getCondition();
		Formula f1 = translateToRC(e);
		Formula f2 = translateToRC(cond);
		return new Conjunction(f1, f2);
	}
	
	private Formula renamingToRC(Renaming ren) throws TranslationException {
		Expression e = ren.getOperand();
		Formula f = translateToRC(e);
		Map<String,String> replacements = ren.getReplacements();
		if (replacements.keySet().size() > 1) {
			throw new TranslationException("Only one replacement is allowed");
		}
		for (String fromAttr : replacements.keySet()) {
			String toAttr = replacements.get(fromAttr);
			if (!env.containsKey(toAttr)) {
				String var = "?" + toAttr;
				env.put(toAttr, var);
			}
			Term from = new Term(fromAttr, false);
			Term to = new Term(toAttr, false);
			Term primed = new Term(toAttr + "'", false);
			try {
				f = f.rename(to, primed);
				f = f.rename(from, to);
			} catch (Exception e1) {
				throw new TranslationException(e1.getMessage());
			}
		}
		return f;
	}
	
	private Formula productToRC(Product prod) throws TranslationException { 
		Expression left = prod.getLeftOperand();
		Expression right = prod.getRightOperand();
		Formula f1 = translateToRC(left);
		Formula f2 = translateToRC(right);
		return new Conjunction(f1, f2);
	}
	
	private Formula intersectionToRC(Intersection inter) throws TranslationException { 
		Expression left = inter.getLeftOperand();
		Expression right = inter.getRightOperand();
		Formula f1 = translateToRC(left);
		Formula f2 = translateToRC(right);
		return new Conjunction(f1, f2);
	}
	
	private Formula unionToRC(Union uni) throws TranslationException { 
		Expression left = uni.getLeftOperand();
		Expression right = uni.getRightOperand();
		Formula f1 = translateToRC(left);
		Formula f2 = translateToRC(right);
		return new Disjunction(f1, f2);
	}
	
	private Formula differenceToRC(Difference diff) throws TranslationException { 
		Expression left = diff.getLeftOperand();
		Expression right = diff.getRightOperand();
		Formula f1 = translateToRC(left);
		Formula f2 = translateToRC(right);
		return new Conjunction(f1, new Negation(f2));
	}
	
	public Formula translateToRC( Expression e ) throws TranslationException {
		if (e instanceof BaseExpression) {
			return baseExprToRC((BaseExpression) e);
		} else if (e instanceof Projection) {
			return projectionToRC((Projection) e);
		} else if (e instanceof Selection) {
			return selectionToRC((Selection) e);
		} else if (e instanceof Renaming) {
			return renamingToRC((Renaming) e);
		} else if (e instanceof Product) { 
			return productToRC((Product) e);
		} else if (e instanceof Intersection) { 
			return intersectionToRC((Intersection) e);
		} else if (e instanceof Union) { 
			return unionToRC((Union) e);
		} else if (e instanceof Difference) {  
			return differenceToRC((Difference) e);
		} else { // we should never reach this point
			throw new RuntimeException("Unknown kind of expression");
		}
	}
	
//	private Formula renameConjunction(Conjunction conj, String fromAttr, String toAttr) throws TranslationException { 
//		Formula f1 = conj.getLeftOperand();
//		Formula f2 = conj.getRightOperand();
//		f1 = renameFormula(f1, fromAttr, toAttr);
//		f2 = renameFormula(f2, fromAttr, toAttr);
//		return new Conjunction(f1, f2);
//	}
//	
//	private Formula renameDisjunction(Disjunction disj, String fromAttr, String toAttr) throws TranslationException { 
//		Formula f1 = disj.getLeftOperand();
//		Formula f2 = disj.getRightOperand();
//		f1 = renameFormula(f1, fromAttr, toAttr);
//		f2 = renameFormula(f2, fromAttr, toAttr);
//		return new Disjunction(f1, f2);
//	}
//	
//	private Formula renameExistential(Existential exist, String fromAttr, String toAttr) throws TranslationException { 
//		Formula operand = exist.getOperand();
//		List<Term> terms = exist.getTerms();
//		List<Term> newTerms = new ArrayList<Term>();
//		
//		for (Term t : terms) {
//			if (t.getValue().equals(fromAttr)) {
//				newTerms.add(new Term(toAttr, false));
//			} else if (t.getValue().equals(toAttr)) {
//				newTerms.add(new Term(toAttr + "2", false));
//			} else {
//				newTerms.add(t);
//			}
//		}
//		
//		operand = renameFormula(operand, fromAttr, toAttr);
//		return new Existential(newTerms, operand);
//	}
//	
//	private Formula renamePredicate(Predicate pred, String fromAttr, String toAttr) throws TranslationException { 
//		String name = pred.getName();
//		List<Term> terms = pred.getTerms();
//		List<Term> newTerms = new ArrayList<Term>();
//		for (Term t : terms) {
//			if (t.getValue().equals(fromAttr)) {
//				newTerms.add(new Term(toAttr, false));
//			} else if (t.getValue().equals(toAttr)) {
//				newTerms.add(new Term(toAttr + "2", false));
//			} else {
//				newTerms.add(t);
//			}
//		}
//		return new Predicate(name, newTerms);
//	}
//	
//	private Formula renameEquality(Equality eq, String fromAttr, String toAttr) throws TranslationException { 
//		Term leftTerm = eq.getLeftTerm();
//		Term rightTerm = eq.getRightTerm();
//		if (leftTerm.getValue().equals(fromAttr)) {
//			leftTerm = new Term(toAttr, leftTerm.isConstant());
//		}
//		if (rightTerm.getValue().equals(fromAttr)) {
//			rightTerm = new Term(toAttr, rightTerm.isConstant());
//		}
//		if (leftTerm.getValue().equals(toAttr)) {
//			leftTerm = new Term(toAttr + "2", leftTerm.isConstant());
//		}
//		if (rightTerm.getValue().equals(toAttr)) {
//			rightTerm = new Term(toAttr + "2", rightTerm.isConstant());
//		}
//		return new Equality(leftTerm, rightTerm);
//	}
//	
//	private Formula renameNegation(Negation neg, String fromAttr, String toAttr) throws TranslationException { 
//		Formula operand = neg.getOperand();
//		operand = renameFormula(operand, fromAttr, toAttr);
//		return new Negation(operand);
//	}
	
//	private Formula renameFormula( Formula f, String fromAttr, String toAttr ) throws TranslationException {
//		if (f instanceof Conjunction) {
//			return renameConjunction((Conjunction) f, fromAttr, toAttr);
//		} else if (f instanceof Disjunction) {
//			return renameDisjunction((Disjunction) f, fromAttr, toAttr);
//		} else if (f instanceof Existential) {
//			return renameExistential((Existential) f, fromAttr, toAttr);
//		} else if (f instanceof Predicate) {
//			return renamePredicate((Predicate) f, fromAttr, toAttr);
//		} else if (f instanceof Equality) {
//			return renameEquality((Equality) f, fromAttr, toAttr);
//		} else if (f instanceof Negation) {
//			return renameNegation((Negation) f, fromAttr, toAttr);
//		} else { // we should never reach this point
//			throw new RuntimeException("Unknown kind of formula");
//		}
//	}
	
	private Formula equalityToRC(uk.ac.ed.pguaglia.real.lang.Equality c) { // RA Equality needs these methods: getLeftTerm() and getRightTerm()
		uk.ac.ed.pguaglia.real.lang.Term leftTerm = c.getLeftTerm();
		uk.ac.ed.pguaglia.real.lang.Term rightTerm = c.getRightTerm();
		Term t1 = new Term(env.get(leftTerm.getValue()).substring(1), leftTerm.isConstant());
		Term t2 = new Term(env.get(rightTerm.getValue()).substring(1), rightTerm.isConstant());
		return new Equality(t1, t2);
	}
	
//	private Formula lessThanToRC(uk.ac.ed.pguaglia.real.lang.LessThan c) { // RA LessThan Class is needed
//		uk.ac.ed.pguaglia.real.lang.Term leftTerm = c.getLeftTerm();
//		uk.ac.ed.pguaglia.real.lang.Term rightTerm = c.getRightTerm();
//		Term t1 = new Term(env.get(leftTerm.getValue()), leftTerm.isConstant());
//		Term t2 = new Term(env.get(rightTerm.getValue()), rightTerm.isConstant());
//		return new LessThan(t1, t2);
//	}
	
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
		} else if (c instanceof uk.ac.ed.pguaglia.real.lang.Conjunction) {
			return conjunctionToRC((uk.ac.ed.pguaglia.real.lang.Conjunction) c);
		} else if (c instanceof uk.ac.ed.pguaglia.real.lang.Disjunction) {
			return disjunctionToRC((uk.ac.ed.pguaglia.real.lang.Disjunction) c);
		} else if (c instanceof uk.ac.ed.pguaglia.real.lang.Negation) {
			return negationToRC((uk.ac.ed.pguaglia.real.lang.Negation) c);
		} else { // we should never reach this point
			throw new RuntimeException("Unknown kind of condition");
		}
	}
	
	public Formula translate(Expression e) throws TranslationException {
		this.env  = new HashMap<>(); // Attribute -> Variable
		return translateToRC(e);
	}
}
