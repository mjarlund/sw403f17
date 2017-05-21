
        import CodeGeneration.CodeGenerator;
        import DataStructures.AST.AST;
        import Semantics.Scope.SemanticAnalyzer;
        import Syntax.Parser.Parser;
        import Syntax.Scanner.Scanner;
        import Test.InputTester;

        import javax.swing.*;
        import java.awt.*;
        import java.awt.event.ActionEvent;
        import java.awt.event.ActionListener;
        import java.io.IOException;

        public class Program extends JFrame
{
    private JTextArea Input = new JTextArea(30,20);
    private JTextArea Output = new JTextArea(30,20);
    private JTextArea ErrorText = new JTextArea(10, 20);

    private JButton buttonCompile = new JButton("Compile");

    public Program() {
        super("JPanel Program");
        // create a new panel with GridBagLayout manager
        JPanel newPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        // add components to the panel
        constraints.gridx = 3;
        constraints.gridy = 0;
        constraints.weightx =1;
        buttonCompile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    ErrorText.setText("");
                    Output.setText(Compile(Input.getText()));
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(null, e1);
                }
            }
        } );
        newPanel.add(buttonCompile, constraints);

        JScrollPane pane = new JScrollPane(Input);
        constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.NORTH;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridwidth = 3;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.weightx = 2;
        newPanel.add(pane, constraints);


        JScrollPane pane1 = new JScrollPane(Output);
        constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.NORTH;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridwidth = 3;
        constraints.gridx = 4;
        constraints.gridy = 0;
        constraints.insets = new Insets(0, 10, 0, 10);
        constraints.weightx = 2;
        newPanel.add(pane1, constraints);


        JScrollPane pane2 = new JScrollPane(ErrorText);
        constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.NORTH;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridwidth = 7;
        constraints.gridx = 0;
        constraints.gridy = 10;
        constraints.insets = new Insets(10, 10, 0, 10);
        constraints.weightx = 2;
        newPanel.add(pane2, constraints);


        // set border for the panel
        newPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "NATaL Compiler"));

        // add the panel to this frame
        add(newPanel);

        pack();
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
               Program p = new Program();p.setVisible(true);
                p.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
    private String Compile(String input) throws IOException {

        try {
            Scanner sc = new Scanner(input);
            Parser parser = new Parser(sc);
            AST programTree = parser.ParseProgram();
            SemanticAnalyzer sm = new SemanticAnalyzer();
            sm.BeginSemanticAnalysis(programTree);
            CodeGenerator c = new CodeGenerator(programTree, sm);
            c.ToFile();
            ErrorText.setText("Finished with no errors!");
            return InputTester.readFile("Arduino-C-Program.txt");

        } catch (Error e) {
            ErrorText.setText(ErrorText.getText()+"\n"+e);
        }catch (Exception e) {
            ErrorText.setText(ErrorText.getText()+"\n"+e);
        }

return "";
    }
}