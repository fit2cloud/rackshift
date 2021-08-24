CREATE TABLE profile
(
    id          VARCHAR(50) NOT NULL,
    name        VARCHAR(50) NOT NULL,
    content     text        NOT NULL COMMENT '内容',
    type        VARCHAR(6)  NOT NULL DEFAULT 'user' COMMENT '类型:system:系统，user:用户',
    create_time BIGINT COMMENT '创建时间',
    update_time BIGINT COMMENT '更新时间',
    PRIMARY KEY (`id`)
);

CREATE TABLE template
(
    id          VARCHAR(50) NOT NULL,
    NAME        VARCHAR(50) NOT NULL,
    content     text        NOT NULL COMMENT '内容',
    type        VARCHAR(6)  NOT NULL DEFAULT 'user' COMMENT '类型:system:系统，user:用户',
    create_time BIGINT COMMENT '创建时间',
    update_time BIGINT COMMENT '更新时间',
    PRIMARY KEY (`id`)
)


ALTER TABLE image
    ADD COLUMN profile_id VARCHAR(50) DEFAULT '' COMMENT 'profile id';

ALTER TABLE image
    ADD COLUMN template_id VARCHAR(50) DEFAULT '' COMMENT 'template id';
