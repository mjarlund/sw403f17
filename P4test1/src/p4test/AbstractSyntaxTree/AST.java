package p4test.AbstractSyntaxTree;

import p4test.Token;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mysjkin on 3/4/17.
 */
public class AST
{
    private AST nextRightSibling;
    public AST GetNextRightSibling(){
        return nextRightSibling;
    }
    public void SetNextRightSibling(AST sib){
        nextRightSibling = sib;
    }

    private AST leftMostSibling;
    public AST GetLeftMostSibling(){
        return leftMostSibling;
    }
    public void SetLeftMostSibling(AST sib){
        leftMostSibling = sib;
    }

    private AST leftMostChild;
    public AST GetLeftMostChild(){
        return leftMostChild;
    }
    public void SetLeftMostChild(AST child){
        leftMostChild = child;
    }

    private AST parent;
    public AST GetParent(){
        return parent;
    }
    public void SetParent(AST par){
        parent = par;
    }

    public AST MakeSiblings(AST newSib){
        AST theseSiblings = this;
        AST newSiblings = newSib;

        /* Find the head of list of siblings in this */
        while (theseSiblings != null){
            theseSiblings = theseSiblings.GetNextRightSibling();
        }
        /* Join the list of siblings */
        theseSiblings.SetNextRightSibling(newSiblings);

        /* Set pointers for the new siblings */
        newSiblings.SetLeftMostSibling(theseSiblings.GetLeftMostSibling);
        newSiblings.SetParent(theseSiblings.GetParent());

        while (newSiblings.GetNextRightSibling() != null){
            newSiblings = newSiblings.GetNextRightSibling();
            newSiblings.SetLeftMostSibling(theseSiblings.GetLeftMostSibling());
            newSiblings.SetParent(theseSiblings.GetParent());
        }
        return newSiblings;
    }

    public AST AdoptChildren(AST toAdopt){
        AST newSiblings;

        if (GetLeftMostChild() != null)
            GetLeftMostChild().MakeSiblings(toAdopt);
        else {
            newSiblings = toAdopt.GetLeftMostSibling();
            SetLeftMostChild(newSiblings);
            while (newSiblings != null){
                newSiblings.SetParent(this);
                newSiblings = newSiblings.GetNextRightSibling();
            }
        }
        return newSiblings;
    }

    public AST MakeFamily(AST p, AST... kids){
        /* Do magic */
    }
}
