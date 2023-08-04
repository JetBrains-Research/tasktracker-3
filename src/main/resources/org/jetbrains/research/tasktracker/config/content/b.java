public class LookAndSaySequence3636 {
    public static void main(String[] args) {
        String currentTerm = "1";
        System.out.println(currentTerm);

        for (int i = 1; i < 20; i++) {
            String nextTerm = generateNextTerm(currentTerm);
            System.out.println(nextTerm);
            currentTerm = nextTerm;
        }
    }

    private static String generateNextTerm(String currentTerm) {
        StringBuilder nextTermBuilder = new StringBuilder();
        char currentNumber = currentTerm.charAt(0);
        int currentNumberCount = 1;

        // Iterate through the current term and generate the next term
        for (int i = 1; i < currentTerm.length(); i++) {
            char nextNumber = currentTerm.charAt(i);

            if (nextNumber == currentNumber) {
                // If the next number is the same, increment the count
                currentNumberCount++;
            } else {
                // If the next number is different, add the count and number to the next term
                nextTermBuilder.append(currentNumberCount).append(currentNumber);
                // Reset the count and current number
                currentNumberCount = 1;
                currentNumber = nextNumber;
            }
        }

        // Don't forget to append the final count and number
        nextTermBuilder.append(currentNumberCount).append(currentNumber);
        return nextTermBuilder.toString();
    }
}
    