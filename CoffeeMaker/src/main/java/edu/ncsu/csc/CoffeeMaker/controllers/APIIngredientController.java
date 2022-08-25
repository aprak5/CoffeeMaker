package edu.ncsu.csc.CoffeeMaker.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.services.IngredientService;

@SuppressWarnings ( "rawtypes" )
@RestController
public class APIIngredientController extends APIController {
    /**
     * IngredientService object, to be autowired in by Spring to allow for
     * manipulating the Inventory model
     */
    @Autowired
    private IngredientService service;

    @GetMapping ( BASE_PATH + "/ingredient" )
    public List<Ingredient> getIngredients () {
        return service.findAll();
    }

    @PostMapping ( BASE_PATH + "/ingredient" )
    public ResponseEntity createRecipeIngredient ( @RequestBody final Ingredient ingredient ) {
        final Ingredient ing = service.findByName( ingredient.getName() );

        if ( ing != null ) {
            return new ResponseEntity( HttpStatus.CONFLICT );
        }

        try {
            service.save( ingredient );
            return new ResponseEntity( HttpStatus.CREATED );
        }
        catch ( final Exception e ) {
            return new ResponseEntity( HttpStatus.BAD_REQUEST );
        }
    }

    @PutMapping ( BASE_PATH + "/ingredient/{name}" )
    public ResponseEntity updateIngredient ( @RequestBody final Ingredient ingredient ) {
        final Ingredient ing = service.findByName( ingredient.getName() );

        if ( ing == null ) {
            return new ResponseEntity( HttpStatus.NOT_FOUND );
        }

        ing.setAmount( ingredient.getAmount() );
        ing.setName( ingredient.getName() );

        try {
            service.save( ing );
            return new ResponseEntity( HttpStatus.OK );
        }
        catch ( final Exception e ) {
            return new ResponseEntity( HttpStatus.BAD_REQUEST );
        }

    }

    @DeleteMapping ( BASE_PATH + "/ingredient/{name}" )
    public ResponseEntity deleteIngredient ( @PathVariable final String name ) {
        final Ingredient ing = service.findByName( name );
        if ( null == ing ) {
            return new ResponseEntity( HttpStatus.NOT_FOUND );
        }

        try {
            service.delete( ing );
            return new ResponseEntity( HttpStatus.OK );
        }
        catch ( final Exception e ) {
            return new ResponseEntity( HttpStatus.BAD_REQUEST );
        }
    }
}
