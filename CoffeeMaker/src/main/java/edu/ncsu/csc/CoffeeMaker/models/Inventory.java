package edu.ncsu.csc.CoffeeMaker.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * Inventory for the coffee maker. Inventory is tied to the database using
 * Hibernate libraries. See InventoryRepository and InventoryService for the
 * other two pieces used for database support.
 *
 * @author Kai Presler-Marshall
 */
@Entity
public class Inventory extends DomainObject {

    /** id for inventory entry */
    @Id
    @GeneratedValue
    private Long                   id;
    @OneToMany ( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    private final List<Ingredient> ingredients;

    /**
     * Empty constructor for Hibernate
     */
    public Inventory () {
        // Intentionally empty so that Hibernate can instantiate
        // Inventory object.
        ingredients = new ArrayList<Ingredient>();
    }

    /**
     * Use this to create inventory with specified amts.
     *
     * @param coffee
     *            amt of coffee
     * @param milk
     *            amt of milk
     * @param sugar
     *            amt of sugar
     * @param chocolate
     *            amt of chocolate
     */
    public Inventory ( final ArrayList<Ingredient> ingredients ) {
        this.ingredients = ingredients;
    }

    /**
     * Returns the ID of the entry in the DB
     *
     * @return long
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Returns the ingredients in the inventory DB
     *
     * @return List of ingredients
     */
    public List<Ingredient> getIngredients () {
        return ingredients;
    }

    /**
     * Set the ID of the Inventory (Used by Hibernate)
     *
     * @param id
     *            the ID
     */
    public void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Returns true if there are enough ingredients to make the beverage.
     *
     * @param r
     *            recipe to check if there are enough ingredients
     * @return true if enough ingredients to make the beverage
     */
    public boolean enoughIngredients ( final Recipe r ) {
        boolean isEnough = true;
        for ( int i = 0; i < r.getRecipeIngredients().size(); i++ ) {
            final Ingredient rIng = r.getRecipeIngredients().get( i );
            final Ingredient iIng = getIngredient( rIng.getName() );
            if ( rIng.getAmount() > iIng.getAmount() ) {
                isEnough = false;
            }
        }
        return isEnough;
    }

    /**
     * Removes the ingredients used to make the specified recipe. Assumes that
     * the user has checked that there are enough ingredients to make
     *
     * @param r
     *            recipe to make
     * @return true if recipe is made.
     */
    public boolean useIngredients ( final Recipe r ) {
        if ( enoughIngredients( r ) ) {
            for ( int i = 0; i < r.getRecipeIngredients().size(); i++ ) {
                final Ingredient recipeIng = r.getRecipeIngredients().get( i );
                final Ingredient ivtIng = getIngredient( recipeIng.getName() );
                ivtIng.setAmount( ivtIng.getAmount() - recipeIng.getAmount() );
            }
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Adds ingredients to the inventory
     *
     * @param coffee
     *            amt of coffee
     * @param milk
     *            amt of milk
     * @param sugar
     *            amt of sugar
     * @param chocolate
     *            amt of chocolate
     * @return true if successful, false if not
     */
    public boolean addIngredient ( final String name, final Integer amount ) {
        final Ingredient i = new Ingredient( name, amount );
        if ( checkIngredient( i ) ) {
            ingredients.add( i );
            return true;
        }
        else {
            return false;
        }
    }

    public boolean checkIngredient ( final Ingredient ingredient ) {
        return ingredient.checkAmount( ingredient.getAmount() );
    }

    public void updateInventory ( final Ingredient ingredient, final Integer amount ) {
        for ( int i = 0; i < ingredients.size(); i++ ) {
            if ( ingredients.get( i ).equals( ingredient ) ) {
                ingredients.get( i ).setAmount( ingredients.get( i ).getAmount() + amount );
            }
        }
    }

    public Ingredient getIngredient ( final String name ) {
        for ( int i = 0; i < ingredients.size(); i++ ) {
            if ( ingredients.get( i ).getName().equals( name ) ) {
                return ingredients.get( i );
            }
        }
        return null;
    }

    /**
     * Returns a string describing the current contents of the inventory.
     *
     * @return String
     */
    @Override
    public String toString () {
        String s = "Inventory";
        s += " with ingredients [";
        for ( int i = 0; i < ingredients.size(); i++ ) {
            s += "Ingredient [";
            s += "ingredient=" + ingredients.get( i ).getName();
            s += ", amount=";
            s += ingredients.get( i ).getAmount() + "]";
            if ( i != ingredients.size() - 1 ) {
                s += ", ";
            }
            else {
                s += "]";
            }
        }
        return s;
    }

    @Override
    public int hashCode () {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( id == null ) ? 0 : id.hashCode() );
        result = prime * result + ( ( ingredients == null ) ? 0 : ingredients.hashCode() );
        return result;
    }

    @Override
    public boolean equals ( final Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( ! ( obj instanceof Inventory ) ) {
            return false;
        }
        final Inventory other = (Inventory) obj;
        if ( id == null ) {
            if ( other.id != null ) {
                return false;
            }
        }
        else if ( !id.equals( other.id ) ) {
            return false;
        }
        if ( ingredients == null ) {
            if ( other.ingredients != null ) {
                return false;
            }
        }
        else if ( !ingredients.equals( other.ingredients ) ) {
            return false;
        }
        return true;
    }

}
