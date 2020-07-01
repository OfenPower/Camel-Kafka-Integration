package loanbroker;

import org.apache.camel.builder.RouteBuilder;

public class LoanBrokerResponseRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		
		// Step 1: Normalize aller Dateiformate zu .json
		from("kafka:loan-response?brokers=localhost:9092&groupId=loanBroker")
		.choice()
			.when(simple("${header.type} == 'json'")).bean(Normalizer.class, "normalizeJson")
			.when(simple("${header.type} == 'xml'")).bean(Normalizer.class, "normalizeXml")
			.when(simple("${header.type} == 'clearText'")).bean(Normalizer.class, "normalizeClearText")
		.end()
		//.process(new CorrelationIdPrintProcessor())
		// Step 2: Aggregate, sodass aus allen Angeboten das Beste f�r den Kunden zur�ckgeliefert wird
		.aggregate(new CorrelationExpression(), new BankResponseAggregationStrategy()).completionTimeout(10000)
		.process(new CleanUpProcessor())
		.to("kafka:broker-response?brokers=localhost:9092");
	}

}
