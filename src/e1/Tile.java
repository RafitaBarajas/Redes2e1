
package e1;


public class Tile {
    
    private int status;// 0 -> Closed, 1 -> Open, 2 -> Flag
    private int number;// -1 -> Mine
    
    public Tile (){
        status = 0;
        number = 0;
    }
    
    public Tile (int s, int num){
        status = s;
        number = num;
    }
    
    public void plusOne(){
        if(number != -1){
            number++;
        }
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public int getNumber() {
        return number;
    }
    
    
    
}
