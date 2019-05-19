package judgekit.judger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Contest implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	private String path,name;
	private Map<String,Problem> problems = new HashMap<String,Problem>();
	private Map<String,Contestant> contestants = new HashMap<String,Contestant>();
	private Map<String,String> compileCommand = new HashMap<String,String>();
	
	public Contest(String nName,String nPath){
		this.path=nPath;
		this.name=nName;
	}
	public Contest(String nPath){
		this.path=nPath;
		this.name=null;
	}
	public Contest() {
		this.path=this.name=null;
	}
	
	public String getPath() {
		return path;
	}
	public String getName() {
		return name;
	}
	public Problem getProblem(String key) {
		return problems.get(key);
	}
	public ProblemList getProblemList() {
		ProblemList list = new ProblemList();
		Iterator<Map.Entry<String,Problem>> entry = problems.entrySet().iterator();
		while(entry.hasNext()) {
			list.add(entry.next().getValue());
		}
		return list;
	}
	public ProblemList getProblemList(String[] keys) {
		ProblemList list = new ProblemList();
		for(int i=0;i<keys.length;i++) {
			if(problems.containsKey(keys[i])) {
				list.add(problems.get(keys[i]));
			}
		}
		return list;
	}
	public ContestantList getContestantList() {
		ContestantList list = new ContestantList();
		Iterator<Map.Entry<String,Contestant>> entry = contestants.entrySet().iterator();
		while(entry.hasNext()) {
			list.add(entry.next().getValue());
		}
		return list;
	}
	public ContestantList getContestantList(String[] keys) {
		ContestantList list = new ContestantList();
		for(int i=0;i<keys.length;i++) {
			if(contestants.containsKey(keys[i])) {
				list.add(contestants.get(keys[i]));
			}
		}
		return list;
	}
	
	public void setPath(String nPath) {
		this.path=nPath;
	}
	public void setName(String nName) {
		this.name=nName;
	}
	
	public void addProblem(Problem prob) {
		problems.put(prob.getName(),prob);
	}
	public void addContestant(Contestant cont) {
		contestants.put(cont.getName(),cont);
	}
	public void addCompileCommand(String suffix,String command) {
		compileCommand.put(suffix, command);
	}
	
	public void removeProblem(String key) {
		if(problems.containsKey(key))problems.remove(key);
	}
	public void removeProblem(Problem prob) {
		if(problems.containsKey(prob.getName()))problems.remove(prob.getName());
	}
	public void removeContestant(String key) {
		if(contestants.containsKey(key))contestants.remove(key);
	}
	public void removeContestant(Contestant cont) {
		if(contestants.containsKey(cont.getName()))contestants.remove(cont.getName());
	}
	public void removeCompileCommand(String suffix) {
		if(compileCommand.containsKey(suffix))compileCommand.remove(suffix);
	}
	
	
	
	public void runJudge(ProblemList prob,ContestantList cont) {
		this.runJudge(prob, cont,true);
	}
	public void runJudge(ProblemList prob,ContestantList cont,boolean multiThread) {
		if(multiThread) {
			
		}else {
			Iterator<Contestant> iteContestant=cont.getList().iterator();
			while(iteContestant.hasNext()) {
				Contestant contestant=iteContestant.next();
				Iterator<Problem> iteProblem=prob.getList().iterator();
				while(iteProblem.hasNext()) {
					Problem problem=iteProblem.next();
					compile(contestant,problem);
				}
			}
		}
	}
	
	private void compile(Contestant cont,Problem prob) {
		
	}
	
	
	public static Contest init(String contestName,String contestPath) {
		try {
			ObjectInputStream inStream = new ObjectInputStream(new FileInputStream(contestPath+contestName+".Contest.jk"));
			Contest cont = (Contest)inStream.readObject();
			inStream.close();
			return cont;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public static void save(Contest obj) {
		try {
			File out=new File(obj.getPath());
			if(!out.exists()) {
				out.mkdirs();
			}
			ObjectOutputStream saveStream = new ObjectOutputStream(new FileOutputStream(obj.getPath()+obj.getName()+".Contest.jk",false));
			saveStream.writeObject(obj);
			saveStream.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}

