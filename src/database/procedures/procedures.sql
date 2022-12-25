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
    IN name VARCHAR(125),
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

	INSERT IGNORE customer
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
    IN staff_username VARCHAR(10),
    OUT job_id INT
)
BEGIN
    SELECT staff_role_id INTO @role
    FROM staff
    WHERE username=staff_username;
    -- INTERNATIONAL
    IF @role=5 THEN
        SELECT job_id INTO @curr_job
        FROM job j
        WHERE j.staff_username=staff_username AND j.job_type_id=5;
    end if;
    -- DELIVERY
    IF @role=4 THEN
        SELECT job_id INTO @curr_job
        FROM job j
        WHERE j.staff_username=staff_username AND j.job_type_id=7;
    end if;

    IF @curr_job IS NOT NULL THEN
        SELECT LAST_INSERT_ID() INTO job_id;
    ELSE
        INSERT job
        VALUES(default,NOW(),NULL, job_type, job_status_id, staff_username);
        SELECT LAST_INSERT_ID() INTO job_id;
	end if;
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
-- Get parcel
-- returns parcel data for ID
-- --------------------------------------------------------------
DELIMITER !!
CREATE PROCEDURE get_parcel(
    IN ID VARCHAR(8)
)
BEGIN
    SELECT
        p.id as parcel_id,
        ps.name as parcel_status,
        p.sender as sender,
        p.recipient as recipient,
        p.weight as weight,
        p.height as height,
        p.width as width,
        p.depth as depth,
        p.sender_street_name as sender_street_name,
        p.sender_street_number as sender_street_num,
        p.sender_city_code as sender_city_code,
        p.sender_city_name as sender_city_name,
        p.sender_country_code as sender_country_code,
        p.recipient_street_name as recipient_street_name,
        p.recipient_street_number as recipient_street_num,
        p.recipient_city_code as recipient_city_code,
        p.recipient_city_name as recipient_city_name,
        p.recipient_country_code as recipient_country_code
    FROM parcel p
    INNER JOIN parcel_status ps on p.parcel_status_id = ps.id
    WHERE p.id=ID;
END !!
DELIMITER ;
-- --------------------------------------------------------------
-- links parcel to a job
-- --------------------------------------------------------------
DELIMITER !!
CREATE PROCEDURE link_parcel_job(
    IN parcel_id VARCHAR(8),
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
-- --------------------------------------------------------------
-- returns user's jobs
-- --------------------------------------------------------------
DELIMITER !!
CREATE PROCEDURE get_jobs(
    IN username VARCHAR(10)
)
BEGIN
    SELECT
        j.id as job_id,
        jt.name as job_type,
        js.name as job_status,
        p.id as parcel_id,
        p.weight as weight,
        p.height as height,
        p.width as width,
        p.depth as depth,
        j.date_created as date_created,
        j.date_completed as date_completed
    FROM job j
    INNER JOIN job_packet jp on j.id = jp.job_id
    INNER JOIN job_status js on j.job_status_id = js.id
    INNER JOIN job_type jt on j.job_type_id = jt.id
    INNER JOIN parcel p ON jp.parcel_id = p.id
    WHERE j.staff_username=username AND j.job_status_id=1
    UNION
    SELECT
        j.id as job_id,
        jt.name as job_type,
        js.name as job_status,
        p.id as parcel_id,
        p.weight as weight,
        p.height as height,
        p.width as width,
        p.depth as depth,
        j.date_created as date_created,
        j.date_completed as date_completed
    FROM job j
    INNER JOIN job_packet jp on j.id = jp.job_id
    INNER JOIN job_status js on j.job_status_id = js.id
    INNER JOIN job_type jt on j.job_type_id = jt.id
    INNER JOIN parcel p ON jp.parcel_id = p.id
    WHERE j.staff_username=username AND j.job_status_id IN (2,3)
    ORDER BY date_created DESC;
END !!
DELIMITER ;
-- --------------------------------------------------------------
-- returns user's info (name, surname, role)
-- --------------------------------------------------------------
DELIMITER !!
CREATE PROCEDURE user_info(
    IN username VARCHAR(10),
    OUT u_name VARCHAR(85),
    OUT u_surname VARCHAR(85),
    OUT u_role VARCHAR(65)
)
BEGIN
    SELECT s.name, s.surname, sr.role_name INTO u_name, u_surname, u_role
    FROM staff s
    INNER JOIN staff_role sr ON s.staff_role_id = sr.id
    WHERE s.username=username;
END !!
DELIMITER ;
-- --------------------------------------------------------------
-- update parcel_status (updates status (INT) to parcel with ID)
-- --------------------------------------------------------------
DELIMITER !!
CREATE PROCEDURE update_parcel_status(
    IN parcel_id VARCHAR(8),
    IN new_status INT
)
BEGIN
    UPDATE parcel
    SET parcel_status_id=new_status
    WHERE id=parcel_id;
END !!
DELIMITER ;
-- --------------------------------------------------------------
-- no_of_jobs
-- --------------------------------------------------------------
DELIMITER !!
CREATE PROCEDURE get_no_jobs(
    IN username VARCHAR(10),
    OUT no_of_jobs INT
)
BEGIN
    SELECT COUNT(j.id) INTO no_of_jobs
    FROM job j
    WHERE j.staff_username=username AND j.job_status_id=1;
END !!
DELIMITER ;
-- --------------------------------------------------------------
-- change job status
-- --------------------------------------------------------------
DELIMITER !!
CREATE PROCEDURE update_job_status(
    IN job_ID INT,
    IN new_status INT
)
BEGIN
    UPDATE job
    SET job_status_id=new_status
    WHERE id=job_ID;
END !!
DELIMITER ;
-- --------------------------------------------------------------
-- get_jobs_filter_type
-- returns users jobs with type
-- --------------------------------------------------------------
DELIMITER !!
CREATE PROCEDURE get_jobs_filter_type(
    IN username VARCHAR(10),
    IN type VARCHAR(45)
)
BEGIN
    SELECT j.id as job_id, p.id as parcel_id, p.weight as weight, p.height as height, p.width as width, p.depth as depth, j.date_created
    FROM job j
    INNER JOIN job_packet jp on j.id = jp.job_id
    INNER JOIN job_status js on j.job_status_id = js.id
    INNER JOIN parcel p ON jp.parcel_id = p.id
    INNER JOIN job_type jt on j.job_type_id = jt.id
    WHERE j.staff_username=username AND UPPER(jt.name) = UPPER(type);
END !!
DELIMITER ;
-- --------------------------------------------------------------
-- warehouse_parcel_info
-- --------------------------------------------------------------
DELIMITER !!
CREATE PROCEDURE get_warehouse_parcel_info(
    IN username VARCHAR(10),
    OUT no_of_all_parcels INT,
    OUT no_of_pending_parcels INT,
    OUT no_of_processed_parcels INT
)
BEGIN
    -- all parcels
    SELECT COUNT(DISTINCT jp.parcel_id) INTO no_of_all_parcels
    FROM job j
    INNER JOIN job_packet jp on j.id = jp.job_id
    WHERE j.staff_username IN (
        SELECT s.username
        FROM staff s
        INNER JOIN branch b on s.branch_id = b.id
        WHERE b.id IN(
            SELECT s.branch_id
            FROM staff
            WHERE s.username=username
        )
    );

    -- pending parcels
    SELECT COUNT(DISTINCT jp.parcel_id) INTO no_of_pending_parcels
    FROM job j
    INNER JOIN job_packet jp on j.id = jp.job_id
    WHERE j.job_status_id=1 AND j.staff_username IN (
        SELECT s.username
        FROM staff s
        INNER JOIN branch b on s.branch_id = b.id
        WHERE b.id IN(
            SELECT s.branch_id
            FROM staff
            WHERE s.username=username
        )
    );

    -- processed parcels
    SELECT COUNT(DISTINCT jp.parcel_id) INTO no_of_processed_parcels
    FROM job j
    INNER JOIN job_packet jp on j.id = jp.job_id
    WHERE j.job_status_id IN (2,3) AND j.staff_username IN (
        SELECT s.username
        FROM staff s
        INNER JOIN branch b on s.branch_id = b.id
        WHERE b.id IN(
            SELECT s.branch_id
            FROM staff
            WHERE s.username=username
        )
    );
END !!
DELIMITER ;
-- --------------------------------------------------------------
-- warehouse_employee_info
-- gets all employees of branch where user works
-- --------------------------------------------------------------
DELIMITER !!
CREATE PROCEDURE get_warehouse_employee_info(
    IN username VARCHAR(10)
)
BEGIN
    SELECT s.name as name, s.surname as surname, sr.role_name as role
    FROM staff s
    INNER JOIN staff_role sr on s.staff_role_id = sr.id
    INNER JOIN branch b on s.branch_id = b.id
    WHERE b.id IN(
        SELECT s.branch_id
        FROM staff
        WHERE s.username=username
    );
END !!
DELIMITER ;
-- --------------------------------------------------------------
-- warehouse_parcel_data
-- gets all parcel data of branch where user works
-- --------------------------------------------------------------
DELIMITER !!
CREATE PROCEDURE get_warehouse_parcel_data(
    IN username VARCHAR(10)
)
BEGIN
    SELECT
        p.id as parcel_id,
        ps.name as parcel_status,
        p.sender as sender,
        p.recipient as recipient,
        p.weight as weight,
        p.height as height,
        p.width as width,
        p.depth as depth,
        p.sender_street_name as sender_street_name,
        p.sender_street_number as sender_street_num,
        p.sender_city_code as sender_city_code,
        p.sender_city_name as sender_city_name,
        p.sender_country_code as sender_country_code,
        p.recipient_street_name as recipient_street_name,
        p.recipient_street_number as recipient_street_num,
        p.recipient_city_code as recipient_city_code,
        p.recipient_city_name as recipient_city_name,
        p.recipient_country_code as recipient_country_code
    FROM job j
    INNER JOIN job_packet jp on j.id = jp.job_id
    INNER JOIN parcel p on jp.parcel_id = p.id
    INNER JOIN parcel_status ps on p.parcel_status_id = ps.id
    WHERE j.job_status_id=1 AND j.staff_username IN (
        SELECT s.username
        FROM staff s
        INNER JOIN branch b on s.branch_id = b.id
        WHERE b.id IN(
            SELECT s.branch_id
            FROM staff
            WHERE s.username=username
        )
    );
END !!
DELIMITER ;
-- --------------------------------------------------------------
-- get_user_parcel_data
-- gets all parcel data of user (user's jobs)
-- --------------------------------------------------------------
DELIMITER !!
CREATE PROCEDURE get_user_parcel_data(
    IN username VARCHAR(10)
)
BEGIN
    SELECT
        p.id as parcel_id,
        ps.name as parcel_status,
        p.sender as sender,
        p.recipient as recipient,
        p.weight as weight,
        p.height as height,
        p.width as width,
        p.depth as depth,
        p.sender_street_name as sender_street_name,
        p.sender_street_number as sender_street_num,
        p.sender_city_code as sender_city_code,
        p.sender_city_name as sender_city_name,
        p.sender_country_code as sender_country_code,
        p.recipient_street_name as recipient_street_name,
        p.recipient_street_number as recipient_street_num,
        p.recipient_city_code as recipient_city_code,
        p.recipient_city_name as recipient_city_name,
        p.recipient_country_code as recipient_country_code
    FROM job j
    INNER JOIN job_packet jp on j.id = jp.job_id
    INNER JOIN parcel p on jp.parcel_id = p.id
    INNER JOIN parcel_status ps on p.parcel_status_id = ps.id
    WHERE j.job_status_id=1 AND j.staff_username=username;
END !!
DELIMITER ;
-- --------------------------------------------------------------
-- returns branch employees
-- gets all employees of branch with ID
-- --------------------------------------------------------------
DELIMITER !!
CREATE PROCEDURE branch_employees(
    IN branch_id INT
)
BEGIN
    SELECT s.name as name, s.surname as surname, sr.role_name as role
    FROM staff s
    INNER JOIN staff_role sr ON s.staff_role_id = sr.id
    WHERE s.branch_id=branch_id;
END !!
DELIMITER ;
-- --------------------------------------------------------------
-- returns branch delivery drivers
-- gets all employees of branch(branch_id) delivery drivers
-- --------------------------------------------------------------
DELIMITER !!
CREATE PROCEDURE branch_delivery_drivers(
    IN branch_id INT
)
BEGIN
    SELECT s.name as name, s.surname as surname, sr.role_name as role
    FROM staff s
    INNER JOIN staff_role sr ON s.staff_role_id = sr.id
    WHERE s.branch_id=branch_id AND sr.id=4;
END !!
DELIMITER ;
-- --------------------------------------------------------------
-- returns branch international drivers
-- gets all employees of branch(branch_id) international drivers
-- --------------------------------------------------------------
DELIMITER !!
CREATE PROCEDURE branch_international_drivers(
    IN branch_id INT
)
BEGIN
    SELECT s.name as name, s.surname as surname, sr.role_name as role
    FROM staff s
    INNER JOIN staff_role sr ON s.staff_role_id = sr.id
    WHERE s.branch_id=branch_id AND sr.id=5;
END !!
DELIMITER ;
-- --------------------------------------------------------------
-- returns branches
-- gets all branches
-- --------------------------------------------------------------
DELIMITER !!
CREATE PROCEDURE get_branches(
)
BEGIN
    SELECT b.id as bid, b.name as name, bt.name as type
    FROM branch b
    INNER JOIN branch_type bt on b.branch_type_id = bt.id
    WHERE bt.id=1;
END !!
DELIMITER ;
-- --------------------------------------------------------------
-- returns branch statistics
-- gets all branches
-- --------------------------------------------------------------
DELIMITER !!
CREATE PROCEDURE get_branch_stats(
    IN branch_id INT,
    OUT inbound_parcels INT,
    OUT outbound_parcels INT,
    OUT all_jobs_drivers INT,
    OUT no_of_drivers INT

)
BEGIN
    -- INBOUND PARCELS
    SELECT COUNT(jp.parcel_id) INTO inbound_parcels
    FROM job j
    INNER JOIN job_packet jp on j.id = jp.job_id
    WHERE j.job_status_id=1 AND j.job_type_id=3 AND j.staff_username IN (
        SELECT s.username
        FROM staff s
        INNER JOIN branch b on s.branch_id = b.id
        WHERE b.id = branch_id
    );

    -- OUTBOUND PARCELS
    SELECT COUNT(jp.parcel_id) INTO outbound_parcels
    FROM job j
    INNER JOIN job_packet jp on j.id = jp.job_id
    WHERE j.job_status_id=2 AND j.job_type_id=4 AND j.staff_username IN (
        SELECT s.username
        FROM staff s
        INNER JOIN branch b on s.branch_id = b.id
        WHERE b.id = branch_id
    );

    -- ALL JOBS FOR DRIVERS
    SELECT COUNT(j.id) INTO all_jobs_drivers
    FROM job j
    WHERE j.staff_username IN (
        SELECT s.username
        FROM staff s
        INNER JOIN staff_role sr ON s.staff_role_id = sr.id
        INNER JOIN branch b on s.branch_id = b.id
        WHERE sr.id=4 AND b.id=branch_id
    );

    -- NO OF DRIVERS
    SELECT COUNT(s.username) INTO no_of_drivers
    FROM staff s
    INNER JOIN staff_role sr ON s.staff_role_id = sr.id
    INNER JOIN branch b on s.branch_id = b.id
    WHERE sr.id=4 AND b.id=branch_id;
END !!
DELIMITER ;
-- --------------------------------------------------------------
-- parcel lookup
-- --------------------------------------------------------------
DELIMITER !!
CREATE PROCEDURE parcel_lookup(
    parcel_id VARCHAR(8)
)
BEGIN
    select j.id as job_id
    from job j
    INNER JOIN job_packet jp ON j.id = jp.job_id
    WHERE jp.parcel_id=parcel_id AND j.job_status_id=1;
END !!
DELIMITER ;
-- --------------------------------------------------------------
-- --------------------------------------------------------------
DELIMITER !!
CREATE PROCEDURE get_warehouse_employee_info(
    IN username VARCHAR(10)
)
BEGIN
    SELECT s.name as name, s.surname as surname, sr.role_name as role
    FROM staff s
    INNER JOIN staff_role sr on s.staff_role_id = sr.id
    INNER JOIN branch b on s.branch_id = b.id
    WHERE b.id IN(
        SELECT s.branch_id
        FROM staff
        WHERE s.username=username
    );
END !!
DELIMITER ;
-- --------------------------------------------------------------
-- branch_lookup
-- --------------------------------------------------------------
DELIMITER !!
CREATE PROCEDURE branch_lookup(
   IN username INT
)
BEGIN
    SELECT branch_id as branch_id, c.latitude as latitude, c.longitude as longitude
    FROM branch b
    INNER JOIN staff s ON b.id = s.branch_id
    INNER JOIN city c ON c.code=b.city_code AND c.name=b.city_name AND c.country_code=b.country_code
    WHERE s.username=username;
END !!
DELIMITER ;
-- --------------------------------------------------------------
-- branch_drivers_lookup
-- --------------------------------------------------------------
DELIMITER !!
CREATE PROCEDURE branch_employee_lookup(
   IN branch_id INT,
   IN staff_role INT
)
BEGIN
    SELECT s.username
    FROM branch b
    INNER JOIN staff s ON b.id = s.branch_id
    WHERE b.id=branch_id AND s.staff_role_id=staff_role;
END !!
DELIMITER ;
-- --------------------------------------------------------------
-- Get branch address from branch ID.
-- --------------------------------------------------------------
DELIMITER !!
CREATE PROCEDURE get_branch_address(
   IN branch_id INT
)
BEGIN
    SELECT b.city_code,b.city_name,b.country_code
    FROM branch b
    where id=branch_id;
END !!
DELIMITER ;
-- --------------------------------------------------------------
-- Get branch address from branch ID.
-- --------------------------------------------------------------
DELIMITER !!
CREATE PROCEDURE get_branch_office(
   IN country VARCHAR(3),
   OUT branch_id INT
)
BEGIN
    SELECT id INTO branch_id
    FROM branch b
    where b.branch_type_id=3 AND b.country_code=country;
END !!
DELIMITER ;
-- --------------------------------------------------------------
-- Get branch address from branch ID.
-- --------------------------------------------------------------
DELIMITER !!
CREATE PROCEDURE get_branch_office(
   IN country VARCHAR(3),
   OUT branch_id INT
)
BEGIN
    SELECT id INTO branch_id
    FROM branch b
    where b.branch_type_id=3 AND b.country_code=country;
END !!
DELIMITER ;
-- --------------------------------------------------------------
-- Get branch address from branch ID.
-- --------------------------------------------------------------
DELIMITER !!
CREATE PROCEDURE get_job_type(
   IN jID INT,
   OUT type_id INT
)
BEGIN
    SELECT job_type_id INTO type_id
    FROM job
    where id=jID;
END !!
DELIMITER ;
