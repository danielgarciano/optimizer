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
            startDate = dateFormatter.parse("2012-05-19");
            endDate = dateFormatter.parse("2013-05-19");
        } catch (ParseException ex) {

        }
        if (dataSource.querySymbol("CSCO", startDate, endDate)) {
            StockSymbol stockSymbol = new StockSymbol(dataSource);
            List<Float> dailyReturns = stockSymbol.getDailyReturns();
            List<Date> dates = stockSymbol.getDates();
            int i = 0;
            for (Float dailyReturn: dailyReturns) {
                System.out.println(dateFormatter.format(dates.get(i)));
                System.out.println(dailyReturn);
                i++;
            }
            System.out.println(stockSymbol.averageDailyReturn());
        }
    }
}
