--Technologies Used--
Java 23
Spring Boot 3.4.4
Spring Security (JWT authentication)
Spring Data JPA (Hibernate)
MySQL Database
OpenAPI for API Documentation
Maven for build and dependency management
Lombok

--Entity -> Description--
Users -> Stores player and admin information
Wallet -> Manages user balances
WalletTransaction -> Logs all credits and debits
Match -> Represents cricket matches with statuses (Upcoming, Ongoing, Completed,AutoCompleted)
Bet -> Stores player bets placed on matches
Payout -> Handles the payout process and winning distributions

--Relationships--
Users -> Wallet (One-to-One)
Wallet -> WalletTransaction (One-to-Many)
Match -> Bet (One-to-Many)
Users -> Bet (One-to-Many)


