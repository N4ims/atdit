package com.wurstbox.atdit.discount;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DiscountComputerImpl {
  private static final String url = "jdbc:mariadb://localhost:3306/atdit";
  private static final String user = "atdit_user";
  private static final String password = "LaXI8i3abe1aluYoRAC3nAhIyiJ6hi";

  public List<Discount> computeDiscount(double base, int customer ) {
    List<Discount> result = new ArrayList<Discount>();

    Connection c;
    PreparedStatement s;
    ResultSet r;

    try {
      c = DriverManager.getConnection( url, user, password );

      s = c.prepareStatement(
          """
            SELECT d.discount_id, d.discount, d.discount_text
              FROM discount AS d
                INNER JOIN customer_discount cd ON d.discount_id = cd.discount_id
              WHERE customer_id = ?
          """ );
      s.setInt( 1, customer );
      r = s.executeQuery();

      while( r.next() ) {
        int id = r.getInt( 1 );
        double discountPercentage = r.getDouble( 2 ); // discount_percentage
        String discountText = r.getString( 3 ); //// discount_text

        double discountValue = base * discountPercentage / 100;
        Discount discount = new Discount(discountText, discountPercentage, discountValue);

        result.add(discount);

        System.out.println( "Granted " + discountPercentage + " % " + discountText + " discount: " + result + " €" );
      }


      double aggregatePercentage = 0;
      double aggregateValue = 0;

      for (Discount d : result){
        aggregateValue = aggregateValue + d.discountValue();
        aggregatePercentage += d.discountPercentage();
      }

      result.add(new Discount("Aggregate", aggregatePercentage, aggregateValue));

      r.close();
      s.close();
      c.close();

    }
    catch( Exception sql ) { }
    finally {

    }

    return result;
  }

  public static void main( String[] args ) {
    DiscountComputerImpl m = new DiscountComputerImpl();
    System.out.println( m.computeDiscount( 250, 2 ) );
  }
}
