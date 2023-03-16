-- viewHotels
SELECT *
FROM (
	SELECT hotelID, hotelName, dateEstablished, calculate_distance(90, 24, latitude, longitude) AS distance
	FROM hotel) AS d
WHERE d.distance < 30;

