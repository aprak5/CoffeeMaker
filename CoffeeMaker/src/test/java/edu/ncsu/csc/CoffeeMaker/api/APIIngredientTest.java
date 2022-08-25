package edu.ncsu.csc.CoffeeMaker.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
import edu.ncsu.csc.CoffeeMaker.services.IngredientService;

@RunWith ( SpringRunner.class )
@SpringBootTest
@AutoConfigureMockMvc
public class APIIngredientTest {

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
     * IngredientService object used for testing
     */
    @Autowired
    private IngredientService     service;

    /**
     * Sets up the tests.
     */
    @Before
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();
    }

    @Test
    @Transactional
    public void testIngredientAPI () throws Exception {

        service.deleteAll();

        final Ingredient ingredient = new Ingredient( "Coffee", 3 );

        mvc.perform( post( "/api/v1/ingredient" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredient ) ) );

        Assert.assertEquals( 1, (int) service.count() );

    }

    /**
     * Tests deleting a valid ingredient in the system
     *
     * @throws Exception
     *             if ingredient is not found
     */
    @Test
    @Transactional
    public void testDeleteIngredientAPI () throws Exception {
        service.deleteAll();

        final Ingredient ingredient = new Ingredient( "Coffee", 3 );

        mvc.perform( post( "/api/v1/ingredient" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredient ) ) );

        Assert.assertEquals( 1, (int) service.count() );

        mvc.perform( delete( "/api/v1/ingredient/Coffee" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredient ) ) ).andExpect( status().isOk() );

        Assert.assertEquals( 0, (int) service.count() );

    }

    /**
     * Tests deleting a ingredient that is not in the system when no ingredients
     * are in the system.
     *
     * @throws Exception
     *             if ingredient is invalidly deleted
     */
    @Test
    @Transactional
    public void testDeleteIngredient2 () throws Exception {
        service.deleteAll();

        final Ingredient ingredient = new Ingredient( "Coffee", 3 );

        mvc.perform( delete( "/api/v1/ingredient/Coffee" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredient ) ) ).andExpect( status().isNotFound() );

        Assert.assertEquals( 0, (int) service.count() );
    }

    /**
     * Tests deleting a ingredient that is not in the system when there are
     * ingredients present.
     *
     * @throws Exception
     *             if ingredient is invalidly deleted
     */
    @Test
    @Transactional
    public void testDeleteIngredientAPI3 () throws Exception {
        service.deleteAll();

        final Ingredient ingredient = new Ingredient( "Coffee", 3 );

        mvc.perform( post( "/api/v1/ingredient" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredient ) ) );

        Assert.assertEquals( 1, (int) service.count() );

        mvc.perform( delete( "/api/v1/ingredient/Sugar" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredient ) ) ).andExpect( status().isNotFound() );

        Assert.assertEquals( 1, (int) service.count() );

    }

    /**
     * Tests adding multiple ingredients to the system
     */
    @Test
    @Transactional
    public void testAddIngredients () throws Exception {
        service.deleteAll();

        final Ingredient ingredient = new Ingredient( "Coffee", 3 );

        mvc.perform( post( "/api/v1/ingredient" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredient ) ) );

        Assert.assertEquals( 1, (int) service.count() );

        final Ingredient ingredient2 = new Ingredient( "Sugar", 2 );

        mvc.perform( post( "/api/v1/ingredient" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredient2 ) ) );

        Assert.assertEquals( 2, (int) service.count() );

        final Ingredient ingredient3 = new Ingredient( "Milk", 4 );

        mvc.perform( post( "/api/v1/ingredient" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredient3 ) ) );

        Assert.assertEquals( 3, (int) service.count() );

    }

    /**
     * Tests updating an ingredient that is already in the system
     */
    @Test
    @Transactional
    public void testUpdateIngredient () throws Exception {
        service.deleteAll();

        final Ingredient ingredient = new Ingredient( "Coffee", 3 );

        mvc.perform( post( "/api/v1/ingredient" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredient ) ) );

        Assert.assertEquals( 1, (int) service.count() );

        ingredient.setAmount( 10 );

        mvc.perform( put( "/api/v1/ingredient/Coffee" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredient ) ) ).andExpect( status().isOk() );

        Assert.assertEquals( 1, (int) service.count() );

        final Ingredient dbIngredient = service.findByName( "Coffee" );
        Assert.assertEquals( 10, (int) dbIngredient.getAmount() );

    }

    /**
     * Tests updating an ingredient that is not in the system.
     */
    @Test
    @Transactional
    public void testUpdateIngredient2 () throws Exception {
        service.deleteAll();

        final Ingredient ingredient = new Ingredient( "Coffee", 3 );

        mvc.perform( post( "/api/v1/ingredient" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredient ) ) );

        Assert.assertEquals( 1, (int) service.count() );

        final Ingredient ingredient2 = new Ingredient( "Sugar", 4 );

        mvc.perform( put( "/api/v1/ingredient/Sugar" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredient2 ) ) ).andExpect( status().isNotFound() );

        Assert.assertEquals( 1, (int) service.count() );

    }

    /**
     * Tests that getting all the ingredients returns a list of ingredients.
     */
    @Test
    @Transactional
    public void testGetIngredients () throws Exception {
        service.deleteAll();

        final Ingredient ingredient = new Ingredient( "Coffee", 3 );

        mvc.perform( post( "/api/v1/ingredient" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredient ) ) );

        Assert.assertEquals( 1, (int) service.count() );

        final Ingredient ingredient2 = new Ingredient( "Sugar", 4 );

        mvc.perform( post( "/api/v1/ingredient" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredient2 ) ) );

        Assert.assertEquals( 2, (int) service.count() );

        final Ingredient ingredient3 = new Ingredient( "Milk", 2 );

        mvc.perform( post( "/api/v1/ingredient" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredient3 ) ) );

        Assert.assertEquals( 3, (int) service.count() );

        final String list = mvc.perform( get( "/api/v1/ingredient" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue( list.contains( "Coffee" ) );
        Assert.assertTrue( list.contains( "Sugar" ) );
        Assert.assertTrue( list.contains( "Milk" ) );
        Assert.assertFalse( list.contains( "Chocolate" ) );

    }
}
