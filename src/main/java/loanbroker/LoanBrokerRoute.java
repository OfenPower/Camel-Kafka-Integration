package loanbroker;

import org.apache.camel.builder.RouteBuilder;

public class LoanBrokerRoute extends RouteBuilder {

	@Override
	public void configure() {
		
		// URI Route, welche vom Kafkatopic "loan-request" liest
		String fromKafka = "kafka:loan-request?brokers=localhost:9092&groupId=loanBroker&valueDeserializer=loanbroker.LoanRequestMessageDeserializer";
		
		// Routenaufbau
		from(fromKafka)
				// translate the LoanRequestMessage to canonical Json model
				/*
				{
					"creditRequest" : 123.4,
					"currentCapital" : 123.4,
					"monthlyIncome" : 123.4,
				}
				 */
				.process(new LoanRequestToJsonProcessor())
				// calculate a credit score and add it as a new field
				/*
				{
					"creditRequest" : 123.4,
					"currentCapital" : 123.4,
					"monthlyIncome" : 123.4,
					"creditScore" : 5
				}
				 */
				.process(new CreditAgencyProcessor())
				// check Credit Score and add recipient banks according to the credit score
				/*
				{
					"creditRequest" : 123.4,
					"currentCapital" : 123.4,
					"monthlyIncome" : 123.4,
					"creditScore" : 5,
					"bankList" : [bank01, bank02, bank03]
				}
				*/
				.process(new RuleBasedBankProcessor())
				// send the request to the three banks and prepare the reply message
				// Folgender Codeblock implementiert das "Recipient-List"-Pattern
				.multicast().parallelProcessing()
					// Die Zielbanken des Multicasts werden in der Klasse "DynamicRouterBean" in der Methode "route" ermittelt.
					.bean(DynamicRouterBean.class,"route")
				.end();
		
		
				
	}

}