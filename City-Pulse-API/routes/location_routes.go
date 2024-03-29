package routes

import (
	"City-Pulse-API/presentation/handlers"
	"github.com/gin-gonic/gin"
)

func RegisterLocationRoutes(router *gin.Engine, locationHandler *handlers.LocationHandler) {
	locationGroup := router.Group("/locations")
	{
		locationGroup.GET("/", locationHandler.AllLocations)
		locationGroup.GET("/:id", locationHandler.LocationByID)
		locationGroup.GET("/city/:cityId", locationHandler.LocationsByCityID)
		locationGroup.POST("/", locationHandler.CreateLocation)
		locationGroup.PUT("/:id", locationHandler.UpdateLocation)
		locationGroup.DELETE("/:id", locationHandler.DeleteLocation)
	}
}
