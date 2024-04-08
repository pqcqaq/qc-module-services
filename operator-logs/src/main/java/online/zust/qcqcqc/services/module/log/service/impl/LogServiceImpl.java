package online.zust.qcqcqc.services.module.log.service.impl;

import online.zust.qcqcqc.services.module.log.entity.OperatorLog;
import online.zust.qcqcqc.services.module.log.mapper.OperatorLogMapper;
import online.zust.qcqcqc.services.module.log.service.LogService;
import online.zust.qcqcqc.utils.EnhanceService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author qcqcqc
 */
@Service
public class LogServiceImpl extends EnhanceService<OperatorLogMapper, OperatorLog> implements LogService {
    @Override
    @Async
    public void saveAsync(OperatorLog operatorLog) {
        this.save(operatorLog);
    }
}
