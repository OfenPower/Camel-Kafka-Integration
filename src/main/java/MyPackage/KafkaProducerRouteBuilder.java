package MyPackage;

import org.apache.camel.builder.RouteBuilder;

public class KafkaProducerRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {

		// Kafka Producer Endpoint URI
		String toKafka = "kafka:ofen?brokers=localhost:9092";

		// Aus File lesen und zu Kafka producen
		from("file:src/data?noop=true").split().tokenize("\n").to(toKafka);
	}
}

//String topicName = "topic=ofen";
//String kafkaServer = "kafka:localhost:9092";
//String zooKeeperHost = "zookeeperHost=localhost&zookeeperPort=2181";
//String serializerClass = "serializerClass=kafka.serializer.StringEncoder";
//
//String toKafka = new StringBuilder().append(kafkaServer).append("?").append(topicName).append("&")
//		.append(zooKeeperHost).append("&").append(serializerClass).toString();