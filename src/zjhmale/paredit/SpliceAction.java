package zjhmale.paredit;/*
 * Copyright 2009 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;

/**
 * An action to splice s-expressions imnto their parents.
 *
 * @author <a href="mailto:ianp@ianp.org">Ian Phillips</a>
 */
public class SpliceAction extends EditorAction {

    public SpliceAction() {
        super(new SpliceActionHandler());
    }

    private static class SpliceActionHandler extends AbstractSexpActionHandler {
        protected SpliceActionHandler() {
            super();
        }

        @Override
        protected void executeWriteAction(PsiElement sexp, Editor editor, Project project, DataContext dataContext) {
            System.out.println("caret element -> " + sexp.getText());
            PsiElement first = sexp.getFirstChild();
            PsiElement last = sexp.getLastChild();

            if (first.getText().equals("{") && last.getText().equals("}")) {
                first.delete();
                last.delete();
            }
            if (first.getText().equals("[") && last.getText().equals("]")) {
                first.delete();
                last.delete();
            }
            if (first.getText().equals("(") && last.getText().equals(")")) {
                first.delete();
                last.delete();
            }

            /*PsiElement parent = sexp.getParent();
            if (parent == null) {
                return;
            }

            //PsiElement[] children = sexp.getChildren();

            // if the s-exp is not the first in it's parent, e.g. (a (b c)) -> (a b c)
            PsiElement previous = PsiTreeUtil.getPrevSiblingOfType(sexp, ClojurePsiElement.class);
            if (previous != null) {
                for (PsiElement child : children) {
                    previous = parent.addAfter(child, previous);
                }
                sexp.delete();
                return;
            }

            // if the s-exp is not the last in it's parent, e.g. ((a b) c) -> (a b c)
            PsiElement next = PsiTreeUtil.getNextSiblingOfType(sexp, ClojurePsiElement.class);
            if (next != null) {
                for (int i = children.length; i != 0; ) {
                    next = parent.addBefore(children[--i], next);
                }
                sexp.delete();
                return;
            }

            // the corner case of nested s-exps, e.g. ((a b c)) -> (a b c)
            for (PsiElement child : children) {
                parent.addBefore(child, parent.getLastChild());
            }
            sexp.delete();*/
        }
    }

}
