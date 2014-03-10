package todomato;

public class DeleteProcessor {
	private static final String ARGUMENT_CLEAR_ALL = "all";
	
	/**
	 * @author linxuan
	 * @param argument
	 * @return TaskList containing deleted tasks
	 */
	public static TaskList processDelete(String argument) {
		storeCurrentList();
		
		String[] indices = argument.split(",");
		if(indices.length > 1) {
			return deleteMultiple(indices);
		} else {
			if(indices[0].equals(ARGUMENT_CLEAR_ALL)) {
				return deleteAll();
			} else {
				return deleteSingleTask(indices[0]);
			} 
		}
	}
	
	private static TaskList deleteAll() {
		TaskList deletedTasks = new TaskList();
		while(list.getSize() != 0) {
			deletedTasks.addToList(list.getListItem(0));
			list.deleteListItem(0);
		}
		fileHandler.updateFile(list);
		return deletedTasks;
	}
	
	private static TaskList deleteSingleTask(String indexString) {
		try {
			TaskList deletedTasks = new TaskList();
			int index = Integer.parseInt(indexString) - 1;
			deletedTasks.addToList(list.getListItem(index));
			list.deleteListItem(index);
			fileHandler.updateFile(list);
			return deletedTasks;
		} catch(NumberFormatException e) {
			// TODO
			return null;
		} catch(IndexOutOfBoundsException e) {
			// TODO
			return null;
		}
	}
	
	private static TaskList deleteMultiple(String[] indices) {
		try {
			TaskList deletedTasks = new TaskList();
			for (int i = 0; i < indices.length; i++) {
				int index = Integer.parseInt(indices[i]) - i - 1;
				deletedTasks.addToList(list.getListItem(index));
				list.deleteListItem(index);
			}
			fileHandler.updateFile(list);
			return deletedTasks;
		} catch (NumberFormatException e) {
			// TODO
			return null;
		} catch (IndexOutOfBoundsException e) {
			// TODO
			return null;
		}
	}

}
