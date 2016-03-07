package zjhmale.paredit;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.PsiWhiteSpaceImpl;
import com.intellij.psi.util.PsiTreeUtil;

public final class SlurpForwardsAction extends EditorAction {

    public SlurpForwardsAction() {
        super(new SlurpForwardsActionHandler());
    }

    public static PsiElement lastChildSexp(PsiElement element) {
        PsiElement[] children = element.getChildren();
        return children.length != 0 ? children[children.length - 1] : null;
    }

    private static class SlurpForwardsActionHandler extends AbstractSexpActionHandler {
        protected SlurpForwardsActionHandler() {
            super();
        }

        @Override
        protected void executeWriteAction(PsiElement sexp, Editor editor, Project project, DataContext dataContext) {
            System.out.println("current caret -> " + sexp.getText());

            PsiElement slurpee = PsiTreeUtil.getNextSiblingOfType(sexp, PsiElement.class);
            while (slurpee instanceof PsiWhiteSpaceImpl) {
                slurpee = PsiTreeUtil.getNextSiblingOfType(slurpee, PsiElement.class);
            }
            if (slurpee == null) {
                return;
            }

            System.out.println("slurpee -> " + slurpee.getText());
            System.out.println("slurpee class -> " + slurpee.getClass());
            System.out.println("last child -> " + sexp.getLastChild().getText());
            System.out.println("last child class -> " + sexp.getLastChild().getClass());

            System.out.println("slurpee is valid " + slurpee.isValid());
            System.out.println("slurpee is writable " + slurpee.isWritable());
            System.out.println("last child is valid " + sexp.getLastChild().isValid());
            System.out.println("last child is wriable " + sexp.getLastChild().isWritable());

            //sexp.getLastChild().delete();
            //slurpee.delete();
            //((LeafPsiElement) slurpee).delete();
            PsiElement copy = slurpee.copy();
            slurpee.delete();
            PsiElement lastChild = lastChildSexp(sexp);
            System.out.println("copy -> " + copy.getText());
            if (lastChild != null) {
                System.out.println("last child -> " + lastChild.getText());
                sexp.addAfter(copy, lastChild);
            } else {
                System.out.println("do not have last child");
                sexp.addBefore(copy, sexp.getLastChild());
            }
        }
    }

}
