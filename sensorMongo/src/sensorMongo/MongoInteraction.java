package sensorMongo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoTimeoutException;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.event.ServerHeartbeatFailedEvent;
import com.mongodb.event.ServerHeartbeatStartedEvent;
import com.mongodb.event.ServerHeartbeatSucceededEvent;
import com.mongodb.event.ServerMonitorListener;

public class MongoInteraction implements ServerMonitorListener {

	private MongoCollection<Document> collection;
	private MongoClient mongo;
	private MongoDatabase database;
	private int sleepTime = 500;
	private ServerAddress seed1;
	private ServerAddress seed2;
	private ServerAddress seed3;
	private List<ServerAddress> seedList;
	private String username = "admin";
    private String password = "sid";
    private String DEFAULT_DB = "SidMongo";
    private String topic;

	public MongoInteraction(String topic) {
		this.topic = topic;
		/*try {
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
		}*/
	}

	public MongoInteraction() {
		// TODO Auto-generated constructor stub
	}

	public void mongoConnect() {
		seed1 = new ServerAddress("localhost", 27017);
	    seed2 = new ServerAddress("localhost", 27018);
	    seed3 = new ServerAddress("localhost", 27019);
	    String ReplSetName = "rs0";
	    
	    seedList = new ArrayList<ServerAddress>();
        seedList.add(seed1);
        seedList.add(seed2);
        seedList.add(seed3);
        
        MongoClientURI connectionString = new MongoClientURI("mongodb://" + username + ":" + password + "@" + 
                seed1 + "," + seed2 + "," + seed3 + "/" + 
                DEFAULT_DB + 
                "?replicaSet=" + ReplSetName);
        
        mongo = new MongoClient(connectionString);
        System.out.println("Address: "+mongo.getAddress());
        System.out.println("Conexão efectuada com sucesso.");
        database = mongo.getDatabase("SidMongo");
        collection = database.getCollection("SensorTeste");
		System.out.println("Collection sampleCollection selected successfully "+collection.toString());
	}
	

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

	public List<Document> getDocument() {
		List<Document> documents = (List<Document>) collection.find().into(new ArrayList<Document>());
		return documents;
	}

	public void disconnectMongo() {
		System.out.println("Desligar ligação com MongoDB.");
		mongo.close();
	}

	public int returnSleepTime(int arg) {
		sleepTime = arg * 2;
		System.out.println("A tentar reconectar em " + sleepTime / 1000 + " segundos...");
		return sleepTime;
	}
	
	public void deleteOneDoc(String field, String value) {
		System.out.println("Apagar o campo "+field+" com o valor: "+value);
		mongoConnect();
		String field1 = "temperatura ";
		String value1 = " "+value+" ";
		BasicDBObject query = new BasicDBObject();
		query.append(field1, value1);
		System.out.println("QUERY: "+query.toString());
		collection.deleteOne(query);
		disconnectMongo();
	}
	
	public void deleteMany() {
		System.out.println("A remover todos os registos do MongoDB...");
		collection.deleteMany(new Document());
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
