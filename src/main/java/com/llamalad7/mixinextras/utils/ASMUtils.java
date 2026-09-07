package com.llamalad7.mixinextras.utils;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.spongepowered.asm.mixin.injection.struct.Target;

/* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:META-INF/jarjar/mixinextras-forge-0.4.1.jar:META-INF/jars/MixinExtras-0.4.1.jar:com/llamalad7/mixinextras/utils/ASMUtils.class */
public class ASMUtils {
    public static String annotationToString(AnnotationNode annotation) {
        StringBuilder builder = new StringBuilder("@").append(typeToString(Type.getType(annotation.desc)));
        List<Object> values = annotation.values;
        if (values == null || values.isEmpty()) {
            return builder.toString();
        }
        builder.append('(');
        for (int i = 0; i < values.size(); i += 2) {
            if (i != 0) {
                builder.append(", ");
            }
            String name = (String) values.get(i);
            Object value = values.get(i + 1);
            builder.append(name).append(" = ").append(valueToString(value));
        }
        builder.append(')');
        return builder.toString();
    }

    public static String typeToString(Type type) {
        String name = type.getClassName();
        return name.substring(name.lastIndexOf(46) + 1).replace('$', '.');
    }

    private static String valueToString(Object value) {
        if (value instanceof String) {
            return '\"' + value.toString() + '\"';
        }
        if (value instanceof Type) {
            Type type = (Type) value;
            return typeToString(type) + ".class";
        }
        if (value instanceof String[]) {
            String[] enumInfo = (String[]) value;
            return typeToString(Type.getType(enumInfo[0])) + '.' + enumInfo[1];
        }
        if (value instanceof AnnotationNode) {
            return annotationToString((AnnotationNode) value);
        }
        if (value instanceof List) {
            List<?> list = (List) value;
            if (list.size() == 1) {
                return valueToString(list.get(0));
            }
            return '{' + ((String) list.stream().map(ASMUtils::valueToString).collect(Collectors.joining(", "))) + '}';
        }
        return value.toString();
    }

    public static boolean isPrimitive(Type type) {
        return type.getDescriptor().length() == 1;
    }

    public static MethodInsnNode getInvokeInstruction(ClassNode owner, MethodNode method) {
        int i;
        boolean isInterface = (owner.access & 512) != 0;
        if ((method.access & 8) != 0) {
            i = 184;
        } else if ((method.access & 2) != 0) {
            i = 183;
        } else {
            i = isInterface ? 185 : 182;
        }
        int opcode = i;
        return new MethodInsnNode(opcode, owner.name, method.name, method.desc, isInterface);
    }

    public static int getDummyOpcodeForType(Type type) {
        switch (type.getSort()) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                return 3;
            case 6:
                return 11;
            case 7:
                return 9;
            case 8:
                return 14;
            case 9:
            case 10:
                return 1;
            default:
                throw new UnsupportedOperationException();
        }
    }

    public static MethodInsnNode findInitNodeFor(Target target, TypeInsnNode newNode) {
        int start = target.indexOf(newNode);
        int depth = 0;
        Iterator<AbstractInsnNode> it = target.insns.iterator(start);
        while (it.hasNext()) {
            AbstractInsnNode next = it.next();
            if ((next instanceof TypeInsnNode) && next.getOpcode() == 187) {
                TypeInsnNode typeNode = (TypeInsnNode) next;
                if (typeNode.desc.equals(newNode.desc)) {
                    depth++;
                }
            } else if ((next instanceof MethodInsnNode) && next.getOpcode() == 183) {
                MethodInsnNode methodNode = (MethodInsnNode) next;
                if ("<init>".equals(methodNode.name) && methodNode.owner.equals(newNode.desc)) {
                    depth--;
                    if (depth == 0) {
                        return methodNode;
                    }
                }
            }
        }
        return null;
    }
}
