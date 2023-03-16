source ../lab8/startPostgreSQL.sh 
source ../lab8/createPostgreDB.sh
source ../lab8/stopPostgreDB.sh
source ./sql/scripts/create_db.sh
source ./java/scripts/compile.sh
psql -h localhost -p $PGPORT "$USER"_DB < ./sql/src/query.sql