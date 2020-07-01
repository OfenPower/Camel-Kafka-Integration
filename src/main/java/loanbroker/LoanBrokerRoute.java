package loanbroker;

import org.apache.camel.builder.RouteBuilder;

public class LoanBrokerRoute extends RouteBuilder {

	@Override
	public void configure() {

		String fromKafka = "kafka:loan-request?brokers=localhost:9092&groupId=loanBroker&valueDeserializer=loanbroker.LoanRequestMessageDeserializer";
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
				.multicast().parallelProcessing()
					.bean(DynamicRouterBean.class,"route")
				.end();
				
	}

}