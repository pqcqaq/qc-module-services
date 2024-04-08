package online.zust.qcqcqc.services.module.log.service;

import online.zust.qcqcqc.services.module.log.entity.OperatorLog;
import online.zust.qcqcqc.utils.IServiceEnhance;

/**
 * @author qcqcqc
 */
public interface LogService extends IServiceEnhance<OperatorLog> {
    /**
     * 异步保存操作日志
     * @param operatorLog 操作日志
     */
    void saveAsync(OperatorLog operatorLog);
}
