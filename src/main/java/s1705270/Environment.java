package s1705270;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Environment {
	private Map<String,String> map; // this maps a RC variable name to a RA attribute name
	
	public Environment(Map<String,String> map) {
		this.map = map;
	}
	
	public Set<String> getVariables() {
		return map.keySet();
	}
	
	public Set<String> getAttributes() {
		Set<String> attrs = new HashSet<>();
		for (String attr : map.values()) {
			attrs.add(attr);
		}
		return attrs;
	}
}
