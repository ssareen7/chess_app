package ChessPieces;

import ChessBoard.*;

/**
 * @author Michael Mogilevsky (mm3201)
 * @author Simran Sareen (ss3825)
 */

/**
 * Class that models a King Piece (inherits from abstract class ChessPiece)
 */
public class King extends ChessPiece {

    /**
     * True if white king is castling on King side
     */
    public static boolean whiteCastlingKingside;

    /**
     * True if white king is castling on Queen side
     */
    public static boolean whiteCastlingQueenside;

    /**
     * True if black king is castling on King side
     */
    public static boolean blackCastlingKingside;

    /**
     * True if black king is castling on Queen side
     */
    public static boolean blackCastlingQueenside;

    /**
     * 3-Arg constructor for King
     * 
     * @param teamColor Color of team King is on (ChessPiece.WHITE or
     *                  ChessPiece.BLACK)
     * @param r         The rank (row) coordinate of King
     * @param f         The file (column) coordinate of King
     */
    public King(int teamColor, int r, int f) {
        super(teamColor, r, f);
    }

    @Override
    public boolean canMove(int newR, int newF, ChessPiece[][] chessBoard) {
        int currR = this.r;
        int currF = this.f;

        int dR = Math.abs(currR - newR);
        int dF = Math.abs(currF - newF);

        // Valid move (one in any direction)
        // (diagonal) || (horizontal) || (vertical)
        if ((dR == 1 && dF == 1) || (dR == 0 && dF == 1) || (dR == 1 && dF == 0)) {
            return true;
        }

        ChessPiece k = chessBoard[currR][currF];

        if (checkCastlingKingside(k, currR, currF, newR, newF, chessBoard))

            return true;
        if (checkCastlingQueenside(k, currR, currF, newR, newF, chessBoard))
            return true;

        return false;

    }

    @Override
    public boolean movePiece(int newR, int newF, ChessPiece[][] chessBoard) {
        if (canMove(newR, newF, chessBoard)) {
            // check if king can move
            if (ChessBoard.notOccupied(newR, newF, chessBoard)) {
                // move king to new location (newR, newF) and castle if possible
                chessBoard[this.r][this.f].setBoardWithMove(newR, newF, chessBoard);

                if (whiteCastlingKingside) {
                    if (chessBoard[newR][newF].getTeamColor() == WHITE) {
                        chessBoard[7][7].setBoardWithMove(7, 5, chessBoard);
                    }
                    whiteCastlingKingside = false;
                }

                if (whiteCastlingQueenside) {
                    if (chessBoard[newR][newF].getTeamColor() == WHITE) {
                        chessBoard[7][0].setBoardWithMove(7, 3, chessBoard);
                    }
                    whiteCastlingQueenside = false;
                }

                if (blackCastlingKingside) {
                    if (chessBoard[newR][newF].getTeamColor() == BLACK) {
                        chessBoard[0][7].setBoardWithMove(0, 5, chessBoard);
                    }
                    blackCastlingKingside = false;
                }

                if (blackCastlingQueenside) {
                    if (chessBoard[newR][newF].getTeamColor() == BLACK) {
                        chessBoard[0][0].setBoardWithMove(0, 3, chessBoard);
                    }
                }

                return true;
            }

            // Take another piece
            if (chessBoard[this.r][this.f].isOpponent(chessBoard[newR][newF])) {
                chessBoard[this.r][this.f].takePiece(this.r, this.f, newR, newF, chessBoard);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Checks if King can castle on King side
     * 
     * @param k          The king piece
     * @param currR      Current rank (row) coordinate of King
     * @param currF      Current file (column) coordinate of King
     * @param newR       New rank (row) coordinate of King
     * @param newF       New file (column) coordinate of King
     * @param chessBoard The chess board 2D array of ChessPieces the piece is on
     * @return True if King can castle King side, False otherwise
     */
    private boolean checkCastlingKingside(ChessPiece k, int currR, int currF, int newR, int newF,
            ChessPiece[][] chessBoard) {
        int r;
        if (k.getTeamColor() == WHITE) {
            r = 7;
        } else {
            r = 0;
        }
        if (currR == r && currF == 4 && newR == r && newF == 6 && !k.hasMoved && !chessBoard[newR][7].hasMoved) {
            if (ChessBoard.notOccupied(currR, currF + 1, chessBoard)
                    && ChessBoard.notOccupied(currR, currF + 2, chessBoard)
                    && !ChessPiece.existsCheck(currR, currF, k, chessBoard)
                    && !ChessPiece.existsCheck(currR, currF + 1, k, chessBoard)
                    && !ChessPiece.existsCheck(currR, currF + 2, k, chessBoard)) {
                if (chessBoard[currR][currF].getTeamColor() == WHITE) {
                    whiteCastlingKingside = true;
                } else {
                    blackCastlingKingside = true;
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if King can castle on Queen side
     * 
     * @param k          The king piece
     * @param currR      Current rank (row) coordinate of King
     * @param currF      Current file (column) coordinate of King
     * @param newR       New rank (row) coordinate of King
     * @param newF       New file (column) coordinate of King
     * @param chessBoard The chess board 2D array of ChessPieces the piece is on
     * @return True if King can castle Queen side, False otherwise
     */
    private boolean checkCastlingQueenside(ChessPiece k, int currR, int currF, int newR, int newF,
            ChessPiece[][] chessBoard) {
        int r;
        if (k.getTeamColor() == WHITE) {
            r = 7;
        } else {
            r = 0;
        }
        if (currR == r && currF == 4 && newR == r && newF == 2 && !k.hasMoved && !chessBoard[newR][0].hasMoved) {
            if (ChessBoard.notOccupied(currR, currF - 1, chessBoard)
                    && ChessBoard.notOccupied(currR, currF - 2, chessBoard)
                    && ChessBoard.notOccupied(currR, currF - 3, chessBoard)
                    && !ChessPiece.existsCheck(currR, currF, k, chessBoard)
                    && !ChessPiece.existsCheck(currR, currF - 1, k, chessBoard)
                    && !ChessPiece.existsCheck(currR, currF - 2, k, chessBoard)
                    && !ChessPiece.existsCheck(currR, currF - 3, k, chessBoard)) {
                if (chessBoard[currR][currF].getTeamColor() == WHITE) {
                    whiteCastlingQueenside = true;
                } else {
                    blackCastlingQueenside = true;
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return super.toString() + "K";
    }
}
