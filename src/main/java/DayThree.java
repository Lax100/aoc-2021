import java.io.IOException;
import java.util.List;

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

    public static void main(String[] args) throws IOException {
        System.out.printf(
            "Power Consumption = %d%n", calculatePowerConsumption("DayThreeInput.txt"));
    }

}
