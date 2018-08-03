

import java.util.*;
import org.json.simple.JSONObject;
import java.lang.*;
import java.io.*;

import javax.net.ssl.SSLEngineResult.Status;
import javax.xml.ws.Response;


// add @GET and @POST

public class Todo {
	ArrayList<Todolist> masterList;
	
	//PRE: Takes a todo list
	//POST: If possible, adds the todo list to the main list and returns the corisponding http status.
	@POST
	public Response post(Todolist todo) {
		if(todo == null) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		if(masterList.contains(todo)){
			return Response.status(Status.CONFLICTED).build();
		}
		masterList.add(todo);
		return Response.status(Status.OK).build();
	}
	
	//PRE: Takes an UUID for a list and a task object
	//POST: If possible, adds task to the corisponding list and returns the proper http status.
	@POST
	public Response post(String id, Task task) {
		Todolist todo = this.getTodo(id);
		if(todo == null){
			return Response.status(Status.BAD_REQUEST).build();
			// 400
		}
		int num = todo.tasks.length;
		if(num ==1 && todo.tasks[0]==null){//deals with first task added
			todo.tasks[0] = task;
			return Response.status(Status.OK).build();
		}
		Task[] temp2 = new Task[num+1];
		for(int i =0;i<num;i++) {
			if(todo.tasks[i].id == task.id){
				return Response.status(Status.CONFLICTED).build();
			}
			temp2[i]=todo.tasks[i];
		}
		temp2[num]= task;
		todo.tasks =temp2;
		return Response.status(Status.OK).build();
	}
	
	//PRE: Takes a UUID for a list and for a task
	//POST: Records the corisponding task is completed and returns the proper http
	@POST
	public Response post(String id, String taskId) {
		Todolist todo = this.getTodo(id);
		if(todo == null) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		for(Task t:todo.tasks){
			if(t.id==taskId){
				t.completed =true;
				return Response.status(Status.CREATED).build();
			}
		}
		return Response.status(Status.BAD_REQUEST).build();
	}
	
	//PRE: Takes an intial number, skip, and a total number,num
	//POST: Returns an array at most num containing the lists with indexes num+1 to skip+num
	@GET
	public Response get(int skip, int num) {
		if(skip <0||num<0||masterList.size()<skip){
			return Response.status(Status.BAD_REQUEST).build();
		}
		Todolist[] temp = new Todolist[num];
		int count = 0;
		for(int i= skip+1; i<=skip+num && i<masterList.size();i++){
			temp[count] = masterList.get(i);
		}
		return Response.status(Status.OK).entity(todo).build();
	}
	
	//PRE:Takes a string, an intial number, skip, and a total number,num
	//POST:Returns an array at most num containing the lists with indexes num+1 to skip+num and
	// contains the string in their name and the http status.
	@GET
	public Response get(String searchString, int skip, int num){
		if(skip <0||num<0||masterList.size()<skip||searchString.isEmpty()){
			return Response.status(Status.BAD_REQUEST).build();
		}
		Todolist[] temp = new Todolist[num];
		int count1 =0;
		int count2 =skip+1;
		while(count1<num && count2<masterList.size()){
			Todolist store = masterList.get(count2);
			if(store.name.toLowerCase().contains(searchString)){
				temp[count1]=store;
				count1++;
			}
			count2++;
		}
		return Response.status(Status.OK).entity(todo).build();
	}
	
	//PRE: Takes a UUID for a list
	//POST: Returns the todo list that matches the UUID and the proper http status
	@GET
	public Response get(String id) {
		Todolist todo = this.getTodo(id);
		if(todo == null) {
			return Response.Status(Status.NOT_FOUND).build();
		}
		return Response.status(Status.OK).entity(todo).build();
	}
	
	//PRE: Takes an id
	//POST: Returns matching todo list or null;
	private Todolist getTodo(String id){
		try{
		    UUID uuid = UUID.fromString(id);
		} catch (IllegalArgumentException exception){
		    return null;
		}
		Todolist todo =null;
		for(int i = 0; i < masterList.size();i++){
			Todolist temp = masterList.get(i);
			if(temp.id == id){
				todo = temp;
				break;
			}
		}
		return todo;
	}
	

}
