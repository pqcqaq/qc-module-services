package online.zust.qcqcqc.services.module.log.common;

/**
 * @author qcqcqc
 * Date: 2024/4/11
 * Time: 23:45
 */
@FunctionalInterface
public interface MetadataAppender {
    /**
     * 获取key
     *
     * @return key
     */
    default String getKey() {
        return this.getClass().getSimpleName();
    }

    /**
     * append metadata
     *
     * @return metadata
     */
    String appendMetadata();
}
