package iscas.ac.grand.util;

public class Solution {
	public static void main(String args[]) {
		Solution s=new Solution();
		//System.out.println(s.mySqrt(2147395599));
		
	}
    public int mySqrt(int x) {
    	long pre=0;
    	long i=1;
        long pos;
        long max;
        long prej;
        long base;
        while(true){
            pos=i*i;
            if(pos>x){
                max=i/2;
                prej=pre;
                while(max>=1){
                    base=prej+max;
                    pos=base*base;
                    if(pos==x){
                        return (int)base;
                    }else if(pos<x){
                        if(max==1){
                            return (int)base;
                        }
                        prej=base;
                    }
                    max=max/2;
                }
                return (int)prej;
            }else if(pos==x){
                return (int)i;
            }
            pre=i;
            i=i*2;
        }

    }
}