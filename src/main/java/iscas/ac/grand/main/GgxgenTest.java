package iscas.ac.grand.main;
import com.github.curiousoddman.rgxgen.RgxGen;
import com.github.curiousoddman.rgxgen.iterators.StringIterator;

import java.math.BigInteger;
import java.util.Optional;
import java.util.Random;

public class GgxgenTest {
        public static void main(String[] args){
//            RgxGen rgxGen = new RgxGen("[^0-9]*[12]?[0-9]{1,2}[^0-9]*");         // Create generator
            RgxGen rgxGen = new RgxGen("[a-zA-Z]");         // Create generator
            RgxGen rgxGen1 = new RgxGen("[0-9]");         // Create generator
            RgxGen rgxGen2 = new RgxGen("^(0|[1-9][0-9]*)$");         // Create generator
            RgxGen rgxGen3 = new RgxGen("^([1-9][0-9]*)+(.[0-9]{1,2})?$");         // Create generator
            RgxGen rgxGen4 = new RgxGen("^\\w+$");         // Create generator
            String s = rgxGen.generate();                                        // Generate new random value
            String s1 = rgxGen1.generate();                                        // Generate new random value
            String s2 = rgxGen2.generate();                                        // Generate new random value
            String s3 = rgxGen3.generate();                                        // Generate new random value
            String s4 = rgxGen4.generate();                                        // Generate new random value
//            Optional<BigInteger> estimation = rgxGen.getUniqueEstimation();      // The estimation (not accurate, see Limitations) how much unique values can be generated with that pattern.
//            StringIterator uniqueStrings = rgxGen.iterateUnique();               // Iterate over unique values (not accurate, see Limitations)
//            String notMatching = rgxGen.generateNotMatching();                   // Generate not matching string

            //System.out.println(s);
            //System.out.println(s1);
            //System.out.println(s2);
            //System.out.println(s3);
            //System.out.println(s4);
//            //System.out.println(uniqueStrings);
//            //System.out.println(notMatching);
        }

        public static void main2(String[] args){
            RgxGen rgxGen = new RgxGen("[^0-9]*[12]?[0-9]{1,2}[^0-9]*");         // Create generator
            Random rnd = new Random(1234);
            String s = rgxGen.generate(rnd);                                     // Generate first value
            String s1 = rgxGen.generate(rnd);                                    // Generate second value
            String s2 = rgxGen.generate(rnd);                                    // Generate third value
            String notMatching = rgxGen.generateNotMatching(rnd);                // Generate not matching string
            // On each launch s, s1 and s2 will be the same
        }
}
