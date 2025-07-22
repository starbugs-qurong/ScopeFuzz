package anonymous.ac.grand.main.antlr4;

import anonymous.ac.grand.main.common.ScopeTree;
import anonymous.ac.grand.main.mutation.CppProgram;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * anonymousAuthor
 * 2025-1-3
 * 解析的ASR结果存储类
 */
public class CPPParserASTListener {
    private static List<CPP14Parser.FunctionDefinitionContext> funList = new ArrayList<>();//函数列表
    private static List<CPP14Parser.StatementContext> statementList = new ArrayList<>();//语句列表

    private static List<CPP14Parser.UnqualifiedIdContext> unqualifiedIdList = new ArrayList<>();//标识符列表1
    private static List<CPP14Parser.QualifiedIdContext> qualifiedIdList = new ArrayList<>();//标识符列表2
    private static List<CPP14Parser.AssignmentExpressionContext> assignmentExpressionList = new ArrayList<>();//赋值语句列表
    private static List<CPP14Parser.PostfixExpressionContext> postfixExpressionList = new ArrayList<>();//postfix表达式列表
    public static List<CppProgram> cppList = new ArrayList<CppProgram>();//程序列表
    public static String filePath="";//源程序的路径
    public static ScopeTree rootScope=new ScopeTree(0,null);

    public static List<CPP14Parser.ClassSpecifierContext> classContextList=new ArrayList<>();//用于基于AST的变异，记录每个源程序中的类上下文
    public static List<CPP14Parser.TypeSpecifierContext> typeContextList=new ArrayList<>();//用于基于AST的变异，记录每个源程序中的type上下文
    public static List<CPP14Parser.TrailingTypeSpecifierContext> trailTypeContextList=new ArrayList<>();//用于基于AST的变异，记录每个源程序中的type上下文
    public static List<CPP14Parser.EnumSpecifierContext> enumContextList=new ArrayList<>();//用于基于AST的变异，记录每个源程序中的enum上下文

    public static List<CPP14Parser.ClassSpecifierContext> getClassContextList() { return classContextList; }
    public static void setClassContextList(List<CPP14Parser.ClassSpecifierContext> classContextList) { CPPParserASTListener.classContextList = classContextList; }
    public static void setClassContextList(CPP14Parser.ClassSpecifierContext classContext) {
        List<CPP14Parser.ClassSpecifierContext> classContextList=getClassContextList();
        classContextList.add(classContext);
        setClassContextList(classContextList);}

    public static List<CPP14Parser.TypeSpecifierContext> getTypeContextList() { return typeContextList; }
    public static void setTypeContextList(List<CPP14Parser.TypeSpecifierContext> typeContextList) { CPPParserASTListener.typeContextList = typeContextList; }
    public static void setTypeContextList(CPP14Parser.TypeSpecifierContext typeContext) {
        List<CPP14Parser.TypeSpecifierContext> typeContextList=getTypeContextList();
        typeContextList.add(typeContext);
        setTypeContextList(typeContextList);
    }

    public static List<CPP14Parser.TrailingTypeSpecifierContext> getTrailTypeContextList() { return trailTypeContextList; }
    public static void setTrailTypeContextList(List<CPP14Parser.TrailingTypeSpecifierContext> trailTypeContextList) { CPPParserASTListener.trailTypeContextList = trailTypeContextList; }
    public static void setTrailTypeContextList(CPP14Parser.TrailingTypeSpecifierContext trailTypeContext) {
        List<CPP14Parser.TrailingTypeSpecifierContext> trailTypeContextList=getTrailTypeContextList();
        trailTypeContextList.add(trailTypeContext);
        setTrailTypeContextList(trailTypeContextList); }

    public static List<CPP14Parser.EnumSpecifierContext> getEnumContextList() { return enumContextList; }
    public static void setEnumContextList(List<CPP14Parser.EnumSpecifierContext> enumContextList) { CPPParserASTListener.enumContextList = enumContextList; }
    public static void setEnumContextList(CPP14Parser.EnumSpecifierContext enumContext) {
        List<CPP14Parser.EnumSpecifierContext> enumContextList=getEnumContextList();
        enumContextList.add(enumContext);
        setEnumContextList(enumContextList);
    }


    public static List<CPP14Parser.FunctionDefinitionContext> getFunList() {
        return funList;
    }


    public static void setFunList(List<CPP14Parser.FunctionDefinitionContext> funList) {
        CPPParserASTListener.funList = funList;
    }

    public static void setFunList(CPP14Parser.FunctionDefinitionContext fun) {
        List<CPP14Parser.FunctionDefinitionContext> newFunList=getFunList();
        newFunList.add(fun);
        setFunList(newFunList);
    }

    public static List<CPP14Parser.StatementContext> getStatementList() {
        return statementList;
    }

    public static void setStatementList(List<CPP14Parser.StatementContext> statementList) {
        CPPParserASTListener.statementList = statementList;
    }

    public static void setStatementList(CPP14Parser.StatementContext statement) {
        List<CPP14Parser.StatementContext> newStateList=getStatementList();
        newStateList.add(statement);
        setStatementList(newStateList);
    }

    public static List<CPP14Parser.UnqualifiedIdContext> getUnqualifiedIdList() {
        return unqualifiedIdList;
    }

    public static void setUnqualifiedIdList(List<CPP14Parser.UnqualifiedIdContext> unqualifiedIdList) {
        CPPParserASTListener.unqualifiedIdList = unqualifiedIdList;
    }

    public static void setUnqualifiedIdList(CPP14Parser.UnqualifiedIdContext unqualifiedId) {
        List<CPP14Parser.UnqualifiedIdContext> newUnqualifiedIdList=getUnqualifiedIdList();
        newUnqualifiedIdList.add(unqualifiedId);
        setUnqualifiedIdList(newUnqualifiedIdList);
    }

    public static List<CPP14Parser.QualifiedIdContext> getQualifiedIdList() {
        return qualifiedIdList;
    }

    public static void setQualifiedIdList(List<CPP14Parser.QualifiedIdContext> qualifiedIdList) {
        CPPParserASTListener.qualifiedIdList = qualifiedIdList;
    }

    public static void setQualifiedIdList(CPP14Parser.QualifiedIdContext qualifiedId) {
        List<CPP14Parser.QualifiedIdContext> newQualifiedIdList=getQualifiedIdList();
        newQualifiedIdList.add(qualifiedId);
        setQualifiedIdList(newQualifiedIdList);
    }

    public static List<CPP14Parser.AssignmentExpressionContext> getAssignmentExpressionList() {
        return assignmentExpressionList;
    }


    public static void setAssignmentExpressionList(List<CPP14Parser.AssignmentExpressionContext> assignmentExpressionList) {
        CPPParserASTListener.assignmentExpressionList = assignmentExpressionList;
    }

    public static void setAssignmentExpressionList(CPP14Parser.AssignmentExpressionContext assignmentExpression) {
        List<CPP14Parser.AssignmentExpressionContext> newAssignmentExpressionList=getAssignmentExpressionList();
        newAssignmentExpressionList.add(assignmentExpression);
        setAssignmentExpressionList(newAssignmentExpressionList);
    }

    public static List<CPP14Parser.PostfixExpressionContext> getPostfixExpressionList() {
        return postfixExpressionList;
    }

    public static void setPostfixExpressionList(List<CPP14Parser.PostfixExpressionContext> postfixExpressionList) {
        CPPParserASTListener.postfixExpressionList = postfixExpressionList;
    }

    public static void setPostfixExpressionList(CPP14Parser.PostfixExpressionContext postfixExpression) {
        List<CPP14Parser.PostfixExpressionContext> newPostfixExpressionList=getPostfixExpressionList();
        newPostfixExpressionList.add(postfixExpression);
        setPostfixExpressionList(newPostfixExpressionList);
    }

    public static String getFilePath() {
        return filePath;
    }

    public static void setFilePath(String filePath) {
        CPPParserASTListener.filePath = filePath;
    }

    public static List<CppProgram> getCppList() {return cppList; }
    public static void setCppList(List<CppProgram> cppList) { CPPParserASTListener.cppList = cppList; }
    public static void setCppList(CppProgram cpp) { List<CppProgram> cppList=getCppList();  cppList.add(cpp);setCppList(cppList);}



    /**
     * anonymousAuthor
     * 2025-1-8
     * 根据解析器中的AST树，得到c++程序中的一些节点的上下文列表
     * @param fileName
     * @return
     * @throws IOException
     */
    public CppProgram getFunctionByFile(String fileName) throws IOException {
        CharStream input = CharStreams.fromFileName(fileName);
        // 创建词法分析器
        CPP14Lexer lexer = new CPP14Lexer(input);
        // 创建Token流
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        // 创建解析器
        CPP14Parser parser = new CPP14Parser(tokens);
        // 解析C程序，生成AST
        ParseTree tree = parser.translationUnit();
        // 打印AST树结构（可选）
        //System.out.println(tree.toStringTree(parser));

        // 遍历AST树并处理节点
        ParseTreeWalker walker = new ParseTreeWalker();
        ScopeFuzzListener scopeFuzzListener = new ScopeFuzzListener();
        walker.walk(scopeFuzzListener, tree);
        //        MyListener myListener = new MyListener();
        //        walker.walk(myListener, tree);

        //把程序中的语句和程序中的标识符关联起来
        List<CPP14Parser.StatementContext> statements = scopeFuzzListener.getStatementsInOneCPP();
        List<CPP14Parser.UnqualifiedIdContext> unqualifiedIds=scopeFuzzListener.getUnqualifiedsIdInOneCpp();
        List<CPP14Parser.QualifiedIdContext> qualifiedIds=scopeFuzzListener.getQualifiedIdsInOneCpp();
//        if(statements.size()>0){
//            System.out.println(statements.size());
//        }
        for(CPP14Parser.StatementContext statement:statements){
            statement.setUnqualifiedIds(unqualifiedIds);
            statement.setQualifiedIds(qualifiedIds);
            setStatementList(statements);//CPP解析类CPPParserDemo
        }

        CppProgram cp=new CppProgram(rootScope);
        cp.setCppFilePath(filePath);

        //把程序中的函数和程序中的语句列表关联起来
        List<CPP14Parser.FunctionDefinitionContext> funs=scopeFuzzListener.getFunsInOneCpp();
        List<CPP14Parser.FunctionDefinitionContext> funsTemp=new ArrayList<>();
//        if(statements.size()>0){
//            System.out.println(statements.size());
//        }
        for(CPP14Parser.FunctionDefinitionContext fun:funs){
            fun.setStatementList(statements);
            fun.setCpp(cp);
            funsTemp.add(fun);
            setFunList(fun);//CPP解析类CPPParserDemo
        }

        //把程序中的函数和程序关联起来
        cp.setFunctionContextList(funsTemp);

        //把程序中的类和程序关联起来
        List<CPP14Parser.ClassSpecifierContext> classContextList=scopeFuzzListener.getClassContextListInOneCpp();//用于基于AST的变异，记录每个源程序中的类上下文
        List<CPP14Parser.TypeSpecifierContext> typeContextList=scopeFuzzListener.getTypeContextListInOneCpp();//用于基于AST的变异，记录每个源程序中的type上下文
        List<CPP14Parser.TrailingTypeSpecifierContext> trailTypeContextList=scopeFuzzListener.getTrailTypeContextListInOneCpp();//用于基于AST的变异，记录每个源程序中的type上下文
        List<CPP14Parser.EnumSpecifierContext> enumContextList=scopeFuzzListener.getEnumContextListInOneCpp();//用于基于AST的变异，记录每个源程序中的enum上下文

        cp.setClassContextList(classContextList);
        cp.setTypeContextList(typeContextList);
        cp.setTrailTypeContextList(trailTypeContextList);
        cp.setEnumContextList(enumContextList);

        setCppList(cp);
        return cp;
    }

    /**
     * anonymousAuthor
     * 2025-1-8
     * 监听节点并将其加入到静态列表中     // 自定义监听器，用于处理AST节点
     */
    static class ScopeFuzzListener extends CPP14ParserBaseListener {
        @Override
        public void enterFunctionDefinition(CPP14Parser.FunctionDefinitionContext ctx) {
            // 处理函数定义节点 //System.out.println("Function definition found: " + ctx.getText());
            ctx.setFilePath(filePath);
            //setFunList(ctx);//CPP解析类CPPParserDemo 在遍历完之后会将标识符列表关联到语句，再将语句列表关联进去再更新到list里
            setFuns(ctx);//监视器类ScopeFuzzListener
        }

        @Override
        public void enterStatement(CPP14Parser.StatementContext ctx) {
            // 处理语句节点
            //System.out.println("Statement found: " + ctx.getText());
//            setStatementList(ctx);//CPP解析类CPPParserDemo 在遍历完之后会将标识符列表关联进去再更新到list里
            setStatements(ctx);//监视器类ScopeFuzzListener
        }

        @Override
        public void enterUnqualifiedId(CPP14Parser.UnqualifiedIdContext ctx) {
            // 处理标识符节点1
            //System.out.println("UnqualifiedId found: " + ctx.getText());
            setUnqualifiedIdList(ctx);
            setUnqualifiedsIdInOneCpp(ctx);
        }

        @Override
        public void enterQualifiedId(CPP14Parser.QualifiedIdContext ctx) {
            // 处理标识符节点2
            //System.out.println("QualifiedId found: " + ctx.getText());
            setQualifiedIdList(ctx);
            setQualifiedIdsInOneCpp(ctx);
        }

        @Override
        public void enterClassSpecifier(CPP14Parser.ClassSpecifierContext ctx) {
            // 处理类节点
            setClassContextList(ctx);
            setClassContextListInOneCpp(ctx);
        }

        @Override
        public void enterTypeSpecifier(CPP14Parser.TypeSpecifierContext ctx) {
            // 处理类节点
            setTypeContextList(ctx);
            setTypeContextListInOneCpp(ctx);
        }

        @Override
        public void enterTrailingTypeSpecifier(CPP14Parser.TrailingTypeSpecifierContext ctx) {
            // 处理类节点
            setTrailTypeContextList(ctx);
            setTrailTypeContextListInOneCpp(ctx);
        }

        @Override
        public void enterEnumSpecifier(CPP14Parser.EnumSpecifierContext ctx) {
            // 处理类节点
            setEnumContextList(ctx);
            setEnumContextListInOneCpp(ctx);
        }


        public List<CPP14Parser.StatementContext> statementsInOneCPP = new ArrayList<>();//语句列表 每个程序的不一样
        public List<CPP14Parser.StatementContext> getStatementsInOneCPP() { return statementsInOneCPP; }
        public void setStatementsInOneCPP(List<CPP14Parser.StatementContext> statementsInOneCPP) {
            this.statementsInOneCPP = statementsInOneCPP;
        }
        public void setStatements(CPP14Parser.StatementContext statement) {
            List<CPP14Parser.StatementContext> newStatementList= getStatementsInOneCPP();
            newStatementList.add(statement);
            setStatementsInOneCPP(newStatementList);
        }
        public List<CPP14Parser.FunctionDefinitionContext> funsInOneCpp = new ArrayList<>();//函数列表
        public List<CPP14Parser.FunctionDefinitionContext> getFunsInOneCpp() {
            return funsInOneCpp;
        }
        public void setFunsInOneCpp(List<CPP14Parser.FunctionDefinitionContext> funsInOneCpp) { this.funsInOneCpp = funsInOneCpp; }
        public void setFuns(CPP14Parser.FunctionDefinitionContext fun) {
            List<CPP14Parser.FunctionDefinitionContext> newFuns= getFunsInOneCpp();
            newFuns.add(fun);
            setFunsInOneCpp(newFuns);
        }

        public List<CPP14Parser.UnqualifiedIdContext> unqualifiedsIdInOneCpp = new ArrayList<>();//标识符列表1
        public List<CPP14Parser.QualifiedIdContext> qualifiedIdsInOneCpp = new ArrayList<>();//标识符列表2

        public List<CPP14Parser.UnqualifiedIdContext> getUnqualifiedsIdInOneCpp() {return unqualifiedsIdInOneCpp; }
        public void setUnqualifiedsIdInOneCpp(List<CPP14Parser.UnqualifiedIdContext> unqualifiedsIdInOneCpp) { this.unqualifiedsIdInOneCpp = unqualifiedsIdInOneCpp; }
        public void setUnqualifiedsIdInOneCpp(CPP14Parser.UnqualifiedIdContext unqualifiedId) {
            List<CPP14Parser.UnqualifiedIdContext> newUnqualifiedIds= getUnqualifiedsIdInOneCpp();
            newUnqualifiedIds.add(unqualifiedId);
            setUnqualifiedsIdInOneCpp(newUnqualifiedIds);
        }

        public List<CPP14Parser.QualifiedIdContext> getQualifiedIdsInOneCpp() { return qualifiedIdsInOneCpp; }
        public void setQualifiedIdsInOneCpp(List<CPP14Parser.QualifiedIdContext> qualifiedIdsInOneCpp) { this.qualifiedIdsInOneCpp = qualifiedIdsInOneCpp; }
        public void setQualifiedIdsInOneCpp(CPP14Parser.QualifiedIdContext qualifiedId) {
            List<CPP14Parser.QualifiedIdContext> newQualifiedIds= getQualifiedIdsInOneCpp();
            newQualifiedIds.add(qualifiedId);
            setQualifiedIdsInOneCpp(newQualifiedIds);
        }

        List<CPP14Parser.ClassSpecifierContext> classContextListInOneCpp=new ArrayList<>();//用于基于AST的变异，记录每个源程序中的类上下文
        public List<CPP14Parser.ClassSpecifierContext> getClassContextListInOneCpp() { return classContextListInOneCpp; }
        public void setClassContextListInOneCpp(List<CPP14Parser.ClassSpecifierContext> classContextListInOneCpp) { this.classContextListInOneCpp = classContextListInOneCpp; }
        public void setClassContextListInOneCpp(CPP14Parser.ClassSpecifierContext classContext) {
            List<CPP14Parser.ClassSpecifierContext> classContextListInOneCpp=getClassContextListInOneCpp();
            classContextListInOneCpp.add(classContext);
            setClassContextListInOneCpp(classContextListInOneCpp);}

        List<CPP14Parser.TypeSpecifierContext> typeContextListInOneCpp=new ArrayList<>();//用于基于AST的变异，记录每个源程序中的type上下文
        public List<CPP14Parser.TypeSpecifierContext> getTypeContextListInOneCpp() { return typeContextListInOneCpp; }
        public void setTypeContextListInOneCpp(List<CPP14Parser.TypeSpecifierContext> typeContextListInOneCpp) { this.typeContextListInOneCpp = typeContextListInOneCpp; }
        public void setTypeContextListInOneCpp(CPP14Parser.TypeSpecifierContext typeContext) {
            List<CPP14Parser.TypeSpecifierContext> typeContextListInOneCpp=getTypeContextListInOneCpp();
            typeContextListInOneCpp.add(typeContext);
            setTypeContextListInOneCpp(typeContextListInOneCpp);
        }

        List<CPP14Parser.TrailingTypeSpecifierContext> trailTypeContextListInOneCpp=new ArrayList<>();//用于基于AST的变异，记录每个源程序中的type上下文
        public List<CPP14Parser.TrailingTypeSpecifierContext> getTrailTypeContextListInOneCpp() { return trailTypeContextListInOneCpp; }
        public void setTrailTypeContextListInOneCpp(List<CPP14Parser.TrailingTypeSpecifierContext> trailTypeContextListInOneCpp) { this.trailTypeContextListInOneCpp = trailTypeContextListInOneCpp; }
        public void setTrailTypeContextListInOneCpp(CPP14Parser.TrailingTypeSpecifierContext trailTypeContext) {
            List<CPP14Parser.TrailingTypeSpecifierContext> trailTypeContextListInOneCpp=getTrailTypeContextListInOneCpp();
            trailTypeContextListInOneCpp.add(trailTypeContext);
            setTypeContextListInOneCpp(typeContextListInOneCpp); }

        List<CPP14Parser.EnumSpecifierContext> enumContextListInOneCpp=new ArrayList<>();//用于基于AST的变异，记录每个源程序中的enum上下文
        public List<CPP14Parser.EnumSpecifierContext> getEnumContextListInOneCpp() { return enumContextListInOneCpp; }
        public void setEnumContextListInOneCpp(List<CPP14Parser.EnumSpecifierContext> enumContextListInOneCpp) { this.enumContextListInOneCpp = enumContextListInOneCpp; }
        public void setEnumContextListInOneCpp(CPP14Parser.EnumSpecifierContext enumContext) {
            List<CPP14Parser.EnumSpecifierContext> enumContextListInOneCpp=getEnumContextListInOneCpp();
            enumContextListInOneCpp.add(enumContext);
            setEnumContextListInOneCpp(enumContextListInOneCpp);
        }



    }

    // 自定义监听器，用于处理AST节点
    static class MyListener extends CPP14ParserBaseListener {
        @Override
        public void enterFunctionDefinition(CPP14Parser.FunctionDefinitionContext ctx) {
            // 处理函数定义节点
            System.out.println("Function definition found: " + ctx.getText());
        }

        @Override
        public void enterFunctionBody(CPP14Parser.FunctionBodyContext ctx) {
            // 处理函数定义节点
            System.out.println("Function Body found: " + ctx.getText());
        }

        @Override
        public void enterStatement(CPP14Parser.StatementContext ctx) {
            // 处理函数定义节点
            System.out.println("Statement found: " + ctx.getText());
        }

        @Override
        public void enterParameterDeclaration(CPP14Parser.ParameterDeclarationContext ctx) {
            // 处理函数定义节点
            System.out.println("Parameter definition found: " + ctx.getText());
        }

        @Override
        public void enterBlockDeclaration(CPP14Parser.BlockDeclarationContext ctx) {
            // 处理函数定义节点
            System.out.println("Block definition found: " + ctx.getText());
        }

        @Override
        public void enterTemplateDeclaration(CPP14Parser.TemplateDeclarationContext ctx) {
            // 处理函数定义节点
            System.out.println("Template definition found: " + ctx.getText());
        }

        @Override
        public void enterClassOrDeclType(CPP14Parser.ClassOrDeclTypeContext ctx) {
            // 处理函数定义节点
            System.out.println("ClassOrDeclType found: " + ctx.getText());
        }

        @Override
        public void enterLambdaExpression(CPP14Parser.LambdaExpressionContext ctx) {
            // 处理函数定义节点
            System.out.println("LambdaExpression found: " + ctx.getText());
        }

        @Override
        public void enterTemplateArgument(CPP14Parser.TemplateArgumentContext ctx) {
            // 处理函数定义节点
            System.out.println("TemplateArgument found: " + ctx.getText());
        }


        @Override
        public void enterAssignmentExpression(CPP14Parser.AssignmentExpressionContext ctx) {
            // 处理赋值语句节点
            System.out.println("Assignment Expression found: " + ctx.getText());
            setAssignmentExpressionList(ctx);
        }

        @Override
        public void enterPostfixExpression(CPP14Parser.PostfixExpressionContext ctx) {
            // 处理赋值语句节点
            System.out.println("Postfix Expression found: " + ctx.getText());
            setPostfixExpressionList(ctx);
        }

        @Override
        public void enterPrimaryExpression(CPP14Parser.PrimaryExpressionContext ctx) {
            // 处理函数定义节点
            System.out.println("PrimaryExpression found: " + ctx.getText());
        }

        @Override
        public void enterExpression(CPP14Parser.ExpressionContext ctx) {
            // 处理函数定义节点
            System.out.println("Expression found: " + ctx.getText());
        }

        @Override
        public void enterDeclaration(CPP14Parser.DeclarationContext ctx) {
            // 处理函数定义节点
            System.out.println("Declaration found: " + ctx.getText());
        }

        @Override
        public void enterExplicitInstantiation(CPP14Parser.ExplicitInstantiationContext ctx) {
            // 处理函数定义节点
            System.out.println("ExplicitInstantiation found: " + ctx.getText());
        }

        @Override
        public void enterLinkageSpecification(CPP14Parser.LinkageSpecificationContext ctx) {
            // 处理函数定义节点
            System.out.println(" LinkageSpecification: " + ctx.getText());
        }

        @Override
        public void enterExplicitSpecialization(CPP14Parser.ExplicitSpecializationContext ctx) {
            // 处理函数定义节点
            System.out.println("ExplicitSpecialization found: " + ctx.getText());
        }
        @Override
        public void enterNamespaceDefinition(CPP14Parser.NamespaceDefinitionContext ctx) {
            // 处理函数定义节点
            System.out.println("NamespaceDefinition found: " + ctx.getText());
        }
        @Override
        public void enterEmptyDeclaration_(CPP14Parser.EmptyDeclaration_Context ctx) {
            // 处理函数定义节点
            System.out.println("EmptyDeclaration_ found: " + ctx.getText());
        }
        @Override
        public void enterAttributeDeclaration(CPP14Parser.AttributeDeclarationContext ctx) {
            // 处理函数定义节点
            System.out.println("AttributeDeclaration found: " + ctx.getText());
        }

        @Override
        public void enterUnqualifiedId(CPP14Parser.UnqualifiedIdContext ctx) {
            // 处理函数定义节点
            System.out.println("UnqualifiedId found: " + ctx.getText());
        }

        @Override
        public void enterQualifiedId(CPP14Parser.QualifiedIdContext ctx) {
            // 处理函数定义节点
            System.out.println("QualifiedId found: " + ctx.getText());
        }

        // 你可以覆盖其他enter*和exit*方法来处理不同类型的AST节点
    }
    public static void main(String[] args) throws Exception {
        // 读取C程序源代码
        CharStream input = CharStreams.fromFileName("H:\\demo\\lambda-programs\\1227\\random\\1701\\1\\programs\\program8.cpp");
//        CharStream input = CharStreams.fromFileName("F:\\data-compiler-testing\\2024-12-16cpp-gcc-llvm-testsuit\\problem-cases\\920908-2.C");
//        CharStream input = CharStreams.fromFileName("F:\\data-compiler-testing\\2024-12-16cpp-gcc-llvm-testsuit\\problem-cases\\980506-3.c");
//        CharStream input = CharStreams.fromFileName("F:\\data-compiler-testing\\2024-12-16cpp-gcc-llvm-testsuit\\problem-cases\\concepts-requires22.C");

        // 创建词法分析器
        CPP14Lexer lexer = new CPP14Lexer(input);

        // 创建Token流
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        // 创建解析器
        CPP14Parser parser = new CPP14Parser(tokens);

        // 解析C程序，生成AST
        ParseTree tree = parser.translationUnit();

        // 打印AST树结构（可选）
        System.out.println(tree.toStringTree(parser));

        // 遍历AST树并处理节点
        ParseTreeWalker walker = new ParseTreeWalker();
        MyListener listener = new MyListener();
        walker.walk(listener, tree);
    }
}