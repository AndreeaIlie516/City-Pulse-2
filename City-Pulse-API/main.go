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
		&entities.User{},
	}

	for _, entity := range entitiesToMigrate {
		err := db.AutoMigrate(entity)
		if err != nil {
			log.Fatalf("Failed to migrate database: %v", err)
		}
	}

	userRepository := dataaccess.NewGormUserRepository(db)

	userService := services.UserService{Repo: userRepository}

	userHandler := handlers.UserHandler{Service: &userService}

	routes.RegisterUserRoutes(router, &userHandler)

	err := router.Run("localhost:8080")
	if err != nil {
		log.Fatalf("Failed to run server: %v", err)
		return
	}
}
