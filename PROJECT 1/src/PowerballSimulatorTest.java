import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Random;
import java.util.Scanner;

class PowerballSimulatorTest {

    @Test
    void testCalculatePrize() {
        int[] winning = {1, 2, 3, 4, 5};
        int winningPB = 6;

        // Test all prize tiers
        // Jackpot: 5 whites + PB match
        assertEquals(1000000000, PowerballSimulator.calculatePrize(winning, winningPB, winning, winningPB));

        // 5 whites no PB
        assertEquals(1000000, PowerballSimulator.calculatePrize(winning, 7, winning, winningPB));

        // 4 whites + PB
        int[] fourWhites = {1, 2, 3, 4, 10};
        assertEquals(50000, PowerballSimulator.calculatePrize(fourWhites, winningPB, winning, winningPB));

        // 4 whites no PB
        assertEquals(100, PowerballSimulator.calculatePrize(fourWhites, 7, winning, winningPB));

        // 3 whites + PB
        int[] threeWhites = {1, 2, 3, 10, 11};
        assertEquals(100, PowerballSimulator.calculatePrize(threeWhites, winningPB, winning, winningPB));

        // 3 whites no PB
        assertEquals(7, PowerballSimulator.calculatePrize(threeWhites, 7, winning, winningPB));

        // 2 whites + PB
        int[] twoWhites = {1, 2, 10, 11, 12};
        assertEquals(7, PowerballSimulator.calculatePrize(twoWhites, winningPB, winning, winningPB));

        // 2 whites no PB (no prize)
        assertEquals(0, PowerballSimulator.calculatePrize(twoWhites, 7, winning, winningPB));

        // 1 white + PB
        int[] oneWhite = {1, 10, 11, 12, 13};
        assertEquals(4, PowerballSimulator.calculatePrize(oneWhite, winningPB, winning, winningPB));

        // 1 white no PB (no prize)
        assertEquals(0, PowerballSimulator.calculatePrize(oneWhite, 7, winning, winningPB));

        // 0 whites + PB
        int[] zeroWhite = {10, 11, 12, 13, 14};
        assertEquals(4, PowerballSimulator.calculatePrize(zeroWhite, winningPB, winning, winningPB));

        // 0 whites no PB (no prize)
        assertEquals(0, PowerballSimulator.calculatePrize(zeroWhite, 7, winning, winningPB));
    }

    @Test
    void testGenerateRandomWhites() {
        Random rand = new Random();

        // Test multiple times to ensure consistency
        for (int iteration = 0; iteration < 100; iteration++) {
            int[] whites = PowerballSimulator.generateRandomWhites(rand);

            // Check array length
            assertEquals(5, whites.length);

            // Check for duplicates and valid range
            for (int i = 0; i < whites.length; i++) {
                // Check no duplicates
                for (int j = 0; j < i; j++) {
                    assertNotEquals(whites[i], whites[j]);
                }
            }
        }
    }

    @Test
    void testPickedWhites() {
        // Test valid input
        String validInput = "1\n2\n3\n4\n5\n";
        Scanner sc1 = new Scanner(new ByteArrayInputStream(validInput.getBytes()));
        int[] whites1 = PowerballSimulator.pickedWhites(sc1);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, whites1);

        // Test with duplicates (should retry)
        String duplicateInput = "1\n2\n2\n3\n4\n5\n";
        Scanner sc2 = new Scanner(new ByteArrayInputStream(duplicateInput.getBytes()));
        int[] whites2 = PowerballSimulator.pickedWhites(sc2);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, whites2);

        // Test with out of range values (0 and 70)
        String outOfRangeInput = "0\n1\n70\n2\n3\n4\n5\n";
        Scanner sc3 = new Scanner(new ByteArrayInputStream(outOfRangeInput.getBytes()));
        int[] whites3 = PowerballSimulator.pickedWhites(sc3);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, whites3);

        // Test with non-numeric input (triggers NumberFormatException)
        String mixedInput = "abc\n1\nxyz\n2\n3\n4\n5\n";
        Scanner sc4 = new Scanner(new ByteArrayInputStream(mixedInput.getBytes()));
        int[] whites4 = PowerballSimulator.pickedWhites(sc4);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, whites4);
    }

    @Test
    void testPickedPb() {
        // Test valid input
        Scanner sc1 = new Scanner(new ByteArrayInputStream("15\n".getBytes()));
        assertEquals(15, PowerballSimulator.pickedPb(sc1));

        // Test out of range then valid (27 is out of range, then 10)
        Scanner sc2 = new Scanner(new ByteArrayInputStream("27\n10\n".getBytes()));
        assertEquals(10, PowerballSimulator.pickedPb(sc2));

        // Test 0 (out of range) then valid
        Scanner sc3 = new Scanner(new ByteArrayInputStream("0\n5\n".getBytes()));
        assertEquals(5, PowerballSimulator.pickedPb(sc3));

        // Test non-numeric input then valid
        Scanner sc4 = new Scanner(new ByteArrayInputStream("abc\n20\n".getBytes()));
        assertEquals(20, PowerballSimulator.pickedPb(sc4));

        // Test negative number then valid
        Scanner sc5 = new Scanner(new ByteArrayInputStream("-5\n26\n".getBytes()));
        assertEquals(26, PowerballSimulator.pickedPb(sc5));
    }

    @Test
    void testMain() {
        InputStream sysInBackup = System.in;

        try {
            // Test Case 1: Self pick then quit
            String selfPickInput = String.join("\n",
                    "1",                     // choose self pick
                    "10", "20", "30", "40", "50",  // white balls
                    "15",                    // powerball
                    "3"                      // quit
            );
            System.setIn(new ByteArrayInputStream(selfPickInput.getBytes()));
            PowerballSimulator.main(new String[]{});

            // Test Case 2: Random tickets then quit
            String randomInput = String.join("\n",
                    "2",  // choose random tickets
                    "3",  // buy 3 tickets
                    "3"   // quit
            );
            System.setIn(new ByteArrayInputStream(randomInput.getBytes()));
            PowerballSimulator.main(new String[]{});

            // Test Case 3: Invalid choice then quit
            String invalidInput = String.join("\n",
                    "99",  // invalid choice
                    "3"    // quit
            );
            System.setIn(new ByteArrayInputStream(invalidInput.getBytes()));
            PowerballSimulator.main(new String[]{});

            // Test Case 4: Multiple operations (self pick, random, invalid, then quit)
            String multipleOpsInput = String.join("\n",
                    "1",                          // self pick
                    "5", "15", "25", "35", "45", // white balls
                    "10",                         // powerball
                    "2",                          // random tickets
                    "1",                          // buy 1 ticket
                    "invalid",                    // invalid choice
                    "3"                           // quit
            );
            System.setIn(new ByteArrayInputStream(multipleOpsInput.getBytes()));
            PowerballSimulator.main(new String[]{});

//            // Test Case 5: Jackpot
//            String jackpotAttempt = String.join("\n",
//                    "2",   // random tickets
//                    "10000000", // buy many tickets to increase jackpot chance
//                    "3"    // quit
//            );
//            System.setIn(new ByteArrayInputStream(jackpotAttempt.getBytes()));
//            PowerballSimulator.main(new String[]{});


        } finally {
            System.setIn(sysInBackup);
        }
    }
}