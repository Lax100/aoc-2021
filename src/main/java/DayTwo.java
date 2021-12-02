import java.io.IOException;
import java.util.List;

public class DayTwo {

    private enum SubmarineAction {
        FORWARD,
        UP,
        DOWN
    }

    private static class Location {

        int position = 0;
        int depth = 0;

        Location() {
        }

        Location(int position, int depth) {
            this.position = position;
            this.depth = depth;
        }
    }

    private static class Submarine {

        Location location;
        int aim;

        Submarine(Location location, int aim) {
            this.location = location;
            this.aim = aim;
        }

        void drive(SubmarineAction action, int amount) {
            switch (action) {
                case DOWN:
                    this.aim += amount;
                    break;
                case UP:
                    this.aim -= amount;
                    break;
                case FORWARD:
                    this.location.position += amount;
                    this.location.depth += (this.aim * amount);
                    break;
                default:
                    break;
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Submarine submarine = new Submarine(new Location(0, 0), 0);
        List<String> actions = FileUtils.readInputFile("DayTwoInput.txt");
        for (String actionStr : actions) {
            String[] actionParts = actionStr.split(" ");
            SubmarineAction action = SubmarineAction.valueOf(actionParts[0].toUpperCase());
            int amount = Integer.parseInt(actionParts[1]);
            submarine.drive(action, amount);
        }
        System.out.printf(
            "Final location of submarine is (position = %d, depth = %d); (position * depth) = %d%n",
            submarine.location.position,
            submarine.location.depth,
            submarine.location.position * submarine.location.depth);
    }
}
