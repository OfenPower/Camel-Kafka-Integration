package loanbroker;

import org.apache.camel.builder.RouteBuilder;

public class LoanBrokerBankRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		
		// LoanRequestMessage als .json an Banken weiterleiten. Jede Bank erwartet ein anderes
		// Datenformat (.json, .xml, Klartext), weshalb die .json Message für jede Bank erst mit 
		// Translator-Processors übersetzt werden muss
		from("direct:bank01").process(new ToJsonBankTranslator()).to("kafka:bank01?brokers=localhost:9092");
		from("direct:bank02").process(new ToXmlBankTranslator()).to("kafka:bank02?brokers=localhost:9092");
		from("direct:bank03").process(new ToClearTextBankTranslator()).to("kafka:bank03?brokers=localhost:9092");
		
	}

}
