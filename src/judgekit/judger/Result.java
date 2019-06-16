package judgekit.judger;

import java.util.HashMap;

public class Result implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private HashMap<Contestant,HashMap<Problem,String>> compileInfo=new HashMap<Contestant,HashMap<Problem,String>>();
	private HashMap<Contestant,HashMap<Problem,String>> compileStates=new HashMap<Contestant,HashMap<Problem,String>>();
	private HashMap<Contestant,HashMap<Problem,String>> finalStates=new HashMap<Contestant,HashMap<Problem,String>>();
	private HashMap<Contestant,HashMap<Problem,String>> description=new HashMap<Contestant,HashMap<Problem,String>>();
	
	void setCompileState(Contestant cont,Problem prob,String State) {
		if(!compileStates.containsKey(cont)) {
			compileStates.put(cont, new HashMap<Problem,String>());
		}
		compileStates.get(cont).put(prob, State);
	}
	void setCompileInfo(Contestant cont,Problem prob,String Info) {
		if(!compileInfo.containsKey(cont)) {
			compileInfo.put(cont, new HashMap<Problem,String>());
		}
		compileInfo.get(cont).put(prob, Info);
	}
	void setFinalState(Contestant cont,Problem prob,String State) {
		if(!finalStates.containsKey(cont)) {
			finalStates.put(cont, new HashMap<Problem,String>());
		}
		finalStates.get(cont).put(prob, State);
	}
	void setDescription(Contestant cont,Problem prob,String Description) {
		if(!description.containsKey(cont)) {
			description.put(cont, new HashMap<Problem,String>());
		}
		description.get(cont).put(prob, Description);
	}
	
	public String getCompileState(Contestant cont,Problem prob) {
		if(!compileStates.containsKey(cont))return null;
		return compileStates.get(cont).get(prob);
	}
	public String getCompileInfo(Contestant cont,Problem prob) {
		if(!compileInfo.containsKey(cont))return null;
		return compileInfo.get(cont).get(prob);
	}
	public String getFinalState(Contestant cont,Problem prob) {
		if(!finalStates.containsKey(cont))return null;
		return finalStates.get(cont).get(prob);
	}
	public String getDescription(Contestant cont,Problem prob) {
		if(!description.containsKey(cont))return null;
		return description.get(cont).get(prob);
	}
}
