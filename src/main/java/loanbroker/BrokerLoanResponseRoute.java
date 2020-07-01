package loanbroker;

import org.apache.camel.builder.RouteBuilder;

public class BrokerLoanResponseRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		
		// Step 1: Normalize aller Dateiformate zu .json
		from("kafka:loan-response?brokers=localhost:9092&groupId=loanBroker")
		.choice()
			.when(simple("${header.type} == 'json'")).bean(Normalizer.class, "normalizeJson")
			.when(simple("${header.type} == 'xml'")).bean(Normalizer.class, "normalizeXml")
			.when(simple("${header.type} == 'clearText'")).bean(Normalizer.class, "normalizeClearText")
		.end()
		.process(new CorrelationIdPrintProcessor())
		// Step 2: Aggregate, sodass aus allen Angeboten das Beste für den Kunden zurückgeliefert wird
		.aggregate(new CorrelationExpression(), new BankResponseAggregationStrategy()).completionTimeout(5000)
		.process(new PrintMessageProcessor());
		//.to("kafka:broker-response?brokers=localhost:9092");
	
	
	}

}
