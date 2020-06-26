package loanbroker;

import javax.swing.*;
import java.awt.*;

public class SwingTestMain {

    public static void main(String[] args) {
        JFrame frame = new JFrame();

        JTextField text1 = new JTextField("Input 1");
        JTextField text2 = new JTextField("Input 2");
        JTextField text3 = new JTextField("Input 3");

        frame.add(text1);
        frame.add(text2);
        frame.add(text3);

        frame.add(new Button("send request"));

        frame.setLayout(new GridLayout(4,1));
        frame.setSize(640,480);
        frame.setVisible(true);

    }

}
