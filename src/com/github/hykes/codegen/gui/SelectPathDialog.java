package com.github.hykes.codegen.gui;

import com.intellij.ide.util.PackageChooserDialog;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.PackageChooser;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.JavaDirectoryService;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiPackage;
import com.intellij.psi.impl.file.PsiDirectoryFactory;

import javax.swing.*;
import java.awt.event.*;
import java.util.Objects;

public class SelectPathDialog extends JDialog implements ActionListener{
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField outPutText;
    private JButton selectOutputBtn;
    private JTextField packageText;
    private JButton selectPackageBtn;
    private String outputPath;
    private String basePackage;

    public SelectPathDialog(Project project) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(this);

        buttonCancel.addActionListener(this);

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        selectOutputBtn.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                FileChooserDescriptor descriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor();
                descriptor.setTitle("Select output path");
                descriptor.setShowFileSystemRoots(false);
                descriptor.setDescription("Select output path");
                descriptor.setHideIgnored(true);
                descriptor.setRoots(project.getBaseDir());
                descriptor.setForcedToUseIdeaFileChooser(true);
                VirtualFile virtualFile = FileChooser.chooseFile(descriptor, project, project.getBaseDir());
                if (Objects.nonNull(virtualFile)) {
                    String output = virtualFile.getPath();
                    PsiDirectory psiDirectory = PsiDirectoryFactory.getInstance(project).createDirectory(virtualFile);
                    PsiPackage psiPackage = JavaDirectoryService.getInstance().getPackage(psiDirectory);
                    if(psiPackage != null && psiPackage.getName() != null) {
                        final StringBuilder path = new StringBuilder();
                        path.append(psiPackage.getName());
                        while (psiPackage.getParentPackage() != null && psiPackage.getParentPackage().getName() != null) {
                            psiPackage = psiPackage.getParentPackage();
                            if (path.length() > 0) {
                                path.insert(0, '.');
                            }
                            path.insert(0, psiPackage.getName());
                        }
                        packageText.setText(path.toString());
                        output = output.replace(path.toString().replace(".", "/"), "");
                    }
                    outPutText.setText(output);
                }
            }
        });
        selectPackageBtn.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                PackageChooser packageChooser = new PackageChooserDialog("Select Base Package", project);
                packageChooser.show();
                PsiPackage psiPackage = packageChooser.getSelectedPackage();
                if (Objects.nonNull(psiPackage)) {
                    packageText.setText(psiPackage.getQualifiedName());
                }
            }
        });
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public String getOutPutPath(){
        return outputPath;
    }

    public String getBasePackage(){
        return basePackage;
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        outputPath = outPutText.getText();
        basePackage = packageText.getText();
        setVisible(false);
    }
}
