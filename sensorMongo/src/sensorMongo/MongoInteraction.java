package sensorMongo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoTimeoutException;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.event.ServerHeartbeatFailedEvent;
import com.mongodb.event.ServerHeartbeatStartedEvent;
import com.mongodb.event.ServerHeartbeatSucceededEvent;
import com.mongodb.event.ServerMonitorListener;

public class MongoInteraction implements ServerMonitorListener{

	private MongoCollection<Document> collection;
	private MongoClient mongo;
	private MongoDatabase database;
	private int sleepTime = 500;
	
	public MongoInteraction() {

		try {
			mongoConnect();
		} catch (MongoTimeoutException e) {
			System.out.println("Apanhei!");
			try {
				Thread.sleep(returnSleepTime(sleepTime));
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			mongoConnect();
		}
	}

	public void mongoConnect() throws MongoTimeoutException  {
	
            MongoClientOptions clientOptions = new MongoClientOptions.Builder()
                .addServerMonitorListener((ServerMonitorListener) this)
                .build();

            mongo = new MongoClient(new ServerAddress("localhost", 27017), clientOptions);
            mongo.getAddress();
        
		System.out.println("Conexão efectuada com sucesso.");
		database = mongo.getDatabase("SidMongo");		
		// Devolver uma colecção existente
		collection = database.getCollection("SensorTeste");
		System.out.println("Collection sampleCollection selected successfully");
	}
	/*public void mongoConnect() throws Exception {
		// cria novo cliente mongo
		mongo = new MongoClient("localhost", 27017);
		try {
		mongo.getAddress();
		} catch (MongoException m) {
			System.out.println("ASDSAD");
		}
		System.out.println("Conexão efectuada com sucesso.");

		database = mongo.getDatabase("SidMongo");
		
		// Devolver uma colecção existente
		collection = database.getCollection("SensorTeste");
		System.out.println("Collection sampleCollection selected successfully");
	}*/

	public void insertDocument(Map<String, String> mapMessage) throws Exception {
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
	
	
	public void disconnectMongo() {
		System.out.println("Desligar ligação com MongoDB.");
		mongo.close();
	}
	
	public int returnSleepTime(int arg) {
		sleepTime = arg*2;
		System.out.println("A tentar reconectar em "+sleepTime/1000+" segundos...");
		return sleepTime;		
	}

	@Override
	public void serverHearbeatStarted(ServerHeartbeatStartedEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void serverHeartbeatSucceeded(ServerHeartbeatSucceededEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void serverHeartbeatFailed(ServerHeartbeatFailedEvent event) {
		mongoConnect();
		
	}

}
