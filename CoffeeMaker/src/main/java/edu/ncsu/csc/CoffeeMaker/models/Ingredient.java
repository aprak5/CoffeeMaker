package edu.ncsu.csc.CoffeeMaker.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Ingredient extends DomainObject {
    @Id
    @GeneratedValue
    private long    id;
    private String  name;
    private Integer amount;

    public Ingredient () {
        super();
    }

    @Override
    public String toString () {
        return "Ingredient [id=" + id + ", ingredient=" + name + ", amount=" + amount + "]";
    }

    public String getName () {
        return name;
    }

    @Override
    public int hashCode () {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( amount == null ) ? 0 : amount.hashCode() );
        result = prime * result + (int) ( id ^ ( id >>> 32 ) );
        result = prime * result + ( ( name == null ) ? 0 : name.hashCode() );
        return result;
    }

    @Override
    public boolean equals ( final Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( ! ( obj instanceof Ingredient ) ) {
            return false;
        }
        final Ingredient other = (Ingredient) obj;
        if ( amount == null ) {
            if ( other.amount != null ) {
                return false;
            }
        }
        else if ( !amount.equals( other.amount ) ) {
            return false;
        }
        if ( id != other.id ) {
            return false;
        }
        if ( name == null ) {
            if ( other.name != null ) {
                return false;
            }
        }
        else if ( !name.equals( other.name ) ) {
            return false;
        }
        return true;
    }

    public void setName ( final String name ) {
        this.name = name;
    }

    public Integer getAmount () {
        return amount;
    }

    public void setAmount ( final Integer amount ) {
        this.amount = amount;
    }

    public boolean checkAmount ( final Integer amount ) {
        if ( amount < 0 ) {
            return false;
        }
        else {
            return true;
        }
    }

    @SuppressWarnings ( "unused" )
    private void setId ( final long id ) {
        this.id = id;
    }

    public Ingredient ( final String name, final Integer amount ) {
        super();
        setName( name );
        if ( checkAmount( amount ) ) {
            setAmount( amount );
        }
        else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public Long getId () {
        return id;
    }

}
