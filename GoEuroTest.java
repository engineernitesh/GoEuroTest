import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Iterator;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.*;
import org.json.simple.parser.*;


public class GoEuroTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
			String inputString = args[0];
			String queryString = "http://api.goeuro.com/api/v2/position/suggest/en/" + inputString ;
			queryString = queryString.replaceAll(" ", "%20");
//			Default Http Client creation
			DefaultHttpClient httpClient = new DefaultHttpClient();
			
//			Initialize request object with queryString and set request Header as JSON
			HttpGet request = new HttpGet(queryString);
			request.addHeader("accept", "application/json");
			HttpResponse response = httpClient.execute(request);
			
//			Check for error in response status
			if (response.getStatusLine().getStatusCode() != 200) {
				
				throw new RuntimeException("Reason of Failure : " + response.getStatusLine().getStatusCode());
			}
	 
//			Read response content
			BufferedReader br = new BufferedReader( new InputStreamReader((response.getEntity().getContent())));
			String output = br.readLine();
			if(output.length() > 2){
//				Create a PrintWriter Class object for csv file creation
				PrintWriter writer = new PrintWriter("TheOutputFile.csv", "UTF-8");

//				Parsing of returned JSON Array			
				JSONParser parser=new JSONParser();
				Object obj = parser.parse(output);
		        JSONArray array = (JSONArray)obj;
		        Iterator itr = array.iterator();
		        while(itr.hasNext()) {
		        	 JSONObject obj2 = (JSONObject)itr.next();
		        	 writer.print(obj2.get("_type") + ",");
		        	 writer.print(obj2.get("_id") + ",");
		        	 writer.print(obj2.get("name") + ",");
		        	 writer.print(obj2.get("type") + ",");
		        	 JSONObject obj3 = (JSONObject)obj2.get("geo_position");
		        	 writer.print(obj3.get("latitude") + ",");
		        	 writer.println(obj3.get("longitude"));
		         }
		         
//		        Close the file writer
		        writer.close();

			}
			else{
				System.out.println("Provided String argument doesn't have any match");
			}
			
//	        Close http connection
			httpClient.getConnectionManager().shutdown();
			
		} catch(ArrayIndexOutOfBoundsException e){
			System.out.println("Please provide your choise string as argument");
		} catch(ClientProtocolException e){
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch(ParseException pe){
			pe.printStackTrace();
		} catch(Exception e){
			e.getMessage();
		}
	}
}
