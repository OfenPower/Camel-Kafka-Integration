package MyPackage;

import org.apache.camel.builder.RouteBuilder;

public class KafkaConsumerRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {

		// Kafka Consumer Endpoint URI
		String fromKafka = "kafka:ofen?brokers=localhost:9092&groupId=groupB";

		// Von Kafka consumen
		from(fromKafka).process(new StringProcessor()).to("file:dest");

	}
}

//toKafka="kafka://localhost:9092?topic=test&brokers=localhost:9092";

// KafkaEndpoint kafkaEndpoint = new KafkaEndpoint();
// kafkaEndpoint.configureProperties(options);
// KafkaConsumer kafkaConsumer = new KafkaConsumer(endpoint, processor);
// System.out.println(kafkaConsumer.toString());
// from(kafkaConsumer.getEndpoint()).to("file:dest");

// String fromKafka5 =
// "kafka:ofen?brokers=localhost:9092&groupId=groupOfen&exchangePattern=InOnly&valueDeserializer=org.apache.kafka.common.serialization.StringDeserializer";

// from("kafka:ofen?brokers=localhost:9092").log("Message received from Kafka :
// ${body}");