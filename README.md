# Search-Facade API

# Assumptions

I have used mongo db for this assessment
I have assumed the entity would be in the following structure

  {
    "id": "string",
    "type": "string",
    "time": long,
    "email": "string",
    "ip": "string",
    "name" : "string"
  }
  
  I have implemented an end-point to upload json data from json file. I have assumed the json file would contain number of entities in the structure like shown above
  
  To implement a search I could not find a way to pass filter objects as request parameters.
  So, what I have done is I have implemented a POST method and made the filters as request body
  
  
  # To run this project 
  
  Make sure mongo db is running on your local my running the following commands
  
 1) docker pull mongo:latest
 2) docker  run --name mongo-koti -e MYSQL_ROOT_PASSWORD=password -e MYSQL_DATABASE=test  -e MYSQL_PASSWORD=password -d mysql:latest
 3) docker run -d -p 27777:27017  --name mongo-koti mongo:latest
 4) docker ps
 
 Now you should see mongodb running on your local
 
 5) Go to the root directory of this project 
 6) docker build -f Dockerfile -t search-facade .
 7) docker run -p 6868:6868 --name search-facade --link mongo-koti:mongo -d search-facade
 8) docker logs search-facade
 9) docker ps 
 
 Now you should see spring-boot application successfully running on the 6868 port
 
 # To Access the swagger 
 http://localhost:6868/swagger-ui.html
 
 There you could see different REST end-points for viewing / searching resources, create resources, delete resources
 
 GET : http://localhost:6868/resource -> To Access all resources
 GET : http://localhost:6868/resource/{id} -> To access resource by id
 POST: http://localhost:6868/resource  -> To create resource
 POST: http://localhost:6868/resource/search -> To search the resource
 DELETE: http://localhost:6868/resource  -> To delete all the existing resources
 
 DELETE: http://localhost:6868/resource/id -> To delete any specific resource by id
 
 #Note: To run this project with out using docker 
 1) You need to make sure you have installed mongo db locally 
 2) go to the root directory of this project and execute "mvn clean install" from command prompt
 3) run "mvn spring-boot:run"
 
 Thanks
 

  
  


