package teste;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JFileChooser;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientOptions.Builder;
import com.mongodb.MongoCredential;
import com.mongodb.MongoTimeoutException;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;

public class teste {

	public static void main(String[] args) {
		/*URL url;
        String response = null;
        String username = "mario";
        try {
            url = new URL("http://192.168.1.76/testConnection.php?user="+username+"&pass="+password);
            URLConnection conn = url.openConnection();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;

            while ((line = rd.readLine()) != null) {
                response = line;
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Log.d("login", response);
        if(response.contains("Connected")) {*/
		
	}
	
	
	public static void connect() throws MongoTimeoutException {
		System.out.println("Antes");
		Builder o = MongoClientOptions.builder().serverSelectionTimeout(3000);

		String databaseName = "SidMongo";

		System.out.println("Builder");

		MongoClient mongoClient = new MongoClient(new ServerAddress("localhost", 27017), o.build());
		System.out.println("mongoClient");
		// call this method to check if the connection succeeded as the mongo client lazy loads the connection 
		mongoClient.getAddress();
		System.out.println("getAddress");
		System.out.println(mongoClient.getDatabase(databaseName));
	}

}
