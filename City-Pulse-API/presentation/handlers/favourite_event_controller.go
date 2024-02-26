package handlers

import (
	"City-Pulse-API/domain/entities"
	"City-Pulse-API/domain/services"
	"errors"
	"fmt"
	"github.com/gin-gonic/gin"
	"github.com/go-playground/validator/v10"
	"net/http"
	"strconv"
)

type FavouriteEventHandler struct {
	Service *services.FavouriteEventService
}

func (handler *FavouriteEventHandler) AllFavouriteEventAssociations(c *gin.Context) {
	favouriteEventAssociations, err := handler.Service.AllFavouriteEventAssociations()
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": "failed to fetch favourite event associations"})
		return
	}
	c.JSON(http.StatusOK, favouriteEventAssociations)
}

func (handler *FavouriteEventHandler) FavouriteEventAssociationByID(c *gin.Context) {
	id := c.Param("id")
	favouriteEventAssociation, err := handler.Service.FavouriteEventAssociationByID(id)
	if err != nil {
		if err.Error() == "invalid ID format" {
			c.JSON(http.StatusBadRequest, gin.H{"error": "invalid ID format"})
		} else {
			c.JSON(http.StatusNotFound, gin.H{"error": "favourite event association not found"})
		}
		return
	}
	c.JSON(http.StatusOK, favouriteEventAssociation)
}

func (handler *FavouriteEventHandler) FavouriteEventAssociation(c *gin.Context) {
	eventID := c.Query("eventId")
	userID := c.Query("userId")

	if eventID == "" || userID == "" {
		c.JSON(http.StatusBadRequest, gin.H{"error": "eventId and userId query parameters are required"})
		return
	}

	favouriteEventAssociation, err := handler.Service.FavouriteEventAssociation(eventID, userID)
	if err != nil {
		if err.Error() == "invalid ID format" {
			c.JSON(http.StatusBadRequest, gin.H{"error": "invalid ID format"})
		} else {
			c.JSON(http.StatusNotFound, gin.H{"error": "favourite event association not found"})
		}
		return
	}
	c.JSON(http.StatusOK, favouriteEventAssociation)
}

func (handler *FavouriteEventHandler) EventWithUsers(c *gin.Context) {
	eventID := c.Param("eventId")
	eventWithUsers, err := handler.Service.EventWithUsers(eventID)
	if err != nil {
		if err.Error() == "invalid ID format" {
			c.JSON(http.StatusBadRequest, gin.H{"error": "invalid ID format"})
		} else {
			c.JSON(http.StatusNotFound, gin.H{"error": err.Error()})
		}
		return
	}
	c.JSON(http.StatusOK, eventWithUsers)
}

func (handler *FavouriteEventHandler) UserWithEvents(c *gin.Context) {
	requestedID := c.Param("id")
	userIDInterface, _ := c.Get("userID")
	role, _ := c.Get("role")

	userIDFloat, _ := userIDInterface.(float64)

	var reqID uint
	_, err := fmt.Sscan(requestedID, &reqID)
	if err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "Invalid ID format"})
		return
	}

	if role == entities.NormalUser && uint(userIDFloat) != reqID {
		c.JSON(http.StatusForbidden, gin.H{"error": "Access denied"})
		return
	}

	userWithEvents, err := handler.Service.UserWithEvents(requestedID)
	if err != nil {
		fmt.Println("Error fetching events for user:", err)
		c.JSON(http.StatusNotFound, gin.H{"error": "User not found"})
		return
	}
	c.JSON(http.StatusOK, userWithEvents)
}

func (handler *FavouriteEventHandler) AddEventToFavourites(c *gin.Context) {
	var newFavouriteEventAssociation entities.FavouriteEvent

	if err := c.BindJSON(&newFavouriteEventAssociation); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}

	validate := validator.New()

	err := validate.Struct(newFavouriteEventAssociation)

	if err != nil {

		var invalidValidationError *validator.InvalidValidationError
		if errors.As(err, &invalidValidationError) {
			c.JSON(http.StatusInternalServerError, gin.H{"error": "Invalid validation error"})
			return
		}

		var errorMessages []string
		for _, err := range err.(validator.ValidationErrors) {
			errorMessage := "Validation error on field '" + err.Field() + "': " + err.ActualTag()
			if err.Param() != "" {
				errorMessage += " (Parameter: " + err.Param() + ")"
			}
			errorMessages = append(errorMessages, errorMessage)
		}

		c.JSON(http.StatusBadRequest, gin.H{"errors": errorMessages})
		return
	}

	requestedID := newFavouriteEventAssociation.UserID
	userIDInterface, _ := c.Get("userID")
	role, _ := c.Get("role")

	userIDFloat, _ := userIDInterface.(float64)

	var reqID uint
	_, err = fmt.Sscan(strconv.Itoa(int(requestedID)), &reqID)
	if err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "Invalid ID format"})
		return
	}

	if role == entities.NormalUser && uint(userIDFloat) != reqID {
		c.JSON(http.StatusForbidden, gin.H{"error": "Access denied"})
		return
	}

	favouriteEventAssociation, err := handler.Service.AddEventToFavourites(newFavouriteEventAssociation)
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}

	c.JSON(http.StatusCreated, favouriteEventAssociation)
}

func (handler *FavouriteEventHandler) DeleteEventFromFavourites(c *gin.Context) {
	id := c.Param("id")

	favouriteEventAssociation, err := handler.Service.FavouriteEventAssociationByID(id)

	requestedID := favouriteEventAssociation.User.ID
	userIDInterface, _ := c.Get("userID")
	role, _ := c.Get("role")

	userIDFloat, _ := userIDInterface.(float64)

	var reqID uint
	_, err = fmt.Sscan(strconv.Itoa(int(requestedID)), &reqID)
	if err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "Invalid ID format"})
		return
	}

	if role == entities.NormalUser && uint(userIDFloat) != reqID {
		c.JSON(http.StatusForbidden, gin.H{"error": "Access denied"})
		return
	}

	_, err = handler.Service.DeleteEventFromFavourites(id)

	if err != nil {
		c.JSON(http.StatusNotFound, gin.H{"error": "favourite event association not found"})
		return
	}

	c.JSON(http.StatusOK, favouriteEventAssociation)
}
