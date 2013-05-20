package optimizer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParseException;
import java.util.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 */
public class YahooFinanceDataSource implements FinanceDataSource {

    private String symbol;
    private List<Double> adjClose;
    private List<Integer> volume;
    private List<Date> dates;
    private String baseUrl = "http://ichart.finance.yahoo.com/table.csv";
    private String timeZone = "America/New_York";
    private String datePattern = "yyyy-MM-dd";

    @Override
    public Boolean querySymbol(String symbol, Date startDate, Date endDate) {

        this.symbol = symbol;
        this.adjClose = new ArrayList<Double>();
        this.volume = new ArrayList<Integer>();
        this.dates = new ArrayList<Date>();

        DateFormat dateFormatter = new SimpleDateFormat(datePattern);
        dateFormatter.setTimeZone(TimeZone.getTimeZone(timeZone));

        String startDateString = dateFormatter.format(startDate);
        String endDateString = dateFormatter.format(endDate);

        String[] startDateElements = startDateString.split("-");
        String[] endDateElements = endDateString.split("-");

        String startYear = startDateElements[0];
        String startDay = startDateElements[2];
        Integer yahooStartMonth = Integer.parseInt(startDateElements[1]) - 1;
        String startMonth = yahooStartMonth.toString();

        String endYear = endDateElements[0];
        String endDay = endDateElements[2];
        Integer yahooEndMonth = Integer.parseInt(endDateElements[1]) - 1;
        String endMonth = yahooEndMonth.toString();

        BufferedReader reader = null;

        String queryString = "?s=" + symbol +
                "&a=" + startMonth + "&b=" + startDay +
                "&c=" + startYear + "&d=" + endMonth +
                "&e=" + endDay + "&f=" + endYear +
                "&g=d&ignore=.csv";

        try {

            URL url = new URL(baseUrl + queryString);
            reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));

            List<String> dailyRecord;
            Boolean firstLine = true;
            String line;
            int dateIndex = 0;
            int volumeIndex = 0;
            int adjCloseIndex = 0;


            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    String[] headers = line.split(",");
                    firstLine = false;
                    int numberOfColumns = headers.length;
                    for (int i = 0; i < numberOfColumns; i++) {
                        if (headers[i].equals("Date")) {
                            dateIndex = i;
                        } else if (headers[i].equals("Volume")) {
                            volumeIndex = i;
                        } else if (headers[i].equals("Adj Close")) {
                            adjCloseIndex = i;
                        }
                    }
                } else {
                    dailyRecord = Arrays.asList(line.split(","));
                    Date dateElement = null;
                    try {
                        dateElement = dateFormatter.parse(dailyRecord.get(dateIndex));
                    } catch (ParseException ex) {

                    }
                    dates.add(dateElement);
                    Integer volumeElement = Integer.parseInt(dailyRecord.get(volumeIndex));
                    volume.add(volumeElement);
                    Double adjCloseElement = Double.parseDouble(dailyRecord.get(adjCloseIndex));
                    adjClose.add(adjCloseElement);
                }
            }

            Collections.reverse(dates);
            Collections.reverse(volume);
            Collections.reverse(adjClose);

        } catch (IOException ignore) {

        } finally {
            if (reader != null) try { reader.close(); } catch (IOException ignore) {}
        }

        return true;
    }

    @Override
    public String getSymbol() {
        return this.symbol;
    }

    @Override
    public List<Double> getAdjClose() {
        return adjClose;
    }

    @Override
    public List<Integer> getVolume() {
        return volume;
    }

    @Override
    public List<Date> getDates() {
        return dates;
    }
}
