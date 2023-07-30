package ChessPieces;

import ChessBoard.ChessBoard;

/**
 * @author Michael Mogilevsky (mm3201)
 * @author Simran Sareen (ss3825)
 */

/**
 * Class that models a Bishop Piece (inherits from abstract class ChessPiece)
 */
public class Bishop extends ChessPiece {

    /**
     * 3-Arg constructor to create an instance of a Bishop
     * 
     * @param teamColor Color of team the Bishop instance is on; ChessPiece.WHITE if
     *                  White team, ChessPiece.BLACK if Black
     *                  team
     * @param r         The rank (row) index of the Bishop on the board
     * @param f         The file (column) index of the Bishop on the board
     */
    public Bishop(int teamColor, int r, int f) {
        super(teamColor, r, f);
    }

    /*
     * 1 = southeast
     * 2 = southwest
     * 3 = northeast
     * 4 = northwest
     */
    @Override
    public boolean canMove(int newR, int newF, ChessPiece[][] chessBoard) {
        int rp = this.r;
        int fp = this.f;
        int rx = newR - rp;
        int fy = newF - fp;
        if (Math.abs(rx) != Math.abs(fy)) {
            return false;
        }

        if (rx < 0 && fy < 0) {
            return canBishopMove(chessBoard, newR, newF, rp, fp, 4);
        } else if (rx < 0 && fy > 0) {
            return canBishopMove(chessBoard, newR, newF, rp, fp, 3);
        } else if (rx > 0 && fy < 0) {
            return canBishopMove(chessBoard, newR, newF, rp, fp, 2);
        } else if (rx > 0 && fy > 0) {
            return canBishopMove(chessBoard, newR, newF, rp, fp, 1);
        }
        return false;
    }

    /**
     * Check if bishop can move to new position
     * 
     * @param chessBoard 2D array of ChessPieces representing chess board for the
     *                   game
     * @param r          New rank (row) coordinate of bishop
     * @param f          New file (column) coordinate of bishop
     * @param rp         Old rank (row) coordinate of bishop
     * @param fp         Old file (column) coordinate of bishop
     * @param direction  Direction bishop moves in
     * @return True if bishop can move to new position, False otherwise
     */
    public boolean canBishopMove(ChessPiece[][] chessBoard, int r, int f, int rp, int fp, int direction) {
        switch (direction) {
            case 1:
                return canSouthDiagnol(chessBoard, r, f, rp, fp, direction);
            case 2:
                return canSouthDiagnol(chessBoard, r, f, rp, fp, direction);
            case 3:
                return canNorthDiagnol(chessBoard, r, f, rp, fp, direction);
            case 4:
                return canNorthDiagnol(chessBoard, r, f, rp, fp, direction);
            default:
                return false;
        }
    }

    /**
     * Check if Bishop can move any diagonal going South
     * 
     * @param chessBoard 2D array of ChessPieces representing chess board for the
     *                   game
     * @param r          New rank (row) coordinate of bishop
     * @param f          New file (column) coordinate of bishop
     * @param rp         Old rank (row) coordinate of bishop
     * @param fp         Old file (column) coordinate of bishop
     * @param direction  Direction bishop moves in
     * @return True if bishop can move to new position, False otherwise
     */
    public boolean canSouthDiagnol(ChessPiece[][] chessBoard, int r, int f, int rp, int fp, int direction) {
        if (direction == 1) { // southeast
            while (rp != (r - 1) && fp != (f - 1)) {
                fp += 1;
                rp += 1;
                if (!ChessBoard.notOccupied(rp, fp, chessBoard)) {
                    return false;
                }
            }
        }
        if (direction == 2) { // southwest
            while (rp != (r - 1) && fp != (f + 1)) {
                rp += 1;
                fp -= 1;
                if (!ChessBoard.notOccupied(rp, fp, chessBoard)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Check if Bishop can move any diagonal going North
     * 
     * @param chessBoard 2D array of ChessPieces representing chess board for the
     *                   game
     * @param r          New rank (row) coordinate of bishop
     * @param f          New file (column) coordinate of bishop
     * @param rp         Old rank (row) coordinate of bishop
     * @param fp         Old file (column) coordinate of bishop
     * @param direction  Direction bishop moves in
     * @return True if bishop can move to new position, False otherwise
     */
    public boolean canNorthDiagnol(ChessPiece[][] chessBoard, int r, int f, int rp, int fp, int direction) {
        if (direction == 3) { // northeast
            while (rp != (r + 1) && fp != (f - 1)) {
                rp -= 1;
                fp += 1;
                if (!ChessBoard.notOccupied(rp, fp, chessBoard)) {
                    return false;
                }
            }
        }
        if (direction == 4) { // northwest
            while (rp != (r + 1) && fp != (f + 1)) {
                fp -= 1;
                rp -= 1;
                if (!ChessBoard.notOccupied(rp, fp, chessBoard)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return super.toString() + "B";
    }
}
