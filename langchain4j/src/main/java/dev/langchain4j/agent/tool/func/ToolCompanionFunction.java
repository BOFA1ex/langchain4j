package dev.langchain4j.agent.tool.func;

import dev.langchain4j.agent.tool.ToolExecutor;
import dev.langchain4j.agent.tool.lambda.LambdaMeta;
import dev.langchain4j.agent.tool.lambda.ToolSerializedCompanionFunction;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.function.Function;

@FunctionalInterface
public interface ToolCompanionFunction<T, R> extends Function<T, R>, ToolSerializedCompanionFunction {

    default ToolExecutor delegate() {
        final @NotNull LambdaMeta meta = LambdaMeta.extract(this);
        final Class<?> implClass = meta.getImplClass();
        final @NotNull Method implMethod = meta.getImplMethod();

        return new ToolExecutor(implClass, implMethod) {
            @Override
            public Object execute(Object[] args) {
                return apply(sneakyCast(args[0]));
            }
        };
    }
}