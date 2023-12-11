package Array1_230807_231211;

import java.util.Scanner;

/*
 * [문제] 상자들이 쌓여 있는 방, 오른쪽으로 90도 회전할 때 가장 큰 낙차를 리턴. 
 * [조건] 방의 가로길이는 항상 100. 세로 길이도 항상 100.상자는 최소 0~최대 100 높이
 * [입력] 박스 가로길이, 다음 줄에 박스 높이
*/
public class gravity {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int h = sc.nextInt();//박스 가로 길이
		int [] boxes = new int [h];
		for (int i = 0; i < h; i++) {
			boxes[i] = sc.nextInt();
		}//박스 높이

		//그냥 최대 높이가 몇개 있는지 갯수를 세면 된다
		int max = 0;//박스 쌓이 최대 높이
		int maxIdx = 0;//박스 최대 높이 쌓인 곳 중 가장 왼 쪽에 있는 인덱스
		int maxNum = 0;//최대 높이가 몇 군데 있는지
		
		for (int i = 0; i < h; i++) {
			if (boxes[i] > max) {
				max = boxes[i];
				maxIdx = i;
				maxNum = 1;
			} else if (boxes[i] == max) {
				maxNum++;
			}
		}	
		
		System.out.println(h-maxIdx-maxNum);

	}

}
