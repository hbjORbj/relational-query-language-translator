package s1705270;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Schema {

	private Map<String,List<String>> map; // this maps a relation name to a list of attribute names
	
	public Schema(Map<String,List<String>> map) {
		this.map = map;
	}
	
	public Set<String> getRelations() {
		return map.keySet();
	}
	
	public List<String> getAttributes(String rel) {
		return map.get(rel);
	}
}
