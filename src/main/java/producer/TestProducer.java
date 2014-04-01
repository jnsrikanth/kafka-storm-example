package producer;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Reads from Standard Input and creates messages on the local Kafka broker
 *
 * User: tonymeng
 * Date: 3/26/14
 */
public class TestProducer {
  public static void main(String[] args) throws Exception {
    String topic = "logs";
    String target = "s";

    Properties props = new Properties();
    if (args != null && args.length > 0) {
      props.put("metadata.broker.list", args[0] + ":9092");
    } else {
      props.put("metadata.broker.list", "localhost:9092");
    }
    props.put("serializer.class", "kafka.serializer.StringEncoder");
    props.put("request.required.acks", "1");

    ProducerConfig config = new ProducerConfig(props);
    Producer<String, String> producer = new Producer<String, String>(config);
    try {
      System.out.println("Enter messages:");
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
      String input;
      while ((input=br.readLine()) != null) {
        if (input.equals("quit")) {
          break;
        }
        producer.send(new KeyedMessage<String, String>(topic, target, input));
      }
    } finally {
      producer.close();
    }
  }
}
