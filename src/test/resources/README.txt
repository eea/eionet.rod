The rod.ddl is a mysqldump -d on the ROD database, but with all
comments removed until we can make a more intelligent importer.

To avoid having to reorder table create we SET FOREIGN_KEY_CHECKS=0;
and SET FOREIGN_KEY_CHECKS=1; around the CREATE TABLE statements.
