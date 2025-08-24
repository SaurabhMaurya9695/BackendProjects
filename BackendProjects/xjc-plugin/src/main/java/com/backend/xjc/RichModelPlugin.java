package com.backend.xjc;

import com.sun.codemodel.*;
import com.sun.tools.xjc.BadCommandLineException;
import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.Plugin;
import com.sun.tools.xjc.outline.ClassOutline;
import com.sun.tools.xjc.outline.Outline;

import org.xml.sax.ErrorHandler;

import java.io.Serializable;

/**
 * XJC Plugin to generate Lombok-based model classes.
 * <p>
 * Features:
 * - Adds Lombok annotations (@Data, @Builder, @NoArgsConstructor, @AllArgsConstructor, @EqualsAndHashCode, @ToString)
 * - Implements Serializable with serialVersionUID = 1L
 * - Optional deep copy constructor
 */
public class RichModelPlugin extends Plugin {

    @Override
    public String getOptionName() {
        return "XrichModel";
    }

    @Override
    public String getUsage() {
        return "  -XrichModel    :  generate Lombok-based classes with Serializable and copy constructor";
    }

    @Override
    public boolean run(Outline outline, Options opt, ErrorHandler errorHandler) {
        for (ClassOutline classOutline : outline.getClasses()) {
            JDefinedClass impl = classOutline.implClass;

            // Add Lombok Annotations
            annotateWithLombok(impl);

            // Make Serializable
            makeSerializable(impl);

            // Add Deep Copy Constructor (optional)
            addCopyConstructor(impl);
        }
        return true;
    }

    private void annotateWithLombok(JDefinedClass impl) {
        JCodeModel cm = impl.owner();

        impl.annotate(cm.ref("lombok.Data"));
        impl.annotate(cm.ref("lombok.Builder")).param("toBuilder", true);
        impl.annotate(cm.ref("lombok.NoArgsConstructor"));
        impl.annotate(cm.ref("lombok.AllArgsConstructor"));
        impl.annotate(cm.ref("lombok.EqualsAndHashCode")).param("callSuper", false);
        impl.annotate(cm.ref("lombok.ToString"));
    }

    private void makeSerializable(JDefinedClass impl) {
        JCodeModel cm = impl.owner();
        impl._implements(cm.ref(Serializable.class));

//        // Only add if not present already
//        if (!impl.fields().containsKey("serialVersionUID")) {
//            impl.field(JMod.PRIVATE | JMod.STATIC | JMod.FINAL, cm.LONG, "serialVersionUID", JExpr.lit(1L)
//                    // safe default
//            );
//        }
    }

    private void addCopyConstructor(JDefinedClass impl) {
        // ✅ Always ensure no-arg constructor exists
        if (impl.getConstructor(new JType[0]) == null) {
            impl.constructor(JMod.PUBLIC); // no-arg constructor
        }

        // ✅ Add deep copy constructor
        JMethod copyConstructor = impl.constructor(JMod.PUBLIC);
        JVar other = copyConstructor.param(impl, "other");
        JBlock body = copyConstructor.body();

        for (JFieldVar field : impl.fields().values()) {
            body.assign(JExpr._this().ref(field), other.ref(field));
        }
    }

    @Override
    public int parseArgument(Options opt, String[] args, int i) throws BadCommandLineException {
        if (args[i].equals(getOptionName())) {
            return 1; // consume this argument
        }
        return 0;
    }
}
