public abstract class BCD {

    /**
     * Returns a binary representation of a single digit using an Unpacked BCD. Utilizes
     * @see #toBinaryDigit(int, int)
     * @param digit - the integer to be processed
     * @return a String object containing the result of the conversion (min/max bits = 8)
     * @throws Exception when the parameter is more than one digit or is negative
     */
    public static String toUnpacked (int digit) throws Exception {
        if (digit <= 9 && digit >= 0)
            return toBinaryDigit(digit, 8);
        else
            throw new Exception("Error: Digit must be >= 0 or <= 9.");
    }

    /**
     * Returns a binary representation of a single digit using an Packed BCD. Utilizes
     * @see #toBinaryDigit(int, int)
     * @param digit - the integer to be processed
     * @return a String object containing the result of the conversion (min/max bits = 4)
     * @throws Exception when the parameter is more than one digit or is negative
     */
    public static String toPacked (int digit) throws Exception {
        if (digit <= 9 && digit >= 0)
            return toBinaryDigit(digit, 4);
        else
            throw new Exception("Error: Digit " +
                    "must be >= 0 or <= 9.");
    }

    /**
     * Returns the binary representation of a number. Utilizes
     * @see #toUnpacked(int) to convert each digit in digits
     * @param digits - the String objects containing the digits to be processed
     * @return a String object containing the Unpacked BCD representation of each digit in the parameter
     * @throws Exception depends on @link #toUnpacked(int)
     */
    public static String toUnpacked (String digits) throws Exception {
        String str = "";

        for (int i = 0; i < digits.length(); i++)
            str = str.concat(" ").concat(toUnpacked(digits.charAt(i) - 48));

        return str;
    }

    /**
     * Returns the binary representation of a number. Utilizes
     * @see #toPacked(int) to convert each digit in digits
     * @param digits - the String objects containing the digits to be processed
     * @return a String object containing the Packed BCD representation of each digit in the parameter
     * @throws Exception depends on @link #toPacked(int)
     */
    public static String toPacked (String digits) throws Exception {
        String str = "";

        for (int i = 0; i < digits.length(); i++)
            str = str.concat(" ").concat(toPacked(digits.charAt(i) - 48));

        return str;
    }
    
    /**
     * counts how many big numbers(8/9)
     */
    public static int countBig(String n){
        int count = 0;
        for(int i=0; i<n.length(); i++){
            if(n.charAt(i)=='8' || n.charAt(i)=='9'){
                count++;
            }
        }
        return count;
    }

    /**
     * gets the biggest number position
     */
    public static int getBigPos(String n){
        for(int i=0; i<n.length(); i++){
            if(n.charAt(i)=='8' || n.charAt(i)=='9'){
                return i;
            }
        }
        return 0;
    }

    /**
     * gets the smallest number position
     */
    public static int getSmallPos(String n){
        for(int i=0; i<n.length(); i++){
            if(n.charAt(i)!='8' && n.charAt(i)!='9'){
                return i;
            }
        }
        return 0;
    }
    
    /**
     * Returns the binary representation of a number. Utilizes
     * @see #toBinaryDigit(int, int)
     * @param digits - the String objects containing the digits to be processed
     * @return a String object containing the Densely Packed BCD representation of each digit in the parameter
     * @throws Exception depends on @link #toDenselyPacked(String)
     */
    public static String toDenselyPacked (String digits) throws Exception {
        String str = "";
        String group;
        StringBuilder sb = null;
        String temp = "";

        if (digits.length()==1){
            digits = "00"+digits;
        } else if(digits.length()==2){
            digits = "0"+digits;
        }

        if (digits.length() % 3 == 0) {
            for (int i = 0; i < digits.length() / 3; i++) {
                
                group = digits.substring(i * 3, i * 3 + 3);

                if ((group.charAt(0) - 48 <= 7 && group.charAt(1) - 48 <= 7 && group.charAt(2) - 48 <= 7) || (group.charAt(1) - 48 == 0 && group.charAt(1) - 48 <= 7 && group.charAt(2) - 48 <= 9)) { // handles groups with less than 8 numebrs

                    str = str.concat(" ").concat(toBinaryDigit(group.charAt(0) - 48, 3)).concat(" ").concat(toBinaryDigit(group.charAt(1) - 48, 3)).concat(" ").concat(toBinaryDigit(group.charAt(2) - 48, 4));

                } else {    
                    str = str.concat(" ").concat(toBinaryDigit(0, 3)).concat(" ").concat(toBinaryDigit(0, 3)).concat(" ").concat(toBinaryDigit(0, 4));
                    sb = new StringBuilder(str);

                    sb.setCharAt(3, toPacked(group.charAt(0) - 48).charAt(3));
                    sb.setCharAt(7, toPacked(group.charAt(1) - 48).charAt(3));
                    sb.setCharAt(9, '1');
                    sb.setCharAt(12, toPacked(group.charAt(2) - 48).charAt(3));

                    // 0 123 4 567 8 9101112

                    if(countBig(digits)==1){
                        if(getBigPos(digits)==0){
                            temp = toBinaryDigit(group.charAt(1) - 48, 4);
                            sb.replace(5, 7, temp.substring(1, 3));
                            temp = toBinaryDigit(group.charAt(2) - 48, 4);
                            sb.replace(1, 3, temp.substring(1, 3));
                            sb.setCharAt(10, '1');
                            sb.setCharAt(11, '0');
                        } else if(getBigPos(digits)==1){
                            temp = toBinaryDigit(group.charAt(0) - 48, 4);
                            sb.replace(1, 3, temp.substring(1, 3));
                            temp = toBinaryDigit(group.charAt(2) - 48, 4);
                            sb.replace(5, 7, temp.substring(1, 3));
                            sb.setCharAt(10, '0');
                            sb.setCharAt(11, '1');
                        } else if(getBigPos(digits)==2){
                            temp = toBinaryDigit(group.charAt(0) - 48, 4);
                            sb.replace(1, 3, temp.substring(1, 3));
                            temp = toBinaryDigit(group.charAt(1) - 48, 4);
                            sb.replace(5, 7, temp.substring(1, 3));
                            sb.setCharAt(10, '0');
                            sb.setCharAt(11, '0');
                        }
                    } else if(countBig(digits)==2){
                        if(getSmallPos(digits)==0){
                            sb.setCharAt(5, '1');
                            sb.setCharAt(6, '0');
                        } else if(getSmallPos(digits)==1){
                            temp = toBinaryDigit(group.charAt(1) - 48, 4);
                            sb.replace(1, 3, temp.substring(1, 3));
                            sb.setCharAt(5, '0');
                            sb.setCharAt(6, '1');
                        } else if(getSmallPos(digits)==2){
                            temp = toBinaryDigit(group.charAt(2) - 48, 4);
                            sb.replace(1, 3, temp.substring(1, 3));
                            sb.setCharAt(5, '0');
                            sb.setCharAt(6, '0');
                        }
                        sb.setCharAt(10, '1');
                        sb.setCharAt(11, '1');
                    } else if(countBig(digits)==3){
                        sb.setCharAt(1, '0');
                        sb.setCharAt(2, '0');
                        sb.setCharAt(5, '1');
                        sb.setCharAt(6, '1');
                        sb.setCharAt(10, '1');
                        sb.setCharAt(11, '1');
                    }
                    str = sb.toString();
                }
            }
        } else
            throw new Exception("Error: Invalid number");

        return str;
    }

    /**
     * Converts a number into it's binary representation. Uses sign extension to fit specified length.
     * @param digit - the integer to be processed
     * @param len - the preferred length of the binary
     * @return a String object containing the binary representation.
     * @throws Exception if the preferred length is not enough to fit the number (overflow)
     */
    public static String toBinaryDigit (int digit, int len) throws Exception{
        String binaryString = "";
        int numBits = 0;
        int weight = (int) Math.pow(2, len - 1);
        int temp = digit;

        digit = Math.abs(digit);

        while (weight > 0) {
            if (digit >= weight) {
                binaryString = binaryString.concat("1");
                digit -= weight;
            } else
                binaryString = binaryString.concat("0");

            weight /= 2;
            numBits++;
        }

        if (Math.pow(2, numBits) - 1 < temp)
            throw new Exception("Error: Cannot convert, not enought allowable bits.");

        if (temp < 0) {
            boolean reverse = false;

            if (Math.pow(2, numBits - 1) < Math.abs(temp))
                throw new Exception("Error: Cannot convert, not enought allowable bits.");

            StringBuilder negated = new StringBuilder(binaryString);

            for (int i = binaryString.length() - 1; i >= 0; i--)
                if (reverse && negated.charAt(i) == '1')
                    negated.setCharAt(i, '0');
                else if (reverse && negated.charAt(i) == '0')
                    negated.setCharAt(i, '1');
                else if (!reverse && negated.charAt(i) == '1')
                    reverse = true;

            binaryString = negated.toString();
        }

        return binaryString;
    }
}