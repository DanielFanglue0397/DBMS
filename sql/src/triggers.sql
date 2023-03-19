-- After updating the room, record it to RoomUpdatesLog
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

-- Return manager ID from the Hotel table
CREATE OR REPLACE FUNCTION get_managerID_update_log()
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

-- After plaicing the request, record it to RoomRepairRequests
CREATE OR REPLACE FUNCTION insert_room_repair_request()
RETURNS "trigger" AS
$BODY$
BEGIN
    INSERT INTO
    RoomRepairRequests (repairID)
    VALUES (NEW.repairID);
    RETURN NEW;
END;
$BODY$
LANGUAGE plpgsql VOLATILE;

-- Return manager ID from the Hotel table
CREATE OR REPLACE FUNCTION get_managerID_repair_request()
RETURNS "trigger" AS
$BODY$
BEGIN
    SELECT h.managerUserID
    INTO NEW.managerID
    FROM Hotel h, RoomRepairs r
    WHERE NEW.repairID = r.repairID AND r.hotelID = h.hotelID;
    RETURN NEW;
END;
$BODY$
LANGUAGE plpgsql VOLATILE;

-- When manager updates a room also update the RoomUpdatesLog table
DROP TRIGGER IF EXISTS roomUpdateLog ON Rooms;
CREATE TRIGGER roomUpdateLog
AFTER UPDATE
ON Rooms
FOR EACH ROW
EXECUTE PROCEDURE insert_room_update_log();

-- Before insert to RoomUpdatesLog, get the manager ID
DROP TRIGGER IF EXISTS getManagerIDForUpdateLog ON RoomUpdatesLog;
CREATE TRIGGER getManagerIDForUpdateLog
BEFORE INSERT
ON RoomUpdatesLog
FOR EACH ROW
EXECUTE PROCEDURE get_managerID_update_log();

-- When manager places a repair request also update the RoomRepairRequests table
DROP TRIGGER IF EXISTS repairRequest ON RoomRepairs;
CREATE TRIGGER repairRequest
AFTER INSERT
ON RoomRepairs
FOR EACH ROW
EXECUTE PROCEDURE insert_room_repair_request();

-- Before insert to RoomRepairRequests, get the manager ID
DROP TRIGGER IF EXISTS getManagerIDForRepairRequest ON RoomRepairRequests;
CREATE TRIGGER getManagerIDForRepairRequest
BEFORE INSERT
ON RoomRepairRequests
FOR EACH ROW
EXECUTE PROCEDURE get_managerID_repair_request();
