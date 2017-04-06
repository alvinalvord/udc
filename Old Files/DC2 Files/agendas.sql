drop schema if exists `agendas`;

create schema `agendas`;

create table `agendas`.`table`(
	`id` int not null auto_increment, 
	`name` varchar(50) not null,
	`start` datetime not null,
	`end` datetime not null,
	`type` varchar(1) not null,
	`status` varchar(1) not null,
	primary key (`id`),
	unique index `id_UNIQUE` (`id` ASC),
	unique index `start_UNIQUE` (`start` ASC),
	unique index `end_UNIQUE` (`end` ASC)
);