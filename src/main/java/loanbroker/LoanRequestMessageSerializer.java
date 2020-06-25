package loanbroker;

import java.util.Map;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.kafka.common.serialization.Serializer;

public class LoanRequestMessageSerializer implements Serializer<LoanRequestMessage> {

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
		// TODO Auto-generated method stub

	}

	@Override
	public byte[] serialize(String topic, LoanRequestMessage data) {
		byte[] dataInBytes = SerializationUtils.serialize(data);
		return dataInBytes;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

}
