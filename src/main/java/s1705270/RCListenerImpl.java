package s1705270;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import s1705270.RCBaseListener;
import s1705270.RCParser;

public class RCListenerImpl extends RCBaseListener {

	private Stack<Formula> formulaStack = new Stack<Formula>();
	private List<Term> termList = null;
	private Stack<Term> termStack = new Stack<Term>();
	private List<Term> varList = null;
	private List<Term> freeVarList = new ArrayList<Term>(); // tracks free variables of the entire formula
	private Set<Term> boundVarList = new HashSet<Term>(); // tracks bound variables of the entire formula
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
	public void enterTermList(RCParser.TermListContext ctx) {
		termList = new ArrayList<>();
	}

	@Override
	public void exitVariable(RCParser.VariableContext ctx) {
		String value = ctx.getText().substring(1); // get rid of initial '?'
		Term t = new Term(value, false);
		if ( ctx.getParent() instanceof RCParser.TermListContext ) {
			if (termList.contains(t)) { // No variable name is repeated within a predicate
				throw new RuntimeException("You can't repeat a variable within a predicate.");
			} else {
				termList.add(t);
			}
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
		if ( ctx.getParent() instanceof RCParser.TermListContext ) { // No constants in predicates
			throw new RuntimeException("You can't add constants in predicates.");
		} else {
			termStack.push(t);
		}
	}

	@Override
	public void exitPredicate(RCParser.PredicateContext ctx) {
		String name = ctx.getText();
		for (Term var : termList) {
			if (boundVarList.contains(var)) { // this variable is already bound by some quantifier somewhere else in the formula
				throw new RuntimeException("No variable name can occur both free and bound.");
			} else {
				freeVarList.add(var);
			}
		}
		formulaStack.push(new Predicate(name, termList));
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
		throw new RuntimeException("Implication cannot be used.");
		// Formula right = formulaStack.pop();
		// Formula left = formulaStack.pop();
		// formulaStack.push(new Implication(left, right));
	}

	@Override
	public void exitNegation(RCParser.NegationContext ctx) {
		Formula f = formulaStack.pop();
		if (f.toString().charAt(0) == '~') throw new RuntimeException("Double negation cannot be used.");
		else formulaStack.push(new Negation(f));
	}

	@Override
	public void exitUniversalQuantifier(RCParser.UniversalQuantifierContext ctx) {
		throw new RuntimeException("Universal Quantifier cannot be used.");
		// Formula formula = formulaStack.pop();
		// formulaStack.push(new Universal(varList, formula));
	}

	@Override
	public void exitExistentialQuantifier(RCParser.ExistentialQuantifierContext ctx) {
		Formula f = formulaStack.pop();
		Set<Term> free = f.free();
		for (Term var : varList) {
			if (free.contains(var) == false) { // Check whether each term is free in given formula
				throw new RuntimeException("One or more variables are not free in given formula.");
			}
			freeVarList.remove(var); // this variable is no more free
			boundVarList.add(var); // this variable is now bound
			if (freeVarList.contains(var)) { // this bound variable is free somewhere else in the formula, so we throw an error
				throw new RuntimeException("No variable name can occur both free and bound.");
			}
		}
		formulaStack.push(new Existential(varList, f));
	}

	@Override
	public void exitEquality(RCParser.EqualityContext ctx) { // No atoms of the form: x op x OR c1 op c2
		Term right = termStack.pop();
		Term left = termStack.pop();
		if (left.isVariable() && right.isVariable() && left == right) {
			throw new RuntimeException("You can't use the same variable for comparison.");
		}
		if (left.isConstant() && right.isConstant()) {
			throw new RuntimeException("You can't compare constants.");
		}
		formulaStack.push(new Equality(left, right));
	}

	@Override
	public void exitLessThan(RCParser.LessThanContext ctx) { // No atoms of the form: x op x OR c1 op c2
		Term right = termStack.pop();
		Term left = termStack.pop();
		if (left.isVariable() && right.isVariable() && left == right) {
			throw new RuntimeException("You can't use the same variable for comparison.");
		}
		if (left.isConstant() && right.isConstant()) {
			throw new RuntimeException("You can't compare constants.");
		}
		formulaStack.push(new LessThan(left, right));
	}
}
