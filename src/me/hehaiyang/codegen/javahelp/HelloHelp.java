package me.hehaiyang.codegen.javahelp;

import javax.help.CSH;
import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.help.HelpSetException;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.net.URL;

public class HelloHelp {

  public static void main(String args[]) {

    JButton overview = new JButton();

    HelpSet helpset;
    ClassLoader loader = null;
    URL url = HelpSet.findHelpSet(loader, "javahelp/hello.hs");
    try {
      helpset = new HelpSet(loader, url);
    } catch (HelpSetException e) {
      System.err.println("Error loading");
      return;
    }

    HelpBroker helpbroker = helpset.createHelpBroker();
    ActionListener listener = new CSH.DisplayHelpFromSource(helpbroker);
    overview.addActionListener(listener);
    overview.doClick();
  }

}