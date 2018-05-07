package sensorMongo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoInteraction {

	MongoCollection<Document> collection;
	private MongoClient mongo;
	private MongoDatabase database;

	public MongoInteraction() {

		// cria novo cliente mongo
		mongo = new MongoClient("localhost", 27017);

		// Creating Credentials
		MongoCredential credential;
		credential = MongoCredential.createCredential("sampleUser", "SidMongo", "".toCharArray());

		System.out.println("Conexão efectuada com sucesso.");

		database = mongo.getDatabase("SidMongo");

		// Devolver uma colecção existente
		collection = database.getCollection("SensorTeste");
		System.out.println("Collection sampleCollection selected successfully");
	}

	public void insertDocument(Map<String, String> mapMessage) {
		// Cria um novo documento para ser adicionado ao mongodb
		// Document document = new Document().append("nome", nome).append("timestamp",
		// data);
		Document document = new Document();
		for (Map.Entry<String, String> entry : mapMessage.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			document.append(key, value);
		}
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
