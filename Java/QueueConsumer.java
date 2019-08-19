package src;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class QueueConsumer extends Thread implements ExceptionListener {

    private ActiveMQConnectionFactory connectionFactory;
    private Connection connection;
    private  Session session;

    public QueueConsumer() {
        try {
            // Create a ConnectionFactory
            connectionFactory = new ActiveMQConnectionFactory();

            // Create a Connection
            connection = connectionFactory.createConnection();
            connection.start();
            connection.setExceptionListener(this);

            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        } catch (Exception e) {
            System.out.println("Caught: " + e);
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            // Create the destination (Topic or Queue)
            Destination destination_f = session.createQueue("TEST.F");
            Destination destination_s = session.createQueue("TEST.S");

            // Create a MessageConsumer from the Session to the Topic or Queue
            MessageConsumer consumer = session.createConsumer(destination_f);

            // Create a Producate
            MessageProducer producer = session.createProducer(destination_s);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);


            int i = 0;
            while (i < 16) {
                sleep(500);
                // Wait for a message
                Message message = consumer.receive(1000);
                if (message instanceof TextMessage) {
                    TextMessage textMessage = (TextMessage) message;
                    String text = textMessage.getText();
                    System.out.println("OPReceived: " + text);
                    // Create a messages
                    String text_o = "Hello world! From: " + Thread.currentThread().getName() + " : " + this.hashCode();
                    TextMessage message_o = session.createTextMessage(text_o);

                    // Tell the producer to send the message
                    System.out.println("Sent message: "+ message.hashCode() + " : " + Thread.currentThread().getName());
                    producer.send(message_o);
                } else {
                    System.out.println("Received: " + message);
                }
                ++i;
            }
            producer.close();
            consumer.close();

        } catch (Exception e) {
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

    public synchronized void onException(JMSException ex) {
        System.out.println("JMS Exception occured.  Shutting down client.");
    }
}