package GameLogic;

import java.util.Arrays;

/**
 * main board class which has the implementation of the game logic
 */
public class Board {

    /**
     * a string used to add spaces in the visual representation of the board
     */
    private static final String DELIM = "     ";
    /**
     * representation of the board
     */
    public static final int DIM = 6;
    /**
     * a string used to add spaces in the visual representation of the board
     */
    private static final String SEPARATOR = "----+----+----+----+----+----";
    private static final String[] NUMBERING = {"    0 | 1  | 2  | 3  | 4  | 5 ", SEPARATOR,
            "    6 | 7  | 8  | 9  | 10 | 11 ", SEPARATOR, "   12 | 13 | 14 | 15 | 16 | 17 ",
            SEPARATOR, "   18 | 19 | 20 | 21 | 22 | 23 ", SEPARATOR, "   24 | 25 | 26 | 27 | 28 | 29 ",
            SEPARATOR, "   30 | 31 | 32 | 33 | 34 | 35 "};
    /**
     * a string used to add spaces in the visual representation of the board
     */
    private static final String LINE = NUMBERING[1];

    /**
     * an integer for the whole field of the board from 0 to 36
     * protected to make it easier to access this field in the same package(for the computerPlayers)
     */
    protected int[] field;
    /**
     * a sub board of the main board (topLeft)
     */
    private int[] topLeft = new int[9];
    /**
     * a sub board of the main board (ropRight)
     */
    private int[] topRight = new int[9];
    /**
     * a sub board of the main board (bottomLeft)
     */
    private int[] bottomLeft = new int[9];
    /**
     * a sub board of the main board (bottomRight)
     */
    private int[] bottomRight = new int[9];

    /**
     * and integer made to represent a player mark
     */
    int playerMark;

    /**
     * Constructor for the board initialises an empty board
     */
    public Board() {
        this.field = new int[36];
        Arrays.fill(field, 0);
        fillSmallerTables();
    }

    /**
     * returns the valid index of the field (-1 if not valid)
     *
     * @param x coordinate x
     * @param y coordinate y
     * @return returns field[pos(x,y)] which will be the index of the field
     */
    //@ ensures x>=0 && x<6 && y>=0 && y<6;
    public int get(int x, int y) {
        if (x >= 0 && x < 6 && y >= 0 && y < 6) return field[pos(x, y)];
        else return -1;
    }

    /**
     * using the formula to calculate the index of the field with row and column
     * this method returns the index of the field
     *
     * @param x row
     * @param y column
     * @return calculates the index of the field
     */
    //@ ensures \result == y*6+x;
    public int pos(int x, int y) {
        return y * 6 + x;
    }

    /**
     * returns the valid index of the field, -1 if the index is not valid
     *
     * @param z index of the field
     * @return z if it is a valid index
     */
    //@ ensures z>=0 && z<36;
    public int checkMove(int z) {
        if (z >= 0 && z < 36) return z;
        else return -1;
    }

    /**
     * method places down the mark on the field and rotates a sub board
     *
     * @param z          index of the field
     * @param rotation   the indicator to which sub board to rotate and which way
     * @param playerMark the current player mark 1 or 2
     */
    //@ ensures field[checkMove(z)] != \old(field[checkMove(z)]);
    public void placeMark(int z, int rotation, int playerMark) {
        field[checkMove(z)] = playerMark;
        fillSmallerTables();
        rotateBoard(rotation);
    }

    /**
     * this method rotates the wanted sub board which ever way was declared
     *
     * @param b variable that decides which sub board to rotate and which way
     */
    //@ requires b>=0 && b<=7;
    public void rotateBoard(int b) {
        if (b == 0) {
            moveCounterClockWise(topLeft);
        } else if (b == 1) {
            moveClockWise(topLeft);
        } else if (b == 2) {
            moveCounterClockWise(topRight);
        } else if (b == 3) {
            moveClockWise(topRight);
        } else if (b == 4) {
            moveCounterClockWise(bottomLeft);
        } else if (b == 5) {
            moveClockWise(bottomLeft);
        } else if (b == 6) {
            moveCounterClockWise(bottomRight);
        } else if (b == 7) {
            moveClockWise(bottomRight);
        }
    }

    /**
     * this methods takes a sub board and rotates the indexes counter clock wise
     *
     * @param arr the given sub board to rotate counter clock wise
     */
    //@ requires arr != null;
    public void moveCounterClockWise(int arr[]) {
        int temp1, temp2, temp3;
        temp1 = arr[0];
        arr[0] = arr[2];
        temp2 = arr[3];
        arr[3] = arr[1];
        temp3 = arr[6];
        arr[6] = temp1;
        temp1 = arr[7];
        arr[7] = temp2;
        temp2 = arr[8];
        arr[8] = temp3;
        temp3 = arr[5];
        arr[5] = temp1;
        arr[2] = temp2;
        arr[1] = temp3;
        fillMainTableWithSmallerOnes();
    }

    /**
     * this methods takes a sub board and rotates the indexes coutner clock wise
     *
     * @param arr the given sub board to rotate counter clock wise
     */
    //@ requires arr != null;
    public void moveClockWise(int arr[]) {
        int temp1, temp2, temp3;
        temp1 = arr[0];
        arr[0] = arr[6];
        temp2 = arr[1];
        arr[1] = arr[3];
        temp3 = arr[2];
        arr[2] = temp1;
        temp1 = arr[5];
        arr[5] = temp2;
        temp2 = arr[8];
        arr[8] = temp3;
        temp3 = arr[7];
        arr[7] = temp1;
        arr[6] = temp2;
        arr[3] = temp3;
        fillMainTableWithSmallerOnes();
    }

    /**
     * this method checks for a win case (5 of the same marks in a row)
     *
     * @param playerMark the current player mark 1 or 2
     * @return returns true when the nested for loop finds a win case 5 in a row and false if it does not
     */
    //@ requires playerMark != 0;
    public boolean wins(int playerMark) {
        for (int x = 0; x < 6; x++) {
            for (int y = 0; y < 6; y++) {
                if (get(x, y) == playerMark) {
                    // checks for vertical win
                    if (get(x + 1, y) == playerMark && get(x + 2, y) == playerMark &&
                            get(x + 3, y) == playerMark && get(x + 4, y) == playerMark) return true;
                    // checks for horizontal win
                    if (get(x, y + 1) == playerMark && get(x, y + 2) == playerMark &&
                            get(x, y + 3) == playerMark && get(x, y + 4) == playerMark) return true;
                    // checks for diagonal win right-up
                    if (get(x - 1, y + 1) == playerMark && get(x - 2, y + 2) == playerMark &&
                            get(x - 3, y + 3) == playerMark &&
                            get(x - 4, y + 4) == playerMark) return true;
                    // checks for diagonal win left-up
                    if (get(x - 1, y - 1) == playerMark && get(x - 2, y - 2) == playerMark &&
                            get(x - 3, y - 3) == playerMark &&
                            get(x - 4, y - 4) == playerMark) return true;
                }
            }
        }
        return false;
    }

    /**
     * method determines weather or not the given index is empty (0 means it is empty)
     *
     * @param z the index of the field
     * @return returns true if the declared index is 0 (meaning empty)
     */
    //@ requires z >=0 && z<36;
    public boolean isEmpty(int z) {
        return field[z] == 0;
    }

    /**
     * method determines weather or not the bard is full
     *
     * @return true if there are no indexes with the value of 0 (0 meaning it is empty)
     */
    //@requires field != null;
    public boolean isFull() {
        for (int j : field) {
            if (j == 0) return false;
        }
        return true;
    }

    /**
     * method determines weather there is a case for draw
     *
     * @return true if there is a case of a full board or both players winning after a rotation
     */
    //@ requires field != null;
    //@ ensures isFull() == true || (wins(1) == true && wins(2) == true);
    public boolean Draw() {
        return isFull() || (wins(1) && wins(2));
    }

    /**
     * resets the board, sets all the values of the field to 0
     */
    //@ requires field != null;
    //@ ensures field != \old(field);
    public void reset() {
        for (int i = 0; i < 36; i++) {
            field[i] = 0;
        }
    }

    /**
     * method determines weather the given player mark is set 5 times in a row , meaning a win
     *
     * @param playerMark the mark of the current player
     * @return true if there is a case of 5 in a row of the same marks
     */
    //@ requires field != null;
    //@ ensures wins(playerMark) == true;
    public boolean isWinner(int playerMark) {
        return wins(playerMark);
    }

    /**
     * method determines weather or not the board has a winner
     *
     * @return true if any mark is set in a row of 5 meaning one of the players won
     */
    //@ requires field != null;
    //@ ensures wins(1) == true || wins(2) == true;
    public boolean hasWinner() {
        return isWinner(1) || isWinner(2);
    }

    /**
     * method determines weather or not the game is full or has a winner to indicate that the game is over
     *
     * @return true if the board is full or the game has a winner
     */
    //@ requires field != null;
    //@ ensures isFull() == true || hasWinner() == true;
    public boolean gameOver() {
        return isFull() || hasWinner();
    }


    /**
     * this method makes a toString to print out the board with the indexes and board with picked moves
     *
     * @return s which is the formatted board (visual representation)
     */
    @Override
    public String toString() {
        String s = "";
        for (int i = 0; i < DIM; i++) {
            String row = " ";
            for (int j = 0; j < DIM; j++) {
                row = row + "" + get(j, i);
                if (j < DIM - 1) {
                    row = row + "  | ";
                }
            }
            s = s + row + DELIM + NUMBERING[i * 2];
            if (i < DIM - 1) {
                s = s + "\n" + LINE + DELIM + NUMBERING[i * 2 + 1] + "\n";
            }
        }
        return s;
    }


    /**
     * this method fills the 4 sub boards of the board and fills them up with according indexes
     */
    public void fillSmallerTables() {
        // fill the topLeft
        int index = 0;
        for (int y = 0; y < DIM / 2; y++) {
            for (int x = 0; x < DIM / 2; x++) {
                topLeft[index] = field[pos(x, y)];
                index++;
            }
        }

        // fills the topRight
        index = 0;
        for (int y = 0; y < DIM / 2; y++) {// 3 1
            for (int x = DIM / 2; x < DIM; x++) {
                topRight[index] = field[pos(x, y)];
                index++;
            }
        }


        // fills the bottomLeft
        index = 0;
        for (int y = DIM / 2; y < DIM; y++) {
            for (int x = 0; x < DIM / 2; x++) {
                bottomLeft[index] = field[pos(x, y)];
                index++;
            }
        }

        //fills the bottomRight
        index = 0;
        for (int y = DIM / 2; y < DIM; y++) {
            for (int x = DIM / 2; x < DIM; x++) {
                bottomRight[index] = field[pos(x, y)];
                index++;
            }
        }
    }

    /**
     * this method fills up the main board with the newly rotated sub boards
     */
    public void fillMainTableWithSmallerOnes() {
        // fills with the topLeft rotated moves
        int index = 0;
        for (int y = 0; y < DIM / 2; y++) {
            for (int x = 0; x < DIM / 2; x++) {
                field[pos(x, y)] = topLeft[index];
                index++;
            }
        }

        // fills with the topRight rotated moves
        index = 0;
        for (int y = 0; y < DIM / 2; y++) {
            for (int x = DIM / 2; x < DIM; x++) {
                field[pos(x, y)] = topRight[index];
                index++;
            }
        }

        // fills with the bottomLeft rotated moves
        index = 0;
        for (int y = DIM / 2; y < DIM; y++) {
            for (int x = 0; x < DIM / 2; x++) {
                field[pos(x, y)] = bottomLeft[index];
                index++;
            }
        }

        // fills with the bottomRight rotated moves
        index = 0;
        for (int y = DIM / 2; y < DIM; y++) {
            for (int x = DIM / 2; x < DIM; x++) {
                field[pos(x, y)] = bottomRight[index];
                index++;
            }
        }
    }


}
