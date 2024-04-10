CREATE TABLE operator_log
(
    id          BIGINT        NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    level       VARCHAR(63)   NOT NULL COMMENT '日志级别',
    msg         VARCHAR(255)  NOT NULL COMMENT '日志信息',
    create_time DATETIME      NOT NULL COMMENT '创建时间',
    create_by   BIGINT COMMENT '操作人',
    success     TINYINT       NOT NULL DEFAULT 0 COMMENT '是否成功',
    cause       VARCHAR(2047) NOT NULL DEFAULT '' COMMENT '失败原因'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;
