package sensorMongo;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class sensorMessages {

	public static void main(String[] args) {

		String topic = "sensorSID";
		String broker = "tcp://iot.eclipse.org:1883";
		String clientId = "JavaSample";
		MemoryPersistence persistence = new MemoryPersistence();

		try {
			MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(true);
			System.out.println("Connecting to broker: " + broker);
			
			sampleClient.setCallback(new MqttCallback() {
				public void connectionLost(Throwable cause) {
				}

				public void messageArrived(String topic, MqttMessage message) throws Exception {
					System.out.println("Message: " + message.toString());
				}

				public void deliveryComplete(IMqttDeliveryToken token) {
				}
			});
			
			sampleClient.connect(connOpts);
			System.out.println("Connected");
			sampleClient.subscribe(topic);

			//sampleClient.disconnect();
			//System.out.println("Disconnected");
		//	System.exit(0);
		} catch (MqttException me) {
			System.out.println("reason " + me.getReasonCode());
			System.out.println("msg " + me.getMessage());
			System.out.println("loc " + me.getLocalizedMessage());
			System.out.println("cause " + me.getCause());
			System.out.println("excep " + me);
			me.printStackTrace();
		}

	}

}
