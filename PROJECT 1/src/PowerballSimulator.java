import java.util.Scanner;
import java.util.Random;
public class PowerballSimulator {
    //public constant variables to be used in all methods:
    public static final int TICKET_COST = 2;//ticket price
    public static final int MAX_WHITE = 69;//max nb of white balls
    public static final int MAX_PB = 26;//max nb of power balls

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Random rand = new Random();
        int spent = 0;
        int won = 0;
        boolean play = true;
        while (play) {
            System.out.println("\nChoose:");
            System.out.println("1. Self pick ticket");
            System.out.println("2. Random tickets");
            System.out.println("3. Quit");
            String choice = sc.nextLine();

            if (choice.equals("1")) {
                int[] playerWhites = pickedWhites(sc);
                int playerPb = pickedPb(sc);

                int[] winningWhites = generateRandomWhites(rand);
                int winningPB = rand.nextInt(MAX_PB) + 1;

                int prize = calculatePrize(playerWhites, playerPb, winningWhites, winningPB);

            }
            else if (choice.equals("2")) {
                // TODO: implement 2
                System.out.println("Not implemented yet!");
            }
            else if (choice.equals("3")) {
                play = false;
            } else {
                System.out.println("Invalid choice.");
            }

        }
    }

    public static int[] pickedWhites(Scanner sc) {
        int[] whites = new int[5];
        int chosenBalls = 0;
        while (chosenBalls < 5) {
            System.out.print("Enter white ball #" + (chosenBalls + 1) + " (1–69): ");

            try {
                int ballNum = Integer.parseInt(sc.nextLine());

                // 1. check if ballNum in range
                if (ballNum > MAX_WHITE || ballNum < 1) {
                    throw new NumberFormatException("Error: Out of range!");
                }

                // 2. check if duplicate number chosen
                for (int i = 0; i < chosenBalls; i++) {
                    if (ballNum == whites[i]) {
                        throw new NumberFormatException("Error: Duplicate number!");
                    }
                }
                // if all good
                whites[chosenBalls] = ballNum;
                chosenBalls++;
            } catch (NumberFormatException e) {
                System.out.println(e.getMessage());
            }
        }
        return whites;
    }

    public static int pickedPb(Scanner sc) {
        int pbNum = 0;
        while (pbNum > MAX_PB || pbNum < 1) {
            System.out.println("Enter the powerball (1-26):");
            pbNum = Integer.parseInt(sc.nextLine());
            if (pbNum > MAX_PB || pbNum < 1) {
                System.out.println("Error: Out of range!");
            }
        }
        return pbNum;
    }

    public static int calculatePrize(int[] playerWhites, int playerPb, int[] winningWhites, int winningPB) {
        int whiteMatches = 0;

        // Count how many white balls match
        for (int pw : playerWhites) {
            for (int ww : winningWhites) {
                if (pw == ww) {
                    whiteMatches++;
                }
            }
        }

        boolean powerballMatch = (playerPb == winningPB);

        // Decide prize based on matches
        if (whiteMatches == 5 && powerballMatch) return 1000000000; // Jackpot placeholder
        if (whiteMatches == 5) return 1000000;
        if (whiteMatches == 4 && powerballMatch) return 50000;
        if (whiteMatches == 4) return 100;
        if (whiteMatches == 3 && powerballMatch) return 100;
        if (whiteMatches == 3) return 7;
        if (whiteMatches == 2 && powerballMatch) return 7;
        if (whiteMatches == 1 && powerballMatch) return 4;
        if (whiteMatches == 0 && powerballMatch) return 4;

        return 0; // no prize
    }
}



