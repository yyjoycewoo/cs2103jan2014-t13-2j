package todomato;

public class SyncProcessor extends Processor {

	public static String processSync() {
		if (list.getUserName() == null && list.getPassword() == null ||
		(list.getUserName().equals("null") && list.getPassword().equals("null"))){
			new UsernamePasswordDialogueBox();
			fileHandler.updateFile(list);
		}
		
		return "syncing...";
	}
}