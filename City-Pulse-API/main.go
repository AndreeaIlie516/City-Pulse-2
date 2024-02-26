package main

import (
	"City-Pulse-API/database"
	"City-Pulse-API/domain/entities"
	"City-Pulse-API/domain/services"
	"City-Pulse-API/infrastructure/dataaccess"
	"City-Pulse-API/infrastructure/middlewares"
	"City-Pulse-API/presentation/handlers"
	"City-Pulse-API/routes"
	"github.com/gin-gonic/gin"
	"github.com/joho/godotenv"
	"log"
)

func main() {
	router := gin.Default()

	if err := godotenv.Load(); err != nil {
		log.Fatal("Error loading .env file")
	}

	db := database.ConnectDB()

	entitiesToMigrate := []interface{}{
		&entities.Artist{},
		&entities.ArtistGenre{},
		&entities.City{},
		&entities.Event{},
		&entities.EventArtist{},
		&entities.Genre{},
		&entities.Location{},
		&entities.User{},
	}

	for _, entity := range entitiesToMigrate {
		err := db.AutoMigrate(entity)
		if err != nil {
			log.Fatalf("Failed to migrate database: %v", err)
		}
	}

	authMiddleware := middlewares.AuthMiddleware{}

	artistRepository := dataaccess.NewGormArtistRepository(db)
	artistGenreRepository := dataaccess.NewGormArtistGenreRepository(db)
	cityRepository := dataaccess.NewGormCityRepository(db)
	eventRepository := dataaccess.NewGormEventRepository(db)
	eventArtistRepository := dataaccess.NewGormEventArtistRepository(db)
	genreRepository := dataaccess.NewGormGenreRepository(db)
	locationRepository := dataaccess.NewGormLocationRepository(db)
	userRepository := dataaccess.NewGormUserRepository(db)

	artistService := services.ArtistService{Repo: artistRepository}
	artistGenreService := services.ArtistGenreService{Repo: artistGenreRepository, ArtistRepo: artistRepository, GenreRepo: genreRepository}
	cityService := services.CityService{Repo: cityRepository, LocationRepo: locationRepository}
	eventService := services.EventService{Repo: eventRepository, LocationRepo: locationRepository, CityRepo: cityRepository}
	eventArtistService := services.EventArtistService{Repo: eventArtistRepository, EventRepo: eventRepository, ArtistRepo: artistRepository, LocationRepo: locationRepository, CityRepo: cityRepository}
	genreService := services.GenreService{Repo: genreRepository}
	locationService := services.LocationService{Repo: locationRepository, CityRepo: cityRepository}
	userService := services.UserService{Repo: userRepository}

	artistHandler := handlers.ArtistHandler{Service: &artistService}
	artistGenreHandler := handlers.ArtistGenreHandler{Service: &artistGenreService}
	cityHandler := handlers.CityHandler{Service: &cityService}
	eventHandler := handlers.EventHandler{Service: &eventService}
	eventArtistHandler := handlers.EventArtistHandler{Service: &eventArtistService}
	genreHandler := handlers.GenreHandler{Service: &genreService}
	locationHandler := handlers.LocationHandler{Service: &locationService}
	userHandler := handlers.UserHandler{Service: &userService}

	routes.RegisterArtistRoutes(router, &artistHandler)
	routes.RegisterArtistGenreRoutes(router, &artistGenreHandler)
	routes.RegisterCityRoutes(router, &cityHandler)
	routes.RegisterEventRoutes(router, &eventHandler)
	routes.RegisterEventArtistRoutes(router, &eventArtistHandler)
	routes.RegisterGenreRoutes(router, &genreHandler)
	routes.RegisterLocationRoutes(router, &locationHandler)
	routes.RegisterUserRoutes(router, &userHandler, authMiddleware)

	err := router.Run("localhost:8080")
	if err != nil {
		log.Fatalf("Failed to run server: %v", err)
		return
	}
}
