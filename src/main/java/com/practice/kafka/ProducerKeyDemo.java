package com.practice.kafka;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Properties;

/*To produce messages using Keys*/


public class ProducerKeyDemo {
    public static void main(String[] args) {

        final Logger log = LoggerFactory.getLogger(ProducerDemo.class.getSimpleName());
        //1) Create Producer Properties

        String bootstrapServers = "127.0.0.1:9092";
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        //2) Create the producer
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);

        for (int i = 0; i < 10; i++) {

            String topic = "Demo_Topic";
            String value = "I am a Kafka Producer";
            String key = "id_" + i;

            ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic, key, value);

            // 3) send the data
            producer.send(record, new Callback() {
                @Override
                public void onCompletion(RecordMetadata metadata, Exception exception) {
                    if (exception == null) {
                        log.info("Received new Metadata \n" +
                                "Topic : " + metadata.topic() + "\n" +
                                "Partition : " + metadata.partition() + "\n"+
                                "key : "  + record.key()  + "\n" +
                                "Offset : " + metadata.offset());

                    } else {
                        log.error("Error while Producing");
                    }
                }
            });
        }
        producer.flush();
        producer.close();
    }

}

