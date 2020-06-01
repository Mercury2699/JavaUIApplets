import java.util.HashMap;
import java.util.Map;
import java.lang.String;
import java.io.File;

public class FindFiles {
    static boolean RegEx = false, Recursive = false;
    static String FileName = null, Directory = ".";
    static String [] Extension = null;

    public static void printHelp(){ // Print command-line syntax
        System.out.println("Usage: java FindFiles filetofind [-option arg]");
        System.out.println("-help                  :: print out a help page and exit the program.");
        System.out.println("-reg                   :: find files using [filietofind] argument as a regular expression.");
        System.out.println("-r                     :: execute the command recursively in subfiles. ");
        System.out.println("-dir [directory]       :: find the files the specified directory [directory]. Default directory is the calling directory. ");
        System.out.println("-ext [ext1,ext2,...]   :: find the files matching [filetofind] and with the given extensions [ext1, ext2,...]");
    }

    // Build a dictionary of key:value pairs (without the leading "-")
    static HashMap<String, String> parse(String[] args) {
        HashMap<String, String> arguments = new HashMap<>();
        String key = null, value = null;
        FileName = args[0];
        // process each argument as either a key or value in the pair
        for (int i = 0; i < args.length; i++){
            // assume that keys start with a dash
            if (args[i].startsWith("-")){
                // if we already have a key, and then find a second key
                // before we've found the corresponding value, it's an error.
                if (i == 0) {
                    System.out.println("No file name to find provided.");
                    System.exit(0);
                }
                key = args[i].substring(1); // skip leading "-"
                if (key.equals("help")){ // Print help and exit if there's help, ignore anything else.
                    printHelp();
                    System.exit(0);
                } else if (key.equals("r")){
                    Recursive = true;
                    key = null;
                } else if (key.equals("reg")){
                    RegEx = true;
                    key = null;
                } else if (key.equals("dir")){
                    continue;
                } else if (key.equals("ext")){
                    continue;
                } else {
                    System.out.println("Invalid argument: " + key + ". Exiting. Use \"-help\" for usage. ");
                    System.exit(1);
                }
            } else {
                if (value != null) System.out.println("Duplicate value found. Skipping value " + value);
                value = args[i];
                if (i == 0) value = null;
            }
            if (key != null && value != null) {
                arguments.put(key, value);
                key = null;
                value = null;
            }
        }
        //  check final values
        if (key != null) System.out.println("No argument provided after -" + key + ".");
        if (value != null) System.out.println("No key for value " + value);

        return arguments;
    }

    public static void main(String[] args){
        if (args.length == 0){
            printHelp();
        } else {
            // extract options from command line
            java.util.HashMap<String, String> options = parse(args);
            // print them to verify that it worked
            for (Map.Entry entry : options.entrySet()) {
                if (entry.getKey().equals("ext")) Extension = entry.getValue().toString().split(",");
                if (entry.getKey().equals("dir")) Directory = entry.getValue().toString();
            }

            System.out.println("FileName " + FileName);
            System.out.println("Recursive " + Recursive);
            System.out.println("RegEx " + RegEx);
            System.out.println("Directory " + Directory);
            System.out.println("Extension:");
            for (String strings : Extension){
                System.out.println(strings);
            }

            System.out.println("Trying to find file named: " + FileName);
            File search = new File(FileName);
            if (!search.exists() || !search.isFile()){
                System.out.println(args[0] + " is not found. ");
                System.exit(0);
            }
            System.out.println("Absolute path: " + search.getAbsolutePath());
        }
    }
}
