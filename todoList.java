import java.util.*;

public class todoList {
	public String id;
	public String name;
	public String description;
	public Task[] tasks;
	
	public todoList(String id, String name, String description){
		this.id = id;
		this.name = name;
		this.description = description;
		this.tasks = new Task[1];
	}
}
