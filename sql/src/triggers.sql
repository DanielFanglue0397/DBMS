-- DROP SEQUENCE IF EXISTS bookingID_seq;
-- CREATE SEQUENCE bookingID_seq START WITH 1000;

-- CREATE OR REPLACE LANGUAGE plpgsql;
-- CREATE OT REPLACE FUNCTION return_bookingID()
-- RETURN "trigger" AS
-- $BODY$
-- BEGIN
--     NEW.bookingID = nextval('bookingID_seq');
--     RETURN NEW;
-- END;
-- $BODY$
-- LANGUAGE plpgsql VOLATILE;

-- DROP TRIGGER IF EXISTS bookingID ON RoomBookings;
-- CREATE TRIGGER bookingID
-- BEFORE INSERT
-- ON RoomBookings
-- FOR EACH ROW
-- EXECUTE PROCEDURE return_bookingID();