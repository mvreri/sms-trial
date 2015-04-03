package learn.tools;

import java.util.Arrays;

public class Permutation {
	private int[] num;
	private int len;

	public Permutation(int[] num) {
		init(num);
	}

	/**
	 * Setup the permutation for number [1,n]
	 * @param n
	 */
	public Permutation(int n) {
		int[] num = new int[n];
		for(int i = 1 ; i <= n ; i++) {
			num[i-1] = i;
		}
		init(num);
	}
	
	
	/**
	 * Init the permutation with [start,end) (exclude end)
	 * @param start
	 * @param end
	 */
	public Permutation(int start,int end) {
		assert end > start;
		int[] num = new int[end-start];
		for(int i = start ; i < end ; i++) {
			num[i-start] = i;
		}
		init(num);
	}

	/**
	 * @pre i != j => num[i] != num[j]
	 * @param num
	 */
	private void init(int[] num) {
		this.num = num;
		this.len =  num.length;		
		for(int i = 0 ; i < len ; i++)
			for(int j  = i+1; j < len ; j++)
				assert num[i] != num[j];
	}
	
	
	/**
	 * Reset the permutation to the first one and return it
	 * @return first permutation 
	 */
	public int[] firstPermutation() {
		Arrays.sort(num);
		return num;
	}
	/**
	 * 
	 * @return next permutation if there is one exists, null otherwise
	 */
	public int[] nextPermutation() {
		int index = -1;
		for(int i = len - 2; i >= 0 ; i--) {
			if ( num[i] < num[i+1]) {
				index = i;
				break;
			}
		}
		if ( index == -1) return null;
		
		int swapIndex = findSwapIndex(index,num);
		assert swapIndex != -1;
		swap(num,index,swapIndex);
		
		reverse(num,index+1,len);
			
		return num;
	}

	/**
	 * @param a
	 * @param start
	 * @param end
	 * Reverse the array a from index [start,end) (exclude end)
	 */
	private void reverse(int[] a, int start, int end) {
		for(int i = 0; ; i++) {
			int indexSmaller = start + i;
			int indexBigger  = end-i-1;
			if ( indexBigger <= indexSmaller ) break;
			swap(a,indexSmaller,indexBigger);
		}
	}

	private void swap(int[] a, int i, int j) {
		int temp = a[i];
		a[i] = a[j];
		a[j] = temp;
	}

	/**
	 * 
	 * @param index
	 * @param num
	 * @return the swapIndex : num[swapIndex] = min { s | s > index & num[s] > num[index] } , -1 if the set is empty
	 */
	private int findSwapIndex(int index,int[] num) {
		int swapIndex = -1;
		for(int s = index+1 ; s < num.length ; s++) {
			if ( num[s] > num[index]) {
				if ( swapIndex == -1 ) swapIndex = s;
				else {
					if (num[s] < num[swapIndex]) swapIndex = s;
				}
			}
		}
		return swapIndex;
	}
}
