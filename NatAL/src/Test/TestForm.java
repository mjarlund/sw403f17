package Test;

import CodeGeneration.CodeGenerator;
import DataStructures.AST.AST;
import Semantics.Scope.SemanticAnalyzer;
import Syntax.Parser.Parser;
import Syntax.Scanner.Scanner;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class TestForm {
    private JPanel Test;
    private JButton button1;
    private JButton button2;
    private JTextArea textArea1;
    private JTextArea textArea2;
    private JList list1;
    private JTextField textField1;
    private JTextField textField2;
    int index = 0;
    int max = 0;
    ArrayList data = new ArrayList();
    public TestForm() {

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
            JOptionPane.showMessageDialog(null, "Error when reading files: " + e.getMessage());
            System.out.println("Error when reading files: " + e.getMessage());

        }
        max = data.size();
        Update();


        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                    Update();
            }
        });

        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (index < data.size()) {
                    d.addElement(data.get(index));
                    list1.setModel(d);
                }
                Update();
            }
        });
    }

    private void Update(){
        if (index < data.size()) {
            try {
                String file = data.get(index).toString();
                textField1.setText(file);
                textArea1.setText(InputTester.readFile(file));
                textField2.setText(index+1 +" / " + max);
                Scanner sc = new Scanner(InputTester.readFile(file));
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
            index++;
        }
            else {
            JOptionPane.showMessageDialog(null, "No more files");
        }
    }

    public static void main(String args[]) {
        JFrame frame = new JFrame("test");
        frame.setSize(600, 600);
        frame.setContentPane(new TestForm().Test);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
