package com.llamalad7.mixinextras.utils;

import com.llamalad7.mixinextras.lib.apache.commons.StringUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.objectweb.asm.Type;
import org.objectweb.asm.signature.SignatureReader;
import org.objectweb.asm.signature.SignatureVisitor;
import org.spongepowered.asm.util.asm.ASM;

/* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:META-INF/jarjar/mixinextras-forge-0.4.1.jar:META-INF/jars/MixinExtras-0.4.1.jar:com/llamalad7/mixinextras/utils/GenericParamParser.class */
public class GenericParamParser extends SignatureVisitor {
    private final List<Type> results;

    private GenericParamParser() {
        super(ASM.API_VERSION);
        this.results = new ArrayList();
    }

    public static List<Type> getParameterGenerics(String desc, String signature) {
        if (signature == null || signature.isEmpty()) {
            return Collections.nCopies(Type.getArgumentTypes(desc).length, null);
        }
        GenericParamParser parser = new GenericParamParser();
        new SignatureReader(signature).accept(parser);
        return parser.results;
    }

    public SignatureVisitor visitParameterType() {
        final int index = this.results.size();
        this.results.add(null);
        return new SignatureVisitor(this.api) { // from class: com.llamalad7.mixinextras.utils.GenericParamParser.1
            public SignatureVisitor visitTypeArgument(char wildcard) {
                if (wildcard != '=') {
                    return this;
                }
                return new SignatureVisitor(this.api) { // from class: com.llamalad7.mixinextras.utils.GenericParamParser.1.1
                    private int depth;
                    private int arrayDimensions;
                    private String internalName;

                    public SignatureVisitor visitArrayType() {
                        if (this.depth == 0) {
                            this.arrayDimensions++;
                        }
                        return this;
                    }

                    public void visitBaseType(char descriptor) {
                        if (this.depth == 0) {
                            GenericParamParser.this.results.set(index, Type.getType(StringUtils.repeat('[', this.arrayDimensions) + descriptor));
                        }
                    }

                    public void visitClassType(String name) {
                        int i = this.depth + 1;
                        this.depth = i;
                        if (i == 1) {
                            this.internalName = name;
                        }
                    }

                    public void visitInnerClassType(String name) {
                        if (this.depth == 1) {
                            this.internalName += '$' + name;
                        }
                    }

                    public void visitEnd() {
                        this.depth--;
                        String prefix = StringUtils.repeat('[', this.arrayDimensions);
                        GenericParamParser.this.results.set(index, Type.getType(prefix + Type.getObjectType(this.internalName).getDescriptor()));
                    }
                };
            }
        };
    }
}
