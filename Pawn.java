package ChessPieces;

import ChessBoard.*;

/**
 * @author Michael Mogilevsky (mm3201)
 * @author Simran Sareen (ss3825)
 */

/**
 * Class that models a Pawn piece
 */
public class Pawn extends ChessPiece {

    /**
     * '*' represents pawn is not being promoted
     */
    private final static char NO_PROMOTION = '*';

    /**
     * True if pawn piece can be enpassanted
     */
    boolean canBeEnpassanted;

    /**
     * True if pawn piece can use enpassant
     */
    public boolean useEnpassant = false;

    /**
     * Character that represents what piece next pawn is being promoted to ('*' for
     * no promotion)
     */
    public static char promotionTo = NO_PROMOTION; // '*' represents no current state of promotion on pawn

    /**
     * 3-Arg constructor for Pawn
     * 
     * @param teamColor Color of team pawn is on
     * @param r         Rank (row) coordinate of Pawn
     * @param f         File (column) coordinate of Pawn
     */
    public Pawn(int teamColor, int r, int f) {
        super(teamColor, r, f);
    }

    /**
     * Checks and promotes Pawn to indicated piece
     * 
     * @param chessBoard    2D array of ChessPieces that the pawn is on
     * @param rP            Rank coordinate of pawn at promotion
     * @param fP            File coordinate of pawn at promotion
     * @param promotedPiece Piece to promote Pawn to
     * @return True if pawn was successfully promoted to indicated piece, False
     *         otherwise
     */
    public static boolean promoteTo(ChessPiece[][] chessBoard, int rP, int fP, char promotedPiece) {
        ChessPiece pieceToPromote = chessBoard[rP][fP];
        int color = pieceToPromote.getTeamColor();
        if (promotedPiece == 'p') {
            chessBoard[rP][fP] = new Pawn(color, rP, fP);
        } else if (promotedPiece == 'N') {
            chessBoard[rP][fP] = new Knight(color, rP, fP);
        } else if (promotedPiece == 'R') {
            chessBoard[rP][fP] = new Rook(color, rP, fP);
        } else if (promotedPiece == 'B') {
            chessBoard[rP][fP] = new Bishop(color, rP, fP);
        } else if (promotedPiece == 'Q') {
            chessBoard[rP][fP] = new Queen(color, rP, fP);
        } else {
            // instructions say to assume queen if no promotion piece indicated
            chessBoard[rP][fP] = new Queen(color, rP, fP);
        }

        chessBoard[rP][fP].hasMoved = true;
        return true;
    }

    @Override
    public boolean canMove(int newR, int newF, ChessPiece[][] chessBoard) {
        int move = -1; // white moves up the ranks (from larger rank index to smaller index)
        if (this.teamColor == BLACK)
            move *= -1; // black moves down the ranks (from smaller index to larger index)

        // Check if pawn can take a piece diagonal from it
        if (!(ChessBoard.notOccupied(newR, newF, chessBoard)) && (chessBoard[newR][newF].teamColor != this.teamColor)
                && (this.r + move == newR) && (Math.abs(newF - this.f) == 1)) {
            return true;
        }
        // Check that IF THIS IS PAWN'S FIRST MOVE, it can move two spaces (up for
        // white, down for black), and is now vulnerable to Enpassant rule takeover
        if (!this.hasMoved && ChessBoard.notOccupied(newR, newF, chessBoard)
                && ChessBoard.notOccupied(newR - move, newF, chessBoard)
                && (this.r + move == newR || this.r + move + move == newR) && (this.f - newF == 0)) {
            chessBoard[this.r][this.f].canBeEnpassanted = true;
            return true;
        }
        // Check if pawn can move 1 space ahead
        if (ChessBoard.notOccupied(newR, newF, chessBoard) && (this.r + move == newR) && (this.f - newF == 0)) {
            return true;
        }
        // We know canBeEnpassanted field of a pawn piece is true if it moved two spaces
        // previous step,
        // so now we can also check if a move is valid if it is a valid Enpassant
        // condition
        if ((this.r + move == newR) && (Math.abs(this.f - newF) == 1)) {
            // get the piece that might be enpassanted
            ChessPiece possibleEnpassantPiece = chessBoard[newR - move][newF];

            if ((possibleEnpassantPiece != null) && (possibleEnpassantPiece instanceof Pawn)
                    && possibleEnpassantPiece.isOpponent(chessBoard[this.r][this.f])
                    && possibleEnpassantPiece.canBeEnpassanted) {
                this.useEnpassant = true;
                return true;
            } else {
                return false;
            }
        }

        return false;
    }

    @Override
    public boolean movePiece(int newR, int newF, ChessPiece[][] chessBoard) {

        int move = -1; // white moves up the ranks (from larger rank index to smaller index)
        if (this.teamColor == BLACK) {
            move *= -1; // black moves down the ranks (from smaller index to larger index)
        }

        int currR = this.r;
        int currF = this.f;

        if (canMove(newR, newF, chessBoard)) {

            // check if pawn piece is trying to Enpassant
            if (useEnpassant) {
                chessBoard[currR][currF].setBoardWithMove(newR, newF, chessBoard);
                chessBoard[newR - move][newF] = null;
                useEnpassant = false;
                return true;
            }
            // check for promotion (promotion will vary depending on client input in
            // ChessController.java)
            if (promotionTo != NO_PROMOTION) {
                // first try to get the move itself is board is empty at new position
                if (ChessBoard.notOccupied(newR, newF, chessBoard)) {
                    chessBoard[currR][currF].setBoardWithMove(newR, newF, chessBoard);
                }
                // if space is taken, check if can take the piece
                else if (chessBoard[currR][currF].isOpponent(chessBoard[newR][newF])) {
                    chessBoard[currR][currF].takePiece(currR, currF, newR, newF, chessBoard);
                }
                // if space is taken by same team piece, can't move there/promote
                else {
                    promotionTo = NO_PROMOTION;
                    return false;
                }

                // after trying to move or take, we now try the actual promotion
                if (promoteTo(chessBoard, newR, newF, promotionTo)) {
                    promotionTo = NO_PROMOTION;
                    return true;
                } else {
                    promotionTo = NO_PROMOTION;
                    return false;
                }
            }
            // Take a piece if opponent
            if (chessBoard[currR][currF].isOpponent(chessBoard[newR][newF])) {
                chessBoard[currR][currF].takePiece(currR, currF, newR, newF, chessBoard);
                return true;
            }
            // Lastly, just try a simple move to open space
            if (ChessBoard.notOccupied(newR, newF, chessBoard)) {
                chessBoard[currR][currF].setBoardWithMove(newR, newF, chessBoard);
                return true;
            }
            // If none of the moves work here, move is not executed
            else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Resets enpassant vulnerability of all Pawn pieces on board
     * 
     * @param team       Color of team to reset all enpassants on
     * @param chessBoard 2D array of ChessPieces that the pawns are on
     */
    public static void resetAllEnpassants(int team, ChessPiece[][] chessBoard) {
        for (int r = 0; r < ChessBoard.N; r++) {
            for (int f = 0; f < ChessBoard.N; f++) {
                if (chessBoard[r][f] != null && chessBoard[r][f].teamColor == team
                        && chessBoard[r][f] instanceof Pawn) {
                    chessBoard[r][f].canBeEnpassanted = false;
                }
            }
        }
    }

    @Override
    public String toString() {
        return super.toString() + "p";
    }
}
