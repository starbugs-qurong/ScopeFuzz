package iscas.ac.grand.main;
import java.io.IOException;
import java.util.Scanner;
import java.io.*;
public class Main {
    static char []V=new char[10];//变量
    static char []T=new char[10];//终结符
    static String [][]P=new String[100][100];   //产生式输入
    static int []Arry=new int[100];    //记录每个产生式右边的个数
    static String S;
    static int Len_P;  //产生式的个数
    static int LenGth;      //目标长度
    static String []P_1=new String[100];
    static int RT=0;
    //获取该符号是第几个产生式
    public static int Start(String S) {
        char s=S.charAt(0);         //获取开始符
        for(int i=0;i<Len_P;i++) {
            if(P[i][0].charAt(0)==s) {
                return i;     //开始符产生的是第几个产生式
            }
        }
        return -1;
    }
    //判断该元素是否为非终结符
    public static boolean noZhongjie(char a) {
        for(int i=0;i<V.length;i++) {
            if(V[i]==a) {
                return true;
            }
        }
        return false;
    }
    //检查数组里面还有没有非终结符，
    public static int Check(String str) {
        int flag=0;
        for(int i=0;i<str.length();i++) {
            if(noZhongjie(str.charAt(i))==true) {  //还有非终结符
                return i;
            }
        }
        return -1;
    }
    public static String Splic(String str,int flag,String str_1) {
        str=str.substring(0, flag)+str_1+str.substring(flag+1, str.length());
        return str;
    }

    public static void DFS(String str) {
        int flag;
        if(str.length()>LenGth)
            return ;
        else if(str.length()==LenGth) {
            flag=Check(str);       //记录第一个非终结符出现的位置
            if(flag!=-1) {      //长度够，但是里面还有非终结符
                int k=Start(str.substring(flag, flag+1));
                String str2 = new String(str);
                for(int i=1;i<Arry[k];i++) {
                    str=Splic(str,flag,P[k][i]);
                    P_1[RT++]=str;
                    DFS(str);
                    RT--;
                    str = str2;
                }
            }
            else {       //满足要求
                System.out.print(S+"->");
                for(int i=0;i<RT-1;i++) {
                    System.out.print(P_1[i]);
                    System.out.print("\n"+" ->");
                }
                System.out.print(P_1[RT-1]+"\n");
                return ;
            }
        }else {
            flag=Check(str);
            if(flag==-1)            //长度不够，但没有变量了
                return ;
            else {                   //长度够，有变量
                String str2 = new String(str);
                int k=Start(str.substring(flag, flag+1));
                for(int i=1;i<Arry[k];i++) {
                    str=Splic(str,flag,P[k][i]);
                    P_1[RT++]=str;
                    DFS(str);
                    RT--;
                    str = str2;
                }
            }
        }//else
    }

    public static void run() {
        DFS(S);
    }

    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);
        BufferedReader filereader = new BufferedReader(new FileReader("Read.txt"));
        int Leng = 1;
        String Temp;
        int t = 0; // 记录产生式的个数
        while ((Temp = filereader.readLine()) != null) {
            Leng++;
        }
        Leng--;

        BufferedReader filereade = new BufferedReader(new FileReader("Read.txt"));
        int Len = 1;
        while ((Temp = filereade.readLine()) != null) {
            if (Len == 1) {
                int p=0;
                for(int i=3;i<Temp.length();i+=2) {
                    V[p++]=Temp.charAt(i);
                }
            }
            else if (Len == 2) {
                int p=0;
                for(int i=3;i<Temp.length();i+=2) {
                    T[p++]=Temp.charAt(i);
                }
            }
            else if (Len >= 3 && Len <= Leng - 1) {
                int k = 0;
                int begin;
                int end;
                P[t][k++] = Temp.substring(0, 1);
                begin = 3;
                for (int i = 3; i < Temp.length(); i++) {
                    if (Temp.charAt(i) == '|' || Temp.charAt(i) == ',') {
                        end = i;
                        P[t][k++] = Temp.substring(begin, end);
                        begin = i + 1;
                    }
                }
                Arry[t] = k;
                t++;
            } else {
                S = Temp;
            }
            Len++;
        }
        Len_P=t;
        System.out.print("输入目标文法的长度:");
        LenGth=in.nextInt();
        run();

    }
}