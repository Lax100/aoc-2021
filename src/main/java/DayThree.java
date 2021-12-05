import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class DayThree {

    private static int calculatePowerConsumption(String fileName) throws IOException {
        List<String> diagVals = FileUtils.readInputFile(fileName);
        int powerConsumption = 0;
        if (!diagVals.isEmpty()) {
            // 0 -> [zeroCountPerColumn]
            // 1 -> [oneCountPerColumn]
            int[][] bitCountPerColumn = new int[2][diagVals.get(0).length()];
            for (String diagVal : diagVals) {
                for (int i = 0; i < diagVal.length(); i++) {
                    bitCountPerColumn[diagVal.charAt(i) - '0'][i]++;
                }
            }
            StringBuilder gammaSb = new StringBuilder();
            StringBuilder epsilonSb = new StringBuilder();
            for (int i = 0; i < bitCountPerColumn[0].length; i++) {
                gammaSb.append(bitCountPerColumn[0][i] > bitCountPerColumn[1][i] ? "0" : "1");
                epsilonSb.append(bitCountPerColumn[0][i] < bitCountPerColumn[1][i] ? "0" : "1");
            }
            int gamma = Integer.parseInt(gammaSb.toString(), 2);
            int epsilon = Integer.parseInt(epsilonSb.toString(), 2);
            powerConsumption = gamma * epsilon;
        }
        return powerConsumption;
    }

    private static int calculateLifeSupportRating(String fileName) throws IOException {
        List<String> diagVals = FileUtils.readInputFile(fileName);
        final int o2Rating = calculateOxygenGeneratorRating(diagVals);
        final int co2Rating = calculateCo2ScrubberRating(diagVals);
        System.out.printf("Oxygen Generator Rating = %d%n", o2Rating);
        System.out.printf("Co2 Generator Rating = %d%n", co2Rating);
        return o2Rating * co2Rating;
    }

    private static int calculateRatingWithCriteria(List<String> diagVals,
        Function<int[], Integer> bitCriteria) {
        List<String> remainingVals = new ArrayList<>(diagVals);
        int indexToConsider = 0;
        int[] currentBitCounts = new int[2];
        while (remainingVals.size() > 1) {
            // System.out.printf("Remaining vals = %s%n", remainingVals);
            // System.out.printf("Index to consider = %d%n", indexToConsider);
            int finalIndexToConsider = indexToConsider;
            remainingVals.stream()
                .forEach(val -> currentBitCounts[val.charAt(finalIndexToConsider) - '0']++);
            int keepBit = bitCriteria.apply(currentBitCounts);
            remainingVals.removeIf(val -> (val.charAt(finalIndexToConsider) - '0') != keepBit);
            Arrays.fill(currentBitCounts, 0);
            indexToConsider = (indexToConsider + 1) % diagVals.get(0).length();
        }
        assert remainingVals.size() == 1;
        // System.out.printf("Remaining vals = %s%n", remainingVals);
        return Integer.parseInt(remainingVals.get(0), 2);
    }

    private static int calculateOxygenGeneratorRating(List<String> diagVals) {
        return calculateRatingWithCriteria(diagVals,
            bitCounts -> bitCounts[0] > bitCounts[1] ? 0 : 1);
    }

    private static int calculateCo2ScrubberRating(List<String> diagVals) {
        return calculateRatingWithCriteria(diagVals,
            bitCounts -> bitCounts[1] < bitCounts[0] ? 1 : 0);
    }

    public static void main(String[] args) throws IOException {
        // System.out.printf(
        //    "Power Consumption = %d%n", calculatePowerConsumption("DayThreeInput.txt"));
        System.out.printf(
            "Life Support Rating = %d%n", calculateLifeSupportRating("DayThreeInput.txt"));
    }

}
