create table address (
	id uuid not null,
	additional_address varchar(100),
	city varchar(100) not null,
	created_date timestamp(6) not null,
	last_modified_date timestamp(6) not null,
	neighborhood varchar(100) not null,
	number integer not null,
	public_area varchar(100) not null,
	state varchar(50) not null,
	zip_code varchar(8) not null,
	user_id uuid not null,
	primary key (id)
)

create table user_account (
	id uuid not null,
	created_date timestamp(6) not null,
	email varchar(100) not null,
	last_modified_date timestamp(6) not null,
	name varchar(100) not null,
	password varchar(200) not null,
	primary key (id)
)

create table user_account_role (
	user_account_id uuid not null,
	roles varchar(255)
)

alter table if exists user_account 
   drop constraint if exists unique_user_account_email

alter table if exists user_account 
   add constraint unique_user_account_email unique (email)

alter table if exists address 
   add constraint fk_user_id_address 
   foreign key (user_id) 
   references user_account

alter table if exists user_account_role 
   add constraint fk_user_id_role 
   foreign key (user_account_id) 
   references user_account