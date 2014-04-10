package todomato;

public class SyncProcessor extends Processor {

	public static String processSync() {
		if ((list.getUserName() == null && list.getPassword() == null) ||
		(list.getUserName().equals("null") && list.getPassword().equals("null"))){
			new UsernamePasswordDialogueBox();
			fileHandler.updateFile(list);

		}
		
		String username = list.getUserName();
		String password = list.getPassword();
		
		DataSyncer Syncer = new DataSyncer(list);
		TaskList newList = Syncer.sync(username, password, list.getLastSyncTime());
		list = newList;
		fileHandler.updateFile(list);
		displayList.deepCopy(list);
		
		return "sync completed";

	}
}