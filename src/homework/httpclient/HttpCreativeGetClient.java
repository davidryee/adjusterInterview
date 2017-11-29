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
public class HttpCreativeGetClient 
{
    // jdbc Connection
    private static Connection conn = null;
    private static Statement stmt = null;
    private static String dbURL = "jdbc:derby://localhost:1527/AdJusterDerby;create=true;user=davidryee;password=abc123";    
    private static String CREATIVE_TABLE_NAME = "CREATIVE";
	public static void main(String[] args) throws ClientProtocolException, IOException
	{
		HttpClient client = new DefaultHttpClient();		
		HttpGet creativeRequest = new HttpGet("http://homework.ad-juster.com/api/creative");
		HttpResponse response = client.execute(creativeRequest);
		
		JsonReader jsonReader = Json.createReader((new InputStreamReader(
		    response.getEntity().getContent())));
		
		JsonArray jsonarray = jsonReader.readArray();
		try
        {
            Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
            //Get a connection
            conn = DriverManager.getConnection("jdbc:derby:C:\\Users\\David\\AdjusterDerby", "davidryee", "abc123");
            
            for (int i = 0; i < jsonarray.size(); i++) 
            {
			    JsonObject jsonobject = jsonarray.getJsonObject(i);
			    int views = jsonobject.getInt("views");
			    int clicks = jsonobject.getInt("clicks");	
			    int parentid = jsonobject.getInt("parentId");
			    int id = jsonobject.getInt("id");		 
			    insertIntoCreativeTable(views, clicks, parentid, id);
	        }
        }
        catch (Exception except)
        {
        	except.printStackTrace();
        }		
    }
	
	private static void insertIntoCreativeTable(int views, int clicks, int parentId, int id)
	{
		try 
		{
			stmt = conn.createStatement();
			String insertCreative = "INSERT INTO " + CREATIVE_TABLE_NAME
			        + " (VIEWS, CLICKS, PARENTID, ID)"
			        + " VALUES (" + views + "," + clicks + ","  + parentId + "," + id+ ")";
		
			stmt.executeUpdate(insertCreative);
            stmt.close();
		} 
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
