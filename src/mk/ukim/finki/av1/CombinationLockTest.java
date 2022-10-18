package mk.ukim.finki.av1;

class CombinationLock {

    private int combination;
    private boolean isOpen;
    private static int DEFAULT_COMBINATION = 123;
    private static int MIN_COMBINATION = 100;
    private static int MAX_COMBINATION = 990;

    public CombinationLock(int combination) {
        if (validateCombination(combination))
            this.combination = combination;
        else this.combination = DEFAULT_COMBINATION;

        this.isOpen = false;
    }

    public boolean open(int combination) {
        this.isOpen = (this.combination == combination);
        return this.isOpen;
    }

    public boolean changeCombination(int oldCombination, int newCombination) {

        if (open(oldCombination) && validateCombination(newCombination)) {
            this.combination = newCombination;
            return true;
        }

        return false;
    }

    private boolean validateCombination(int combination) {
        return combination >= MIN_COMBINATION && combination <= MAX_COMBINATION;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void lock() {
        this.isOpen = false;
    }


}

public class CombinationLockTest {

    public static void main(String[] args) {

        CombinationLock validLock = new CombinationLock(238);

        System.out.println("Testing isOpen() .....");
        System.out.println(validLock.isOpen());

        System.out.println("Testing open() .....");
        System.out.println(validLock.open(239));
        System.out.println(validLock.open(9));
        System.out.println(validLock.open(123));
        System.out.println(validLock.open(238));
        System.out.println(validLock.open(999));

        validLock.lock();
        System.out.println("Testing changeCombination() .....");
        System.out.println(validLock.changeCombination(230, 290));
        System.out.println(validLock.changeCombination(230, 238));
        System.out.println(validLock.changeCombination(238, 700));
        System.out.println(validLock.changeCombination(700, 400));
        System.out.println(validLock.changeCombination(700, 400));

        CombinationLock invalidLock = new CombinationLock(2332125);

        System.out.println("Testing isOpen() .....");
        System.out.println(invalidLock.isOpen());

        System.out.println("Testing open() .....");
        System.out.println(invalidLock.open(2332125));
        System.out.println(invalidLock.open(450));
        System.out.println(invalidLock.open(123));
        System.out.println(invalidLock.open(238));

        invalidLock.lock();
        System.out.println("Testing changeCombination() .....");
        System.out.println(invalidLock.changeCombination(2332125, 290));
        System.out.println(invalidLock.changeCombination(909, 238));
        System.out.println(invalidLock.changeCombination(123, 700));
        System.out.println(invalidLock.changeCombination(123, 400));
        System.out.println(invalidLock.changeCombination(700, 427));

    }
}
