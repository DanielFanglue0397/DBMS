CREATE OR REPLACE LANGUAGE plpgsql;
CREATE OR REPLACE FUNCTION insert_room_update_log()
RETURNS "trigger" AS
$BODY$
BEGIN
    INSERT INTO
    RoomUpdatesLog (hotelID, roomNumber, updatedOn)
    VALUES (NEW.hotelID, NEW.roomNumber, now()::timestamp(0));
    RETURN NEW;
END;
$BODY$
LANGUAGE plpgsql VOLATILE;

CREATE OR REPLACE LANGUAGE plpgsql;
CREATE OR REPLACE FUNCTION complete_update_log()
RETURNS "trigger" AS
$BODY$
BEGIN
    SELECT h.managerUserID
    INTO NEW.managerID
    FROM Hotel h
    WHERE h.hotelID = NEW.hotelID;
    RETURN NEW;
END;
$BODY$
LANGUAGE plpgsql VOLATILE;

DROP TRIGGER IF EXISTS roomUpdateLog ON Rooms;
CREATE TRIGGER roomUpdateLog
AFTER UPDATE
ON Rooms
FOR EACH ROW
EXECUTE PROCEDURE insert_room_update_log();

DROP TRIGGER IF EXISTS completeUpdateLog ON RoomUpdatesLog;
CREATE TRIGGER completeUpdateLog
BEFORE INSERT
ON RoomUpdatesLog
FOR EACH ROW
EXECUTE PROCEDURE complete_update_log();

