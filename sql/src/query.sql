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
INSERT INTO RoomBookings(customerID, hotelID, roomNumber, bookingDate) VALUES (1, 2, 5, '03/15/2023') RETURNING bookingID;
SELECT b.bookingID, b.customerID, b.hotelID, b.roomNumber, b.bookingDate, r.price
FROM RoomBookings b, Rooms r
WHERE b.bookingID = 500 AND r.hotelID = b.hotelID AND r.roomNumber = b.roomNumber;

-- viewRecentBookingsfromCustomer
SELECT r.bookingID, r.hotelID, r.roomNumber, r.bookingDate
FROM RoomBookings r
WHERE r.customerID = 99 AND r.bookingDate < CURRENT_DATE
ORDER BY r.bookingDate DESC
LIMIT 5;
