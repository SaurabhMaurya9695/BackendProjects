package com.backend.xjc;

import com.sun.codemodel.*;
import com.sun.tools.xjc.Plugin;
import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.outline.ClassOutline;
import com.sun.tools.xjc.outline.Outline;
import org.xml.sax.ErrorHandler;

import java.io.Serializable;
import java.util.*;

/**
 * Custom XJC Plugin that augments generated classes with:
 * - Getters / Fluent setters
 * - Deep copy constructor
 * - toString()
 * - Builder pattern
 * - equals/hashCode
 * - Withers (withX methods)
 * - Serializable
 *
 * Enable with: -Xrichmodel
 */
public class RichModelPlugin extends Plugin {

    @Override
    public String getOptionName() {
        return "Xrichmodel";
    }

    @Override
    public String getUsage() {
        return "  -Xrichmodel : add getters/setters, copy ctor, toString, builder, equals/hashCode, withers, Serializable";
    }

    @Override
    public boolean run(Outline outline, Options opt, ErrorHandler errorHandler) {
        for (ClassOutline co : outline.getClasses()) {
            JDefinedClass impl = co.implClass;
            Map<String, JFieldVar> fields = impl.fields();

            addGettersAndSetters(impl, fields);
            addToString(impl, fields);
            addDeepCopyConstructor(impl, fields);
            addBuilder(impl, fields);
            addEqualsAndHashCode(impl, fields);
            addWithers(impl, fields);
            makeSerializable(impl);
        }
        return true;
    }

    private void addGettersAndSetters(JDefinedClass impl, Map<String, JFieldVar> fields) {
        for (Map.Entry<String, JFieldVar> e : fields.entrySet()) {
            String name = e.getKey();
            JFieldVar f = e.getValue();
            JType type = f.type();
            String cap = capitalize(name);

            // Getter
            String getterName = (isBoolean(type) ? "is" : "get") + cap;
            if (!hasMethod(impl, getterName)) {
                JMethod getter = impl.method(JMod.PUBLIC, type, getterName);
                getter.body()._return(JExpr._this().ref(f));
            }

            // Fluent Setter
            String setterName = "set" + cap;
            if (!hasMethod(impl, setterName, type)) {
                JMethod setter = impl.method(JMod.PUBLIC, impl, setterName);
                JVar param = setter.param(type, name);
                setter.body().assign(JExpr._this().ref(f), param);
                setter.body()._return(JExpr._this());
            }
        }
    }

    private void addToString(JDefinedClass impl, Map<String, JFieldVar> fields) {
        if (hasMethod(impl, "toString")) return;

        JCodeModel cm = impl.owner();
        JMethod m = impl.method(JMod.PUBLIC, String.class, "toString");
        m.annotate(Override.class);
        JBlock body = m.body();

        JClass sbClass = cm.ref(StringBuilder.class);
        JVar sb = body.decl(sbClass, "sb", JExpr._new(sbClass));
        body.add(sb.invoke("append").arg(impl.name() + "{"));

        int i = 0, size = fields.size();
        for (Map.Entry<String, JFieldVar> e : fields.entrySet()) {
            String name = e.getKey();
            body.add(sb.invoke("append").arg(name + "="));
            body.add(sb.invoke("append").arg(JExpr._this().ref(e.getValue())));
            if (++i < size) {
                body.add(sb.invoke("append").arg(", "));
            }
        }
        body.add(sb.invoke("append").arg("}"));
        body._return(sb.invoke("toString"));
    }

    private void addDeepCopyConstructor(JDefinedClass impl, Map<String, JFieldVar> fields) {
        if (hasConstructorWithSelfParam(impl)) return;

        JMethod ctor = impl.constructor(JMod.PUBLIC);
        JVar other = ctor.param(impl, "other");
        JBlock body = ctor.body();

        for (Map.Entry<String, JFieldVar> e : fields.entrySet()) {
            JFieldVar f = e.getValue();
            JType type = f.type();
            JExpression rhs;

            if (isList(type)) {
                JCodeModel cm = impl.owner();
                JClass listImpl = cm.ref(ArrayList.class).narrow(getListArg(type));
                rhs = JOp.cond(other.ref(f).ne(JExpr._null()),
                        JExpr._new(listImpl).arg(other.ref(f)),
                        JExpr._null());
            } else if (isArray(type)) {
                JCodeModel cm = impl.owner();
                JClass arrays = cm.ref(Arrays.class);
                rhs = JOp.cond(other.ref(f).ne(JExpr._null()),
                        arrays.staticInvoke("copyOf").arg(other.ref(f)).arg(other.ref(f).ref("length")),
                        JExpr._null());
            } else {
                rhs = other.ref(f);
            }

            body.assign(JExpr._this().ref(f), rhs);
        }
    }

    private void addBuilder(JDefinedClass impl, Map<String, JFieldVar> fields) {
        for (Iterator<JDefinedClass> it = impl.classes(); it.hasNext(); ) {
            if (it.next().name().equals("Builder")) return;
        }

        try {
            JDefinedClass builder = impl._class(JMod.PUBLIC | JMod.STATIC, "Builder");

            // builder fields + methods
            for (Map.Entry<String, JFieldVar> e : fields.entrySet()) {
                String name = e.getKey();
                JType type = e.getValue().type();
                builder.field(JMod.PRIVATE, type, name);

                JMethod with = builder.method(JMod.PUBLIC, builder, name);
                JVar param = with.param(type, name);
                with.body().assign(JExpr._this().ref(name), param);
                with.body()._return(JExpr._this());
            }

            // build()
            JMethod build = builder.method(JMod.PUBLIC, impl, "build");
            JVar inst = build.body().decl(impl, "inst", JExpr._new(impl));
            for (Map.Entry<String, JFieldVar> e : fields.entrySet()) {
                build.body().assign(inst.ref(e.getKey()), JExpr._this().ref(e.getKey()));
            }
            build.body()._return(inst);

            // static builder()
            JMethod factory = impl.method(JMod.PUBLIC | JMod.STATIC, builder, "builder");
            factory.body()._return(JExpr._new(builder));

        } catch (JClassAlreadyExistsException ignored) {
        }
    }

    private void addEqualsAndHashCode(JDefinedClass impl, Map<String, JFieldVar> fields) {
        JCodeModel cm = impl.owner();

        if (!hasMethod(impl, "equals", cm.ref(Object.class))) {
            JMethod eq = impl.method(JMod.PUBLIC, cm.BOOLEAN, "equals");
            eq.annotate(Override.class);
            JVar o = eq.param(Object.class, "o");
            JBlock body = eq.body();

            body._if(o.eq(JExpr._this()))._then()._return(JExpr.TRUE);
            body._if(o.eq(JExpr._null()).cor(o.invoke("getClass").ne(JExpr._this().invoke("getClass"))))._then()._return(JExpr.FALSE);

            JVar other = body.decl(impl, "other", JExpr.cast(impl, o));
            JClass objects = cm.ref(Objects.class);

            for (JFieldVar f : fields.values()) {
                body._if(objects.staticInvoke("equals").arg(JExpr._this().ref(f)).arg(other.ref(f)).not())._then()._return(JExpr.FALSE);
            }
            body._return(JExpr.TRUE);
        }

        if (!hasMethod(impl, "hashCode")) {
            JMethod hash = impl.method(JMod.PUBLIC, cm.INT, "hashCode");
            hash.annotate(Override.class);
            JBlock body = hash.body();
            JClass objects = cm.ref(Objects.class);
            JInvocation inv = objects.staticInvoke("hash");
            for (JFieldVar f : fields.values()) {
                inv.arg(JExpr._this().ref(f));
            }
            body._return(inv);
        }
    }

    private void addWithers(JDefinedClass impl, Map<String, JFieldVar> fields) {
        for (Map.Entry<String, JFieldVar> e : fields.entrySet()) {
            String name = e.getKey();
            JType type = e.getValue().type();
            String cap = capitalize(name);

            if (!hasMethod(impl, "with" + cap, type)) {
                JMethod with = impl.method(JMod.PUBLIC, impl, "with" + cap);
                JVar param = with.param(type, "newValue");
                with.body().assign(JExpr._this().ref(name), param);
                with.body()._return(JExpr._this());
            }
        }
    }

    private void makeSerializable(JDefinedClass impl) {
        JCodeModel cm = impl.owner();
        impl._implements(cm.ref(Serializable.class));
        impl.field(JMod.PRIVATE | JMod.STATIC | JMod.FINAL, cm.LONG, "serialVersionUID", JExpr.lit(1L));
    }

    // ---------- helpers ----------

    private boolean hasMethod(JDefinedClass impl, String name, JType... params) {
        for (JMethod m : impl.methods()) {
            if (m.name().equals(name) && m.listParamTypes().length == params.length) {
                boolean same = true;
                JType[] mp = m.listParamTypes();
                for (int i = 0; i < params.length; i++) {
                    if (!mp[i].fullName().equals(params[i].fullName())) {
                        same = false;
                        break;
                    }
                }
                if (same) return true;
            }
        }
        return false;
    }

    private boolean hasConstructorWithSelfParam(JDefinedClass impl) {
        for (Iterator<JMethod> it = impl.constructors(); it.hasNext(); ) {
            JMethod c = it.next();
            if (c.listParamTypes().length == 1 &&
                    c.listParamTypes()[0].fullName().equals(impl.fullName())) {
                return true;
            }
        }
        return false;
    }

    private boolean isBoolean(JType t) {
        return t.fullName().equals("boolean") || t.fullName().equals(Boolean.class.getName());
    }

    private boolean isList(JType t) {
        if (!(t instanceof JClass)) return false;
        for (JClass c = (JClass) t; c != null; c = c._extends()) {
            if (c.fullName().equals(List.class.getName())) return true;
        }
        return false;
    }

    private JClass getListArg(JType t) {
        if (!(t instanceof JClass)) return null;
        JClass jc = (JClass) t;
        if (jc.getTypeParameters().size() == 1) return jc.getTypeParameters().get(0);
        return jc.owner().ref(Object.class);
    }

    private boolean isArray(JType t) {
        return t.isArray();
    }

    private String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        if (s.length() == 1) return s.toUpperCase();
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }
}
