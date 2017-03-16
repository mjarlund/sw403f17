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
    ArrayList<VisualNode> children;
    int bubbleWidth = 0;

    public VisualNode(AST node, PVector position){
        pos = position;
        base = node;
        children = new ArrayList<VisualNode>();
    }

    /* Assings positions to the nodes based on the
     * number of children a given node has */
    public void AssignPositionsToChildren(){
        int numChildren = base.children.size();
        int childPosX = (int)( pos.x - ( numChildren * 100 * 0.5) + 50); //Don't ask
        int childPosY = (int)(pos.y + 100);

        for (AST currentChild : base.children){
            PVector newPos = new PVector(childPosX, childPosY);
            VisualNode visChild = new VisualNode(currentChild, newPos);
            childPosX += 120;
            children.add(visChild);
            visChild.AssignPositionsToChildren();
        }
    }

    /* Recursively displays all nodes in the tree */
    public void Show(PApplet ProcessingInstance){

        if (base.GetValue() != null){
            bubbleWidth = base.GetValue().length() * 18;
        }

        ProcessingInstance.ellipse(pos.x, pos.y, bubbleWidth, 30);
        ProcessingInstance.fill(0); //Black
        if (base.GetValue() != null) ProcessingInstance.text(base.GetValue(),
                (int)(pos.x-bubbleWidth*0.25) , pos.y+5); //Just so it looks good
        ProcessingInstance.fill(255); //White

        for (VisualNode n : children){
            ProcessingInstance.line(pos.x, pos.y+15, n.pos.x, n.pos.y);
            n.Show(ProcessingInstance);
        }
    }
}
