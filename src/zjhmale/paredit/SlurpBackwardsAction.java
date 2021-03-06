package zjhmale.paredit;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.PsiWhiteSpaceImpl;
import com.intellij.psi.util.PsiTreeUtil;

public final class SlurpBackwardsAction extends EditorAction {

    public SlurpBackwardsAction() {
        super(new SlurpBackwardsActionHandler());
    }

    private static class SlurpBackwardsActionHandler extends AbstractSexpActionHandler {
        protected SlurpBackwardsActionHandler() {
            super();
        }

        @Override
        protected void executeWriteAction(PsiElement sexp, Editor editor, Project project, DataContext dataContext) {
            PsiElement slurpee = PsiTreeUtil.getPrevSiblingOfType(sexp, PsiElement.class);
            while (slurpee instanceof PsiWhiteSpaceImpl) {
                slurpee = PsiTreeUtil.getPrevSiblingOfType(slurpee, PsiElement.class);
            }
            if (slurpee == null) {
                return;
            }

            PsiElement copy = slurpee.copy();
            slurpee.delete();
            PsiElement firstChild = SexpUtils.firstChildSexp(sexp);
            if (firstChild != null) {
                sexp.addBefore(copy, firstChild);
            } else {
                sexp.addAfter(copy, sexp.getFirstChild());
            }
        }
    }
}
