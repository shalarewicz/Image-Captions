/* Copyright (c) 2017 MIT 6.031 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package memely;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * Console interface to the expression system.
 * 
 * <p>PS3 instructions: you are free to change this user interface class.
 */
public class Main {
    
    /**
     * Read expression and command inputs from the console and output results.
     * An empty input terminates the program.
     * @param args unused
     * @throws IOException if there is an error reading the input
     */
    public static void main(String[] args) throws IOException {
        final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        Optional<String> currentExpression = Optional.empty();

        while (true) {
            System.out.print("> ");
            final String input = in.readLine();
            
            if (input.isEmpty()) {
                System.exit(0); // exits the program
            }

            try {
                final String output;
                
                if (input.equals(LAYOUT_COMMAND)) {
                    output = Commands.layout(currentExpression.get());
                    currentExpression = Optional.of(output);
                    System.out.println(output);
                } else if (input.equals(GENERATE_COMMAND)) {
                    showInWindow(currentExpression.get(), Commands.generate(currentExpression.get()));
                    // ... but don't change currentExpression
                } else {
                    final Expression expression = Expression.parse(input);
                    output = expression.toString();
                    currentExpression = Optional.of(output);
                    System.out.println(output);
                }
                
            } catch (NoSuchElementException nse) {
                // currentExpression was empty
                System.out.println("must enter an expression before using this command");
            } catch (RuntimeException re) {
                System.out.println(re.getClass().getName() + ": " + re.getMessage());
            }
        }
    }
    
    private static final String LAYOUT_COMMAND = "!layout";
    private static final String GENERATE_COMMAND = "!generate";

    /**
     * Display an image in a new window exactly sized to fit the image.
     * @param windowTitle title for window's titlebar
     * @param image image to display
     */
    private static void showInWindow(final String windowTitle, final BufferedImage image) {
        JFrame frame = new JFrame(windowTitle);
        frame.add(new JLabel(new ImageIcon(image)));
        frame.pack();
        frame.setVisible(true);
    }

}
