package maze;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Maze {
    private int width;
    private int height;


    Maze(int x, int y) {
        this.width = x;
        this.height = y;
    }

    //the maze it self
    private maze.Pixel[][] maze;

    //used to set the pixel or on
    int pixelOff = 0;
    int pixelOn = 1;


    Random random = new Random();


    int startPosX = 1;
    int startPosY = 1;
    //the start position of the maze generator
    int selPixelX = startPosX;
    int selPixelY = startPosY;


    //set to 4 because it will make a opening behind the selected pixel
    char direction = 'S';


    private maze.Pixel getPixel(int posX, int posY) {
        return maze[posX][posY];
    }

    public void generateMaze() {
        int oldWidth = width;
        int oldLength = height;
        setBetterSizeMaze();

        maze = new maze.Pixel[width][height];

        addPixels();



        //set pixel under selPixel off
        // done for the first pixel
        maze.Pixel pixel = getPixel(selPixelX, selPixelY);
        pixel.setDisplayPixel(pixelOff);


        while (true) {
            iterateGeneration();
            if (testIfAtStart()==false){
                break;
            }

            //tmpDirection = direction;
            while ((direction == 'X'&&testIfAtStart()==true)){
                direction = getDir();
                if (direction == 'X') {
                    traceBack();
                }

            }
        }


        int exitX = (width - 1);
        int exitY = 1;
        maze.Pixel pixelEnd = getPixel(exitX, exitY);
        pixelEnd.setDisplayPixel(pixelOff);

        int entranceX = 0;
        int entranceY = 1;
        maze.Pixel pixelEntrance = getPixel(entranceX, entranceY);
        pixelEntrance.setDisplayPixel(pixelOff);

        //the maze could have been resized so we need to add extra lines so it is normal again.

        if (width!=oldWidth || height != oldLength){
            maze.Pixel[][] newMaze = new maze.Pixel[oldWidth][oldLength];
            changeBackToOrginalSize(newMaze,oldWidth,oldLength); // add borders to the maze
            width = oldWidth;
            height = oldLength;

            //add new exit
            exitX = (width - 1);
            exitY = 1;
            maze.Pixel pixelEnd2 = getPixel(exitX, exitY);
            pixelEnd2.setDisplayPixel(pixelOff);


        }


    }

    private void setBetterSizeMaze(){
        if ((width%2==0)){
            width = width - 1;
        }

        if ((height %2)==0){
            height = height - 1;
        }
    }

    private void changeBackToOrginalSize(maze.Pixel[][] newMaze, int oldWidth, int oldLength){

        for (int posY = 0; posY < oldLength; posY++) {
            for (int posX = 0; posX < oldWidth; posX++) {

                //get cell from the cut maze or generate a new cell
                maze.Pixel pixel;
                try {
                    pixel = maze[posX][posY];;
                } catch (Exception e){
                    pixel = new maze.Pixel(pixelOn);
                }

                newMaze[posX][posY] = pixel;
            }

        }
        maze = newMaze;



    }

    private boolean testIfAtStart(){

        if ((selPixelX!=1)||(selPixelY!=1)){
            return true;
        } else{
            return false;
        }
    }

    public void displayDevMaze() {

        //make question mark pixel
        maze.Pixel pixel2 = getPixel(selPixelX, selPixelY);
        int oldValue = pixel2.getDisplayPixel();
        pixel2.setDisplayPixel(-1);


        for (int posY = 0; posY < height; posY++) {
            System.out.println();

            for (int posX = 0; posX < width; posX++) {

                maze.Pixel pixel = maze[posX][posY];

                if (pixel.getDisplayPixel() == -1) {
                    System.out.print("??");
                } else if (pixel.getTraceBackPixel()=='N'){
                    System.out.print("NN");
                } else if (pixel.getTraceBackPixel()=='W'){
                    System.out.print("WW");
                } else if (pixel.getTraceBackPixel()=='S'){
                    System.out.print("SS");
                } else if (pixel.getTraceBackPixel()=='E'){
                    System.out.print("EE");



                } else if (pixel.getDisplayPixel() == 1) {
                    System.out.print("\u2588\u2588");
                } else if (pixel.getDisplayPixel() == 0) {
                    System.out.print("  ");
                }


            }

        }

        pixel2.setDisplayPixel(oldValue);
        System.out.println();
        System.out.println();
        System.out.println();
    }

    private void iterateGeneration(){
        /*
        1. set pixels off behind and under the selected pixels
        2. set new direction and the traceback direction is calculated
        3. the selPixel moves forward to the generated direction
        */

        direction = getDir();
        //set new location
        selPixelX = selPixelX + (getDirX(direction) * 2);
        selPixelY = selPixelY + (getDirY(direction) * 2);


        //set pixel under selPixel off
        maze.Pixel pixel = getPixel(selPixelX, selPixelY);
        pixel.setDisplayPixel(pixelOff);

        //set pixel behind selPixel off

        maze.Pixel infrontPixel = getPixel(
                (selPixelX - getDirX(direction))
                , (selPixelY - getDirY(direction)));
        infrontPixel.setDisplayPixel(pixelOff);



        //calc and set traceback value

        char directionTraceBack = 'X';
        switch (direction){
            case 'N':
                directionTraceBack = 'S';
                break;
            case 'E':
                directionTraceBack = 'W';
                break;
            case 'S':
                directionTraceBack = 'N';
                break;
            case 'W':
                directionTraceBack = 'E';
                break;
        }

        //only update pixel if it's not X
        if (directionTraceBack != 'X'){
            pixel.setTraceBackPixel(directionTraceBack);
        }
    }

    public void traceBack() {


        maze.Pixel pixel = getPixel(selPixelX, selPixelY);
        char traceBackDir = pixel.getTraceBackPixel();



        switch (traceBackDir){
            case 'N':
                selPixelY = selPixelY + 2;
                break;

            case 'E':
                selPixelX = selPixelX + 2;
                break;

            case 'S':
                selPixelY = selPixelY - 2;
                break;

            case 'W':
                selPixelX = selPixelX - 2;
                break;
        }

    }

    private char getDir() {
        /*
        will generate a dir that is possible
        if no dir is possible it will return -1

        1. first check for all valid options
        2. if the list is empty it will return -1
        3. returns and generates a random value that is in validDirections list
        */

        List<Character> validDirections = new ArrayList<Character>();

        //get pixels from directions
        try {
            maze.Pixel pixelNorth = getPixel(selPixelX, selPixelY + 2);
            if (pixelNorth.getDisplayPixel() == 1) {
                validDirections.add('N');
            }
        } catch (Exception e) {
        }

        try {
            maze.Pixel pixelEast = getPixel(selPixelX - 2, selPixelY);
            if (pixelEast.getDisplayPixel() == 1) {
                validDirections.add('W');
            }
        } catch (Exception e) {
        }


        try {
            maze.Pixel pixelSouth = getPixel(selPixelX, selPixelY - 2);
            if (pixelSouth.getDisplayPixel() == 1) {
                validDirections.add('S');
            }
        } catch (Exception e) {
        }

        try {
            maze.Pixel pixelWest = getPixel(selPixelX + 2, selPixelY);
            if (pixelWest.getDisplayPixel() == 1) {
                validDirections.add('E');
            }
        } catch (Exception e) {
        }

        //return -1 if validDirections == 0 because if the list is empty it can't go anywhere
        if (validDirections.size() == 0) {
            return 'X';

        }

        //generate value that is in the validDirections list
        int index = random.nextInt(validDirections.size());
        //int index = (int) (Math.random() * validDirections.size());
        return validDirections.get(index);
    }

    //gets direction input and return the dir
    private int getDirX(int dir) {

        if (dir == 'E') {
            return 1;

        } else if (dir == 'W') {
            return -1;
        }

        return 0;
    }

    private int getDirY(int dir) {

        if (dir == 'N') {
            return 1;

        } else if (dir == 'S') {
            return -1;

        }

        return 0;
    }

    //fils maze  with pixel objects
    private void addPixels() {

        for (int posY = 0; posY < height; posY++) {
            for (int posX = 0; posX < width; posX++) {
                maze[posX][posY] = new maze.Pixel(pixelOn);

            }

        }

    }

    public void displayMaze() {
        for (int posY = 0; posY < height; posY++) {
            System.out.println();

            for (int posX = 0; posX < width; posX++) {

                maze.Pixel pixel = maze[posX][posY];

                if (pixel.getDisplayPixel() == 1) {
                    System.out.print("\u2588\u2588");
                } else if (pixel.getDisplayPixel() == 0) {
                    System.out.print("  ");
                }

            }
        }
        System.out.println("");
    }


    public boolean checkIfMazeContainsPixels(){

        try{
            maze.Pixel pixel = maze[0][0];
            return true;

        } catch (Exception e){
            return false;
        }

    }

    public void saveMaze(String filename){
        File file = new File(filename);
        //make file

        try {
            file.createNewFile();
        }  catch (Exception e){}



        //change maze pixel data into a string
        StringBuilder builder = new StringBuilder();

        for (int posY = 0; posY < height; posY++) {

            //won't add a enter on the first line
            if (posY!= 0) {
                builder.append("\n");
            }

            for (int posX = 0; posX < width; posX++) {

                maze.Pixel pixel = maze[posX][posY];

                if (pixel.getDisplayPixel() == 1) {
                    builder.append("1");
                } else if (pixel.getDisplayPixel() == 0) {
                    builder.append("0");
                }
            }
        }
        String mazeData = builder.toString();

        //save data
        try(FileOutputStream fos = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fos)) {
            //convert string to byte array
            byte[] bytes = mazeData.getBytes();
            //write byte array to file
            bos.write(bytes);
            bos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void loadMaze(String filename){
        ArrayList<String> mazeString = new ArrayList<>();

        //add loaded maze file into the
        try {

            File file = new File(filename);

            BufferedReader br = new BufferedReader(new FileReader(file));

            String string;



            while ((string = br.readLine()) != null) {
                mazeString.add(string);
            }


        }catch (Exception e){
            e.printStackTrace();
        }

        width = mazeString.get(0).length();
        height = mazeString.size();
        maze = new maze.Pixel[width][height];


        for (int posY = 0; posY < height; posY++) {
            String line = mazeString.get(posY);
            for (int posX = 0; posX < width; posX++) {
                int pixelData = Integer.parseInt(String.valueOf(line.charAt(posX)));



                maze[posX][posY] = new maze.Pixel(pixelData);



            }
        }

    }
}








