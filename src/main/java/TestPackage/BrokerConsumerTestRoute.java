package TestPackage;

import org.apache.camel.builder.RouteBuilder;

public class BrokerConsumerTestRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {

		// Kafka Consumer Endpoint URI
		String fromKafka = "kafka:loan-request?brokers=localhost:9092&groupId=groupA&valueDeserializer=Message.LoanRequestMessageDeserializer";

		// Von Kafka consumen
		// from(fromKafka).unmarshal().serialization().process(new
		// LoanRequestProcessorTest()).to("file:dest");
		from(fromKafka).process(new LoanRequestProcessorTest()).to("file:dest");

	}
}
