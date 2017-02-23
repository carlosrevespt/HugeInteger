/**
 * Creates HugeIntegers with up to 40 digits.
 * 
 * @author Carlos Revés
 * @version 3.0
 */

import java.util.Arrays;

public class HugeInteger
{
    /** 
     * The signum and digits fields below represent the
     * full value stored by this {@code HugeInteger}.
     * 
     * @implNote The {@code digits} array is stored in Little-Endian form.
     * @implNote The {@code digits} array is implemented as an array of decimal digits
     *           for instrutional purposes. In the future is considered to convert it
     *           to an array of bytes.
     */
    final int[] digits;
    final int signum;
    
    /** 
     * Final fields (contants) representing {@code HugeIntegers} with
     * values of 1, -1 and 0.
     */
    public static final HugeInteger ONE = new HugeInteger ("1");
    public static final HugeInteger MINUS_ONE = new HugeInteger ("-1");
    public static final HugeInteger ZERO = new HugeInteger ();

    // Constructors for class HugeInteger.
    
    /**
     * This constructor initializes the {@code HugeInteger} to zero.
     */
    public HugeInteger() 
    {
        digits = new int[0];
        signum = 0;
    }
    
    /**
     * This public constructor translates an array of decimal digits (integers) containing the representation 
     * of a {@code HugeInteger} into a {@code HugeInteger}. 
     * 
     * @param int[] The input array. It's assumed to be in big-endian order: the most significant digit is in the zeroth element.
     * @throws NumberFormatException If the input array is not a valid representation of a {@code HugeInteger} number.
     */ 
    public HugeInteger(int[] digitsBigEndian) 
    {
        validateArray(digitsBigEndian);
        digitsBigEndian = stripLeadingZerosArray(digitsBigEndian);
                   
        if (digitsBigEndian.length > 0 && digitsBigEndian[0] < 0)
        {
            signum = -1;
            digitsBigEndian[0] *= -1;
        }
        else
        {                
            signum = digitsBigEndian.length == 0 ? 0 : 1;
        }
            
        digits = convertToLittleEndian(digitsBigEndian);        
    }
    
    /**
     * This public constructor translates a String containing the decimal representation 
     * of a {@code HugeInteger} into a {@code HugeInteger}. The string may contain one optional
     * minus or plus signs followed by at least one decimal digit. The string may not contain
     * other characters other than these optional signs.
     * 
     * @param String The input string. Is assumed to be in big-endian order: the most significant digit is in the zeroth element
     *               if no sign is present, or in the first element if a sign is present.
     * @throws NumberFormatException If the input string is not a valid decimal representation of a {@code HugeInteger} number.
     */ 
    public HugeInteger(String digitsBigEndian) 
    {
        validateString(digitsBigEndian);
        boolean isNegative = false;
            
        if (!Character.isDigit(digitsBigEndian.charAt(0)))
        {
            if (digitsBigEndian.charAt(0) == '-')
            {
                isNegative = true;
            }
            digitsBigEndian = digitsBigEndian.substring(1);
        }
            
        digitsBigEndian = stripLeadingZerosString(digitsBigEndian);
            
        if (digitsBigEndian.length() == 0)
        {
            signum = 0;
            digits = new int[0];
        }
        else
        {
            signum = isNegative ? -1 : 1;
            digits = new int[digitsBigEndian.length()];
              
            for (int finalIndex = 0; finalIndex < digitsBigEndian.length(); finalIndex++)
            {
                int originalIndex = digitsBigEndian.length() - finalIndex - 1;
                digits[finalIndex] = Character.getNumericValue(digitsBigEndian.charAt(originalIndex));
            }
        }        
    }
    
    /**
     * This public constructor translates into a {@code HugeInteger} the representation of a {@code HugeInteger}
     * consisting in an array of decimal digits (integers) and a signum value.
     * 
     * @param int[] The input array. It's assumed to be in big-endian order: the most significant digit is in the zeroth element.
     * @param int The value of the signum function of the {@code HugeInteger} where {@code -1} represents a 
     *            negative number, {@code 1} a positive number and {@code 0} the number zero.
     * @throws NumberFormatException If the input array is not a valid representation of a {@code HugeInteger} number, if the provided
     *                               signum value if less than -1 or greater than 1 or if there is a mismatch between the signum value
     *                               and the input array.
     */
    public HugeInteger(int[] digitsBigEndian, int signum) 
    {
        if (signum < -1 || signum > 1)
        {
            throw new NumberFormatException("Invalid signum value");
        }
        
        validateArray(digitsBigEndian);
        digitsBigEndian = stripLeadingZerosArray(digitsBigEndian);        
        
        if (digitsBigEndian.length == 0)
        {
             this.signum = 0;
        }
        else if (signum == 0 || (digitsBigEndian[0] < 0 && signum != -1))
        {
            throw new NumberFormatException("Signum mismatch");
        }
        else
        {
            this.signum = signum;
        }
            
        digits = convertToLittleEndian(digitsBigEndian);                     
    }
    
    /**
     * This private constructor translates into a {@code HugeInteger} the representation of a {@code HugeInteger}
     * consisting in a signum value and an array of decimal digits (integers). It assumes that all the inputs are
     * correct and that the input array is in little-endian order.
     * 
     * @param int The value of the signum function of the {@code HugeInteger} where {@code -1} represents a 
     *            negative number, {@code 1} a positive number and {@code 0} the number zero.
     * @param int[] The input array. It's assumed to be in little-endian order: the least significant digit is in the zeroth element.
     */
    private HugeInteger(int signum, int[] digitsLittleEndian)
    {
        digits = digitsLittleEndian;
        this.signum = signum;
    }
    
    // Private methods of class HugeInteger.
    
    /**
     * This private method verifies if the input array of decimal digits is a valid representation
     * of a {@code HugeInteger} number. To be valid its length must not be greater than 40, all digits
     * must be in the range of 0 to 9 and all but the first digit must be non-negative.
     * 
     * @param int[] The input array. Is assumed to be in big-endian order: the most significant digit is in the zeroth element.
     * @return An {@code ArgumentValidator} object containing the results of the validation.
     */ 
    private final void validateArray(int[] digitsBigEndian)
    {
        boolean validationResult = true;
        String validationMessage = "";
        
        if (digitsBigEndian.length > 40) 
        {
            validationMessage = "Number too large. Must have at most 40 digits";
            validationResult = false;
        }
        
        if (digitsBigEndian.length > 0)
        {
            for (int n = 0; n < digitsBigEndian.length; n++) 
            {
                if (n == 0 && (digitsBigEndian[n] < -9 || digitsBigEndian[n] > 9)) 
                {
                    validationMessage = "Invalid Number";
                    validationResult = false;
                }
                else if (n > 0 && (digitsBigEndian[n] < 0 || digitsBigEndian[n] > 9)) 
                {
                    validationMessage = "Invalid Number";
                    validationResult = false;
                }
            }
        }
        
        if (!validationResult)
        {
            throw new NumberFormatException(validationMessage);
        }        
    }
    
    /**
     * This private method verifies if the input String is a valid decimal representation of a
     * {@code HugeInteger} number. To be valid its length must in the range of 1 to 40 if no sign is
     * present, or 1 to 41 if a sign is present, all digits must be in the range of 0 to 9 and 
     * apart from the optional minus or plus sign at the zeroth element, may not contain other
     * non-numeric characters.
     * 
     * @param String The input string. Is assumed to be in big-endian order: the most significant digit is in
     *               the zeroth element if no sign is present, or in the first element if a sign is present.
     * @return An {@code ArgumentValidator} object containing the results of the validation.
     */ 
    private final void validateString(String digitsBigEndian)
    {        
        boolean validationResult = true;
        String validationMessage = "";
        
        if (digitsBigEndian.length() == 0) 
        {
            validationMessage = "Empty string";
            validationResult = false;
        }
        else if (digitsBigEndian.length() > 41)
        {
            validationMessage = "Number too large. Must have at most 40 digits.";
            validationResult = false;
        }
        else if (digitsBigEndian.length() > 40 && Character.isDigit(digitsBigEndian.charAt(0)))
        {
            validationMessage = "Number too large. Must have at most 40 digits.";
            validationResult = false;
        }
        else
        {
            int numberOfNonDigits = digitsBigEndian.replaceAll("[0-9]", "").length();
            
            if (numberOfNonDigits > 1)
            {
                validationMessage = "Invalid Number";
                validationResult = false;
            }
            else if (numberOfNonDigits == 1)
            {
                if (digitsBigEndian.length() == 1 || (digitsBigEndian.charAt(0) != '-' && digitsBigEndian.charAt(0) != '+'))
                {
                    validationMessage = "Invalid Number";
                    validationResult = false;
                }
            }
        }
        
        if (!validationResult)
        {
            throw new NumberFormatException(validationMessage);
        }
    }
    
    /**
     * This private method converts the input big-endian order array of decimal digits to little-endian order.
     * 
     * @param int[] The input array. Is assumed to be in big-endian order: the most significant digit 
     *              is in the zeroth element.
     * @return An integer array which is a copy of the input array in little-endian order: 
     *         the least significant digit is in the zeroth element.
     */
    private final int[] convertToLittleEndian(int[] digitsBigEndian)
    {
        int[] digitsLittleEndian = new int[digitsBigEndian.length];
        
        for (int index = 0; index < digitsBigEndian.length; index++)
        {
            digitsLittleEndian[index] = digitsBigEndian[digitsBigEndian.length - index - 1];
        }
        
        return digitsLittleEndian;
    }
    
    /**
     * This private method eliminate the leading zeros (if any) from the input array.
     * 
     * @param int[] The input array. Is assumed to be in big-endian order: the most significant digit 
     *              is in the zeroth element.
     * @return An integer array which is a copy of the input array without leading zeros.
     */
    private final int[] stripLeadingZerosArray(int[] digitsBigEndian) 
    {
        int numberLength = digitsBigEndian.length;
        int firstNonZero;
        
        // Find first non-zero integer in array.
        for (firstNonZero = 0; firstNonZero < numberLength && digitsBigEndian[firstNonZero] == 0; firstNonZero++)
            ;
        
        return firstNonZero == 0 ? digitsBigEndian : Arrays.copyOfRange(digitsBigEndian, firstNonZero, numberLength);
        
    }
    
    /**
     * This private method eliminate the trailing zeros (if any) from the input array.
     * 
     * @param int[] The input array. Is assumed to be in little-endian order: the least significant digit 
     *              is in the zeroth element.
     * @return An integer array which is a copy of the input array without trailing zeros.
     */
    private int[] stripTrailingZerosArray(int[] digitsLittleEndian) 
    {
        int numberLength = digitsLittleEndian.length;
        int firstNonZero = numberLength - 1;
        
        // Find first non-zero integer in array.
        for (; firstNonZero >= 0 && digitsLittleEndian[firstNonZero] == 0; firstNonZero--)
            ;
        
        return firstNonZero == numberLength - 1 ? digitsLittleEndian : Arrays.copyOfRange(digitsLittleEndian, 0, firstNonZero + 1);
        
    }
    
    /**
     * This private method eliminates the leading zeros (if any) from the input String.
     * 
     * @param int[] The input String. Is assumed to be in big-endian order: the most significant digit 
     *              is in the zeroth element.
     * @return A String which is a copy of the input string without leading zeros.
     */
    private final String stripLeadingZerosString(String digitsBigEndian) 
    {
        return digitsBigEndian.replaceAll("^0+","");
    }
    
    /**
     * This private method compares the absolute values of the {@code HugeIntegers} represented by the
     * two input digits arrays. Returns {@code -1} if the absolute value of the first is less than the second,
     * {@code 0} if they are equal and {@code 1} if the first is greater than the second.
     * 
     * @param int[] The array representing the absolute value of the first {@code HugeInteger}. Is assumed to be
     *              in little-endian order: the least significant digit is in the zeroth element.
     * @param int[] The array representing the absolute value of the second {@code HugeInteger}. Is assumed to be
     *              in little-endian order: the least significant digit is in the zeroth element.
     * @return {@code -1} if the absolute value of the first is less than the second, {@code 0} if they
     *         are equal and {@code 1} if the first is greater than the second.
     */
    private int compareAbsoluteValues(int[] firstNumber, int[] secondNumber)
    {
        int comparisonResult;
        int firstLength = firstNumber.length;
        int secondLength = secondNumber.length;
        
        if (firstLength == secondLength)
        {
            int firstNotEqual  = firstLength - 1;            
            
            for (; firstNotEqual >= 0 && firstNumber[firstNotEqual] == secondNumber[firstNotEqual]; firstNotEqual--)
                ;
            
            if (firstNotEqual < 0)
            {
                comparisonResult = 0;
            }
            else
            {
                comparisonResult = firstNumber[firstNotEqual] < secondNumber[firstNotEqual] ? -1 : 1;
            }
        }
        else
        {
            comparisonResult = firstNumber.length < secondNumber.length ? -1 : 1;
        }
        
        return comparisonResult;
    }
    
    /**
     * This private method sums the absolute values of the {@code HugeIntegers} represented by the
     * two input digits arrays.
     * 
     * @param int[] The array representing the absolute value of the first {@code HugeInteger}. Is assumed to be
     *              in little-endian order: the least significant digit is in the zeroth element.
     * @param int[] The array representing the absolute value of the second {@code HugeInteger}. Is assumed to be
     *              in little-endian order: the least significant digit is in the zeroth element.
     * @return An array in little-endian order which is the result of the sum of the two input arrays.
     */
    private int[] intAdd(int[] firstAddend, int[] secondAddend) 
    {
        int carry = 0;
        int maxLength = Math.max(firstAddend.length, secondAddend.length);
        int firstDigit = 0;
        int secondDigit = 0;
        int[] result = new int[maxLength + 1];
        
        for (int n = 0; n < maxLength; n++) 
        {
            if (n < firstAddend.length) 
            {
                firstDigit = firstAddend[n];
            }
            else 
            {
                firstDigit = 0;
            }
                
            if (n < secondAddend.length) 
            {
                secondDigit = secondAddend[n];
            }
            else 
            {
                secondDigit = 0;
            }
                
            int sum = firstDigit + secondDigit + carry;
            carry = sum > 9 ? 1 : 0; 
            result[n] = sum % 10;
        }
                
        if (carry == 1) 
        {
            result[maxLength] = 1;
        }
        else 
        {
            result = stripTrailingZerosArray(result);
        }
            
        return result;
    }
    
    /**
     * This private method subtracts the absolute values of the {@code HugeIntegers} represented by the
     * two input digits arrays.
     * 
     * @param int[] The array representing the absolute value of the first {@code HugeInteger}. Is assumed to be
     *              in little-endian order: the least significant digit is in the zeroth element.
     * @param int[] The array representing the absolute value of the second {@code HugeInteger}. Is assumed to be
     *              in little-endian order: the least significant digit is in the zeroth element.
     * @return An array in little-endian order which is the result of the subtraction of the two input arrays.
     */
    private int[] intSubtract(int[] minuend, int[] subtrahend, int compare) 
    {
        int carry = 0;
        int maxLength = Math.max(minuend.length, subtrahend.length);
        int firstDigit = 0;
        int secondDigit = 0;
        int[] result = new int[maxLength];
        
        if (subtrahend.length > minuend.length || compare == -1) 
        {
            int[] temp = minuend;
            minuend = subtrahend;
            subtrahend = temp;
        }
        
        for (int n = 0; n < maxLength; n++) 
        {
            firstDigit = minuend[n];
                
            if (n < subtrahend.length) 
            {
                secondDigit = subtrahend[n];
            }
            else 
            {
                secondDigit = 0;
            }
            
            int diff = firstDigit - secondDigit - carry;
            
            carry = diff < 0 ? 1 : 0;

            result[n] = (diff + 10) % 10;
        }
       
        result = stripTrailingZerosArray(result);
            
        return result;
    }
    
    /**
     * This private method gets the addition of the absolute values of two {@code hugeIntegers}.
     * 
     * @param int[] The array representing the absolute value of the first {@code HugeInteger}. Is assumed to be
     *              in little-endian order: the least significant digit is in the zeroth element.
     * @param int[] The array representing the absolute value of the second {@code HugeInteger}. Is assumed to be
     *              in little-endian order: the least significant digit is in the zeroth element.
     * @return A {@code hugeInteger} representing the sum of the input arrays with the signum of this {@code HugeInteger}.
     * @throws ArithmeticException If the resulting sum exceeds the capacity of a {@HugeInteger}.
     */
    private HugeInteger getAddition(int[] firstAddend, int[] secondAddend)
    {
        int[] addition = intAdd(firstAddend, secondAddend);
        if (addition.length > 40)
        {
            // If the sum has more than 40 digits than throw exception.
            throw new ArithmeticException("Overflow! The result exceeds the capacity of a HugeInteger.");
        }
        else
        {
            // If not then create a HugeInteger with the sum and the original signum.
            return new HugeInteger(getSignum(), addition);
        }            
    }
    
    /**
     * This private method gets the subtraction of the absolute values of two {@code hugeIntegers}.
     * 
     * @param int[] The array representing the absolute value of the first {@code HugeInteger}. Is assumed to be
     *              in little-endian order: the least significant digit is in the zeroth element.
     * @param int[] The array representing the absolute value of the second {@code HugeInteger}. Is assumed to be
     *              in little-endian order: the least significant digit is in the zeroth element.
     * @return A {@code hugeInteger} representing the subtraction of the input arrays with the signum of greatest.
     */
    private HugeInteger getSubtraction(int[] minuend, int[] subtrahend)
    {
        int compare = compareAbsoluteValues(minuend, subtrahend);
        HugeInteger result;
        
        // If both numbers are equal then the result is zero.
        if (compare == 0) 
        {
            result = new HugeInteger();
        }
        else
        {
            // If the numbers are not equal than subtract them.
            int[] subtraction = intSubtract (minuend, subtrahend, compare);
            
            // Create a HugeInteger with the subtraction and the signum of the greatest.
            result = new HugeInteger(compare * getSignum(), subtraction);
        }
            
        return result;
    }
    
    /**
     * This private method multiplies the absolute values of the {@code HugeIntegers} represented by the
     * two input digits arrays.
     * 
     * @param int[] The array representing the absolute value of the first {@code HugeInteger}. Is assumed to be
     *              in little-endian order: the least significant digit is in the zeroth element.
     * @param int[] The array representing the absolute value of the second {@code HugeInteger}. Is assumed to be
     *              in little-endian order: the least significant digit is in the zeroth element.
     * @return An array in little-endian order which is the result of the multiplication of the two input arrays.
     */
    private int[] intMultiply(int[] multiplier, int[] multiplicand) 
    {
        int multiplierLength = multiplier.length;
        int multiplicandLength = multiplicand.length;
        int totalLength = multiplierLength + multiplicandLength;
        int[][] multiplicationArray = new int[multiplicandLength][totalLength];
        
        for (int i = 0; i < multiplicandLength; i++) 
        {
            for (int j = 0; j < multiplierLength; j++) 
            {
                multiplicationArray[i][i + j] = multiplier[j] * multiplicand[i];
            }
        }
        
        int[] result = new int[totalLength];
        int carry = 0;
        
        for (int i = 0; i < totalLength; i++) 
        {
            int sum = carry;
            
            for (int j = 0; j < multiplicandLength; j++) 
            {
                sum += multiplicationArray[j][i];
            }
                
            result[i] = sum % 10; 
            carry = sum / 10;
        }
        
        return stripTrailingZerosArray(result);
    }
    
    // Static methods of class HugeInteger.
    
    /**
     * This static method creates a new {@code HugeInteger} from a string containing the decimal 
     * representation of a {@code HugeInteger}.
     * 
     * @param String The input String. Is assumed to be in big-endian order: the most significant digit 
     *               is in the zeroth element, if no sign present, or in the first element, if sign is present.
     * @return A new {@code HugeInteger} with the value represented by the input string.
     */
    public static HugeInteger parse(String digitsBigEndian) 
    {
        return new HugeInteger(digitsBigEndian);
    }
    
    /**
     * This static method creates a new {@code HugeInteger} from an array of decimal digits (integers)
     * containing the representation of a {@code HugeInteger}.
     * 
     * @param int[] The input array. Is assumed to be in big-endian order: the most significant digit 
     *              is in the zeroth element.
     * @return A new {@code HugeInteger} with the value represented by the input array.
     */
    public static HugeInteger parse(int[] digitsBigEndian) 
    {
        return new HugeInteger(digitsBigEndian);
    }
    
    /**
     * This static method creates a new {@code HugeInteger} from an array of decimal digits (integers)
     * containing the representation of a {@code HugeInteger} and a specified signum.
     * 
     * @param int[] The input array. Is assumed to be in big-endian order: the most significant digit 
     *              is in the zeroth element.
     * @param int The value of the signum function of the {@code HugeInteger} where {@code -1} represents a 
     *            negative number, {@code 1} a positive number and {@code 0} the number zero.
     * @return A new {@code HugeInteger} with the value represented by the input array.
     */
    public static HugeInteger parse(int[] digitsBigEndian, int signum) 
    {
        return new HugeInteger(digitsBigEndian, signum);
    }
    
    /**
     * This static method creates a new {@code HugeInteger} which is the absolute value of the
     * provided {@code HugeInteger}.
     * 
     * @param HugeInteger The input {@code HugeInteger}.
     * @return A new {@code HugeInteger} with the absolute value of the input {@code HugeInteger}.
     */
    public static HugeInteger abs(HugeInteger originalHugeInteger) 
    {
        HugeInteger absoluteValue;
        
        if (originalHugeInteger.isZero()) 
        {
            absoluteValue = new HugeInteger();
        }
        else 
        {
            absoluteValue = new HugeInteger(1, originalHugeInteger.digits);
        }
        
        return absoluteValue;
    }
    
    /**
     * This static method creates a new {@code HugeInteger} which is a clone (copy) of the
     * provided {@code HugeInteger}.
     * 
     * @param HugeInteger The input {@code HugeInteger}.
     * @return A new {@code HugeInteger} with the same value of the input {@code HugeInteger}.
     */
    public static HugeInteger clone(HugeInteger originalHugeInteger) 
    {
        return new HugeInteger(originalHugeInteger.getSignum(), originalHugeInteger.digits);
    }
    
    /**
     * This static method creates a new {@code HugeInteger} which is has a opposite value of the
     * provided {@code HugeInteger}.
     * 
     * @param HugeInteger The input {@code HugeInteger}.
     * @return A new {@code HugeInteger} with the opposite value of the input {@code HugeInteger}.
     */
    public static HugeInteger opposite(HugeInteger originalHugeInteger) 
    {
        return new HugeInteger(-1 * originalHugeInteger.getSignum(), originalHugeInteger.digits);
    }
    
    // Public methods of class HugeInteger.
    
    /**
     * This public method returns the value of the signum function of the {@code HugeInteger}.
     * 
     * @return The value of the signum function of the {@code HugeInteger} where {@code -1} represents a 
     *         negative number, {@code 1} a positive number and {@code 0} the number zero.
     */
    public int getSignum() 
    {
        return signum;
    }
    
    /**
     * This public method returns {@code true} if the {@code HugeInteger} is equal to {@code zero}
     * and {@code false} if not.
     * 
     * @return {@code true} if the {@code HugeInteger} is equal to {@code zero} and {@code false} if not.
     */
    public boolean isZero() 
    {
        return getSignum() == 0;        
    }
    
    /**
     * This public method returns {@code true} if the {@code HugeInteger} is equal to {@code one}
     * and {@code false} if not.
     * 
     * @return {@code true} if the {@code HugeInteger} is equal to {@code one} and {@code false} if not.
     */
    public boolean isOne() 
    {
        return isEqualTo(ONE);        
    }
    
    /**
     * This public method returns {@code true} if the {@code HugeInteger} is equal to {@code minus one}
     * and {@code false} if not.
     * 
     * @return {@code true} if the {@code HugeInteger} is equal to {@code minus one} and {@code false} if not.
     */
    public boolean isMinusOne() 
    {
        return isEqualTo(MINUS_ONE);        
    }
    
    /**
     * This public method returns {@code true} if the {@code HugeInteger} is positive and
     * {@code false} if not.
     * 
     * @return {@code true} if the {@code HugeInteger} is positive and {@code false} if not.
     */
    public boolean isPositive() 
    {
        return getSignum() == 1;
    }
    
    /**
     * This public method returns {@code true} if the {@code HugeInteger} is negative and
     * {@code false} if not.
     * 
     * @return {@code true} if the {@code HugeInteger} is negative and {@code false} if not.
     */
    public boolean isNegative() 
    {
        return getSignum() == -1;
    }
    
    /**
     * This public method compares two {@code HugeIntegers} for equality. Assumes that they are
     * equal if both signums are the same and if every digit in both {@code HugeIntegers} are equal.
     * Returns {@code true} if the two {@code HugeIntegers} are equal and {@code false} if not. 
     * 
     * @param HugeInteger The input {@code HugeInteger} to be compared to this {@code HugeInteger}.
     * @return {@code true} if the two {@code HugeIntegers} are equal and {@code false} if not.
     */
    public boolean isEqualTo(HugeInteger comparedHugeinteger) 
    {
        boolean isEqual;
        // If signums are different so the HugeIntegers are.
        if (getSignum() != comparedHugeinteger.getSignum()) 
        {
            isEqual = false;
        }
        else if (isZero() && comparedHugeinteger.isZero()) 
        {
            // If both are zero then the HugeIntegers are equal.
            isEqual = true;
        }
        else
        {
            // Compare the 2 value arrays return true if result is 0.
            isEqual = compareAbsoluteValues(digits, comparedHugeinteger.digits) == 0;
        }
        
        return isEqual;
    }
    
    /**
     * This public method compares two {@code HugeIntegers} for inequality. Assumes that they are different
     * if both signums are the not same or if at least one digit in the {@code HugeIntegers} is different.
     * Returns {@code true} if the two {@code HugeIntegers} are different and {@code false} if not. 
     * 
     * @param HugeInteger The input {@code HugeInteger} to be compared to this {@code HugeInteger}.
     * @return {@code true} if the two {@code HugeIntegers} are different and {@code false} if not.
     */
    public boolean isNotEqualTo(HugeInteger comparedHugeinteger) 
    {
        return !isEqualTo(comparedHugeinteger);
    }
    
    /**
     * This public method verifies if the value of this {@code HugeInteger} is less than the value of
     * the input {@code HugeInteger}. Returns {@code true} if it is less and {@code false} if not. 
     * 
     * @param HugeInteger The input {@code HugeInteger} to be compared to this {@code HugeInteger}.
     * @return {@code true} if this {@code HugeInteger} is less than the input {@code HugeInteger}
     *         and {@code false} if not.
     */
    public boolean isLessThan(HugeInteger comparedHugeinteger) 
    {
        boolean thisIsLess;
        // If the current signum is less than the second than this HugeInteger is less also.
        // If it's greater than not.
        if (getSignum() < comparedHugeinteger.getSignum()) 
        {
            thisIsLess = true;
        }
        else if (getSignum() > comparedHugeinteger.getSignum()) 
        {
            thisIsLess = false;
        }            
        // signums are equal so we need to verify:
        // signum == 0: they are equal. Return false;
        // signum == -1: return true if the current HugeInteger has a greater absolute value;
        // signum == 1: return true if the current HugeInteger has a less absolute value.        
        else if (signum == 0) 
        {
            thisIsLess = false;
        }
        else if (signum == -1) 
        {
            thisIsLess = compareAbsoluteValues(digits, comparedHugeinteger.digits) == 1;
        }
        else 
        {
            thisIsLess = compareAbsoluteValues(digits, comparedHugeinteger.digits) == -1;
        }
        
        return thisIsLess;
    }
    
    /**
     * This public method verifies if the value of this {@code HugeInteger} is less than or equal to the
     * value of the input {@code HugeInteger}. Returns {@code true} if it is less than or equal to and
     * {@code false} if not.
     * 
     * @param HugeInteger The input {@code HugeInteger} to be compared to this {@code HugeInteger}.
     * @return {@code true} if this {@code HugeInteger} is less than or equal to the input {@code HugeInteger}
     *         and {@code false} if not.
     */
    public boolean isLessThanOrEqualTo(HugeInteger comparedHugeinteger) 
    {
        // Resturn true if they are equal or the current HugeInteger is less than the other.
        return isEqualTo(comparedHugeinteger) || isLessThan(comparedHugeinteger);
    }
        
    /**
     * This public method verifies if the value of this {@code HugeInteger} is greater than the value of
     * the input {@code HugeInteger}. Returns {@code true} if it is greater and {@code false} if not. 
     * 
     * @param HugeInteger The input {@code HugeInteger} to be compared to this {@code HugeInteger}.
     * @return {@code true} if this {@code HugeInteger} is greater than the input {@code HugeInteger}
     *         and {@code false} if not.
     */
    public boolean isGreaterThan(HugeInteger comparedHugeinteger) 
    {
        // Resturn true if current HugeInteger is not less than or euqal to the other.
        return !isLessThanOrEqualTo(comparedHugeinteger);
    }
    
    /**
     * This public method verifies if the value of this {@code HugeInteger} is greater than or equal to the
     * value of the input {@code HugeInteger}. Returns {@code true} if it is greater than or equal to and
     * {@code false} if not.
     * 
     * @param HugeInteger The input {@code HugeInteger} to be compared to this {@code HugeInteger}.
     * @return {@code true} if this {@code HugeInteger} is greater than or equal to the input {@code HugeInteger}
     *         and {@code false} if not.
     */
    public boolean isGreaterThanOrEqualTo(HugeInteger comparedHugeinteger) 
    {
        // Resturn true if current HugeInteger is not less than the other.
        return !isLessThan(comparedHugeinteger);
    }
    
    /**
     * This public method performs the addition of this {@code HugeInteger} with the input {@code HugeInteger}.
     * 
     * @param HugeInteger The {@code HugeInteger} to be summed with this {@code HugeInteger}.
     * @return A {@code HugeInteger} which is the result of the addition.
     * @throws ArithmeticException If the result of the sum of the absolute values of the {@code HugeIntegers}
     *                             is greater than 40 digits.
     */
    public HugeInteger add (HugeInteger addend) 
    {
        HugeInteger result;
        // If any of the numbers is zero then the result is the other one.
        if (isZero()) 
        {
            result = clone(addend);
        }
        else if (addend.isZero()) 
        {
            result = clone(this);
        }
        else if (getSignum() == addend.getSignum()) 
        { 
            result = getAddition(digits, addend.digits);
        }
        else
        {
            result = getSubtraction(digits, addend.digits);
        }
        
        return result;
    }
    
    /**
     * This public method performs the subtraction of the input {@code HugeInteger} from this {@code HugeInteger}.
     * 
     * @param HugeInteger The {@code HugeInteger} to be subtracted from this {@code HugeInteger}.
     * @return A {@code HugeInteger} which is the result of the subtraction.
     * @throws ArithmeticException If the result of the sum of the absolute values of the {@code HugeIntegers}
     *                             is greater than 40 digits.
     */
    public HugeInteger subtract(HugeInteger subtrahend) 
    {
        HugeInteger result;
        
        if (subtrahend.isZero()) 
        {
            // If the second number is zero then the result is the first one.
            result = clone(this);
        }
        else if (isZero()) 
        {
            // If the fist number is zero then the result is the opposit of the second one.
            result = opposite(subtrahend);
        }
        else if (getSignum() != subtrahend.getSignum()) 
        {
            result = getAddition(digits, subtrahend.digits);
        } 
        else
        {
            result = getSubtraction(digits, subtrahend.digits);
        }
        
        return result;
    }
    
    /**
     * This public method performs the multiplication of this {@code HugeInteger} by the input {@code HugeInteger}.
     * 
     * @param HugeInteger The {@code HugeInteger} to multiply this {@code HugeInteger}.
     * @return A {@code HugeInteger} which is the result of the multiplication.
     * @throws ArithmeticException If the result of the multiplication of the absolute values of the {@code HugeIntegers}
     *                             is greater than 40 digits.
     */
    public HugeInteger multiply(HugeInteger multiplicand) 
    {
        HugeInteger result;
        
        if (isZero() || multiplicand.isZero()) 
        {
            result = new HugeInteger();
        }
        else if (isOne()) 
        {
            result = HugeInteger.clone(multiplicand);
        }
        else if (isMinusOne()) 
        {
            result = HugeInteger.opposite(multiplicand);
        }
        else if (multiplicand.isOne()) 
        {
            result = HugeInteger.clone(this);
        }
        else if (multiplicand.isMinusOne()) 
        {
            result = HugeInteger.opposite(this);
        }
        else
        {
            int[] multiplicationOfAbsoluteValues = intMultiply(digits, multiplicand.digits);
        
            if (multiplicationOfAbsoluteValues.length > 40) 
            {
                throw new ArithmeticException("Overflow! The result exceeds the capacity of a HugeInteger.");
            }
            else
            {
                result = new HugeInteger(getSignum() * multiplicand.getSignum(), multiplicationOfAbsoluteValues);
            }
        }
        
        return result;
    }
    
    /**
     * This public method returns the String representation of this {@code HugeInteger}.
     * A {@code minus} sign is added if the {@code HugeInteger} is negative.
     * 
     * @return String representation of this {@code HugeInteger}.
     */
    @Override
    public String toString() 
    {
        String result = "";
        
        if (isZero()) 
        {
            result = "0";
        }
        else
        {
            StringBuilder buffer = new StringBuilder(41);
            if (getSignum() == -1) 
            {
                buffer.append("-");
            }
            
            for (int index = digits.length - 1; index >= 0 ; index--) 
            {
                buffer.append(digits[index]);
            }
            
            result = buffer.toString();
        }
        
        return result;
    }
    
    public HugeInteger divide(HugeInteger divisor) 
    {
        HugeInteger result;
        
        if (divisor.isZero()) 
        {
            throw new ArithmeticException("Division by Zero.");
        }
        else if (isZero() || compareAbsoluteValues(digits, divisor.digits) == -1) 
        {
            result = new HugeInteger();
        }
        else if (isEqualTo(divisor)) 
        {
            result = new HugeInteger("1");
        }        
        else if (divisor.isOne()) 
        {
            result = HugeInteger.clone(this);
        }
        else if (divisor.isMinusOne()) 
        {
            result = HugeInteger.opposite(this);
        }
        else
        {
            int[] quotient = intDivide(digits, divisor.digits);
            result = new HugeInteger(getSignum() * divisor.getSignum(), quotient);
        }
        
        return result;
    }
    
    private int[] intDivide(int[] dividend, int[] divisor) 
    {
        int index = dividend.length - divisor.length;
        int[] parcel = Arrays.copyOfRange(dividend, index, dividend.length);
        int[] remainder;
        StringBuilder stringBuffer = new StringBuilder(dividend.length);
        
        
        if (compareAbsoluteValues(parcel, divisor) == -1)
        {
            parcel = Arrays.copyOfRange(dividend, --index, dividend.length);
        }
        
        while (index >= 0)
        {
            index--;
            int[] buffer = divisor;
            int[] quotientDigit = new int[] {0};
            
            while (compareAbsoluteValues(parcel, buffer) > -1)
            {
                buffer = intAdd(buffer, divisor);
                quotientDigit = intAdd(quotientDigit, ONE.digits);
            }
            
            stringBuffer.append(quotientDigit[0]);
            remainder = intSubtract(parcel, intMultiply(quotientDigit, divisor), 1);
            parcel = new int[remainder.length + 1];
            
            if (index > -1)
            {
                parcel[0] = dividend[index];
            
                for (int n = 0; n < remainder.length; n++)
                {
                    parcel[n + 1] = remainder[n];
                }
            }
        }
        
        String quotientString = stringBuffer.toString();
        int[] quotient = new int[quotientString.length()];
        
        for (int n = 0; n < quotientString.length(); n++)
        {
            quotient[quotient.length - 1 - n] = Character.getNumericValue(quotientString.charAt(n));
        }
        
        return quotient;
    }
    
    public HugeInteger remainder(HugeInteger divisor) 
    {
        HugeInteger result;
        
        if (divisor.isZero()) 
        {
            throw new ArithmeticException("Division by Zero.");
        }
        else if (isZero() || compareAbsoluteValues(digits, divisor.digits) == 0) 
        {
            result = new HugeInteger();
        }       
        else if (divisor.isOne() || divisor.isMinusOne()) 
        {
            result = HugeInteger.clone(this);
        }
        else if (compareAbsoluteValues(digits, divisor.digits) == -1) 
        {
            result = HugeInteger.clone(this);
        }
        else
        {
            int[] quotient = intDivide(digits, divisor.digits);
            int[] remainder = intSubtract(digits, intMultiply(quotient, divisor.digits), 1);
            
            if (remainder.length == 0)
            {
                result = new HugeInteger();
            }
            else
            {
                result = new HugeInteger(getSignum(), remainder);
            }
        }
        
        return result;
    }
    
}
