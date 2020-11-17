package s1705270;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import s1705270.RCBaseListener;
import s1705270.RCParser;

public class RCListenerImpl extends RCBaseListener {

	private String predicateName = null;
	private Stack<Formula> formulaStack = new Stack<Formula>();
	private List<Term> termList = null;
	private Stack<Term> termStack = new Stack<Term>();
	private List<Term> varList = null;
	private List<Term> freeVarList = new ArrayList<Term>(); // tracks free variables of the entire formula
	private Set<Term> boundVarList = new HashSet<Term>(); // tracks bound variables of the entire formula
	private Formula parsedFormula = null;
	private boolean inTermList = false;

	public Formula parsedFormula() {
		if (formulaStack.size() != 1) {
			// something went wrong
		}
		if (parsedFormula == null) {
			parsedFormula = formulaStack.pop();
		}
		return parsedFormula;
	}

	@Override
	public void enterTermList(RCParser.TermListContext ctx) {
		termList = new ArrayList<>();
		inTermList = true;
	}
	
	@Override
	public void exitTermList(RCParser.TermListContext ctx) {
		termList = new ArrayList<>();
		inTermList = false;
	}

	@Override
	public void exitVariable(RCParser.VariableContext ctx) {
		String value = ctx.getText().substring(1); // get rid of initial '?'
		Term t = new Term(value, false);
		if ( ctx.getParent() instanceof RCParser.TermListContext ) {
//			if (termList.contains(t)) { // No variable name is repeated within a predicate
//				throw new RuntimeException("You can't repeat a variable within a predicate.");
//			} else {
				termList.add(t);
//			}
		} else if ( ctx.getParent() instanceof RCParser.VariableListContext ) {
			varList.add(t);
		} else {
			termStack.push(t);
		}
	}

	@Override
	public void enterVariableList(RCParser.VariableListContext ctx) {
		varList = new ArrayList<Term>();
	}

	@Override public void exitConstant(RCParser.ConstantContext ctx) {
		String value = ctx.getText();
		Term t = new Term(value, true);
		if (inTermList) {
			termList.add(t);
		} else {
			termStack.push(t);
		}
	}

	@Override
	public void exitPredicateName(RCParser.PredicateNameContext ctx) {
		predicateName = ctx.getText();
	}
	
	@Override
	public void exitPredicate(RCParser.PredicateContext ctx) {
//		for (Term var : termList) {
//			if (boundVarList.contains(var)) { // this variable is already bound by some quantifier somewhere else in the formula
//				throw new RuntimeException("No variable name can occur both free and bound.");
//			} else {
//				freeVarList.add(var);
//			}
//		}
		formulaStack.push(new Predicate(predicateName, termList));
	}

	@Override
	public void exitDisjunction(RCParser.DisjunctionContext ctx) {
		Formula right = formulaStack.pop();
		Formula left = formulaStack.pop();
		formulaStack.push(new Disjunction(left, right));
	}

	@Override
	public void exitConjunction(RCParser.ConjunctionContext ctx) {
		Formula right = formulaStack.pop();
		Formula left = formulaStack.pop();
		formulaStack.push(new Conjunction(left, right));
	}

	@Override
	public void exitImplication(RCParser.ImplicationContext ctx) {
		 Formula right = formulaStack.pop();
		 Formula left = formulaStack.pop();
		 formulaStack.push(new Implication(left, right));
	}

	@Override
	public void exitNegation(RCParser.NegationContext ctx) {
		Formula f = formulaStack.pop();
		formulaStack.push(new Negation(f));
	}

	@Override
	public void exitUniversalQuantifier(RCParser.UniversalQuantifierContext ctx) {
		 Formula formula = formulaStack.pop();
		 formulaStack.push(new Universal(varList, formula));
	}

	@Override
	public void exitExistentialQuantifier(RCParser.ExistentialQuantifierContext ctx) {
		Formula f = formulaStack.pop();
//		Set<Term> free = f.free();
//		for (Term var : varList) {
//			if (boundVarList.contains(var)) { // another quantifier is already binding this variable
//				throw new RuntimeException("No distinct pair of quantifiers can bind the same variable name.");
//			} else {
//				boundVarList.add(var); // this variable is now bound
//			}
//			
//			if (free.contains(var) == false) { // Check whether each term is free in given formula
//				throw new RuntimeException("One or more variables are not free in given formula.");
//			} else {
//				freeVarList.remove(var); // this variable is no more free
//			}
//			
//			if (freeVarList.contains(var)) { // this bound variable is free somewhere else in the formula, so we throw an error
//				throw new RuntimeException("No variable name can occur both free and bound.");
//			}
//		}
		formulaStack.push(new Existential(varList, f));
	}

	@Override
	public void exitEquality(RCParser.EqualityContext ctx) { // No atoms of the form: x op x OR c1 op c2
		Term right = termStack.pop();
		Term left = termStack.pop();
//		if (left.isVariable() && right.isVariable() && left == right) {
//			throw new RuntimeException("You can't use the same variable for comparison.");
//		}
//		if (left.isConstant() && right.isConstant()) {
//			throw new RuntimeException("You can't compare constants.");
//		}
		formulaStack.push(new Equality(left, right));
	}

	@Override
	public void exitLessThan(RCParser.LessThanContext ctx) { // No atoms of the form: x op x OR c1 op c2
		Term right = termStack.pop();
		Term left = termStack.pop();
//		if (left.isVariable() && right.isVariable() && left == right) {
//			throw new RuntimeException("You can't use the same variable for comparison.");
//		}
//		if (left.isConstant() && right.isConstant()) {
//			throw new RuntimeException("You can't compare constants.");
//		}
		formulaStack.push(new LessThan(left, right));
	}
}
