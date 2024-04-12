package online.zust.qcqcqc.services.entity.checker;

import lombok.extern.slf4j.Slf4j;
import online.zust.qcqcqc.services.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author qcqcqc
 * @date 2024/03
 * @time 15-20-11
 */
@Component
@Slf4j
public class CheckHandler {
    /**
     * 依赖检查器
     */
    private static Map<Class<? extends DependencyChecker>, DependencyChecker> dependencyCheckers;

    @Autowired
    public void setDependencyCheckers(List<DependencyChecker> dependencyCheckers) {
        CheckHandler.dependencyCheckers = dependencyCheckers.stream().collect(Collectors.toMap(DependencyChecker::getClass, Function.identity()));
        log.info("依赖检查器加载完成: {}", dependencyCheckers);
    }

    public static DependencyChecker getChecker(Class<? extends DependencyChecker> checker) {
        if (dependencyCheckers.containsKey(checker)) {
            return dependencyCheckers.get(checker);
        }
        throw new ServiceException("未找到依赖检查器: " + checker.getCanonicalName());
    }

    public static <T extends DependencyChecker> void doCheck(Class<T> checker, Long id) {
        log.debug("使用:{} 检查依赖: {}", checker.getCanonicalName(), id);
        getChecker(checker).check(id);
    }
}
