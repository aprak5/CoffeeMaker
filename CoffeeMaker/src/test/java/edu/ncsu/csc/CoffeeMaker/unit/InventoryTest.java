package edu.ncsu.csc.CoffeeMaker.unit;

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
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;

@RunWith ( SpringRunner.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class InventoryTest {

    @Autowired
    private InventoryService inventoryService;

    @Before
    public void setup () {
        final Inventory ivt = inventoryService.getInventory();

        ivt.addIngredient( "Chocolate", 500 );
        ivt.addIngredient( "Coffee", 500 );
        ivt.addIngredient( "Milk", 500 );
        ivt.addIngredient( "Sugar", 500 );

        inventoryService.save( ivt );
    }

    @Test
    @Transactional
    public void testConsumeInventory () {
        final Inventory i = inventoryService.getInventory();

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

        i.useIngredients( recipe );

        /*
         * Make sure that all of the inventory fields are now properly updated
         */

        Assert.assertEquals( 490, (int) i.getIngredient( "Chocolate" ).getAmount() );
        Assert.assertEquals( 480, (int) i.getIngredient( "Milk" ).getAmount() );
        Assert.assertEquals( 495, (int) i.getIngredient( "Sugar" ).getAmount() );
        Assert.assertEquals( 499, (int) i.getIngredient( "Coffee" ).getAmount() );
    }

    /**
     * Tests that no inventory is consumed when there is not enough milk, sugar,
     * or chocolate
     *
     * @author Lydia Pearson (lrpears2)
     */
    @Test
    @Transactional
    public void testConsumeInventory2 () {
        final Inventory i = inventoryService.getInventory();

        final Recipe recipe = new Recipe();
        recipe.setName( "Delicious Not-Coffee" );
        final Ingredient i1 = new Ingredient( "Chocolate", 504 );
        recipe.addIngredient( i1 );
        final Ingredient i2 = new Ingredient( "Milk", 501 );
        recipe.addIngredient( i2 );
        final Ingredient i3 = new Ingredient( "Sugar", 503 );
        recipe.addIngredient( i3 );
        final Ingredient i4 = new Ingredient( "Coffee", 1 );
        recipe.addIngredient( i4 );

        recipe.setPrice( 5 );

        final String inventory = "Inventory with ingredients [Ingredient"
                + " [ingredient=Chocolate, amount=500], Ingredient [ingredient=Coffee"
                + ", amount=500], Ingredient [ingredient=Milk, amount=500], Ingredient"
                + " [ingredient=Sugar, amount=500]]";

        Assert.assertEquals( 500, (int) i.getIngredient( "Chocolate" ).getAmount() );
        Assert.assertEquals( 500, (int) i.getIngredient( "Milk" ).getAmount() );
        Assert.assertEquals( 500, (int) i.getIngredient( "Sugar" ).getAmount() );
        Assert.assertEquals( 500, (int) i.getIngredient( "Coffee" ).getAmount() );
        Assert.assertEquals( inventory, i.toString() );

        Assert.assertFalse( i.useIngredients( recipe ) );

        Assert.assertEquals( 500, (int) i.getIngredient( "Chocolate" ).getAmount() );
        Assert.assertEquals( 500, (int) i.getIngredient( "Milk" ).getAmount() );
        Assert.assertEquals( 500, (int) i.getIngredient( "Sugar" ).getAmount() );
        Assert.assertEquals( 500, (int) i.getIngredient( "Coffee" ).getAmount() );
        Assert.assertEquals( inventory, i.toString() );

    }

    @Test
    @Transactional
    public void testAddInventory1 () {
        Inventory ivt = inventoryService.getInventory();

        ivt.updateInventory( ivt.getIngredient( "Coffee" ), 5 );
        ivt.updateInventory( ivt.getIngredient( "Milk" ), 3 );
        ivt.updateInventory( ivt.getIngredient( "Sugar" ), 7 );
        ivt.updateInventory( ivt.getIngredient( "Chocolate" ), 2 );

        /* Save and retrieve again to update with DB */
        inventoryService.save( ivt );

        ivt = inventoryService.getInventory();

        Assert.assertEquals( "Adding to the inventory should result in correctly-updated values for coffee", 505,
                (int) ivt.getIngredient( "Coffee" ).getAmount() );
        Assert.assertEquals( "Adding to the inventory should result in correctly-updated values for milk", 503,
                (int) ivt.getIngredient( "Milk" ).getAmount() );
        Assert.assertEquals( "Adding to the inventory should result in correctly-updated values sugar", 507,
                (int) ivt.getIngredient( "Sugar" ).getAmount() );
        Assert.assertEquals( "Adding to the inventory should result in correctly-updated values chocolate", 502,
                (int) ivt.getIngredient( "Chocolate" ).getAmount() );

    }

    @Test
    @Transactional
    public void testAddInventory2 () {
        final Inventory ivt = inventoryService.getInventory();

        try {
            final Ingredient i = ivt.getIngredient( "Coffee" );
            ivt.updateInventory( i, -5 );
        }
        catch ( final IllegalArgumentException iae ) {
            Assert.assertEquals(
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- coffee",
                    500, (int) ivt.getIngredient( "Coffee" ).getAmount() );
            Assert.assertEquals(
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- milk",
                    500, (int) ivt.getIngredient( "Milk" ).getAmount() );
            Assert.assertEquals(
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- sugar",
                    500, (int) ivt.getIngredient( "Sugar" ).getAmount() );
            Assert.assertEquals(
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- chocolate",
                    500, (int) ivt.getIngredient( "Chocolate" ).getAmount() );
        }
    }

    /**
     * Tests if two recipes with the same name are considered equal
     *
     * @author Lydia Pearson (lrpears2)
     */
    @Test
    @Transactional
    public void testEqualRecipes () {
        final Recipe r1 = new Recipe();
        final Recipe r2 = new Recipe();
        Assert.assertTrue( r1.equals( r2 ) );

    }

}
