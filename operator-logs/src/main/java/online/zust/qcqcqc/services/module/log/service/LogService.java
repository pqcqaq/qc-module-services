package online.zust.qcqcqc.services.module.log.service;

import online.zust.qcqcqc.services.module.log.entity.OperatorLog;
import online.zust.qcqcqc.utils.IServiceEnhance;

/**
 * @author qcqcqc
 */
public interface LogService extends IServiceEnhance<OperatorLog> {
    void saveAsync(OperatorLog operatorLog);
}
