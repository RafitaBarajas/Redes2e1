
package e1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;


public class Client {
    
    static int pto = 1234;
    static String host = "localhost";

    public static void main(String[] args) {
        
        int opt;
        
        Tile[][] board9 = new Tile[9][9];
        Tile[][] board16 = new Tile[16][16];
        Tile[][] board30 = new Tile[16][30];
        
        String scores;
        
        try{
            Socket cl = new Socket(host, pto);
            DataOutputStream dos = new DataOutputStream(cl.getOutputStream());
            DataInputStream dis = new DataInputStream(cl.getInputStream());
            
            
            Scanner scan = new Scanner(System.in);
            
            System.out.println("¿Qué dificultad quieres?");
            System.out.println("1. Principiante"); //9x9
            System.out.println("2. Intermedio");   //16x16
            System.out.println("3. Experto");      //16x30
            System.out.println("0. Ver puntuaciones");
            
            opt = scan.nextInt();
            
            dos.write(opt);
            dos.flush();
            
            switch (opt) {
                case 0:
                    scores = dis.readUTF();
                    System.out.println(scores);
                    break;
                case 1:
                    board9 = tileNumbers( arrToTiles( getBoard(9, 9, dis) ) );
                    break;
                case 2:
                    board16 = tileNumbers( arrToTiles( getBoard(16, 16, dis) ) );
                    break;
                case 3:
                    board30 = tileNumbers( arrToTiles( getBoard(16, 30, dis) ) );
                    break;
            }
            
            cl.close();
            dos.close();
            dis.close();
        } catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    public static int[][] getBoard(int rows, int cols, DataInputStream dis){
        int[][] board = new int[rows][cols];
        
        try{
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    board[i][j] = dis.readInt(); //0 -> no bomba, -1 -> bomba
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return board;
    }
    
    public static Tile[][] arrToTiles(int[][] arr){
        Tile[][] b = new Tile[arr.length][arr[0].length];
        
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                b[i][j] = new Tile(0, arr[i][j]);
            }
        }
        
        return b;
    }
    
    public static Tile[][] tileNumbers(Tile[][] board){
        //Tile[][] b = new Tile[board.length][board[0].length];
        
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j].getNumber() == -1) {
                    if (i != 0) {
                        board[i - 1][j].plusOne();
                    }
                    if (i != board.length - 1) {
                        board[i + 1][j].plusOne();
                    }
                    
                    if (j != 0){
                        board[i][j - 1].plusOne();
                    }
                    if (j != board[0].length - 1){
                        board[i][j + 1].plusOne();
                    }
                    
                    if (i != 0 && j != 0) {
                        board[i - 1][j - 1].plusOne();
                    }
                    if (i != 0 && j != board[0].length - 1){
                        board[i - 1][j + 1].plusOne();
                    }
                    
                    if (i != (board.length - 1) && j != 0) {
                        board[i + 1][j - 1].plusOne();
                    }
                    if (i != (board.length - 1) && j != (board[0].length - 1)) {
                        board[i + 1][j + 1].plusOne();
                    }
                }
            }
        }
        
        return board;
    }
    
}
