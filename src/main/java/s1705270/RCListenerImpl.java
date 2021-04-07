package s1705270;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;



public class RCListenerImpl extends RCBaseListener {

	private String predicateName = null;
	private Stack<Formula> formulaStack = new Stack<Formula>();
	private List<Term> termList = null;
	private Stack<Term> termStack = new Stack<Term>();
	private List<Term> varList = null;
	private Stack<List<Term>> varListStack = new Stack<List<Term>>();
	private Formula parsedFormula = null;

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
	public void exitVariable(RCParser.VariableContext ctx) {
		String value = ctx.getText().substring(1); // get rid of initial '?'
		Term t = new Term(value, false);
		if ( ctx.getParent() instanceof RCParser.TermContext && (
				ctx.getParent().getParent() instanceof RCParser.LessThanContext ||
				ctx.getParent().getParent() instanceof RCParser.EqualityContext )) {
			termStack.push(t);
		} else if ( ctx.getParent() instanceof RCParser.VariableListContext ) {
			varList.add(t);
		} else {
			termList.add(t);
		}
	}

	@Override
	public void enterVariableList(RCParser.VariableListContext ctx) {
		varList = new ArrayList<Term>();
	}
	
	@Override
	public void exitVariableList(RCParser.VariableListContext ctx) {
		varListStack.push(varList);
	}

	@Override public void exitConstant(RCParser.ConstantContext ctx) {
		String value = ctx.getText();
		Term t = new Term(value, true);
		if ( ctx.getParent() instanceof RCParser.TermContext && (
				ctx.getParent().getParent() instanceof RCParser.LessThanContext ||
				ctx.getParent().getParent() instanceof RCParser.EqualityContext )) {
			termStack.push(t);
		} else {
			termList.add(t);
		}
	}
	
	@Override
	public void enterTermList(RCParser.TermListContext ctx) {
		termList = new ArrayList<Term>();
	}

	@Override
	public void exitPredicateName(RCParser.PredicateNameContext ctx) {
		predicateName = ctx.getText();
	}
	
	@Override
	public void exitPredicate(RCParser.PredicateContext ctx) {
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
		formulaStack.push(new Existential(varListStack.pop(), f));
	}

	@Override
	public void exitEquality(RCParser.EqualityContext ctx) {
		Term right = termStack.pop();
		Term left = termStack.pop();
		formulaStack.push(new Equality(left, right));
	}

	@Override
	public void exitLessThan(RCParser.LessThanContext ctx) {
		Term right = termStack.pop();
		Term left = termStack.pop();
		formulaStack.push(new LessThan(left, right));
	}
}
