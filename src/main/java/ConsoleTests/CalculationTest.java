package ConsoleTests;

import ConsoleEngine.CalculationEngine.Engine.Calculator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculationTest {

    @Test
    void calculateExpression() {
        assertEquals("4.0", Calculator.calculateExpression("4"));
        assertEquals("", Calculator.calculateExpression(""));
        assertEquals("", Calculator.calculateExpression(" "));
        assertEquals("-4.0", Calculator.calculateExpression("-4"));
        assertEquals("5.0", Calculator.calculateExpression("4 + 1"));
        assertEquals("3.0", Calculator.calculateExpression("4 + -1"));
        assertEquals("0.2", Calculator.calculateExpression("0.2"));
        assertEquals("1.6", Calculator.calculateExpression("1.2 + 0.4"));
        assertEquals("1.6", Calculator.calculateExpression("1.2 + .4"));
        assertEquals("0.6000000000000001", Calculator.calculateExpression("0.2 + 0.4"));
        assertEquals("-0.2", Calculator.calculateExpression("0.2 - 0.4"));
        assertEquals("6.0", Calculator.calculateExpression("2 - -4"));
        assertEquals("-3.0", Calculator.calculateExpression("-4 + 1"));
        assertEquals("-5.0", Calculator.calculateExpression("-4 + -1"));
        assertEquals("3.0", Calculator.calculateExpression("4 - 1"));
        assertEquals("-3.0", Calculator.calculateExpression("1 - 4"));
        assertEquals("12.0", Calculator.calculateExpression("4 * 3"));
        assertEquals("-12.0", Calculator.calculateExpression("4 * -3"));
        assertEquals("12.0", Calculator.calculateExpression("-4 * -3"));
        assertEquals("2.0", Calculator.calculateExpression("4 / 2"));
        assertEquals("0.5", Calculator.calculateExpression("2 / 4"));
        assertEquals("-2.0", Calculator.calculateExpression("4 / -2"));

        assertEquals("14.0", Calculator.calculateExpression("4 * 3 + 2"));
        assertEquals("10.0", Calculator.calculateExpression("4 + 3 * 2"));
        assertEquals("16.0", Calculator.calculateExpression("4 / 2 * 8"));
        assertEquals("4.0", Calculator.calculateExpression("(4)"));
        assertEquals("-4.0", Calculator.calculateExpression("(-4)"));
        assertEquals("-4.0", Calculator.calculateExpression("-(4)"));
        assertEquals("4.0", Calculator.calculateExpression("-(-4)"));
        assertEquals("4.0", Calculator.calculateExpression("-(-(4))"));
        assertEquals("7.0", Calculator.calculateExpression("(4 + 3)"));
        assertEquals("-6.0", Calculator.calculateExpression("-(3 + 3)"));
        assertEquals("4.0", Calculator.calculateExpression("(3) + 1"));
        assertEquals("2.0", Calculator.calculateExpression("(3) - 1"));
        assertEquals("14.0", Calculator.calculateExpression("(4 + 3) * 2"));
        assertEquals("13.0", Calculator.calculateExpression("4 + (3 + 1) + (3 + 1) + 1"));
        assertEquals("14.0", Calculator.calculateExpression("((4 + 3) * 2)"));
        assertEquals("42.0", Calculator.calculateExpression("((4 + 3) * 2) * 3"));
        assertEquals("-42.0", Calculator.calculateExpression("((4 + 3) * -2) * 3"));
        assertEquals("-2.0", Calculator.calculateExpression("((4 + 3) * 2) / -7"));
        assertEquals("16.0", Calculator.calculateExpression("(4 / 2) * 8"));
        assertEquals("0.25", Calculator.calculateExpression("4 / (2 * 8)"));
        assertEquals("1.0", Calculator.calculateExpression("(4 * 2) / 8"));
        assertEquals("1.0", Calculator.calculateExpression("4 * (2 / 8)"));
        assertEquals("16.0", Calculator.calculateExpression("(4 / (2) * 8)"));
        assertEquals("-4.0", Calculator.calculateExpression("-(3 + -(3 - 4))"));

        assertEquals("-34.0", Calculator.calculateExpression("3 + 5 - 6 * 7"));
        assertEquals("26.0", Calculator.calculateExpression("3 + 5 * 6 - 7"));

        assertEquals("48.0", Calculator.calculateExpression("(3 + 5) * 6"));
        assertEquals("16.5", Calculator.calculateExpression("(3 + 5 * 6) / 2"));
        assertEquals("20.0", Calculator.calculateExpression("(3 * (5 + 6) + 7) / 2"));
        assertEquals("42.0", Calculator.calculateExpression("((5 * 6) + (3 * 4))"));
        assertEquals("5.0", Calculator.calculateExpression("-3 + 4 * 2"));

        assertEquals("-5.0", Calculator.calculateExpression("+3 + +4 * -2"));

        assertEquals(Calculator.divizionByZero, Calculator.calculateExpression("4 / 0"));
    }
}