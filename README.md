# City-Pulse
Feel the rhythm of your city with City Pulse, your personal guide to the most exciting events. Discover concerts, festivals, art shows, meetups, and more. Never miss a beat with City Pulse!

City Pulse: Where your city comes alive. 🏙️

## Key Functionalities

* **Main Screen:**
    * Effortlessly browse a complete list of upcoming events.
    * Add interesting events to your favourites for quick access.
* **Favorites Screen:** 
    * View your list of favourite events.
    * Create private events to keep your calendar organized.
    * Effortlessly update or delete private events.
    * Receive confirmation prompts before deleting private events or from favourites.
* **Offline Resilience**
    * Enjoy all the functionalities even without an internet connection.
    * City Pulse uses Room for leveraging the database to ensure data accessibility.
    * The offline changes are automatically synchronized with the server when connectivity is restored. 

## Technology Stack

* **Language:** Kotlin
* **Architecture:**  MVVM (Model-View-ViewModel) and Clean Architecture
* **Database Persistence:** SQLite (accessed through Room persistence library)
* **Networking:** Retrofit


# City-Pulse-API
This REST API provides a robust backend for applications focused on event discovery and management. It leverages Go, the Gin framework, GORM, and PostgreSQL database along with a clean architecture design for scalability and maintainability.

## Core Entities

* **Events ( /events )** 
    * Retrieve all events  (`GET /events`)
    * View event details by ID ( `GET /events/:id`)
    * Filter events by location ( `GET /events/location/:locationId`)
    * Filter events by city (`GET /events/city/:cityId`)
    * Create event (`POST /events`)
    * Update event (`PUT /events/:id`)
    * Delete event (`DELETE /events/:id`)

* **Artists ( /artists )**
    * Get all artists (`/artists`)
    * Get an artist by ID (`/artists/:id`)
    * Create artist (`POST /artists`)
    * Update artist (`PUT /artists/:id`)
    * Delete artist (`DELETE /artists/:id`)

* **Users ( /users )**
    * Authentication: register and login (`POST /users/register`, `POST /users/login`)
    * Account management:
         * view own profile (normal users) (`GET /users/:id`)
         * view all users (admin) (`GET /users`)
         * update an account (restricted by role) (`PUT /users/:id`)
         * delete an account (restricted by role) (`DELETE /users/:id`)

* **Genres ( /genres )**
    * List all genres (`/genres`)
    * View a genre by ID (`/genres/:id`)
    * Create genre (`POST /genres`)
    * Update genre (`PUT /genres/:id`)
    * Delete genre (`DELETE /genres/:id`)

* **Locations ( /locations )**
    * Retrieve all locations (`/locations`)
    * View a location by ID (`/locations/:id`)
    * Filter locations by city (`/locations/city/:cityId`)
    * Create location (`POST /locations`)
    * Update location (`PUT /locations/:id`)
    * Delete location (`DELETE /locations/:id`)
 
* **Cities ( /cities )**
    * Retrieve all cities (`/cities`)
    * View a city by ID (`/cities/:id`)
    * Create city (`POST /cities`)
    * Update city (`PUT /cities/:id`)
    * Delete city (`DELETE /cities/:id`)

## Important Associations

* **Event-Artist Relationships ( /event-artist )**
    * Manage associations between events and artists 
    * Retrieve events for a specific artist or vice versa

* **Artist-Genre Relationships ( /artist-genre )**
    * Manage associations between artists and genres
    * Find artists with a specific genre or vice versa 

* **Favourite Events ( /favourite-events )**
    * Users can mark events as favourites
    * Retrieve favourites for a specific user 

## Key Considerations

* **User Roles:** The API supports a distinction between normal users and admins, with appropriate restrictions on certain actions.
* **Prioritization:** Entities and actions likely to be the most frequently used in an event-focused app are listed at the top. 
