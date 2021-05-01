package maze;

import java.util.Scanner;

public class Menu {
    private Scanner sc;

    public Menu(){
        sc = new Scanner(System.in);
    }

    private Maze maze;

    public int startingMenu(){
        System.out.println("=== Menu ===");
        System.out.println("1. Generate a new maze");
        System.out.println("2. Load a maze.");
        System.out.println("3. Save the maze");
        System.out.println("4. Display the maze.");
        System.out.println("5. Exit");

        int selection = -1;
        while (true) {
            String input = getUserInputStringLine();
            try {
                selection = Integer.parseInt(input);

                if ((selection <= 5) && (selection >= 1)) {
                    return selection;

                } else{
                    System.out.println("Incorrect option. Please try again");
                }


            } catch (Exception e) {
                System.out.println("Incorrect option. Please try again");
            }
        }
    }

    public String[] newMaze(){

        System.out.println("Enter the size of a new maze");

        String inputNumbers = getUserInputStringLine();
        String[] numbers = inputNumbers.split(" ");

        return numbers;


    }
    public String getFilename(){

        System.out.println("Enter filename");

        return getUserInputStringLine();

    }

    public String getUserInputStringLine(){
        System.out.print("> ");
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        return input;
    }
}
