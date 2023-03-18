DROP SEQUENCE IF EXISTS bookingID_seq;
CREATE OR REPLACE LANGUAGE plpgsql;
CREATE OR REPLACE FUNCTION return_bookingID()
RETURN "trigger" AS
$BODY$
BEGIN
    NEW.bookingID = nextval('bookingID_seq');
    RETURN NEW;
END;
$BODY$
LANGUAGE plpgsql VOLATILE;

DROP TRIGGER IF EXISTS bookingID ON RoomBookings;
CREATE TRIGGER bookingID
AFTER INSERT
ON RoomBookings
FOR EACH ROW
EXECUTE PROCEDURE return_bookingID();