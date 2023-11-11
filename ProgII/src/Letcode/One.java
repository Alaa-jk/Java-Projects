package Letcode;

public class One {
    public static void main(String[] args) {

        int[] nums = {1, 2, 3};
        int[] numbers = towSum(nums, 3);
        for(int i = 0; i <numbers.length ; i++) {
            System.out.print(numbers[i]+ " ");

        }

    }
    public static int[] towSum(int[] nums, int target) {
        for(int i = 0; i < nums.length; i++) {
            for(int j = i +1; j < nums.length ; j++) {
               if(nums[i]+nums[j] == target)
                   return new int[] {i, j};
            }

        }
       return null;
    }
}
