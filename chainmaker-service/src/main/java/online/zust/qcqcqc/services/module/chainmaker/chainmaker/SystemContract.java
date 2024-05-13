package online.zust.qcqcqc.services.module.chainmaker.chainmaker;

import lombok.extern.slf4j.Slf4j;
import online.zust.qcqcqc.services.exception.ServiceException;
import online.zust.qcqcqc.services.module.chainmaker.entity.response.BlockInfo;
import org.chainmaker.pb.common.ChainmakerBlock;
import org.chainmaker.sdk.ChainClient;

/**
 * @author qcqcqc
 */
@Slf4j
public class SystemContract {

    public static BlockInfo getBlockByHeight(Long height, ChainClient chainClient) {
        ChainmakerBlock.BlockInfo blockInfo;
        try {
            blockInfo = chainClient.getBlockByHeight(height, false, 10000);
        } catch (Exception e) {
            log.error("get block by height error: {}", e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return new BlockInfo(blockInfo);
    }
}
