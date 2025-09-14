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
                // Update totals
                spent += TICKET_COST;
                won += prize;

                // Print results
                System.out.print("Winning numbers: ");
                for (int i = 0; i < winningWhites.length; i++) {
                    System.out.print(winningWhites[i] + " ");
                }
                System.out.println("PB:" + winningPB);

                System.out.print("Your numbers: ");
                for (int i = 0; i < playerWhites.length; i++) {
                    System.out.print(playerWhites[i] + " ");
                }
                System.out.println("PB:" + playerPb + " -> Prize: $" + prize);

                System.out.println("Spent $" + spent + ", Won $" + won + ", Total Won or Lost $" + (won - spent));

            } else if (choice.equals("2")) {
                System.out.print("How many random tickets do you want to buy? ");
                int ticketCount = Integer.parseInt(sc.nextLine());

                // Generate winning numbers
                int[] winningWhites = generateRandomWhites(rand);
                int winningPB = rand.nextInt(MAX_PB) + 1;

                System.out.print("Winning numbers: ");

                for (int w = 0; w < 5; w++)
                    System.out.print(winningWhites[w] + " ");
                System.out.println("PB:" + winningPB);

                int roundWinnings = 0;
                // Generate and check each ticket
                for (int t = 0; t < ticketCount; t++) {
                    int[] playerWhites = generateRandomWhites(rand);
                    int playerPB = rand.nextInt(MAX_PB) + 1;

                    int prize = calculatePrize(playerWhites, playerPB, winningWhites, winningPB);
                    roundWinnings += prize;

                    // Print ticket numbers
                    System.out.print("Ticket #" + (t + 1) + ": ");
                    for (int num : playerWhites) {
                        System.out.print(num + " ");
                    }
                    System.out.println("PB:" + playerPB + " -> Prize: $" + prize);
                }

                // Update totals
                spent += ticketCount * TICKET_COST;
                won += roundWinnings;

                System.out.println("Spent $" + spent + ", Won $" + won + ", Total Won or Lost $" + (won - spent));

            } else if (choice.equals("3")) {
                play = false;
                System.out.println("\nQuitting Powerball Simulator...");
                System.out.println("Final Summary: ");
                System.out.println("Total spent: $" + spent);
                System.out.println("Total won: $" + won);

                if (won >= spent) {
                    System.out.println("You profited: $" + (won - spent));
                } else {
                    System.out.println("You lost: $" + (spent - won));
                }
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
            try {
                System.out.println("Enter the powerball (1-26):");
                pbNum = Integer.parseInt(sc.nextLine());
                if (pbNum > MAX_PB || pbNum < 1) {
                    System.out.println("Error: Out of range!");
                }
            }
            catch (NumberFormatException e) {
                System.out.println(e.getMessage() + ": Error, try again!");
            }
        }
        return pbNum;
    }

    public static int[] generateRandomWhites(Random rand) {
        int[] randomWhites = new int[5];
        int index = 0;

        while (index < 5) {
            int ballNum = rand.nextInt(MAX_WHITE) + 1; // 1–69
            boolean duplicate = false;

            for (int i = 0; i < index; i++) {
                if (ballNum == randomWhites[i]) {
                    duplicate = true;
                    break;
                }
            }
            if (!duplicate) {  // only add if not duplicate
                randomWhites[index] = ballNum;
                index++;
            }
        }
        return randomWhites;
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



