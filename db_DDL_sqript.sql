CREATE DATABASE IF NOT EXISTS forum;
USE forum;

/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;

DROP TABLE IF EXISTS user;
CREATE TABLE user
(
	id				int	 not null auto_increment,
	username		varchar(50)		not null,
    password		varchar(200)	not null,
    email			varchar(50)		not null,
    role			varchar(10)		not null,
    verified		boolean,
    banned			boolean,
    code			int,
    PRIMARY KEY(id)
);

DROP TABLE IF EXISTS room;
CREATE TABLE room
(
	id				int	 			not null auto_increment,
	name			varchar(50)		not null,
    PRIMARY KEY(id)
);

DROP TABLE IF EXISTS comment;
CREATE TABLE comment 
(
	id					int	 			not null auto_increment,
	user_id				int				not null,
    room_id				int				not null,
    comment_date		datetime		not null,
    content				varchar(500),
    PRIMARY KEY(id),
    FOREIGN KEY(user_id) REFERENCES user(id) ON DELETE cascade,
    FOREIGN KEY(room_id) REFERENCES room(id) ON DELETE cascade
);

DROP TABLE IF EXISTS permission;
CREATE TABLE permission 
(
	id					int	 			not null auto_increment,
	user_id				int				not null,
    room_id				int				not null,
    post 				boolean,
    edit				boolean,
    delete_comment		boolean,
    PRIMARY KEY(id),
    FOREIGN KEY(user_id) REFERENCES user(id) ON DELETE cascade,
    FOREIGN KEY(room_id) REFERENCES room(id) ON DELETE cascade
);

DROP TABLE IF EXISTS log;
CREATE TABLE log
(
	id					int	 			not null auto_increment,
	message				varchar(70)    	not null,
    log_date			timestamp		not null,
    level 				varchar(10)    	not null,
    logger 				varchar(70)    	not null,
    PRIMARY KEY(id)
);


/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=1 */;


INSERT INTO room(name) VALUES ('Nauka'),('Kultura'),('Sport'),('Muzika');
INSERT INTO user(username, password, email, role, verified, banned, code) values 
('admin', '$2a$10$UX16JKuVniDvVRfxX6sEDul.LSFGkEvLcVp38rILTOoA/2TTrOt9a', 'igorgrubisa@hotmail.com', 'admin', 1, 0, 0), 
('mm', '$2a$10$KMBfB0DMsfyZIpxjSAGeqO/B/HuRqnp1mhBUxu7/oDXYKiPBtf2ZC', 'igorgrubisa@hotmail.com', 'moder', 1, 0, 0),
('jj', '$2a$10$OYL2ff104Z6c7..rK5LJ0.UzUpI6jhhRg5JmkstEmH25VvwYX2gTi', 'igorgrubisa@hotmail.com', 'user', 1, 0, 0);
insert into comment(user_id, room_id, comment_date, content) values 
(2, 1, '2024-04-02 22:34', 'trening je bio naporan'), (3, 1, '2024-04-15 22:34', 'neki komentar'), (3, 1, '2024-05-25 22:34', 'neki komentar'), (3, 1, '2024-05-25 22:34', 'neki komentar'),
(3, 1, '2024-05-25 22:34', 'neki komentar'),
(1, 4, '2024-04-02 22:34', 'trening je bio naporan'),
(3, 2, '2024-04-02 22:34', 'trening je bio naporan');
insert into permission(user_id, room_id, post, edit, delete_comment) values 
(1, 1, 1, 1, 1), (1, 2, 1, 1, 1), (1, 3, 1, 1, 1), (1, 4, 1, 1, 1),
(2, 1, 1, 1, 1), (2, 2, 1, 1, 1), (2, 3, 1, 1, 1), (2, 4, 1, 1, 1),
(3, 1, 1, 0, 0), (3, 2, 1, 0, 0), (3, 3, 1, 0, 0), (3, 4, 1, 0, 0);