package niuke;
import java.util.*;
public class Main {
	// 可以回溯的状态，如果后面匹配不成功，可以从该状态重新开始尝试匹配，i:str数组index，j:pattern数组index
	// c为pattern的字符，用于回溯到该状态时判断是否可以继续匹配
	public class State { 
        private int i = 0;
        private int j = 0;
        private char c = 'a';
        State (int i, int j, char c) {
            this.i = i;
            this.j = j;
            this.c = c;
        }
        int getI() {
        	return i;
        }
        int getJ() {
        	return j;
        }
        char getC() {
        	return c;
        }
    }
    public boolean match(char[] str, char[] pattern)
    {
        if (str == null || pattern == null)
            return false;
        if (str.length == 0 && pattern.length == 0)
        	return true;
        if (str.length == 0 && pattern.length == 2 && pattern[1] == '*')
        	return true;
        if (str.length == 0 || pattern.length == 0)
        	return false;
        int str_idx = 0, pat_idx = 0;
        int str_len = str.length, pat_len = pattern.length;
        LinkedList<State> states = new LinkedList<State>();
        while (true) {
        	//出现'*'，保存新的回溯点，实质就是*表示可以吃掉零个或多个字符
            if (pat_idx < pat_len - 1 && pattern[pat_idx + 1] == '*') {
                if (pattern[pat_idx] == '.') {
                    states.add(new State(str_idx, pat_idx+2, pattern[pat_idx]));
                    System.out.println(str_idx + "--1--" + (pat_idx+2) + pattern[pat_idx]);
                    pat_idx += 2;
                } else if (str[str_idx] == pattern[pat_idx]) {
                	states.add(new State(str_idx, pat_idx+2, pattern[pat_idx]));
                	System.out.println(str_idx + "--2--" + (pat_idx+2) + pattern[pat_idx]);
                    pat_idx += 2;
                } else {
                    pat_idx += 2;
                }
            } else if (pattern[pat_idx] == '.') {//'.'正常匹配
                str_idx++;
                pat_idx++;
            } else if (str[str_idx] == pattern[pat_idx]) {//单个字符正常匹配
                str_idx++;
                pat_idx++;
            } else if (!states.isEmpty()) {// 匹配不成功，回溯到以前的状态
            	while (!states.isEmpty()) {
	                State s = states.pollLast();
	                if (s.getI() < str_len && (s.getC() == '.' || str[s.getI()] == s.getC())) {
	                	str_idx = s.getI() + 1;
	                	pat_idx = s.getJ();
	                	states.add(new State(str_idx, pat_idx, s.getC()));
	                	System.out.println(str_idx + "--3--" + (pat_idx) + s.getC());
	                	break;
	                }
                }
            } else {
                return false;
            }
            if (str_idx == str_len && pat_idx == pat_len) {
            	break;
            }
            //特殊情况，str已经结束，但是pattern后面都是像a*b*c*d*这样的，那么应该是成功匹配
            if (str_idx == str_len && pat_idx < pat_len - 1 && pattern[pat_idx+1] == '*') {
            	int tmp_pat_idx = pat_idx + 1;
            	while (tmp_pat_idx < pat_len && pattern[tmp_pat_idx] == '*') {
            		tmp_pat_idx += 2;
            	}
            	if (tmp_pat_idx == pat_len+1) {
            		return true;
            	}
            }
            //处理越界情况，如果越界，回溯到以前的状态，如果不能回溯则说明匹配失败
            while (str_idx >= str_len || pat_idx >= pat_len) {
            	if (!states.isEmpty()) {
                	while (!states.isEmpty()) {
    	                State s = states.pollLast();
    	                if (s.getI() < str_len && (s.getC() == '.' || str[s.getI()] == s.getC())) {
    	                	str_idx = s.getI() + 1;
    	                	pat_idx = s.getJ();
    	                	states.add(new State(str_idx, pat_idx, s.getC()));
    	                	System.out.println(str_idx + "--4--" + (pat_idx) + s.getC());
    	                	break;
    	                }
                    }
            	} else {
            		return false;
            	}
            	if (str_idx == str_len && pat_idx == pat_len) {
                	return true;
                }
            }
        }
        
        return true;
    }
	public static void main(String[] args) {
		Main m = new Main();
		char[] s = "bcbbabab".toCharArray();
		char[] p = ".*a*a".toCharArray();
		System.out.println(m.match(s, p));
	}
        
}
