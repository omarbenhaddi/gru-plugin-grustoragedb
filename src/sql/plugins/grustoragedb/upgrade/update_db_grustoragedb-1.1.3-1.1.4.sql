--
-- ATTENTION ce script update ne fait pas la migration de données
-- Structure for table grustoragedb_notification
--
ALTER TABLE grustoragedb_notification ADD COLUMN has_backoffice int NOT NULL default 0;
ALTER TABLE grustoragedb_notification ADD COLUMN has_sms int NOT NULL default 0;
ALTER TABLE grustoragedb_notification ADD COLUMN has_customer_email int NOT NULL default 0;
ALTER TABLE grustoragedb_notification ADD COLUMN has_mydashboard int NOT NULL default 0;
ALTER TABLE grustoragedb_notification ADD COLUMN has_broadcast_email int NOT NULL default 0; 
ALTER TABLE grustoragedb_notification ADD COLUMN notification_content LONG VARBINARY;

--
-- Suppression des tables notifications inutiles
-- 
ALTER TABLE grustoragedb_notification_backoffice DROP FOREIGN KEY fk_grustoragedb_notification_backoffice_notification_id;
ALTER TABLE grustoragedb_notification_sms DROP FOREIGN KEY fk_grustoragedb_notification_sms_notification_id;
ALTER TABLE grustoragedb_notification_customer_email DROP FOREIGN KEY fk_grustoragedb_notification_customer_email_notification_id;
ALTER TABLE grustoragedb_notification_mydashboard DROP FOREIGN KEY fk_grustoragedb_notification_mydashboard_notification_id;
ALTER TABLE grustoragedb_notification_broadcast_email DROP FOREIGN KEY fk_grustoragedb_notification_broadcast_email_notification_id;

DROP TABLE IF EXISTS grustoragedb_notification_backoffice;
DROP TABLE IF EXISTS grustoragedb_notification_sms;
DROP TABLE IF EXISTS grustoragedb_notification_customer_email;
DROP TABLE IF EXISTS grustoragedb_notification_mydashboard;
DROP TABLE IF EXISTS grustoragedb_notification_broadcast_email;
