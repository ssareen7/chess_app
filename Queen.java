package ChessPieces;

import ChessBoard.ChessBoard;

/**
 * @author Michael Mogilevsky (mm3201)
 * @author Simran Sareen (ss3825)
 */

/**
 * Class that models a Queen piece, inheriting from ChessPiece abstract class
 */
public class Queen extends ChessPiece {

    /**
     * 3-Arg constructor for a Queen
     * 
     * @param teamColor Color of team Queen is on
     * @param r         Rank (row) coordinate of Queen
     * @param f         File (column) coordinate of Queen
     */
    public Queen(int teamColor, int r, int f) {
        super(teamColor, r, f);
    }

    /*
     * north = 1
     * north east = 2
     * north west = 3
     * south = 4
     * south east = 5
     * south west = 6
     * east = 7
     * west = 8
     */
    @Override
    public boolean canMove(int newR, int newF, ChessPiece[][] chessBoard) {
        int rp = this.r;
        int fp = this.f;

        int distr = Math.abs(rp - newR);
        int distf = Math.abs(fp - newF);

        int rx = newR - rp;
        int fy = newF - fp;

        if (distr != 0 && distf == 0) { // north south
            if (rx < 0) {
                return canQueenMove(chessBoard, rp, fp, newR, newF, 1);
            } else {
                return canQueenMove(chessBoard, rp, fp, newR, newF, 4);
            }
        } else if (distr == 0 && distf != 0) { // east west
            if (fy < 0) {
                return canQueenMove(chessBoard, rp, fp, newR, newF, 8);
            } else {
                return canQueenMove(chessBoard, rp, fp, newR, newF, 7);
            }
        } else if (distr == distf) { // diagnol
            if (rx > 0 && fy < 0) {
                return canQueenMove(chessBoard, rp, fp, newR, newF, 6);
            } else if (rx < 0 && fy < 0) {
                return canQueenMove(chessBoard, rp, fp, newR, newF, 3);
            } else if (rx < 0 && fy > 0) {
                return canQueenMove(chessBoard, rp, fp, newR, newF, 2);
            } else if (rx > 0 && fy > 0) {
                return canQueenMove(chessBoard, rp, fp, newR, newF, 5);
            }
        }

        return false;
    }

    /**
     * Verify that queen can move to new position
     * 
     * @param chessBoard 2D array of ChessPieces that the pawn is on
     * @param rp         Old rank coordinate of Queen
     * @param fp         Old file coordinate of Queen
     * @param r          New rank coordinate of Queen
     * @param f          New file coordinate of Queen
     * @param direction  Direction Queen will move in
     * @return True if Queen can move to new position, False otherwise
     */
    public boolean canQueenMove(ChessPiece[][] chessBoard, int rp, int fp, int r, int f, int direction) {
        switch (direction) {
            case 1:
                return moveAroundNorth(chessBoard, rp, fp, r, f, direction);
            case 2:
                return moveAroundNorth(chessBoard, rp, fp, r, f, direction);
            case 3:
                return moveAroundNorth(chessBoard, rp, fp, r, f, direction);
            case 4:
                return moveAroundSouth(chessBoard, rp, fp, r, f, direction);
            case 5:
                return moveAroundSouth(chessBoard, rp, fp, r, f, direction);
            case 6:
                return moveAroundSouth(chessBoard, rp, fp, r, f, direction);
            case 7:
                return moveEastWest(chessBoard, rp, fp, r, f, direction);
            case 8:
                return moveEastWest(chessBoard, rp, fp, r, f, direction);
            default:
                return false;
        }
    }

    /**
     * Check if Queen can move North any direction
     * 
     * @param chessBoard 2D array of ChessPieces that the pawn is on
     * @param rp         Old Rank coordinate of Queen
     * @param fp         Old File coordinate of Queen
     * @param r          New Rank coordinate of Queen
     * @param f          New File coordinate of Queen
     * @param direction  Direction to move Queen in
     * @return True if Queen can move in direction, False otherwise
     */
    public boolean moveAroundNorth(ChessPiece[][] chessBoard, int rp, int fp, int r, int f, int direction) {
        if (direction == 1) { // north
            while (rp != (r + 1)) {
                rp -= 1;
                if (!ChessBoard.notOccupied(rp, fp, chessBoard)) {
                    return false;
                }
            }
        }
        if (direction == 2) { // north east
            while (rp != (r + 1) && fp != (f - 1)) {
                rp -= 1;
                fp += 1;
                if (!ChessBoard.notOccupied(rp, fp, chessBoard)) {
                    return false;
                }
            }
        }
        if (direction == 3) { // north west
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

    /**
     * Check if Queen can move South any direction
     * 
     * @param chessBoard 2D array of ChessPieces that the pawn is on
     * @param rp         Old Rank coordinate of Queen
     * @param fp         Old File coordinate of Queen
     * @param r          New Rank coordinate of Queen
     * @param f          New File coordinate of Queen
     * @param direction  Direction to move Queen in
     * @return True if Queen can move in direction, False otherwise
     */
    public boolean moveAroundSouth(ChessPiece[][] chessBoard, int rp, int fp, int r, int f, int direction) {
        if (direction == 4) { // south
            while (rp != (r - 1)) {
                rp += 1;
                if (!ChessBoard.notOccupied(rp, fp, chessBoard)) {
                    return false;
                }
            }
        }
        if (direction == 5) { // southeast
            while (rp != (r - 1) && fp != (f - 1)) {
                fp += 1;
                rp += 1;
                if (!ChessBoard.notOccupied(rp, fp, chessBoard)) {
                    return false;
                }
            }
        }
        if (direction == 6) { // southwest
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
     * Check if Queen can move East or West any direction
     * 
     * @param chessBoard 2D array of ChessPieces that the pawn is on
     * @param rp         Old Rank coordinate of Queen
     * @param fp         Old File coordinate of Queen
     * @param r          New Rank coordinate of Queen
     * @param f          New File coordinate of Queen
     * @param direction  Direction to move Queen in
     * @return True if Queen can move in direction, False otherwise
     */
    public boolean moveEastWest(ChessPiece[][] chessBoard, int rp, int fp, int r, int f, int direction) {
        if (direction == 7) { // east
            while (fp != (f - 1)) {
                fp += 1;
                if (!ChessBoard.notOccupied(rp, fp, chessBoard)) {
                    return false;
                }
            }
        }
        if (direction == 8) { // west
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
        return super.toString() + "Q";
    }
}
