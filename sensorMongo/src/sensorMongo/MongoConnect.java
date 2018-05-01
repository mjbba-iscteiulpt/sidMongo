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

		System.out.println("Conexão efectuada com sucesso.");

		// Iniciar conecção ao servidor MongoDB
		InsertDataMongo newConnectionMongo = new InsertDataMongo(mongo);

		Date date = new Date();

		// Insere dados teste no servidor MongoDB
		newConnectionMongo.insertDocument("sensor2", date);
	}

}
