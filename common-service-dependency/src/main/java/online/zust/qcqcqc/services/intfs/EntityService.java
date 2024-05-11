package online.zust.qcqcqc.services.intfs;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import online.zust.qcqcqc.utils.EnhanceService;
import online.zust.qcqcqc.utils.IServiceEnhance;
import online.zust.qcqcqc.utils.entity.BaseEntity;

/**
 * @author qcqcqc
 * Date: 2024/5/10
 * Time: 下午6:38
 * 在这里实现Entity特有的一些逻辑
 */
public class EntityService<Mapper extends BaseMapper<Entity>, Entity extends BaseEntity> extends EnhanceService<Mapper, Entity> implements IServiceEnhance<Entity> {
}
