package online.zust.qcqcqc.services.entity.checker;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import online.zust.qcqcqc.services.exception.ServiceException;

/**
 * @author qcqcqc
 * Date: 2024/3/20
 * Time: 7:54
 */
public interface DependencyChecker {
    /**
     * 检查是否存在依赖
     * 示例：doCheck(userMapper, new LambdaQueryWrapper<User>().eq(User::getAvatarImageId, id), "用户头像正在使用该照片");
     *
     * @param id id
     * @throws ServiceException ServiceException
     */
    void check(Long id) throws ServiceException;

    /**
     * 检查是否存在依赖
     *
     * @param mapper  mapper
     * @param wrapper wrapper
     * @param msg     msg
     * @param <T>     T
     */
    default <T> void doCheck(BaseMapper<T> mapper, LambdaQueryWrapper<T> wrapper, String msg) {
        if ((mapper.selectCount(wrapper) != 0)) {
            throw new ServiceException(msg);
        }
    }

    /**
     * 下一个检查
     */
    void next();
}
