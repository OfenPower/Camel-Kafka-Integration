package loanbroker;

import org.apache.camel.builder.RouteBuilder;

public class BrokerLoanResponseRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {

		from("kafka:loan-response?brokers=localhost:9092&groupId=loanBroker")
		.choice()
			.when(simple("${header.type} == 'json'")).bean(Normalizer.class, "normalizeJson")
			.when(simple("${header.type} == 'xml'")).bean(Normalizer.class, "normalizeXml")
			.when(simple("${header.type} == 'clearText'")).bean(Normalizer.class, "normalizeClearText")
		.end();
	
	
	}

}
