package sensorMongo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoInteraction {

	MongoCollection<Document> collection;

	public MongoInteraction(MongoClient mongo) {
		MongoDatabase database = mongo.getDatabase("SidMongo");

		// Devolver uma colecção existente
		collection = database.getCollection("SensorTeste");
		System.out.println("Collection sampleCollection selected successfully");
	}

	public void insertDocument(String nome, Date data) {
		// Cria um novo documento para ser adicionado ao mongodb
		Document document = new Document().append("nome", nome).append("timestamp", data);
		collection.insertOne(document);
		System.out.println("Document inserted successfully");
	}

	public void getDocument() {

		List<Document> documents = (List<Document>) collection.find().into(new ArrayList<Document>());

		for (Document document : documents) {
			System.out.println(document);
		}
	}

}
