/**
 * Класс, описывающий "Клетку".
 */
public class Cell {
    private boolean state = false;

    public Cell() {
    }

    public Cell(boolean state) {
        this.state = state;
    }

    public boolean getState() {
        return state;
    }

    public void setNewState(boolean newState) {
        state = newState;
    }

}
