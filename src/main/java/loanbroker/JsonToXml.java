package loanbroker;

import java.util.Iterator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonToXml {

    public static String jsonToXml(String json)
    {
        ObjectMapper mapper = new ObjectMapper();
        StringBuilder xml = new StringBuilder();
        //xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\">\n");
        xml.append("<CreditRequest>\n");
        try {
            JsonNode node = mapper.readTree(json);

            Iterator<String> fieldNames = node.fieldNames();

            while(fieldNames.hasNext())
            {
                String fieldName = fieldNames.next();
                xml.append("\t<" + fieldName + ">" + node.get(fieldName) + "</"+fieldName+">\n");
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        xml.append("</CreditRequest>");
        return xml.toString();
    }
}
