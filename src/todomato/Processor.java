package todomato;

import java.util.Stack;

public class Processor {
	protected static String fileLoc = "C:\\Users\\Joyce\\Documents\\Year 2\\test.txt";
	//protected static String fileLoc = "D:\\test.txt";
	protected static FileHandler fileHandler = new FileHandler(fileLoc);
	protected static TaskList list = fileHandler.readFile();
	protected static Stack<TaskList> oldLists = new Stack<TaskList>();
	
	protected static void storeCurrentList() {
		//store the current list before modifications for possible undo
		TaskList lastList = new TaskList();
		lastList.deepCopy(list);
		oldLists.push(lastList);
	}
	
	public static TaskList getList() {
		return list;
	}

}
