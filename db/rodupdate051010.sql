ALTER TABLE `rod`.`t_delivery` DROP COLUMN `PK_DELIVERY_ID`,
 DROP PRIMARY KEY,
 ADD INDEX `fk_ra_id`(`FK_RA_ID`),
 ADD INDEX `fk_spatial_id`(`FK_SPATIAL_ID`);