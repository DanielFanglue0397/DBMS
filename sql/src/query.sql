-- viewHotels
SELECT *
FROM (
	SELECT hotelID, hotelName, dateEstablished, calculate_distance(90, 24, latitude, longitude) AS distance
	FROM hotel) AS d
WHERE d.distance < 30;

-- viewRooms
SELECT r.hotelID, r.roomNumber, r.price, r.imageURL
FROM rooms r
WHERE NOT EXISTS (SELECT * FROM roombookings b
					WHERE r.hotelID = hotelID AND r.roomNumber = b.roomNumber AND b.bookingDate = '05/12/2015')
	AND r.hotelID = 1;

-- bookRooms
-- INSERT INTO RoomBookings(customerID, hotelID, roomNumber, bookingDate) VALUES (1, 2, 5, '03/15/2023') RETURNING bookingID;
SELECT b.bookingID, b.customerID, b.hotelID, b.roomNumber, b.bookingDate, r.price
FROM RoomBookings b, Rooms r
WHERE b.bookingID = 500 AND r.hotelID = b.hotelID AND r.roomNumber = b.roomNumber;

-- viewRecentBookingsfromCustomer
SELECT b.bookingID, b.hotelID, b.roomNumber, b.bookingDate, r.price
FROM RoomBookings b, Rooms r
WHERE b.customerID = 101 AND b.bookingDate < CURRENT_DATE AND r.hotelID = b.hotelID AND r.roomNumber = b.roomNumber
ORDER BY b.bookingDate DESC
LIMIT 5;

-- print Users table
SELECT * FROM Users WHERE userID = 101;

-- viewBookingHistoryofHotel
SELECT b.bookingID, u.name, b.hotelID, b.roomNumber, b.bookingDate, r.price
FROM RoomBookings b, Hotel h, Rooms r, Users u
WHERE h.managerUserID = 25 AND h.hotelID = b.hotelID AND b.hotelID = r.hotelID AND b.customerID = u.userID AND b.roomNumber = r.roomNumber AND b.bookingDate BETWEEN '05/01/2018' AND '03/21/2023'
ORDER By b.bookingDate DESC;

-- viewRegularCustomers
SELECT u.userID, u.name, COUNT(*) AS numberOfBooking
FROM Users u, RoomBookings b
WHERE b.hotelID = 1 AND b.customerID = u.userID
GROUP BY u.userID, u.name
ORDER BY numberOfBooking DESC
LIMIT 5;