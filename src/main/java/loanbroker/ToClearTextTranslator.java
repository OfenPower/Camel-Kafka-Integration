package loanbroker;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class ToClearTextTranslator implements Processor {
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
     requestedFunds : 123.4, startCapital : 123.4, monthlyIncome : 123.4\0

      */
    @Override
    public void process(Exchange exchange) throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        JsonNode inputNode = mapper.readTree(exchange.getIn().getBody(String.class));

        String outputString = "requestedFunds " + inputNode.get("creditRequest")
                + ", startCapital " + inputNode.get("currentCapital")
                + ",monthlyIncome " + inputNode.get("monthlyIncome")
                + "\0";
        exchange.getIn().setBody(outputString);
    }
}
