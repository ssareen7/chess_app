package ChessPieces;

import ChessBoard.*;

/**
 * @author Michael Mogilevsky (mm3201)
 * @author Simran Sareen (ss3825)
 */

/**
 * Abstract class ChessPiece that models a standard chess piece on a chess board
 */
public abstract class ChessPiece {

    /**
     * True if the pawn can be enpassanted
     */
    public boolean canBeEnpassanted;

    /**
     * Static final variable representing team color for White team
     */
    public static final int WHITE = 1;

    /**
     * Static final variable representing team color for Black team
     */
    public static final int BLACK = 2;

    /**
     * The rank (row) index of chess piece on the board
     */
    int r;

    /**
     * The file (column) index of chess piece on the board
     */
    int f;

    /**
     * True if the piece has already previously moved
     */
    boolean hasMoved = false;

    /**
     * Represents color of the team the piece is on (ChessPiece.WHITE or
     * ChessPiece.BLACK)
     */
    int teamColor;

    /**
     * Only for pawns; True if has enpassant and can capture
     */
    public boolean hasEnpassant;

    /**
     * 3-Arg Constructor to be invoked by subclasses inheriting from ChessPiece
     * 
     * @param teamColor Color of team the piece is on (ChessPiece.WHITE or
     *                  ChessPiece.BLACK)
     * @param r         The rank (row) index of chess piece on the board
     * @param f         The file (column) index of chess piece on the board
     */
    public ChessPiece(int teamColor, int r, int f) {
        this.teamColor = teamColor;
        this.r = r;
        this.f = f;
    }

    /**
     * Sets the rank (row) index of piece
     * 
     * @param r The rank (row) index to set piece's index to
     */
    public void setR(int r) {
        this.r = r;
    }

    /**
     * Sets the file (column) index of piece
     * 
     * @param f The file (column) index to set the piece's index to
     */
    public void setF(int f) {
        this.f = f;
    }

    /**
     * Sets the team color for the piece
     * 
     * @param color Color to set team color to (ChessPiece.WHITE or
     *              ChessPiece.BLACK)
     */
    public void setTeamColor(int color) {
        if (color == WHITE || color == BLACK) {
            this.teamColor = color;
        }
    }

    /**
     * Returns the color of the team the piece is on
     * 
     * @return WHITE if on white team, BLACK if on black team
     */
    public int getTeamColor() {
        return teamColor;
    }

    /**
     * Returns whether the piece has moved previously
     * 
     * @return True if piece has moved previously, False otherwise
     */
    public boolean getHasMoved() {
        return hasMoved;
    }

    /**
     * Returns whether otherPiece is on the opposite team of current piece
     * 
     * @param otherPiece The other piece being compared
     * @return True if otherPiece is on opposite team, False otherwise
     */
    public boolean isOpponent(ChessPiece otherPiece) {
        if (otherPiece == null)
            return false;
        return this.teamColor != otherPiece.teamColor;
    }

    /**
     * Return the rank (row) index of piece on the board
     * 
     * @return rank coordinate
     */
    public int getRank() {
        return this.r;
    }

    /**
     * Return the file (column) index of piece on the board
     * 
     * @return file coordinate
     */
    public int getFile() {
        return this.f;
    }

    /**
     * Updates the board with new location of piece, sets previous spot before move
     * to null
     * 
     * @param newR       New rank coordinate of piece
     * @param newF       New file coordinate of piece
     * @param chessBoard The chess board 2D array of ChessPieces the piece is on
     */
    public void setBoardWithMove(int newR, int newF, ChessPiece[][] chessBoard) {
        this.hasMoved = true;
        int oldR = this.r;
        int oldF = this.f;
        ChessPiece pieceInOldLocation = chessBoard[oldR][oldF];
        chessBoard[newR][newF] = pieceInOldLocation;
        chessBoard[newR][newF].hasEnpassant = pieceInOldLocation.hasEnpassant;
        chessBoard[oldR][oldF] = null;
        chessBoard[newR][newF].r = newR;
        chessBoard[newR][newF].f = newF;
    }

    /**
     * Undo the piece's recent move, and update the chess board accordingly
     * 
     * @param r          Rank coordinate of piece
     * @param f          File coordinate of piece
     * @param has_moved  True if is first move for a piece, False
     *                   otherwise
     * @param chessBoard The chess board 2D array of ChessPieces the piece is on
     */
    public void undoBoardWithMove(int r, int f, boolean has_moved, ChessPiece[][] chessBoard) {
        // get old position
        int rP = this.r;
        int fP = this.f;

        // switch everything back, and set old position to null
        // if piece was taken, etc. then this will be handled in whatever piece of code
        // uses this function
        chessBoard[r][f] = chessBoard[rP][fP];
        chessBoard[r][f].hasMoved = has_moved;
        chessBoard[r][f].hasEnpassant = chessBoard[rP][fP].hasEnpassant;
        chessBoard[rP][fP] = null;
        chessBoard[r][f].r = r;
        chessBoard[r][f].f = f;
    }

    /**
     * Take a piece with attacking move
     * 
     * @param currR      Current rank coordinate of piece
     * @param currF      Current file coordinate of piece
     * @param oppR       Opponent's rank coordinate
     * @param oppF       Opponent's file coordinate
     * @param chessBoard The chess board 2D array of ChessPieces the piece is on
     */
    public void takePiece(int currR, int currF, int oppR, int oppF, ChessPiece[][] chessBoard) {
        ChessPiece curr = chessBoard[currR][currF];

        chessBoard[oppR][oppF] = curr;
        chessBoard[currR][currF] = null;
        chessBoard[oppR][oppF].r = oppR;
        chessBoard[oppR][oppF].f = oppF;
        chessBoard[oppR][oppF].hasMoved = true;
    }

    /**
     * If possible, moves the piece to new position
     * 
     * @param newR       New rank coordinate of piece
     * @param newF       New file coordinate of piece
     * @param chessBoard The chess board 2D array of ChessPieces the piece is on
     * @return True if move to new position was successful, False otherwise
     */
    public boolean movePiece(int newR, int newF, ChessPiece[][] chessBoard) {
        int oldR = this.r;
        int oldF = this.f;
        ChessPiece oldPos = chessBoard[oldR][oldF];

        if (canMove(newR, newF, chessBoard)) {
            ChessPiece newPos = chessBoard[newR][newF];
            // Check that chessBoard[newX][newY] is not null first
            if (ChessBoard.notOccupied(newR, newF, chessBoard)) {
                oldPos.setBoardWithMove(newR, newF, chessBoard);
                return true;
            } else if (oldPos.isOpponent(newPos)) {
                oldPos.takePiece(oldR, oldF, newR, newF, chessBoard);
                return true;
            }
            // if position at newX, newY is occupied but not an opponent, then it's same
            // team and can't move there
            else
                return false;

        }
        // Invalid move if path is blocked
        else {
            return false;
        }
    }

    @Override
    public String toString() {
        String color;
        if (this.teamColor == WHITE) {
            color = "w";
        } else {
            color = "b";
        }
        return color;
    }

    /**
     * Attempt to locate a check for provided king
     * 
     * @param r          Rank coordinate of piece
     * @param f          File coordinate of piece
     * @param k          The king who is being examined for a check
     * @param chessBoard The chess board 2D array of ChessPieces the piece is on
     * @return True if the provided king is checked, False otherwise
     */
    public static boolean existsCheck(int r, int f, ChessPiece k, ChessPiece[][] chessBoard) {
        // Check upward path for a Rook or Queen (index gets smaller)
        for (int rp = r - 1; rp >= 0; rp--) {
            // Check something is actually in the space (else keep looking)
            if (!ChessBoard.notOccupied(rp, f, chessBoard)) {
                if (checkOpponent(chessBoard, rp, f, k, 'R', 0, 0)) {
                    return true;
                } else
                    break;
            }
        }
        // Check downward path for a Rook or Queen (index gets larger)
        for (int rp = r + 1; rp < ChessBoard.N; rp++) {
            // Check something is actually in the space (else keep looking)
            if (!ChessBoard.notOccupied(rp, f, chessBoard)) {
                if (checkOpponent(chessBoard, rp, f, k, 'R', 0, 0)) {
                    return true;
                } else
                    break;
            }
        }
        // Check left path for a Rook or Queen (f gets smaller)
        for (int fp = f - 1; fp >= 0; fp--) {
            // Check something is actually in the space (else keep looking)
            if (!ChessBoard.notOccupied(r, fp, chessBoard)) {
                if (checkOpponent(chessBoard, r, fp, k, 'R', 0, 0)) {
                    return true;
                } else
                    break;
            }
        }
        // Check right path for a Rook or Queen (f gets larger)
        for (int fp = f + 1; fp < ChessBoard.N; fp++) {
            // Check something is actually in the space (else keep looking)
            if (!ChessBoard.notOccupied(r, fp, chessBoard)) {
                if (checkOpponent(chessBoard, r, fp, k, 'R', 0, 0)) {
                    return true;
                } else
                    break;
            }
        }

        // Check diagonally up and right path for Bishop or Queen (r decreases, f
        // increases)
        for (int rp = r - 1, fp = f + 1; rp >= 0 && fp < ChessBoard.N; rp--, fp++) {
            if (!ChessBoard.notOccupied(rp, fp, chessBoard)) {
                if (checkOpponent(chessBoard, rp, fp, k, 'B', 0, 0)) {
                    return true;
                } else
                    break;
            }
        }
        // Check diagonally up and left path for Bishop or Queen (r decreases, f
        // decreases)
        for (int rp = r - 1, fp = f - 1; rp >= 0 && fp >= 0; rp--, fp--) {
            if (!ChessBoard.notOccupied(rp, fp, chessBoard)) {
                if (checkOpponent(chessBoard, rp, fp, k, 'B', 0, 0)) {
                    return true;
                } else
                    break;
            }
        }
        // Check diagonally down and left path for Bishop or Queen (r increases, f
        // decreases)
        for (int rp = r + 1, fp = f - 1; rp < ChessBoard.N && fp >= 0; rp++, fp--) {
            if (!ChessBoard.notOccupied(rp, fp, chessBoard)) {
                if (checkOpponent(chessBoard, rp, fp, k, 'B', 0, 0)) {
                    return true;
                } else
                    break;
            }
        }
        // Check diagonally down and right path for Bishop or Queen (r increases, f
        // increases)
        for (int rp = r + 1, fp = f + 1; rp < ChessBoard.N && fp < ChessBoard.N; rp++, fp++) {
            if (!ChessBoard.notOccupied(rp, fp, chessBoard)) {
                if (checkOpponent(chessBoard, rp, fp, k, 'B', 0, 0)) {
                    return true;
                } else
                    break;
            }
        }

        // Check for a white pawn that can put King in check
        if (k.getTeamColor() == BLACK) {
            // Check southwest and southeast squares for a valid checking pawn
            if (!ChessBoard.notOccupied(r + 1, f - 1, chessBoard) && chessBoard[r + 1][f - 1].isOpponent(k)
                    && chessBoard[r + 1][f - 1] instanceof Pawn) {
                return true;
            }
            if (!ChessBoard.notOccupied(r + 1, f + 1, chessBoard) && chessBoard[r + 1][f + 1].isOpponent(k)
                    && chessBoard[r + 1][f + 1] instanceof Pawn) {
                return true;
            }
        }
        // Check for a black pawn that can put King in check
        else {
            // Check northwest and northeast squares for a valid checking pawn
            if (!ChessBoard.notOccupied(r - 1, f - 1, chessBoard) && chessBoard[r - 1][f - 1].isOpponent(k)
                    && chessBoard[r - 1][f - 1] instanceof Pawn) {
                return true;
            }
            if (!ChessBoard.notOccupied(r - 1, f + 1, chessBoard) && chessBoard[r - 1][f + 1].isOpponent(k)
                    && chessBoard[r - 1][f + 1] instanceof Pawn) {
                return true;
            }
        }

        // Check for a knight that can put King in check
        // Check all SHORT VERTICAL, LONG HORIZONTAL
        if (checkOpponent(chessBoard, r, f, k, 'N', 1, 2))
            return true;
        if (checkOpponent(chessBoard, r, f, k, 'N', 1, -2))
            return true;
        if (checkOpponent(chessBoard, r, f, k, 'N', -1, 2))
            return true;
        if (checkOpponent(chessBoard, r, f, k, 'N', -1, -2))
            return true;

        // Check all LONG VERTICAL, SHORT HORIZONTAL
        if (checkOpponent(chessBoard, r, f, k, 'N', -2, -1))
            return true;
        if (checkOpponent(chessBoard, r, f, k, 'N', 2, -1))
            return true;
        if (checkOpponent(chessBoard, r, f, k, 'N', -2, 1))
            return true;
        if (checkOpponent(chessBoard, r, f, k, 'N', 2, 1))
            return true;

        // Check for opponent KING that might put current King in check
        // Check vertical/horizontals
        if (checkOpponent(chessBoard, r, f, k, 'K', 0, -1))
            return true;
        if (checkOpponent(chessBoard, r, f, k, 'K', 0, 1))
            return true;
        if (checkOpponent(chessBoard, r, f, k, 'K', 1, 0))
            return true;
        if (checkOpponent(chessBoard, r, f, k, 'K', -1, 0))
            return true;

        // Check diagonals
        if (checkOpponent(chessBoard, r, f, k, 'K', 1, 1))
            return true;
        if (checkOpponent(chessBoard, r, f, k, 'K', -1, 1))
            return true;
        if (checkOpponent(chessBoard, r, f, k, 'K', 1, -1))
            return true;
        if (checkOpponent(chessBoard, r, f, k, 'K', -1, -1))
            return true;

        return false;
    }

    /**
     * Check if provided piece matches provided character p and is an opponent of
     * the king
     * 
     * @param chessBoard The chess board 2D array of ChessPieces the piece is on
     * @param r          The rank coordinate of piece
     * @param f          The file coordinate of piece
     * @param k          The king who is being examined for a Check
     * @param p          Character representing what type of piece the opponent is
     * @param offR       Rank coordinate offset to the piece being checked
     * @param offF       File coordinate offset to the piece being checked
     * @return True if is an opponent in check of King, False otherwise
     */
    private static boolean checkOpponent(ChessPiece[][] chessBoard, int r, int f, ChessPiece k, char p, int offR,
            int offF) {
        if (p == 'R') {
            return ((chessBoard[r][f].isOpponent(k))
                    && (chessBoard[r][f] instanceof Rook || chessBoard[r][f] instanceof Queen));
        } else if (p == 'B') {
            return ((chessBoard[r][f].isOpponent(k))
                    && (chessBoard[r][f] instanceof Bishop || chessBoard[r][f] instanceof Queen));
        } else if (p == 'N') {
            return !ChessBoard.notOccupied(r + offR, f + offF, chessBoard)
                    && chessBoard[r + offR][f + offF].isOpponent(k) && chessBoard[r + offR][f + offF] instanceof Knight;
        } else if (p == 'K') {
            return !ChessBoard.notOccupied(r + offR, f + offF, chessBoard)
                    && chessBoard[r + offR][f + offF] instanceof King && chessBoard[r + offR][f + offF].isOpponent(k);
        }
        return false;
    }

    /**
     * Check if the piece can move along a path to (newR,newF) position on board,
     * implemented by all subclasses inheriting from ChessPiece
     * 
     * @param newR       Desired new rank coordinate for piece
     * @param newF       Desired new file coordinate for piece
     * @param chessBoard The chess board 2D array of ChessPieces the piece is on
     * @return True if piece can move to (newR,newF) on board, False otherwise
     */
    public abstract boolean canMove(int newR, int newF, ChessPiece[][] chessBoard);
}
