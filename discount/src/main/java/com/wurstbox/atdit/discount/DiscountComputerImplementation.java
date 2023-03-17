package com.wurstbox.atdit.discount;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


/**
 Current Implementation for discount computation (see {@link DiscountComputer}).
 */
public class DiscountComputerImplementation implements DiscountComputer {
  private static final String url = "jdbc:mariadb://localhost:3306/atdit";
  private static final String user = "atdit_user";
  private static final String password = "LaXI8i3abe1aluYoRAC3nAhIyiJ6hi";

  DiscountComputerImplementation() {
  }

  /**
   @implSpec To compute the discount, the customer number is looked up in database. Associated discounts are retrieved and
   computed according to the rules defined in {@link DiscountComputer}.
   The database fulfill following requirements:
   <ul>
   <li>MariaDB</li>
   <li>Run on localhost:3306</li>
   <li>Schema: atdit</li>
   <li>user: atdit_user</li>
   <li>have a certain password that we can name here for it is written in the code anyway: LaXI8i3abe1aluYoRAC3nAhIyiJ6hi</li>
   </ul>
   */
  @Override
  public List<Discount> computeDiscount( double base, int customer ) {
    List<Discount> result = new ArrayList<>();
//
//    Connection c = null;
//    PreparedStatement s = null;
//    ResultSet r = null;
//
//    try {
//      c = DriverManager.getConnection( url, user, password );
//
//      s = c.prepareStatement(
//          """
//          SELECT d.discount_id, d.discount, d.discount_text
//            FROM discount AS d
//              INNER JOIN customer_discount cd ON d.discount_id = cd.discount_id
//            WHERE customer_id = ?
//                                """ );
//      s.setInt( 1, customer );
//      r = s.executeQuery();
//
//      while( r.next() ) {
//        double discountPercentage = r.getDouble( 2 ); // discount_text
//        String discountText = r.getString( 3 ); // discount
//        double discountValue = base * discountPercentage / 100;
//
//        Discount discount = new Discount(
//            discountText,
//            discountPercentage,
//            discountValue );
//
//        System.out.println(
//            "Granted " + discountPercentage + " % " +
//            discountText + " discount: " + discountValue + " €" );
//        result.add( discount );
//      }
//
//      Discount aggregate = new Discount( "Aggregate", 0, 0 );
//      for( Discount d : result ) {
//        aggregate = new Discount(
//            aggregate.description(),
//            aggregate.percentage() + d.percentage(),
//            aggregate.amount() + d.amount() );
//      }
//      result.add( 0, aggregate );
//
//      r.close();
//      s.close();
//      c.close();
//    }
//    catch( Exception sql ) { }
//

    List<Discount> result = new ArrayList<>();

    List<DiscountDB> dbData = getDiscountFromDB(int customerId);

    return result;

  }

  private List<DiscountDB> getDiscountFromDB( int customerId ) {

    List<DiscountDB> discounts = new ArrayList<DiscountDB>();



    String url = dbAccessProperties.getProperty("url");
    String user = dbAccessProperties.getProperty("user");
    String password = dbAccessProperties.getProperty("password");

    try(Connection connection = DriverManager.getConnection(url, user, password); )
    {
        Properties dbAccessProperties = getDBAccessProperties();

      String sql =  """
                      SELECT d.discount_id, d.discount, d.discount_text
                        FROM discount AS d
                          INNER JOIN customer_discount cd ON d.discount_id = cd.discount_id
                        WHERE customer_id = ?
                    """;

      try( PreparedStatement statement = connection.prepareStatement(sql); )
      {
          statement.setInt(1, customerId);
          try (ResultSet dbQueryResult = statement.executeQuery()) {
              while(dbQueryResult.next()) {
                  int discountId = dbQueryResult.getInt("discount_id");
                  double discountValue = dbQueryResult.getDouble("discount_value");
                  String discountText = dbQueryResult.getString("discount_text");

                  DiscountDB resultLine = new DiscountDB(discountId, discountValue, discountText);
                  discounts.add(resultLine);
              }
          }

      } catch( SQLException e2){
          throw new RuntimeException(e2);
      }

    }catch (SQLException e){
        throw new RuntimeException(e);
    }

    return discounts;
  }

  private PreparedStatement getPreparedStatement(Connection connection, int customerId) throws SQLException {
      String sql =  """
                      SELECT d.discount_id, d.discount, d.discount_text
                        FROM discount AS d
                          INNER JOIN customer_discount cd ON d.discount_id = cd.discount_id
                        WHERE customer_id = ?
                    """;
      try {
          PreparedStatement statement = connection.prepareStatement(sql);
      }  catch (SQLException e2){
          throw new RuntimeException(e2);
      }


  }

  private Properties getDBAccessProperties(){
      Properties dbAccessProperties;
      try( InputStream inputStream = getClass().getClassLoader().getResourceAsStream("db.properties"); )
      {
          dbAccessProperties = new Properties();
          dbAccessProperties.load(inputStream);
      } catch ( IOException e ){
          throw new RuntimeException(e);
      }
      return dbAccessProperties;
  }
}