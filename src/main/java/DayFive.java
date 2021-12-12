import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DayFive {

    private static final Pattern LINE_PATTERN = Pattern
        .compile("(\\d+),\\s*(\\d+)\\s+->\\s+(\\d+),\\s*(\\d+)");

    private static class Line {

        int x1;
        int y1;
        int x2;
        int y2;

        Line(int x1, int y1, int x2, int y2) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }

        @Override
        public String toString() {
            return String.format("(%d, %d) -> (%d, %d)", x1, x2, y1, y2);
        }
    }

    private static int calcNumPointsWhereLinesOverlap(String fileName) throws IOException {
        List<Line> lines = FileUtils.readInputFile(fileName).parallelStream()
            .map(s -> {
                Matcher m = LINE_PATTERN.matcher(s);
                m.matches();
                MatchResult matchResult = m.toMatchResult();
                return new Line(Integer.parseInt(matchResult.group(1)),
                    Integer.parseInt(matchResult.group(2)),
                    Integer.parseInt(matchResult.group(3)), Integer.parseInt(matchResult.group(4)));
            })
            .filter(l -> (l.x1 == l.x2) || (l.y1 == l.y2)
                || Math.abs(l.y2 - l.y1) / Math.abs(l.x2 - l.x1) == 1)
            .collect(Collectors.toList());
        // System.out.printf("Valid Lines: %s%n", lines);

        int floorWidth = getFloorWidth(lines);
        int[][] oceanFloor = new int[floorWidth][floorWidth];

        lines.stream().forEach(l -> fillOceanFloor(oceanFloor, l));
        // System.out.printf("Ocean floor is: %s%n", Arrays.deepToString(oceanFloor));

        return (int) Arrays.stream(oceanFloor).flatMapToInt(Arrays::stream).filter(i -> i >= 2)
            .count();
    }

    private static void fillOceanFloor(int[][] oceanFloor, Line l) {
        if (l.x1 == l.x2) { // vertical line
            int y1 = Math.min(l.y1, l.y2);
            int y2 = Math.max(l.y1, l.y2);
            for (int i = y1; i <= y2; i++) {
                oceanFloor[l.x1][i]++;
            }
        } else if (l.y1 == l.y2) { // horizontal line
            int x1 = Math.min(l.x1, l.x2);
            int x2 = Math.max(l.x1, l.x2);
            for (int i = x1; i <= x2; i++) {
                oceanFloor[i][l.y1]++;
            }
        } else if (Math.abs(l.y2 - l.y1) / Math.abs(l.x2 - l.x1) == 1) { // 45 deg diagonal line
            int xDelta = l.x2 > l.x1 ? 1 : -1;
            int yDelta = l.y2 > l.y1 ? 1 : -1;
            int x = l.x1;
            int y = l.y1;
            do {
                oceanFloor[x][y]++;
                x += xDelta;
                y += yDelta;
            } while (x != l.x2 + xDelta);
        } else {
            throw new IllegalArgumentException(String.format("Line %s is not valid!%n", l));
        }
    }

    private static int getFloorWidth(List<Line> lines) {
        int maxCoord = Integer.MIN_VALUE;
        for (Line l : lines) {
            maxCoord = Math.max(maxCoord, Math.max(Math.max(l.x1, l.x2), Math.max(l.y1, l.y2)));
        }
        return maxCoord + 1;
    }

    public static void main(String[] args) throws IOException {
        System.out.printf("Num points where more than two lines overlap is %d%n",
            calcNumPointsWhereLinesOverlap("DayFiveInput.txt"));
    }

}
