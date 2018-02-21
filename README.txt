EDA344 2018
Group 1 - Oscar Evertsson and Neda Farhand
Simple Web Server in Java
----------------------------------------------------------

This .txt file contains the documentation for the web 
server program in its entirety, with mentions of specific
classes.

----------------------------------------------------------
			OVERVIEW
----------------------------------------------------------
The structure of the program is largely modular, with 
separate classes responsible for separate steps in the 
request handling process.

Any request made to the server will be dealt with by the 
RequestManager.java class, which will parse the input
string and determine 1) if it is of an acceptable format, 
2) if so, what kind of request has been made. 

The acceptable format follows the HTTP/1.0 RFC (1945). It
should contain either 3 or 5 elements, and be of /abs_val
format. Any other request will be considered a 
'Bad Request'.

If it is of type HEAD or GET, it will go to either
HeadRequest.java or GetRequest.java, which will 
subsequently return the appropriate response. 

The response message is assembled by the class
RequestHelper.java, depending on what type of request it
is and if it is valid. An assembly of an accepted 
message contains for instance the status code, HTTP 
version, content length, last modified date, and content 
type (+/- depending on what kind of request has been made. 

Response messages are implemented in Enum form.

The class FileHandler.java manages handling files and 
surrounding information.

