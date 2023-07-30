package ChessPieces;

import ChessBoard.ChessBoard;

/**
 * @author Michael Mogilevsky (mm3201)
 * @author Simran Sareen (ss3825)
 */

/**
 * Class that models a Rook piece, inherit from ChessPiece abstract class
 */
public class Rook extends ChessPiece {

    /**
     * 3-Arg constructor for a Rook
     * 
     * @param teamColor color of team of Rook piece
     * @param r         Rank (row) coordinate of Rook
     * @param f         File (column) coordinate of Rook
     */
    public Rook(int teamColor, int r, int f) {
        super(teamColor, r, f);
    }
    // 1 = north
    // 2 = south
    // 3 = east
    // 4 = west

    @Override
    public boolean canMove(int newR, int newF, ChessPiece[][] chessBoard) {
        int rp = this.r;
        int fp = this.f;

        int distr = Math.abs(rp - newR);
        int distf = Math.abs(fp - newF);

        int rx = newR - rp;
        int fy = newF - fp;

        if (distr != 0 && distf == 0) { // north south
            if (rx < 0) { // north
                return canRookMove(chessBoard, rp, fp, newR, newF, 1);
            } else { // south
                return canRookMove(chessBoard, rp, fp, newR, newF, 2);
            }
        } else if (distr == 0 && distf != 0) { // east west
            if (fy < 0) { // west
                return canRookMove(chessBoard, rp, fp, newR, newF, 4);
            } else { // east
                return canRookMove(chessBoard, rp, fp, newR, newF, 3);
            }
        }
        return false;
    }

    /**
     * Validate if Rook can move to new position
     * 
     * @param chessBoard 2D array of ChessPieces representing chess board for the
     *                   game
     * @param rp         Old rank (row) coordinate of Rook
     * @param fp         Old file (column) coordinate of Rook
     * @param r          New rank (row) coordinate of Rook
     * @param f          New file (column) coordinate of Rook
     * @param direction  Direction Rook moves in
     * @return True if Rook can move to new position, False otherwise
     */
    public boolean canRookMove(ChessPiece[][] chessBoard, int rp, int fp, int r, int f, int direction) {
        switch (direction) {
            case 1:
                return moveNorthSouth(chessBoard, rp, fp, r, f, direction);
            case 2:
                return moveNorthSouth(chessBoard, rp, fp, r, f, direction);
            case 3:
                return moveEastWest(chessBoard, rp, fp, r, f, direction);
            case 4:
                return moveEastWest(chessBoard, rp, fp, r, f, direction);
            default:
                return false;
        }

    }

    /**
     * Check if Rook can move North/South directions
     * 
     * @param chessBoard 2D array of ChessPieces representing chess board for the
     *                   game
     * @param rp         Old rank (row) coordinate of Rook
     * @param fp         Old file (column) coordinate of Rook
     * @param r          New rank (row) coordinate of Rook
     * @param f          New file (column) coordinate of Rook
     * @param direction  Direction Rook moves in
     * @return True if Rook can move to new position, False otherwise
     */
    public boolean moveNorthSouth(ChessPiece[][] chessBoard, int rp, int fp, int r, int f, int direction) {
        if (direction == 1) { // north
            while (rp != (r + 1)) {
                rp -= 1;
                if (!ChessBoard.notOccupied(rp, fp, chessBoard)) {
                    return false;
                }
            }
        } else if (direction == 2) { // south
            while (rp != (r - 1)) {
                rp += 1;
                if (!ChessBoard.notOccupied(rp, fp, chessBoard)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Check if Rook can move East/West directions
     * 
     * @param chessBoard 2D array of ChessPieces representing chess board for the
     *                   game
     * @param rp         Old rank (row) coordinate of Rook
     * @param fp         Old file (column) coordinate of Rook
     * @param r          New rank (row) coordinate of Rook
     * @param f          New file (column) coordinate of Rook
     * @param direction  Direction Rook moves in
     * @return True if Rook can move to new position, False otherwise
     */
    public boolean moveEastWest(ChessPiece[][] chessBoard, int rp, int fp, int r, int f, int direction) {
        if (direction == 3) { // east
            while (fp != (f - 1)) {
                fp += 1;
                if (!ChessBoard.notOccupied(rp, fp, chessBoard)) {
                    return false;
                }
            }
        }

        else if (direction == 4) { // west
            while (fp != (f + 1)) {
                fp -= 1;
                if (!ChessBoard.notOccupied(rp, fp, chessBoard)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return super.toString() + "R";
    }
}
