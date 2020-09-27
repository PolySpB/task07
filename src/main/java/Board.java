import java.io.*;

/**
 * Класс, описывающий поле, где живут "клетки".
 */

public class Board {
    private Cell[][] board;
    private int height;
    private int width;

    /**
     * В конструкторе считываем начальное состояние игры из файла.
     * @param startState файл, из которого считываем начальное состояние игрового поля
     */
    public Board(String startState) {
        String line = "";
        int height = 0;
        int width = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(startState));) {
            while (br.ready()) {
                line += br.readLine();
                width++;
                height = line.length() / width;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Cell[][] b = new Cell[width][height];

        int charIndex = 0;
        for (int h = 0; h < b.length; h++) {
            for (int w = 0; w < b[h].length; w++) {
                b[h][w] = new Cell();
                if (line.charAt(charIndex++) == '0') {
                    b[h][w].setNewState(false);
                } else {
                    b[h][w].setNewState(true);
                }
            }
        }

        this.board = b;
        this.height = height;
        this.width = width;
    }

    public Board(Cell[][] board, int height, int width) {
        this.board = board;
        this.height = height;
        this.width = width;
    }

    /**
     * Функия, проверяющая жива ли клетка.
     * @param row строка, в которой находится клетка
     * @param col столбец, в котором находится клетка
     * @return true, если клетка жива; false, если клетка метртва
     */
    private boolean isAlive(int row, int col) {
        return board[row][col].getState();
    }

    /**
     * Функция подсчета количества соседей клетки.
     * @param row строка, в которой находится клетка
     * @param col столбец, в котором находится клетка
     * @return количество соседей
     */
    private int neighboursAmount(int row, int col) {
        int sum = 0;
        if (row != 0 && col != 0) {
            if (isAlive(row - 1, col - 1)) {
                sum++;
            }
        }

        if (row != 0) {
            if (isAlive(row - 1, col)) {
                sum++;
            }
        }

        if (row != 0 && col != width - 1) {
            if (isAlive(row - 1, col + 1)) {
                sum++;
            }
        }

        if (col != 0) {
            if (isAlive(row, col - 1)) {
                sum++;
            }
        }

        if (col != width - 1) {
            if (isAlive(row, col + 1)) {
                sum++;
            }
        }

        if (row != height - 1 && col != 0) {
            if (isAlive(row + 1, col - 1)) {
                sum++;
            }
        }

        if (row != height - 1) {
            if (isAlive(row + 1, col)) {
                sum++;
            }
        }

        if (row != height - 1 && col != width - 1) {
            if (isAlive(row + 1, col + 1)) {
                sum++;
            }
        }

        return sum;
    }

    /**
     * Обновление игрового поля.
     */
    public void updateBoard() {
        Cell[][] updatedStates = countCellsNeighbours();
        updateCellsState(updatedStates);
    }

    /**
     * Обновление игрового поля c использованием потоков Thread.
     */
    public void updateBoardMultiThread() {
        Cell[][] updatedStates = countCellsNeighboursUsingThreads();
        updateCellsState(updatedStates);
    }

    /**
     * Функция подсчета соседей каждой клетки.
     * @return таблицу клеток для следующего состояния игры
     */
    private Cell[][] countCellsNeighbours() {
        Cell[][] updatedBoard = new Cell[board.length][board.length];

        for (int h = 0; h < updatedBoard.length; h++) {
            for (int w = 0; w < updatedBoard[h].length; w++) {
                updatedBoard[h][w] = new Cell();
            }
        }

        for (int h = 0; h < board.length; h++) {
            for (int w = 0; w < board[h].length; w++) {
                int nr = neighboursAmount(h, w);
                if (nr < 2 || nr > 3) {
                    updatedBoard[h][w].setNewState(false);
                } else if (nr == 3 || nr == 2) {
                    updatedBoard[h][w].setNewState(true);
                }
            }
        }
        return updatedBoard;
    }

    /**
     * Функция подсчета соседей каждой клетки с использованием потоков Thread.
     * @return таблицу клеток для следующего состояния игры
     */
    private Cell[][] countCellsNeighboursUsingThreads() {

        class CountCellRunnable implements Runnable {
            Cell[][] newBoard;
            int start;
            int end;

            public CountCellRunnable(Cell[][] newBoard, int start, int end) {
                this.newBoard = newBoard;
                this.start = start;
                this.end = end;
            }

            @Override
            public void run() {
                for (int h = start; h < end; h++) {
                    for (int w = 0; w < newBoard[h].length; w++) {
                        newBoard[h][w] = new Cell();
                    }
                }

                for (int h = start; h < end; h++) {
                    for (int w = 0; w < board[h].length; w++) {
                        int nr = neighboursAmount(h, w);
                        if (nr < 2 || nr > 3) {
                            newBoard[h][w].setNewState(false);
                        } else if (nr == 3 || nr == 2) {
                            newBoard[h][w].setNewState(true);
                        }
                    }
                }
            }
        }

        Cell[][] updatedBoard = new Cell[board.length][board.length];

        Thread thread1 = new Thread(new CountCellRunnable(updatedBoard, 0, board.length / 2));
        Thread thread2 = new Thread(new CountCellRunnable(updatedBoard, board.length / 2, board.length));

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return updatedBoard;
    }

    /**
     * Обновляем состояние игры (т.е. обновляем все клетки одновроменно).
     * @param updatedBoard таблица новых состояний клеток
     */
    private void updateCellsState(Cell[][] updatedBoard) {
        for (int h = 0; h < board.length; h++) {
            for (int w = 0; w < board[h].length; w++) {
                boolean state = updatedBoard[h][w].getState();
                board[h][w].setNewState(state);
            }
        }
    }

    /**
     * Вывод текущего состояния игры в консоль.
     */
    public void displayBoard() {
        StringBuilder sb = new StringBuilder("--");
        for (int i = 0; i < board.length; i++) {
            sb.append("--");
        }
        System.out.println(sb.toString());

        for (Cell[] element : board) {
            String r = "|";
            for (Cell c : element) {
                r += c.getState() ? "* " : "  ";
            }
            r += "|";
            System.out.println(r);
        }

        System.out.println(sb.toString());
    }

    /**
     * Сохранение состояния игры в файл.
     * @param endState файл, хранящий конечное состояние игры
     * @throws IOException
     */
    public void saveState(String endState) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(endState))) {

            for (int h = 0; h < board.length; h++) {
                for (int w = 0; w < board[h].length; w++) {
                    if (board[h][w].getState()) {
                        bw.append('1');
                    } else {
                        bw.append('0');
                    }
                    if (w == board[h].length - 1) {
                        bw.append('\n');
                    }
                }
            }
        }

    }

}
