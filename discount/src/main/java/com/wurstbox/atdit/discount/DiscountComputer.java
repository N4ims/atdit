package com.wurstbox.atdit.discount;
import java.util.List;


public interface DiscountComputer {
    /**
     * The head of the list is the overall discount.
     * Following list items are the single discounts.
     */
    List<Discount> computeDiscount(double base, int customerId);
}

