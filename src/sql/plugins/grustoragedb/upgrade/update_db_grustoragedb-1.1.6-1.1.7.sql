
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

ALTER TABLE grustoragedb_notification_event
ADD INDEX `IDX_NOTIFICATION_EVENT_DEMAND_ID` (`demand_id` ASC, `demand_type_id` ASC) ;

ALTER TABLE grustoragedb_notification_event
ADD INDEX `IDX_NOTIFICATION_EVENT_DATE` (event_date ASC, `demand_type_id` ASC) ;


ALTER TABLE grustoragedb_notification
ADD INDEX `idx_grustoragedb_notification_date` (`date` ASC, `demand_type_id` ASC) ;


ALTER TABLE grustoragedb_notification ADD INDEX index_notif_typdemands (demand_type_id ASC, demand_id ASC) ;


DELETE FROM core_admin_right WHERE id_right = 'DEMAND_MANAGEMENT';
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url,documentation_url, id_order ) VALUES
('DEMAND_MANAGEMENT','grustoragedb.adminFeature.ManageDemand.name',1,'jsp/admin/plugins/grustoragedb/ManageDemand.jsp','grustoragedb.adminFeature.ManageDemand.description',0,'grustoragedb',NULL,NULL,NULL,4);


--
-- Data for table core_user_right
--
DELETE FROM core_user_right WHERE id_right = 'DEMAND_MANAGEMENT';
INSERT INTO core_user_right (id_right,id_user) VALUES ('DEMAND_MANAGEMENT',1);
