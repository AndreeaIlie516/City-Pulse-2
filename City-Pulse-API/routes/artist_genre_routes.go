package routes

import (
	"City-Pulse-API/presentation/handlers"
	"github.com/gin-gonic/gin"
)

func RegisterArtistGenreRoutes(router *gin.Engine, artistGenreHandler *handlers.ArtistGenreHandler) {
	artistGenreGroup := router.Group("/artist-genre")
	{
		artistGenreGroup.GET("/", artistGenreHandler.AllArtistGenreAssociations)
		artistGenreGroup.GET("/:id", artistGenreHandler.ArtistGenreAssociationByID)
		artistGenreGroup.GET("/associationByArtistAndGenre", artistGenreHandler.ArtistGenreAssociation)
		artistGenreGroup.GET("/artist/:artistId", artistGenreHandler.ArtistWithGenre)
		artistGenreGroup.GET("/genre/:genreId", artistGenreHandler.GenreWithArtist)
		artistGenreGroup.POST("/", artistGenreHandler.CreateArtistGenreAssociation)
		artistGenreGroup.DELETE("/:id", artistGenreHandler.DeleteArtistGenreAssociation)
		artistGenreGroup.PUT("/:id", artistGenreHandler.UpdateArtistGenreAssociation)
	}
}
