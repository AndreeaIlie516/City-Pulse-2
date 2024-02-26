package routes

import (
	"City-Pulse-API/presentation/handlers"
	"github.com/gin-gonic/gin"
)

func RegisterEventRoutes(router *gin.Engine, eventHandler *handlers.EventHandler) {
	eventGroup := router.Group("/events")
	{
		eventGroup.GET("/", eventHandler.AllEvents)
		eventGroup.GET("/:id", eventHandler.EventByID)
		eventGroup.GET("/location/:locationId", eventHandler.EventsByLocationID)
		eventGroup.GET("/city/:cityId", eventHandler.EventsByCityID)
		eventGroup.POST("/", eventHandler.CreateEvent)
		eventGroup.PUT("/:id", eventHandler.UpdateEvent)
		eventGroup.DELETE("/:id", eventHandler.DeleteEvent)
	}
}
