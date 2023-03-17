package com.wurstbox.atdit.discount;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class DiscountComputerImplTest {
    @Test
    public void singleDiscount() {
        var cut = new DiscountComputerImpl();

        List<Discount> discounts = cut.computeDiscount(100, 1);
        Discount expectedDiscount = new Discount("Aggregate", 0, 0);

        Discount actualDiscount = discounts.get(0);

        Assertions.assertEquals(expectedDiscount, actualDiscount);
    }
}
