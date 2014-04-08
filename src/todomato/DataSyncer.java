package todomato;
import hirondelle.date4j.DateTime;

import java.util.ArrayList;
import java.util.TimeZone;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class DataSyncer extends Processor {
	
	TaskList localList;
	
	/**
	 * @param fileLoc
	 */
	public DataSyncer (TaskList localList) {
		this.localList = localList;
	}
	
	private TaskList Sync(String username, String password, DateTime lastsync) {
		JsonObject localJson = prepareData(this.localList, username, password, lastsync);
		list.setLastSyncTime(DateTime.now(TimeZone.getDefault()));
		/*JsonObject responseJson = sendRequest(localJson);
		localList = processResponse(responseJson);
		
		return localList;*/
		return null;
	}
	
	private String formatTime(DateTime dt) {
		return dt.format("YYYY-MM-DD") + "T" + dt.format("hh:mm:ss") + ".000+08:00";
	}

	private JsonObject prepareData(TaskList localList, String username, String password, DateTime lastsync) {
		JsonObject localJson = new JsonObject();
		JsonObject auth = new JsonObject();
		JsonObject data = new JsonObject();
		auth.addProperty("username", username);
		auth.addProperty("password", password);
		auth.addProperty("last_sync", formatTime(lastsync));
		localJson.add("auth", auth);
		
		ArrayList<Task> list = localList.getList();
		JsonArray tasklist = new JsonArray();
		
		for (Task t: list) {
			JsonObject tJson = new JsonObject();
			
			String id = Integer.toString(t.getId());
			String description = t.getDescription();
			String starttime = formatTime(t.getStartTime());
			String endtime = formatTime(t.getEndTime());
			String created = formatTime(t.getTimeCreated());
			String edit = formatTime(t.getUpdateTime());
			String location = t.getLocation();
			String eid = t.getEventId();
			
			tJson.addProperty("id", id);
			tJson.addProperty("description", description);
			tJson.addProperty("starttime", starttime);
			tJson.addProperty("endtime", endtime);
			tJson.addProperty("created", created);
			tJson.addProperty("location", location);
			tJson.addProperty("eid", eid);
			tJson.addProperty("edit", edit);
			
			tasklist.add(tJson);
		}
		
		data.add("tasklist", tasklist);
		localJson.add("data", data);
		
		return localJson;
	}
}
