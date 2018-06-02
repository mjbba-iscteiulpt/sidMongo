package teste;

import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class sendMessages {

	public static void main(String[] args) {

		String topic = "sid_lab_2018_teste2";
		int qos = 0;
		String broker = "tcp://iot.eclipse.org:1883";
		String clientId = "Java";
		MemoryPersistence persistence = new MemoryPersistence();

		try {
			double temp = 25;
			double hum = 40;
			int t = 0;
			while (t < 100) {
				LocalTime hour = ZonedDateTime.now().toLocalTime().truncatedTo(ChronoUnit.SECONDS);
				String content = "{temperatura : \""+temp+"\" , humidade : \""+hum+"\" , data : \"01/06/2018\" , hora : \""+hour+"\"}";

				MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
				MqttConnectOptions connOpts = new MqttConnectOptions();
				connOpts.setCleanSession(true);
				System.out.println("Connecting to broker: " + broker);
				sampleClient.connect(connOpts);
				System.out.println("Connected");

				System.out.println("Publishing message: " + content);
				MqttMessage message = new MqttMessage(content.getBytes());
				message.setQos(qos);
				sampleClient.publish(topic, message);
				System.out.println("Message published");
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				sampleClient.disconnect();
				System.out.println("Disconnected");

				/*if (t==5 || t == 16)
					temp=70;*/
				if (t>=20) {
					temp += 0.5;
					hum += 0.5;
				}
				t++;
			}
			System.exit(0);
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
