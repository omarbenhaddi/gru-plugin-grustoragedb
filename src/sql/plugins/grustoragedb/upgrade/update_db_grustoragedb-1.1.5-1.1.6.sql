ALTER TABLE grustoragedb_demand MODIFY customer_id VARCHAR(255) NULL;

ALTER TABLE grustoragedb_demand
DROP PRIMARY KEY,
ADD COLUMN demand_id INT NOT NULL AUTO_INCREMENT FIRST,
ADD PRIMARY KEY ( demand_id );
ALTER TABLE grustoragedb_demand ADD UNIQUE db_demand_index ( id, type_id );