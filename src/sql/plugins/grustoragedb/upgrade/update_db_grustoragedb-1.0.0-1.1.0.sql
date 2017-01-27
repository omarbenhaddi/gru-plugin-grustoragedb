DROP TABLE IF EXISTS grustoragedb_customer;

--
-- Structure for table grustoragedb_demand
--
DROP TABLE IF EXISTS grustoragedb_demand;
CREATE TABLE grustoragedb_demand (
id varchar(50) NOT NULL,
type_id varchar(50) NOT NULL,
reference varchar(50) NOT NULL,
status_id int NOT NULL default 0,
customer_id varchar(50) NULL,
creation_date bigint NOT NULL,
closure_date bigint NULL,
max_steps int NULL,
current_step int NULL,
PRIMARY KEY (id, type_id)
);

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

--
-- Structure for table grustoragedb_notification_backoffice
--
DROP TABLE IF EXISTS grustoragedb_notification_backoffice;
CREATE TABLE grustoragedb_notification_backoffice (
notification_id int NOT NULL, 
message text COLLATE utf8_unicode_ci NOT NULL, 
status_text varchar(255) NOT NULL,
PRIMARY KEY (notification_id)
);

--
-- Structure for table grustoragedb_notification_sms
--
DROP TABLE IF EXISTS grustoragedb_notification_sms;
CREATE TABLE grustoragedb_notification_sms (
notification_id int NOT NULL, 
message text COLLATE utf8_unicode_ci NOT NULL, 
phone_number varchar(15) NOT NULL,
PRIMARY KEY (notification_id)
);

--
-- Structure for table grustoragedb_notification_customer_email
--
DROP TABLE IF EXISTS grustoragedb_notification_customer_email;
CREATE TABLE grustoragedb_notification_customer_email (
notification_id int NOT NULL, 
sender_email varchar(255) NULL, 
sender_name varchar(255) NULL, 
subject varchar(255) NULL, 
message text COLLATE utf8_unicode_ci NOT NULL, 
recipients varchar(255) NOT NULL, 
copies varchar(255) NULL, 
blind_copies varchar(255) NULL,
PRIMARY KEY (notification_id)
);

--
-- Structure for table grustoragedb_notification_mydashboard
--
DROP TABLE IF EXISTS grustoragedb_notification_mydashboard;
CREATE TABLE grustoragedb_notification_mydashboard (
notification_id int NOT NULL, 
status_id int NOT NULL, 
status_text varchar(255) NOT NULL, 
message text COLLATE utf8_unicode_ci NOT NULL, 
subject varchar(255) NULL, 
data text COLLATE utf8_unicode_ci NULL, 
sender_name varchar(255) NULL,
PRIMARY KEY (notification_id)
);

--
-- Structure for table grustoragedb_notification_broadcast_email
--
DROP TABLE IF EXISTS grustoragedb_notification_broadcast_email;
CREATE TABLE grustoragedb_notification_broadcast_email (
notification_id int NOT NULL, 
sender_email varchar(255) NULL, 
sender_name varchar(255) NULL, 
subject varchar(255) NULL, 
message text COLLATE utf8_unicode_ci NOT NULL, 
recipients varchar(255) NOT NULL, 
copies varchar(255) NULL, 
blind_copies varchar(255) NULL
);

ALTER TABLE grustoragedb_notification ADD CONSTRAINT fk_grustoragedb_notification_demand_id FOREIGN KEY (demand_id, demand_type_id)
      REFERENCES grustoragedb_demand (id, type_id) ON DELETE CASCADE ON UPDATE RESTRICT;
ALTER TABLE grustoragedb_notification_backoffice ADD CONSTRAINT fk_grustoragedb_notification_backoffice_notification_id FOREIGN KEY (notification_id)
      REFERENCES grustoragedb_notification (id) ON DELETE CASCADE ON UPDATE RESTRICT;
ALTER TABLE grustoragedb_notification_sms ADD CONSTRAINT fk_grustoragedb_notification_sms_notification_id FOREIGN KEY (notification_id)
      REFERENCES grustoragedb_notification (id) ON DELETE CASCADE ON UPDATE RESTRICT;
ALTER TABLE grustoragedb_notification_customer_email ADD CONSTRAINT fk_grustoragedb_notification_customer_email_notification_id FOREIGN KEY (notification_id)
      REFERENCES grustoragedb_notification (id) ON DELETE CASCADE ON UPDATE RESTRICT;
ALTER TABLE grustoragedb_notification_mydashboard ADD CONSTRAINT fk_grustoragedb_notification_mydashboard_notification_id FOREIGN KEY (notification_id)
      REFERENCES grustoragedb_notification (id) ON DELETE CASCADE ON UPDATE RESTRICT;
ALTER TABLE grustoragedb_notification_broadcast_email ADD CONSTRAINT fk_grustoragedb_notification_broadcast_email_notification_id FOREIGN KEY (notification_id)
      REFERENCES grustoragedb_notification (id) ON DELETE CASCADE ON UPDATE RESTRICT;
