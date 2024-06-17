// Matthew Lee
// Artificial Intelligence
// TTT #1
// 12/20/22

import java.util.Iterator;
import java.io.Console;

public class TicTacToe {

    public static enum Player {
        ME('X') {
            @Override
            public Player other() {
                return Player.YOU;
            }
        },

        YOU('O') {
            @Override
            public Player other() {
                return Player.ME;
            }
        };

        private final char mark;

        private Player(char mark) {
            this.mark = mark;
        }
        
        public char mark() {
            return this.mark;
        }

        public abstract Player other();
    }

    public static class Action {
        private int row;
        private int col;
        private Player player;

        public Action(int row, int col, Player player) {
            // TODO
            this.row = row;
            this.col = col;
            this.player = player;
        }
        
        @Override
        public String toString() {
            return "Row: " + (row + 1) + "\nCol: " + (col + 1);
        }
    }

    public static class State implements Iterable<Action>, Game.State<Action> {

        public static final int N = 3;
        private Player player;
        private char[][] board;

        public State(Player player) {
            // TODO
            this.player = player;
            this.board = new char[N][N];
            for(int i = 0; i < N; i++) {
                for(int j = 0; j < N; j++) {
                    this.board[i][j] = ' ';
                }
            }
        }

        public State(State state, Action move) {
            // TODO
            this.board = new char[N][N];
            this.player = move.player;
            for(int i = 0; i < N; i++) {
                for(int j = 0; j < N; j++) {
                    this.board[i][j] = state.board[i][j];
                }
            }
            this.board[move.row][move.col] = move.player.mark();
            this.player = this.player.other();
        }
        
        public Player getPlayer() {
            return player;
        }

        public int emptySquares() {
            // TODO
            int counter = 0;
            for(int i = 0; i < N; i++) {
                for(int j = 0; j < N; j++) {
                    if(this.board[i][j] == ' ') counter++;
                }
            }
            return counter;
        }

        public char get(int row, int col) {
            // TODO
            return this.board[row][col];
        }

        public boolean isEmpty(int row, int col) {
            // TODO
            return this.board[row][col] == ' ';
        }

        public boolean wins(Player player) {
            // TODO
            int diagLeftCount = 0;
            int diagRightCount = 0;
            
            //Checking each row
            for(int i = 0; i < N; i++) {
                int count = 0;
                for(int j = 0; j < N; j++) {
                    if(this.board[i][j] == player.mark()) count++;
                }
                if(count == N) return true;
            }
            
            //Checking each col
            for(int i = 0; i < N; i++) {
                int count = 0;
                for(int j = 0; j < N; j++) {
                    if(this.board[j][i] == player.mark()) {
                        count++;
                    }
                }
                if(count == N) return true;

            }
            
            //Checking diag
            for(int i = 0; i < N; i++) {
                if(this.board[N - (i + 1)][i] == player.mark()) diagLeftCount++;
                if(this.board[i][i] == player.mark()) diagRightCount++;
            }
            return diagLeftCount == N || diagRightCount == N;
        }

        @Override
        public boolean isTerminal() {
            // TODO
            return this.emptySquares() == 0 || wins(this.player);
        }

        @Override
        public int evaluate() {
            // TODO

            return 0;
        }
        
        public Iterator<Action> iterator() {
            return new MoveIterator();
        }

        @Override
        public State next(Action action) {
            return new State(this, action);
        }

        @Override
        public Iterable<Action> moves() {
            return new Iterable<Action> () {
                @Override
                public Iterator<Action> iterator() {
                    return State.this.new MoveIterator();
                }
            };
        }

        @Override
        public String toString() {
            String result = "";
            String separator = " ";
            for (int row = 0; row < N; row++) {
                if (row > 0) {
                    result += " \n";
                    result += "---+---+---\n";
                    separator = " ";
                }
                for (int col = 0; col < N; col++) {
                    result += separator;
                    result += get(row, col);
                    separator = " | ";
                }
            }
            result += " \n";
            return result;
        }

        private class MoveIterator implements Iterator<Action> {
            private int row;
            private int col;

            public MoveIterator() {
                this.row = 0;
                this.col = 0;
            }
            
            @Override
            public boolean hasNext() {
                // TODO
                for(int i = row; i < N; i++) {
                    for(int j = col; j < N; j++) {
                        if(isEmpty(i, j)) {
                            row = i;
                            col = j;
                            return true;
                        }
                    }
                    this.col = 0;
                }
                
                return false;
            }

            @Override
            public Action next() {
                // TODO
                return new Action(this.row, this.col++, State.this.player);
            }
        }
    }

    public static void main(String[] args) {
        boolean playerGoingFirst = false;
        TicTacToe.State state = new TicTacToe.State(Player.ME);
        Minimax<Action> minimax = new Minimax<>();
        Console console = System.console();
        
        //Hax = tie or loss from computer
        //L Bozo = computer win
        while (state.emptySquares() > 0) {
            if (state.wins(Player.YOU)) {
                System.out.println("Hax !!!");
                break;
            } else if (state.emptySquares() == 0) {
                System.out.println("Hax !!!");
                break;
            }
            
            if(args.length != 0) {
                if(args[0].toLowerCase().equals("-first") || playerGoingFirst == true) {
                    System.out.println("going second");
                    state = state.next(minimax.bestMove(state));
                    System.out.println(state);
                //Second
                } else playerGoingFirst = true;
            } else {
                state = state.next(minimax.bestMove(state));
                System.out.println(state);
            }

            if (state.wins(Player.ME)) {
                System.out.println("L Bozo !!!");
                break;
            } else if (state.emptySquares() == 0) {
                System.out.println("Hax !!!");
                break;
            }

            int row = 0;
            int col = 0;
            String line = "";
            do {
                do {
                    System.out.println("Enter your move");
                    line = console.readLine("Row: ");
                    try {
                        row = Integer.parseInt(line) - 1;
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid row: " + line);
                    }
                    if (row >= 0 && row < 3) break;
                    System.out.println("Invalid row: " + line);
                } while (true);
                
                do {
                    line = console.readLine("Col: ");
                    try {
                        col = Integer.parseInt(line) - 1;
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid column: " + line);
                    }
                    if (col >= 0 && col < 3) break;
                    System.out.println("Invalid col: " + line);
                } while (true);
        
                if (state.isEmpty(row, col)) break;
                System.out.println("Square is not empty");
            } while (true);

            state = new State(state, new Action(row, col, Player.YOU));
            if(state.emptySquares() == 0) System.out.println("Hax !!!");
        }
    }
}

