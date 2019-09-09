
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
        int[][] board9 = new int[9][9];
        int[][] board16= new int[16][16];
        int[][] board30 = new int[16][30];
        
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
                    break;
                case 1:
                    board9 = getBoard(9, 9);
                    break;
                case 2:
                    board16 = getBoard(16, 16);
                    break;
                case 3:
                    board30 = getBoard(16, 30);
                    break;
                default:
                    break;
            }
            
            cl.close();
            dos.close();
        } catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    public static int[][] getBoard(int rows, int cols){
        int[][] board = new int[rows][cols];
        
        return board;
    }
    
    
    
}
