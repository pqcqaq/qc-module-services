package online.zust.qcqcqc.services.entity.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author qcqcqc
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Slf4j
public class PageResult<T> {
    /**
     * 当前页码
     */
    private Long pageNum;
    /**
     * 每页数量
     */
    private Long pageSize;
    /**
     * 总条数
     */
    private Long total;
    /**
     * 分页数据
     */
    private List<T> data;

    public PageResult(Page<?> page, List<T> datas) {
        this.pageNum = page.getCurrent();
        this.pageSize = page.getSize();
        this.total = page.getTotal();
        this.data = datas;
    }

    public PageResult(Page<T> pageResult){
        this.pageNum = pageResult.getCurrent();
        this.pageSize = pageResult.getSize();
        this.total = pageResult.getTotal();
        this.data = pageResult.getRecords();
    }
}
