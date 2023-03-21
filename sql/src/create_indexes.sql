-- In the log in process it looks thorugh the Users table to retrieve user info
CREATE INDEX user_id_index
ON Users USING BTREE
(userID);

-- Used in bookRooms, updateRoomInfo, viewRegularCustomers, placeRoomRepairRequests
CREATE INDEX hotel_id_index
ON Hotel USING BTREE
(hotelID);

-- Used in viewRooms, bookRooms, updateRoomInfo, placeRoomRepairRequests
CREATE INDEX rooms_index
ON Rooms USING BTREE
(hotelID, roomNumber);

-- Used in viewRooms, bookRooms, viewBookingHistoryofHotel
CREATE INDEX room_bookings_index
ON RoomBookings USING BTREE
(bookingDate);

-- Used in viewRecentUpdates
CREATE INDEX room_updates_log_index
ON RoomUpdatesLog USING BTREE
(managerID);

-- Used in placeRoomRepairRequests
CREATE INDEX maintenance_company_index
ON MaintenanceCompany USING BTREE
(companyID);

-- Used in placeRoomRepairRequests
CREATE INDEX room_repairs_index
ON RoomRepairs USING BTREE
(repairID);