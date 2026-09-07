package com.llamalad7.mixinextras.sugar.impl.handlers;

import com.llamalad7.mixinextras.lib.apache.commons.tuple.Pair;
import com.llamalad7.mixinextras.service.MixinExtrasService;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.impl.SugarParameter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.objectweb.asm.tree.MethodNode;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

/* JADX INFO: loaded from: arcane_convergence-1.0.0bugfix-all.jar:META-INF/jarjar/mixinextras-forge-0.4.1.jar:META-INF/jars/MixinExtras-0.4.1.jar:com/llamalad7/mixinextras/sugar/impl/handlers/HandlerTransformer.class */
public abstract class HandlerTransformer {
    private static final Map<String, Class<? extends HandlerTransformer>> MAP = new HashMap();
    protected final IMixinInfo mixin;
    protected final SugarParameter parameter;

    public abstract boolean isRequired(MethodNode methodNode);

    public abstract void transform(HandlerInfo handlerInfo);

    static {
        List<Pair<Class<? extends Annotation>, Class<? extends HandlerTransformer>>> sugars = Arrays.asList(Pair.of(Local.class, LocalHandlerTransformer.class));
        for (Pair<Class<? extends Annotation>, Class<? extends HandlerTransformer>> pair : sugars) {
            for (String name : MixinExtrasService.getInstance().getAllClassNames(pair.getLeft().getName())) {
                MAP.put('L' + name.replace('.', '/') + ';', pair.getRight());
            }
        }
    }

    HandlerTransformer(IMixinInfo mixin, SugarParameter parameter) {
        this.mixin = mixin;
        this.parameter = parameter;
    }

    public static HandlerTransformer create(IMixinInfo mixin, SugarParameter parameter) {
        try {
            Class<? extends HandlerTransformer> clazz = MAP.get(parameter.sugar.desc);
            if (clazz == null) {
                return null;
            }
            Constructor<? extends HandlerTransformer> ctor = clazz.getDeclaredConstructor(IMixinInfo.class, SugarParameter.class);
            return ctor.newInstance(mixin, parameter);
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
