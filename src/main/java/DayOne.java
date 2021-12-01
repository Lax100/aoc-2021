import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class DayOne {

    private static int sonarSweep(String fileName) throws IOException {
        List<String> input = FileUtils.readInputFile(fileName);
        int numDepthIncreases = 0;
        for (int i = 1; i < input.size(); i++) {
            if (Integer.valueOf(input.get(i)) > Integer.valueOf(input.get(i - 1))) {
                numDepthIncreases++;
            }
        }
        return numDepthIncreases;
    }

    private static int sonarSweepWithSlidingWindow(String fileName) throws IOException {
        List<String> input = FileUtils.readInputFile(fileName);
        List<Integer> inputInts = input.stream().map(Integer::valueOf).collect(Collectors.toList());
        int numWindowSumIncreases = 0;
        int prevSum =
            inputInts.size() > 2 ? inputInts.get(0) + inputInts.get(1) + inputInts.get(2) : 0;
        // Consider (i-1, i, i+1) sliding window sums
        for (int i = 1; i < inputInts.size() - 1; i++) {
            int currSum = inputInts.get(i - 1) + inputInts.get(i) + inputInts.get(i + 1);
            if (currSum > prevSum) {
                numWindowSumIncreases++;
            }
            prevSum = currSum;
        }
        return numWindowSumIncreases;
    }


    public static void main(String[] args) {
        try {
            /*
            System.out.println(String
                .format("Number of depth increases in sonar sweep report is: %d", sonarSweep(
                    "DayOneInput.txt")));
             */
            System.out.println(String
                .format("Number of sliding window depth sum increases in sonar sweep report is: %d",
                    sonarSweepWithSlidingWindow(
                        "DayOnePartTwoInput.txt")));
        } catch (IOException e) {
            System.err.println("Could not find input file!");
        }
    }

}
