package sensorMongo;

import org.bson.Document;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;  

public class Main {

	public static void main(String[] args) {
		//cria novo cliente mongo
		MongoClient mongo = new MongoClient( "localhost" , 27017 ); 

		
		// Creating Credentials 
	      MongoCredential credential; 
	      credential = MongoCredential.createCredential("sampleUser", "SidMongo", "".toCharArray()); 
	      
	      System.out.println("Connected to the database successfully");  

	      
	      MongoDatabase database = mongo.getDatabase("SidMongo"); 
	     
	      
	      // Devolver uma colecção existente
	      MongoCollection<Document> collection = database.getCollection("SensorTeste"); 
	      System.out.println("Collection sampleCollection selected successfully");

	      //Cria um novo documento para ser adicionado ao mongodb
	      Document document = new Document("title", "MongoDB") 
	      .append("id", 1)
	      .append("nome", "sensorteste")
	      .append("timestamp", System.currentTimeMillis());  
	      collection.insertOne(document); 
	      System.out.println("Document inserted successfully");     
	   } 
	

}
