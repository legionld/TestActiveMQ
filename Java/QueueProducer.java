package src;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class QueueProducer extends Thread {

    private ActiveMQConnectionFactory connectionFactory;
    private Connection connection;
    private  Session session;

    public QueueProducer() {
        try {
            // Create a ConnectionFactory
            connectionFactory = new ActiveMQConnectionFactory();

            // Create a Connection
            connection = connectionFactory.createConnection();
            connection.start();

            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        } catch (Exception e) {
            System.out.println("Caught: " + e);
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        int i = 0;
            try {
                sleep(500);
                // Create the destination (Topic or Queue)
                Destination destination = session.createQueue("TEST.F");

                // Create a MessageProducer from the Session to the Topic or Queue
                MessageProducer producer = session.createProducer(destination);
                producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

                while (i < 12) {
                    // Create a messages
                    String text = new StringBuilder().append("i = ").append(i).append("Hello world! From: ").append(Thread.currentThread().getName()).append(" : ").append(this.hashCode()).toString();
                    TextMessage message = session.createTextMessage(text);

                    // Tell the producer to send the message
                    System.out.println("Sent message: " + message.hashCode() + " : " + Thread.currentThread().getName());
                    producer.send(message);
                    ++i;
                }
                producer.close();
            }
            catch (Exception e) {
                System.out.println("Caught: " + e);
                e.printStackTrace();
            }
    }

    public void end() {
        try {
            session.close();
            connection.close();
        } catch (Exception e) {
            System.out.println("Caught: " + e);
            e.printStackTrace();
        }
    }
}
