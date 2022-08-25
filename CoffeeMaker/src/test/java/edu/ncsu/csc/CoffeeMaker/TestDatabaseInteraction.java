package edu.ncsu.csc.CoffeeMaker;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@RunWith ( SpringRunner.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )

public class TestDatabaseInteraction {

    @Autowired
    private RecipeService    recipeService;
    @Autowired
    private InventoryService inventoryService;

    @Test
    @Transactional
    public void testRecipes () {
        recipeService.deleteAll();

        final Recipe r = new Recipe();

        r.setName( "Mocha" );
        r.setPrice( 350 );
        final Ingredient i1 = new Ingredient( "Coffee", 2 );
        r.addIngredient( i1 );
        final Ingredient i2 = new Ingredient( "Sugar", 1 );
        r.addIngredient( i2 );
        final Ingredient i3 = new Ingredient( "Milk", 3 );
        r.addIngredient( i3 );
        final Ingredient i4 = new Ingredient( "Chocolate", 4 );
        r.addIngredient( i4 );

        recipeService.save( r );

        final List<Recipe> dbRecipes = recipeService.findAll();

        assertEquals( 1, dbRecipes.size() );

        final Recipe dbRecipe = dbRecipes.get( 0 );

        assertEquals( r.getName(), dbRecipe.getName() );
        assertEquals( r.getPrice(), dbRecipe.getPrice() );

        assertEquals( r.getIngredient( "Coffee" ), dbRecipe.getIngredient( "Coffee" ) );
        assertEquals( r.getIngredient( "Sugar" ), dbRecipe.getIngredient( "Sugar" ) );
        assertEquals( r.getIngredient( "Milk" ), dbRecipe.getIngredient( "Milk" ) );
        assertEquals( r.getIngredient( "Chocolate" ), dbRecipe.getIngredient( "Chocolate" ) );

        final Recipe foundRecipe = recipeService.findByName( "Mocha" );

        assertEquals( r.getName(), foundRecipe.getName() );
        assertEquals( r.getPrice(), foundRecipe.getPrice() );
        assertEquals( r.getIngredient( "Coffee" ), foundRecipe.getIngredient( "Coffee" ) );
        assertEquals( r.getIngredient( "Sugar" ), foundRecipe.getIngredient( "Sugar" ) );
        assertEquals( r.getIngredient( "Milk" ), foundRecipe.getIngredient( "Milk" ) );
        assertEquals( r.getIngredient( "Chocolate" ), foundRecipe.getIngredient( "Chocolate" ) );

        dbRecipe.setPrice( 15 );
        dbRecipe.updateIngredient( i2, 12 );
        recipeService.save( dbRecipe );

        assertEquals( 1, recipeService.count() );

        final Recipe dbRecipe2 = recipeService.findAll().get( 0 );

        assertEquals( 15, (int) dbRecipe2.getPrice() );
        assertEquals( 12, (int) dbRecipe2.getIngredient( "Sugar" ).getAmount() );

        final Recipe r2 = new Recipe();

        r2.setName( "Choco Latte" );
        r2.setPrice( 650 );
        final Ingredient i5 = new Ingredient( "Coffee", 1 );
        r2.addIngredient( i5 );
        final Ingredient i6 = new Ingredient( "Sugar", 3 );
        r2.addIngredient( i6 );
        final Ingredient i7 = new Ingredient( "Milk", 4 );
        r2.addIngredient( i7 );
        final Ingredient i8 = new Ingredient( "Chocolate", 4 );
        r2.addIngredient( i8 );

        recipeService.save( r2 );

        final Recipe r3 = new Recipe();

        r3.setName( "Cappuccino" );
        r3.setPrice( 550 );
        final Ingredient i9 = new Ingredient( "Coffee", 1 );
        r3.addIngredient( i9 );
        final Ingredient i10 = new Ingredient( "Sugar", 3 );
        r3.addIngredient( i10 );
        final Ingredient i11 = new Ingredient( "Milk", 4 );
        r3.addIngredient( i11 );
        final Ingredient i12 = new Ingredient( "Chocolate", 1 );
        r3.addIngredient( i12 );

        recipeService.save( r3 );

        final Recipe r4 = new Recipe();

        r4.setName( "Macchiato" );
        r4.setPrice( 450 );
        final Ingredient i13 = new Ingredient( "Coffee", 3 );
        r4.addIngredient( i13 );
        final Ingredient i14 = new Ingredient( "Sugar", 2 );
        r4.addIngredient( i14 );
        final Ingredient i15 = new Ingredient( "Milk", 2 );
        r4.addIngredient( i15 );
        final Ingredient i16 = new Ingredient( "Chocolate", 1 );
        r4.addIngredient( i16 );

        recipeService.save( r4 );

        final Recipe r5 = new Recipe();

        r5.setName( "Americano" );
        r5.setPrice( 500 );
        final Ingredient i17 = new Ingredient( "Coffee", 2 );
        r5.addIngredient( i17 );
        final Ingredient i18 = new Ingredient( "Sugar", 2 );
        r5.addIngredient( i18 );
        final Ingredient i19 = new Ingredient( "Milk", 3 );
        r5.addIngredient( i19 );
        final Ingredient i20 = new Ingredient( "Chocolate", 1 );
        r5.addIngredient( i20 );

        recipeService.save( r5 );

        assertEquals( 5, recipeService.count() );
        assertEquals( r5, recipeService.findAll().get( 4 ) );
        assertEquals( r4, recipeService.findAll().get( 3 ) );
        assertEquals( r3, recipeService.findAll().get( 2 ) );
        assertEquals( r2, recipeService.findAll().get( 1 ) );
        assertEquals( r, recipeService.findAll().get( 0 ) );
        assertEquals( true, recipeService.existsById( r.getId() ) );
        assertEquals( true, recipeService.existsById( r2.getId() ) );
        assertEquals( true, recipeService.existsById( r3.getId() ) );
        assertEquals( true, recipeService.existsById( r4.getId() ) );
        assertEquals( true, recipeService.existsById( r5.getId() ) );

        recipeService.delete( r4 );
        assertEquals( 4, recipeService.count() );
        assertEquals( r5, recipeService.findAll().get( 3 ) );
        assertEquals( r3, recipeService.findAll().get( 2 ) );
        assertEquals( r2, recipeService.findAll().get( 1 ) );
        assertEquals( r, recipeService.findAll().get( 0 ) );
        assertEquals( true, recipeService.existsById( r.getId() ) );
        assertEquals( true, recipeService.existsById( r2.getId() ) );
        assertEquals( true, recipeService.existsById( r3.getId() ) );
        assertEquals( false, recipeService.existsById( r4.getId() ) );
        assertEquals( true, recipeService.existsById( r5.getId() ) );

        recipeService.delete( r );
        assertEquals( 3, recipeService.count() );
        assertEquals( r5, recipeService.findAll().get( 2 ) );
        assertEquals( r3, recipeService.findAll().get( 1 ) );
        assertEquals( r2, recipeService.findAll().get( 0 ) );
        assertEquals( false, recipeService.existsById( r.getId() ) );
        assertEquals( true, recipeService.existsById( r2.getId() ) );
        assertEquals( true, recipeService.existsById( r3.getId() ) );
        assertEquals( false, recipeService.existsById( r4.getId() ) );
        assertEquals( true, recipeService.existsById( r5.getId() ) );

        recipeService.delete( r5 );
        assertEquals( 2, recipeService.count() );
        assertEquals( r3, recipeService.findAll().get( 1 ) );
        assertEquals( r2, recipeService.findAll().get( 0 ) );
        assertEquals( false, recipeService.existsById( r.getId() ) );
        assertEquals( true, recipeService.existsById( r2.getId() ) );
        assertEquals( true, recipeService.existsById( r3.getId() ) );
        assertEquals( false, recipeService.existsById( r4.getId() ) );
        assertEquals( false, recipeService.existsById( r5.getId() ) );

        recipeService.deleteAll();
        assertEquals( 0, recipeService.count() );
        assertEquals( false, recipeService.existsById( r.getId() ) );
        assertEquals( false, recipeService.existsById( r2.getId() ) );
        assertEquals( false, recipeService.existsById( r3.getId() ) );
        assertEquals( false, recipeService.existsById( r4.getId() ) );
        assertEquals( false, recipeService.existsById( r5.getId() ) );

    }

    /**
     * Tests that the getInventory() method checks the cases for the correct
     * value for non-initialized input.
     *
     * @author Amit Prakash (aprakas5)
     */
    @Test
    @Transactional
    public void testGetInventory () {
        inventoryService.deleteAll();
        final Inventory testInv = inventoryService.getInventory();
        assertEquals( 0, testInv.getIngredients().size() );

    }

    /**
     * Tests that the findById() method for invalid inputs and returns the
     * correct value for valid input.
     *
     * @author Amit Prakash (aprakas5)
     */
    @Test
    @Transactional
    public void testFindById () {
        final Recipe r2 = new Recipe();

        r2.setName( "Choco Latte" );
        r2.setPrice( 650 );
        final Ingredient i1 = new Ingredient( "Coffee", 1 );
        r2.addIngredient( i1 );
        final Ingredient i2 = new Ingredient( "Sugar", 3 );
        r2.addIngredient( i2 );
        final Ingredient i3 = new Ingredient( "Milk", 4 );
        r2.addIngredient( i3 );
        final Ingredient i4 = new Ingredient( "Chocolate", 4 );
        r2.addIngredient( i4 );

        assertEquals( null, recipeService.findById( r2.getId() ) );

        recipeService.save( r2 );

        final Recipe r3 = new Recipe();

        r3.setName( "Cappuccino" );
        r3.setPrice( 550 );
        final Ingredient i5 = new Ingredient( "Coffee", 1 );
        r3.addIngredient( i5 );
        final Ingredient i6 = new Ingredient( "Sugar", 3 );
        r3.addIngredient( i6 );
        final Ingredient i7 = new Ingredient( "Milk", 4 );
        r3.addIngredient( i7 );
        final Ingredient i8 = new Ingredient( "Chocolate", 1 );
        r3.addIngredient( i8 );

        assertEquals( null, recipeService.findById( r3.getId() ) );

        recipeService.save( r3 );

        final Recipe r4 = new Recipe();

        r4.setName( "Macchiato" );
        r4.setPrice( 450 );
        final Ingredient i9 = new Ingredient( "Coffee", 3 );
        r4.addIngredient( i9 );
        final Ingredient i10 = new Ingredient( "Sugar", 2 );
        r4.addIngredient( i10 );
        final Ingredient i11 = new Ingredient( "Milk", 2 );
        r4.addIngredient( i11 );
        final Ingredient i12 = new Ingredient( "Chocolate", 1 );
        r4.addIngredient( i12 );

        assertEquals( null, recipeService.findById( r4.getId() ) );

        recipeService.save( r4 );

        final Recipe r5 = new Recipe();

        r5.setName( "Americano" );
        r5.setPrice( 500 );
        final Ingredient i13 = new Ingredient( "Coffee", 2 );
        r5.addIngredient( i13 );
        final Ingredient i14 = new Ingredient( "Sugar", 2 );
        r5.addIngredient( i14 );
        final Ingredient i15 = new Ingredient( "Milk", 3 );
        r5.addIngredient( i15 );
        final Ingredient i16 = new Ingredient( "Chocolate", 1 );
        r5.addIngredient( i16 );

        assertEquals( null, recipeService.findById( r5.getId() ) );

        recipeService.save( r5 );

        assertEquals( r2, recipeService.findById( r2.getId() ) );
        assertEquals( r3, recipeService.findById( r3.getId() ) );
        assertEquals( r4, recipeService.findById( r4.getId() ) );
        assertEquals( r5, recipeService.findById( r5.getId() ) );
    }
}
