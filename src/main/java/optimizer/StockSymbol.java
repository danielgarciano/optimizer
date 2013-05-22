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

    private Double calculateAverageDailyReturn(double[] dailyReturns) {
        double sum = 0;
        for (double dailyReturn: dailyReturns) {
            sum += dailyReturn;
        }
        return sum / dailyReturns.length;
    }

    private Double calculateSharpeRatio(double[] dailyReturns) {
        Double avgDailyReturn = calculateAverageDailyReturn(dailyReturns);
        StandardDeviation stdDev = new StandardDeviation();
        Double stdDevDailyReturn = stdDev.evaluate(dailyReturns);
        return Math.sqrt(dailyReturns.length) * (avgDailyReturn/stdDevDailyReturn);
    }

    private Double calculateReturn(List<Double> adjClose) {
        double start = adjClose.get(0);
        double end = adjClose.get(adjClose.size() - 1);
        return ((end / start) - 1) * 100;
    }

    public Double getSharpeRatio() {
        return this.calculateSharpeRatio(this.dailyReturns);
    }

    private double[] getDailyReturnsByDays(Integer days) {
        double[] dailyReturnsDays = new double[days];
        int start = this.dailyReturns.length - days;
        int j = 0;
        for (int i = start; i < this.dailyReturns.length; i++) {
            dailyReturnsDays[j] = this.dailyReturns[i];
            j++;
        }
        return dailyReturnsDays;
    }

    private List<Double> getAdjCloseByDays(Integer days) {
        int start = this.adjClose.size() - days;
        int end =  this.adjClose.size();
        return this.adjClose.subList(start, end);
    }

    public Double getSharpeRatio(Integer days) {
        return this.calculateSharpeRatio(getDailyReturnsByDays(days));
    }

    public Double getReturn() {
        return this.calculateReturn(this.adjClose);
    }

    public Double getReturn(Integer days) {
        return this.calculateReturn(getAdjCloseByDays(days));
    }

}
