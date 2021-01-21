package s1705270;

import java.io.File;
import java.util.HashMap;
import java.util.Properties;

import org.junit.jupiter.api.Test;

import uk.ac.ed.pguaglia.real.lang.Expression;

public class TranslationTest {

	HashMap<Formula,Expression> translationPairsRC = new HashMap<>();
	HashMap<Expression,Formula> translationPairsRA = new HashMap<>();
	
	// take care of schemas

	public TranslationTest(File f) { // maybe another file needed for RA/RC pairs
		// read file f with pairs (stringRC_i, stringRA_i) of RC/RA strings
		// for each pair (stringRC_i, stringRA_i):
		//   parse RC string stringRC_i to get Formula formula_i
		//   parse RA string stringRA_i to get Expression expr_i
		//   put (formula_i,expr_i) into translationPairs map
		
		// similarly for RA->RC
	}
	
	@Test
	public void RCTranslationTest() {
		// for each pair (formula_i,expr_i):
		//   translate formula_i to trans_expr_i
		//   compare trans_expr_i.toString() with expr_i.toString()
	}
	
	@Test
	public void RATranslationTest() {
		// similar to RCTranslationTest
	}
}
