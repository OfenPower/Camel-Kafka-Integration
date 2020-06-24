package TestPackage;

import org.apache.camel.builder.RouteBuilder;

public class LoanRequestBrokerRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {

		// Kafka Producer Endpoint URI
		String toKafka = "kafka:loan-request?brokers=localhost:9092&serializerClass=Message.LoanRequestMessageSerializer";

		// /sa/src/main/java/Message/LoanRequestMessageSerializer
		// serializerClass=org.apache.kafka.common.serialization.ByteArraySerializer
		// valueDeserializer

		// Alles was an "direct:start" geschickt wird an Kafka weiterleiten
		// from("direct:start").marshal().serialization().to(toKafka);
		from("direct:start").to(toKafka);
	}

}
