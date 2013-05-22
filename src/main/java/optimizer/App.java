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
            startDate = dateFormatter.parse("2012-05-21");
            endDate = dateFormatter.parse("2013-05-21");
        } catch (ParseException ex) {

        }

        List<String> symbols = new ArrayList<String>();
        symbols.add("TLT");
        symbols.add("SPY");

        for (String symbol: symbols) {
            if (dataSource.querySymbol(symbol, startDate, endDate)) {
                StockSymbol stockSymbol = new StockSymbol(dataSource);
                System.out.println(stockSymbol.getSymbol() + " : ");
                System.out.print("[1 year] Sharpe " +stockSymbol.getSharpeRatio() + " Return " + stockSymbol.getReturn() + " : ");
                System.out.println("[6 months] Sharpe " + stockSymbol.getSharpeRatio(126) + " Return " + stockSymbol.getReturn(126));
                System.out.print("[3 months] Sharpe " + stockSymbol.getSharpeRatio(63) + " Return " + stockSymbol.getReturn(63) + " : ");
                System.out.println("[1 month] Sharpe " + stockSymbol.getSharpeRatio(21) + " Return " + stockSymbol.getReturn(21));
            }
        }
    }
}
