package io.androidovshchik.antiyoy.gameplay;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;
import yio.tro.antiyoy.YioGdxGame;

public class AnalyzeBalanceResults {
    private final int f92N = 5;
    private ArrayList<String> distributions;
    private ArrayList<String> results;

    public static void main(String[] args) {
        new AnalyzeBalanceResults().analyze();
    }

    private void analyze() {
        String balanceResults = "[ 156 154 159 177 154 ] - [ 207 183 217 199 194 ] - [ 221 193 181 211 194 ] - [ 198 190 211 198 203 ] - [ 202 204 209 207 178 ] - [ 204 176 203 209 208 ]";
        YioGdxGame.say("\nAnalyzing balance results:");
        YioGdxGame.say(balanceResults);
        createResults(balanceResults);
        createDistributions();
        showDistributionStuff();
        YioGdxGame.say("");
        showGeneralResult();
        YioGdxGame.say("");
        showNormalizedResults();
    }

    private void showNormalizedResults() {
        YioGdxGame.say("Normalized:");
        for (int i = 0; i < this.results.size(); i++) {
            System.out.print(getNormalizedResult((String) this.results.get(i)));
            if (i != this.results.size() - 1) {
                System.out.print(" - ");
            }
        }
        YioGdxGame.say("");
    }

    private String getNormalizedResult(String result) {
        int[] array = getArray(result);
        double average = getAverageValueInResult(result);
        double[] norm = new double[5];
        for (int i = 0; i < norm.length; i++) {
            norm[i] = ((double) array[i]) / average;
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("[ ");
        for (double d : norm) {
            stringBuffer.append(trimDoubleString(Double.toString(d)) + " ");
        }
        stringBuffer.append("]");
        return stringBuffer.toString();
    }

    private void showGeneralResult() {
        YioGdxGame.say("General: " + getGeneralResult() + " - " + getDistributionFromResult(getGeneralResult()));
    }

    private String getGeneralResult() {
        int i;
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("[ ");
        int[] general = new int[5];
        for (i = 0; i < general.length; i++) {
            general[i] = 0;
        }
        Iterator it = this.results.iterator();
        while (it.hasNext()) {
            int[] array = getArray((String) it.next());
            for (i = 0; i < array.length; i++) {
                general[i] = general[i] + array[i];
            }
        }
        for (int i2 : general) {
            stringBuffer.append(i2 + " ");
        }
        stringBuffer.append("]");
        return stringBuffer.toString();
    }

    private double getAverageValueInResult(String result) {
        int[] array = getArray(result);
        double average = 0.0d;
        for (int i : array) {
            average += (double) i;
        }
        return average / ((double) array.length);
    }

    private void showDistributionStuff() {
        for (int i = 0; i < this.distributions.size(); i++) {
            if (i != 0) {
                System.out.print(" - ");
            }
            System.out.print((String) this.distributions.get(i));
        }
        YioGdxGame.say(" = " + getGeneralDistribution());
    }

    private double[] getDistributionDoubles() {
        double[] array = new double[this.distributions.size()];
        for (int i = 0; i < this.distributions.size(); i++) {
            array[i] = Double.valueOf((String) this.distributions.get(i)).doubleValue();
        }
        return array;
    }

    private String getGeneralDistribution() {
        double[] doubles = getDistributionDoubles();
        double sum = 0.0d;
        for (double d : doubles) {
            sum += d;
        }
        return trimDoubleString(Double.toString(sum / ((double) doubles.length)));
    }

    private void createResults(String balanceResults) {
        StringTokenizer balanceResultsTokenizer = new StringTokenizer(balanceResults, "-");
        this.results = new ArrayList();
        while (balanceResultsTokenizer.hasMoreTokens()) {
            String token = balanceResultsTokenizer.nextToken();
            if (token.charAt(0) == ' ') {
                token = token.substring(1, token.length());
            }
            if (token.charAt(token.length() - 1) == ' ') {
                token = token.substring(0, token.length() - 1);
            }
            this.results.add(token);
        }
    }

    private String trimDoubleString(String str) {
        if (str.length() > 4) {
            return str.substring(0, 4);
        }
        return str;
    }

    String getDistributionFromResult(String result) {
        int[] balanceIndicator = getArray(result);
        double D = 0.0d;
        int max = balanceIndicator[0];
        int min = balanceIndicator[0];
        for (int i = 0; i < balanceIndicator.length; i++) {
            if (balanceIndicator[i] > max) {
                max = balanceIndicator[i];
            }
            if (balanceIndicator[i] < min) {
                min = balanceIndicator[i];
            }
        }
        if (max > 0) {
            D = 1.0d - (((double) min) / ((double) max));
        }
        return trimDoubleString(Double.toString(D));
    }

    private void createDistributions() {
        this.distributions = new ArrayList();
        Iterator it = this.results.iterator();
        while (it.hasNext()) {
            this.distributions.add(getDistributionFromResult((String) it.next()));
        }
    }

    private int[] getArray(String resultString) {
        int[] array = new int[5];
        StringTokenizer tokenizer = new StringTokenizer(resultString, " ");
        ArrayList<String> integers = new ArrayList();
        while (tokenizer.hasMoreTokens()) {
            integers.add(tokenizer.nextToken());
        }
        integers.remove(0);
        integers.remove(integers.size() - 1);
        for (int i = 0; i < 5; i++) {
            array[i] = Integer.valueOf((String) integers.get(i)).intValue();
        }
        return array;
    }
}
