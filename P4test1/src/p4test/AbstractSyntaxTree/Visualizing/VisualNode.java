package p4test.AbstractSyntaxTree.Visualizing;

import p4test.AbstractSyntaxTree.AST;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

/**
 * Created by Anders Brams on 3/15/2017.
 */
public class VisualNode extends PApplet {
    AST base;
    PVector pos;
    PVector textPos;
    ArrayList<VisualNode> children;
    int bubbleWidth = 0;
    int defaultXOffset = 85;
    int defaultYOffset = 50;

    public VisualNode(AST node, PVector position){
        pos = position;
        base = node;
        children = new ArrayList<VisualNode>();
    }

    /* Assings positions to the nodes based on the
     * number of children a given node has */
    public void AssignPositionsToChildren(ArrayList<VisualNode> tree){
        int currentXOffset = -defaultXOffset;

        if (base.GetValue() != null)
            bubbleWidth = base.GetValue().length() * 12;

        textPos = new PVector((int)(pos.x-bubbleWidth*0.25) , pos.y+5);

        for (AST n : base.children){
            PVector visPos = new PVector(pos.x+currentXOffset, pos.y + defaultYOffset);
            VisualNode visNode = new VisualNode(n, visPos);

            for (VisualNode node : tree){
                if ( visNode.pos.x == node.pos.x && visNode.pos.y == node.pos.y ){
                    visNode.pos.x += defaultXOffset;
                    currentXOffset += defaultXOffset;
                    break;
                }
            }
            tree.add(visNode);
            children.add(visNode);
            visNode.AssignPositionsToChildren(tree);

            currentXOffset += defaultXOffset;
        }

        PositionCleanup(tree);
    }

    void PositionCleanup(ArrayList<VisualNode> tree){
        for (int i = tree.size()-1; i >= 0; i--){
            VisualNode n = tree.get(i);
            for (int k = i-1; k >= 0; k--){
                VisualNode m = tree.get(k);
                if (n.pos.x == m.pos.x && n.pos.y == m.pos.y){
                    n.pos.x += defaultXOffset;
                    n.textPos.x += defaultXOffset;
                }
            }
        }
    }

    /* Recursively displays all nodes in the tree */
    public void Show(PApplet ProcessingInstance){

        if (base.GetValue() != null){
            ProcessingInstance.ellipse(pos.x, pos.y, bubbleWidth, 30);
            ProcessingInstance.fill(0); //Black draw color (for the text)
            ProcessingInstance.text(base.GetValue(), textPos.x, textPos.y);
            ProcessingInstance.fill(200); //White draw color (for everything else)
        }

        for (VisualNode n : children){
            if (n.base.GetValue() != null) {
                ProcessingInstance.line(pos.x, pos.y + 15, n.pos.x, n.pos.y - 15);
                n.Show(ProcessingInstance);
            }
        }
    }
}
