package loanbroker;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import bank.JsonBankMain;

/*
 * Translator-Processor, welcher die eingehende .json Message für die Bank anpasst.
 * Die Bankenliste wird hier entfernt
 */
public class ToJsonBankTranslator implements Processor {

    /*
    Translates a message from the given format
    {
		"creditRequest" : 123.4,
		"currentCapital" : 123.4,
		"monthlyIncome" : 123.4,
		"creditScore" : 5,
		"bankList" : [bank01, bank02, bank03]
	}
    to the format required by the JsonBank
    {
        "requestedFunds" : 123.4,
        "startCapital" : 123.4,
        "monthlyIncome" : 123.4,
        "creditScore" : 5
    }
     */
    @Override
    public void process(Exchange exchange) throws Exception {

        // .json Message in neue .json Datei mit anderen Feldnamen übersetzen
    	ObjectMapper mapper = new ObjectMapper();
        JsonNode inputNode = mapper.readTree(exchange.getIn().getBody(String.class));
        JsonBankMain.Request request = new JsonBankMain.Request();
        request.requestedFunds = inputNode.get("creditRequest").asDouble();
        request.startCapital = inputNode.get("currentCapital").asDouble();
        request.monthlyIncome = inputNode.get("monthlyIncome").asDouble();
        request.creditScore = inputNode.get("creditScore").asInt();
        request.correlationId = inputNode.get("correlationId").asInt();

        exchange.getIn().setBody(mapper.writeValueAsString(request));
    }
}
