# Scheduling_App
Program developed to support a global consulting organization that conducts business in multiple languages and time zones and pulls data from a provided MySQL database. The database is used for other systems and therefore its structure cannot be modified. 

Features:<br/>
* Login form that can determine the user’s location and translate login and error control messages (e.g., “The username and password did not match.”) into two languages. English and Spanish. The login form then queries the database to verify the user’s credentials.

* Add, update, and delete customer records in the database including name, address, and phone number.
 
* Add, update, and delete appointments. Capturing the type of appointment and a link to the specific customer record in the database using primary and foreign keys based on the customer id.

* All SQL queries are passed using prepared statements in an attempt to eliminate the possibility of SQL injection attacks.

* View the calendar by month and by week.
 
* Automatically adjusts appointment times based on user location to determine time zones and daylight-saving time.
 
* Exception controls to prevent scheduling an appointment outside business hours, scheduling overlapping appointments, entering nonexistent or invalid customer data, and entering an incorrect username and password. All user input is also validated and sanitized.

* Alert the user when logging in if they have an appointment within 15 minutes.

* Generate reports detailing the number of appointment types by month, the schedule for each consultant, and the number of appointments made by each customer.

* Track user activity by recording timestamps for user logins in a .txt file. Each new record is appended to the log file, if the file already exists. Otherwise, a new file is created first.
