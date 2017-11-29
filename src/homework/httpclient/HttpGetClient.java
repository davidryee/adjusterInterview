package homework.httpclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSetMetaData;
import javax.json.*;
public class HttpGetClient 
{
    // jdbc Connection
    private static Connection conn = null;
    private static Statement stmt = null;
    private static String dbURL = "jdbc:derby://localhost:1527/AdJusterDerby;create=true;user=davidryee;password=abc123";
    private static String CAMPAIGN_TABLE_NAME = "CAMPAIGN";
	public static void main(String[] args) throws ClientProtocolException, IOException
	{
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet("http://homework.ad-juster.com/api/campaign");
		HttpResponse response = client.execute(request);
		
		JsonReader jsonReader = Json.createReader((new InputStreamReader(
		    response.getEntity().getContent())));
		
		JsonArray jsonarray = jsonReader.readArray();
		try
        {
            Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
            //Get a connection
            //conn = DriverManager.getConnection("jdbc:derby://localhost:1527/AdJusterDerby", "davidryee", "abc123"); 
            conn = DriverManager.getConnection("jdbc:derby:C:\\Users\\David\\AdjusterDerby", "davidryee", "abc123");
            for (int i = 0; i < jsonarray.size(); i++) 
            {
			    JsonObject jsonobject = jsonarray.getJsonObject(i);
			    String name = jsonobject.getString("name");
			    String startDate = jsonobject.getString("startDate");	
			    String cpm = jsonobject.getString("cpm");
			    int id = jsonobject.getInt("id");		 
			    insertIntoCampaignTable(name, startDate, cpm, id);
	        }
        }
        catch (Exception except)
        {
        	except.printStackTrace();
        }		
    }
	
	private static void insertIntoCampaignTable(String name, String startDate, String cpm, int id)
	{
		try 
		{
			stmt = conn.createStatement();
			//stmt.execute("insert into " + CAMPAIGN_TABLE_NAME + " values ('" +
               //     cpm + "','" + startDate + "','" + name + "'," + id + ")");
			String insertCampaign = "INSERT INTO " + CAMPAIGN_TABLE_NAME
			        + " (NAME, START_DATE_NEW, CPM_NEW, ID)"
			        + " VALUES (" + "'" + name + "'," + "'" + startDate + "'," + "'" + cpm + "'," + id+ ")";
		
			stmt.executeUpdate(insertCampaign);
            stmt.close();
		} 
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
