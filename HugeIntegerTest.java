/**
 * Escreva a descrição da classe HugeIntegerTest aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
 
import java.util.Arrays;
 
import org.junit.Before;
import org.junit.Test;
 
public class HugeIntegerTest {
     
    private static String MAX_NUM = "9999999999999999999999999999999999999999";
    private static String MAX_LESS_ONE_NUM = "9999999999999999999999999999999999999998";
    private static String MIN_NUM = "-" + MAX_NUM;
    private static String MIN_PLUS_ONE_NUM = "-" + MAX_LESS_ONE_NUM;
    private static String PRODUCT = "1536360";
    private static String NEG_PRODUCT = "-1536360";
     
    private int[] maxInt;
    private int[] minInt;
    private int[] tooBig;
    private int[] badFirst;
    private int[] badSecond;
     
    private HugeInteger maxHuge;
    private HugeInteger minHuge;
    private HugeInteger maxLessOneHuge;
    private HugeInteger minPlusOneHuge;
    private HugeInteger huge1239;
    private HugeInteger huge1240;
    private HugeInteger hugeMinus1240;
    private HugeInteger huge1536360;
    private HugeInteger hugeMinus1536360;
 
    @Before
    public void setUp() throws Exception {
        createNecessaryArrays();
        testStaticNumbers();
        setupHugeNumbers();
    }
 
    private void createNecessaryArrays() {
        maxInt = new int[ MAX_NUM.length() ];
        int index = 0;
         
        for ( String s : MAX_NUM.split("")) {
            maxInt[index++] = Integer.valueOf(s);
        }
         
        minInt = Arrays.copyOf(maxInt, maxInt.length);
        minInt[0] = minInt[0] * -1;
        tooBig = Arrays.copyOf(maxInt, MAX_NUM.length() + 1);
        badFirst = Arrays.copyOf(maxInt, maxInt.length);
        badFirst[0] = -10;
        badSecond = Arrays.copyOf(maxInt, maxInt.length);
        badSecond[1] = 10;
    }
     
    private void testStaticNumbers() {
        assertEquals("0", HugeInteger.ZERO.toString());
        assertEquals("1", HugeInteger.ONE.toString());
        assertEquals("-1", HugeInteger.MINUS_ONE.toString());
    }
     
    private void setupHugeNumbers() {
        maxHuge = new HugeInteger(MAX_NUM);
        minHuge = new HugeInteger(MIN_NUM);
        maxLessOneHuge = new HugeInteger(MAX_LESS_ONE_NUM);
        minPlusOneHuge = new HugeInteger(MIN_PLUS_ONE_NUM);
        huge1239 = new HugeInteger("1239");
        huge1240 = new HugeInteger("1240");
        hugeMinus1240 = new HugeInteger("-1240");
        huge1536360 = new HugeInteger("1536360");
        hugeMinus1536360 = new HugeInteger("-1536360");
    }
 
    @Test
    public void testHugeInteger() {
        HugeInteger hi = new HugeInteger();
        assertEquals("0", hi.toString());
    }
     
    @Test(expected=NumberFormatException.class)
    public void testHugeIntegerIntArrayTooBig() {
        new HugeInteger(tooBig);
    }
     
    @Test(expected=NumberFormatException.class)
    public void testHugeIntegerIntArrayBadFirstDigit() {
        new HugeInteger(badFirst);
    }
 
    @Test(expected=NumberFormatException.class)
    public void testHugeIntegerIntArrayBadSecondDigit() {
        new HugeInteger(badSecond);
    }
     
    /*@Test(expected=NumberFormatException.class)
    public void testHugeIntegerIntArrayEmpty() {
        new HugeInteger(new int[0]);
    }*/
     
    @Test
    public void testHugeIntegerIntArray() {
        HugeInteger hi = new HugeInteger(maxInt);
        assertEquals(MAX_NUM, hi.toString());
        hi = new HugeInteger(minInt);
        assertEquals(MIN_NUM, hi.toString());
    }
 
    @Test(expected=NumberFormatException.class)
    public void testHugeIntegerStringEmpty() {
        new HugeInteger("");
    }
     
    @Test(expected=NumberFormatException.class)
    public void testHugeIntegerStringJustMinus() {
        new HugeInteger("-");
    }
     
    @Test(expected=NumberFormatException.class)
    public void testHugeIntegerStringNonNumeric() {
        new HugeInteger("123a");
    }
     
    @Test
    public void testHugeIntegerString() {
        HugeInteger hi = new HugeInteger(MAX_NUM);
        assertEquals(MAX_NUM, hi.toString());
        hi = new HugeInteger(MIN_NUM);
        assertEquals(MIN_NUM, hi.toString());
    }
 
    @Test(expected=NumberFormatException.class)
    public void testHugeIntegerIntArrayIntBadDigit() {
        new HugeInteger(badSecond, 1);
    }
     
    @Test(expected=NumberFormatException.class)
    public void testHugeIntegerIntArrayIntBadSignum() {
        new HugeInteger(maxInt, 2);
    }
     
    @Test(expected=NumberFormatException.class)
    public void testHugeIntegerIntArrayIntTooBig() {
        new HugeInteger(tooBig, 1);
    }
     
    @Test(expected=NumberFormatException.class)
    public void testHugeIntegerIntArrayIntZero() {
        new HugeInteger(new int[] {9}, 0);
    }
 
    @Test
    public void testAbs() {
        assertEquals("0", HugeInteger.abs(HugeInteger.ZERO).toString());
        assertEquals("1", HugeInteger.abs(HugeInteger.MINUS_ONE).toString());
        assertEquals("1", HugeInteger.abs(HugeInteger.ONE).toString());
    }
 
    @Test
    public void testOpposite() {
        assertEquals("1", HugeInteger.opposite(HugeInteger.MINUS_ONE).toString());
        assertEquals("-1", HugeInteger.opposite(HugeInteger.ONE).toString());
        assertEquals("0", HugeInteger.opposite(HugeInteger.ZERO).toString());
    }
 
    @Test
    public void testToString() {
        String number = "123";
        assertEquals(number, new HugeInteger(number).toString());
    }
 
    @Test
    public void testIsZero() {
        assertTrue(HugeInteger.ZERO.isZero());
    }
 
    @Test
    public void testIsOne() {
        assertTrue(HugeInteger.ONE.isOne());
    }
 
    @Test
    public void testIsMinusOne() {
        assertTrue(HugeInteger.MINUS_ONE.isMinusOne());
    }
 
    @Test
    public void testIsPositive() {
        assertTrue(maxHuge.isPositive());
        assertFalse(minHuge.isPositive());
        assertFalse(HugeInteger.ZERO.isPositive());
    }
 
    @Test
    public void testIsNegative() {
        assertFalse(maxHuge.isNegative());
        assertTrue(minHuge.isNegative());
        assertFalse(HugeInteger.ZERO.isNegative());
    }
 
    @Test
    public void testIsEqualTo() {
        assertTrue(HugeInteger.ZERO.isEqualTo(HugeInteger.ZERO));
        assertTrue(maxHuge.isEqualTo(maxHuge));
        assertTrue(minHuge.isEqualTo(minHuge));
        assertFalse(HugeInteger.ONE.isEqualTo(HugeInteger.MINUS_ONE));
        assertFalse(HugeInteger.ONE.isEqualTo(new HugeInteger("2")));
        assertFalse(huge1239.isEqualTo(huge1240));
    }
 
    @Test
    public void testIsNotEqualTo() {
        assertFalse(HugeInteger.ZERO.isNotEqualTo(HugeInteger.ZERO));
        assertFalse(maxHuge.isNotEqualTo(maxHuge));
        assertFalse(minHuge.isNotEqualTo(minHuge));
        assertTrue(HugeInteger.ONE.isNotEqualTo(HugeInteger.MINUS_ONE));
        assertTrue(HugeInteger.ONE.isNotEqualTo(new HugeInteger("2")));
        assertTrue(huge1239.isNotEqualTo(huge1240));
    }
 
    @Test
    public void testIsLessThan() {
        assertFalse(HugeInteger.ZERO.isLessThan(HugeInteger.ZERO));
        assertFalse(maxHuge.isLessThan(maxHuge));
        assertFalse(minHuge.isLessThan(minHuge));
        assertFalse(HugeInteger.ONE.isLessThan(HugeInteger.MINUS_ONE));
        assertTrue(HugeInteger.MINUS_ONE.isLessThan(HugeInteger.ONE));
        assertTrue(maxLessOneHuge.isLessThan(maxHuge));
        assertTrue(minHuge.isLessThan(minPlusOneHuge));
        assertTrue(minHuge.isLessThan(maxHuge));
        assertTrue(huge1239.isLessThan(huge1240));
        assertFalse(huge1240.isLessThan(huge1239));
    }
 
    @Test
    public void testIsLessThanOrEqualTo() {
        assertTrue(HugeInteger.ZERO.isLessThanOrEqualTo(HugeInteger.ZERO));
        assertTrue(maxHuge.isLessThanOrEqualTo(maxHuge));
        assertTrue(minHuge.isLessThanOrEqualTo(minHuge));
        assertFalse(HugeInteger.ONE.isLessThanOrEqualTo(HugeInteger.MINUS_ONE));
        assertTrue(HugeInteger.MINUS_ONE.isLessThanOrEqualTo(HugeInteger.ONE));
        assertTrue(maxLessOneHuge.isLessThanOrEqualTo(maxHuge));
        assertTrue(minHuge.isLessThanOrEqualTo(minPlusOneHuge));
        assertTrue(minHuge.isLessThanOrEqualTo(maxHuge));
        assertTrue(huge1239.isLessThanOrEqualTo(huge1240));
        assertFalse(huge1240.isLessThanOrEqualTo(huge1239));
    }
 
    @Test
    public void testIsGreaterThan() {
        assertFalse(HugeInteger.ZERO.isGreaterThan(HugeInteger.ZERO));
        assertFalse(maxHuge.isGreaterThan(maxHuge));
        assertFalse(minHuge.isGreaterThan(minHuge));
        assertTrue(HugeInteger.ONE.isGreaterThan(HugeInteger.MINUS_ONE));
        assertFalse(HugeInteger.MINUS_ONE.isGreaterThan(HugeInteger.ONE));
        assertTrue(maxHuge.isGreaterThan(maxLessOneHuge));
        assertTrue(minPlusOneHuge.isGreaterThan(minHuge));
        assertTrue(maxHuge.isGreaterThan(minHuge));
        assertFalse(huge1239.isGreaterThan(huge1240));
        assertTrue(huge1240.isGreaterThan(huge1239));
    }
 
    @Test
    public void testIsGreaterThanOrEqualTo() {
        assertTrue(HugeInteger.ZERO.isGreaterThanOrEqualTo(HugeInteger.ZERO));
        assertTrue(maxHuge.isGreaterThanOrEqualTo(maxHuge));
        assertTrue(minHuge.isGreaterThanOrEqualTo(minHuge));
        assertTrue(HugeInteger.ONE.isGreaterThanOrEqualTo(HugeInteger.MINUS_ONE));
        assertFalse(HugeInteger.MINUS_ONE.isGreaterThanOrEqualTo(HugeInteger.ONE));
        assertTrue(maxHuge.isGreaterThanOrEqualTo(maxLessOneHuge));
        assertTrue(minPlusOneHuge.isGreaterThanOrEqualTo(minHuge));
        assertTrue(maxHuge.isGreaterThanOrEqualTo(minHuge));
        assertFalse(huge1239.isGreaterThanOrEqualTo(huge1240));
        assertTrue(huge1240.isGreaterThanOrEqualTo(huge1239));
    }
     
    @Test(expected=ArithmeticException.class)
    public void  testAddOverflow() {
        HugeInteger.ONE.add(maxHuge);
        maxHuge.subtract(minHuge);
    }
 
    @Test
    public void testAdd() {
        assertEquals("0", HugeInteger.ZERO.add(HugeInteger.ZERO).toString());
        assertEquals("1", HugeInteger.ZERO.add(HugeInteger.ONE).toString());
        assertEquals("1", HugeInteger.ONE.add(HugeInteger.ZERO).toString());
        assertEquals("0", HugeInteger.MINUS_ONE.add(HugeInteger.ONE).toString());
        assertEquals(MAX_NUM, HugeInteger.ZERO.add(maxHuge).toString());
        assertEquals(MIN_NUM, HugeInteger.ZERO.add(minHuge).toString());
        assertEquals(MAX_NUM, maxLessOneHuge.add(HugeInteger.ONE).toString());
        assertEquals(MIN_PLUS_ONE_NUM, minHuge.add(HugeInteger.ONE).toString());
        assertEquals("2479", huge1240.add(huge1239).toString());
    }
     
    @Test(expected=ArithmeticException.class)
    public void testSubtractUnderflow() {
        minHuge.subtract(HugeInteger.ONE);
    }
 
    @Test
    public void testSubtract() {
        assertEquals("0", HugeInteger.ZERO.subtract(HugeInteger.ZERO).toString());
        assertEquals("-1", HugeInteger.ZERO.subtract(HugeInteger.ONE).toString());
        assertEquals("1", HugeInteger.ONE.subtract(HugeInteger.ZERO).toString());
        assertEquals("1", HugeInteger.ZERO.subtract(HugeInteger.MINUS_ONE).toString());
        assertEquals(MAX_NUM, maxHuge.subtract(HugeInteger.ZERO).toString());
        assertEquals(MIN_NUM, minHuge.subtract(HugeInteger.ZERO).toString());
        assertEquals(MAX_LESS_ONE_NUM, maxHuge.subtract(HugeInteger.ONE).toString());
        assertEquals(MIN_NUM, minPlusOneHuge.subtract(HugeInteger.ONE).toString());
        assertEquals("0", maxHuge.subtract(maxHuge).toString());
        assertEquals("1", huge1240.subtract(huge1239).toString());
    }
    
    @Test(expected=ArithmeticException.class)
    public void testMultiplyOverflow() {
        minHuge.multiply(minHuge);
        maxHuge.multiply(maxHuge);
    }
    
    @Test(expected=ArithmeticException.class)
    public void testMultiplyUnderflow() {
        minHuge.multiply(maxHuge);
        maxHuge.multiply(minHuge);
    }
    
    @Test
    public void testMultiply()
    {
        assertEquals(MAX_NUM, maxHuge.multiply(HugeInteger.ONE).toString());
        assertEquals(MIN_NUM, maxHuge.multiply(HugeInteger.MINUS_ONE).toString());
        assertEquals("0", maxHuge.multiply(HugeInteger.ZERO).toString());
        assertEquals("0", HugeInteger.ZERO.multiply(maxHuge).toString());
        assertEquals(PRODUCT, huge1240.multiply(huge1239).toString());
        assertEquals(NEG_PRODUCT, hugeMinus1240.multiply(huge1239).toString());
    }
    
    @Test(expected=ArithmeticException.class)
    public void testDivisionByZero() {
        maxHuge.divide(HugeInteger.ZERO);
    }
    
    @Test
    public void testDivide()
    {
        assertEquals(MAX_NUM, maxHuge.divide(HugeInteger.ONE).toString());
        assertEquals(MIN_NUM, maxHuge.divide(HugeInteger.MINUS_ONE).toString());
        assertEquals("0", huge1240.divide(maxHuge).toString());
        assertEquals("0", HugeInteger.ZERO.divide(maxHuge).toString());
        assertEquals("1240", huge1536360.divide(huge1239).toString());
        assertEquals("-1240", hugeMinus1536360.divide(huge1239).toString());
        assertEquals("1239", hugeMinus1536360.divide(hugeMinus1240).toString());
        assertEquals("1", hugeMinus1240.divide(hugeMinus1240).toString());
    }
    
    @Test(expected=ArithmeticException.class)
    public void testDivisionByZeroInRemainder() {
        maxHuge.remainder(HugeInteger.ZERO);
    }
    
    @Test
    public void testRemainder()
    {
        assertEquals(MAX_NUM, maxHuge.remainder(HugeInteger.ONE).toString());
        assertEquals(MIN_NUM, minHuge.remainder(HugeInteger.MINUS_ONE).toString());
        assertEquals("0", maxHuge.remainder(maxHuge).toString());
        assertEquals("0", HugeInteger.ZERO.remainder(maxHuge).toString());
        assertEquals("1239", huge1239.remainder(huge1240).toString());
        assertEquals("0", hugeMinus1536360.remainder(huge1239).toString());
        assertEquals("159", maxHuge.remainder(huge1240).toString());
        assertEquals("-159", minHuge.remainder(hugeMinus1240).toString());
    }
}

