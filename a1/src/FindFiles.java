import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.lang.String;
import java.io.File;
import java.util.regex.Pattern;

public class FindFiles {
    static boolean RegEx = false, Recursive = false, found = false;
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
    static HashMap<String, String> parse(String[] args){
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
                } else if (key.equals("dir") || key.equals("ext")){
                    continue;
                } else {
                    System.out.println("Invalid argument: " + key + ". Exiting.");
                    printHelp();
                    System.exit(1);
                }
            } else {
                if (value != null){
                    System.out.println(args[i] + " is not a valid argument. ");
                }
                value = args[i];
                if (i == 0) value = null;
            }
            if (key != null && value != null){
                arguments.put(key, value);
                key = null;
                value = null;
            }
        }
        //  check final values
        if (key != null) {
            System.out.println("No argument provided after -" + key + ".");
            printHelp();
        }
        if (value != null) System.out.println("No key for value " + value);

        return arguments;
    }

    public static void SearchFile(File aFile){
        String tgtFileName = aFile.getName();
        if (RegEx){
            Pattern p = Pattern.compile(FileName);
            if (Extension != null) {
                for (String ext : Extension) {
                    if (p.matcher(tgtFileName).matches() && aFile.isFile() && tgtFileName.endsWith(ext)) {
                        System.out.println("Matched: " + aFile.getAbsolutePath());
                        found = true;
                    }
                }
            } else {
                if (p.matcher(tgtFileName).matches() && aFile.isFile()) {
                    System.out.println("Matched: " + aFile.getAbsolutePath());
                    found = true;
                }
            }
        } else {
            if (Extension != null) {
                for (String ext : Extension) {
                    if (tgtFileName.equals(FileName + "." + ext) && aFile.isFile()) {
                        System.out.println("Found: " + aFile.getAbsolutePath());
                        found = true;
                    }
                }
            } else {
                if (tgtFileName.equals(FileName) && aFile.isFile()) {
                    System.out.println("Found: " + aFile.getAbsolutePath());
                    found = true;
                }
            }
        }
    }

    public static void SearchRecursive(File[] files){
        for (File aFile : files){
            if (aFile.isFile()){
                SearchFile(aFile);
            } else if (aFile.isDirectory()){
                if (!aFile.canRead()) {
                    System.out.println("Cannot read the directory " + Directory + ": Permission Denied.");
                    return;
                }
                System.out.println("Searching subdirectory: " + aFile.getName() + " ...");
                File[] list = aFile.listFiles();
                if (list != null) SearchRecursive(list);
            }
        }
    }

    public static void main(String[] args){
        if (args.length == 0){
            printHelp();
        } else {
            // extract options from command line
            java.util.HashMap<String, String> options = parse(args);
            // print them to verify that it worked
            for (Map.Entry entry : options.entrySet()){
                if (entry.getKey().equals("ext")) Extension = entry.getValue().toString().split(",");
                if (entry.getKey().equals("dir")) Directory = entry.getValue().toString();
            }

            System.err.println("FileName " + FileName);
            System.err.println("Recursive " + Recursive);
            System.err.println("RegEx " + RegEx);
            System.err.println("Directory " + Directory);
            System.err.println("Extension:");
            if (Extension != null) {
                for (String strings : Extension){
                    System.err.println(strings);
                }
            }

            try {
                File dir = new File(Directory);
                if (!dir.canRead()) {
                    System.out.println("Cannot read the directory " + Directory + ": Permission Denied.");
                    System.exit(0);
                }
                File[] files = dir.listFiles();
                if (files != null) {
                    if (Recursive) {
                        System.out.println("Searching in current directory... ");
                        SearchRecursive(files);
                    } else {
                        System.out.println("Trying to find file named: " + FileName + " in " + Directory);
                        File search = new File(FileName);
                        if (!dir.exists() || !dir.isDirectory()) {
                            System.out.println(Directory + " is not a valid directory. ");
                            System.exit(0);
                        }
                        for (File aFile : files) {
                            SearchFile(aFile);
                        }
                    }
                }
                if (!found) {
                    System.out.print(FileName + " is not ");
                    if (RegEx) System.out.print("matched");
                    else System.out.print("found");
                    System.out.print(" in " + dir.getCanonicalPath());
                    if (Recursive) System.out.print("Recursively");
                }
            } catch (Exception ex){
                System.out.println(ex.toString());
            }
        }
    }
}

