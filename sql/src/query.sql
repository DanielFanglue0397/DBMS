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