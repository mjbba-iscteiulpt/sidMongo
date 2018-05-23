package mongoJava;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import auxClasses.AuxClass;
import sensorMongo.MongoInteraction;

public class ThreadSybaseProcessor {
	final ExecutorService workerThreadPool = Executors.newFixedThreadPool(1);
	public static MongoInteraction mongoInt;
	private AuxClass auxClass;

	public static MongoInteraction getMongoInt() {
		return mongoInt;
	}

	public ThreadSybaseProcessor(MongoInteraction mongoInt, AuxClass auxClass) {
		this.mongoInt =mongoInt;
		this.auxClass = auxClass;
	}

	public ThreadSybaseProcessor() {
		// TODO Auto-generated constructor stub
	}

	public void startNewThread() {
		workerThreadPool.execute(new StorageSybaseProcess());
	}

	public void onShutdown() {
		workerThreadPool.shutdown();
	}
}
