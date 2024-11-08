package online.zust.qcqcqc.services.module.chainmaker.entity.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import online.zust.qcqcqc.services.module.chainmaker.entity.response.blockinfo.Block;
import org.chainmaker.pb.common.ChainmakerBlock;

/**
 * @author qcqcqc
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlockInfo {

    private Block block;

    public BlockInfo(ChainmakerBlock.BlockInfo blockInfo){
        this.block = new Block(blockInfo.getBlock());
    }
}
