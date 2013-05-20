package optimizer;

import java.util.Date;
import java.util.List;

/**
 *
 */
public interface FinanceDataSource {

    public Boolean querySymbol(String symbol, Date startDate, Date endDate);
    public String getSymbol();
    public List<Float> getAdjClose();
    public List<Integer> getVolume();
    public List<Date> getDates();

}
