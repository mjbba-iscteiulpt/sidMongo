package sensorMongo;


import java.util.Date;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class InsertDataMongo {
	
	MongoCollection<Document> collection;

	public InsertDataMongo(MongoClient mongo) {
		MongoDatabase database = mongo.getDatabase("SidMongo"); 
	     
	      
	      // Devolver uma colecção existente
	      collection = database.getCollection("SensorTeste"); 
	      System.out.println("Collection sampleCollection selected successfully");
	}
	
	public void insertDocument(String nome, Date data) {
		//Cria um novo documento para ser adicionado ao mongodb
	      Document document = new Document() 
	      .append("nome", "sensorteste")
	      .append("timestamp", data);  
	      collection.insertOne(document); 
	      System.out.println("Document inserted successfully");
	}

}
