/**
 * Реализация игры "Жизнь".
 * Для запуска использовать следующую конфигурацию:
 * 1 аргумент - файл, хранящий начальное состояние игры
 * 2 аргумент - файл, в который сохраняем конечное состояние игры
 * 3 аргумент - число итераций игры
 * Пример: D:\task07\startState.txt D:\task07\endState.txt 10
 * В качестве многопоточной версии игры реализован метод countCellsNeighboursUsingThreads() -
 * считающий соседей каждой клетки с использованием потоков Thread.
 */

public class Main {

    public static void main(String[] args) throws Exception {
        String startState = args[0];
        String endState = args[1];
        int iterationsAmount = Integer.parseInt(args[2]);

        Board board = new Board(startState);

        for (int i = 0; i < iterationsAmount; i++) {
            if (i == iterationsAmount - 1) {
                board.saveState(endState);
            }

            board.displayBoard();
            board.updateBoard();
            // board.updateBoardMultiThread();
        }

    }

}
