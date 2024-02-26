package main

import (
	"City-Pulse-API/database"
	"City-Pulse-API/domain/entities"
	"City-Pulse-API/domain/services"
	"City-Pulse-API/infrastructure/dataaccess"
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
		&entities.City{},
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

	artistRepository := dataaccess.NewGormArtistRepository(db)
	cityRepository := dataaccess.NewGormCityRepository(db)
	genreRepository := dataaccess.NewGormGenreRepository(db)
	locationRepository := dataaccess.NewGormLocationRepository(db)
	userRepository := dataaccess.NewGormUserRepository(db)

	artistService := services.ArtistService{Repo: artistRepository}
	cityService := services.CityService{Repo: cityRepository, LocationRepo: locationRepository}
	genreService := services.GenreService{Repo: genreRepository}
	locationService := services.LocationService{Repo: locationRepository, CityRepo: cityRepository}
	userService := services.UserService{Repo: userRepository}

	artistHandler := handlers.ArtistHandler{Service: &artistService}
	cityHandler := handlers.CityHandler{Service: &cityService}
	genreHandler := handlers.GenreHandler{Service: &genreService}
	locationHandler := handlers.LocationHandler{Service: &locationService}
	userHandler := handlers.UserHandler{Service: &userService}

	routes.RegisterArtistRoutes(router, &artistHandler)
	routes.RegisterCityRoutes(router, &cityHandler)
	routes.RegisterGenreRoutes(router, &genreHandler)
	routes.RegisterLocationRoutes(router, &locationHandler)
	routes.RegisterUserRoutes(router, &userHandler)

	err := router.Run("localhost:8080")
	if err != nil {
		log.Fatalf("Failed to run server: %v", err)
		return
	}
}
