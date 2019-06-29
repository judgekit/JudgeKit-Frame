package judgekit.judger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
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
	private Map<String,String> compileCommand = new HashMap<String,String>(),execFileSuffix=new HashMap<String,String>();
	private Map<String,String> execCommand = new HashMap<String,String>();
	String judgerPath=new String();
	private Result result=new Result();
	public Callback realtimeCallback;
	
	public Contest(String nName,String nPath){
		this.path=nPath;
		this.name=nName;
		this.realtimeCallback=(prob,cont,testdata,state,score)->{};
	}
	public Contest(String nPath){
		this.path=nPath;
		this.name=null;
		this.realtimeCallback=(prob,cont,testdata,state,score)->{};
	}
	public Contest() {
		this.path=this.name=null;
		this.realtimeCallback=(prob,cont,testdata,state,score)->{};
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
	public String getJudgerPath() {
		return judgerPath;
	}
	
	public void setPath(String nPath) {
		this.path=nPath;
	}
	public void setName(String nName) {
		this.name=nName;
	}
	public void setJudgerPath(String path) {
		this.judgerPath=path;
	}
	
	public void addProblem(Problem prob) {
		problems.put(prob.getName(),prob);
	}
	public void addContestant(Contestant cont) {
		contestants.put(cont.getName(),cont);
	}
	public void addCompileCommand(String suffix,String command,String execSuffix) {
		compileCommand.put(suffix, command);
		execFileSuffix.put(suffix, execSuffix);
	}
	public void addExecCommand(String suffix,String command) {
		execCommand.put(suffix, command);
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
	public void removeExecCommand(String suffix) {
		if(execCommand.containsKey(suffix))execCommand.remove(suffix);
	}
	
	
	
	private void copyFile(String from,String dest) {
		try {
			Files.copy((new File(from)).toPath(), (new File(dest)).toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void removeFile(String dest) {
		File file=new File(dest);
		if(file.exists()) {
			if(file.isFile()) {
				file.delete();
			}else if(file.isDirectory()) {
				File[] files=file.listFiles();
				for(int i=0;i<files.length;i++) {
					removeFile(files[i].getPath());
				}
				file.delete();
			}
		}
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
					String[] exec=compile(contestant,problem);
					if(exec!=null) {
						for(Testdata testdata:problem.getAllTestData().values()) {
							judge(exec[0],exec[1],contestant,problem,testdata);
							removeFile(path+"tmp"+File.separator+contestant.hashCode()+File.separator+problem.hashCode()+File.separator+testdata.hashCode());
						}
					}else {
						for(Testdata testdata:problem.getAllTestData().values()) {
							realtimeCallback.callback(problem, contestant, testdata, "compilation error", 0.0);
						}
					}
				}
				removeFile(path+"tmp"+File.separator+contestant.hashCode());
			}
		}
	}
	
	private String[] compile(Contestant cont,Problem prob) {
		String[] files=new File(this.path+File.separator+cont.getPath()+prob.getPath()).list();
		for(int i=0;i<files.length;i++) {
			int index=files[i].lastIndexOf('.');
			String fileName=files[i].substring(0, index);
			String suffix=files[i].substring(index+1, files[i].length());
			if(fileName.equals(prob.getFileName())) {
				try {
					String destDir=this.path+"tmp"+File.separator+cont.hashCode()+File.separator+prob.hashCode()+File.separator;
					(new File(destDir)).mkdirs();
					List<String> command=getCompileCommand(suffix,cont,prob);
					if(command==null)continue;
					ProcessBuilder compile=new ProcessBuilder(command);
					compile.redirectErrorStream(true);
					Process compileProcess=compile.start();
					BufferedReader reader=new BufferedReader(new InputStreamReader(compileProcess.getInputStream()));
					
					compileProcess.waitFor();
					
					if(compileProcess.exitValue()!=0) {
						result.setCompileState(cont, prob, "compile error");
					}
					
					String line,compileInfo=new String();
					while((line=reader.readLine())!=null) {
						compileInfo+=line+'\n';
					}
					result.setCompileInfo(cont, prob, compileInfo);
					
					if((new File(destDir+prob.getFileName()+"."+execFileSuffix.get(suffix))).exists()) {
						result.setCompileState(cont, prob, "ok");
						String[] ret=new String[2];
						ret[0]=suffix;
						ret[1]=prob.getFileName()+"."+execFileSuffix.get(suffix);
						return ret;
					}else {
						result.setCompileState(cont, prob, "compilation error");
						return null;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	private List<String> getCompileCommand(String suffix,Contestant cont,Problem prob) {
		if(!compileCommand.containsKey(suffix))return null;
		List<String> list=new ArrayList<String>();
		String[] command=compileCommand.get(suffix).split(" ");
		String src=this.path+File.separator+cont.getPath()+prob.getPath()+prob.getFileName()+"."+suffix,dest=this.path+"tmp"+File.separator+cont.hashCode()+File.separator+prob.hashCode()+File.separator+prob.getFileName()+"."+execFileSuffix.get(suffix);
		for(int i=0;i<command.length;i++) {
			command[i]=command[i].replace("<src>", src);
			command[i]=command[i].replace("<dest>", dest);
			list.add(command[i]);
		}
		return list;
	}
	
	private void judge(String suffix,String execFileName,Contestant cont,Problem prob,Testdata testdata) {
		try {
			String path=this.path+"tmp"+File.separator+cont.hashCode()+File.separator+prob.hashCode()+File.separator;
			(new File(path+testdata.hashCode()+File.separator)).mkdirs();
			copyFile(path+execFileName,path+testdata.hashCode()+File.separator+execFileName);
			copyFile(testdata.getStandardInputFile().getPath(),path+testdata.hashCode()+File.separator+prob.getInputFileName());
			ProcessBuilder exec=new ProcessBuilder(getRunCommand(suffix,path+testdata.hashCode()+File.separator+execFileName));
			exec.directory(new File(path+testdata.hashCode()+File.separator));
			Process execProcess = exec.start();
			
			execProcess.waitFor();
			
			if(execProcess.exitValue()!=0) {
				result.setFinalState(cont, prob, testdata, "runtime error");
				realtimeCallback.callback(prob, cont, testdata, "runtime error", 0.0);
				result.setDescription(cont, prob, testdata, "");
				return;
			}
			
			ProcessBuilder judger=new ProcessBuilder("\""+judgerPath+"\" \""+testdata.getStandardInputFile().getPath()+"\" \""+path+testdata.hashCode()+File.separator+prob.getAnsFileName()+"\" \""+testdata.getStandardOutputFile().getPath()+"\"");
			String[] state= {"ok","wrong answer","presentation error","unknown","unknown","unknown","unknown","partially correct","unknown","unknown","unknown","unknown","unknown","unknown","unknown","unknown"};
			judger.redirectErrorStream(true);
			Process judgerProcess=judger.start();
			
			judgerProcess.waitFor();
			
			if(judgerProcess.exitValue()>=0&&judgerProcess.exitValue()<state.length) {
				result.setFinalState(cont, prob, testdata, state[judgerProcess.exitValue()]);
				realtimeCallback.callback(prob, cont, testdata, state[judgerProcess.exitValue()], (judgerProcess.exitValue()==0)?1.0:0.0);
			}else{
				result.setFinalState(cont, prob, testdata, "unknown");
				realtimeCallback.callback(prob, cont, testdata, "unknown",0.0);
			}
			
			BufferedReader reader=new BufferedReader(new InputStreamReader(judgerProcess.getInputStream()));
			String line,description=new String();
			while((line=reader.readLine())!=null) {
				description+=line+"\n";
			}
			result.setDescription(cont, prob, testdata, description);
			
			removeFile(path+testdata.hashCode()+File.separator);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private List<String> getRunCommand(String suffix,String dest) {
		List<String> list=new ArrayList<String>();
		String[] command=execCommand.get(suffix).split(" ");
		for(int i=0;i<command.length;i++) {
			command[i]=command[i].replace("<dest>", "\""+dest+"\"");
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

