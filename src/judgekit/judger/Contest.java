package judgekit.judger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Contest implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	private String path,name;
	private Map<String,Problem> problems = new HashMap<String,Problem>();
	private Map<String,Contestant> contestants = new HashMap<String,Contestant>();
	private Map<String,String> compileCommand = new HashMap<String,String>();
	private Result result=new Result();
	
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
	public Contestant getContestant(String key) {
		return contestants.get(key);
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
	public Result getResult() {
		return result;
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
	
	
	
	public void runJudge(ProblemList prob,ContestantList cont) throws InterruptedException {
		this.runJudge(prob, cont,true);
	}
	public void runJudge(ProblemList prob,ContestantList cont,boolean multiThread) throws InterruptedException {
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
	
	private void compile(Contestant cont,Problem prob) throws InterruptedException {
		String[] files=new File(this.path+"src/"+cont.getPath()+prob.getPath()).list();
		for(int i=0;i<files.length;i++) {
			int index=files[i].lastIndexOf('.');
			String fileName=files[i].substring(0, index);
			String suffix=files[i].substring(index+1, files[i].length());
			if(fileName.equals(prob.getFileName())) {
				try {
					List<String> command=getCompileCommand(suffix,cont,prob);
					if(command==null)continue;
					ProcessBuilder compile=new ProcessBuilder(command);
					compile.redirectErrorStream(true);
					Process compileProcess=compile.start();
					BufferedReader reader=new BufferedReader(new InputStreamReader(compileProcess.getInputStream()));
					
					compileProcess.waitFor();
					
					if(compileProcess.exitValue()!=0) {
						result.setCompileState(cont, prob, "Compile Error");
					}
					
					String line,compileInfo=new String();
					while((line=reader.readLine())!=null) {
						compileInfo+=line+'\n';
					}
					result.setCompileInfo(cont, prob, compileInfo);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private List<String> getCompileCommand(String suffix,Contestant cont,Problem prob) {
		if(!compileCommand.containsKey(suffix))return null;
		List<String> list=new ArrayList<String>();
		String[] command=compileCommand.get(suffix).split(" ");
		String src=this.path+"src/"+cont.getPath()+prob.getPath()+prob.getFileName()+"."+suffix,dest=this.path+"tmp/"+cont.hashCode()+"/"+prob.hashCode()+"/"+prob.getFileName()+".exe";
		for(int i=0;i<command.length;i++) {
			if(command[i].equals("<src>"))command[i]=src;
			if(command[i].equals("<dest>"))command[i]=dest;
			list.add(command[i]);
		}
		return list;
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

