package optimizer;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Hello world!
 */
public class App {

    private static String timeZone = "America/New_York";
    private static String datePattern = "yyyy-MM-dd";

    public static void main( String[] args ) {
        System.out.println( "Hello World!" );
        FinanceDataSource dataSource = new YahooFinanceDataSource();
        DateFormat dateFormatter = new SimpleDateFormat(datePattern);
        dateFormatter.setTimeZone(TimeZone.getTimeZone(timeZone));
        Date startDate = null;
        Date endDate = null;
        try {
            startDate = dateFormatter.parse("2012-04-20");
            endDate = dateFormatter.parse("2013-04-20");
        } catch (ParseException ex) {

        }

        List<String> symbols = new ArrayList<String>();
        symbols.add("TLT");

        for (String symbol: symbols) {
            if (dataSource.querySymbol(symbol, startDate, endDate)) {
                StockSymbol stockSymbol = new StockSymbol(dataSource);
                System.out.print(stockSymbol.getSymbol() + " : ");
                System.out.println(stockSymbol.getSharpeRatio());
            }
        }
    }
}
