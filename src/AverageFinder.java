public class AverageFinder {

    private int[] numberHistory;
    AverageFinder(int size){
        numberHistory = new int[size];
    }
    public int averageOf(int newValue){
        for(int i = numberHistory.length - 1; i >= 1; i--) numberHistory[i] = numberHistory[i-1];
        numberHistory[0] = newValue;
        int sum = 0;
        for(int num : numberHistory) sum += num;
        return sum / numberHistory.length;
    }
}
