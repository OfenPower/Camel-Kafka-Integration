package loanbroker;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class CleanUpProcessor implements Processor {
    
    @Override
    public void process(Exchange exchange) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(exchange.getIn().getBody(String.class));
        ObjectNode objNode = (ObjectNode)jsonNode;
        objNode.remove("receivedResponseCount");
        objNode.remove("expectedResponseCount");
        exchange.getIn().setBody(objNode.toString());
    }
}
