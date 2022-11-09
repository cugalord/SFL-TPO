-- --------------------------------------------------------------
-- Inserts a new address
-- --------------------------------------------------------------
DELIMITER !!
CREATE PROCEDURE street_add(
    IN st_name VARCHAR(150),
    IN st_num INT,
    IN city_code VARCHAR(20),
    IN city_name VARCHAR(145),
    IN country_code VARCHAR(3)
)
BEGIN
	INSERT IGNORE street
	VALUES(st_name,st_num,city_code,city_name,country_code);
END !!
DELIMITER ;
-- --------------------------------------------------------------
-- Inserts a branch
-- --------------------------------------------------------------
DELIMITER !!
CREATE PROCEDURE branch_add(
    IN branch_type_id INT,
    IN name VARCHAR(45),
    IN st_name VARCHAR(150),
    IN st_num INT,
    IN city_code VARCHAR(20),
    IN city_name VARCHAR(145),
    IN country_code VARCHAR(3)
)
BEGIN
    CALL street_add(st_name,st_num,city_code,city_name,country_code);

	INSERT branch
	VALUES(default,branch_type_id,name,st_name,st_num,city_code,city_name,country_code);
END !!
DELIMITER ;
-- --------------------------------------------------------------
-- Inserts a new staff member
-- USERNAME IS NOT GENERATED IN THE DATABASE
-- --------------------------------------------------------------
DELIMITER !!
CREATE PROCEDURE staff_add(
    IN username VARCHAR(10),
    IN name VARCHAR(85),
    IN surname VARCHAR(85),
    IN branch_id INT,
    IN staff_role_id INT
)
BEGIN
	INSERT staff
	VALUES(username,name, surname, branch_id, staff_role_id);
END !!
DELIMITER ;
-- --------------------------------------------------------------
-- Inserts a new customer
-- --------------------------------------------------------------
DELIMITER !!
CREATE PROCEDURE customer_add(
    IN username VARCHAR(10),
    IN name VARCHAR(85),
    IN surname VARCHAR(85),
    IN company_name VARCHAR(85),
    IN tel_num VARCHAR(45),
    IN st_name VARCHAR(150),
    IN st_num INT,
    IN city_code VARCHAR(20),
    in city_name VARCHAR(145),
    IN country_code VARCHAR(3)
)
BEGIN
    CALL street_add(st_name,st_num,city_code,city_name,country_code);

	INSERT customer
	VALUES(username,name,surname,company_name,tel_num,st_name,st_num,city_code,city_name,country_code);
END !!
DELIMITER ;
-- --------------------------------------------------------------
-- Creates a new job
-- date_created column generated using NOW() function
-- --------------------------------------------------------------
DELIMITER !!
CREATE PROCEDURE job_create(
    IN job_type INT,
    IN job_status_id INT,
    IN staff_username VARCHAR(10)
)
BEGIN
	INSERT job
	VALUES(default,NOW(),NULL, job_type_id, job_status_id, staff_username);
END !!
DELIMITER ;
-- --------------------------------------------------------------
-- Creates a new parcel
-- parcel_status_id == 1 == In IT system
-- --------------------------------------------------------------
DELIMITER !!
CREATE PROCEDURE parcel_create(
    IN ID VARCHAR(8),
    IN sender VARCHAR(10),
    IN sender_st_name VARCHAR(150),
    IN sender_st_num INT,
    IN sender_city_code VARCHAR(20),
    IN sender_city_name VARCHAR(145),
    IN sender_country_code VARCHAR(3),
    IN recipient VARCHAR(10),
    IN recipient_street_name VARCHAR(150),
    IN recipient_street_num INT,
    IN recipient_city_code VARCHAR(20),
    IN recipient_city_name VARCHAR(145),
    IN recipient_country_code VARCHAR(3),
    IN weight DOUBLE,
    IN height INT,
    IN width INT,
    IN depth INT
)
BEGIN
    CALL street_add(sender_st_name,sender_st_num,sender_city_code,sender_city_name,sender_country_code);
    CALL street_add(recipient_street_name,recipient_street_num,recipient_city_code,recipient_city_name,recipient_country_code);
    INSERT parcel
	VALUES(
        ID,
        1,
        sender,
        sender_st_name,
	    sender_st_num,
	    sender_city_code,
	    sender_city_name,
        sender_country_code,
        recipient,
        recipient_street_name,
        recipient_street_num,
        recipient_city_code,
        recipient_city_name,
        recipient_country_code,
        weight,
        height,
        width,
        depth
	);
END !!
DELIMITER ;
-- --------------------------------------------------------------
-- links parcel to a job
-- --------------------------------------------------------------
DELIMITER !!
CREATE PROCEDURE link_parcel_job(
    IN parcel_id INT,
    IN job_id INT
)
BEGIN
	INSERT job_packet
	VALUES(job_id,parcel_id);
END !!
DELIMITER ;
-- --------------------------------------------------------------
-- returns coordinates for given address
-- --------------------------------------------------------------
DELIMITER !!
CREATE PROCEDURE resolve_address(
    IN postcode VARCHAR(20),
    IN cityname VARCHAR(145),
    IN country VARCHAR(3),
    OUT latitude VARCHAR(7),
    OUT longitude VARCHAR(7)
)
BEGIN
    SELECT ci.latitude, ci.longitude INTO latitude, longitude
    FROM city ci
    WHERE ci.code=postcode AND ci.name=cityname AND ci.country_code=country;
END !!
DELIMITER ;