import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class ConcurrentPancakeShop {
    private static final int NUM_TIME_SLOTS = 10;
    private static final long SLOT_DURATION = 30000; // 30 seconds in milliseconds

    private int totalPancakesMade = 0;
    private int totalPancakesOrdered = 0;
    private int totalPancakesEaten = 0;
    private int totalOrdersNotMet = 0;
    private int totalPancakesWasted = 0;
    private int timeSlot = 1;

    public static void main(String[] args) {
//        Observations

//        In the the non-concurrent code I was not able to repeat the task for every 30 seconds but in the concurrent code, the code was able to re-run every 30 seconds



        ConcurrentPancakeShop shop = new ConcurrentPancakeShop();
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new ShopkeeperTask(shop, timer), 0, SLOT_DURATION);

    }

    private static class ShopkeeperTask extends TimerTask {
        private ConcurrentPancakeShop shop;
        private Timer timer;

        ShopkeeperTask(ConcurrentPancakeShop shop, Timer timer) {
            this.shop = shop;
            this.timer = timer;
        }

        @Override
        public void run() {
            long startTime = System.currentTimeMillis();
            System.out.println(startTime + " starting time");
            System.out.println("Time Slot: " + shop.timeSlot);

            int pancakesMade = shop.makePancakes();
            shop.totalPancakesMade += pancakesMade;

            int[] orders = shop.generateUserOrders();
            shop.totalPancakesOrdered = 0;

            for (int order : orders) {
                int pancakesToEat = Math.min(order, 5);
                shop.totalPancakesOrdered += pancakesToEat;
            }

            if (shop.totalPancakesMade > shop.totalPancakesOrdered) {
                shop.totalPancakesWasted = shop.totalPancakesMade - shop.totalPancakesOrdered;
                shop.totalPancakesEaten = shop.totalPancakesOrdered;
            }
            if (shop.totalPancakesOrdered > shop.totalPancakesMade) {
                shop.totalOrdersNotMet = shop.totalPancakesOrdered - shop.totalPancakesMade;
                shop.totalPancakesEaten = shop.totalPancakesMade;
            }

//            System.out.println("Pancakes Made: " + pancakesMade);
//            System.out.println("Pancakes Eaten: " + shop.totalPancakesEaten);
            System.out.println("Total Pancakes Made: " + shop.totalPancakesMade);
            System.out.println("Total Pancakes Ordered: " + shop.totalPancakesOrdered);
            System.out.println("Total Pancakes Eaten: " + shop.totalPancakesEaten);
            System.out.println("Total Orders Not Met: " + shop.totalOrdersNotMet);
            System.out.println("Total Pancakes Wasted: " + shop.totalPancakesWasted);

            long endTime = System.currentTimeMillis();
            System.out.println(endTime + " ending time");
            System.out.println(endTime - startTime);

            if (shop.timeSlot >= NUM_TIME_SLOTS) {
                timer.cancel();
                timer.purge();
            }

            shop.timeSlot++;
        }

    }

    private int makePancakes() {
        Random random = new Random();
        return random.nextInt(13); // Maximum of 12 pancakes
    }

    private int[] generateUserOrders() {
        Random random = new Random();
        int[] orders = new int[3];

        for (int i = 0; i < 3; i++) {
            orders[i] = random.nextInt(6); // Maximum of 5 pancakes per user
        }

        return orders;
    }
}
