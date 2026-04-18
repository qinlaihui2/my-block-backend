-- 在 Hibernate ddl 执行之后运行（见 application.yml defer-datasource-initialization）。
-- 线上若 posts 已由旧版本建成 varchar(255)，此处强制改为大文本；与 ddl-auto: update 是否升级列无关。
ALTER TABLE posts MODIFY COLUMN content LONGTEXT;
ALTER TABLE posts MODIFY COLUMN summary TEXT;
