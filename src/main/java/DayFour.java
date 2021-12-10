import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Stream;

public class DayFour {

    // Bingo inputs are positive; hack
    private static final int MARKED = -1;

    static class EvalResult {

        int score;
        List<int[][]> winningBoards;

        EvalResult(int score, List<int[][]> winningBoards) {
            this.score = score;
            this.winningBoards = winningBoards;
        }
    }

    private static int solveBingoInput(String fileName) throws IOException {
        List<String> bingoInput = FileUtils.readInputFile(fileName);
        ListIterator<String> listIter = bingoInput.listIterator();
        int lastWinningScore = -1;
        if (listIter.hasNext()) {
            List<int[][]> bingoBoards = new ArrayList<>();
            int[] bingoNumbers = Stream.of(listIter.next().split(",")).mapToInt(Integer::parseInt)
                .toArray();

            int row = 0;
            int[][] board = new int[5][5];
            while (listIter.hasNext()) {
                String line = listIter.next();
                // System.out.printf("Processing line %s%n", line);
                if (line == null || line.trim().isEmpty()) {
                    board = new int[5][5];
                    row = 0;
                    continue;
                }
                board[row] = Stream.of(line.split(" "))
                    .filter(s -> !s.isEmpty())
                    .map(String::trim)
                    .mapToInt(Integer::parseInt)
                    .toArray();
                row++;
                if (row == 4) {
                    bingoBoards.add(board);
                }
            }
            // System.out.printf("Bingo boards at the beginning of game are: %s%n",
            //    Arrays.deepToString(bingoBoards.toArray()));

            for (int num : bingoNumbers) {
                EvalResult evalResult = eval(bingoBoards, num);
                // System.out.println(Arrays.deepToString(bingoBoards.toArray()));
                int score = evalResult.score;
                if (score != -1) {
                    lastWinningScore = score;
                    bingoBoards.removeAll(evalResult.winningBoards);
                    // break;
                }
            }
        }
        return lastWinningScore;
    }

    // TODO: Separate marking from eval?
    // This is pretty hacky right now
    private static EvalResult eval(List<int[][]> bingoBoards, int num) {
        // System.out.printf("Calling number %d...%n", num);
        EvalResult lastEvalResult = new EvalResult(-1, null);
        for (int boardIndex = 0; boardIndex < bingoBoards.size(); boardIndex++) {
            int[][] b = bingoBoards.get(boardIndex);
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    if (b[i][j] == num) {
                        b[i][j] = MARKED;
                        // Check if whole row or whole column marked
                        boolean isRowBingo = true;
                        for (int k = 0; k < 5; k++) {
                            if (b[i][k] != MARKED) {
                                isRowBingo = false;
                                break;
                            }
                        }
                        boolean isColumnBingo = true;
                        for (int k = 0; k < 5; k++) {
                            if (b[k][j] != MARKED) {
                                isColumnBingo = false;
                                break;
                            }
                        }
                        if (isRowBingo || isColumnBingo) {
                            System.out
                                .printf("Number... %d; BINGO! Board %d won!%n", num, boardIndex);
                            System.out.println(Arrays.deepToString(b));
                            List<int[][]> winningBoards =
                                lastEvalResult.winningBoards == null ? new ArrayList<>()
                                    : lastEvalResult.winningBoards;
                            winningBoards.add(b);
                            lastEvalResult = new EvalResult(
                                calculateBingoBoardScore(b) * num,
                                winningBoards);
                        }
                    }
                }
            }
        }
        return lastEvalResult;
    }

    private static int calculateBingoBoardScore(int[][] bingoBoard) {
        return Arrays.stream(bingoBoard).flatMapToInt(Arrays::stream).filter(n -> n != MARKED)
            .sum();
    }

    public static void main(String[] args) throws IOException {
        System.out.printf("Bing score is: %d%n", solveBingoInput("DayFourInput.txt"));
    }

}