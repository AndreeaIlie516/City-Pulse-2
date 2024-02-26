package routes

import (
	"City-Pulse-API/presentation/handlers"
	"github.com/gin-gonic/gin"
)

func RegisterGenreRoutes(router *gin.Engine, genreHandler *handlers.GenreHandler) {
	genreGroup := router.Group("/genres")
	{
		genreGroup.GET("/", genreHandler.AllGenres)
		genreGroup.GET("/:id", genreHandler.GenreByID)
		genreGroup.POST("/", genreHandler.CreateGenre)
		genreGroup.PUT("/:id", genreHandler.UpdateGenre)
		genreGroup.DELETE("/:id", genreHandler.DeleteGenre)
	}
}
