
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
    
    public void printTile(){
        switch (status) {
            case 0:
                System.out.print("□ ");
                break;
            case 1:
                if(number == -1){
                    System.out.print("☼ ");
                } else {
                    System.out.print(number + " ");
                }
                break;
            case 2:
                System.out.print("■ ");
                break;
        }
    }
    
    public int open(boolean force){
        switch (status) {
            case 1: //The tile is already open
                return -2;
            case 2: //The tile has a flag
                if(force){
                    status = 1;
                    return number;
                }
                return -3;
            default:
                status = 1;
                return number;
        }
    }
    
    public void flag(){
        if (status == 0) {
            status = 2;
        } else if(status == 2){
            status = 0;
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
