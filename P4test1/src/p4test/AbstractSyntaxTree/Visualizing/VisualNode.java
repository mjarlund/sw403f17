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

    public VisualNode(AST node, PVector position){
        pos = position;
        base = node;
        children = new ArrayList<VisualNode>();
    }

    public void AssignPositionsToChildren(){
        int numChildren = base.children.size();
        int childPosX = (int)( pos.x - ( numChildren * 100 * 0.5) + 50); //Don't ask
        int childPosY = (int)(pos.y + 100);

        for (AST currentChild : base.children){
            PVector newPos = new PVector(childPosX, childPosY);
            VisualNode visChild = new VisualNode(currentChild, newPos);
            childPosX += 100;
            children.add(visChild);
            visChild.AssignPositionsToChildren();
        }
    }

    public void Show(PApplet ProcessingInstance){
        ProcessingInstance.ellipse(pos.x+5, pos.y-5, 50, 50);
        ProcessingInstance.fill(0); //Black
        if (base.GetValue() != null) ProcessingInstance.text(base.GetValue(), pos.x, pos.y);
        ProcessingInstance.fill(255); //White
        for (VisualNode n : children){
            ProcessingInstance.line(pos.x, pos.y, n.pos.x, n.pos.y);
            n.Show(ProcessingInstance);
        }
    }
}
