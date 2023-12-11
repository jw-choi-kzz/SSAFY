package Array1_230807_231211;

import java.util.Scanner;

/*
 * n개의 점수 중 중간값
 * */
public class 중간값찾기 {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		int [] score = new int [n];
		for (int i = 0; i < n; i++) {
			score[i] = sc.nextInt();
		}
		int medianIdx = (n+1)/2;
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j <n; j++) {
				if (score[i] > score[j]) {
					int tmp = score[i];
					score[i] = score[j];
					score[j] = tmp;
				}
			}
		}
		
		System.out.println(score[medianIdx-1]); //인덱스는 0부터 시작하니까
	}//main
}//class
