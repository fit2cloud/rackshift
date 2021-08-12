CREATE TABLE PROFILE
(
    id          VARCHAR(50) NOT NULL,
    name          VARCHAR(50) NOT NULL,
    content     text        NOT NULL COMMENT '内容',
    type        VARCHAR(1)  NOT NULL DEFAULT '0' COMMENT '类型:0:系统，1:用户',
    image_id    VARCHAR(50)          DEFAULT NULL COMMENT '镜像 id',
    create_time BIGINT COMMENT '创建时间',
    update_time BIGINT COMMENT '更新时间',
    PRIMARY KEY (`id`)
);

CREATE TABLE install_script
(
    id          VARCHAR(50) NOT NULL,
    name          VARCHAR(50) NOT NULL,
    content     text        NOT NULL COMMENT '内容',
    type        VARCHAR(1)  NOT NULL DEFAULT '0' COMMENT '类型:0:系统，1:用户',
    image_id    VARCHAR(50)          DEFAULT NULL COMMENT '镜像 id',
    create_time BIGINT COMMENT '创建时间',
    update_time BIGINT COMMENT '更新时间',
    PRIMARY KEY (`id`)
)