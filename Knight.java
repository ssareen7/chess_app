package ChessPieces;

/**
 * @author Michael Mogilevsky (mm3201)
 * @author Simran Sareen (ss3825)
 */

/**
 * Class that models a Knight Piece (inherits from abstract class ChessPiece)
 */
public class Knight extends ChessPiece {

    /**
     * 3-Arg constructor for Knight
     * 
     * @param teamColor Color of team Knight is on
     * @param r         Rank (row) coordinate of Knight
     * @param f         File (column) coordinate of Knight
     */
    public Knight(int teamColor, int r, int f) {
        super(teamColor, r, f);
    }

    @Override
    public boolean canMove(int newR, int newF, ChessPiece[][] chessBoard) {
        // can only move in L-shape, vertical or horizontal (one leg is longer). Also,
        // the Knight can jump over pieces
        return ((Math.abs(this.r - newR) == 1) && (Math.abs(this.f - newF) == 2))
                || ((Math.abs(this.r - newR) == 2) && (Math.abs(this.f - newF) == 1));
    }

    // NOTE: movePiece(...) is NOT overridden because it accounts for the rest of
    // the checks before moving... nothing special for knight
    // that wasn't already accounted for in canMove(...)

    @Override
    public String toString() {
        return super.toString() + "N";
    }
}
