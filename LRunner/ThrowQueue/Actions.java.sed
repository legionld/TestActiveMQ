/*
 * LoadRunner Java script. (Build: _build_number_)
 * 
 * Script Description: 
 *                     
 */

import lrapi.lr;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

public class Actions
{

	private ActiveMQConnectionFactory factory;
	private Connection connection;
	private Session session;
	private Destination destination_f;
	private Destination destination_s;
	
	public int init() throws Throwable {
		factory = new ActiveMQConnectionFactory();
        connection = factory.createConnection();
        connection.start();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        destination_f = session.createQueue("QUE.first.data");
        destination_s = session.createQueue("QUE.seccond.data");
		return 0;
	}//end of init


	public int action() throws Throwable {
		try {
			MessageConsumer consumer = session.createConsumer(destination_f);
			Message message = consumer.receive(1000);
			if (message instanceof TextMessage) {
				TextMessage textMessage = (TextMessage) message;
                String text = textMessage.getText();
                System.out.println("Received: " + text);
                MessageProducer producer = session.createProducer(destination_s);
                TextMessage message_s = session.createTextMessage("Second text massage");
                producer.send(message_s);
            } else {
                System.out.println("Received: " + message);
            }
		} catch (Exception e) {
                System.out.println("Caught: " + e);
                e.printStackTrace();
        }
		return 0;
	}//end of action


	public int end() throws Throwable {
		session.close();
        connection.close();
		return 0;
	}//end of end
}
