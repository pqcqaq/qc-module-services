package online.zust.qcqcqc.services.entity.checker;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.zust.qcqcqc.services.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

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
    private static List<DependencyChecker> dependencyCheckers;

    @Autowired
    public void setDependencyCheckers(List<DependencyChecker> dependencyCheckers) {
        CheckHandler.dependencyCheckers = dependencyCheckers;
        log.info("依赖检查器加载完成: {}", dependencyCheckers);
    }

    public static DependencyChecker getChecker(Class<? extends DependencyChecker> checker) {
        for (DependencyChecker dependencyChecker : dependencyCheckers) {
            if (dependencyChecker.getClass().equals(checker)) {
                return dependencyChecker;
            }
        }
        throw new ServiceException("未找到对应的依赖检查器");
    }

    public static <T extends DependencyChecker> void doCheck(Class<T> checker, Long id) {
        log.debug("使用:{} 检查依赖: {}", checker.getCanonicalName(), id);
        getChecker(checker).check(id);
    }
}
