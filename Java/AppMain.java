package src;
/**
 * Hello world!
 */
public class AppMain {

    public static void main(String[] args) throws Exception {
        int i = 0;

        QueueProducer firstQue = new QueueProducer();
        QueueConsumer secondQue = new QueueConsumer();

        firstQue.start();
        secondQue.start();

        while (i < 12)
        {
            Thread.sleep(1000);
            ++i;
        }

        firstQue.end();
        secondQue.end();
    }
}


