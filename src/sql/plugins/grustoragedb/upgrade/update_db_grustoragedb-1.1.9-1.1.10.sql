
--
-- Structure for table grustoragedb_status
--

DROP TABLE IF EXISTS grustoragedb_status;
CREATE TABLE grustoragedb_status (
id_status int AUTO_INCREMENT,
status long varchar NOT NULL,
status_code varchar(255) default '' NOT NULL,
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

CREATE INDEX index_notification_id_type ON grustoragedb_notification_content (notification_id,notification_type);

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
-- Data for table core_admin_right
--
DELETE FROM core_admin_right WHERE id_right = 'GRUSTORAGEDB_MANAGEMENT';
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url,documentation_url, id_order ) VALUES 
('GRUSTORAGEDB_MANAGEMENT','grustoragedb.adminFeature.Manage.name',1,'jsp/admin/plugins/grustoragedb/ManageStatus.jsp','grustoragedb.adminFeature.Manage.description',0,'grustoragedb',NULL,NULL,NULL,4);


--
-- Data for table core_user_right
--
DELETE FROM core_user_right WHERE id_right = 'GRUSTORAGEDB_MANAGEMENT';
INSERT INTO core_user_right (id_right,id_user) VALUES ('GRUSTORAGEDB_MANAGEMENT',1);

--
-- Data for table core_admin_right
--
DELETE FROM core_admin_right WHERE id_right = 'GRUSTORAGEDB_DEMANDTYPE_MANAGEMENT';
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url,documentation_url, id_order ) VALUES 
('GRUSTORAGEDB_DEMANDTYPE_MANAGEMENT','grustoragedb.adminFeature.ManageDemandType.name',1,'jsp/admin/plugins/grustoragedb/ManageDemandTypes.jsp','grustoragedb.adminFeature.ManageDemandType.description',0,'grustoragedb',NULL,NULL,NULL,4);

--
-- Data for table core_user_right
--
DELETE FROM core_user_right WHERE id_right = 'GRUSTORAGEDB_DEMANDTYPE_MANAGEMENT';
INSERT INTO core_user_right (id_right,id_user) VALUES ('GRUSTORAGEDB_DEMANDTYPE_MANAGEMENT',1);

-- 
-- Alter grustoragedb_demand
--

ALTER TABLE grustoragedb_demand
ADD COLUMN modify_date bigint NULL,;

update grustoragedb_demand  set modify_date = creation_date;

CREATE INDEX index_demand_id_type_customer ON grustoragedb_demand (demand_id,type_id,customer_id);


-- 
-- Alter table grustoragedb_notification
--

ALTER TABLE grustoragedb_notification
DROP COLUMN has_sms;

ALTER TABLE grustoragedb_notification
DROP COLUMN has_backoffice;

ALTER TABLE grustoragedb_notification
DROP COLUMN has_customer_email;

ALTER TABLE grustoragedb_notification
DROP COLUMN has_mydashboard;

ALTER TABLE grustoragedb_notification
DROP COLUMN has_broadcast_email;

ALTER TABLE grustoragedb_notification DROP FOREIGN KEY fk_grustoragedb_notification_demand_id;

ALTER TABLE grustoragedb_notification ADD CONSTRAINT fk_grustoragedb_notification_demand_id FOREIGN KEY (demand_id, demand_type_id)
      REFERENCES grustoragedb_demand (demand_id, type_id) ON DELETE CASCADE ON UPDATE RESTRICT;