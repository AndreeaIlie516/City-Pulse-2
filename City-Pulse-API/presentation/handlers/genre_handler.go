package handlers

import (
	"City-Pulse-API/domain/entities"
	"City-Pulse-API/domain/services"
	"errors"
	"github.com/gin-gonic/gin"
	"github.com/go-playground/validator/v10"
	"net/http"
)

type GenreHandler struct {
	Service *services.GenreService
}

func (handler *GenreHandler) AllGenres(c *gin.Context) {
	genres, err := handler.Service.AllGenres()
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": "failed to fetch genres"})
		return
	}
	c.JSON(http.StatusOK, genres)
}

func (handler *GenreHandler) GenreByID(c *gin.Context) {
	id := c.Param("id")
	genre, err := handler.Service.GenreByID(id)
	if err != nil {
		if err.Error() == "invalid ID format" {
			c.JSON(http.StatusBadRequest, gin.H{"error": "invalid ID format"})
		} else {
			c.JSON(http.StatusNotFound, gin.H{"error": "genre not found"})
		}
		return
	}
	c.JSON(http.StatusOK, genre)
}

func (handler *GenreHandler) CreateGenre(c *gin.Context) {
	var newGenre entities.Genre

	if err := c.BindJSON(&newGenre); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}

	validate := validator.New()

	err := validate.Struct(newGenre)

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

	genre, err := handler.Service.CreateGenre(newGenre)
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": "failed to create genre"})
		return
	}

	c.JSON(http.StatusCreated, genre)
}

func (handler *GenreHandler) DeleteGenre(c *gin.Context) {
	id := c.Param("id")

	genre, err := handler.Service.DeleteGenre(id)

	if err != nil {
		c.JSON(http.StatusNotFound, gin.H{"error": "genre not found"})
		return
	}

	c.JSON(http.StatusOK, genre)
}

func (handler *GenreHandler) UpdateGenre(c *gin.Context) {
	id := c.Param("id")

	var updatedGenre entities.Genre

	if err := c.BindJSON(&updatedGenre); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}

	validate := validator.New()

	err := validate.Struct(updatedGenre)

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

	genre, err := handler.Service.UpdateGenre(id, updatedGenre)

	if err != nil {
		c.JSON(http.StatusNotFound, gin.H{"error": "genre not found"})
		return
	}

	c.JSON(http.StatusOK, genre)
}
