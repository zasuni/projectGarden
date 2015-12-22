
USE garden;

CREATE TABLE IF NOT EXISTS groups (
id INT NOT NULL AUTO_INCREMENT,
groupName VARCHAR(50) NOT NULL DEFAULT '',
state SMALLINT NOT NULL DEFAULT 0,
PRIMARY KEY(id),
UNIQUE(groupName)
);

CREATE TABLE IF NOT EXISTS espModules (
id INT NOT NULL AUTO_INCREMENT,
idGroup INT NOT NULL,
mac VARCHAR(17) NOT NULL DEFAULT '',
gpioUsed VARCHAR(16) NOT NULL DEFAULT '0000000000000000',
gpioType VARCHAR(16) NOT NULL DEFAULT '0000000000000000',
gpioState VARCHAR(16) NOT NULL DEFAULT '0000000000000000',
modulName VARCHAR(50) NOT NULL DEFAULT '',
CONSTRAINT FOREIGN KEY (idGroup) REFERENCES groups (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
PRIMARY KEY(id),
UNIQUE(mac)
);

CREATE TABLE IF NOT EXISTS espDevices (
id INT NOT NULL AUTO_INCREMENT,
idEspModul INT NOT NULL,
gpioNo SMALLINT NOT NULL DEFAULT 1,
gpioType SMALLINT NOT NULL DEFAULT 0,
deviceName VARCHAR(50) NOT NULL DEFAULT '',
gpioState SMALLINT NOT NULL DEFAULT 0,
CONSTRAINT FOREIGN KEY (idEspModul) REFERENCES espModules (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
PRIMARY KEY(id),
UNIQUE(idEspModul,gpioNo)
);

CREATE TABLE IF NOT EXISTS noNames (
id INT NOT NULL AUTO_INCREMENT,
mac VARCHAR(17) NOT NULL DEFAULT '',
gpioUsed VARCHAR(16) NOT NULL DEFAULT '0000000000000000',
gpioType VARCHAR(16) NOT NULL DEFAULT '0000000000000000',
PRIMARY KEY(id),
UNIQUE(mac)
);

/* 
** Procedury składowane:
**
*/

DELIMITER //

DROP PROCEDURE IF EXISTS insertGroup; //
CREATE DEFINER=`zanussi_ugarden`@`%` PROCEDURE insertGroup (
IN gName VARCHAR(50), 
INOUT id INT)
BEGIN
	
	DECLARE _rollback BOOL DEFAULT 0;
    DECLARE CONTINUE HANDLER FOR SQLEXCEPTION SET _rollback = 1;

    START TRANSACTION;

	INSERT INTO groups (groupName, state) VALUES (gName, 0);
    
    IF _rollback THEN
		ROLLBACK;
        SET id=-1;
	ELSE
		COMMIT;
        SET id=LAST_INSERT_ID();
	END IF;
END; //

DROP PROCEDURE IF EXISTS updateGroup; //
CREATE DEFINER=`zanussi_ugarden`@`%` PROCEDURE updateGroup (
IN ident INT,
IN gName VARCHAR(50), 
INOUT wynik INT)
BEGIN
	DECLARE _rollback BOOL DEFAULT 0;
    DECLARE CONTINUE HANDLER FOR SQLEXCEPTION SET _rollback = 1;
    SET wynik=0;
    
    START TRANSACTION;

	UPDATE groups SET groupName=gName WHERE id=ident;
    
    IF _rollback THEN
		ROLLBACK;
        SET wynik=-1;
	ELSE
		COMMIT;
        SET wynik=1;
	END IF;
END; //


DROP PROCEDURE IF EXISTS deleteRekord; //
CREATE DEFINER=`zanussi_ugarden`@`%` PROCEDURE deleteRekord (
IN id INT,
IN tableName VARCHAR(30),
INOUT wynik INT)
BEGIN
	DECLARE query VARCHAR(100);
	DECLARE _rollback BOOL DEFAULT 0;
    DECLARE CONTINUE HANDLER FOR SQLEXCEPTION SET _rollback = 1;
    
    START TRANSACTION;
	
    SET @query = CONCAT('DELETE FROM ',tableName,' WHERE id=',id,';');
    PREPARE stmt FROM @query;
	EXECUTE stmt;
    
    IF _rollback THEN
		ROLLBACK;
        SET wynik=0;
	ELSE
		COMMIT;
        SET wynik=1;
	END IF;
END; //

DROP PROCEDURE IF EXISTS insertNoName; //
CREATE DEFINER=`zanussi_ugarden`@`%` PROCEDURE insertNoName (
IN _mac VARCHAR(17), 
IN _gpioUsed VARCHAR(16), 
IN _gpioType VARCHAR(16), 
INOUT id INT)
BEGIN
	
	DECLARE _rollback BOOL DEFAULT 0;
    DECLARE CONTINUE HANDLER FOR SQLEXCEPTION SET _rollback = 1;

    START TRANSACTION;

	INSERT INTO noNames (mac, gpioUsed, gpioType) VALUES (_mac, _gpioUsed, _gpioType);
    
    IF _rollback THEN
		ROLLBACK;
        SET id=-1;
	ELSE
		COMMIT;
        SET id=LAST_INSERT_ID();
	END IF;
END; //


DROP PROCEDURE IF EXISTS insertEspModul; //
CREATE DEFINER=`zanussi_ugarden`@`%` PROCEDURE insertEspModul (
IN _idGroup INT,
IN _mac VARCHAR(17),
IN _gpioUsed VARCHAR(16),
IN _gpioType VARCHAR(16),
IN _modulName VARCHAR(50),
INOUT _id INT)
BEGIN
	DECLARE _rollback BOOL DEFAULT 0;
    DECLARE CONTINUE HANDLER FOR SQLEXCEPTION SET _rollback = 1;

    START TRANSACTION;

	INSERT INTO espModules (idGroup, mac, gpioUsed, gpioType, gpioState, modulName) VALUES (_idGroup, _mac, _gpioUsed, _gpioType, 0, _modulName);
    
    IF _rollback THEN
		ROLLBACK;
        SET _id=-1;
	ELSE
		COMMIT;
        SET _id=LAST_INSERT_ID();
	END IF;
END; //


DROP PROCEDURE IF EXISTS insertEspDevice; //
CREATE DEFINER=`zanussi_ugarden`@`%` PROCEDURE insertEspDevice (
IN _idEspModul INT,
IN _gpioNo INT,
IN _deviceName VARCHAR(50),
IN _gpioType INT,
INOUT _id INT)
BEGIN
	DECLARE _rollback BOOL DEFAULT 0;
    DECLARE CONTINUE HANDLER FOR SQLEXCEPTION SET _rollback = 1;

    START TRANSACTION;

	INSERT INTO espDevices (idEspModul, gpioNo, deviceName, gpioState, gpioType) VALUES (_idEspModul, _gpioNo, _deviceName, 0, _gpioType);
    
    IF _rollback THEN
		ROLLBACK;
        SET _id=-1;
	ELSE
		COMMIT;
        SET _id=LAST_INSERT_ID();
	END IF;
END; //


DROP PROCEDURE IF EXISTS przypiszModul; //
CREATE DEFINER=`zanussi_ugarden`@`%` PROCEDURE przypiszModul (
IN _idNoName INT,
IN _idGroup INT,
IN _modulName VARCHAR(50),
INOUT _idModule INT)
BEGIN
	DECLARE _mac VARCHAR(17);
    DECLARE _gpioUsed VARCHAR(16);
    DECLARE _gpioType VARCHAR(16);
    DECLARE _wynik INT;
    DECLARE _isUsed VARCHAR(1);
    DECLARE _pozycja INT;
    DECLARE _idDevice INT;
    
	DECLARE _rollback BOOL DEFAULT 0;
    DECLARE CONTINUE HANDLER FOR SQLEXCEPTION SET _rollback = 1;
    
    START TRANSACTION;

	SELECT mac, gpioUsed, gpioType
    INTO _mac, _gpioUsed, _gpioType
    FROM noNames WHERE id=_idNoName;
    
    INSERT INTO espModules (idGroup, mac, gpioUsed, gpioType, gpioState, modulName) VALUES (_idGroup, _mac, _gpioUsed, _gpioType, 0, _modulName);
    SET _idModule = LAST_INSERT_ID(); 
    SET _pozycja=1;
    
    petla: LOOP
		SET _isUsed = SUBSTRING(_gpioUsed,_pozycja,1);        
        IF _isUsed='1' THEN
				INSERT INTO espDevices (idEspModul, gpioNo, deviceName, gpioState, gpioType) 
                VALUES (_idModule, _pozycja, 'Urządzenie...', 0, SUBSTRING(_gpioType,_pozycja,1));
        END IF;
        
		SET _pozycja=_pozycja+1;
        
        IF _pozycja>=LENGTH(_gpioUsed) THEN
			LEAVE petla;
		END IF;
    END LOOP petla;
    
    DELETE FROM noNames WHERE id=_idNoName;
    
    IF _rollback THEN
		ROLLBACK;
        SET _idModule=-1;
	ELSE
		COMMIT;
	END IF;
END;  //


DROP PROCEDURE IF EXISTS updateDevice; //
CREATE DEFINER=`zanussi_ugarden`@`%` PROCEDURE updateDevice (
IN ident INT,
IN dName VARCHAR(50), 
INOUT wynik INT)
BEGIN
	DECLARE _rollback BOOL DEFAULT 0;
    DECLARE CONTINUE HANDLER FOR SQLEXCEPTION SET _rollback = 1;
    SET wynik=0;
    
    START TRANSACTION;

	UPDATE espDevices SET deviceName=dName WHERE id=ident;
    
    IF _rollback THEN
		ROLLBACK;
        SET wynik=-1;
	ELSE
		COMMIT;
        SET wynik=1;
	END IF;
END; //

DELIMITER ;
