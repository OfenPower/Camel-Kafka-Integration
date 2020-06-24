package TestPackage;

import org.apache.camel.Exchange;
import org.apache.camel.NoTypeConversionAvailableException;
import org.apache.camel.TypeConversionException;
import org.apache.camel.TypeConverter;

public class LoanRequestTypeConverter implements TypeConverter {

	@Override
	public boolean allowNull() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <T> T convertTo(Class<T> type, Object value) throws TypeConversionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T convertTo(Class<T> type, Exchange exchange, Object value) throws TypeConversionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T mandatoryConvertTo(Class<T> type, Object value)
			throws TypeConversionException, NoTypeConversionAvailableException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T mandatoryConvertTo(Class<T> type, Exchange exchange, Object value)
			throws TypeConversionException, NoTypeConversionAvailableException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T tryConvertTo(Class<T> type, Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T tryConvertTo(Class<T> type, Exchange exchange, Object value) {
		// TODO Auto-generated method stub
		return null;
	}

}
