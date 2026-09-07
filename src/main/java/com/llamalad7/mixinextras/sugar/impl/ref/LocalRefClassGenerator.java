package com.llamalad7.mixinextras.sugar.impl.ref;

import com.llamalad7.mixinextras.lib.apache.commons.StringUtils;
import com.llamalad7.mixinextras.service.MixinExtrasService;
import com.llamalad7.mixinextras.service.MixinExtrasVersion;
import com.llamalad7.mixinextras.sugar.impl.ref.generated.GeneratedImplDummy;
import com.llamalad7.mixinextras.utils.ClassGenUtils;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.InstructionAdapter;
import org.objectweb.asm.tree.ClassNode;

/* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:META-INF/jarjar/mixinextras-forge-0.4.1.jar:META-INF/jars/MixinExtras-0.4.1.jar:com/llamalad7/mixinextras/sugar/impl/ref/LocalRefClassGenerator.class */
public class LocalRefClassGenerator {
    private static final String IMPL_PACKAGE = StringUtils.substringBeforeLast(LocalRefClassGenerator.class.getName(), ".").replace('.', '/') + "/generated";
    private static final Map<Class<?>, String> interfaceToImpl = new HashMap();

    public static String getForType(Type type) {
        Class<?> refInterface = LocalRefUtils.getInterfaceFor(type);
        String owner = interfaceToImpl.get(refInterface);
        if (owner != null) {
            return owner;
        }
        String owner2 = IMPL_PACKAGE + '/' + StringUtils.substringAfterLast(refInterface.getName(), ".") + "Impl";
        String desc = type.getDescriptor();
        String innerDesc = desc.length() == 1 ? desc : Type.getDescriptor(Object.class);
        interfaceToImpl.put(refInterface, owner2);
        ClassNode node = new ClassNode();
        node.visit(52, 49, owner2, (String) null, Type.getInternalName(Object.class), (String[]) null);
        generateClass(node, owner2, innerDesc, refInterface.getName());
        ClassGenUtils.defineClass(node, GeneratedImplDummy.getLookup());
        return owner2;
    }

    private static void generateClass(ClassNode node, String owner, String innerDesc, String interfaceName) {
        Type objectType = Type.getType(Object.class);
        Type innerType = Type.getType(innerDesc);
        for (String name : MixinExtrasService.getInstance().getAllClassNamesAtLeast(interfaceName, MixinExtrasVersion.V0_2_0_BETA_5)) {
            node.interfaces.add(name.replace('.', '/'));
        }
        node.visitField(2, "value", innerDesc, (String) null, (Object) null);
        node.visitField(2, "state", "B", (String) null, (Object) null);
        Consumer<InstructionAdapter> checkState = code -> {
            String runtime = Type.getInternalName(LocalRefRuntime.class);
            code.load(0, objectType);
            code.getfield(owner, "state", "B");
            Label passed = new Label();
            code.ifeq(passed);
            code.load(0, objectType);
            code.getfield(owner, "state", "B");
            code.invokestatic(runtime, "checkState", "(B)V", false);
            code.mark(passed);
        };
        genMethod(node, "<init>", "()V", code2 -> {
            code2.load(0, objectType);
            code2.invokespecial(objectType.getInternalName(), "<init>", "()V", false);
            code2.load(0, objectType);
            code2.iconst(1);
            code2.putfield(owner, "state", "B");
            code2.areturn(Type.VOID_TYPE);
        });
        genMethod(node, "get", "()" + innerDesc, code3 -> {
            checkState.accept(code3);
            code3.load(0, objectType);
            code3.getfield(owner, "value", innerDesc);
            code3.areturn(innerType);
        });
        genMethod(node, "set", "(" + innerDesc + ")V", code4 -> {
            checkState.accept(code4);
            code4.load(0, objectType);
            code4.load(1, innerType);
            code4.putfield(owner, "value", innerDesc);
            code4.areturn(Type.VOID_TYPE);
        });
        genMethod(node, "init", "(" + innerDesc + ")V", code5 -> {
            code5.load(0, objectType);
            code5.load(1, innerType);
            code5.putfield(owner, "value", innerDesc);
            code5.load(0, objectType);
            code5.iconst(0);
            code5.putfield(owner, "state", "B");
            code5.areturn(Type.VOID_TYPE);
        });
        genMethod(node, "dispose", "()" + innerDesc, code6 -> {
            checkState.accept(code6);
            code6.load(0, objectType);
            code6.iconst(2);
            code6.putfield(owner, "state", "B");
            code6.load(0, objectType);
            code6.getfield(owner, "value", innerDesc);
            code6.areturn(innerType);
        });
    }

    private static void genMethod(ClassVisitor cv, String name, String desc, Consumer<InstructionAdapter> code) {
        code.accept(new InstructionAdapter(cv.visitMethod(1, name, desc, (String) null, (String[]) null)));
    }
}
