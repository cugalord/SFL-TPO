DELIMITER !!
CREATE TRIGGER job_parcel_status_2
AFTER INSERT
ON job FOR EACH ROW
BEGIN
    -- at the final parcel center
    IF new.job_type_id=7 AND new.job_status_id=1 THEN
        UPDATE parcel
        SET parcel_status_id=3
        WHERE id IN(
            SELECT parcel_id
            FROM job_packet
            WHERE job_id=new.id
        );
    END IF;
    -- in delivery
    IF new.job_type_id=8 AND new.job_status_id=1 THEN
        UPDATE parcel
        SET parcel_status_id=4
        WHERE id IN(
            SELECT parcel_id
            FROM job_packet
            WHERE job_id=new.id
        );
    END IF;
END!!
DELIMITER ;
