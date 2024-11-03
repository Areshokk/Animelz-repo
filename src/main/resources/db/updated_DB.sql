-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema animelzdb
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema animelzdb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `animelzdb` DEFAULT CHARACTER SET utf8mb3 ;
USE `animelzdb` ;

-- -----------------------------------------------------
-- Table `animelzdb`.`anime`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `animelzdb`.`anime` (
                                                   `id` BIGINT NOT NULL,
                                                   `age_restrictions` VARCHAR(255) NULL DEFAULT NULL,
    `alternative_name` VARCHAR(255) NULL DEFAULT NULL,
    `description` VARCHAR(2000) NULL DEFAULT NULL,
    `duration` VARCHAR(255) NULL DEFAULT NULL,
    `episodes` VARCHAR(255) NULL DEFAULT NULL,
    `file_name` VARCHAR(255) NULL DEFAULT NULL,
    `protagonists` VARCHAR(255) NULL DEFAULT NULL,
    `season` INT NULL DEFAULT NULL,
    `source` VARCHAR(255) NULL DEFAULT NULL,
    `status` VARCHAR(255) NULL DEFAULT NULL,
    `studio` VARCHAR(255) NULL DEFAULT NULL,
    `title` VARCHAR(255) NULL DEFAULT NULL,
    `type` VARCHAR(255) NULL DEFAULT NULL,
    `video_url` VARCHAR(255) NULL DEFAULT NULL,
    `voiceover` VARCHAR(255) NULL DEFAULT NULL,
    PRIMARY KEY (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `animelzdb`.`genre`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `animelzdb`.`genre` (
                                                   `id` BIGINT NOT NULL AUTO_INCREMENT,
                                                   `name` VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `name` (`name` ASC) VISIBLE)
    ENGINE = InnoDB
    AUTO_INCREMENT = 17
    DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `animelzdb`.`anime_genre`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `animelzdb`.`anime_genre` (
                                                         `anime_id` BIGINT NOT NULL,
                                                         `genre_id` BIGINT NOT NULL,
                                                         PRIMARY KEY (`anime_id`, `genre_id`),
    INDEX `FKfsd5quou9lv0tlt8br825w9yn` (`genre_id` ASC) VISIBLE,
    CONSTRAINT `FK72693b55ypxe99ik71d43od0v`
    FOREIGN KEY (`anime_id`)
    REFERENCES `animelzdb`.`anime` (`id`),
    CONSTRAINT `FKfsd5quou9lv0tlt8br825w9yn`
    FOREIGN KEY (`genre_id`)
    REFERENCES `animelzdb`.`genre` (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `animelzdb`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `animelzdb`.`users` (
                                                   `id` BIGINT NOT NULL,
                                                   `activation_code` VARCHAR(255) NULL DEFAULT NULL,
    `active` BIT(1) NOT NULL,
    `email` VARCHAR(255) NULL DEFAULT NULL,
    `password` VARCHAR(255) NULL DEFAULT NULL,
    `username` VARCHAR(255) NULL DEFAULT NULL,
    `join_date` VARCHAR(255) NULL DEFAULT NULL,
    `user_image` VARCHAR(255) NULL DEFAULT NULL,
    PRIMARY KEY (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `animelzdb`.`anime_rating`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `animelzdb`.`anime_rating` (
                                                          `id` BIGINT NOT NULL,
                                                          `rating` INT NOT NULL,
                                                          `anime_id` BIGINT NULL DEFAULT NULL,
                                                          `user_id` BIGINT NULL DEFAULT NULL,
                                                          PRIMARY KEY (`id`),
    INDEX `FKgcu4rybvcsj04qvk5j8rmeeob` (`anime_id` ASC) VISIBLE,
    INDEX `FKafdhiythc8j73xht6e6pttti1` (`user_id` ASC) VISIBLE,
    CONSTRAINT `FKafdhiythc8j73xht6e6pttti1`
    FOREIGN KEY (`user_id`)
    REFERENCES `animelzdb`.`users` (`id`),
    CONSTRAINT `FKgcu4rybvcsj04qvk5j8rmeeob`
    FOREIGN KEY (`anime_id`)
    REFERENCES `animelzdb`.`anime` (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `animelzdb`.`anime_rating_seq`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `animelzdb`.`anime_rating_seq` (
    `next_val` BIGINT NULL DEFAULT NULL)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `animelzdb`.`anime_seq`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `animelzdb`.`anime_seq` (
    `next_val` BIGINT NULL DEFAULT NULL)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `animelzdb`.`animegenre`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `animelzdb`.`animegenre` (
                                                        `anime_id` BIGINT NOT NULL,
                                                        `genre_id` BIGINT NOT NULL,
                                                        PRIMARY KEY (`anime_id`, `genre_id`),
    INDEX `genre_id` (`genre_id` ASC) VISIBLE,
    CONSTRAINT `animegenre_ibfk_1`
    FOREIGN KEY (`anime_id`)
    REFERENCES `animelzdb`.`anime` (`id`),
    CONSTRAINT `animegenre_ibfk_2`
    FOREIGN KEY (`genre_id`)
    REFERENCES `animelzdb`.`genre` (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `animelzdb`.`user_role`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `animelzdb`.`user_role` (
                                                       `user_id` BIGINT NOT NULL,
                                                       `roles` ENUM('ADMIN', 'USER') NULL DEFAULT NULL,
    INDEX `FKj345gk1bovqvfame88rcx7yyx` (`user_id` ASC) VISIBLE,
    CONSTRAINT `FKj345gk1bovqvfame88rcx7yyx`
    FOREIGN KEY (`user_id`)
    REFERENCES `animelzdb`.`users` (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `animelzdb`.`users_seq`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `animelzdb`.`users_seq` (
    `next_val` BIGINT NULL DEFAULT NULL)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `animelzdb`.`users_want_to_watch_list`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `animelzdb`.`users_want_to_watch_list` (
                                                                      `user_id` BIGINT NOT NULL,
                                                                      `want_to_watch_list_id` BIGINT NOT NULL,
                                                                      INDEX `FKfkewjtfdkf3oxfcfm5p787dw4` (`want_to_watch_list_id` ASC) VISIBLE,
    INDEX `FK1axu932gok5oeppqbbuoqoqle` (`user_id` ASC) VISIBLE,
    CONSTRAINT `FK1axu932gok5oeppqbbuoqoqle`
    FOREIGN KEY (`user_id`)
    REFERENCES `animelzdb`.`users` (`id`),
    CONSTRAINT `FKfkewjtfdkf3oxfcfm5p787dw4`
    FOREIGN KEY (`want_to_watch_list_id`)
    REFERENCES `animelzdb`.`anime` (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `animelzdb`.`users_watched_list`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `animelzdb`.`users_watched_list` (
                                                                `user_id` BIGINT NOT NULL,
                                                                `watched_list_id` BIGINT NOT NULL,
                                                                INDEX `FKcum5jflsalx42np63pp6negqi` (`watched_list_id` ASC) VISIBLE,
    INDEX `FKgid1j07n55fi4fau6tw5h2qfd` (`user_id` ASC) VISIBLE,
    CONSTRAINT `FKcum5jflsalx42np63pp6negqi`
    FOREIGN KEY (`watched_list_id`)
    REFERENCES `animelzdb`.`anime` (`id`),
    CONSTRAINT `FKgid1j07n55fi4fau6tw5h2qfd`
    FOREIGN KEY (`user_id`)
    REFERENCES `animelzdb`.`users` (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `animelzdb`.`users_watching_list`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `animelzdb`.`users_watching_list` (
                                                                 `user_id` BIGINT NOT NULL,
                                                                 `watching_list_id` BIGINT NOT NULL,
                                                                 INDEX `FKicoktdcocwo2pqjragq2gw0ej` (`watching_list_id` ASC) VISIBLE,
    INDEX `FK1pb00kw7ichrpaq4se4j2xfc1` (`user_id` ASC) VISIBLE,
    CONSTRAINT `FK1pb00kw7ichrpaq4se4j2xfc1`
    FOREIGN KEY (`user_id`)
    REFERENCES `animelzdb`.`users` (`id`),
    CONSTRAINT `FKicoktdcocwo2pqjragq2gw0ej`
    FOREIGN KEY (`watching_list_id`)
    REFERENCES `animelzdb`.`anime` (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb3;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
