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

            while (slurpee != null && ((slurpee instanceof PsiWhiteSpaceImpl) || slurpee.getText().equals(" "))) {
                slurpee = PsiTreeUtil.getNextSiblingOfType(slurpee, PsiElement.class);
            }

            if (slurpee == null) {
                return;
            }

            PsiElement copy = slurpee.copy();
            slurpee.delete();
            PsiElement lastChild = lastChildSexp(sexp);
            if (lastChild != null) {
                sexp.addAfter(copy, lastChild);
            } else {
                sexp.addBefore(copy, sexp.getLastChild());
            }
        }
    }
}