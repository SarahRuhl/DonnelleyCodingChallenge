
public class Task {
	public String id;
	public String name;
	public boolean completed;
	
	public Task(String id, String name){
		this.id = id;
		this.name = name;
		this.completed = false;
	}
	
	public void completed(){
		this.completed = true;
	}
	
	public String getTask(){
		return this.name;
	}
	
	public String getID(){
		return this.id;
	}
}
