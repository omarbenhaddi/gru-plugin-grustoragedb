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
PRIMARY KEY ( demand_id )
);

CREATE INDEX db_demand_index on grustoragedb_demand ( id, type_id );

--
-- Structure for table grustoragedb_notification
--
DROP TABLE IF EXISTS grustoragedb_notification;
CREATE TABLE grustoragedb_notification (
id int NOT NULL,
demand_id varchar(50) NOT NULL,
demand_type_id varchar(50) NOT NULL,
date bigint NOT NULL,
has_backoffice int  default 0 NOT NULL,
has_sms int default 0 NOT NULL ,
has_customer_email int default 0 NOT NULL ,
has_mydashboard int default 0 NOT NULL ,
has_broadcast_email int default 0 NOT NULL , 
notification_content LONG VARBINARY,
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
      REFERENCES grustoragedb_demand (id, type_id) ON DELETE CASCADE ON UPDATE RESTRICT;
