package ChessController;

import ChessBoard.*;
import ChessPieces.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.io.IOException;

/**
 * @author Michael Mogilevsky (mm3201)
 * @author Simran Sareen (ss3825)
 */

/**
 * Chess class that allows user to play game of Chess
 */
public class Chess {

    /**
     * True if a draw has been proposed
     */
    static boolean proposeDraw = false;

    /**
     * True if white team is in checkmate
     */
    static boolean whiteCheckmated = false;

    /**
     * True if black team is in checkmate
     */
    static boolean blackCheckmated = false;

    /**
     * HashMap that converts a letter (file/column) to board column
     */
    public static Map<String, Integer> letterToFile = new HashMap<String, Integer>();

    /**
     * No-arg constructor for Chess, initializes letterToFile hashmap for input
     * conversion
     */
    public Chess() {
        letterToFile.put("a", 0);
        letterToFile.put("b", 1);
        letterToFile.put("c", 2);
        letterToFile.put("d", 3);
        letterToFile.put("e", 4);
        letterToFile.put("f", 5);
        letterToFile.put("g", 6);
        letterToFile.put("h", 7);
    }

    /**
     * Main loop to run game of chess. Ends when either team is in checkmate or if a
     * team resigns or draws.
     * 
     * @param args Main method arguments
     * @throws IOException Used for user input reading
     */
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        // initialize Chess game to allow conversion of user input
        Chess c = new Chess();

        // initialize the chess board
        ChessPiece[][] chessBoard = ChessBoard.createBoard();
        ChessBoard.printBoard(chessBoard);

        int currTurn = ChessPiece.WHITE; // white starts

        // continue until game is over
        while (!gameOver(currTurn, chessBoard)) {
            King currKing = findKing(currTurn, chessBoard);

            // Reset enpassant so enpassant can't be done on next iteration even if pawn
            // moves 2 spaces
            Pawn.resetAllEnpassants(currTurn, chessBoard);

            // formatting as per instructions
            System.out.println();

            if (currTurn == ChessPiece.WHITE) {
                System.out.print("White's move: ");
            } else {
                System.out.print("Black's move: ");
            }

            // take user input
            String in = reader.readLine().trim();

            // formatting as per instructions
            System.out.println();

            // always check for draw proposal first
            if (proposeDraw) {
                if (in.equals("draw")) {
                    finishGame(true, -1);
                }
                proposeDraw = false;
            }

            if (validInput(currTurn, in, chessBoard)) {

                // translate user input to board lang
                int[] boardMove = convertInput(in);
                int startR = boardMove[0];
                int startF = boardMove[1];
                int endR = boardMove[2];
                int endF = boardMove[3];

                // if user trying to move a null piece, invalid move
                if (chessBoard[startR][startF] == null) {
                    printIllegalMove();
                    continue;
                }

                // if user trying to move an opponent's piece, invalid move
                if (chessBoard[startR][startF].getTeamColor() != currTurn) {
                    printIllegalMove();
                    continue;
                }

                // endSquare = the square the user is trying to move their piece to
                ChessPiece endSquare = chessBoard[endR][endF];
                // check if piece can actually move to (endR, endF)
                if (chessBoard[startR][startF].movePiece(endR, endF, chessBoard)) {
                    // if moving piece causes currTurn's King to be in check, ask for another move
                    if (ChessPiece.existsCheck(currKing.getRank(), currKing.getFile(), currKing, chessBoard)) {
                        // undo move
                        chessBoard[endR][endF].setBoardWithMove(startR, startF, chessBoard);
                        chessBoard[endR][endF] = endSquare;
                        printIllegalMove();
                        continue;
                    }
                    // valid move performed, board was updated, print board
                    ChessBoard.printBoard(chessBoard);

                    // if is a valid move, then check for a Check to the other King for next round
                    King oppKing = findKing(oppositeTeam(currTurn), chessBoard);
                    if (oppKing != null
                            && ChessPiece.existsCheck(oppKing.getRank(), oppKing.getFile(), oppKing, chessBoard)) {
                        // we know other king is checked, check for checkmate
                        {
                            if (isCheckMate(oppKing, chessBoard)) {
                                if (oppositeTeam(currTurn) == ChessPiece.WHITE) {
                                    whiteCheckmated = true;
                                } else {
                                    blackCheckmated = true;
                                }
                                break;
                            }
                            // if not checkmate, then it's just check
                            else {
                                System.out.println();
                                System.out.println("Check");
                            }
                        }
                    }

                    currTurn = oppositeTeam(currTurn);
                } else {
                    printIllegalMove();
                    continue;
                }
            } else {
                printIllegalMove();
                continue;
            }

        }

        // when game over, we go to this branch. Either currTurn lost their king, or was
        // checkmated, so other team wins
        finishGame(false, currTurn);
    }

    /**
     * Checks whether a team is in checkmate
     * 
     * @param k          The king that is currently checked
     * @param chessBoard 2D array of ChessPieces that the game is currently using
     * @return True if king is in checkmate, False otherwise
     */
    public static boolean isCheckMate(King k, ChessPiece[][] chessBoard) {
        int turnInCheck = k.getTeamColor();

        ChessPiece savior = null;
        ChessPiece endSquareSavior = null;

        for (int r1 = 0; r1 < ChessBoard.N; r1++) {
            for (int f1 = 0; f1 < ChessBoard.N; f1++) {
                if (!ChessBoard.notOccupied(r1, f1, chessBoard) && chessBoard[r1][f1].getTeamColor() == turnInCheck) {
                    savior = chessBoard[r1][f1];

                    for (int r2 = 0; r2 < ChessBoard.N; r2++) {
                        for (int f2 = 0; f2 < ChessBoard.N; f2++) {

                            int startR = savior.getRank();
                            int startF = savior.getFile();
                            endSquareSavior = chessBoard[r2][f2];
                            ChessPiece[][] tempBoard = ChessBoard.cloneBoard(chessBoard);
                            King tK = findKing(turnInCheck, tempBoard);
                            if (savior.movePiece(r2, f2, tempBoard)) {
                                if (!ChessPiece.existsCheck(tK.getRank(), tK.getFile(), tK, tempBoard)) {
                                    // king is no longer checked if savior moves to (r2, f2), so undo move, not a
                                    // checkmate
                                    return false;
                                }
                                // if not return false, then it is a checkmate, but not a move yet so undo the
                                // move
                            }
                        }
                    }
                }
            }
        }

        // if did not return from inner loop, then no savior exists... this is a
        // CHECKMATE!
        System.out.println();
        System.out.println("Checkmate");
        return true;
    }

    /**
     * Finishes game by exiting program
     * 
     * @param draw      True if game ends on a draw, False otherwise
     * @param teamColor Color of team that won the game (ChessPiece.WHITE or
     *                  ChessPiece.BLACK)
     */
    public static void finishGame(boolean draw, int teamColor) {
        if (draw)
            System.out.println("draw");
        else if (teamColor == ChessPiece.WHITE) {
            System.out.println("White wins");
        } else if (teamColor == ChessPiece.BLACK)
            System.out.println("Black wins");

        System.exit(0); // terminates program when game ends
    }

    /**
     * Finds and returns king of current team color
     * 
     * @param team       Current turn's team color
     * @param chessBoard 2D array of ChessPieces representing chess board for the
     *                   game
     * @return King piece from current team color from the board
     */
    private static King findKing(int team, ChessPiece[][] chessBoard) {
        King k = null;
        for (int r = 0; r < ChessBoard.N; r++) {
            for (int f = 0; f < ChessBoard.N; f++) {
                if (!ChessBoard.notOccupied(r, f, chessBoard) && (chessBoard[r][f] instanceof King)
                        && (chessBoard[r][f].getTeamColor() == team)) {
                    k = (King) chessBoard[r][f];
                }
            }
        }
        return k;
    }

    /**
     * Check if King of turnColor team is still alive
     * 
     * @param turnColor  color of team with current turn
     * @param chessBoard 2D array of ChessPieces representing chess board for the
     *                   game
     * @return True if king is found and alive for team turnColor, False otherwise
     */
    public static boolean checkForKing(int turnColor, ChessPiece[][] chessBoard) {
        // return TRUE if a king is found for team turnColor
        // return FALSE if king is NOT FOUND for team turnColor
        King k = findKing(turnColor, chessBoard);

        // if no king was found, k stays null, meaning other team won
        if (k != null) {
            return true;
        } else {
            if (turnColor == ChessPiece.WHITE) {
                finishGame(false, ChessPiece.BLACK);
                return false;
            } else {
                finishGame(false, ChessPiece.WHITE);
                return false;
            }
        }
    }

    /**
     * Check if game is concluded (either team is in checkmate or current team's
     * king is dead)
     * 
     * @param turnColor  color of team with current turn
     * @param chessBoard 2D array of ChessPieces representing chess board for the
     *                   game
     * @return True if game is over, False otherwise
     */
    private static boolean gameOver(int turnColor, ChessPiece[][] chessBoard) {
        return !checkForKing(turnColor, chessBoard) || whiteCheckmated || blackCheckmated;
    }

    /**
     * Prints prompt to user to try another move because their current move is
     * illegal
     */
    private static void printIllegalMove() {
        System.out.println("Illegal move, try again");
        System.out.println();
    }

    /**
     * Inverts provided team color
     * 
     * @param currTurn Team color of current turn
     * @return Inverted team color of current turn (White -> Black or Black ->
     *         White)
     */
    private static int oppositeTeam(int currTurn) {
        if (currTurn == ChessPiece.WHITE) {
            return ChessPiece.BLACK;
        } else {
            return ChessPiece.WHITE;
        }
    }

    /**
     * Validate user's input based on resign, draw, promotion, etc.
     * 
     * @param turnColor  Color of team with current turn
     * @param in         User input for move
     * @param chessBoard 2D array of ChessPieces representing chess board for the
     *                   game
     * @return True if input was valid, False otherwise
     */
    public static boolean validInput(int turnColor, String in, ChessPiece[][] chessBoard) {
        // Check for resign
        if (in.equals("resign")) {
            if (turnColor == ChessPiece.WHITE) {
                finishGame(false, ChessPiece.BLACK);
            } else {
                finishGame(false, ChessPiece.WHITE);
            }
            // Check for draw
        } else if (in.contains("draw?")) {
            proposeDraw = true;
            return true;
            // e.g. "e2 e4"
        } else if (in.length() == 5) {
            int[] boardMove = convertInput(in);
            ChessPiece piece = chessBoard[boardMove[0]][boardMove[1]];
            if (piece == null) {
                return false;
            }
            // promoting pawn without argument
            if (piece instanceof Pawn && (piece.getTeamColor() == ChessPiece.BLACK && boardMove[2] == 7)
                    || (piece.getTeamColor() == ChessPiece.WHITE && boardMove[2] == 0)) {
                Pawn.promotionTo = 'Q';
                return true;
            }
            // else, just moving a piece like usual
            else {
                return true;
            }
        } else if (in.length() == 7) {
            Pawn.promotionTo = in.charAt(6);
            return true;
        }

        return false;
    }

    /**
     * Convert user input move of "FileRank FileRank" to board language
     * 
     * @param in User input for move
     * @return Integer Array of [beginRank, beginFile, endRank, endFile]
     */
    public static int[] convertInput(String in) {
        // input example:
        // FileRank FileRank
        // e2 e4
        // translates to...
        // (f0, r0) --> (fF, rF)
        // (1, 4) --> (3, 4)
        // convertedIn = {startRank, startFile, endRank, endFile}
        int[] convertedIn = new int[4];

        convertedIn[0] = 8 - Integer.parseInt(in.substring(1, 2));
        convertedIn[1] = letterToFile.get(in.substring(0, 1));
        convertedIn[2] = 8 - Integer.parseInt(in.substring(4, 5));
        convertedIn[3] = letterToFile.get(in.substring(3, 4));

        return convertedIn;
    }

}
