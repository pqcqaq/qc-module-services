package online.zust.qcqcqc.services.module.tasks.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import online.zust.qcqcqc.services.module.tasks.entity.DynamicCronTask;
import org.apache.ibatis.annotations.Mapper;


/**
 * @author qcqcqc
 * Date: 2024/4/25
 * Time: 下午8:53
 */
@Mapper
public interface DynamicTaskMapper extends BaseMapper<DynamicCronTask> {

}
