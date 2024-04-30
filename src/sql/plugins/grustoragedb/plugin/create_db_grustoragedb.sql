--
-- Structure for table grustoragedb_demand
--
DROP TABLE IF EXISTS grustoragedb_demand;
CREATE TABLE grustoragedb_demand (
demand_id int  AUTO_INCREMENT,
id varchar(50) NOT NULL,
type_id varchar(50) NOT NULL,
subtype_id varchar(50) NULL,
reference varchar(50) NOT NULL,
status_id int default 0 NOT NULL ,
customer_id varchar(255) NULL,
creation_date bigint NOT NULL,
closure_date bigint NULL,
max_steps int NULL,
current_step int NULL,
modify_date bigint NULL,
PRIMARY KEY ( demand_id )
);

CREATE INDEX db_demand_index on grustoragedb_demand ( id, type_id );
CREATE INDEX index_demand_id_type_customer ON grustoragedb_demand (demand_id,type_id,customer_id);

--
-- Structure for table grustoragedb_notification
--
DROP TABLE IF EXISTS grustoragedb_notification;
CREATE TABLE grustoragedb_notification (
id int NOT NULL,
demand_id varchar(50) NOT NULL,
demand_type_id varchar(50) NOT NULL,
date bigint NOT NULL,
PRIMARY KEY (id)
);



CREATE INDEX idx_grustoragedb_notification_date on grustoragedb_notification (date ASC, demand_type_id ASC) ;

DROP TABLE IF EXISTS grustoragedb_notification_event;
CREATE TABLE grustoragedb_notification_event (
id int AUTO_INCREMENT,
event_date bigint NOT NULL,
demand_id varchar(50) NOT NULL,
demand_type_id varchar(50) NOT NULL,
notification_date bigint NOT NULL,
msg_id varchar(255) ,
type varchar(255) default '' NOT NULL,
status varchar(255) default '' NOT NULL,
redelivry int default '0',
message long varchar,
PRIMARY KEY (id)
);

CREATE INDEX  IDX_NOTIFICATION_EVENT_DEMAND_ID on  grustoragedb_notification_event (demand_id ASC, demand_type_id ASC) ;
CREATE INDEX IDX_NOTIFICATION_EVENT_DATE on grustoragedb_notification_event (event_date ASC, demand_type_id ASC) ;

ALTER TABLE grustoragedb_notification ADD CONSTRAINT fk_grustoragedb_notification_demand_id FOREIGN KEY (demand_id, demand_type_id)
      REFERENCES grustoragedb_demand (demand_id, type_id) ON DELETE CASCADE ON UPDATE RESTRICT;
      
--
-- Structure for table grustoragedb_status
--


DROP TABLE IF EXISTS grustoragedb_status;
CREATE TABLE grustoragedb_status (
id_status int AUTO_INCREMENT,
status long varchar NOT NULL,
status_code varchar(255) default '' NOT NULL,
label_color_code varchar(255) default '' NOT NULL,
banner_color_code varchar(255) default '' NOT NULL,
PRIMARY KEY (id_status)
);
--
-- Structure for table grustoragedb_demand_type
--

DROP TABLE IF EXISTS grustoragedb_demand_type;
CREATE TABLE grustoragedb_demand_type (
id_demand_type int AUTO_INCREMENT,
code varchar(255) default '' NOT NULL,
label varchar(255) default '' NOT NULL,
code_category varchar(255) default '',
PRIMARY KEY (id_demand_type)
);

--
-- Structure for table grustoragedb_demand_category
--

DROP TABLE IF EXISTS grustoragedb_demand_category;
CREATE TABLE grustoragedb_demand_category (
id_demand_category int AUTO_INCREMENT,
code varchar(255) default '' NOT NULL,
label long varchar NOT NULL,
PRIMARY KEY (id_demand_category)
);

--
-- Structure for table grustoragedb_notification_content
--

DROP TABLE IF EXISTS grustoragedb_notification_content;
CREATE TABLE grustoragedb_notification_content (
id_notification_content int AUTO_INCREMENT,
notification_id int NOT NULL,
notification_type varchar(255) default '' NOT NULL,
status_id int NULL,
content mediumblob,
PRIMARY KEY (id_notification_content)
);

CREATE INDEX index_notification_id ON grustoragedb_notification_content (notification_id);
CREATE INDEX index_notification_type ON grustoragedb_notification_content (notification_type);