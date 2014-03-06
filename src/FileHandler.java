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
	
	private static final String LINE_BREAK = "\r\n";
	private String fileLocation;
	private File file;
	
	public FileHandler (String fileLoc) {
		this.fileLocation = fileLoc;
		this.file = new File (fileLocation);
	}

	/**
	 * @return a task list generated by strings in the data file
	 * @throws IOException
	 */
	public TaskList readFile() {
		
		try {
		
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
			
		} catch (IOException e) {
			//TODO
			return null;
			
		}
			
			
	}
	
	/**
	 * @param taskList the updated task list stored in runtime
	 * @return the updated task list generated by strings in the date file
	 * @throws IOException
	 */
	public TaskList updateFile(TaskList taskList) {
		
		try {
		
			String content;
			
			FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
	
			if (!file.exists()) {
				file.createNewFile();
			}
			
			for (Task task : taskList.getList()) {
				content = task.toString() + LINE_BREAK;
				bufferedWriter.write(content);
			}
	
			bufferedWriter.close();
			
			TaskList updatedList = readFile();
			
			return updatedList;
		
		} catch (IOException e) {
			
			//TODO
			return null;
			
		}
	}
	
	/**
	 * @param line each line in data file that represents a task
	 * @return task generated by the input line
	 */
	private Task readTask(String line) {
		Task task = Task.createTaskFromString(line);
		return task;
	}
	
	
}
