package Array1_230807_231211;

import java.util.Scanner;

/*
 * 10개의 수 중 가장 큰 수 출력
 * */
public class 최대수구하기_230807_231211 {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int t = sc.nextInt();//test
		for (int tc = 1; tc <= t; tc++) {
			int max = 0;
			for (int i = 0; i < 10; i++) {
				int tmp = sc.nextInt();
				if (tmp > max) {
					max = tmp;
				}
			}
			
			System.out.println("#"+tc+" "+max);
		}//test case
	}//main
}//class
