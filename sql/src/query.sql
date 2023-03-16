SELECT *
FROM (
	SELECT h.hotelID, h.hotelName, h.dateEstablished, calculate_distance(90, 24, latitude, longitude)
	FROM hotel h) AS d
WHERE d.calculate_distance < 30;

-- source ../lab8/startPostgreSQL.sh 
-- source ../lab8/createPostgreDB.sh
-- source ./sql/scripts/create_db.sh
-- source ./java/scripts/compile.sh
-- psql -h localhost -p $PGPORT "$USER"_DB < ./sql/src/query.sql 
