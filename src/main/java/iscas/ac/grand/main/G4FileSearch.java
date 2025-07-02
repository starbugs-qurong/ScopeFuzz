package iscas.ac.grand.main;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 读取目录下所有的g4文件
 */
public class G4FileSearch {
    //res数组用来存放所有的txt文件（绝对路径）
    public List<String> res = new ArrayList<>();

    public List<String> getRes() {
        return res;
    }

    //递归函数需要提供 当前目录 作为形参
    public void find (File root) {
        File[] sons = root.listFiles();
        //如果sons已经没有子文件了(即root本身就是一个文件时) 则return 结束递归
        if (sons == null) return;
        for (File son: sons) {
            //如果son是一个文件，且它的文件名的后缀是txt结尾,则保存该文件
            if (son.isFile()){
//                String[] names=son.getName().split(""+File.separator+".");
//                if(names.length==2&&names[1].equals("g4")){
//                    System.out.println("in find for child is g4:  "+son.toString());//在linux上不适用
//                    res.add(son.toString());
//                }
                if(son.getName().endsWith("g4")){
//                    System.out.println("in find for child is g4:  "+son.toString());
                    res.add(son.toString());
                }
            } else {
                //反之，son是一个目录 则继续查找
                find(son);
            }
        }
    }
    public static void main(String[] args) {
        G4FileSearch tfs = new G4FileSearch();
        tfs.find(new File("F:"+File.separator+"produce-program"+File.separator+"grammars-v4-master"));
        for (String file: tfs.res) {
//            //System.out.println(file);
        }
    }

}