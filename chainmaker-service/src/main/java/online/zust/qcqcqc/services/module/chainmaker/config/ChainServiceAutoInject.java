package online.zust.qcqcqc.services.module.chainmaker.config;

import online.zust.qcqcqc.services.module.chainmaker.chainmaker.client.InitClient;
import online.zust.qcqcqc.services.module.chainmaker.chainmaker.contract.DefaultContract;
import online.zust.qcqcqc.services.module.chainmaker.controller.ChainController;
import online.zust.qcqcqc.services.module.chainmaker.service.impl.ChainServiceImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author qcqcqc
 * Date: 2024/5/13
 * Time: 下午9:53
 */
@Configuration
@Import({
        InitClient.class,
        DefaultContract.class,
        ChainController.class,
        ChainServiceImpl.class
})
public class ChainServiceAutoInject {
}
