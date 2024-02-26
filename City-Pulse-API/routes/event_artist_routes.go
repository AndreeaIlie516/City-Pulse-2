package routes

import (
	"City-Pulse-API/presentation/handlers"
	"github.com/gin-gonic/gin"
)

func RegisterEventArtistRoutes(router *gin.Engine, eventArtistHandler *handlers.EventArtistHandler) {
	eventArtistGroup := router.Group("/event-artist")
	{
		eventArtistGroup.GET("/", eventArtistHandler.AllEventArtistAssociations)
		eventArtistGroup.GET("/:id", eventArtistHandler.EventArtistAssociationByID)
		eventArtistGroup.GET("/associationByEventAndArtist", eventArtistHandler.EventArtistAssociation)
		eventArtistGroup.GET("/event/:eventId", eventArtistHandler.EventWithArtists)
		eventArtistGroup.GET("/artist/:artistId", eventArtistHandler.ArtistWithEvents)
		eventArtistGroup.POST("/", eventArtistHandler.CreateEventArtistAssociation)
		eventArtistGroup.DELETE("/:id", eventArtistHandler.DeleteEventArtistAssociation)
		eventArtistGroup.PUT("/:id", eventArtistHandler.UpdateEventArtistAssociation)
	}
}
