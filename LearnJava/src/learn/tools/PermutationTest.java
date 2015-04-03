package learn.tools;

import org.junit.Assert;
import org.junit.Test;

public class PermutationTest {

	
	final int[][] PERMUTATION_3 = {
			{1,2,3},
			{1,3,2},
			{2,1,3},
			{2,3,1},
			{3,1,2},
			{3,2,1}
	};
	@Test
	public void testFullPermu3() {
		Permutation permutation = new Permutation(3);
		int[] per = permutation.firstPermutation();
		Assert.assertArrayEquals(per, PERMUTATION_3[0]);
		Assert.assertArrayEquals(permutation.nextPermutation(), PERMUTATION_3[1]);
		Assert.assertArrayEquals(permutation.nextPermutation(), PERMUTATION_3[2]);
		Assert.assertArrayEquals(permutation.nextPermutation(), PERMUTATION_3[3]);
		Assert.assertArrayEquals(permutation.nextPermutation(), PERMUTATION_3[4]);
		Assert.assertArrayEquals(permutation.nextPermutation(), PERMUTATION_3[5]);
		Assert.assertEquals(permutation.nextPermutation(), null);
	}
	
	@Test
	public void testPermuOne () {
		Permutation permutation = new Permutation(2,3);
		int[] first = permutation.firstPermutation();
		Assert.assertArrayEquals(first, new int[] {2});
		Assert.assertNull(permutation.nextPermutation());
	}
	
	@Test
	public void someRandomTest() {
		final int[][] TEST_PER = {
				{0,1,4,5,2,3}, //0
				{0,1,4,5,3,2}, //1
				{0,1,5,2,3,4}, //2
				{0,1,5,2,4,3}, //3
				{0,1,5,3,2,4}, //4
				{0,1,5,3,4,2}, //5
				{0,1,5,4,2,3} //6				
		};
		Permutation permutation = new Permutation(TEST_PER[0]);
		Assert.assertArrayEquals(permutation.nextPermutation(), TEST_PER[1]);
		Assert.assertArrayEquals(permutation.nextPermutation(), TEST_PER[2]);
		Assert.assertArrayEquals(permutation.nextPermutation(), TEST_PER[3]);
		Assert.assertArrayEquals(permutation.nextPermutation(), TEST_PER[4]);
		Assert.assertArrayEquals(permutation.nextPermutation(), TEST_PER[5]);
		
		int[] per = null;
		int[] prev = null;
		do {
			prev = per;
		} while ((per = permutation.nextPermutation()) != null);
		Assert.assertArrayEquals(prev, new int[] {5,4,3,2,1,0});
	}

}
