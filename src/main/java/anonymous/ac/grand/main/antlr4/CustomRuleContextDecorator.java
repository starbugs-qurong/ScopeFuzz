package anonymous.ac.grand.main.antlr4;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * anonymousAuthor
 *2025-1-15
 * RuleContext的装饰器类，为了重写其中的getText函数，在其中加入空格
 */

public class CustomRuleContextDecorator {
    private final RuleContext ruleContext;

    public CustomRuleContextDecorator(RuleContext ruleContext) {
        this.ruleContext = ruleContext;
    }

    public String getTextWithSpaces() {
        if(ruleContext==null)return "";
        if (ruleContext.getChildCount() == 0) {
            return "";
        } else {
            StringBuilder builder = new StringBuilder();

            for (int i = 0; i < ruleContext.getChildCount(); ++i) {
                if (i > 0) {
                    builder.append(" ");
                }
                Object node=ruleContext.getChild(i);
                if (node instanceof RuleContext) {
                    // 处理 RuleContext 类型
                    RuleContext childRuleContext = (RuleContext) node;
                    CustomRuleContextDecorator decorator = new CustomRuleContextDecorator(childRuleContext);
                    builder.append(decorator.getTextWithSpaces());
                } else if (node instanceof TerminalNodeImpl) {
                    // 处理 TerminalNodeImpl 类型
                    TerminalNodeImpl childTerminalNode = (TerminalNodeImpl) node;
                    builder.append(childTerminalNode.getText());
                } else {
                    // 处理未知类型或错误情况
                    throw new IllegalArgumentException("Unsupported node type: " + node.getClass().getName());
                }
//                builder.append(childRuleContext.getText());
            }

            return builder.toString();
        }
    }

    // 你可以根据需要添加其他方法，以代理到原始的RuleContext实例
    public int getChildCount() {
        return ruleContext.getChildCount();
    }

    // 示例：如何使用这个装饰器
    public static void main(String[] args) {
        // 假设你有一个RuleContext实例
        RuleContext originalRuleContext = new RuleContext(); // 从解析器或其他地方获取

        CustomRuleContextDecorator decorator = new CustomRuleContextDecorator(originalRuleContext);
        String customText = decorator.getTextWithSpaces();
        System.out.println(customText);
    }
    /**
     * anonymousAuthor
     * 2025-2-6
     * 把一个上下文列表中的每一个上下文，转换成文本后返回一个字符串列表
     *
     * @param <T> 上下文类型，必须是ParserRuleContext的子类
     * @param contexts 上下文列表
     * @return 包含每个上下文文本的字符串列表
     */
    public <T extends ParserRuleContext> List<String> getTextByContext(List<T> contexts) {
        List<String> result = new ArrayList<>();
        for (ParserRuleContext context : contexts) {
            result.add(context.getText());
        }
        return result;
    }

}