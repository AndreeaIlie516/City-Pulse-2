package routes

import (
	"City-Pulse-API/domain/entities"
	"City-Pulse-API/infrastructure/middlewares"
	"City-Pulse-API/presentation/handlers"
	"github.com/gin-gonic/gin"
)

func RegisterUserRoutes(router *gin.Engine, userHandler *handlers.UserHandler, roleMiddleware middlewares.IAuthMiddleware) {
	userGroup := router.Group("/users")
	{
		userGroup.GET("/", roleMiddleware.RequireRole(entities.Admin), userHandler.AllUsers)
		userGroup.GET("/:id", roleMiddleware.RequireRole(entities.NormalUser), userHandler.UserByID)
		userGroup.POST("/register", userHandler.Register)
		userGroup.POST("/login", userHandler.Login)
		userGroup.PUT("/:id", roleMiddleware.RequireRole(entities.NormalUser), userHandler.UpdateUser)
		userGroup.DELETE("/:id", roleMiddleware.RequireRole(entities.NormalUser), userHandler.DeleteUser)
	}
}
