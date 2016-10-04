import java.util.*;

/*
 * SRM452 Div1Easy／Div2Medium
 * http://www.topcoder.com/stat?c=problem_statement&pm=10561&rd=13906
 *
 * http://www.itmedia.co.jp/enterprise/articles/0912/05/news002_2.html
 */
public class StonePlacingDistanceNotTwo {

    public int maxStones(int width, int height) {
		if (width == 0 || height == 0) {
			return 0;
		} else if (width <= 2 && height <= 2) {
			return width * height;
		} if (width <= 4 && height <= 4) {
			if (width == 1 || height == 1) {
				return 2;
			}
			return maxStones(2, 2) + (width - 2) * (height - 2);
		}
		int result = maxStones(4, 4) * (width / 4) * (height / 4);
		int width_leftover  = width  % 4;
		int height_leftover = height % 4;
        return result + (width  / 4) * height_leftover * 2
			          + (height / 4) * width_leftover  * 2
					  + maxStones(width_leftover, height_leftover);
		/* 出題者側からの正答例
        int i, j, res = 0;
        for (i = 0; i < 2; i++)
        {
            for (j = 0; j < 2; j++)
            {
                res += (((width + i) / 2) * ((height + j) / 2) + 1) / 2;
            }
        }
        return res;
		*/
    }

// BEGIN CUT HERE
    public static void main(String[] args) {
    	new StonePlacingDistanceNotTwo().test();
    }
    
    public void test(){
        try {
        	test_case_0(); test_case_1(); test_case_2(); test_case_3(); test_case_4(); test_case_5(); test_case_6(); test_case_7(); test_case_8(); test_case_9();
        } catch( Exception exx) {
            System.err.println(exx);
            exx.printStackTrace(System.err);
        }
    }
    
    private void test_case_0() { int Arg0 = 3; int Arg1 = 2; int Arg2 = 4; verify_case(0, Arg2, maxStones(Arg0, Arg1)); }
    private void test_case_1() { int Arg0 = 3; int Arg1 = 3; int Arg2 = 5; verify_case(1, Arg2, maxStones(Arg0, Arg1)); }
    private void test_case_2() { int Arg0 = 8; int Arg1 = 5; int Arg2 = 20; verify_case(2, Arg2, maxStones(Arg0, Arg1)); }
    private void test_case_3() { int Arg0 = 1000; int Arg1 = 1000; int Arg2 = 500000; verify_case(3, Arg2, maxStones(Arg0, Arg1)); }
    private void test_case_4() { int Arg0 = 1000; int Arg1 = 999; int Arg2 = 499500; verify_case(4, Arg2, maxStones(Arg0, Arg1)); }
    private void test_case_5() { int Arg0 = 999; int Arg1 = 1000; int Arg2 = 499500; verify_case(5, Arg2, maxStones(Arg0, Arg1)); }
    private void test_case_6() { int Arg0 = 999; int Arg1 = 999; int Arg2 = 499001; verify_case(6, Arg2, maxStones(Arg0, Arg1)); }
    private void test_case_7() { int Arg0 = 2; int Arg1 = 2; int Arg2 = 4; verify_case(7, Arg2, maxStones(Arg0, Arg1)); }
    private void test_case_8() { int Arg0 = 1; int Arg1 = 1; int Arg2 = 1; verify_case(8, Arg2, maxStones(Arg0, Arg1)); }
    private void test_case_9() { int Arg0 = 1; int Arg1 = 997; int Arg2 = 499; verify_case(9, Arg2, maxStones(Arg0, Arg1)); }
    
    private static void verify_case( int n, int a, int b ) {
        if ( a==b )
            System.err.println("Case "+n+" passed.");
        else
            System.err.println("Case "+n+" failed: expected "+a+", received "+b+".");
    }
// END CUT HERE
}
