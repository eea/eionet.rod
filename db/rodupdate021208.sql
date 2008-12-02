INSERT INTO HLP_SCREEN VALUES ('documentation', 'Abstract screen to serve as the root of ROD documentation areas');
DELETE FROM HLP_SCREEN WHERE SCREEN_ID = 'Disclaimer_page' OR SCREEN_ID = 'Help_page' OR SCREEN_ID = 'RSSHelp_page';

UPDATE HLP_AREA SET SCREEN_ID = 'documentation' WHERE SCREEN_ID = 'Disclaimer_page' OR SCREEN_ID = 'Help_page' OR SCREEN_ID = 'RSSHelp_page';