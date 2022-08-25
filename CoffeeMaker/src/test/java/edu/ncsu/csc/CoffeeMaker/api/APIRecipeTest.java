package edu.ncsu.csc.CoffeeMaker.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

/**
 * API tests for recipe functionality
 *
 * @author Lydia Pearson (lrpears2)
 */
@RunWith ( SpringRunner.class )
@SpringBootTest
@AutoConfigureMockMvc
public class APIRecipeTest {

    /**
     * MockMvc uses Spring's testing framework to handle requests to the REST
     * API
     */
    private MockMvc               mvc;

    /**
     * WebApplicationContext object used for testing
     */
    @Autowired
    private WebApplicationContext context;

    /**
     * RecipeService object used for testing
     */
    @Autowired
    private RecipeService         service;

    /**
     * Sets up the tests.
     */
    @Before
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();
    }

    @Test
    @Transactional
    public void ensureRecipe () throws Exception {
        service.deleteAll();

        final Recipe r = new Recipe();
        final Ingredient i1 = new Ingredient( "Chocolate", 5 );
        r.addIngredient( i1 );
        final Ingredient i2 = new Ingredient( "Coffee", 3 );
        r.addIngredient( i2 );
        final Ingredient i3 = new Ingredient( "Milk", 4 );
        r.addIngredient( i3 );
        final Ingredient i4 = new Ingredient( "Sugar", 8 );
        r.addIngredient( i4 );
        r.setPrice( 10 );
        r.setName( "Mocha" );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r ) ) ).andExpect( status().isOk() );

    }

    @Test
    @Transactional
    public void testRecipeAPI () throws Exception {

        service.deleteAll();

        final Recipe recipe = new Recipe();
        recipe.setName( "Delicious Not-Coffee" );
        final Ingredient i1 = new Ingredient( "Chocolate", 10 );
        recipe.addIngredient( i1 );
        final Ingredient i2 = new Ingredient( "Milk", 20 );
        recipe.addIngredient( i2 );
        final Ingredient i3 = new Ingredient( "Sugar", 5 );
        recipe.addIngredient( i3 );
        final Ingredient i4 = new Ingredient( "Coffee", 1 );
        recipe.addIngredient( i4 );

        recipe.setPrice( 5 );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( recipe ) ) );

        Assert.assertEquals( 1, (int) service.count() );

    }

    /**
     * Tests deleting a valid recipe in the system
     *
     * @throws Exception
     *             if recipe is not found
     */
    @Test
    @Transactional
    public void testDeleteRecipeAPI () throws Exception {
        service.deleteAll();

        final Recipe recipe = new Recipe();
        recipe.setName( "Delicious Not-Coffee" );
        final Ingredient i1 = new Ingredient( "Chocolate", 10 );
        recipe.addIngredient( i1 );
        final Ingredient i2 = new Ingredient( "Milk", 20 );
        recipe.addIngredient( i2 );
        final Ingredient i3 = new Ingredient( "Sugar", 5 );
        recipe.addIngredient( i3 );
        final Ingredient i4 = new Ingredient( "Coffee", 1 );
        recipe.addIngredient( i4 );

        recipe.setPrice( 5 );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( recipe ) ) );

        Assert.assertEquals( 1, (int) service.count() );

        mvc.perform( delete( "/api/v1/recipes/Delicious Not-Coffee" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( recipe ) ) ).andExpect( status().isOk() );

        Assert.assertEquals( 0, (int) service.count() );

    }

    /**
     * Tests deleting a recipe that is not in the system when no recipes are in
     * the system. Should return a not found error.
     *
     * @throws Exception
     *             if recipe is invalidly deleted
     */
    @Test
    @Transactional
    public void testDeleteRecipe2 () throws Exception {
        service.deleteAll();

        final Recipe recipe = new Recipe();
        recipe.setName( "Delicious Not-Coffee" );
        final Ingredient i1 = new Ingredient( "Chocolate", 10 );
        recipe.addIngredient( i1 );
        final Ingredient i2 = new Ingredient( "Milk", 20 );
        recipe.addIngredient( i2 );
        final Ingredient i3 = new Ingredient( "Sugar", 5 );
        recipe.addIngredient( i3 );
        final Ingredient i4 = new Ingredient( "Coffee", 1 );
        recipe.addIngredient( i4 );

        recipe.setPrice( 5 );

        mvc.perform( delete( "/api/v1/recipes/Delicious Not-Coffee" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( recipe ) ) ).andExpect( status().isNotFound() );

        Assert.assertEquals( 0, (int) service.count() );
    }

    /**
     * Tests deleting a recipe that is not in the system when there are recipes
     * present. Should get a not found error
     *
     * @throws Exception
     *             if recipe is invalidly deleted
     */
    @Test
    @Transactional
    public void testDeleteRecipeAPI3 () throws Exception {
        service.deleteAll();

        final Recipe recipe = new Recipe();
        recipe.setName( "Delicious Not-Coffee" );
        final Ingredient i1 = new Ingredient( "Chocolate", 10 );
        recipe.addIngredient( i1 );
        final Ingredient i2 = new Ingredient( "Milk", 20 );
        recipe.addIngredient( i2 );
        final Ingredient i3 = new Ingredient( "Sugar", 5 );
        recipe.addIngredient( i3 );
        final Ingredient i4 = new Ingredient( "Coffee", 1 );
        recipe.addIngredient( i4 );

        recipe.setPrice( 5 );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( recipe ) ) );

        Assert.assertEquals( 1, (int) service.count() );

        mvc.perform( delete( "/api/v1/recipes/Coffee" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( recipe ) ) ).andExpect( status().isNotFound() );

        Assert.assertEquals( 1, (int) service.count() );

    }

    @Test
    @Transactional
    public void testAddRecipe2 () throws Exception {

        /* Tests a recipe with a duplicate name to make sure it's rejected */

        Assert.assertEquals( "There should be no Recipes in the CoffeeMaker", 0, service.findAll().size() );
        final String name = "Coffee";
        final Recipe r1 = createRecipe( name, 50, 3, 1, 1, 0 );

        service.save( r1 );

        final Recipe r2 = createRecipe( name, 50, 3, 1, 1, 0 );
        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r2 ) ) ).andExpect( status().is4xxClientError() );

        Assert.assertEquals( "There should only one recipe in the CoffeeMaker", 1, service.findAll().size() );
    }

    @Test
    @Transactional
    public void testAddRecipe15 () throws Exception {

        /* Tests to make sure that our cap of 3 recipes is enforced */

        Assert.assertEquals( "There should be no Recipes in the CoffeeMaker", 0, service.findAll().size() );

        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        service.save( r1 );
        final Recipe r2 = createRecipe( "Mocha", 50, 3, 1, 1, 2 );
        service.save( r2 );
        final Recipe r3 = createRecipe( "Latte", 60, 3, 2, 2, 0 );
        service.save( r3 );

        Assert.assertEquals( "Creating three recipes should result in three recipes in the database", 3,
                service.count() );

        final Recipe r4 = createRecipe( "Hot Chocolate", 75, 0, 2, 1, 2 );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r4 ) ) ).andExpect( status().isInsufficientStorage() );

        Assert.assertEquals( "Creating a fourth recipe should not get saved", 3, service.count() );
    }

    private Recipe createRecipe ( final String name, final Integer price, final Integer coffee, final Integer milk,
            final Integer sugar, final Integer chocolate ) {
        final Recipe recipe = new Recipe();
        recipe.setName( name );
        recipe.setPrice( price );
        final Ingredient i1 = new Ingredient( "Coffee", coffee );
        recipe.addIngredient( i1 );
        final Ingredient i2 = new Ingredient( "Milk", milk );
        recipe.addIngredient( i2 );
        final Ingredient i3 = new Ingredient( "Sugar", sugar );
        recipe.addIngredient( i3 );
        final Ingredient i4 = new Ingredient( "Chocolate", chocolate );
        recipe.addIngredient( i4 );

        return recipe;
    }

}
