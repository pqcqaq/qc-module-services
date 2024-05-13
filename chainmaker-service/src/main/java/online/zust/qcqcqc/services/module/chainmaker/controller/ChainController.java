package online.zust.qcqcqc.services.module.chainmaker.controller;

import online.zust.qcqcqc.services.entity.dto.Result;
import online.zust.qcqcqc.services.module.chainmaker.entity.dto.Contract;
import online.zust.qcqcqc.services.module.chainmaker.entity.response.BlockInfo;
import online.zust.qcqcqc.services.module.chainmaker.entity.response.ChainConfig;
import online.zust.qcqcqc.services.module.chainmaker.entity.response.TransactionInfo;
import online.zust.qcqcqc.services.module.chainmaker.entity.response.TxResponse;
import online.zust.qcqcqc.services.module.chainmaker.service.ChainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author qcqcqc
 */
@RestController
public class ChainController {

    private final ChainService chainService;

    @Autowired
    public ChainController(ChainService chainService) {
        this.chainService = chainService;
    }

    /**
     * 获取链配置
     *
     * @return 链配置
     */
    @GetMapping("/chainConfig")
    public Result<ChainConfig> getChainConfig() {
        return Result.success(200, "success", chainService.getChainConfig());
    }

    /**
     * 创建合约
     *
     * @return 交易响应
     */
    @GetMapping("/createContract")
    public Result<TxResponse> createContract() {
        return Result.success(200, "success", chainService.createContract());
    }

    /**
     * 调用合约
     *
     * @return 交易响应
     */
    @GetMapping("/invokeContract")
    public Result<TxResponse> invokeContract() {
        return Result.success(200, "success", chainService.invokeContract());
    }

    /**
     * 查询合约
     *
     * @return 交易响应
     */
    @GetMapping("/queryContract")
    public Result<TxResponse> queryContract() {
        return Result.success(200, "success", chainService.queryContract());
    }

    /**
     * 上传并创建合约
     *
     * @return 交易响应
     */
    @PostMapping("/uploadAndCreateContract")
    public Result<TxResponse> uploadAndCreateContract(@RequestParam("file") MultipartFile file,
                                                      @Validated Contract contract
    ) {
        return Result.success(200, "success", chainService.uploadAndCreateContract(file, contract.getContractName(), contract.getContractVersion(), contract.getRuntimeType()));
    }

    /**
     * 根据区块高度获取区块信息
     *
     * @param height 交易hash
     * @return 交易信息
     */
    @GetMapping("/getBlockByHeight")
    public Result<BlockInfo> getBlockByHeight(@RequestParam("height") Long height) {
        return Result.success(200, "success", chainService.getBlockByHeight(height));
    }

    /**
     * 根据交易ID获取交易信息
     *
     * @param txId 交易ID
     * @return 交易信息
     */
    @GetMapping("/getTxByTxId")
    public Result<TransactionInfo> getTxByTxId(@RequestParam("txId") String txId) {
        return Result.success(200, "success", chainService.getTxByTxId(txId));
    }

    /**
     * 获取合约列表
     *
     * @return 合约列表
     */
    @GetMapping("/getContractList")
    public Result<List<online.zust.qcqcqc.services.module.chainmaker.entity.response.Contract>> getContractList() {
        return Result.success(200, "success", chainService.getContractList());
    }

    /**
     * 根据交易hash获取区块信息
     *
     * @param hash 交易hash
     * @return 区块信息
     */
    @GetMapping("/getBlockByTxHash")
    public Result<BlockInfo> getBlockByTxHash(@RequestParam("hash") String hash) {
        return Result.success(200, "success", chainService.getBlockByHash(hash));
    }

    /**
     * 根据交易id获取区块信息
     *
     * @param txId 交易id
     * @return 交易信息
     */
    @GetMapping("/getBlockByTxId")
    public Result<BlockInfo> getBlockByTxId(@RequestParam("txId") String txId) {
        return Result.success(200, "success", chainService.getBlockByTxId(txId));
    }

}
