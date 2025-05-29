DROP TABLE IF EXISTS `ticket_codes`;
CREATE TABLE IF NOT EXISTS `ticket_codes` (
    ticket_code_id BIGINT                            NOT NULL AUTO_INCREMENT COMMENT '이용권 코드 ID',
    user_id        BIGINT                                                    COMMENT '이용자 ID',
    code           VARCHAR(255)                      NOT NULL COMMENT '이용권 코드',
    status         ENUM ('ISSUED', 'UNUSED', 'USED') NOT NULL DEFAULT 'ISSUED' COMMENT '이용권 상태',
    created_at     TIMESTAMP                                  DEFAULT CURRENT_TIMESTAMP COMMENT '등록일',
    updated_at     TIMESTAMP                                  DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일',
    version        BIGINT                            NOT NULL DEFAULT 0,
    PRIMARY KEY (ticket_code_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT ='이용권 코드';
CREATE UNIQUE INDEX `uix_ticket_codes` ON ticket_codes (code);