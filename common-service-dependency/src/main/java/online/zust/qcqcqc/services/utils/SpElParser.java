package online.zust.qcqcqc.services.utils;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author qcqcqc
 * @date 2024/4/9
 * @time 9:55
 */
@Component
public class SpElParser implements ApplicationContextAware {
    /**
     * SpEL 表达式解析器
     */
    private static final ExpressionParser EXPRESSION_PARSER = new SpelExpressionParser();

    /**
     * SpEL 上下文
     */
    private static ApplicationContext applicationContext;

    /**
     * 解析 SpEL 表达式
     *
     * @param expressionString SpEL 表达式字符串
     * @param paramMap         参数映射
     * @param clazz            返回值类型
     * @param <T>              返回值类型
     * @return 解析结果
     */
    public static <T> T parseExpression(String expressionString, Map<String, Object> paramMap, Class<T> clazz) {
        StandardEvaluationContext evaluationContext = initEvaluationContext(paramMap);
        return EXPRESSION_PARSER.parseExpression(expressionString).getValue(evaluationContext, clazz);
    }

    /**
     * 解析 SpEL 表达式
     *
     * @param expressionString SpEL 表达式字符串
     * @param context          解析上下文
     * @param paramMap         参数映射
     * @param clazz            返回值类型
     * @param <T>              返回值类型
     * @return 解析结果
     */
    public static <T> T parseExpression(String expressionString, ParserContext context, Map<String, Object> paramMap, Class<T> clazz) {
        StandardEvaluationContext evaluationContext = initEvaluationContext(paramMap);
        return EXPRESSION_PARSER.parseExpression(expressionString, context).getValue(evaluationContext, clazz);
    }

    /**
     * 初始化 SpEL 上下文
     *
     * @param paramMap 参数映射
     * @return SpEL 上下文
     */
    @NotNull
    private static StandardEvaluationContext initEvaluationContext(Map<String, Object> paramMap) {
        StandardEvaluationContext evaluationContext = new StandardEvaluationContext();
        evaluationContext.setBeanResolver(new BeanFactoryResolver(applicationContext));
        paramMap.forEach(evaluationContext::setVariable);
        return evaluationContext;
    }

    /**
     * 设置上下文
     *
     * @param applicationContext 上下文
     * @throws BeansException BeansException
     */
    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        SpElParser.applicationContext = applicationContext;
    }
}
