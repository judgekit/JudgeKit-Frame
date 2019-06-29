package judgekit.judger;

import java.util.HashMap;

public class Result implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private HashMap<Contestant,HashMap<Problem,String>> compileInfo=new HashMap<Contestant,HashMap<Problem,String>>();
	private HashMap<Contestant,HashMap<Problem,String>> compileStates=new HashMap<Contestant,HashMap<Problem,String>>();
	private HashMap<Contestant,HashMap<Problem,HashMap<Testdata,String>>> finalStates=new HashMap<Contestant,HashMap<Problem,HashMap<Testdata,String>>>();
	private HashMap<Contestant,HashMap<Problem,HashMap<Testdata,String>>> description=new HashMap<Contestant,HashMap<Problem,HashMap<Testdata,String>>>();
	
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
	void setFinalState(Contestant cont,Problem prob,Testdata data,String State) {
		if(!finalStates.containsKey(cont)) {
			finalStates.put(cont, new HashMap<Problem,HashMap<Testdata,String>>());
		}
		if(!finalStates.get(cont).containsKey(prob)) {
			finalStates.get(cont).put(prob, new HashMap<Testdata,String>());
		}
		finalStates.get(cont).get(prob).put(data, State);
	}
	void setDescription(Contestant cont,Problem prob,Testdata data,String Description) {
		if(!description.containsKey(cont)) {
			description.put(cont, new HashMap<Problem,HashMap<Testdata,String>>());
		}
		if(!description.get(cont).containsKey(prob)) {
			description.get(cont).put(prob, new HashMap<Testdata,String>());
		}
		description.get(cont).get(prob).put(data, Description);
	}
	
	public String getCompileState(Contestant cont,Problem prob) {
		if(!compileStates.containsKey(cont))return null;
		return compileStates.get(cont).get(prob);
	}
	public String getCompileInfo(Contestant cont,Problem prob) {
		if(!compileInfo.containsKey(cont))return null;
		return compileInfo.get(cont).get(prob);
	}
	public String getFinalState(Contestant cont,Problem prob,Testdata data) {
		if(!finalStates.containsKey(cont))return null;
		if(!finalStates.get(cont).containsKey(prob))return null;
		return finalStates.get(cont).get(prob).get(data);
	}
	public String getDescription(Contestant cont,Problem prob,Testdata data) {
		if(!description.containsKey(cont))return null;
		if(!description.get(cont).containsKey(prob))return null;
		return description.get(cont).get(prob).get(data);
	}
}
