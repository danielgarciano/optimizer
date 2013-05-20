package optimizer;

import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import java.util.Date;
import java.util.List;


/**
 * author: danielgarciano
 */
public class StockSymbol {

    private String symbol;
    private List<Date> dates;
    private List<Integer> volume;
    private List<Double> adjClose;
    private double[] dailyReturns;
    private List<Double> bollingerBands;
    private List<Double> beta;
    private List<Double> covariance;


    /**
     *
     * @param financeDataSource
     */
    public StockSymbol(FinanceDataSource financeDataSource) {
        this.symbol = financeDataSource.getSymbol();
        this.dates = financeDataSource.getDates();
        this.volume = financeDataSource.getVolume();
        this.adjClose = financeDataSource.getAdjClose();
        this.dailyReturns = calculateDailyReturns();
    }

    public List<Date> getDates() {
        return dates;
    }

    public double[] getDailyReturns() {
        return this.dailyReturns;
    }

    public String getSymbol() {
        return this.symbol;
    }

    private double[] calculateDailyReturns() {
        double[] dailyReturns = new double[this.adjClose.size()];
        Double previousClose = null;
        int i = 0;
        for (Double dailyAdjClose: this.adjClose) {
            if (previousClose == null) {
                dailyReturns[i] = 0;
            } else {
                dailyReturns[i] = (dailyAdjClose/previousClose) - 1;
            }
            previousClose = dailyAdjClose;
            i++;
        }
        return dailyReturns;
    }

    private Double calculateAverageDailyReturn() {
        Double sum = new Double(0);
        for (Double dailyReturn: this.dailyReturns) {
            sum += dailyReturn;
        }
        return sum / this.dailyReturns.length;
    }

    public Double getSharpeRatio() {
        Double avgDailyReturn = calculateAverageDailyReturn();
        StandardDeviation stdDev = new StandardDeviation();
        Double stdDevDailyReturn = stdDev.evaluate(this.dailyReturns);
        return Math.sqrt(this.dailyReturns.length) * (avgDailyReturn/stdDevDailyReturn);
    }

}
