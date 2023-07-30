package ChessBoard;

import ChessPieces.*;

/**
 * @author Michael Mogilevsky (mm3201)
 * @author Simran Sareen (ss3825)
 */

/**
 * Class to create and display the ChessBoard, an 8x8 array of ChessPieces.
 * Each ChessPiece on the board can be referened by their rank (row) and file
 * (column) index.
 */
public class ChessBoard {

    /**
     * N represents the standard size of a 8x8 ChessBoard
     */
    public static final int N = 8;

    /**
     * Displays header showing authors and project name.
     */
    public static void displayHeader() {
        System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
        System.out.println();
        System.out.println("                CHESS                      ");
        System.out.println("  by Michael Mogilevsky, Simran Sareen     ");
        System.out.println();
        System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
        System.out.println();
    }

    /**
     * Create the chess board with all respective pieces starting in their start
     * positions.
     * 
     * @return A 2D array of ChessPieces representing all dynamic types of chess
     *         pieces.
     */
    public static ChessPiece[][] createBoard() {
        ChessPiece[][] board = new ChessPiece[N][N];
        // Set up white pieces
        for (int i = 0; i < N; i++) {
            board[6][i] = new Pawn(ChessPiece.WHITE, 6, i);
        }
        board[7][0] = new Rook(ChessPiece.WHITE, 7, 0);
        board[7][1] = new Knight(ChessPiece.WHITE, 7, 1);
        board[7][2] = new Bishop(ChessPiece.WHITE, 7, 2);
        board[7][3] = new Queen(ChessPiece.WHITE, 7, 3);
        board[7][4] = new King(ChessPiece.WHITE, 7, 4);
        board[7][5] = new Bishop(ChessPiece.WHITE, 7, 5);
        board[7][6] = new Knight(ChessPiece.WHITE, 7, 6);
        board[7][7] = new Rook(ChessPiece.WHITE, 7, 7);

        // Set up black pieces
        for (int i = 0; i < N; i++) {
            board[1][i] = new Pawn(ChessPiece.BLACK, 1, i);
        }
        board[0][0] = new Rook(ChessPiece.BLACK, 0, 0);
        board[0][1] = new Knight(ChessPiece.BLACK, 0, 1);
        board[0][2] = new Bishop(ChessPiece.BLACK, 0, 2);
        board[0][3] = new Queen(ChessPiece.BLACK, 0, 3);
        board[0][4] = new King(ChessPiece.BLACK, 0, 4);
        board[0][5] = new Bishop(ChessPiece.BLACK, 0, 5);
        board[0][6] = new Knight(ChessPiece.BLACK, 0, 6);
        board[0][7] = new Rook(ChessPiece.BLACK, 0, 7);

        return board;
    }

    /**
     * Check if spot at (r,f) is not occupied on the current chess board instance.
     * 
     * @param r     The rank (row) index in the 2D array chess board
     * @param f     The file (column) index in the 2D array chess board
     * @param board The instance of 2D array chess board of ChessPieces
     * @return True if the square at (r,f) in board is not occupied, False otherwise
     */
    public static boolean notOccupied(int r, int f, ChessPiece[][] board) {
        return (r > 7 || r < 0 || f < 0 || f > 7) || board[r][f] == null;
    }

    /**
     * Print out the correct chess board pattern ("##" represents a black square, "
     * " represents a white square.)
     * 
     * @param r The rank (row) on the board
     * @param f The file (column) on the board
     */
    public static void printSquare(int r, int f) {
        if (r % 2 == 0) {
            if (f % 2 == 1) {
                System.out.print("##");
            } else {
                System.out.print("  ");
            }
        } else {
            if (f % 2 == 0) {
                System.out.print("##");
            } else {
                System.out.print("  ");
            }
        }
    }

    /**
     * Prints the entire chess board with square pattern and dynamic instances of
     * ChessPiece in their current location
     * 
     * @param board The 2D array of ChessPieces
     */
    public static void printBoard(ChessPiece[][] board) {
        for (int r = 0; r < N; r++) {
            for (int f = 0; f < N; f++) {
                if (notOccupied(r, f, board)) {
                    printSquare(r, f);
                    System.out.print(" ");
                } else {
                    System.out.print(board[r][f]);
                    System.out.print(" ");
                }
            }
            System.out.println(N - r);
        }

        // letter starts at 'a' (ascii value)
        int letter = 97;
        for (int i = 0; i < 24; i++) {
            if ((i - 1) % 3 == 0) {
                System.out.print((char) letter);
                letter++;
            } else {
                System.out.print(" ");
            }
        }

        System.out.println();
    }

    /**
     * Performs a deep copy of the board (i.e. creates new instances of all
     * ChessPieces at their current locations in board)
     * 
     * @param board The 2D array of ChessPieces to clone.
     * @return A deep copy clone of the input chess board (another 2D array of
     *         ChessPieces)
     */
    public static ChessPiece[][] cloneBoard(ChessPiece[][] board) {

        ChessPiece[][] ret = new ChessPiece[N][N];
        for (int r = 0; r < N; r++) {
            for (int f = 0; f < N; f++) {
                ChessPiece p = board[r][f];
                if (p == null)
                    ret[r][f] = null;
                else {
                    int t = p.getTeamColor();
                    if (p instanceof Pawn) {
                        ret[r][f] = new Pawn(t, r, f);
                    } else if (p instanceof Bishop) {
                        ret[r][f] = new Bishop(t, r, f);
                    } else if (p instanceof Knight) {
                        ret[r][f] = new Knight(t, r, f);
                    } else if (p instanceof Queen) {
                        ret[r][f] = new Queen(t, r, f);
                    } else if (p instanceof King) {
                        ret[r][f] = new King(t, r, f);
                    } else if (p instanceof Rook) {
                        ret[r][f] = new Rook(t, r, f);
                    }
                }
            }
        }

        return ret;
    }
}
