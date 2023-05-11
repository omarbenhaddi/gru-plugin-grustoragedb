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