/*
 * Template JAVA User Interface
 * =============================
 *
 * Database Management Systems
 * Department of Computer Science &amp; Engineering
 * University of California - Riverside
 *
 * Target DBMS: 'Postgres'
 *
 */


import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;
import java.lang.Math;

/**
 * This class defines a simple embedded SQL utility class that is designed to
 * work with PostgreSQL JDBC drivers.
 *
 */
public class Hotel {

   public static final String ANSI_RESET = "\u001B[0m";
   public static final String ANSI_BLACK = "\u001B[30m";
   public static final String ANSI_RED = "\u001B[31m";
   public static final String ANSI_GREEN = "\u001B[32m";
   public static final String ANSI_YELLOW = "\u001B[33m";
   public static final String ANSI_BLUE = "\u001B[34m";
   public static final String ANSI_PURPLE = "\u001B[35m";
   public static final String ANSI_CYAN = "\u001B[36m";
   public static final String ANSI_WHITE = "\u001B[37m";

   public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
   public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
   public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
   public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
   public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
   public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
   public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
   public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

   // reference to physical database connection.
   private Connection _connection = null;

   // handling the keyboard inputs through a BufferedReader
   // This variable can be global for convenience.
   static BufferedReader in = new BufferedReader(
                                new InputStreamReader(System.in));

   /**
    * Creates a new instance of Hotel 
    *
    * @param hostname the MySQL or PostgreSQL server hostname
    * @param database the name of the database
    * @param username the user name used to login to the database
    * @param password the user login password
    * @throws java.sql.SQLException when failed to make a connection.
    */
   public Hotel(String dbname, String dbport, String user, String passwd) throws SQLException {

      System.out.print("Connecting to database...");
      try{
         // constructs the connection URL
         String url = "jdbc:postgresql://localhost:" + dbport + "/" + dbname;
         System.out.println ("\nConnection URL: " + url);

         // obtain a physical connection
         this._connection = DriverManager.getConnection(url, user, passwd);
         System.out.println("Done");
      }catch (Exception e){
         System.err.println("Error - Unable to Connect to Database: " + e.getMessage() );
         System.out.println("Make sure you started postgres on this machine");
         System.exit(-1);
      }//end catch
   }//end Hotel

   // Method to calculate euclidean distance between two latitude, longitude pairs. 
   public double calculateDistance (double lat1, double long1, double lat2, double long2){
      double t1 = (lat1 - lat2) * (lat1 - lat2);
      double t2 = (long1 - long2) * (long1 - long2);
      return Math.sqrt(t1 + t2); 
   }
   /**
    * Method to execute an update SQL statement.  Update SQL instructions
    * includes CREATE, INSERT, UPDATE, DELETE, and DROP.
    *
    * @param sql the input SQL string
    * @throws java.sql.SQLException when update failed
    */
   public void executeUpdate (String sql) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the update instruction
      stmt.executeUpdate (sql);

      // close the instruction
      stmt.close ();
   }//end executeUpdate

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and outputs the results to
    * standard out.
    *
    * @param query the input query string
    * @return the number of rows returned
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int executeQueryAndPrintResult (String query) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the query instruction
      ResultSet rs = stmt.executeQuery (query);

      /*
       ** obtains the metadata object for the returned result set.  The metadata
       ** contains row and column info.
       */
      ResultSetMetaData rsmd = rs.getMetaData ();
      int numCol = rsmd.getColumnCount ();
      int rowCount = 0;

      // iterates through the result set and output them to standard out.
      boolean outputHeader = true;
      while (rs.next()){
		 if(outputHeader){
			for(int i = 1; i <= numCol; i++){
			System.out.print(rsmd.getColumnName(i) + "\t");
			}
			System.out.println();
			outputHeader = false;
		 }
         for (int i=1; i<=numCol; ++i)
            System.out.print (rs.getString (i) + "\t");
         System.out.println ();
         ++rowCount;
      }//end while
      stmt.close ();
      return rowCount;
   }//end executeQuery

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and returns the results as
    * a list of records. Each record in turn is a list of attribute values
    *
    * @param query the input query string
    * @return the query result as a list of records
    * @throws java.sql.SQLException when failed to execute the query
    */
   public List<List<String>> executeQueryAndReturnResult (String query) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the query instruction
      ResultSet rs = stmt.executeQuery (query);

      /*
       ** obtains the metadata object for the returned result set.  The metadata
       ** contains row and column info.
       */
      ResultSetMetaData rsmd = rs.getMetaData ();
      int numCol = rsmd.getColumnCount ();
      int rowCount = 0;

      // iterates through the result set and saves the data returned by the query.
      boolean outputHeader = false;
      List<List<String>> result  = new ArrayList<List<String>>();
      while (rs.next()){
        List<String> record = new ArrayList<String>();
		for (int i=1; i<=numCol; ++i)
			record.add(rs.getString (i));
        result.add(record);
      }//end while
      stmt.close ();
      return result;
   }//end executeQueryAndReturnResult

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and returns the number of results
    *
    * @param query the input query string
    * @return the number of rows returned
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int executeQuery (String query) throws SQLException {
       // creates a statement object
       Statement stmt = this._connection.createStatement ();

       // issues the query instruction
       ResultSet rs = stmt.executeQuery (query);

       int rowCount = 0;

       // iterates through the result set and count nuber of results.
       while (rs.next()){
          rowCount++;
       }//end while
       stmt.close ();
       return rowCount;
   }

   /**
    * Method to fetch the last value from sequence. This
    * method issues the query to the DBMS and returns the current
    * value of sequence used for autogenerated keys
    *
    * @param sequence name of the DB sequence
    * @return current value of a sequence
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int getCurrSeqVal(String sequence) throws SQLException {
      Statement stmt = this._connection.createStatement ();

      ResultSet rs = stmt.executeQuery (String.format("Select currval('%s')", sequence));
      if (rs.next())
         return rs.getInt(1);
      return -1;
   }

   public int getNewUserID(String sql) throws SQLException {
      Statement stmt = this._connection.createStatement ();
      ResultSet rs = stmt.executeQuery (sql);
      if (rs.next())
         return rs.getInt(1);
      return -1;
   }
   /**
    * Method to close the physical connection if it is open.
    */
   public void cleanup(){
      try{
         if (this._connection != null){
            this._connection.close ();
         }//end if
      }catch (SQLException e){
         // ignored.
      }//end try
   }//end cleanup

   /**
    * The main execution method
    *
    * @param args the command line arguments this inclues the <mysql|pgsql> <login file>
    */
   public static void main (String[] args) {
      if (args.length != 3) {
         System.err.println (
            "Usage: " +
            "java [-classpath <classpath>] " +
            Hotel.class.getName () +
            " <dbname> <port> <user>");
         return;
      }//end if

      Hotel esql = null;
      try{
         // use postgres JDBC driver.
         Class.forName ("org.postgresql.Driver").newInstance ();
         // instantiate the Hotel object and creates a physical
         // connection.
         String dbname = args[0];
         String dbport = args[1];
         String user = args[2];
         esql = new Hotel (dbname, dbport, user, "");
         boolean attempted = false;
         boolean badchoice = false;
         boolean newUserCreated = false;
         boolean keepon = true;
         while(keepon) {
            Greeting();
            if (attempted) {
               attempted = false;
               System.out.println(ANSI_RED +"\nWe can't find that userID and password. Please try again." + ANSI_RESET);
            }
            if (badchoice) {
               badchoice = false;
               System.out.println(ANSI_RED +"\nUnrecognized choice!" + ANSI_RESET);
            }
            if (newUserCreated) {
               newUserCreated = false;
               System.out.println (ANSI_GREEN + "\nUser successfully created with userID = " + esql.getNewUserID("SELECT last_value FROM users_userID_seq") + ANSI_RESET);         
            }
            System.out.println();
            System.out.println("1. Create user");
            System.out.println("2. Log in");
            System.out.println("9. < EXIT");
            System.out.println("----------------------------------------------------------");
            String authorisedUser = null;
            switch (readChoice()){
               case 1: CreateUser(esql); newUserCreated = true; break;
               case 2: authorisedUser = LogIn(esql); attempted = true; break;
               case 9: keepon = false; break;
               default : badchoice = true; break;
            }//end switch
            if (authorisedUser != null) {
               String query = "SELECT u.userType FROM Users u WHERE u.userID = " + authorisedUser;
               String userType = esql.executeQueryAndReturnResult(query).get(0).get(0);
               if (userType.toLowerCase().contains("customer")) {
                  boolean usermenu = true;                  
                  while(usermenu) {
                     System.out.println();
                     System.out.println("----------------------------------------------------------");
                     System.out.println("|" + ANSI_YELLOW + "                     User Main Menu                     " + ANSI_RESET + "|");
                     System.out.println("----------------------------------------------------------");                
                     System.out.println("| 1. View Hotels within 30 units                         |");
                     System.out.println("| 2. View Rooms                                          |");
                     System.out.println("| 3. Book a Room                                         |");
                     System.out.println("| 4. View recent booking history                         |");
                     System.out.println("----------------------------------------------------------");
                     System.out.println("| 20. Log out                                            |");
                     System.out.println("----------------------------------------------------------");                
                     if (badchoice) {
                        badchoice = false;
                        System.out.println(ANSI_RED +"Unrecognized choice!" + ANSI_RESET);
                     }
                     switch (readChoice()){
                        case 1: viewHotels(esql); break;
                        case 2: viewRooms(esql); break;
                        case 3: bookRooms(esql, authorisedUser); break;
                        case 4: viewRecentBookingsfromCustomer(esql, authorisedUser); break;
                        case 20: usermenu = false; attempted = false; break;
                        default : badchoice = true; break;
                     }
                  }
               }else {
                  boolean managermenu = true;                  
                  while(managermenu) {
                     System.out.println();
                     System.out.println("----------------------------------------------------------");
                     System.out.println("|" + ANSI_YELLOW + "                    Manager Main Menu                   " + ANSI_RESET + "|");
                     System.out.println("----------------------------------------------------------"); 
                     System.out.println("| 1. View Hotels within 30 units                         |");
                     System.out.println("| 2. View Rooms                                          |");
                     System.out.println("| 3. Book a Room                                         |");
                     System.out.println("| 4. View recent booking history                         |");
   
                     //the following functionalities basically used by managers
                     System.out.println("| 5. Update Room Information                             |");
                     System.out.println("| 6. View 5 recent Room Updates Info                     |");
                     System.out.println("| 7. View booking history of the hotel                   |");
                     System.out.println("| 8. View 5 regular Customers                            |");
                     System.out.println("| 9. Place room repair Request to a company              |");
                     System.out.println("| 10. View room repair Requests history                  |");
                     System.out.println("----------------------------------------------------------");
                     System.out.println("| 20. Log out                                            |");
                     System.out.println("----------------------------------------------------------");                
                     if (badchoice) {
                        badchoice = false;
                        System.out.println(ANSI_RED +"Unrecognized choice!" + ANSI_RESET);
                     }
                     switch (readChoice()){
                        case 1: viewHotels(esql); break;
                        case 2: viewRooms(esql); break;
                        case 3: bookRooms(esql, authorisedUser); break;
                        case 4: viewRecentBookingsfromCustomer(esql, authorisedUser); break;
                        case 5: updateRoomInfo(esql, authorisedUser); break;
                        case 6: viewRecentUpdates(esql, authorisedUser); break;
                        case 7: viewBookingHistoryofHotel(esql, authorisedUser); break;
                        case 8: viewRegularCustomers(esql, authorisedUser); break;
                        case 9: placeRoomRepairRequests(esql, authorisedUser); break;
                        case 10: viewRoomRepairHistory(esql, authorisedUser); break;
                        case 20: managermenu = false; attempted = false; break;
                        default : badchoice = true; break;
                     }                  
                  }
               }
            }
         }//end while
      }catch(Exception e) {
         System.err.println (e.getMessage ());
      }finally{
         // make sure to cleanup the created table and close the connection.
         try{
            if(esql != null) {
               System.out.print("Disconnecting from database...");
               esql.cleanup ();
               System.out.println("Done\n\nThank you for using the Hotel Database Management System!");
            }//end if
         }catch (Exception e) {
            // ignored.
         }//end try
      }//end try
   }//end main

   public static void Greeting(){
      System.out.println();      
      System.out.println();      
      System.out.println();      
      System.out.println(ANSI_YELLOW +"======================= Welcome To =======================" + ANSI_RESET);
      System.out.println(ANSI_CYAN + "         ___       ___       ___       ___       ___   " + ANSI_RESET);
      System.out.println(ANSI_CYAN + "        /\\__\\     /\\  \\     /\\  \\     /\\  \\     /\\__\\  " + ANSI_RESET);
      System.out.println(ANSI_CYAN + "       /:/__/_   /::\\  \\    \\:\\  \\   /::\\  \\   /:/  /  " + ANSI_RESET);
      System.out.println(ANSI_CYAN + "      /::\\/\\__\\ /:/\\:\\__\\   /::\\__\\ /::\\:\\__\\ /:/__/   " + ANSI_RESET);
      System.out.println(ANSI_CYAN + "      \\/\\::/  / \\:\\/:/  /  /:/\\/__/ \\:\\:\\/  / \\:\\  \\   " + ANSI_RESET);
      System.out.println(ANSI_CYAN + "        /:/  /   \\::/  /   \\/__/     \\:\\/  /   \\:\\__\\  " + ANSI_RESET);
      System.out.println(ANSI_CYAN + "        \\/__/     \\/__/               \\/__/     \\/__/  " + ANSI_RESET);
      System.out.println();      
      System.out.println(ANSI_YELLOW + "                Database Management System                " + ANSI_RESET);      
      System.out.println(ANSI_YELLOW + "==========================================================" + ANSI_RESET);                                          
   }//end Greeting

   /*
    * Reads the users choice given from the keyboard
    * @int
    **/
   public static int readChoice() {
      int input;
      // returns only if a correct value is given.
      do {
         System.out.print("Please make your choice: ");
         try { // read the integer, parse it and break.
            input = Integer.parseInt(in.readLine());
            break;
         }catch (Exception e) {
            System.out.println(ANSI_RED + "Your input is invalid!" + ANSI_RESET);
            continue;
         }//end try
      }while (true);
      return input;
   }//end readChoice
   
   /*
    * Reads the userID given from the keyboard
    * @int
    **/
   public static int readUserID() {
      int input;
      // returns only if a correct value is given.
      do { // read the integer, parse it and break.
         try {
            input = Integer.parseInt(in.readLine());
            break;
         }catch (Exception e){
            System.out.println(ANSI_RED + "\tUserID should only contain integers!" + ANSI_RESET);
            System.out.print("\tEnter userID: ");
            continue;
         }
      }while (true);
      return input;
   }//end readUserID

   /*
    * Creates a new user
    **/
   public static void CreateUser(Hotel esql){
      try{
         System.out.print("\tEnter name: ");
         String name = in.readLine();
         System.out.print("\tEnter password: ");
         String password = in.readLine(); 
         String type="customer";
			String query = String.format("INSERT INTO USERS (name, password, userType) VALUES ('%s','%s', '%s')", name, password, type);
         esql.executeUpdate(query);
      }catch(Exception e){
         System.err.println (e.getMessage ());
      }
   }//end CreateUser

   /*
    * Check log in credentials for an existing user
    * @return User login or null is the user does not exist
    **/
   public static String LogIn(Hotel esql){
      try{
         System.out.print("\tEnter userID: ");
         String userID = Integer.toString(readUserID());
         System.out.print("\tEnter password: ");
         String password = in.readLine();

         String query = String.format("SELECT * FROM USERS WHERE userID = '%s' AND password = '%s'", userID, password);
         int userNum = esql.executeQuery(query);
         if (userNum > 0)
            return userID;
         return null;
      }catch(Exception e){
         System.err.println (e.getMessage ());
         return null;
      }
   }//end

// Rest of the functions definition go in here

   public static void viewHotels(Hotel esql) {
      try{
         System.out.print("\tEnter latitude: ");
         String latitude = in.readLine();
         System.out.print("\tEnter longitude: ");
         String longitude = in.readLine();

         String query = "SELECT d.hotelID, d.hotelName, d.dateEstablished FROM (SELECT hotelID, hotelName, dateEstablished, calculate_distance(";
         query += latitude + ", " + longitude + ", ";
         query += "latitude, longitude) AS distance FROM hotel) AS d WHERE d.distance < 30";
         
         List<List<String>> output = esql.executeQueryAndReturnResult(query);
         int rowCount = output.size();

         System.out.printf("\n----------------------------------------------------------------\n");
         System.out.printf("|                    Hotels within 30 units                    |\n");
         System.out.printf("----------------------------------------------------------------\n");
         System.out.printf("| %8s | %-30s | %16s |%n", "Hotel ID", "Hotel Name", "Date Established");
         System.out.printf("----------------------------------------------------------------\n");
         for(int i = 0; i < rowCount; i++){
            System.out.printf("| %8s | %-30s | %16s |%n", output.get(i).get(0), output.get(i).get(1), output.get(i).get(2));
         }
         System.out.printf("----------------------------------------------------------------\n\n");
      }catch(Exception e){
         System.err.println (e.getMessage());
      }
   }
   public static void viewRooms(Hotel esql) {
      try{
         System.out.print("\tEnter Hotel ID: ");
         String hotelID = in.readLine();
         int row = esql.executeQuery("SELECT hotelID FROM Hotel WHERE hotelID = " + hotelID);
         while (row == 0){
            System.out.print("\tInvalid Hotel ID. Enter hotel ID: ");
            hotelID = in.readLine();
            row = esql.executeQuery("SELECT hotelID FROM Hotel WHERE hotelID = " + hotelID);
         }
         System.out.print("\tEnter a date (MM/DD/YYYY): ");
         String date = in.readLine();

         String query = "SELECT r.hotelID, r.roomNumber, r.price, r.imageURL FROM rooms r WHERE NOT EXISTS (SELECT * FROM roombookings b WHERE r.hotelID = hotelID AND r.roomNumber = b.roomNumber AND b.bookingDate = '";
         query += date + "')";
         query += "AND r.hotelID = ";
         query += hotelID  + " ORDER BY r.roomNumber";
         
         List<List<String>> output = esql.executeQueryAndReturnResult(query);
         int rowCount = output.size();
         System.out.printf("\n---------------------------------------------------------------------\n");
         String title = String.format("|                   Rooms Available on %10s                   |", date);
         System.out.println(title);
         System.out.printf("---------------------------------------------------------------------\n");
         System.out.printf("| %8s | %11s | %7s | %-30s |%n", "Hotel ID", "Room Number", "Price", "Image URL");
         System.out.printf("---------------------------------------------------------------------\n");
         for(int i = 0; i < rowCount; i++){
            System.out.printf("| %8s | %11s | %7s | %30s |%n", output.get(i).get(0), output.get(i).get(1), output.get(i).get(2), output.get(i).get(3));
         }
         System.out.printf("---------------------------------------------------------------------\n\n");
      }catch(Exception e){
         System.err.println (e.getMessage());
      }
   }
   public static void bookRooms(Hotel esql, String userID) {
      try{
         System.out.print("\tEnter Hotel ID: ");
         String hotelID = in.readLine();
         int row = esql.executeQuery("SELECT hotelID FROM Hotel WHERE hotelID = " + hotelID);
         while (row == 0){
            System.out.print("\tInvalid Hotel ID. Enter hotel ID: ");
            hotelID = in.readLine();
            row = esql.executeQuery("SELECT hotelID FROM Hotel WHERE hotelID = " + hotelID);
         }

         System.out.print("\tEnter Room Number: ");
         String roomNum = in.readLine();
         row = esql.executeQuery("SELECT roomNumber FROM Rooms WHERE roomNumber = "+ roomNum + " AND HotelID = " + hotelID);
         while (row == 0){
            System.out.print("\tInvalid Room Number. Enter Room Number: ");
            roomNum = in.readLine();
            row = esql.executeQuery("SELECT roomNumber FROM Rooms WHERE roomNumber = "+ roomNum + " AND HotelID = " + hotelID);
         }         
         System.out.print("\tEnter Date (MM/DD/YYYY): ");
         String date = in.readLine();
         row = esql.executeQuery("SELECT bookingDate FROM roomBookings WHERE roomNumber = "+ roomNum + " AND HotelID = " + hotelID + " AND bookingDate = '" + date + "'");
         while (row != 0){
            String notAvailable = String.format("\tRoom %s at Hotel ID %s is not available on %s", roomNum, hotelID, date);
            System.out.print(notAvailable);
            System.out.print("\n\tEnter another date (MM/DD/YYYY): ");
            date = in.readLine();
            row = esql.executeQuery("SELECT bookingDate FROM roomBookings WHERE roomNumber = "+ roomNum + " AND HotelID = " + hotelID + " AND bookingDate = '" + date + "'");
         }

         String bookingQuery = "INSERT INTO RoomBookings(customerID, hotelID, roomNumber, bookingDate) VALUES (";
         bookingQuery += userID + ", ";
         bookingQuery += hotelID + ", ";
         bookingQuery += roomNum + ", '";
         bookingQuery += date + "') RETURNING bookingID ";
         String bookingID = esql.executeQueryAndReturnResult(bookingQuery).get(0).get(0);

         String reservation = "SELECT b.bookingID, b.customerID, b.hotelID, b.roomNumber, b.bookingDate, r.price FROM RoomBookings b, Rooms r WHERE ";
         reservation += "bookingID = " + bookingID + " AND r.hotelID = b.hotelID AND r.roomNumber = b.roomNumber";
         List<List<String>> output = esql.executeQueryAndReturnResult(reservation);

         System.out.printf("\n------------------------------------------------------------------------------\n");
         System.out.printf("|                              Your Reservatoin                              |\n");
         System.out.printf("------------------------------------------------------------------------------\n");
         System.out.printf("| %10s | %11s | %8s | %11s | %12s | %7s |%n", "Booking ID", "Customer ID", "Hotel ID", "Room Number", "Booking Date", "Price");
         System.out.printf("------------------------------------------------------------------------------\n");
         System.out.printf("| %10s | %11s | %8s | %11s | %12s | %7s |%n", output.get(0).get(0), output.get(0).get(1), output.get(0).get(2), output.get(0).get(3), output.get(0).get(4), output.get(0).get(5));
         System.out.printf("------------------------------------------------------------------------------\n\n");
      }catch(Exception e){
         System.err.println (e.getMessage());
      }
   }
   public static void viewRecentBookingsfromCustomer(Hotel esql, String userID) {
      try{
         String histortQuery = "SELECT b.bookingID, b.hotelID, b.roomNumber, b.bookingDate, r.price FROM RoomBookings b, Rooms r WHERE b.customerID = ";
         histortQuery += userID;
         histortQuery += " AND r.hotelID = b.hotelID AND r.roomNumber = b.roomNumber ORDER BY b.bookingID DESC LIMIT 5";
         
         List<List<String>> output = esql.executeQueryAndReturnResult(histortQuery);
         int rowCount = output.size();

         System.out.printf("\n----------------------------------------------------------------\n");
         System.out.printf("|                  Your Recent Booking History                 |\n");
         System.out.printf("----------------------------------------------------------------\n");
         System.out.printf("| %10s | %8s | %11s | %12s | %7s |%n", "Booking ID", "Hotel ID", "Room Number", "Booking Date", "Price");
         System.out.printf("----------------------------------------------------------------\n");
         for(int i = 0; i < rowCount; i++){
            System.out.printf("| %10s | %8s | %11s | %12s | %7s |%n", output.get(i).get(0), output.get(i).get(1), output.get(i).get(2), output.get(i).get(3), output.get(i).get(4));
         }
         System.out.printf("----------------------------------------------------------------\n\n");
      }catch(Exception e){
         System.err.println (e.getMessage());
      }     
   }
   public static void updateRoomInfo(Hotel esql, String userID) {
      try{
         boolean keepon = true;
         while(keepon) {
            System.out.print("\tEnter Hotel ID: ");
            String hotelID = in.readLine();
            int row = esql.executeQuery("SELECT hotelID FROM Hotel WHERE hotelID = " + hotelID + " AND managerUserID = " + userID);
            while (row == 0){
               System.out.print("\tInvalid hotel ID or you don't manage that hotel.\n\tEnter hotel ID: ");
               hotelID = in.readLine();
               row = esql.executeQuery("SELECT hotelID FROM Hotel WHERE hotelID = " + hotelID + " AND managerUserID = " + userID);
            }

            System.out.print("\tEnter Room Number: ");
            String roomNum = in.readLine();
            row = esql.executeQuery("SELECT roomNumber FROM Rooms WHERE roomNumber = "+ roomNum + " AND HotelID = " + hotelID);
            while (row == 0){
               System.out.print("\tInvalid Room Number. Enter Room Number: ");
               roomNum = in.readLine();
               row = esql.executeQuery("SELECT roomNumber FROM Rooms WHERE roomNumber = "+ roomNum + " AND HotelID = " + hotelID);
            }
            boolean updateMenu = true;
            while(updateMenu) {
               System.out.println(String.format("\nWhat info would you like to update on Room %2s at Hotel ID %2s", roomNum, hotelID));
               System.out.println("---------");
               System.out.println("1. Price");
               System.out.println("2. Image URL");
               System.out.println(".........................");
               System.out.println("8. Update other room.");
               System.out.println("9. < Return to Main Menu.");
               switch (readChoice()){
                  case 1:
                     System.out.print("\tEnter the new price: $");
                     String newPrice = in.readLine();
                     String priceQuery = "UPDATE Rooms SET price = ";
                     priceQuery += newPrice;
                     priceQuery += " WHERE hotelID = " + hotelID;
                     priceQuery += " AND roomNumber = " + roomNum;
                     esql.executeUpdate(priceQuery);

                     String showPriceResult = "SELECT * FROM Rooms ";
                     showPriceResult += " WHERE hotelID = " + hotelID;
                     showPriceResult += " AND roomNumber = " + roomNum;
                     List<List<String>> priceOutput = esql.executeQueryAndReturnResult(showPriceResult);

                     System.out.printf("\n---------------------------------------------------------------------\n");
                     System.out.printf("|                           Updated Info                            |\n");
                     System.out.printf("---------------------------------------------------------------------\n");
                     System.out.printf("| %8s | %11s | %7s | %-30s |%n", "Hotel ID", "Room Number", "Price", "Image URL");
                     System.out.printf("---------------------------------------------------------------------\n");
                     System.out.printf("| %8s | %11s | %7s | %30s |%n", priceOutput.get(0).get(0), priceOutput.get(0).get(1), priceOutput.get(0).get(2), priceOutput.get(0).get(3));
                     System.out.printf("---------------------------------------------------------------------\n\n");
                     break;
                  case 2: 
                     System.out.print("\tEnter the new image URL: ");
                     String newUrl = in.readLine();
                     String urlQuery = "UPDATE Rooms SET imageURL = '";
                     urlQuery += newUrl + "'";
                     urlQuery += " WHERE hotelID = " + hotelID;
                     urlQuery += " AND roomNumber = " + roomNum;
                     esql.executeUpdate(urlQuery);

                     String showUrlResult = "SELECT * FROM Rooms ";
                     showUrlResult += " WHERE hotelID = " + hotelID;
                     showUrlResult += " AND roomNumber = " + roomNum;
                     List<List<String>> urlOutput = esql.executeQueryAndReturnResult(showUrlResult);

                     System.out.printf("\n---------------------------------------------------------------------\n");
                     System.out.printf("|                           Updated Info                            |\n");
                     System.out.printf("---------------------------------------------------------------------\n");
                     System.out.printf("| %8s | %11s | %7s | %-30s |%n", "Hotel ID", "Room Number", "Price", "Image URL");
                     System.out.printf("---------------------------------------------------------------------\n");
                     System.out.printf("| %8s | %11s | %7s | %30s |%n", urlOutput.get(0).get(0), urlOutput.get(0).get(1), urlOutput.get(0).get(2), urlOutput.get(0).get(3));
                     System.out.printf("---------------------------------------------------------------------\n\n");
                     break;
                  case 8: updateMenu = false; break;
                  case 9: updateMenu = false; keepon = false; break;
                  default : System.out.println("\nUnrecognized choice!\n"); break;
               }
            }
         }
      }catch(Exception e){
         System.err.println(e.getMessage());
      }
   }
   public static void viewRecentUpdates(Hotel esql, String userID) {
      try{
         String updateLogQuery = "SELECT * FROM RoomUpdatesLog WHERE managerID = ";
         updateLogQuery += userID + " ORDER BY updatedOn DESC LIMIT 5";
         List<List<String>> output = esql.executeQueryAndReturnResult(updateLogQuery);
         int rowCount = output.size();

         System.out.printf("\n-----------------------------------------------------------------------------\n");
         System.out.printf("|                           Last 5 Recent Updates                           |\n");
         System.out.printf("-----------------------------------------------------------------------------\n");
         System.out.printf("| %13s | %10s | %8s | %11s | %19s |%n", "Update Number", "Manager ID", "Hotel ID", "Room Number", "Updated On");
         System.out.printf("-----------------------------------------------------------------------------\n");
         for(int i = 0; i < rowCount; i++){
            System.out.printf("| %13s | %10s | %8s | %11s | %19s |%n", output.get(i).get(0), output.get(i).get(1), output.get(i).get(2), output.get(i).get(3), output.get(i).get(4));
         }
         System.out.printf("-----------------------------------------------------------------------------\n\n");
      }catch(Exception e){
         System.err.println(e.getMessage());
      }
   }
   public static void viewBookingHistoryofHotel(Hotel esql, String userID) {
      try{
         System.out.print("\tSee history from the date (MM/DD/YYYY): ");
         String startDate = in.readLine();
         System.out.print("\tTo the date (MM/DD/YYYY): ");
         String endDate = in.readLine();
         
         String query = "SELECT b.bookingID, u.name, b.hotelID, b.roomNumber, b.bookingDate, r.price FROM RoomBookings b, Hotel h, Rooms r, Users u WHERE h.managerUserID = ";
         query += userID;
         query += " AND h.hotelID = b.hotelID AND b.hotelID = r.hotelID AND b.customerID = u.userID AND b.roomNumber = r.roomNumber AND b.bookingDate BETWEEN '";
         query += startDate + "' AND '" + endDate + "'";
         query += " ORDER By b.bookingDate DESC";
         List<List<String>> output = esql.executeQueryAndReturnResult(query);
         int rowCount = output.size();

         System.out.printf("\n---------------------------------------------------------------------------------------------------------------------\n");
         String title = String.format("|                                   Booking History From %10s to %10s                                   |", startDate, endDate);
         System.out.println(title);
         System.out.printf("---------------------------------------------------------------------------------------------------------------------\n");
         System.out.printf("| %10s | %-50s | %8s | %11s | %12s | %7s |%n", "Booking ID", "Customer Name", "Hotel ID", "Room Number", "Booking Date", "Price");
         System.out.printf("---------------------------------------------------------------------------------------------------------------------\n");
         for(int i = 0; i < rowCount; i++){
            System.out.printf("| %10s | %-50s | %8s | %11s | %12s | %7s |%n", output.get(i).get(0), output.get(i).get(1), output.get(i).get(2), output.get(i).get(3), output.get(i).get(4), output.get(i).get(5));
         }
         System.out.printf("---------------------------------------------------------------------------------------------------------------------\n\n");
      }catch(Exception e){
         System.err.println(e.getMessage());
      }
   }
   public static void viewRegularCustomers(Hotel esql, String userID) {
      try{
         System.out.print("\tEnter Hotel ID: ");
         String hotelID = in.readLine();
         int row = esql.executeQuery("SELECT hotelID FROM Hotel WHERE hotelID = " + hotelID + " AND managerUserID = " + userID);
         while (row == 0){
            System.out.print("\tInvalid hotel ID or you don't manage that hotel.\n\tEnter hotel ID: ");
            hotelID = in.readLine();
            row = esql.executeQuery("SELECT hotelID FROM Hotel WHERE hotelID = " + hotelID + " AND managerUserID = " + userID);
         }

         String query = "SELECT u.userID, u.name, COUNT(*) AS numberOfBooking FROM Users u, RoomBookings b WHERE ";
         query += "b.hotelID = " + hotelID;
         query += " AND b.customerID = u.userID GROUP BY u.userID, u.name ORDER BY numberOfBooking DESC LIMIT 5";
         List<List<String>> output = esql.executeQueryAndReturnResult(query);
         int rowCount = output.size();
        
         System.out.printf("\n--------------------------------------------------------------------------------\n");
         String title = String.format("|                    Top 5 Regular Customer at Hotel ID: %2s                    |", hotelID);
         System.out.println(title);
         System.out.printf("--------------------------------------------------------------------------------\n");
         System.out.printf("| %7s | %-50s | %13s |%n", "User ID", "Customer Name", "Bookings Made");
         System.out.printf("--------------------------------------------------------------------------------\n");
         for(int i = 0; i < rowCount; i++){
            System.out.printf("| %7s | %-50s | %13s |%n", output.get(i).get(0), output.get(i).get(1), output.get(i).get(2));
         }
         System.out.printf("--------------------------------------------------------------------------------\n\n");
      }catch(Exception e){
         System.err.println(e.getMessage());
      }
   }
   public static void placeRoomRepairRequests(Hotel esql, String userID) {
      try{
         System.out.print("\tEnter Hotel ID: ");
         String hotelID = in.readLine();
         int row = esql.executeQuery("SELECT hotelID FROM Hotel WHERE hotelID = " + hotelID + " AND managerUserID = " + userID);
         while (row == 0){
            System.out.print("\tInvalid hotel ID or you don't manage that hotel.\n\tEnter hotel ID: ");
            hotelID = in.readLine();
            row = esql.executeQuery("SELECT hotelID FROM Hotel WHERE hotelID = " + hotelID + " AND managerUserID = " + userID);
         }
         System.out.print("\tEnter Room Number: ");
         String roomNum = in.readLine();
         row = esql.executeQuery("SELECT roomNumber FROM Rooms WHERE roomNumber = "+ roomNum + " AND HotelID = " + hotelID);
         while (row == 0){
            System.out.print("\tInvalid Room Number. Enter Room Number: ");
            roomNum = in.readLine();
            row = esql.executeQuery("SELECT roomNumber FROM Rooms WHERE roomNumber = "+ roomNum + " AND HotelID = " + hotelID);
         }
         System.out.print("\tEnter Repair Company ID: ");
         String companyID = in.readLine();
         row = esql.executeQuery("SELECT companyID FROM MaintenanceCompany WHERE companyID = " + companyID);
         while (row == 0){
            System.out.print("\tInvalid Repair Company ID. Enter Repair Company ID: ");
            companyID = in.readLine();
            row = esql.executeQuery("SELECT companyID FROM MaintenanceCompany WHERE companyID = " + companyID);
         }
         
         String repairRequest = "INSERT INTO RoomRepairs (companyID, hotelID, roomNumber, repairDate) VALUES (";
         repairRequest += companyID + ", ";
         repairRequest += hotelID + ", ";
         repairRequest += roomNum + ", CURRENT_DATE) RETURNING repairID";

         String repairID = esql.executeQueryAndReturnResult(repairRequest).get(0).get(0);

         String requestRecord = "SELECT * FROM RoomRepairs WHERE repairID = ";
         requestRecord += repairID;
         List<List<String>> output = esql.executeQueryAndReturnResult(requestRecord);

         System.out.printf("\n-----------------------------------------------------------------\n");
         System.out.printf("|                        Repair Request                         |\n");
         System.out.printf("-----------------------------------------------------------------\n");
         System.out.printf("| %9s | %10s | %8s | %11s | %11s |%n", "Repair ID", "Company ID", "Hotel ID", "Room Number", "Repair Date");
         System.out.printf("-----------------------------------------------------------------\n");
         System.out.printf("| %9s | %10s | %8s | %11s | %11s |%n", output.get(0).get(0), output.get(0).get(1), output.get(0).get(2), output.get(0).get(3), output.get(0).get(4));
         System.out.printf("-----------------------------------------------------------------\n\n");
      }catch(Exception e){
         System.err.println(e.getMessage());
      }
   }
   public static void viewRoomRepairHistory(Hotel esql, String userID) {
      try{
         String repairHistory = "SELECT r.repairid, r.companyid, r.hotelid, r.roomnumber, r.repairdate FROM RoomRepairs r, Hotel h WHERE r.hotelID = h.hotelID AND h.managerUserID = ";
         repairHistory += userID + " ORDER BY r.repairdate DESC";
         List<List<String>> output = esql.executeQueryAndReturnResult(repairHistory);
         int rowCount = output.size();

         System.out.printf("\n-----------------------------------------------------------------\n");
         System.out.printf("|                        Repair History                         |\n");
         System.out.printf("-----------------------------------------------------------------\n");
         System.out.printf("| %9s | %10s | %8s | %11s | %11s |%n", "Repair ID", "Company ID", "Hotel ID", "Room Number", "Repair Date");
         System.out.printf("-----------------------------------------------------------------\n");
         for(int i = 0; i < rowCount; i++){
            System.out.printf("| %9s | %10s | %8s | %11s | %11s |%n", output.get(i).get(0), output.get(i).get(1), output.get(i).get(2), output.get(i).get(3), output.get(i).get(4));
         }
         System.out.printf("-----------------------------------------------------------------\n\n");
      }catch(Exception e){
         System.err.println(e.getMessage());
      }
   }

}//end Hotel

