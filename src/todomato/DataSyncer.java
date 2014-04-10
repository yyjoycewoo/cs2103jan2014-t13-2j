package todomato;
import hirondelle.date4j.DateTime;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.TimeZone;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

/**
 * This class is the process for the Google Sync of the application.
 * @author yiwen
 *
 */

public class DataSyncer extends Processor {
	
	TaskList localList;
	static String SERVER_URL = "http://todomato-sync.herokuapp.com/todomato/api/v1.0/update";
	
	/**
	 * @param fileLoc
	 */
	public DataSyncer (TaskList localList) {
		this.localList = localList;
	}
			
	public TaskList sync(String username, String password, DateTime lastsync) {
		JsonObject localJson = prepareData(this.localList, username, password, lastsync);
		list.setLastSyncTime(DateTime.now(TimeZone.getDefault()));
		JsonObject responseJson = sendRequest(localJson);
		localList = processResponse(responseJson);
		return localList;
	}
	
	private JsonObject sendRequest(JsonObject localJson) {
		
		HttpClient client = HttpClientBuilder.create().build();
		String postUrl = SERVER_URL;
		HttpPost post = new HttpPost(postUrl);
		StringEntity postingString;
		
		try {
			postingString = new StringEntity(localJson.toString());
			post.setEntity(postingString);
			post.setHeader("Content-type", "application/json");
			
			try {
				HttpResponse response = client.execute(post);
				String json = EntityUtils.toString(response.getEntity());
				JsonParser jsonParser = new JsonParser();
				JsonObject tJson = (JsonObject)jsonParser.parse(json);
				
				return tJson;
				
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return localJson;
		
		
	}

	private TaskList processResponse(JsonObject responseJson) {
		TaskList output = new TaskList();
		JsonArray tasklist = responseJson.getAsJsonArray("tasklist");
		for(JsonElement t: tasklist) {
			JsonObject tJson = t.getAsJsonObject();
			String eventId = tJson.get("eid").getAsString();
			String description = tJson.get("description").getAsString();
			Integer id = Integer.parseInt(tJson.get("description").getAsString());
			String location = tJson.get("location").getAsString();
			ArrayList<DateTime> startDTList = stringToDateAndTime(tJson.get("starttime").getAsString());
			ArrayList<DateTime> endDTList = stringToDateAndTime(tJson.get("starttime").getAsString());
			
			DateTime startDate = startDTList.get(0);
			DateTime startTime = startDTList.get(1);
			DateTime endDate = endDTList.get(0);
			DateTime endTime = endDTList.get(1);
			
			DateTime updateTime = stringToDateTime(tJson.get("edit").getAsString());
			DateTime timeCreated = stringToDateTime(tJson.get("created").getAsString());
			Task task = new Task(description);
			task.setStartTime(startTime);
			task.setEndTime(endTime);
			task.setStartDate(startDate);
			task.setEndDate(endDate);		
			task.setLocation(location);
			task.setId(id);
			task.setTimeCreated(timeCreated);
			task.setEventId(eventId);
			task.setUpdateTime(updateTime);
			// task.setPriorityLevel(prioritylevel);
			// task.setCompleted(isCompleted);
			// task.setNoticeTime(noticeTime);
			output.addToList(task);
		}
		
		return output;
	}

	private String formatTime(DateTime d, DateTime t) {
		if (d == null || t == null) {
			return null;
		} else {
			return d.format("YYYY-MM-DD") + "T" + t.format("hh:mm:ss") + ".000+08:00";
		}
	}
	
	private String formatTime(DateTime dt) {
		if (dt == null) {
			return null;
		} else {
			return dt.format("YYYY-MM-DD") + "T" + dt.format("hh:mm:ss") + ".000+08:00";
		}
	}
	
	private DateTime stringToDateTime(String s) {
		DateTime dt = new DateTime(s.split("+")[0]);
		return dt;
	}
	
	private ArrayList<DateTime> stringToDateAndTime(String s) {
		ArrayList<DateTime> datetimeList = new ArrayList<DateTime>();
		DateTime dt = new DateTime(s.split("+")[0]);
		DateTime d = DateTime.forDateOnly(dt.getYear(), dt.getMonth(), dt.getDay()); 
		DateTime t = DateTime.forTimeOnly(dt.getHour(), dt.getMinute(), dt.getSecond(), dt.getNanoseconds());
		datetimeList.add(d);
		datetimeList.add(t);
		return datetimeList;
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
			String starttime = formatTime(t.getStartDate(), t.getStartTime());
			String endtime = formatTime(t.getEndDate(), t.getEndTime());
			String created = formatTime(t.getTimeCreated());
			String edit = formatTime(t.getUpdateTime());
			String location = t.getLocation();
			String eid = t.getEventId();
			
			if (id != null){
				tJson.addProperty("id", id);
			}
			
			if (description != null){
				tJson.addProperty("description", description);
			}
			
			if (starttime != null){
				tJson.addProperty("starttime", starttime);
			}
			
			if (endtime != null){
				tJson.addProperty("endtime", endtime);
			}
			
			if (created != null){
				tJson.addProperty("created", created);
			}
			
			if (location != null){
				tJson.addProperty("location", location);
			}
			
			if (eid != null){
				tJson.addProperty("eid", eid);
			}
			
			if (edit != null){
				tJson.addProperty("edit", edit);
			}
			
			tasklist.add(tJson);
		}
		
		data.add("tasklist", tasklist);
		localJson.add("data", data);
		
		return localJson;
	}
}
