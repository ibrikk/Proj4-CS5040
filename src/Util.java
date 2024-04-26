/**
 * On my honor:
 * - I have not used source code obtained from another student,
 * or any other unauthorized source, either modified or
 * unmodified.
 * 
 * - All source code and documentation used in my program is
 * either my original work, or was derived by me from the
 * source code published in the textbook for this course.
 *
 * - I have not discussed coding details about this project with
 * anyone other than my partner (in the case of a joint
 * submission), instructor, ACM/UPE tutors or the TAs assigned
 * to this course. I understand that I may discuss the concepts
 * of this program with other students, and that another student
 * may help me debug my program so long as neither of us writes
 * anything during the discussion or modifies any computer file
 * during the discussion. I have violated neither the spirit nor
 * letter of this restriction.
 */

/**
 * This is the Handle class the MemoryManager returns
 * 
 * @author {Ibrahim Khalilov ibrahimk}
 * @version 2024-04-20
 */

public class Util {

    /**
     * @param <T>
     *            any
     * @param arr
     *            - array of any - can be used for both hash table and memory
     *            pool
     * 
     * @return - newArr - array of any
     */

    public static <T> T[] doubleSize(T[] arr) {
        @SuppressWarnings("unchecked")
        T[] newArr = (T[])java.lang.reflect.Array.newInstance(arr.getClass()
            .getComponentType(), arr.length * 2);
        for (int i = 0; i < arr.length; i++) {
            newArr[i] = arr[i];
        }
        print("Hash table expanded to " + newArr.length + " records");
        return newArr;
    }


    /**
     * @param x
     *            - checking if x is power of 2
     * 
     * @return - true or false if the value inputted is powerOfTwo
     */
    public static boolean isPowerOfTwo(int x) {

        return (x != 0) && ((x & (x - 1)) == 0);
    }


    /**
     * Printing from a new line
     */

    public static void print() {
        System.out.println();
    }


    /**
     * Printing based on one string argument
     * 
     * @param str
     *            - message
     */

    public static void print(String str) {
        System.out.println(str);
    }


    /**
     * Printing based on two arguments
     * 
     * @param str
     *            - message
     * @param arr
     *            - array of messages
     */

    public static void print(String str, String[] arr) {
        System.out.print(str);
        print(arr);
    }


    /**
     * Printing based on one array - prints the last line on a new line
     * 
     * @param arr
     *            - array of messages
     */

    public static void print(String[] arr) {
        if (arr.length >= 1) {
            System.out.print(arr[0]);
        }

        // note that i starts at 1, since we already printed the element at
        // index 0
        for (int i = 1; i < arr.length; i++) {
            if (i == arr.length - 1) {
                System.out.println(", " + arr[i]);
            }
            else {
                System.out.print(", " + arr[i]);
            }
        }
    }


    /**
     * Checks if the given memory block is cleared (i.e., all bytes are set to
     * zero).
     *
     * @param memory
     *            the byte array representing the memory block to be checked
     * @return true if all bytes in the memory block are zero, false otherwise
     */
    public static boolean isMemoryCleared(byte[] memory) {
        for (byte b : memory) {
            if (b != 0) {
                return false;
            }
        }
        return true;
    }
}
