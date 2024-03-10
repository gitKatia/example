# Getting Started 

### The application uses H2 embedded database
* You can access the H2 console at http://localhost:9092/h2-console
* Enter the credentials:sa, sa

Examples:

GET    http://localhost:9092/routes
GET    http://localhost:9092/1
GET    http://localhost:9092/reservations/1/10052019
POST   http://localhost:9092/reservations/1/10052019
PUT    http://localhost:9092/reservations/1/10052019/9
DELETE http://localhost:9092/reservations/1/10052019/9
GET    http://localhost:9092/reservations/1/10052019/9

One Route --> Many Stops (OneToMany)
No intersection between routes




