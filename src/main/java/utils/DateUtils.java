package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

	private static final String FORMATO_BRASILEIRO_PARA_DATA = "dd/MM/yyy";

	public static Date criarData(String data) throws ParseException {
		return new SimpleDateFormat(FORMATO_BRASILEIRO_PARA_DATA).parse(data);
	}
}
