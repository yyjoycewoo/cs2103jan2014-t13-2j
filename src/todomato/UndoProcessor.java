//@author A0120766H
package todomato;

/** 
 * This class contains methods to process undo commands by the user.
 * It updates the user's lists of tasks, and saves it to disk.
 *
 */
public class UndoProcessor extends Processor {
	private static final String NO_CHANGES_TO_UNDO_MSG = "No changes to undo";
	private static final String SUCCESS_MSG = "Last action undone";
	
	/**
	 * Undo last action, if possible
	 * @return Status message telling user if there were changes to undo
	 */
	public static String processUndo() {		
		if (!undoList.isEmpty()) {	
			//add current list to redoList
			redoList.push(list);
			
			//get latest list from undoList
			list = undoList.pop();	
			fileHandler.updateFile(list);
			
			displayList = list;
			return SUCCESS_MSG;
		} else {
			return NO_CHANGES_TO_UNDO_MSG;
		}
	}

}