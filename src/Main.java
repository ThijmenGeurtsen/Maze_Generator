package maze;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class Main {
    private maze.Maze maze;
    public void runProgram(){

        int selectedOption = -1;
        while(selectedOption != 5) {

            maze.Menu menu = new maze.Menu();
            selectedOption = menu.startingMenu();


            switch (selectedOption){

                //generate Maze
                case 1: {
                    String[] numbers = menu.newMaze();

                    int width = Integer.parseInt(numbers[0]);
                    int length = Integer.parseInt(numbers[1]);

                    maze = new maze.Maze(length, width);
                    maze.generateMaze();
                    maze.displayMaze();
                    break;
                }

                //Load maze
                case 2:{
                    String filename = menu.getFilename();
                    if (checkFileExists(filename)){
                        maze.Maze newMaze = new maze.Maze(0,0);
                        newMaze.loadMaze(filename);
                        maze = newMaze;
                    } else{
                        System.out.println("The file "+filename+" does not exist");
                    }

                    break;
                }


                //Save maze
                case 3:{
                    if(ifInputContainsMaze(maze)){
                        String filename = menu.getFilename();
                        maze.saveMaze(filename);


                    } else{
                        System.out.println("No maze has been loaded or been generated.");
                    }
                    break;

                }

                //Display Maze
                case 4:{
                    if(ifInputContainsMaze(maze)){
                        maze.displayMaze();

                    } else{
                        System.out.println("No maze has been loaded or been generated.");
                    }
                    break;
                }

                //exit
                case 5:{
                    System.out.println("Bye!");
                    break;
                }
            }

        }
    }


    public boolean ifInputContainsMaze(maze.Maze maze){
        try {

            if (maze.checkIfMazeContainsPixels()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e){
            return false;
        }

    }

    public boolean checkFileExists(String filename){
        boolean exists = Files.exists(Path.of(filename));
        return exists;

    }


}

