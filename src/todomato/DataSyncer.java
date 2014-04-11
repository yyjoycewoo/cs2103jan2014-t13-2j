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
 * @author A0099332Y
 *
 */

public class DataSyncer extends Processor {
	
	TaskList localList;
	static String SERVER_URL = "http://todomato-sync.herokuapp.com/todomato/api/v1.0/update";
	//static String SERVER_URL = "http://127.0.0.1:5000/todomato/api/v1.0/update";

	
	public DataSyncer (TaskList localList) {
		this.localList = localList;
	}
			
	public TaskList sync(String username, String password, DateTime lastsync) {
		JsonObject localJson = prepareData(this.localList, username, password, lastsync);
		System.out.println(localJson);
		JsonObject responseJson = sendRequest(localJson);
		localList = processResponse(responseJson);
		localList.setLastSyncTime(DateTime.now(TimeZone.getDefault()));
		localList.setUserName(username);
		localList.setPassword(password);
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
				System.out.println(json);
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
			Integer id = null;
			if (!tJson.get("id").isJsonNull()){
				id = Integer.parseInt(tJson.get("id").getAsString());
			}
			
			String location = tJson.get("location").getAsString();
			ArrayList<DateTime> startDTList = stringToDateAndTime(tJson.get("starttime").getAsString());
			ArrayList<DateTime> endDTList = stringToDateAndTime(tJson.get("endtime").getAsString());
			
			DateTime startDate = startDTList.get(0);
			DateTime startTime = startDTList.get(2);
			DateTime endDate = endDTList.get(0);
			DateTime endTime = endDTList.get(2);
			
//			boolean isCompleted = tJson.get("completed").getAsBoolean();
			
			DateTime updateTime = stringToDateTime(tJson.get("edit").getAsString());
			DateTime timeCreated = stringToDateTime(tJson.get("created").getAsString());
			Task task = new Task(description);
			task.setStartTime(startTime);
			task.setEndTime(endTime);
			task.setStartDate(startDate);
			task.setEndDate(endDate);		
			task.setLocation(location);
			
			if(id != null){
				task.setId(id);
			}
			
			task.setTimeCreated(timeCreated);
//			System.out.println(isCompleted);
			task.setEventId(eventId);
			task.setUpdateTime(updateTime);
			// task.setPriorityLevel(priorityLevel);
//			task.setCompleted(isCompleted);
			// task.setNoticeTime(noticeTime);
			output.addToList(task);
		}
		
		return output;
	}

	private String formatTime(DateTime d, DateTime t) {
		if (d == null || d.toString().equals("null")) {
			return "";
		} else if (t == null || t.toString().equals("null")) {
			return d.format("YYYY-MM-DD");
		} else {
			return d.format("YYYY-MM-DD") + "T" + t.format("hh:mm:") + "00.000+08:00";
		}
	}
	
	private String formatTime(DateTime dt) {
		if (dt == null || dt.toString().equals("null")  ) {
			return "";
		} else {
			return dt.format("YYYY-MM-DD") + "T" + dt.format("hh:mm:ss") + ".000+08:00";
		}
	}
	
	private DateTime stringToDateTime(String s) {
		DateTime dt = new DateTime(s.substring(0,19));
		return dt;
	}
	
	private ArrayList<DateTime> stringToDateAndTime(String s) {
		ArrayList<DateTime> datetimeList = new ArrayList<DateTime>();
		DateTime t = null;
		DateTime d = null;
		DateTime ts = null;
		if(s.length() >= 19){
			d = new DateTime(s.substring(0,10));
			t = new DateTime(s.substring(11,19));
			ts = new DateTime(s.substring(11,16));
		} else if (s.length() >= 11 ){
			d = new DateTime(s.substring(0,10));
		}
		datetimeList.add(d);
		datetimeList.add(t);
		datetimeList.add(ts);
		return datetimeList;
	}
	
	
	private boolean notNullString(String s) {
		boolean isNullString = (s == null || s.equals("null"));
		return !isNullString;
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
			if(t.getStartDate() == null) {
				t.setStartDate(t.getEndDate());
			}
			if(t.getUpdateTime() == null) {
				t.setUpdateTime(t.getTimeCreated());
			}
			String starttime = formatTime(t.getStartDate(), t.getStartTime());
			String endtime = formatTime(t.getEndDate(), t.getEndTime());
			String created = formatTime(t.getTimeCreated());
			String edit = formatTime(t.getUpdateTime());
			String location = t.getLocation();
			String eid = t.getEventId();
			String completed  = t.getCompleted().toString();
			
			tJson.addProperty("id", id);
			System.out.println(completed);
			tJson.addProperty("completed", completed);

			if (notNullString(description)){
				tJson.addProperty("description", description);
			}
			
			if (notNullString(starttime)){
				tJson.addProperty("starttime", starttime);
			}
			
			if (notNullString(endtime)){
				tJson.addProperty("endtime", endtime);
			}
			
			if (notNullString(created)){
				tJson.addProperty("created", created);
			}
			
			if (notNullString(location)){
				tJson.addProperty("location", location);
			} else {
				tJson.addProperty("location", "");
			}
			
			if (notNullString(eid)){
				tJson.addProperty("eid", eid);
			}
			
			if (notNullString(edit)){
				tJson.addProperty("edit", edit);
			}
			
			tasklist.add(tJson);
		}
		
		data.add("tasklist", tasklist);
		localJson.add("data", data);
		
		return localJson;
	}
}
