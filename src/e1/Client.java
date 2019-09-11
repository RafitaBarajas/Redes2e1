
package e1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;


public class Client {
    
    static int pto = 1234;
    static String host = "localhost";

    public static void main(String[] args) {
        
        int opt, status;
        
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
            System.out.println(" 1. Principiante ( 9 x  9 casillas y 10 minas)");
            System.out.println(" 2. Intermedio   (16 x 16 casillas y 40 minas)");
            System.out.println(" 3. Experto      (16 x 30 casillas y 99 minas)");
            System.out.println("");
            System.out.println(" 4. Ver puntuaciones");
            
            opt = scan.nextInt();
            
            if(opt == 4){
                opt = 0;
            }
            
            dos.write(opt);
            dos.flush();
            
            switch (opt) {
                case 4:
                    scores = dis.readUTF();
                    System.out.println(scores);
                    break;
                case 1:
                    board9 = tileNumbers( arrToTiles( getBoard(9, 9, dis) ) );
                    status = play(board9, scan);
                    break;
                case 2:
                    board16 = tileNumbers( arrToTiles( getBoard(16, 16, dis) ) );
                    status = play(board16, scan);
                    break;
                case 3:
                    board30 = tileNumbers( arrToTiles( getBoard(16, 30, dis) ) );
                    status = play(board30, scan);
                    break;
            }
            
            //regresar si ganó o perdió.
            //tomar tiempo desde que empezó a jugar
            //si gana mandar tiempo.
            
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
    
    public static int play(Tile[][] board, Scanner s){
        
        int row, col;
        int opc, num;
        int status; // 0 -> sigue vivo, 1 -> ganó, -1 -> perdió
        
        
        status = 0;
        
        printBoard(board);
        
        while(status == 0){
            System.out.println("Elige una fila.");
            row = s.nextInt();

            System.out.println("Elige una columna.");
            col = s.nextInt();

            num = board[row][col].open(false);
            switch (num){
                case -1:
                    System.out.println("BOOM KAPOOOM KATAPLUM");
                    System.out.println("Perdiste.");
                    status = -1;
                    break;
                case -2:
                    System.out.println("Esa casilla ya está abierta.");
                    break;
                case -3:
                    System.out.println("La casilla está marcada con una bandera.");
                    System.out.println("¿Estás seguro de que quieres abrirla?");
                    System.out.println(" 1. Sí");
                    System.out.println(" 2. No");
                    opc = s.nextInt();

                    if(opc == 1){
                        num = board[row][col].open(true);
                        if(num == -1){
                            System.out.println("BOOM KAPOOOM KATAPLUM");
                            System.out.println("Perdiste.");
                            status = -1;
                            break;
                        }
                    } else {
                        break;
                    }
                default:
                    //Pues le dió a un número jaja
                    //checar si es cero se destapen las de alrededor
                    //checar si ya ganó (status = 1), contando las casillas que quedan cerradas y las minas totales
                    //falta modo bandera.
                    //ver lo de clearear el output cada que mete una casilla
            }

        }
        
        return status;
    }
    
    public static void printBoard(Tile[][] board){
        System.out.print("  \t");
        for (int i = 0; i <= board[0].length; i++) {
            if(i < 10){
                System.out.print(" ");
            }
            System.out.print(i + " ");
        }
        System.out.println("");
        
        for (int i = 0; i < board.length; i++) {
            if(i < 10){
                System.out.print(" ");
            }
            System.out.print(" " + i + "\t");
            
            for (int j = 0; j < board[0].length; j++) {
                System.out.print(" ");
                board[i][j].printTile();
            }
            System.out.println("");
        }
        System.out.println("");
        
    }
    
}
