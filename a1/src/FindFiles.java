
import java.util.HashMap;
import java.util.Map;
import java.lang.String;
import java.io.File;

public class FindFiles {
    public static void printHelp(){ // Print command-line syntax
        System.out.println("-help                     :: print out a help page and exit the program.");
        System.out.println("-reg                      :: find files using [filietofind] argument as a regular expression.");
        System.out.println("-r                        :: execute the command recursively in subfiles. ");
        System.out.println("-dir [directory]          :: find the files the specified directory [directory]. Default directory is the calling directory. ");
        System.out.println("-ext [ext1,ext2,...]      :: find the files matching [filetofind] and with the given extensions [ext1, ext2,...]");
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            printHelp();
        } else {
            // extract options from command line
            // IntelliJ passes sample arguments if you run from the IDE (Run-Edit Configuration)
            java.util.HashMap<String, String> options = parse(args);

            // print them to verify that it worked
            for (Map.Entry entry : options.entrySet()) {
                System.out.println(entry.getKey() + "=" + entry.getValue());
            }
        }
    }

    // Build a dictionary of key:value pairs (without the leading "-")
    static HashMap<String, String> parse(String[] args) {
        HashMap<String, String> arguments = new HashMap<>();
        String key = null;
        String value = null;

        // process each argument as either a key or value in the pair
        for(String entry : args) {
            // assume that keys start with a dash
            if (entry.startsWith("-")) {
                // if we already have a key, and then find a second key
                // before we've found the corresponding value, it's an error.
                if (key != null) {
                    System.out.println("Invalid key:pair, skipping key " + key);
                }
                key = entry.substring(1);   // skip leading "-"

                // values start with anything else
            } else {
                // if we already have a key, and then find a second key
                // before we've found the corresponding value, it's an error.
                if (value != null) {
                    System.out.println("Duplicate value found. Skipping value " + value);
                }
                value = entry;
            }
            if (key != null && value != null) {
                arguments.put(key, value);
                key = null;
                value = null;
            }
        }

        //  check final values
        if (key != null) System.out.println("Invalid key:pair, skipping key " + key);
        if (value != null) System.out.println("No key fo value " + value);

        // return dictionary
        return arguments;
    }
}

