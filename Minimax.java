// Matthew Lee
// Artificial Intelligence
// TTT #1
// 12/20/22

public class Minimax<Action> {

    public TicTacToe.Action bestMove(TicTacToe.State state) {
        // TODO
        int value = -2;
        TicTacToe.Action bestMove = null;
        
        for(TicTacToe.Action action : state) {
            int temp = minValue(state.next(action));
            if(temp > value) {
                value = temp;
                bestMove = action;
            }
        }
        return bestMove;
    }

    public int maxValue(TicTacToe.State state) {
        // TODO
        if(state.isTerminal() == true) {
            if(state.wins(state.getPlayer())) return 1;
            else if(state.wins(state.getPlayer().other())) return -1;
            else return 0;
        }
        
        int value = -2;
        for(TicTacToe.Action action : state) {
            int temp = minValue(state.next(action));
            if(temp > value) {
                value = temp;
            }
        }
        return value;
    }

    //Opposite of max
    public int minValue(TicTacToe.State state) {
        // TODO
        if(state.isTerminal() == true) {
            if(state.wins(state.getPlayer())) return -1;
            else if(state.wins(state.getPlayer().other())) return 1;
            else return 0;
        }
        
        int value = 2;
        for(TicTacToe.Action action : state) {
            int temp = maxValue(state.next(action));
            if(temp < value) {
                value = temp;
            }
        }
        return value;
    }
}
