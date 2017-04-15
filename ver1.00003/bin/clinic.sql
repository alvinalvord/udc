drop schema if exists `clinic`;

create schema `clinic`;

create table `clinic`.`users` (
	`id` int not null auto_increment,
	`name` varchar(50) not null,
	`username` varchar(50) not null,
	`password` varchar(50) not null,
	`user_level` int not null,
	primary key (`id`),
	unique index `id_UNIQUE` (`id` ASC),
	unique index `username_UNIQUE` (`username` ASC)
);

create table `clinic`.`access_levels` (
	`user_level` int not null,
	`access_name` varchar(50) not null,
	primary key (`user_level`),
	unique index `user_level_UNIQUE` (`user_level` ASC)
);

create table `clinic`.`appointments` (
	`id` int not null auto_increment,
	`name` varchar(50) not null,
	`date` date not null,
	`start_time` time not null,
	`end_time` time not null,
	`appointer_id` int not null,
	`appointee_id` int not null,
	`status_done` boolean not null,
	primary key (`id`),
	unique index `id_UNIQUE` (`id` ASC)
);

insert into `clinic`.`access_levels` (
	`user_level`,
	`access_name`
)
values ('1', 'client');

insert into `clinic`.`access_levels` (
	`user_level`,
	`access_name`
)
values ('2', 'secretary');

insert into `clinic`.`access_levels` (
	`user_level`,
	`access_name`
)
values ('3', 'doctor');

insert into `clinic`.`users` (
	`id`,
	`name`,
	`username`,
	`password`,
	`user_level`
)
values (
	'1',
	'doctor 1',
	'doctor 1',
	'1234',
	'3'
);

insert into `clinic`.`users` (
	`id`,
	`name`,
	`username`,
	`password`,
	`user_level`
)
values (
	'2',
	'doctor 2',
	'doctor 2',
	'1234',
	'3'
);

insert into `clinic`.`users` (
	`id`,
	`name`,
	`username`,
	`password`,
	`user_level`
)
values (
	'3',
	'secretary',
	'secretary',
	'1234',
	'2'
);

insert into `clinic`.`users` (
	`id`,
	`name`,
	`username`,
	`password`,
	`user_level`
)
values (
	'4',
	'client 1',
	'client 1',
	'1234',
	'1'
);

insert into `clinic`.`users` (
	`id`,
	`name`,
	`username`,
	`password`,
	`user_level`
)
values (
	'5',
	'client 2',
	'client 2',
	'1234',
	'1'
);

