CREATE TABLE operator_log
(
    id          BIGINT        NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    level       VARCHAR(63)   NOT NULL COMMENT '日志级别',
    msg         VARCHAR(255)  NOT NULL COMMENT '日志信息',
    createTime  DATETIME      NOT NULL COMMENT '创建时间',
    createBy    BIGINT        NOT NULL COMMENT '操作人',
    success     TINYINT       NOT NULL DEFAULT 0 COMMENT '是否成功',
    cause       VARCHAR(255)  NOT NULL DEFAULT '' COMMENT '失败原因'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;