package routes

import (
	"City-Pulse-API/presentation/handlers"
	"github.com/gin-gonic/gin"
)

func RegisterUserRoutes(router *gin.Engine, userHandler *handlers.UserHandler) {
	userGroup := router.Group("/users")
	{
		userGroup.GET("/", userHandler.AllUsers)
		userGroup.GET("/:id", userHandler.UserByID)
		userGroup.POST("/", userHandler.CreateUser)
		userGroup.PUT("/:id", userHandler.UpdateUser)
		userGroup.DELETE("/:id", userHandler.DeleteUser)
	}
}
