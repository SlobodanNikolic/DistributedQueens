package helpers;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import sun.security.util.AuthResources_ko;

public class Functions {
	
	public static CopyOnWriteArrayList<Integer> whoToContact(int myId) {
		int[] base3Id = decToBase3(myId);
		CopyOnWriteArrayList<Integer> result = new CopyOnWriteArrayList<Integer>();
		
//		Da li sam ja 0? Ako jesam, formiram svoj komsiluk. 
//		Treba da nadjem one koji se od mene razlikuju po prvoj cifri koja nije 0
//		Nadjem tu cifru svaki put kad je dekrementiram, posaljem tom id-ju poruku za spajanje		
//		Kad dodjem do 0, saljem i njemu i stajem.
		if(base3Id[Constants.ID_MAX_DIGITS-1]==0) {
			for(int i = Constants.ID_MAX_DIGITS-1; i>=0; i--) {
				if(base3Id[i]==0)
					continue;
				else {
					while(base3Id[i]>0) {
						base3Id[i]--;
						result.add(base3ToDec(base3Id));
					}
					return result;
				}
			}
		}
//		Ako nisam nula, dekrementiram najmanje bitnu cifru do nule i svima saljem poruku za spajanje.
		else {
			while(base3Id[Constants.ID_MAX_DIGITS-1]!=0) {
				base3Id[Constants.ID_MAX_DIGITS-1]--;
				result.add(base3ToDec(base3Id));
			}
			return result;
		}	
		return null;
	}
	
	public static int[] decToBase3(int myId) {
		
		int[] base3Id = new int[Constants.ID_MAX_DIGITS];
		int currentIndex = Constants.ID_MAX_DIGITS-1;
		while(true) {
			int left = myId%3;
			base3Id[currentIndex--] = left;
			myId = myId/3;
			if(myId == 0) {
				break;
			}
		}
		
		return base3Id;
	}
	
	public static int base3ToDec(int[] base3Id) {
		int result = 0;
		for(int i = Constants.ID_MAX_DIGITS-1, j=0; i>=0; i--, j++) {
			result += base3Id[i]*((int) java.lang.Math.pow(3, j));
		}
		return result;
	}
	
	public static int[] decToBaseN(int number, int base) {
		
		int[] baseNNumber = new int[base];
		int currentIndex = base-1;
		while(true) {
			int left = number%base;
			baseNNumber[currentIndex--] = left;
			number = number/base;
			if(number == 0) {
				break;
			}
		}
		
		return baseNNumber;
	}
	
	public static int baseNToDec(int[] baseNNumber, int base) {
		int result = 0;
		for(int i = base-1, j=0; i>=0; i--, j++) {
			result += baseNNumber[i]*((int) java.lang.Math.pow(base, j));
		}
		return result;
	}

	public static String arrayToString(int[] array) {
		String result = "";
		for (int i : array) {
			result+=i;
		}
		return result;
	}
	
	public static String matrixToString(int[][] matrix, int matrixSize) {
		String res = "";
		for(int i = 0; i < matrixSize; i++) {
			for(int j = 0; j < matrixSize; j++) {
				res+=matrix[i][j] + ", "; 
			}
			res+=System.lineSeparator();
		}
		return res;
	}
}
