import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class NonConcurrentPancakeShop {
    public static void main(String[] args) {
        int totalPancakesMade = 0;
        int totalPancakesEaten = 0;
        int totalPancakesOrdered = 0;
        int totalOrdersNotMet = 0;
        int totalPancakesWasted = 0;

        long duration = 10000;
        long startTime = System.currentTimeMillis();
        System.out.println(startTime);
            int pancakesMade = makePancakes();
            totalPancakesMade += pancakesMade;

            int[] orders = generateUserOrders();

            for (int order : orders) {
                int pancakesToEat = Math.min(order, 5);
                totalPancakesOrdered += pancakesToEat;
            }

            if (totalPancakesMade > totalPancakesOrdered) {
                totalPancakesWasted = totalPancakesMade - totalPancakesOrdered;
                totalPancakesEaten = totalPancakesOrdered;
            }
            if (totalPancakesOrdered > totalPancakesMade) {
                totalOrdersNotMet = totalPancakesOrdered - totalPancakesMade;
                totalPancakesEaten = totalPancakesMade;
            }


        System.out.println("Total Pancakes Made: " + totalPancakesMade);
        System.out.println("Total Pancakes Ordered: " + totalPancakesOrdered);
        System.out.println("Total Pancakes Eaten: " + totalPancakesEaten);
        System.out.println("Total Orders Not Met: " + totalOrdersNotMet);
        System.out.println("Total Pancakes Wasted: " + totalPancakesWasted);

        long endTime = System.currentTimeMillis();
        System.out.println(endTime);

        System.out.println(endTime - startTime);

        System.out.println("\n********************************\n********************************\n");

    }

    private static int makePancakes() {
        Random random = new Random();
        return random.nextInt(13);
    }

    private static int[] generateUserOrders() {
        Random random = new Random();
        int[] orders = new int[3];

        for (int i = 0; i < 3; i++) {
            orders[i] = random.nextInt(6);
        }

        return orders;
    }
}
