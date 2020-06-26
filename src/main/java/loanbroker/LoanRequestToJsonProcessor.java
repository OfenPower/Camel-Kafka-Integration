package loanbroker;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class LoanRequestToJsonProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        LoanRequestMessage request = exchange.getIn().getBody(LoanRequestMessage.class);

        ObjectMapper mapper = new ObjectMapper();

        JsonNode node = mapper.createObjectNode();
        ObjectNode obj = (ObjectNode)node;

        obj.put("creditRequest",request.getCreditRequest());
        obj.put("currentCapital",request.getCurrentCapital());
        obj.put("monthlyIncome",request.getMonthlyIncome());

        exchange.getIn().setBody(node.toString());
    }
}
