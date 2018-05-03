package sensorMongo;

import java.util.Date;

import com.mongodb.*;


public class MongoConnect {

	public static void main(String[] args) {
		
		// cria novo cliente mongo
		MongoClient mongo = new MongoClient("localhost", 27017);

		// Creating Credentials
		MongoCredential credential;
		credential = MongoCredential.createCredential("sampleUser", "SidMongo", "".toCharArray());

		System.out.println("Conex�o efectuada com sucesso.");

		// Iniciar conec��o ao servidor MongoDB
		MongoInteraction newConnectionMongo = new MongoInteraction(mongo);

		Date date = new Date();

		// Insere dados teste no servidor MongoDB
		newConnectionMongo.insertDocument("sensor2", date);
		
		// Mostra dados da colec��o
		newConnectionMongo.getDocument();
	}

}
