package loanbroker;

import org.apache.camel.builder.RouteBuilder;

public class BrokerBankRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		// Each bank processor will process the message and put the response message
		// back
		from("direct:bank01").process(new ToJsonBankTranslator()).to("kafka:bank01?brokers=localhost:9092");
		from("direct:bank02").process(new ToXmlBankTranslator()).to("kafka:bank02?brokers=localhost:9092");
		from("direct:bank03").process(new ToClearTextBankTranslator()).to("kafka:bank03?brokers=localhost:9092");
		
	}

}
