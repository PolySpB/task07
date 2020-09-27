import org.junit.jupiter.api.BeforeEach;

class BoardTest {
    Board board;

    @BeforeEach
    public void setUp() {
        int height = 10;
        int width = 10;
        Cell[][] b = new Cell[height][width];
        for (int h = 0; h < b.length; h++) {
            for (int w = 0; w < b[h].length ; w++) {
                if ((h + w) % 4 == 0) {
                    b[h][w] = new Cell();
                    b[h][w].setNewState(true);
                }
                else {
                    b[h][w] = new Cell();
                    b[h][w].setNewState(false);
                }
            }
        }
        board = new Board(b, height, width);
    }


    @org.junit.jupiter.api.Test
    void updateBoardMultiThread() {
        long start = System.currentTimeMillis();
        board.updateBoardMultiThread();
        long end = System.currentTimeMillis();

        long duration = end - start;

        System.out.println("Start:\t" + start);
        System.out.println("End:\t" + end);
        System.out.println("Duration: " + duration);
    }

    @org.junit.jupiter.api.Test
    void updateBoard() {
        long start = System.currentTimeMillis();
        board.updateBoard();
        long end = System.currentTimeMillis();

        long duration = end - start;

        System.out.println("Start:\t" + start);
        System.out.println("End:\t" + end);
        System.out.println("Duration: " + duration);
    }

}