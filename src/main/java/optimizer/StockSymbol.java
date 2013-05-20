package optimizer;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * author: danielgarciano
 */
public class StockSymbol {

    private String symbol;
    private List<Date> dates;
    private List<Integer> volume;
    private List<Float> adjClose;
    private List<Float> dailyReturn;
    private List<Float> bollingerBands;
    private List<Float> beta;
    private List<Float> covariance;


    /**
     *
     * @param financeDataSource
     */
    public StockSymbol(FinanceDataSource financeDataSource) {
        this.symbol = financeDataSource.getSymbol();
        this.dates = financeDataSource.getDates();
        this.volume = financeDataSource.getVolume();
        this.adjClose = financeDataSource.getAdjClose();
    }

    public List<Float> getDailyReturns() {
        List<Float> dailyReturn = new ArrayList<Float>();
        Float previousClose = null;
        for (Float dailyAdjClose: this.adjClose) {
            if (previousClose == null) {
                dailyReturn.add(new Float(0.0));
            } else {
                dailyReturn.add((dailyAdjClose/previousClose) - 1);
            }
            previousClose = dailyAdjClose;
        }
        return dailyReturn;
    }

    public List<Date> getDates() {
        return dates;
    }

    public Float averageDailyReturn() {
        List<Float> dailyReturns = getDailyReturns();
        Float sum = new Float(0.0);
        for (Float dailyReturn: dailyReturns) {
            sum += dailyReturn;
        }
        return sum / dailyReturns.size();
    }
}
