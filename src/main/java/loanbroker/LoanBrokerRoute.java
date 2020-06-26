package loanbroker;

import org.apache.camel.builder.RouteBuilder;

public class LoanBrokerRoute extends RouteBuilder {

	@Override
	public void configure() {

		String fromKafka = "kafka:loan-request?brokers=localhost:9092&groupId=groupA&valueDeserializer=loanbroker.LoanRequestMessageDeserializer";
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
				// send the request to the three banks
				//.multicast(new BankResponseAggregationStrategy()).parallelProcessing()
				//.to("jms:queue:bank1", "jms:queue:bank2", "jms:queue:bank3").end()
				// and prepare the reply message
				.multicast(new BankResponseAggregationStrategy()).parallelProcessing()
					.bean("DynamicRouterBean","route")
				.end()

				.process(new ReplyProcessor());

		// Each bank processor will process the message and put the response message
		// back
		from("jms:queue:bank1").process(new BankProcessor("bank1"));
		from("jms:queue:bank2").process(new BankProcessor("bank2"));
		from("jms:queue:bank3").process(new BankProcessor("bank3"));
	}

}