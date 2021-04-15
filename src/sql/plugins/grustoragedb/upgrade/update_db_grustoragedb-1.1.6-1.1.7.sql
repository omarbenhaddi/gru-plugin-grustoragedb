
DROP TABLE IF EXISTS grustoragedb_notification_event;
CREATE TABLE grustoragedb_notification_event (
id int AUTO_INCREMENT,
event_date bigint NOT NULL,
demand_id varchar(50) NOT NULL,
demand_type_id varchar(50) NOT NULL,
notification_date bigint NOT NULL,
type varchar(255) default '' NOT NULL,
status varchar(255) default '' NOT NULL,
redelivry int default '0',
message long varchar,
PRIMARY KEY (id)
);

ALTER TABLE grustoragedb_notification_event
ADD INDEX `IDX_NOTIFICATION_EVENT_DEMAND_ID` (`demand_id` ASC, `demand_type_id` ASC) ;
;
ALTER TABLE grustoragedb_notification_event
ADD INDEX `IDX_NOTIFICATION_EVENT_DATE` (event_date ASC) ;
;