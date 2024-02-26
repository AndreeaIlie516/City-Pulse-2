package handlers

import (
	"City-Pulse-API/domain/entities"
	"City-Pulse-API/domain/services"
	"errors"
	"github.com/gin-gonic/gin"
	"github.com/go-playground/validator/v10"
	"net/http"
)

type EventArtistHandler struct {
	Service *services.EventArtistService
}

func (handler *EventArtistHandler) AllEventArtistAssociations(c *gin.Context) {
	eventArtistAssociations, err := handler.Service.AllEventArtistAssociations()
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": "failed to fetch event artist associations"})
		return
	}
	c.JSON(http.StatusOK, eventArtistAssociations)
}

func (handler *EventArtistHandler) EventArtistAssociationByID(c *gin.Context) {
	id := c.Param("id")
	eventArtistAssociation, err := handler.Service.EventArtistAssociationByID(id)
	if err != nil {
		if err.Error() == "invalid ID format" {
			c.JSON(http.StatusBadRequest, gin.H{"error": "invalid ID format"})
		} else {
			c.JSON(http.StatusNotFound, gin.H{"error": "event artist association not found"})
		}
		return
	}
	c.JSON(http.StatusOK, eventArtistAssociation)
}

func (handler *EventArtistHandler) EventArtistAssociation(c *gin.Context) {
	eventID := c.Query("eventId")
	artistID := c.Query("artistId")

	if eventID == "" || artistID == "" {
		c.JSON(http.StatusBadRequest, gin.H{"error": "eventId and artistId query parameters are required"})
		return
	}

	eventArtistAssociation, err := handler.Service.EventArtistAssociation(eventID, artistID)
	if err != nil {
		if err.Error() == "invalid ID format" {
			c.JSON(http.StatusBadRequest, gin.H{"error": "invalid ID format"})
		} else {
			c.JSON(http.StatusNotFound, gin.H{"error": "event artist association not found"})
		}
		return
	}
	c.JSON(http.StatusOK, eventArtistAssociation)
}

func (handler *EventArtistHandler) EventWithArtists(c *gin.Context) {
	eventID := c.Param("eventId")
	eventWithArtists, err := handler.Service.EventWithArtists(eventID)
	if err != nil {
		if err.Error() == "invalid ID format" {
			c.JSON(http.StatusBadRequest, gin.H{"error": "invalid ID format"})
		} else {
			c.JSON(http.StatusNotFound, gin.H{"error": err.Error()})
		}
		return
	}
	c.JSON(http.StatusOK, eventWithArtists)
}

func (handler *EventArtistHandler) ArtistWithEvents(c *gin.Context) {
	artistID := c.Param("artistId")
	artistWithEvents, err := handler.Service.ArtistWithEvents(artistID)
	if err != nil {
		if err.Error() == "invalid ID format" {
			c.JSON(http.StatusBadRequest, gin.H{"error": "invalid ID format"})
		} else {
			c.JSON(http.StatusNotFound, gin.H{"error": "artist id not found"})
		}
		return
	}
	c.JSON(http.StatusOK, artistWithEvents)
}

func (handler *EventArtistHandler) CreateEventArtistAssociation(c *gin.Context) {
	var newEventArtistAssociation entities.EventArtist

	if err := c.BindJSON(&newEventArtistAssociation); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}

	validate := validator.New()

	err := validate.Struct(newEventArtistAssociation)

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

	eventArtistAssociation, err := handler.Service.CreateEventArtistAssociation(newEventArtistAssociation)
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}

	c.JSON(http.StatusCreated, eventArtistAssociation)
}

func (handler *EventArtistHandler) DeleteEventArtistAssociation(c *gin.Context) {
	id := c.Param("id")

	eventArtistAssociation, err := handler.Service.DeleteEventArtistAssociation(id)

	if err != nil {
		c.JSON(http.StatusNotFound, gin.H{"error": "event artist association not found"})
		return
	}

	c.JSON(http.StatusOK, eventArtistAssociation)
}

func (handler *EventArtistHandler) UpdateEventArtistAssociation(c *gin.Context) {
	id := c.Param("id")

	var updatedEventArtistAssociation entities.EventArtist

	if err := c.BindJSON(&updatedEventArtistAssociation); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}

	validate := validator.New()

	err := validate.Struct(updatedEventArtistAssociation)

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

	eventArtistAssociation, err := handler.Service.UpdateEventArtistAssociation(id, updatedEventArtistAssociation)

	if err != nil {
		c.JSON(http.StatusNotFound, gin.H{"error": "artist event association not found"})
		return
	}

	c.JSON(http.StatusOK, eventArtistAssociation)
}
