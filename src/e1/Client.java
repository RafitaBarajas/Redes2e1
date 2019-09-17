
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
        long time;
        
        Tile[][] board9 = new Tile[9][9];
        Tile[][] board16 = new Tile[16][16];
        Tile[][] board30 = new Tile[16][30];
        
        String scores;
        
        status = 0;
        
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
            
            time = System.nanoTime();
            
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
            
            time = System.nanoTime() - time;
            time = time / 1000000000; //nanoseconds to seconds
            
            dos.writeInt(status);
            dos.flush();
            
            if(status == 1){
                System.out.println("Tu tiempo: " + time + "s");
                System.out.println("Ingresa tu nombre:");
                
                dos.writeUTF(scan.nextLine() + " " + time);
                dos.flush();
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
        boolean flag;
        String in;
        
        status = 0;
        flag = false;
        
        clearOutput();
        printBoard(board);
        
        while(status == 0){
            
            if(flag)
                System.out.println("ESTÁS EN MODO BANDERA");
           
            System.out.println("(Ingresa una F si quieres activar o desactivar el modo bandera)");
            System.out.println(" Elige una fila.");
            in = s.nextLine();
            
            if(in.equals("F") || in.equals("f")){
                flag = !flag;
                clearOutput();
                printBoard(board);
            } else {
                row = Integer.parseInt(in);

                System.out.println(" Elige una columna.");
                col = s.nextInt();

                if(flag){
                    board[row][col].flag();
                    clearOutput();
                    printBoard(board);
                } else {
                
                    num = board[row][col].open(false);
                    switch (num){
                        case -1:
                            clearOutput();
                            printBoard(board);
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
                                    clearOutput();
                                    printBoard(board);
                                    System.out.println("BOOM KAPOOOM KATAPLUM");
                                    System.out.println("Perdiste.");
                                    status = -1;
                                    break;
                                }
                            } else {
                                break;
                            }
                        default:
                            if(num == 0){
                                openAround(board, row, col);
                            }
                            clearOutput();
                            printBoard(board);

                            status = checkWin(board);

                            if(status == 1){
                                System.out.println("You did it, you crazy son of a ***** you did it.");
                            }
                                                        
                    }
                    
                }
                
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
    
    public static int checkWin(Tile[][] board){
        int nMines = 0;
        int nOpenTiles = 0;
        
        switch (board[0].length) {
            case 9:
                nMines = 10;
                break;
            case 16:
                nMines = 40;
                break;
            case 30:
                nMines = 99;
                break;
        }
        
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if(board[i][j].getStatus() == 1){
                    nOpenTiles++;
                }
            }
        }
        
        if(nOpenTiles == ((board.length * board[0].length) - nMines)){
            return 1;
        } else {
            return 0;
        }
        
    }
    
    public static void openAround(Tile[][] b, int r, int c){
        
        if (r != 0)
            if (b[r - 1][c].open(true) == 0)
                openAround(b, (r - 1), c);
        
        if (r != b.length - 1)
            if (b[r + 1][c].open(true) == 0)
                openAround(b, (r + 1), c);
        
        
        if (c != 0)
            if (b[r][c - 1].open(true) == 0)
                openAround(b, r, (c - 1));
                
        if (c != b[0].length - 1)
            if (b[r][c + 1].open(true) == 0)
                openAround(b, r, (c + 1));
        
        
        if (r != 0 && c != 0)
            if (b[r - 1][c - 1].open(true) == 0)
                openAround(b, (r - 1), (c - 1));
        
        if (r != 0 && c != b[0].length - 1)
            if (b[r - 1][c + 1].open(true) == 0)
                openAround(b, (r - 1), (c + 1));
        

        if (r != (b.length - 1) && c != 0)
            if (b[r + 1][c - 1].open(true) == 0)
                openAround(b, (r + 1), (c - 1));
        
        if (r != (b.length - 1) && c != (b[0].length - 1))
            if (b[r + 1][c + 1].open(true) == 0)
                openAround(b, (r + 1), (c + 1));        
    }
    
    public static void clearOutput() {  
        System.out.print("\033[H\033[2J");  
        System.out.flush();  
    }  
}
