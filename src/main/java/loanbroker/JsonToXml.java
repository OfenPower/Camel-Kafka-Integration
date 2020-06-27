package loanbroker;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Iterator;

public class JsonToXml {

    public static String jsonToXml(String json)
    {
        ObjectMapper mapper = new ObjectMapper();
        StringBuilder xml = new StringBuilder("<Request>\n");
        try {
            JsonNode node = mapper.readTree(json);

            Iterator<String> fieldNames = node.fieldNames();

            while(fieldNames.hasNext())
            {
                String fieldName = fieldNames.next();
                JsonNode fieldValue = node.get(fieldName);
                xml.append("\t<" + fieldName + "> " + fieldValue.textValue() + "</"+fieldName+">\n");
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        xml.append("</Request>");
        return xml.toString();
    }
}
