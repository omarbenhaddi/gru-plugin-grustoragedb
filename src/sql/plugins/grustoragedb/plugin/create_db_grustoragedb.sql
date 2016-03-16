
--
-- Structure for table grustoragedb_customer
--

DROP TABLE IF EXISTS grustoragedb_customer;
CREATE TABLE grustoragedb_customer (
id_customer int(6) NOT NULL,
customer_id varchar(50) NOT NULL default '',
customer_email varchar(255) NULL default '',
PRIMARY KEY (id_customer)
);

--
-- Structure for table grustoragedb_demand
--

DROP TABLE IF EXISTS grustoragedb_demand;
CREATE TABLE grustoragedb_demand (
id_demand int(6) NOT NULL,
customer_id varchar(50) NOT NULL default '',
demand_id varchar(50) NOT NULL default '',
demand_type_id varchar(50) NOT NULL default '',
demand_reference varchar(50) NOT NULL default '',
demand_status int(11) NOT NULL default '0',
max_steps int(11) NOT NULL default '0',
current_step int(11) NOT NULL default '0',
status_customer varchar(255) NULL default '',
status_gru varchar(255) NULL default '',
first_notification_date bigint(20) NOT NULL default '0',
last_notification_date bigint(20) NOT NULL default '0',
PRIMARY KEY (id_demand)
);

--
-- Structure for table grustoragedb_notification
--

DROP TABLE IF EXISTS grustoragedb_notification;
CREATE TABLE grustoragedb_notification (
id_notification int(6) NOT NULL,
id_demand int(11) NOT NULL default '0',
json long varchar NULL ,
PRIMARY KEY (id_notification)
);
