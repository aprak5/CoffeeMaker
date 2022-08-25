package edu.ncsu.csc.CoffeeMaker.unit;

import java.util.List;

import javax.validation.ConstraintViolationException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@RunWith ( SpringRunner.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class RecipeTest {

    @Autowired
    private RecipeService service;

    @Before
    public void setup () {
        service.deleteAll();
    }

    @Test
    @Transactional
    public void testAddRecipe () {

        final Recipe r1 = new Recipe();
        r1.setName( "Black Coffee" );
        r1.setPrice( 1 );
        r1.addIngredient( "Coffee", 1 );
        service.save( r1 );

        final Recipe r2 = new Recipe();
        r2.setName( "Mocha" );
        r2.setPrice( 1 );
        final Ingredient i2 = new Ingredient( "Coffee", 1 );
        r2.addIngredient( i2 );
        final Ingredient i3 = new Ingredient( "Milk", 1 );
        r2.addIngredient( i3 );
        final Ingredient i4 = new Ingredient( "Sugar", 1 );
        r2.addIngredient( i4 );
        final Ingredient i5 = new Ingredient( "Chocolate", 1 );
        r2.addIngredient( i5 );
        service.save( r2 );

        final List<Recipe> recipes = service.findAll();
        Assert.assertEquals( "Creating two recipes should result in two recipes in the database", 2, recipes.size() );

        Assert.assertEquals( "The retrieved recipe should match the created one", r1, recipes.get( 0 ) );
    }

    @Test
    @Transactional
    public void testNoRecipes () {
        Assert.assertEquals( "There should be no Recipes in the CoffeeMaker", 0, service.findAll().size() );

        final Recipe r1 = new Recipe();
        r1.setName( "Tasty Drink" );
        r1.setPrice( 12 );

        try {
            final Ingredient i1 = new Ingredient( "Coffee", -12 );
            r1.addIngredient( i1 );
            service.save( r1 );
            Assert.assertEquals(
                    "Trying to save a collection of elements where one is invalid should result in neither getting saved",
                    0, service.count() );
        }
        catch ( final IllegalArgumentException e ) {
            // Expected. There was an ingredient with an invalid amount.
        }

    }

    @Test
    @Transactional
    public void testAddRecipe1 () {

        Assert.assertEquals( "There should be no Recipes in the CoffeeMaker", 0, service.findAll().size() );
        final String name = "Coffee";
        final Recipe r1 = createRecipe( name, 50, 3, 1, 1, 0 );

        service.save( r1 );

        Assert.assertEquals( "There should only one recipe in the CoffeeMaker", 1, service.findAll().size() );
        Assert.assertNotNull( service.findByName( name ) );

    }

    /* Test2 is done via the API for different validation */

    @Test
    @Transactional
    public void testAddRecipe3 () {
        Assert.assertEquals( "There should be no Recipes in the CoffeeMaker", 0, service.findAll().size() );
        final String name = "Coffee";
        final Recipe r1 = createRecipe( name, -50, 3, 1, 1, 0 );

        try {
            service.save( r1 );

            Assert.assertNull( "A recipe was able to be created with a negative price", service.findByName( name ) );
        }
        catch ( final ConstraintViolationException cvee ) {
            // expected
        }

    }

    @Test
    @Transactional
    public void testAddRecipe4 () {
        Assert.assertEquals( "There should be no Recipes in the CoffeeMaker", 0, service.findAll().size() );
        final String name = "Coffee";

        try {
            final Recipe r1 = createRecipe( name, 50, -3, 1, 1, 2 );
            service.save( r1 );

            Assert.assertNull( "A recipe was able to be created with a negative amount of coffee",
                    service.findByName( name ) );
        }
        catch ( final IllegalArgumentException e ) {
            // expected
        }

    }

    @Test
    @Transactional
    public void testAddRecipe5 () {
        Assert.assertEquals( "There should be no Recipes in the CoffeeMaker", 0, service.findAll().size() );
        final String name = "Coffee";

        try {
            final Recipe r1 = createRecipe( name, 50, 3, -1, 1, 2 );
            service.save( r1 );

            Assert.assertNull( "A recipe was able to be created with a negative amount of milk",
                    service.findByName( name ) );
        }
        catch ( final IllegalArgumentException e ) {
            // expected
        }

    }

    @Test
    @Transactional
    public void testAddRecipe6 () {
        Assert.assertEquals( "There should be no Recipes in the CoffeeMaker", 0, service.findAll().size() );
        final String name = "Coffee";

        try {
            final Recipe r1 = createRecipe( name, 50, 3, 1, -1, 2 );
            service.save( r1 );

            Assert.assertNull( "A recipe was able to be created with a negative amount of sugar",
                    service.findByName( name ) );
        }
        catch ( final IllegalArgumentException e ) {
            // expected
        }

    }

    @Test
    @Transactional
    public void testAddRecipe7 () {
        Assert.assertEquals( "There should be no Recipes in the CoffeeMaker", 0, service.findAll().size() );
        final String name = "Coffee";

        try {
            final Recipe r1 = createRecipe( name, 50, 3, 1, 1, -2 );
            service.save( r1 );

            Assert.assertNull( "A recipe was able to be created with a negative amount of chocolate",
                    service.findByName( name ) );
        }
        catch ( final IllegalArgumentException e ) {
            // expected
        }

    }

    @Test
    @Transactional
    public void testAddRecipe13 () {
        Assert.assertEquals( "There should be no Recipes in the CoffeeMaker", 0, service.findAll().size() );

        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        service.save( r1 );
        final Recipe r2 = createRecipe( "Mocha", 50, 3, 1, 1, 2 );
        service.save( r2 );

        Assert.assertEquals( "Creating two recipes should result in two recipes in the database", 2, service.count() );

    }

    @Test
    @Transactional
    public void testAddRecipe14 () {
        Assert.assertEquals( "There should be no Recipes in the CoffeeMaker", 0, service.findAll().size() );

        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        service.save( r1 );
        final Recipe r2 = createRecipe( "Mocha", 50, 3, 1, 1, 2 );
        service.save( r2 );
        final Recipe r3 = createRecipe( "Latte", 60, 3, 2, 2, 0 );
        service.save( r3 );

        Assert.assertEquals( "Creating three recipes should result in three recipes in the database", 3,
                service.count() );

    }

    /**
     * Tests updating a recipe with values initialized to 0
     *
     * @author Lydia Pearson (lrpears2)
     */
    @Test
    @Transactional
    public void testUpdateRecipe () {
        Assert.assertEquals( "There should be no Recipes in the CoffeeMaker", 0, service.findAll().size() );

        final Recipe drink = new Recipe();
        Assert.assertEquals( "", drink.toString() );

        drink.setPrice( 15 );
        Assert.assertTrue( drink.checkRecipe() );

        final Recipe newDrink = createRecipe( "Frappe", 25, 1, 3, 2, 0 );
        drink.updateRecipe( newDrink );
        Assert.assertFalse( drink.checkRecipe() );
        Assert.assertTrue( 25 == drink.getPrice() );
        Assert.assertTrue( 1 == drink.getIngredient( "Coffee" ).getAmount() );
        Assert.assertTrue( 3 == drink.getIngredient( "Milk" ).getAmount() );
        Assert.assertTrue( 2 == drink.getIngredient( "Sugar" ).getAmount() );
        Assert.assertTrue( 0 == drink.getIngredient( "Chocolate" ).getAmount() );

    }

    @Test
    @Transactional
    public void testDeleteRecipe1 () {
        Assert.assertEquals( "There should be no Recipes in the CoffeeMaker", 0, service.findAll().size() );

        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        service.save( r1 );

        Assert.assertEquals( "There should be one recipe in the database", 1, service.count() );

        service.delete( r1 );
        Assert.assertEquals( "There should be no Recipes in the CoffeeMaker", 0, service.findAll().size() );
    }

    @Test
    @Transactional
    public void testDeleteRecipe2 () {
        Assert.assertEquals( "There should be no Recipes in the CoffeeMaker", 0, service.findAll().size() );

        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        service.save( r1 );
        final Recipe r2 = createRecipe( "Mocha", 50, 3, 1, 1, 2 );
        service.save( r2 );
        final Recipe r3 = createRecipe( "Latte", 60, 3, 2, 2, 0 );
        service.save( r3 );

        Assert.assertEquals( "There should be three recipes in the database", 3, service.count() );

        service.deleteAll();

        Assert.assertEquals( "`service.deleteAll()` should remove everything", 0, service.count() );

    }

    @Test
    @Transactional
    public void testEditRecipe1 () {
        Assert.assertEquals( "There should be no Recipes in the CoffeeMaker", 0, service.findAll().size() );

        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        service.save( r1 );

        r1.setPrice( 70 );

        service.save( r1 );

        final Recipe retrieved = service.findByName( "Coffee" );

        Assert.assertEquals( 70, (int) retrieved.getPrice() );
        Assert.assertEquals( 3, (int) retrieved.getIngredient( "Coffee" ).getAmount() );
        Assert.assertEquals( 1, (int) retrieved.getIngredient( "Milk" ).getAmount() );
        Assert.assertEquals( 1, (int) retrieved.getIngredient( "Sugar" ).getAmount() );
        Assert.assertEquals( 0, (int) retrieved.getIngredient( "Chocolate" ).getAmount() );

        Assert.assertEquals( "Editing a recipe shouldn't duplicate it", 1, service.count() );

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

    @Test
    @Transactional
    public void testEqualsRecipe () {
        final Recipe r1 = new Recipe();
        r1.addIngredient( "Coffee", 2 );
        r1.addIngredient( "Sugar", 3 );
        r1.setName( "Coffee with Sugar" );
        r1.setPrice( 10 );

        final Recipe r2 = new Recipe();
        r2.addIngredient( "Coffee", 3 );
        r2.setName( "Black Coffee" );

        final Recipe r3 = new Recipe();
        r3.setName( null );

        final Recipe r4 = new Recipe();
        r4.setName( "Coffee with Sugar" );

        Assert.assertFalse( r1.equals( r2 ) );
        Assert.assertFalse( r1.equals( r3 ) );
        Assert.assertFalse( r2.equals( r3 ) );
        Assert.assertFalse( r1.equals( null ) );
        Assert.assertFalse( r3.equals( r1 ) );
        Assert.assertTrue( r1.equals( r1 ) );
        Assert.assertTrue( r1.equals( r4 ) );

    }

    @Test
    @Transactional
    public void testGetIngredient () {
        final Recipe r1 = new Recipe();
        final Ingredient i1 = new Ingredient( "Coffee", 3 );
        r1.addIngredient( i1 );
        final Ingredient i2 = new Ingredient( "Milk", 2 );
        r1.addIngredient( i2 );
        final Ingredient i3 = new Ingredient( "Sugar", 3 );
        r1.addIngredient( i3 );
        final Ingredient i4 = new Ingredient( "Chocolate", 2 );
        r1.addIngredient( i4 );
        r1.setName( "Drink" );
        r1.setPrice( 10 );

        Assert.assertEquals( i1, r1.getIngredient( "Coffee" ) );
        Assert.assertEquals( i2, r1.getIngredient( "Milk" ) );
        Assert.assertEquals( i3, r1.getIngredient( "Sugar" ) );
        Assert.assertEquals( i4, r1.getIngredient( "Chocolate" ) );

        Assert.assertEquals( null, r1.getIngredient( "Honey" ) );

        r1.removeIngredient( i4 );
        Assert.assertEquals( null, r1.getIngredient( "Chocolate" ) );

    }

}
