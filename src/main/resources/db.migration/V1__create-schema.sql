CREATE TABLE `user` (
  `id`       BIGINT(20)   NOT NULL AUTO_INCREMENT,
  `name`     VARCHAR(30)  NOT NULL,
  `password` VARCHAR(100) NOT NULL,
  `username` VARCHAR(30)  NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_username` (`username`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
