# mass-file-renamer

**A command-line tool to rename hundreds of files in seconds.**  
Supports regular expressions, prefixes, suffixes, lowercase/uppercase conversion, recursive mode, and safe dry-run.

## Features

-  **Regex rename** – powerful pattern matching (`--regex`, `--replace`)
-  **Add prefix or suffix** – e.g., `--prefix "2024_"`
-  **Case conversion** – `--lower` or `--upper`
-  **Recursive mode** – process subdirectories with `--recursive`
-  **Dry-run preview** – see what changes without touching files (`--dry-run`)
-  **Verbose output** – detailed logging (`--verbose`)
-  **Conflict avoidance** – never overwrites existing files
-  **No external libraries** – pure Java (standard library only)

##  Quick start

```bash
# Clone the repo
git clone https://github.com/ha1ron23/mass-file-renamer.git
cd mass-file-renamer

# Compile
javac main.java

# Preview: add prefix "vacation_" to all files in ./photos
java main.java --dir ./photos --prefix "vacation_" --dry-run --verbose

# Apply: remove the word "old" from filenames
java main.java --dir ./docs --regex "old" --replace "new"
```

Optionally, create an alias in your shell:
```bash
alias renamer='java -cp /path/to/mass-file-renamer main.java'
```

## Command line options
**--dir <path>	Working directory (default: current)**
**--regex <pattern>	Regular expression to search for**
**--replace <text>	Replacement text (default: empty)**
**--prefix <text>	Add prefix to filename**
**--suffix <text>	Add suffix before extension**
**--lower	Convert names to lowercase**
**--upper	Convert names to uppercase**
**--recursive	Process subdirectories**
**--dry-run	Preview changes without renaming**
**--verbose	Show detailed output**
**--help	Show usage instructions**

## Examples
```bash
# Change extension .JPG to .jpg (case‑insensitive regex)
java main.java --dir ./photos --regex "(?i)\.JPG$" --replace ".jpg"

# Add prefix "2024_" to all files in current folder (preview)
java main.java --prefix "2024_" --dry-run

# Recursively replace spaces with underscores
java main.java --regex " " --replace "_" --recursive

# Convert filenames to lowercase
java main.java --lower

# Remove prefix "temp_" and add suffix "_backup"
java main.java --regex "^temp_" --replace "" --suffix "_backup"
```

## Contributing
Ideas for improvements:

  Support for {counter} and {date} placeholders

  Interactive mode

  File filtering by extension or size

Pull requests are welcome!

## License
MIT License - free to use and modify.
