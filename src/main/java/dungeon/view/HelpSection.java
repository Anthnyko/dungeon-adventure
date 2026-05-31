package dungeon.view;

public class HelpSection {

    // Text Colors 
    private static final String TEXT_COLOR_RESET    = "\u001B[0m";
    private static final String TEXT_RED            = "\u001B[31m";
    private static final String TEXT_GREEN          = "\u001B[32m";
    private static final String TEXT_YELLOW         = "\u001B[33m";
    private static final String TEXT_BLUE           = "\u001B[34m";
    private static final String TEXT_PINK           = "\u001B[35m";
    private static final String TEXT_PURPLE         = "\u001B[36m";
    private static final String TEXT_BLACK          = "\u001B[30m";
    private static final String TEXT_DIM            = "\u001B[2m";

    private static final String NEWLINE             = System.lineSeparator();

    private static final int TERMINAL_WIDTH = 60;




    private final void printHelpBanner() {
        System.out.println(
            "─── Help & Reference ───" 
        );
    }

    private final void printCatagory(String theName) {
        System.out.println("── " + theName + " " + "─".repeat(Math.max(0, TERMINAL_WIDTH - theName.length() - 6)));
    }

    private final void printCommand(String theCommand, String theDesription) {
        System.out.printf(TEXT_BLUE + "%-22s" + TEXT_COLOR_RESET + " %s%n", theCommand, theDesription);
    }

    private final void printHeader(String theHeader) {
        System.out.println(
            NEWLINE
            + TEXT_YELLOW + theHeader + TEXT_COLOR_RESET
        );
    }

    private final void printDivider() {
        System.out.println(TEXT_DIM + "  " + "─".repeat(TERMINAL_WIDTH) + TEXT_COLOR_RESET);
    }


    public final void printCommands() {

    }
}
