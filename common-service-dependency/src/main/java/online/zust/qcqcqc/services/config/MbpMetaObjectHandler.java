package online.zust.qcqcqc.services.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.RequiredArgsConstructor;
import online.zust.qcqcqc.services.utils.CurrentUserGetter;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author qcqcqc
 */
@Component
@RequiredArgsConstructor
public class MbpMetaObjectHandler implements MetaObjectHandler {

    private final CurrentUserGetter currentUserGetter;

    /**
     * 自定义插入时填充规则
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        // 注意是类属性字段名称，不是表字段名称
        this.setFieldValByName("createTime", new Date(), metaObject);
        this.setFieldValByName("updateTime", new Date(), metaObject);

        // 设置创建人
        this.setFieldValByName("createBy", currentUserGetter.getCurrentUser(), metaObject);
        // logic
        this.setFieldValByName("deleted", false, metaObject);
        this.setFieldValByName("sequence", System.currentTimeMillis(), metaObject);
    }

    /**
     * 自定义更新时填充规则
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        // 注意是类属性字段名称，不是表字段名称
        this.setFieldValByName("updateTime", new Date(), metaObject);

        // 设置更新人
        this.setFieldValByName("updateBy", currentUserGetter.getCurrentUser(), metaObject);
    }

}
