package com.example.pentago;

import androidx.annotation.NonNull;

public class Board {
    public static final int MisparShurot = 6;
    public static final int MisparAmudot = 6;
    int[][] gameBoard;

    public Board() {
        this.gameBoard = new int[MisparShurot][MisparAmudot];// מאתחל את המערך
        initBoard();// אתחול הלוח
    }

    public Board (String boardString){
        this.gameBoard = new int[MisparShurot][MisparAmudot];// מאתחל את המערך

        String[] boardStringRows = boardString.split("n"); // מפצלים את השורה לכמה שורות
        for (int i =0; i< boardStringRows.length; i++) {// עבור כל שורה
            String [] rowCells = boardStringRows[i].split("\\.");// מורידים את הנקודה שבין כל תא
            this.gameBoard[i] = stringToInt(rowCells);
        }
    }

    private int[] stringToInt(String [] row){
        int[] array = new int[row.length];
        for (int i = 0; i < array.length; i++) {
            array[i] = Integer.parseInt(row[i]); // הופכים כל תא למספר
        }
        return array; // מחזירים את השורה כשהכל מספרים
    }

    // אתחול הלוח
    public void initBoard() {
        for (int i = 0; i < gameBoard.length; i++) {
            for (int k = 0; k < gameBoard[i].length; k++) {
                gameBoard[i][k] = 0;
            }
        }
    }

    public int getValue(int row, int col) {
        return this.gameBoard[row][col];
    }


    public boolean isMoveHoki(int i, int k) {
        if (gameBoard[i][k] == 0)
            return true;
        return false;
    }

    public void turnBoard(int boardChosenToTurn, int turnDirection){
        int [][] mat = new int[3][3]; //יצירת לוח נוסף שנמצא בצד שבו הלוח יסתובב
        int[][] Arrynew;
        CopyMatrix(mat, boardChosenToTurn-1);// מעתיקה לmat (הלוח החדש שמסתובב) את הלוח שנבחר
        if(turnDirection==1)// אם הכיוון הנבחר הוא עם כיוון השעון אז לסובב ימינה, אחרת לסובב שמאלה
            Arrynew=rotateMatrixRight(mat);
        else
            Arrynew=rotateMatrixLeft(mat);

        PasteMatrix(Arrynew,boardChosenToTurn-1);// להדביק את הלוח המסובב בחזרה במקום
    }

    public void CopyMatrix(int[][] mat, int BoardNum) { // העתקת כל אחד מארבעת הלוחות ללוח שנמצא מחוץ למשחק ובו הלוח מסתובב
        int halfBoardLength = 3;
        int fullBoardLength = halfBoardLength * 2;
        switch (BoardNum) {
            case 0:
                for (int i = 0; i < halfBoardLength; i++) {
                    for (int j = 0; j < halfBoardLength; j++) {
                        mat[i][j] = gameBoard[i][j];
                    }
                }
                break;
            case 1:
                for (int i = halfBoardLength; i < fullBoardLength; i++) {
                    for (int j = 0; j < halfBoardLength; j++) {
                        mat[i - halfBoardLength][j] = gameBoard[i][j];
                    }
                }
                break;
            case 2:
                for (int i = 0; i < halfBoardLength; i++) {
                    for (int j = halfBoardLength; j < fullBoardLength; j++) {
                        mat[i][j - halfBoardLength] = gameBoard[i][j];
                    }
                }
                break;
            case 3:
                for (int i = halfBoardLength; i < fullBoardLength; i++) {
                    for (int j = halfBoardLength; j < fullBoardLength; j++) {
                        mat[i - halfBoardLength][j - halfBoardLength] = gameBoard[i][j];
                    }
                }
                break;
            default:
                break;
        }
    }

    public void PasteMatrix(int[][] mat, int BoardNum) { // החזרת הלוח המועתק והמסובב למקומו במשחק
        int halfBoardLength = 3;
        int fullBoardLength = halfBoardLength * 2;
        switch (BoardNum) {
            case 0:
                for (int i = 0; i < halfBoardLength; i++) {
                    for (int j = 0; j < halfBoardLength; j++) {
                        gameBoard[i][j] = mat[i][j];
                    }
                }
                break;
            case 1:
                for (int i = halfBoardLength; i < fullBoardLength; i++) {
                    for (int j = 0; j < halfBoardLength; j++) {
                        gameBoard[i][j] = mat[i - halfBoardLength][j];
                    }
                }
                break;
            case 2:
                for (int i = 0; i < halfBoardLength; i++) {
                    for (int j = halfBoardLength; j < fullBoardLength; j++) {
                        gameBoard[i][j] = mat[i][j - halfBoardLength];
                    }
                }
                break;
            case 3:
                for (int i = halfBoardLength; i < fullBoardLength; i++) {
                    for (int j = halfBoardLength; j < fullBoardLength; j++) {
                        gameBoard[i][j] = mat[i - halfBoardLength][j - halfBoardLength];
                    }
                }
                break;
            default:
                break;
        }
    }

    public int[][] rotateMatrixRight(int[][] matrix) { // סיבוב הלוח ימינה על ידי העתקת כל כפתור למיקום החדש שלו בלוח
        int w = matrix.length;
        int h = matrix[0].length;
        int[][] ret = new int[h][w];
        for (int i = 0; i < h; ++i) {
            for (int j = 0; j < w; ++j) {
                ret[i][j] = matrix[w - j - 1][i];
            }
        }
        return ret;
    }

    public int[][] rotateMatrixLeft(int[][] matrix) {// סיבוב הלוח שמאלה על ידי העתקת כל כפתור למיקום החדש שלו בלוח
        int w = matrix.length;
        int h = matrix[0].length;
        int[][] ret = new int[h][w];
        for (int i = 0; i < h; ++i) {
            for (int j = 0; j < w; ++j) {
                ret[i][j] = matrix[j][h - i - 1];
            }
        }
        return ret;
    }

    //בדיקת ניצחון
    public boolean Checkwin(int PlayerNum) {

        //בדיקה רוחבית
        for (int j = 0; j < MisparShurot - 4; j++) {
            for (int i = 0; i < MisparShurot; i++) {
                if (gameBoard[i][j] == PlayerNum && gameBoard[i][j + 1] == PlayerNum &&
                        gameBoard[i][j + 2] == PlayerNum && gameBoard[i][j + 3] == PlayerNum && gameBoard[i][j + 4] == PlayerNum)
                    return true;
            }
        }

        // בדיקה אנכית
        for (int i = 0; i < MisparShurot - 4; i++) {
            for (int k = 0; k < MisparShurot; k++) {
                if (gameBoard[i][k] == PlayerNum && gameBoard[i + 1][k] == PlayerNum &&
                        gameBoard[i + 2][k] == PlayerNum && gameBoard[i + 3][k] == PlayerNum && gameBoard[i + 4][k] == PlayerNum)
                    return true;
            }
        }

        // בדיקת אלכסון למעלה
        for (int i = 4; i < MisparShurot; i++) {
            for (int l = 0; l < MisparShurot - 4; l++) {
                if (gameBoard[i][l] == PlayerNum && gameBoard[i - 1][l + 1] == PlayerNum &&
                        gameBoard[i - 2][l + 2] == PlayerNum && gameBoard[i - 3][l + 3] == PlayerNum && gameBoard[i - 4][l + 4] == PlayerNum)
                    return true;
            }
        }

        // בדיקת אלכסון למטה
        for (int i = 4; i < MisparShurot; i++) {
            for (int m = 4; m < MisparShurot; m++) {
                if (gameBoard[i][m] == PlayerNum && gameBoard[i - 1][m - 1] == PlayerNum &&
                        gameBoard[i - 2][m - 2] == PlayerNum && gameBoard[i - 3][m - 3] == PlayerNum && gameBoard[i - 4][m - 4] == PlayerNum)
                    return true;
            }
        }
        return false;
    }

    public boolean makeMove(int i, int k, int currentPlayer) {
        if (isMoveHoki(i, k) == true) {
            gameBoard[i][k] = currentPlayer;
            return true;
        }
        return false;
    }
    @NonNull
    @Override
    public String toString() {
        String string = "";
        for (int i = 0; i < gameBoard.length; i++) {
            for (int j = 0; j < gameBoard[i].length; j++) {
                string+=String.valueOf(gameBoard[i][j])+".";// מוסיפים למחרוזת שאנו יוצרים את הערך של התא ובסוף נקודה(זה מסמל סיום תא)
            }
            string+="n";//מוסיפים את האות N בסוף כל שורה בקובץ (היא מסמלת ירידת שורה בלוח)
        }

        return string;// בסוף יצא לנו משהו כמו:
        //0.0.0.0.0.0n0.0.0.0.0.0n0.0.0.0.0.0n
    }


}
