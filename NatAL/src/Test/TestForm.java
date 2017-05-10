package Test;

import CodeGeneration.CodeGenerator;
import DataStructures.AST.AST;
import Semantics.Scope.SemanticAnalyzer;
import Syntax.Parser.Parser;
import Syntax.Scanner.Scanner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;


public class TestForm {
    private JPanel Test;
    private JButton button1;
    private JButton button2;
    private JTextArea textArea1;
    private JTextArea textArea2;
    private JList list1;
    private JTextField textField1;
    int index = 0;

    public TestForm() {
        ArrayList data = new ArrayList();
        DefaultListModel d = new DefaultListModel();
        try{
            File folder = new File("src/Test/TestPrograms/semantics/");
            File[] listOfFiles = folder.listFiles();

            for (File file : listOfFiles) {
                if (file.isFile() && !file.getName().contains("fail"))
                {
                    data.add(file.getPath());

                }
            }
        }
        catch (Exception e){//Catch exception if any
            System.out.println("Error when reading files: " + e.getMessage());
        }
        if (index < data.size())
            Update(data.get(index).toString());


        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                index++;
                if (index < data.size())
                    Update(data.get(index).toString());
                else
                    JOptionPane.showMessageDialog(null, "No more files");
            }
        });

        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                d.addElement(data.get(index));
                list1.setModel(d);
                index++;
                if (index < data.size())
                    Update(data.get(index).toString());
                else
                    JOptionPane.showMessageDialog(null, "No more files");
            }
        });
    }

    private void Update(String data){
        try {
            textField1.setText(data);
            textArea1.setText(InputTester.readFile(data));

            Scanner sc = new Scanner(InputTester.readFile(data));
            Parser parser = new Parser(sc);
            AST programTree = parser.ParseProgram();
            SemanticAnalyzer sm = new SemanticAnalyzer();
            sm.BeginSemanticAnalysis(programTree);
            CodeGenerator c = new CodeGenerator(programTree, sm);
            c.ToFile();

            textArea2.setText(InputTester.readFile("Arduino-C-Program.txt"));

        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public static void main(String args[]) {
        JFrame frame = new JFrame("test");
        frame.setSize(600, 600);
        frame.setContentPane(new TestForm().Test);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
