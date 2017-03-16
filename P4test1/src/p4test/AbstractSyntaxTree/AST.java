package p4test.AbstractSyntaxTree;

import p4test.Token;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mysjkin on 3/4/17.
 */
public class AST
{
    private String value;
    public String GetValue(){
        return value;
    }
    public void SetValue(String val){
        value = val;
    }

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

    // TODO Remove only a test
    public ArrayList<AST> children = new ArrayList<AST>();

    private AST parent;
    public AST GetParent(){
        return parent;
    }
    public void SetParent(AST par){
        parent = par;
    }

    public AST MakeSiblings(AST newSib) {
        AST theseSiblings = this;
        AST newSiblings = newSib;

        /* Find the head of list of siblings in this */
        while (theseSiblings != null) {
            theseSiblings = theseSiblings.GetNextRightSibling();
        }
        /* Join the list of siblings */
        theseSiblings.SetNextRightSibling(newSiblings);

        /* Set pointers for the new siblings */
        newSiblings.SetLeftMostSibling(theseSiblings.GetLeftMostSibling());
        newSiblings.SetParent(theseSiblings.GetParent());

        while (newSiblings.GetNextRightSibling() != null) {
            newSiblings = newSiblings.GetNextRightSibling();
            newSiblings.SetLeftMostSibling(theseSiblings.GetLeftMostSibling());
            newSiblings.SetParent(theseSiblings.GetParent());
        }
        return newSiblings;
    }

    public int CountChildren(){
        int numChildren = 0;
        AST currentChild = GetLeftMostChild();
        while (currentChild != null){
            numChildren++;
            currentChild = currentChild.GetNextRightSibling();
        }

        return numChildren;
    }

    public AST AdoptChildren(AST toAdopt) {
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
            return newSiblings;
        }
        return null;
    }

    /* First node is parent, all others are chained as
    *  sibling children to parent. */
    public AST MakeFamily(AST p, AST... kids) {
        AST rightmost;
        kids[0].SetParent(p);
        p.SetLeftMostChild(kids[0]);
        rightmost = kids[0];
        for (int i = 1; i < kids.length; i++) {
            rightmost.SetNextRightSibling(kids[i]);
            rightmost = kids[i];
        }
        return p;
    }
}
