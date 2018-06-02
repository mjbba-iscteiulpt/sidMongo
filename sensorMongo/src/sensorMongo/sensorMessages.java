package sensorMongo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import auxClasses.AuxClass;
import mongoJava.ThreadSybaseProcessor;


public class sensorMessages {

	public static AuxClass auxClass = new AuxClass();
	
	public static void main(String[] args) {

		String topic = "sid_lab_2018_teste2";
		String broker = "tcp://iot.eclipse.org:1883";
		String clientId = "JavaSample";
		int qos = 0;
		MemoryPersistence persistence = new MemoryPersistence();
		List<Integer> lastReceivedMessages = new ArrayList<>();
		MongoInteraction mongoInt = new MongoInteraction(topic);
		
		ThreadSybaseProcessor dbThreadManager = new ThreadSybaseProcessor(mongoInt, auxClass);
		MqttClient sampleClient;
		
		try {
			sampleClient = new MqttClient(broker, clientId, persistence);
			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(true);
			System.out.println("Connecting to broker: " + broker);
			dbThreadManager.startNewThread();
			// Listener que fica a espera de mensagens.
			sampleClient.setCallback(new MqttCallback() {
				public void connectionLost(Throwable cause) {
					try {
						sampleClient.connect(connOpts);
						System.out.println("Connected");
						sampleClient.subscribe(topic);
					} catch (MqttException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}					
				}

				public void messageArrived(String topic, MqttMessage message) throws Exception {
					System.out.println("Message: " + message.toString());
					try {
						mongoInt.mongoConnect();
						//for (MqttMessage i : new ArrayList<>(pillhaMensagens)) {
							mongoInt.insertDocument(parseMessage(message));
							//pillhaMensagens.remove(i);
						//}
						mongoInt.disconnectMongo();
					} catch (Exception e) {
						
					}
				}

				public void deliveryComplete(IMqttDeliveryToken token) {
				}
			});

			sampleClient.connect(connOpts);
			System.out.println("Connected");
			sampleClient.subscribe(topic, qos);

		} catch (MqttException me) {
			System.out.println("reason " + me.getReasonCode());
			System.out.println("msg " + me.getMessage());
			System.out.println("loc " + me.getLocalizedMessage());
			System.out.println("cause " + me.getCause());
			System.out.println("excep " + me);
			me.printStackTrace();
		}

	}

	// Metodo para desconectar.
	public static void disconnectSensor(MqttClient sampleClient) throws MqttException {
		System.out.println("Disconnected");
		sampleClient.disconnect();
		System.exit(0);
	}

	public static Map<String, String> parseMessage(MqttMessage message) {
		String[] messageArray = message.toString().split("\\,", -1);
		Map<String, String> mapMessage = new HashMap<String, String>();
		for (String value : messageArray) {
			String[] auxSplit = value.split(":(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
			auxSplit[0] = auxSplit[0].replaceAll("[\"\\{\\}]", "");
			auxSplit[1] = auxSplit[1].replaceAll("[\"\\{\\}]", "");
			System.out.println(auxSplit[0]+": "+auxSplit[1]);
			if (auxSplit[0].contains("temperatura") || auxSplit[0].contains("temperature")) {
				auxClass.addTemp(Double.parseDouble(auxSplit[1]));
			}
			if (auxSplit[0].contains("humidade") || auxSplit[0].contains("humidity"))
				auxClass.addHum(Double.parseDouble(auxSplit[1]));
			mapMessage.put(auxSplit[0], auxSplit[1]);
		}
		return mapMessage;
	}
	
	/*public void SubNewTopic(String newTopic) {
		sampleClient.subscribe(newTopic);
	}*/
	
	
	
}
