package anonymous.ac.grand.main;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import anonymous.ac.grand.main.antlr4.CPP14Parser;
import anonymous.ac.grand.main.common.SymbolTable;
import anonymous.ac.grand.main.mutation.CppProgram;

public class Serializable{
	

    /**
     * 把符号表对象序列化到文件中
     */
    public void objectToFile(SymbolTable st1,String filePath){
        // 使用对象字节输出流包装字节输出流
        ObjectOutputStream oos = null;
        try {
            String name=filePath;
            oos= new ObjectOutputStream(new FileOutputStream(name));
            // 3.调用序列化方法, 把对象写到文件当中去
            oos.writeObject(st1);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            // 4.释放资源
            try {
                oos.flush();
                oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 符号表对象从文件中反序列化出来
     */
    public SymbolTable objectFromFile(String filePath){
        // 1.使用对象字节输入流包装字节输入流
    	SymbolTable s1=new SymbolTable();
    	ObjectInputStream ois = null;
        try {
            String name=filePath;
            ois= new ObjectInputStream(new FileInputStream(name));
            // 2.调用反序列化方法, 把数据从文件读取到对象中
            s1= (SymbolTable) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            // 3.释放资源
            try {
                ois.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return s1;
    }


    /**
     * 把切片程序列表对象序列化到文件中
     */
    public void objectToFileForSlice(List<CppProgram> cpList, String filePath){
        // 使用对象字节输出流包装字节输出流
        ObjectOutputStream oos = null;
        try {
            String name=filePath;
            oos= new ObjectOutputStream(new FileOutputStream(name));
            // 3.调用序列化方法, 把对象写到文件当中去
            oos.writeObject(cpList);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            // 4.释放资源
            try {
                oos.flush();
                oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 切片程序列表对象从文件中反序列化出来
     */
    public List<CppProgram> objectFromFileForSlice(String filePath){
        // 1.使用对象字节输入流包装字节输入流
        List<CppProgram> cpList=new ArrayList<>();
        ObjectInputStream ois = null;
        try {
            String name=filePath;
            ois= new ObjectInputStream(new FileInputStream(name));
            // 2.调用反序列化方法, 把数据从文件读取到对象中
            cpList= (List<CppProgram>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            // 3.释放资源
            try {
                ois.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return cpList;
    }

    /**
     * anonymousAuthor
     * 2025-1-9
     * 把AST中解析的函数列表对象序列化到文件中
     */
    public void objectToFileForASTFunction(List<CPP14Parser.FunctionDefinitionContext> funList, String filePath){
        // 使用对象字节输出流包装字节输出流
        ObjectOutputStream oos = null;
        try {
            String name=filePath;
            oos= new ObjectOutputStream(new FileOutputStream(name));
            // 3.调用序列化方法, 把对象写到文件当中去
            oos.writeObject(funList);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            // 4.释放资源
            try {
                oos.flush();
                oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * anonymousAuthor
     * 2025-1-9
     * 把AST中解析的函数列表对象从文件中反序列化出来
     */
    public List<CPP14Parser.FunctionDefinitionContext> objectFromFileForASTFunction(String filePath){
        // 1.使用对象字节输入流包装字节输入流
        List<CPP14Parser.FunctionDefinitionContext> cpList=new ArrayList<>();
        ObjectInputStream ois = null;
        try {
            String name=filePath;
            ois= new ObjectInputStream(new FileInputStream(name));
            // 2.调用反序列化方法, 把数据从文件读取到对象中
            //Object onj=ois.readObject();
            cpList= (List<CPP14Parser.FunctionDefinitionContext>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            // 3.释放资源
            try {
                ois.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return cpList;
    }

    /**
     * anonymousAuthor
     * 2025-1-9
     * 把AST中解析的语句列表对象从文件中反序列化出来
     */
    public void objectToFileForASTStatement(List<CPP14Parser.StatementContext> statementList, String filePath) {
        // 使用对象字节输出流包装字节输出流
        ObjectOutputStream oos = null;
        try {
            String name=filePath;
            oos= new ObjectOutputStream(new FileOutputStream(name));
            // 3.调用序列化方法, 把对象写到文件当中去
            oos.writeObject(statementList);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            // 4.释放资源
            try {
                oos.flush();
                oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * anonymousAuthor
     * 2025-1-9
     * 把AST中解析的语句列表对象从文件中反序列化出来
     */
    public List<CPP14Parser.StatementContext> objectFromFileForASTStatement(String filePath){
        // 1.使用对象字节输入流包装字节输入流
        List<CPP14Parser.StatementContext> cpList=new ArrayList<>();
        ObjectInputStream ois = null;
        try {
            String name=filePath;
            ois= new ObjectInputStream(new FileInputStream(name));
            // 2.调用反序列化方法, 把数据从文件读取到对象中
            cpList= (List<CPP14Parser.StatementContext>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            // 3.释放资源
            try {
                ois.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return cpList;
    }
    /**
     * anonymousAuthor
     * 2025-1-9
     * 把AST中解析的未修饰的标识符列表对象从文件中反序列化出来
     */
    public void objectToFileForASTUnqualifiedId(List<CPP14Parser.UnqualifiedIdContext> unqualifiedIdList, String filePath) {
        // 使用对象字节输出流包装字节输出流
        ObjectOutputStream oos = null;
        try {
            String name=filePath;
            oos= new ObjectOutputStream(new FileOutputStream(name));
            // 3.调用序列化方法, 把对象写到文件当中去
            oos.writeObject(unqualifiedIdList);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            // 4.释放资源
            try {
                oos.flush();
                oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * anonymousAuthor
     * 2025-1-9
     * 把AST中解析的未修饰标识符列表对象从文件中反序列化出来
     */
    public List<CPP14Parser.UnqualifiedIdContext> objectFromFileForASTUnqualifiedId(String filePath){
        // 1.使用对象字节输入流包装字节输入流
        List<CPP14Parser.UnqualifiedIdContext> cpList=new ArrayList<>();
        ObjectInputStream ois = null;
        try {
            String name=filePath;
            ois= new ObjectInputStream(new FileInputStream(name));
            // 2.调用反序列化方法, 把数据从文件读取到对象中
            cpList= (List<CPP14Parser.UnqualifiedIdContext>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            // 3.释放资源
            try {
                ois.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return cpList;
    }

    /**
     * anonymousAuthor
     * 2025-1-9
     * 把AST中解析的修饰的标识符列表对象从文件中反序列化出来
     */
    public void objectToFileForASTQualifiedId(List<CPP14Parser.QualifiedIdContext> qualifiedIdList, String filePath) {
        // 使用对象字节输出流包装字节输出流
        ObjectOutputStream oos = null;
        try {
            String name=filePath;
            oos= new ObjectOutputStream(new FileOutputStream(name));
            // 3.调用序列化方法, 把对象写到文件当中去
            oos.writeObject(qualifiedIdList);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            // 4.释放资源
            try {
                oos.flush();
                oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * anonymousAuthor
     * 2025-1-9
     * 把AST中解析的修饰标识符列表对象从文件中反序列化出来
     */
    public List<CPP14Parser.QualifiedIdContext> objectFromFileForASTQualifiedId(String filePath){
        // 1.使用对象字节输入流包装字节输入流
        List<CPP14Parser.QualifiedIdContext> cpList=new ArrayList<>();
        ObjectInputStream ois = null;
        try {
            String name=filePath;
            ois= new ObjectInputStream(new FileInputStream(name));
            // 2.调用反序列化方法, 把数据从文件读取到对象中
            cpList= (List<CPP14Parser.QualifiedIdContext>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            // 3.释放资源
            try {
                ois.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return cpList;
    }
}

