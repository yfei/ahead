package cn.dcube.ahead.commons.log.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cn.dcube.ahead.commons.log.enums.OperationType;

/**
 * 自定义日志注解
 *
 * @author hejunjian
 * @date 2020/12/4 11:03
 */
// 注解生命周期 程序运行时存在
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.PARAMETER})
@Documented
public @interface Log {


    /**
     * 模块名称
     *
     * @return 模块名称
     */
    public String module() default "";

    /**
     * 操作类型
     *
     * @return 操作类型
     */
    public OperationType operationType() default OperationType.OTHER;
    
    
    


    /**
     * 操作表名
     *
     * @return 操作表名
     */
    public String tableName() default "";


    /**
     * 是否保存请求参数
     *
     * @return 是否保存请求参数
     */
    public boolean isSaveParam() default true;

    
}
