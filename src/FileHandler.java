import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Class to read/write/update local data file
 * @author Yiwen
 *
 */

public class FileHandler {
	
	private String fileLocation;
	private File file;
	
	public FileHandler (String fileLoc) {
		this.fileLocation = fileLoc;
		this.file = new File (fileLocation);
	}
	
	public TaskList readFile() throws IOException { 
		
		String currentLine;
		Task currentTask;
		TaskList taskList = new TaskList();

		BufferedReader bufferedReader = new BufferedReader(new FileReader(file.getAbsoluteFile()));

		while ((currentLine = bufferedReader.readLine()) != null) {
			currentTask = readTask(currentLine);
			taskList.addToList(currentTask);
		}
		
		bufferedReader.close();
		
		return taskList;
	}
	
	public TaskList updateFile(TaskList taskList) throws IOException {
		
		String content;
		
		FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

		if (!file.exists()) {
			file.createNewFile();
		}
		
		for (Task task : taskList.getList()){
			content = task.toString() + "/n";
			bufferedWriter.write(content);
		}

		bufferedWriter.close();
		
		TaskList updatedList = readFile();
		
		return updatedList;
	}
	
	private Task readTask(String line) {
		return null;
	}
	
	
}
