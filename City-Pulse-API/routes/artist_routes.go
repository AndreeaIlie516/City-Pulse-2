package routes

import (
	"City-Pulse-API/presentation/handlers"
	"github.com/gin-gonic/gin"
)

func RegisterArtistRoutes(router *gin.Engine, artistHandler *handlers.ArtistHandler) {
	artistGroup := router.Group("/artists")
	{
		artistGroup.GET("/", artistHandler.AllArtists)
		artistGroup.GET("/:id", artistHandler.ArtistByID)
		artistGroup.POST("/", artistHandler.CreateArtist)
		artistGroup.PUT("/:id", artistHandler.UpdateArtist)
		artistGroup.DELETE("/:id", artistHandler.DeleteArtist)
	}
}
