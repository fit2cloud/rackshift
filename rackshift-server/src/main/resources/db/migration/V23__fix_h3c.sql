ALTER TABLE bare_metal MODIFY COLUMN machine_model VARCHAR (100) DEFAULT '' COMMENT '机器型号';

ALTER TABLE workflow MODIFY COLUMN brands VARCHAR (1000) DEFAULT '' COMMENT '支持的品牌';

UPDATE workflow
SET brands = "['DELL', 'HP', 'Inspur','ZTE', 'Huawei', 'New H3C Technologies Co., Ltd.']"
WHERE event_type = 'POST_OS_WORKFLOW_START';