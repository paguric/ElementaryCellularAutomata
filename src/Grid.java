public class Grid {
    private static final int DEFAULT_RULE = 90; // up to 255
    public static final int ROWS = 200;
    public static final int COLUMNS = 777;
    public boolean[][] grid;
    private int[] rule;
    private static int currentRow;
    public Grid() {
        this.grid = new boolean[ROWS][COLUMNS];
        this.initializeFirstGeneration();
        rule = decimalToBinary(DEFAULT_RULE);

        // adds leading zeros to the rule array if it has less than 8 elements
        if (rule.length < 8) {
            int zeros = 8 -rule.length;
            int[] rule = new int[8];
            for (int i = 0; i < zeros; i++) {
                rule[i] = 0;
            }
            for (int i = zeros; i < 8; i++) {
                rule[i] = this.rule[i -zeros];
            }
            this.rule = rule;
        }

        // invert the rule array
        for (int i = 0; i < 4; i++) {
            int temp = this.rule[i];
            this.rule[i] = this.rule[8 -(1 +i)];
            this.rule[8 -(1 +i)] = temp;
        }

        currentRow = 0;
    }

    // default starting generation: a single cell in the middle of the first row is set to true (alive)
    private void initializeFirstGeneration() {
        grid[0][COLUMNS /2] = true;
    }

    private static int[] decimalToBinary(int decimalValue) {
        int digits = 0;
        int n = decimalValue;
        do {
            n /= 2;
            digits++;
        } while(n != 0);

        int[] binaryRepresentation = new int[digits];
        do {
            binaryRepresentation[--digits] = decimalValue % 2;
            decimalValue = (decimalValue - decimalValue % 2) / 2;
        } while(decimalValue != 0);

        return binaryRepresentation;
    }

    private static int binaryToDecimal(int[] binaryRepresentation) {
        int digits = binaryRepresentation.length;
        int decimalValue = 0;
        for (int i = 0; i < digits; i++) {
            int base = 2 *binaryRepresentation[i];
            if (base == 0) continue;
            decimalValue += (int) Math.pow(base, digits -(1 + i));
        }
        return decimalValue;
    }

    public void nextGeneration() {
        if (currentRow >= ROWS -1) return;
        currentRow += 1;
        // default condition: first and last cell are always false (dead)
        grid[currentRow][0] = false;
        grid[currentRow][COLUMNS -1] = false;

        for (int i = 1; i < COLUMNS -2; i++) {  // skips 2 already set cells
            int[] clusterState = new int[3];    // saves left neighbor, current cell and right neighbor
            for (int j = -1; j < 2; j ++) {
                clusterState[j +1] = grid[currentRow -1][i +j] ? 1 : 0;
            }
            int instruction = binaryToDecimal(clusterState);
            grid[currentRow][i] = rule[instruction] == 1;
        }
    }

    @Override
    public String toString() {
        String s = "";
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                s += grid[i][j] ? 1 : " ";
            }
            if (i != ROWS -1) s += "\n";
        }
        return s;
    }
}