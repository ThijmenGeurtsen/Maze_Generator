package maze;

public class Pixel {
    private int displayPixel; // make this 0 or 1
    private char traceBackPixel;


    Pixel(int displayPixel){
        this.displayPixel = displayPixel;
    }

    public int getDisplayPixel(){
        return displayPixel;

    }

    public void setDisplayPixel(int newValue){
        displayPixel = newValue;
    }

    public char getTraceBackPixel(){
        return traceBackPixel;

    }

    public void setTraceBackPixel(char newValue){
        traceBackPixel = newValue;
    }
}
