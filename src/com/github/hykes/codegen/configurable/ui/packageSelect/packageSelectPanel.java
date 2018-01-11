package com.github.hykes.codegen.configurable.ui.packageSelect;

import com.intellij.codeInsight.intention.impl.config.IntentionActionMetaData;
import com.intellij.codeInsight.intention.impl.config.IntentionDescriptionPanel;
import com.intellij.codeInsight.intention.impl.config.IntentionManagerSettings;
import com.intellij.codeInsight.intention.impl.config.TextDescriptor;
import com.intellij.ide.ui.search.SearchUtil;
import com.intellij.ide.ui.search.SearchableOptionsRegistrar;
import com.intellij.openapi.options.MasterDetails;
import com.intellij.openapi.ui.DetailsComponent;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.GuiUtils;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.util.Alarm;
import org.jetbrains.annotations.NonNls;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class packageSelectPanel implements MasterDetails {
  private JPanel myPanel;
  private final TemplateGroupTree myIntentionSettingsTree;
  private final IntentionDescriptionPanel myIntentionDescriptionPanel = new IntentionDescriptionPanel();

  private JPanel myTreePanel;
  private JPanel myDescriptionPanel;
  private DetailsComponent myDetailsComponent;

  private final Alarm myResetAlarm = new Alarm();

  public packageSelectPanel() {
    myIntentionSettingsTree = new TemplateGroupTree() {
      @Override
      protected void selectionChanged(Object selected) {
        if (selected instanceof IntentionActionMetaData) {
          final IntentionActionMetaData actionMetaData = (IntentionActionMetaData) selected;
          final Runnable runnable = () -> {
            intentionSelected(actionMetaData);
            if (myDetailsComponent != null) {
              String[] text = new String[actionMetaData.myCategory.length + 1];
              System.arraycopy(actionMetaData.myCategory, 0, text, 0, actionMetaData.myCategory.length);
              text[text.length - 1] = actionMetaData.getFamily();
              myDetailsComponent.setText(text);
            }
          };
          myResetAlarm.cancelAllRequests();
          myResetAlarm.addRequest(runnable, 100);
        } else {
          categorySelected((String) selected);
          if (myDetailsComponent != null) {
            myDetailsComponent.setText((String) selected);
          }
        }
      }

      @Override
      protected List<IntentionActionMetaData> filterModel(String filter, final boolean force) {
        final List<IntentionActionMetaData> list = IntentionManagerSettings.getInstance().getMetaData();
        if (filter == null || filter.length() == 0) return list;
        final HashSet<String> quoted = new HashSet<>();
        List<Set<String>> keySetList = SearchUtil.findKeys(filter, quoted);
        List<IntentionActionMetaData> result = new ArrayList<>();
        for (IntentionActionMetaData metaData : list) {
          if (isIntentionAccepted(metaData, filter, force, keySetList, quoted)) {
            result.add(metaData);
          }
        }
        final Set<String> filters = SearchableOptionsRegistrar.getInstance().getProcessedWords(filter);
        if (force && result.isEmpty()) {
          if (filters.size() > 1) {
            result = filterModel(filter, false);
          }
        }
        return result;
      }
    };
    myTreePanel.setLayout(new BorderLayout());
    myTreePanel.add(myIntentionSettingsTree.getComponent(), BorderLayout.CENTER);

    GuiUtils.replaceJSplitPaneWithIDEASplitter(myPanel);

    myDescriptionPanel.setLayout(new BorderLayout());
    myDescriptionPanel.add(myIntentionDescriptionPanel.getComponent(), BorderLayout.CENTER);
  }

  private void intentionSelected(IntentionActionMetaData actionMetaData) {
    myIntentionDescriptionPanel.reset(actionMetaData, myIntentionSettingsTree.getFilter());
  }

  private void categorySelected(String intentionCategory) {
    myIntentionDescriptionPanel.reset(intentionCategory);
  }

  public void reset() {
    myIntentionSettingsTree.reset();
    SwingUtilities.invokeLater(() -> myIntentionDescriptionPanel.init(myPanel.getWidth() / 2));
  }

  @Override
  public void initUi() {
    myDetailsComponent = new DetailsComponent();
    myDetailsComponent.setContent(myDescriptionPanel);
  }

  @Override
  public JComponent getToolbar() {
    return myIntentionSettingsTree.getToolbarPanel();
  }

  @Override
  public JComponent getMaster() {
    return myTreePanel;
  }

  @Override
  public DetailsComponent getDetails() {
    return myDetailsComponent;
  }

  public void apply() {
    myIntentionSettingsTree.apply();
  }

  public JPanel getComponent() {
    return myPanel;
  }

  public JTree getIntentionTree() {
    return myIntentionSettingsTree.getTree();
  }

  public boolean isModified() {
    return myIntentionSettingsTree.isModified();
  }

  public void dispose() {
    myIntentionSettingsTree.dispose();
    myIntentionDescriptionPanel.dispose();
  }

  public void selectIntention(String familyName) {
    myIntentionSettingsTree.selectIntention(familyName);
  }

  private static boolean isIntentionAccepted(IntentionActionMetaData metaData, @NonNls String filter, boolean forceInclude,
                                             final List<Set<String>> keySetList, final HashSet<String> quoted) {
    if (StringUtil.containsIgnoreCase(metaData.getFamily(), filter)) {
      return true;
    }
    for (String category : metaData.myCategory) {
      if (category != null && StringUtil.containsIgnoreCase(category, filter)) {
        return true;
      }
    }
    for (String stripped : quoted) {
      if (StringUtil.containsIgnoreCase(metaData.getFamily(), stripped)) {
        return true;
      }
      for (String category : metaData.myCategory) {
        if (category != null && StringUtil.containsIgnoreCase(category, stripped)) {
          return true;
        }
      }
      try {
        final TextDescriptor description = metaData.getDescription();
        if (StringUtil.containsIgnoreCase(description.getText(), stripped)) {
          if (!forceInclude) return true;
        } else if (forceInclude) return false;
      } catch (IOException e) {
        //skip then
      }
    }
    for (Set<String> keySet : keySetList) {
      if (keySet.contains(metaData.getFamily())) {
        if (!forceInclude) {
          return true;
        }
      } else {
        if (forceInclude) {
          return false;
        }
      }
    }
    return forceInclude;
  }

  public Runnable showOption(final String option) {
    return () -> {
      myIntentionSettingsTree.filter(myIntentionSettingsTree.filterModel(option, true));
      myIntentionSettingsTree.setFilter(option);
    };
  }

  public static void main(String[] args) {
    JFrame jFrame = new JFrame();
    jFrame.getContentPane().add(new packageSelectPanel().getComponent(), BorderLayout.CENTER);
    jFrame.setSize(300, 240);
    jFrame.setLocation(300, 200);
    jFrame.setVisible(true);
    jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
    $$$setupUI$$$();
  }

  /**
   * Method generated by IntelliJ IDEA GUI Designer
   * >>> IMPORTANT!! <<<
   * DO NOT edit this method OR call it in your code!
   *
   * @noinspection ALL
   */
  private void $$$setupUI$$$() {
    myPanel = new JPanel();
    myPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
    final JSplitPane splitPane1 = new JSplitPane();
    splitPane1.setDividerLocation(350);
    splitPane1.setOneTouchExpandable(true);
    splitPane1.setResizeWeight(1.0);
    myPanel.add(splitPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
    myDescriptionPanel = new JPanel();
    myDescriptionPanel.setLayout(new GridLayoutManager(1, 1, new Insets(5, 5, 5, 5), -1, -1));
    splitPane1.setRightComponent(myDescriptionPanel);
    final JPanel panel1 = new JPanel();
    panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 5), -1, -1));
    splitPane1.setLeftComponent(panel1);
    myTreePanel = new JPanel();
    panel1.add(myTreePanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
  }

  /**
   * @noinspection ALL
   */
  public JComponent $$$getRootComponent$$$() {
    return myPanel;
  }
}
