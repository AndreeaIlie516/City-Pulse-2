package routes

import (
	"City-Pulse-API/domain/entities"
	"City-Pulse-API/infrastructure/middlewares"
	"City-Pulse-API/presentation/handlers"
	"github.com/gin-gonic/gin"
)

func RegisterFavouriteEventRoutes(router *gin.Engine, favouriteEventHandler *handlers.FavouriteEventHandler, roleMiddleware middlewares.IAuthMiddleware) {
	eventArtistGroup := router.Group("/favourite-events")
	{
		eventArtistGroup.GET("/", roleMiddleware.RequireRole(entities.Admin), favouriteEventHandler.AllFavouriteEventAssociations)
		eventArtistGroup.GET("/:id", roleMiddleware.RequireRole(entities.Admin), favouriteEventHandler.FavouriteEventAssociationByID)
		eventArtistGroup.GET("/associationByEventAndUser", roleMiddleware.RequireRole(entities.Admin), favouriteEventHandler.FavouriteEventAssociation)
		eventArtistGroup.GET("/event/:eventId", roleMiddleware.RequireRole(entities.Admin), favouriteEventHandler.EventWithUsers)
		eventArtistGroup.GET("/user/:userId", roleMiddleware.RequireRole(entities.NormalUser), favouriteEventHandler.UserWithEvents)
		eventArtistGroup.POST("/", roleMiddleware.RequireRole(entities.NormalUser), favouriteEventHandler.AddEventToFavourites)
		eventArtistGroup.DELETE("/:id", roleMiddleware.RequireRole(entities.NormalUser), favouriteEventHandler.DeleteEventFromFavourites)
	}
}
