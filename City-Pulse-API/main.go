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
		&entities.Genre{},
		&entities.User{},
	}

	for _, entity := range entitiesToMigrate {
		err := db.AutoMigrate(entity)
		if err != nil {
			log.Fatalf("Failed to migrate database: %v", err)
		}
	}

	artistRepository := dataaccess.NewGormArtistRepository(db)
	genreRepository := dataaccess.NewGormGenreRepository(db)
	userRepository := dataaccess.NewGormUserRepository(db)

	artistService := services.ArtistService{Repo: artistRepository}
	genreService := services.GenreService{Repo: genreRepository}
	userService := services.UserService{Repo: userRepository}

	artistHandler := handlers.ArtistHandler{Service: &artistService}
	genreHandler := handlers.GenreHandler{Service: &genreService}
	userHandler := handlers.UserHandler{Service: &userService}

	routes.RegisterArtistRoutes(router, &artistHandler)
	routes.RegisterGenreRoutes(router, &genreHandler)
	routes.RegisterUserRoutes(router, &userHandler)

	err := router.Run("localhost:8080")
	if err != nil {
		log.Fatalf("Failed to run server: %v", err)
		return
	}
}
