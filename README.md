# Voicemail Application Backend

This is the backend code for the Voicemail Application. It is built using the Spring Boot framework and MySQL database.

## Technologies Used

- Spring Boot
- MySQL
- Lombok
- JWT Authentication
- BCrypt
- Refresh Token
- JUnit
- Mockito
- Postman

## Prerequisites

- Java 17
- MySQL database
- Maven

## Getting Started

1. Clone the repository
2. Set up MySQL database with name `database_name`
3. Update `application.properties` file with your database credentials
4. Build and run the project

The application will be available at http://localhost:8080

## API Endpoints

| Mapping | Endpoint                              | Description                    |
|-------  | ---------------------------------     | ------------------------------ |
| POST | `/voicemail/v1/user/register`     | Register new user              |
| POST | `/voicemail/v1/user/login`               | Authenticate user              |
| POST | `/voicemail/v1/user/refreshtoken`         | Refresh access token           |
| POST | `/voicemail/v1/user/logout` | Logout user |
| GET | `/voicemail/v1/mail/inbox`                | Send email                     |
| GET | `/voicemail/v1/mail/send-box`               | Get all sent emails           |
| POST | `/voicemail/v1/mail/send`          | Send email |
| GET | `/voicemail/v1/mail/`               | Get email by id from inbox |
| DELETE | `/voicemail/v1/mail/`               | Delete email by id from inbox |
| GET| `voicemail/v1/draft/`          | Get all drafts  |
| GET| `voicemail/v1/draft/{id}`          |  Get draft by Id |
| POST| `voicemail/v1/draft/`          | Create Draft  |
| PUT| `voicemail/v1/draft/{id}`          |Update Draft |
| DELETE| `voicemail/v1/draft{id}`          | Delete Draft |
| GET| `voicemail/v1/contact/`                | Get All Contacts |
| GET| `voicemail/v1/contact/{id}`                | Get contact by Id |
| POST| `voicemail/v1/contact`                | Create contact |
| PUT| `voicemail/v1/contact/{id}`                | Update contact|
| DELETE| `voicemail/v1/contact/{id}`                |Delete contact |
| POST| `voicemail/v1/attachment`           | Upload Attachment|
| GET| `voicemail/v1/attachment/{id}`           | Download Attachment|
| DELETE| `voicemail/v1/attachment/{id}`           | Delete Attachment| 

## Tests

- 44 unit test cases were created to test the functionality of the application
- Postman was used to test the API endpoints

## Authors

- [Jatin Saini](https://github.com/js-designs-321)
 
