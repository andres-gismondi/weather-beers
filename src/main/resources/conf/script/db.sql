CREATE TABLE `user` (
            `id` INT(11) NOT NULL AUTO_INCREMENT,
            `password` VARCHAR(45) NOT NULL,
            `first_name` VARCHAR(45) NOT NULL,
            `last_name` VARCHAR(45) NOT NULL,
            `surname` VARCHAR(45) NOT NULL,
            `age` INT(11) NOT NULL,
            `role` VARCHAR(45) NOT NULL,
            PRIMARY KEY (`id`)
);
CREATE TABLE `meetup` (
           `id` INT(11) NOT NULL AUTO_INCREMENT,
           `name` VARCHAR(45) NOT NULL,
           `city` VARCHAR(45) NOT NULL,
           `creator` INT(11) NOT NULL,
           `beers` INT(11) NOT NULL,
           `date` DATETIME NOT NULL,
           `duration` INT(11) NULL,
           `temp`  INT(11) NOT NULL,
           PRIMARY KEY (`id`),
           FOREIGN KEY (`creator`) REFERENCES `user` (`id`)
);
CREATE TABLE `meetup_user` (
            `meetup_id` int(11) NOT NULL,
            `user_id` int(11) NOT NULL,
            `checkin` TINYINT(1) NOT NULL DEFAULT 0,
            PRIMARY KEY (`meetup_id`,`user_id`),
            KEY `user_id` (`user_id`),
            CONSTRAINT `meetups_users_ibfk_1`
                FOREIGN KEY (`meetup_id`) REFERENCES `meetup` (`id`),
            CONSTRAINT `meetups_users_ibfk_2`
                FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);

INSERT INTO `user` (id, password, first_name, last_name, surname, age, role) VALUES
(1, 'cataratas', 'andres', 'gismondi', 'andresgismondi', 41, 'ADMIN'),
(2, 'password', 'julian', 'gismondi', 'juliangismondi', 56, 'USER');

INSERT INTO `meetup` (id, name, city, creator, beers, date, duration,temp) VALUES
(1, 'Cervezas TiroLindo', 'Buenos Aires, ar',  1, 0, '2020-09-13 20:00:00', 1, 25),
(2, 'Cumple Fabi', 'Buenos Aires, ar',  1, 1, '2020-09-15 20:00:00', 1, 25);

INSERT INTO `meetup_user` (meetup_id, user_id, checkin) VALUES
(1,1,0),
(2,1,1),
(2,2,0);