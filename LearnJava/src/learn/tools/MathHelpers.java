package learn.tools;

import java.util.Arrays;

public class MathHelpers {
	public static long[][] calCombMod(int m,int n, long mod) {
		long[][] comb = new long[m+1][n+1];
		Arrays.fill(comb[0], 1L);
		for(int i = 1 ; i <= m ; i++) {
			comb[i][i]= 1L;
			for(int j = i+1 ; j <= n ; j++) {
				comb[i][j] = (comb[i-1][j-1] + comb[i][j-1])% mod;
			}
		}
		return comb;
	}
}
