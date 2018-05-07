package sensorMongo;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class sensorMessages {

	static Map<String, String> mapMessage = new HashMap<String, String>();
	
	public static void main(String[] args) {

		String topic = "sid_lab_2018";
		String broker = "tcp://iot.eclipse.org:1883";
		String clientId = "JavaSample";
		MemoryPersistence persistence = new MemoryPersistence();		
		
		try {
			MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(true);
			System.out.println("Connecting to broker: " + broker);
			
			// Listener que fica a espera de mensagens. Desconecta caso a mensagem seja igual a off.
			sampleClient.setCallback(new MqttCallback() {
				public void connectionLost(Throwable cause) {
				}
				public void messageArrived(String topic, MqttMessage message) throws Exception {
					System.out.println("Message: " + message.toString());
					new MongoInteraction().insertDocument(parseMessage(message));
					//if (message.toString().equals("off"))
						//disconnectSensor(sampleClient);						
				}

				public void deliveryComplete(IMqttDeliveryToken token) {
				}
			});
			
			sampleClient.connect(connOpts);
			System.out.println("Connected");
			sampleClient.subscribe(topic);
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
	
	public static Map<String,String> parseMessage(MqttMessage message) {
		String[] messageArray = message.toString().split("\\,", -1);
		for(String value : messageArray) {
			String[] auxSplit = value.split("\\:", -1);
			mapMessage.put(auxSplit[0], auxSplit[1]);
		}
		return mapMessage;
	}


	public static Map<String, String> getMapMessage() {
		return mapMessage;
	}
	
	
}
