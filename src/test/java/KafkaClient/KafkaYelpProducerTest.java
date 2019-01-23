package KafkaClient;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import org.junit.Test;

public class KafkaYelpProducerTest {
    @Test
    public void runSimpleProducerTest() {
        Producer<Long, String> producer = ProducerCreator.createProducer();
        for (int index = 0; index < IKafkaConstants.MESSAGE_COUNT; index++) {
            ProducerRecord<Long, String> record = new ProducerRecord<Long, String>(IKafkaConstants.TOPIC_NAME,
                    "This is record " + index);
            try {
                RecordMetadata metadata = producer.send(record).get();
                System.out.println("Record sent with key " + index + " to partition " + metadata.partition()
                        + " with offset " + metadata.offset());
            }
            catch (ExecutionException | InterruptedException e) {
                System.out.println("Error in sending record");
                System.out.println(e);
            }
        }
    }

    @Test
    public void runJSONProducerTest() throws Exception {
        Producer<Long, String> producer = ProducerCreator.createProducer();
        String filePath = "business.json";
        try (Stream<String> stream = Files.lines(Paths.get(filePath))) {
            final AtomicInteger count = new AtomicInteger();
            stream.forEach(line -> {
                try {
                    doThing(line, producer);
                    System.out.println(count.incrementAndGet());
                } catch(Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    private void doThing(String line, Producer<Long, String> producer) throws InterruptedException {
        ProducerRecord<Long, String> record = new ProducerRecord<Long, String>
                ("yelpBusiness", line);
        try {
            RecordMetadata metadata = producer.send(record).get();
            System.out.println(String.format("Record sent with value %s to %s", record.value(), metadata.offset()));
        }
        catch (ExecutionException | InterruptedException e) {
            System.out.println("Error in sending record");
            System.out.println(e);
        }
    }
}