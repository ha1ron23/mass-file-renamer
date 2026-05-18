import java.io.*;
import java.nio.file.*;
import java.util.regex.Pattern;

public class main {
    private static boolean dryRun = false;
    private static boolean verbose = false;
    private static boolean recursive = false;
    private static boolean lowerCase = false;
    private static boolean upperCase = false;
    private static String prefix = "";
    private static String suffix = "";
    private static Pattern regexPattern = null;
    private static String replacement = "";
    private static Path startDir = Paths.get(".");

    public static void main(String[] args) throws IOException {
        parseArgs(args);
        if (regexPattern == null && prefix.isEmpty() && suffix.isEmpty() && !lowerCase && !upperCase) {
            System.err.println("No operation specified. Use --regex, --prefix, --suffix, --lower, --upper");
            printUsage();
            System.exit(1);
        }
        Files.walk(startDir, recursive ? Integer.MAX_VALUE : 1)
             .filter(Files::isRegularFile)
             .forEach(main::processFile);
    }

    private static void parseArgs(String[] args) {
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--dir": startDir = Paths.get(args[++i]); break;
                case "--regex": regexPattern = Pattern.compile(args[++i]); break;
                case "--replace": replacement = args[++i]; break;
                case "--prefix": prefix = args[++i]; break;
                case "--suffix": suffix = args[++i]; break;
                case "--lower": lowerCase = true; break;
                case "--upper": upperCase = true; break;
                case "--recursive": recursive = true; break;
                case "--dry-run": dryRun = true; break;
                case "--verbose": verbose = true; break;
                case "--help": printUsage(); System.exit(0);
                default: System.err.println("Unknown option: " + args[i]); printUsage(); System.exit(1);
            }
        }
    }

    private static void processFile(Path file) {
        try {
            String oldName = file.getFileName().toString();
            String newName = oldName;
            if (regexPattern != null) {
                newName = regexPattern.matcher(newName).replaceAll(replacement);
            }
            if (!prefix.isEmpty()) newName = prefix + newName;
            if (!suffix.isEmpty()) {
                int dot = newName.lastIndexOf('.');
                if (dot > 0) newName = newName.substring(0, dot) + suffix + newName.substring(dot);
                else newName = newName + suffix;
            }
            if (lowerCase) newName = newName.toLowerCase();
            if (upperCase) newName = newName.toUpperCase();
            if (newName.equals(oldName)) {
                if (verbose) System.out.println("Skipped (no change): " + file);
                return;
            }
            Path target = file.resolveSibling(newName);
            if (Files.exists(target)) {
                System.err.println("Conflict: " + target + " already exists. Skipping " + file);
                return;
            }
            if (dryRun) {
                System.out.println("DRY RUN: " + file + " -> " + target.getFileName());
            } else {
                Files.move(file, target);
                if (verbose) System.out.println("Renamed: " + file + " -> " + target.getFileName());
            }
        } catch (IOException e) {
            System.err.println("Error processing " + file + ": " + e.getMessage());
        }
    }

    private static void printUsage() {
        System.out.println("""
            MassRenamer - Bulk file renamer
            Usage: java main [options]
            Options:
              --dir <path>         Working directory (default: current)
              --regex <pattern>    Regular expression to search
              --replace <text>     Replacement text (default: empty)
              --prefix <text>      Add prefix to name
              --suffix <text>      Add suffix before extension
              --lower              Convert names to lowercase
              --upper              Convert names to uppercase
              --recursive          Process subdirectories
              --dry-run            Preview changes without renaming
              --verbose            Show detailed output
              --help               Show this help
            Examples:
              java main --dir ./photos --regex "\\\\.JPG$" --replace ".jpg"
              java main --prefix "vacation_" --dry-run
              java main --regex "\\\\d+" --replace "pic" --recursive
            """);
    }
}