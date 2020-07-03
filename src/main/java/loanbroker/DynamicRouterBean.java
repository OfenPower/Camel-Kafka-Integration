package loanbroker;

import java.util.HashSet;
import java.util.Set;

import org.apache.camel.RecipientList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DynamicRouterBean {

    @RecipientList
    public Set<String> route(String body)
    {
    	// Set der Banken an die der LoanRequest gesendet werden soll, in URI.
    	// Die Werte im Set sind URI der Form "direct:bank01, direct:bank02, etc."
        Set<String> result = new HashSet<>();
        
        // Json bankList-Feld iterieren, Banken entnehmen und dem Bankenset hinzufügen
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode node = mapper.readTree(body).get("bankList");
            if(node.isArray())
            {
                for(JsonNode objNode : node)
                {
                    result.add("direct:" + objNode.asText().toString());
                    System.out.println("DynamicRouter added endpoint: " + objNode.asText().toString() + " to recipientlist.");
                }
            }
            else
            {
                assert(false); // Node was not an array
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        
        // Set an den Multicast zurückliefern
        return result;
    }

}
