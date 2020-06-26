package loanbroker;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.RecipientList;

import java.util.HashSet;
import java.util.Set;

public class DynamicRouterBean {

    @RecipientList
    public Set<String> route(String body)
    {
        Set<String> result = new HashSet<>();

        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode node = mapper.readTree(body).get("bankList");
            if(node.isArray())
            {
                for(JsonNode objNode : node)
                {
                    result.add(objNode.toString());
                }
            }
            else
            {
                assert(false); // Node was not an array
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return result;
    }

}
