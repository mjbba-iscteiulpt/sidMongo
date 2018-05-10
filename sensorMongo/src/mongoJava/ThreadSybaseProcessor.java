package mongoJava;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadSybaseProcessor {
	final ExecutorService workerThreadPool = Executors.newFixedThreadPool(1);

	public ThreadSybaseProcessor() {
	}

	public void startNewThread() {
		workerThreadPool.execute(new StorageSybaseProcess());
	}

	public void onShutdown() {
		workerThreadPool.shutdown();
	}
}
